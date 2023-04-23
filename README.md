# DNE sequence application
This is a simple Spring Boot rest api application. 

## What you'll need
Java 17  

## Steps to run application
- Download the follwing jar file:
https://github.com/dagrest/DneSequence/blob/main/dnesequence-0.0.1-SNAPSHOT.jar

- Go to folder containing **dnesequence-0.0.1-SNAPSHOT.jar**

- Run Application as: **java -jar dnesequence-0.0.1-SNAPSHOT.jar**

## Test Application
- curl -X POST http://localhost:10000/server -H "content-type:application/json" -d '{"seq":[1,2,3]}'  
or  
For Windows:  
curl -X POST http://localhost:10000/server -H "content-type:application/json" -d "{\\"seq\\":[1,2,3]}"  
Response: false  
- curl -X POST http://localhost:10000/server -H "content-type:application/json" -d '{"seq":[4, 1, 7, 8, 7, 2]}'  
or  
For Windows:  
curl -X POST http://localhost:10000/server -H "content-type:application/json" -d "{\\"seq\\":[4, 1, 7, 8, 7, 2]}"  
Response: true  
- curl -X GET http://localhost:10000/health   
Response: OK
