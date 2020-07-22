# bsam-chatbot
This is BSAM Chatbot app

## Chatbot Service as Lambda Function
The project source includes function code and supporting resources:

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
* The AWS CLI v1.

##### Setup
Download or clone this repository.

 `$ git clone https://github.com/rinktel-git/bsam-chatbot.git
  $ cd bsam-chatbot/api`
To create a new bucket for deployment artifacts, `run 1-create-bucket.sh`.

`api$ ./1-create-bucket.sh`

##### Deploy
To deploy the application, `run 2-deploy.sh`.

`api$ ./2-deploy.sh
BUILD SUCCESSFUL in 1s
Successfully packaged artifacts and wrote output template to file out.yml.
Waiting for changeset to be created..
Successfully created/updated stack - codeathon-kendra-search`

You can also build the application with Maven. To use maven, add mvn to the command.

`api$ ./2-deploy.sh mvn
[INFO] Scanning for projects...
[INFO] -----------------------< com.example:java-basic >-----------------------
[INFO] Building java-basic-function 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
...`

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.
