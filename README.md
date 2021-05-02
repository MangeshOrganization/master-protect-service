# master-protect-service

## Table of Contents
* [Overview](#Overview)
* [Libraries /Services / Frameworks](#Libraries)
* [Overall Flow](#Design)
* [Setup](#setup)
* [See It in Action](#action)
* [Future Considerations](#future)
* [Issues Encountered](#issues)
* [References](References)



## <a name="Overview"/> Overview

master-protect-service is a Spring Boot Web service that listens for [organization events] (https://developer.github.com/webhooks/#events) to know when a repository has been created. 
When the repository is created it applies configurable protection rules on the master branch. 
Once the Protection Rules are created, It notifies Repo Owner with an @mention in an issue within the repository that outlines the protections that were added.
This Service Runs in AWS ECS Fargate Container Platform inside docker container.

## <a name="Libraries"/> Libraries /Services / Frameworks
* Spring Boot 2.3.10.RELEASE - Base Framework
* Spring Cloud Hoxton.SR11 - External Configuration
* Java 11 - Runtime
* Docker - Container Build
* GitHub Actions - Basic CI/CD 
* AWS ECS - Container Hosting

## <a name="Design"/> Overall Flow

![image](https://user-images.githubusercontent.com/2278604/116823471-07f15f00-abc8-11eb-98b6-8a4db2b44c9a.png)

### Components
1. Master Protect Service 
 A Spring Boot Container Running in AWS ECS.
2. Config Server
 A Spring Cloud Config Server (Embedded). This is integrated with Config Server Git Repo for Application Configuration.
3. GitHub WebHook / APIs

### Brief Working
* GitHub Org Webhook is Configured to send the Event Payload for Any Actions performed on the Org Repositories.
* The Master Protect Service Listens on to those Events and Handles the Events of type "created"
* On Receipt of the Repository Creation Events , Master Protect Service Uses [Branch Protection GitHub API](https://docs.github.com/en/rest/reference/repos) to Apply the Branch Protection . The Rules Applied are build by fetching the configuration from Config Server.
* Upon Successful Creation of the Branch Protection , The Master Protect Service then Utilizes [GitHub Issues API](https://docs.github.com/en/rest/reference/issues) to Create the Issue mentioning the Repository Owner with JSON representing the Branch Protection Rules Applied on the Repository.


## <a name="setup"/> Setup

## Pre-requisites
You will need to have following  Resources handy before trying it out.
 * GitHub Account
 * GitHub Organization
 * Github OAuth Token
 * Github Repo for Config Server.
 * AWS Account
 * AWS CLI Setup [reference](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-quickstart.html)
### Repository Setup
1. Fork following repositories [reference](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
* [master-protect-service](https://github.com/MangeshOrganization/master-protect-service)
* [ConfigServer](https://github.com/MangeshOrganization/ConfigServer)
2. Update and Push main/src/main/resources/bootstrap.yml under your forked master-protect-service to point to your ConfigServer.

### Wiring It up 
#### Configuration Setup
1. Get you Git Hub Personal Access Token and Add it as part of the AWS SSM Parameter called "GIT_TOKEN" - This will be served to the Service Runtime as part of the Environment variable for accessing GitHub APIs.

![image](https://user-images.githubusercontent.com/2278604/116749112-cc2d8c80-aa43-11eb-8b42-a11430730661.png)

2. Configure your AWS Account's AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY [Make sure they are not Root User Tokens ] as part of GitHub Repository Secrets. 
#### Spin up the Service
  
 1. Create ECR Repo
      * Run following Command from AWS CLI
 ```
 mangesh@Mangeshs-MacBook-Air master-protect-service % aws ecr create-repository --repository-name my-ecr-repo/master-protect-service

 ```
 2. Create ECS Cluster
    * Update and Push master-protect-cloud-formation.yaml with following parameters values.
      1.  VPC - Fetch your Default VPC Id from AWS Console 
      2.  SubnetA - Fetch your Public subnet 1 ID from AWS Subnet Console- Please make sure this Subnet is associated with Default VPC
      3.  SubnetB - Fetch your Public subnet 2 ID from AWS Subnet Console- Please make sure this Subnet is associated with Default VPC
      4.  Image - ARN of my-ecr-repo/master-protect-service:latest which is created in Step 1.
      5.  GitHubToken - ARN of the GIT_TOKEN Parameter created in the Configuration Setup Step.
    * Run master-protect-cloud-formation.yaml  Cloud Formation Stack from AWS CLI.
      This will create all the necessary AWS Resources for your Service 
         ``` 
         mangesh@Mangeshs-MacBook-Air master-protect-service % aws cloudformation create-stack --stack-name GitGubECSCluster --template-body file://master-protect-cloud-formation.yaml --capabilities CAPABILITY_NAMED_IAM
       {
           "StackId": "arn:aws:cloudformation:ap-southeast-2:102455416451:stack/GitGubECSCluster/fab99740-ab12-11eb-a289-02806d445c90"
       }

         ```

    * Take a Note of the EndPoint Parameter Created as part of Cloud Formation Stack Execution, This is your {LOAD BALANCER DNS URL} (AWS Cosole --> CloudFormation --> Stacks --> GitGubECSCluster -->Outputs)

  3. Create the New Release from your master-protect-service repository
     * This basically will trigger [ Build & Deploy] GitHub Action which builds Docker Image and then pushes the Service Image to the ECR Repository Created in the Setup Steps.
     * Check if the Git Hub Action Build & Deploy is successful.
  4. Open the Web Browser and Paste {LOAD BALANCER DNS URL}:8080/actuator/health. If you get following then Your Service is up and running !!
 
  ```
  {"status":"UP"}
  ```
    In case you dont see it running , You might want to check the ECS Task Logs under Cloud Watch or Just Restart the Service from AWS ECS Console.
  5. Now Go to the https://github.com/organizations/{YourOrgName}/settings/hooks/new 
      * Enter Payload URL as {LOAD BALANCER DNS URL}:8080.
      * Enter Content-Type as application/json.
      * Choose Event using "Let me choose individual events" with just selecting "Repositories" Check Box.
      * Click Add Webhook - This Should basically send all the Repo Related Actions to Your Master Protect Service.


## <a name="action"/> See It in Action

### Brand New Repo Protection with Default Rules.
<details>
 <summary> Default Branch Protection Rules</summary>
 protection:
  required_status_checks:
    strict: true
    contexts: []
  enforce_admins: true
  required_pull_request_reviews:
    dismissal_restrictions:
      users: []
      teams: []
    dismiss_stale_reviews: false
    require_code_owner_reviews: true
    required_approving_review_count: 1
  restrictions:
    users: []
    teams: []
    apps: []
  required_linear_history: true
  allow_force_pushes: false
  allow_deletions: false
 </details>
1. Create a Brand New Public Repository in your Organization ( Via API or Web ) (in order to get a branch, you need a commit! Make sure to initialize with a README)
2. Navigate to your Repository --> Settings --> Branches , Check if it has Branch Protection Rules Applied - As shown in the Sample below.
![image](https://user-images.githubusercontent.com/2278604/116807898-0cdaf200-ab79-11eb-92b1-e922692adc7f.png)
3. Navigate to your Repository-->Issues , Check if It has new Issue Created with jSon payload of Branch Protection Rules Applied . - As shown in the Sample Below.
![image](https://user-images.githubusercontent.com/2278604/116807939-62af9a00-ab79-11eb-88bb-aa3965649c46.png)

### Brand New Repo Protection with Configurable Rules.
1. Update and Push https://github.com/<YOUR_ORG>/ConfigServer/blob/main/MasterProtectApplication.yml to have any of the desired Protection Rules Added/Updated.
2. Just Restart the ECS Service Provisioned as part of the AWS ( This can be automated as well ).
3. Now Follow the Steps in the "Brand New Repo Protection with Default Rules" to check if the Rules which You have configured are reflected in your Brand new Repository .

## <a name="future"/> Future Considerations
1. The CD Portion - Build & Deploy Step is derived form [GitHub Action Template](https://github.com/actions/starter-workflows) , However It has some updates to just push the image to latest tag , It can be improvised further to align with template totally - So that for any subsequent code updates , one need not manually update the service.
2. All the Services enabling this usecase can be "individual loosely coupled microservices" in themselves.
3. With the time constraint - This service does not have test cases implemented - Those need to be implemented.
4. Config Server can be totally seperated from the Main Spring Boot app.


## <a name="issues"/> Issues Encountered
1. Spring Boot - Spring Cloud Integration
 * Initially I started with latest version of Spring Boot and Cloud - However the Integrated Boot and Config Server does not seem to work (That feature is broken), Hence I had to switch back to lower version of Spring Boot and Cloud.
2.  Initially I had the Service with Path based Listening to the WebHook, However I realised that Organization Webhooks do not deliver to Path Based URLs- I did not have time to understand why , Hence swithced the Service to get away with relative Path.
3.  Had Some issues while Integrating with Branch Protection and Issue Creation GitHub APIs for valid Accept Headers, The Preview notices helped to source correct values for each API.
 

## <a name="References"/> References
* Issues API -- https://docs.github.com/en/rest/reference/issues
* Branch Protection API -- https://docs.github.com/en/rest/reference/repos
* GitHub Action for AWS Deployment - https://github.com/actions/starter-workflows

