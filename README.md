# master-protect-service

# Table of Content
* [Overview](#Overview)
* [Libraries /Services / Frameworks](#Libraries)
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

## <a name="setup"/> Setup and Usage

### Pre-requisites
You will need to have following  Resources handy before trying it out.
 * GitHub Account
 * GitHub Organization
 * Github OAuth Token
 * Github Repo for Config Server.
 * AWS Account
 
### Wiring It up 
#### AWS Configuration Sertup
1. Get you Git Hub Personal Access Token and Add it as part of the AWS SSM Parameter called "GIT_TOKEN" - This will be served to the Service Runtime as part of the Environment variable for accessing GitHub APIs.

![image](https://user-images.githubusercontent.com/2278604/116749112-cc2d8c80-aa43-11eb-8b42-a11430730661.png)

2. Configure your AWS Account's AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY [Make sure they are not Root User Tokens ] as part of GitHub Repository Secrets. This Step is Optional in case you want to push the Webservice Docker image directly in to AWS ECR.
3. Spin up the ECS Cluster as detailed [under] (https://ap-southeast-2.console.aws.amazon.com/ecs/home?region=ap-southeast-2#/firstRun)
   As part of this Step , Use [reference](https://github.com/MangeshOrganization/master-protect-service/blob/main/master-protection-def.yaml) Task Definition for setting up the Container Specs - This 

<details>
  <summary>Reference Task Def</summary>
  
  ```yaml
    {
  "ipcMode": null,
  "executionRoleArn": "arn:aws:iam::<YOUR AWS ACCOUNT ID>:role/ecsTaskExecutionRole",
  "containerDefinitions": [
    {
      "dnsSearchDomains": null,
      "environmentFiles": null,
      "logConfiguration": {
        "logDriver": "awslogs",
        "secretOptions": null,
        "options": {
          "awslogs-group": "/ecs/master-protect-service",
          "awslogs-region": "ap-southeast-2",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "entryPoint": null,
      "portMappings": [
        {
          "hostPort": 8080, --> This is important 
          "protocol": "tcp",
          "containerPort": 8080 --> This is important 
        }
      ],
      "command": null,
      "linuxParameters": null,
      "cpu": 256,
      "environment": [],
      "resourceRequirements": null,
      "ulimits": null,
      "dnsServers": null,
      "mountPoints": [],
      "workingDirectory": null,
      "secrets": [
        {
          "valueFrom": "arn:aws:ssm:ap-southeast-2:<YOUR AWS ACCOUNT ID>:parameter/GIT_TOKEN",
          "name": "GIT_TOKEN"
        }
      ],
      "dockerSecurityOptions": null,
      "memory": 512,
      "memoryReservation": null,
      "volumesFrom": [],
      "stopTimeout": null,
      "image": "<YOUR AWS ACCOUNT ID>.dkr.ecr.ap-southeast-2.amazonaws.com/my-ecr-repo/master-protect-service:latest",
      "startTimeout": null,
      "firelensConfiguration": null,
      "dependsOn": null,
      "disableNetworking": null,
      "interactive": null,
      "healthCheck": {
        "retries": 10,
        "command": [
          "CMD-SHELL",
          "curl -f http://localhost:8080/actuator/health || exit 1"
        ],
        "timeout": 5,
        "interval": 60,
        "startPeriod": null
      },
      "essential": true,
      "links": null,
      "hostname": null,
      "extraHosts": null,
      "pseudoTerminal": null,
      "user": null,
      "readonlyRootFilesystem": null,
      "dockerLabels": null,
      "systemControls": null,
      "privileged": null,
      "name": "master-protect-container"
    }
  ],
  "placementConstraints": [],
  "memory": "512",
  "taskRoleArn": "arn:aws:iam::<YOUR AWS ACCOUNT ID>:role/ecsTaskExecutionRole",
  "compatibilities": [
    "EC2",
    "FARGATE"
  ],
  "taskDefinitionArn": "arn:aws:ecs:ap-southeast-2:<YOUR AWS ACCOUNT ID>:task-definition/master-protect-service:1",
  "family": "master-protect-service",
  "requiresAttributes": [
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "com.amazonaws.ecs.capability.logging-driver.awslogs"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "com.amazonaws.ecs.capability.docker-remote-api.1.24"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "ecs.capability.execution-role-awslogs"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "com.amazonaws.ecs.capability.ecr-auth"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "com.amazonaws.ecs.capability.docker-remote-api.1.19"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "com.amazonaws.ecs.capability.task-iam-role"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "ecs.capability.container-health-check"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "ecs.capability.execution-role-ecr-pull"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "ecs.capability.secrets.ssm.environment-variables"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "com.amazonaws.ecs.capability.docker-remote-api.1.18"
    },
    {
      "targetId": null,
      "targetType": null,
      "value": null,
      "name": "ecs.capability.task-eni"
    }
  ],
  "pidMode": null,
  "requiresCompatibilities": [
    "FARGATE"
  ],
  "networkMode": "awsvpc",
  "cpu": "256",
  "revision": 2,
  "status": "ACTIVE",
  "inferenceAccelerators": null,
  "proxyConfiguration": null,
  "volumes": []
}
  ```
</details>
5. Fork [main](https://github.com/MangeshOrganization/master-protect-service) and [config](https://github.com/MangeshOrganization/ConfigServer) in to your GitHub Org.
6. Spin up the ECS Cluster as detailed [under] (https://ap-southeast-2.console.aws.amazon.com/ecs/home?region=ap-southeast-2#/firstRun)
7. Use the Task [Definition](https://github.com/MangeshOrganization/master-protect-service/blob/main/master-protection-def.yaml) to update the Task Definition once the Cloudformation stack successfully creates all other resources. 
8. 


## <a name="future"/> Future Considerations

## <a name="issues"/> Issues Encountered

## <a name="References"/> References
