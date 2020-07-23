# bsam-chatbot
This is BSAM Chatbot app where users can ask questions. Chatbot will be interactive and answers questions using Natural Language Process and Machine Learning. The search is powered by AWS Kendra (https://aws.amazon.com/kendra/).

Currently, the Bot is trained to answer Beta SAM API related questions. For example you can ask "What is EM API?".

## Architecture of the BSAM Chatbot
![Alt text](ui/images/Codeathon-Architecture.png?raw=true "Title")

## Part 1. Chatbot Angular Application
The UI prt of the project contains Chatbot written in Angular. You may learn more about the design of the App [here](ui/README.md).

##### Requirements
* Node
* Angular CLI

##### Setup for the chatbot App
Download or clone this repository.
```
 $ git clone https://github.com/rinktel-git/bsam-chatbot.git
 $ cd bsam-chatbot/ui
```
##### Serving the app locally
1. `ui$ npm install` or npm update to make sure you have the latest version of all the packages used in the app.
2. `ui$ npm run start` to start the local server

Navigate to `localhost:4200` in any browser to view the site.

The Chatbot Angular App is deployed as Elastic Beanstalk application in Docker container environment. Go ahead and check the Bot at http://codeathon.us-east-1.elasticbeanstalk.com/


## Part 2. Chatbot Backend Service as AWS Lambda Function
The API part of the project contains lambda function code and supporting resources:

* `src/main `- A Java function.
* `src/test` - A unit test and helper classes.
* `template.yml` - An AWS CloudFormation template that creates an application.
* `pom.xml` - A Maven build file.
* `1-create-bucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

Use the following instructions to deploy the Chatbot lambda function.

##### Requirements
* Java 8 runtime environment (SE JRE)
* Maven 3
* The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the Windows Subsystem for Linux to get a Windows-integrated version of Ubuntu and Bash.
* The AWS CLI.

##### Setup for the Lambda function
Download or clone this repository.
```
 $ git clone https://github.com/rinktel-git/bsam-chatbot.git
 $ cd bsam-chatbot/api
```
To create a new bucket for deployment artifacts, `run 1-create-bucket.sh`.

`api$ ./1-create-bucket.sh`

##### Deploy
To deploy the application, `api$ ./2-deploy.sh mvn`

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.
