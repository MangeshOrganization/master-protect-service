# master-protect-service

# Table of Content
* [Overview](#Overview)
* [Libraries /Services / Frameworks](#Libraries)
* [Design and Overall Flow] (#Design)
* [Setup and Usage](#setup)
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

## <a name="Design"/> Design and Overall Flow

## <a name="setup"/> Setup and Usage

### Pre-requisites
You will need to have following  Resources handy before trying it out.
 * GitHub Account
 * GitHub Organization
 * Github OAuth Token
 * Github Repo for Config Server.
 * AWS Account
 * AWS CLI Setup [reference](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-quickstart.html)
### Repository Setup
1. Fork following repositories [reference](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
[master-protect-service](https://github.com/MangeshOrganization/master-protect-service)
[ConfigServer](https://github.com/MangeshOrganization/ConfigServer)
2. Update main/src/main/resources/bootstrap.yml under your forked master-protect-service to point to your ConfigServer.
3. 
### Wiring It up 
#### AWS Configuration Sertup
1. Get you Git Hub Personal Access Token and Add it as part of the AWS SSM Parameter called "GIT_TOKEN" - This will be served to the Service Runtime as part of the Environment variable for accessing GitHub APIs.

![image](https://user-images.githubusercontent.com/2278604/116749112-cc2d8c80-aa43-11eb-8b42-a11430730661.png)

2. Configure your AWS Account's AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY [Make sure they are not Root User Tokens ] as part of GitHub Repository Secrets. 
3. Spin up the ECS Cluster 
  ##### Pre-Requisite
  * VPCID - Fetch your Default VPC Id from AWS Console 
  * SubnetA - Fetch your Public subnet 1 ID from AWS Subnet Console- Please make sure this Subnet is associated with Default VPC
  * SubnetB - Fetch your Public subnet 2 ID from AWS Subnet Console- Please make sure this Subnet is associated with Default VPC
  
 ##### Setup
 1. Create ECR Repo
 Run following Command from AWS CLI
 ```
 mangesh@Mangeshs-MacBook-Air master-protect-service % aws ecr create-repository --repository-name my-ecr-repo/master-protect-service

 ```
 2. Create ECS Cluster
 Run master-protect-cloud-formation.yaml  Cloud Formation Stack from AWS CLI. 
 This will create all the necessary AWS Resources for your Service 
  ``` 
  mangesh@Mangeshs-MacBook-Air master-protect-service % aws cloudformation create-stack --stack-name GitGubECSCluster --template-body file://master-protect-cloud-formation.yaml --capabilities CAPABILITY_NAMED_IAM
{
    "StackId": "arn:aws:cloudformation:ap-southeast-2:102455416451:stack/GitGubECSCluster/fab99740-ab12-11eb-a289-02806d445c90"
}

  ```
## <a name="future"/> Future Considerations
1. The CD Portion of the AWS infrastructure can be automated to avoid all the manual steps.
2. All the Services enabling this usecase can be individual loosely coupled microservices in themselves.
3. Config Server can be totally seperated from the Main Spring Boot app.


## <a name="issues"/> Issues Encountered

## <a name="References"/> References
