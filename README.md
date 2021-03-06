# Beta.SAM Chatbot
This is the Beta.SAM Chatbot where users can ask questions. The Chatbot will be interactive and answers questions using Natural Language Process and Machine Learning.

## Value Impact
It is difficult for a user to find information across an extensive collection of documents and data in various systems.

Having a Chatbot which can answer user questions will allow users to quickly find the information they need. This will result in an immediate reduction in Tier 1 Help Desk tickets.

Currently, the Chatbot is trained to answer Beta.SAM API related questions by ingesting API documentation and FAQs. For example, you can ask "What is EM API?".

## Architecture of the Beta.SAM Chatbot
![Alt text](ui/docs/img/Architecture.png?raw=true "Title")

## Part 1. Chatbot Angular Application
The UI part of the project contains Chatbot written in Angular. You may learn more about the technical design of the App [here](ui/README.md).

##### Requirements
* Node
* Angular CLI

##### Setup for the Chatbot App
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
* `template-mvn.yml` - An AWS CloudFormation template that creates an application.
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

##### Deploy the Lambda function

Step 1. To create a new bucket for deployment artifacts, run `1-create-bucket.sh`.

`api$ ./1-create-bucket.sh`

Step 2. To deploy the application, run `2-deploy.sh mvn`.

`api$ ./2-deploy.sh mvn`

This deploy script uses **AWS CloudFormation** (Template file: `template-mvn.yml`) to deploy the Lambda function and an IAM role with necessary permission policies attached.


## Part 3. Future Capabilities

Chatbot functionality can be expanded to include below :

1.	It can be integrated for logged in users so that they can ask questions like:
My registration status ?  What are the new contract opportunities ?, etc...
2.	It can be integrated with other IAE systems like sam.gov, fpds.gov, etc.
3.	It can be made available via mobile app.
4. Documents to be searched against can be added dynamically by crawling, automatic ingestion, nosql DB, RDBMS etc. 


