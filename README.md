# Github API tests

## Build Status

[![Circle CI](https://circleci.com/gh/axwtw/githubApiTests/tree/master.svg?style=svg)](https://circleci.com/gh/axwtw/githubApiTests/tree/master)

## About

This repository contains simple API tests for GITHUB API v3

## Prerequisites

1. Make sure you have [Java](https://java.com/) installed on your system, if not follow the vendor instructions for installing them on your operating system.
2. Ensure JAVA_HOME environment variable is set and points to your JDK installation
3. Install [IntelliJ IDEA](https://www.jetbrains.com/idea/)
4. Install [Maven](https://maven.apache.org/install.html), follow the vendor instructions for installing it on your operating system.

## How to run the tests locally

The following steps should get you set up for running API tests locally on your machine:

1. Clone this repository to your local machine
2. Open terminal/cmd and navigate to the project folder
3. Run the tests using the command -mvn clean test
4. You can see the results in the target folder

## How to run the tests on remote CI server

1. Install [Curl](https://curl.haxx.se/) on your local machine
2. Run the tests via curl request:
curl -X POST https://circleci.com/api/v1.1/project/github/axwtw/githubApiTests/tree/master?circle-token=2326ebfac62387fe95563f124b6d7ac0c9a797f8
3. To get the test results use following link with updated build number which you can get from json response of previous request that triggered build.
https://circleci.com/api/v1.1/project/github/axwtw/githubApiTests/**build_number**/tests?circle-token=2326ebfac62387fe95563f124b6d7ac0c9a797f8


## Built with

* [Maven](https://maven.apache.org/) - Dependency Management
* [TestNG](http://testng.org/) - Testing framework
* [REST-assured](http://rest-assured.io/) - framework for testing and validation of REST services
