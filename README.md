# Project exam-19j18a

#### Getting Started
   
<small>
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.
</small>

##### Prerequisites
  
- Oracle JDK 11 (11.0.5.hs)
- Maven 3.5 or high

<small>
If you use http://sdkman.io, then you are able to install in that way:

    > sdk install java 11.0.5.hs
    > sdk install maven 3.5.2
    
</small>
   
#### Building
 
To build project to do:

    $PROJECT_ROOT> ./mvn clean install

or with maven wrapper:

    $PROJECT_ROOT> ./mvnw clean install

To execute unit and integration tests:

    $PROJECT_ROOT> ./mvnw clean verify

