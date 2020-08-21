### Mentoring Application 
##### Artem Romanenko

#### Project aim 

This is a back-end part of the application which schedules consultations provided by mentors.

Mentors can create consultations using this application and then users can book them.

The project was created as part of my students practice in "Euvic" company

 #### Technologies 
 
1. java 11
2. Spring Boot framework 
3. gradle 
4. Postgre SQL server
5. FlyWay db migration
6. Test - JUnit 

#### API description 

method - pattern - description - 

##### Consultations-related

GET /consultations returns all(or filtered) Consultation objects from db, has 3 possible query parameters
                   ?date ?isReserved(boolean) ?time 
                   
GET /consultations/{id} returns Consultation object from db with specified id

POST /consultations creates new Consultation object in the db
 
 should receive json Consultation object
                    {"date":"yyyy-mm-dd"(not null),
                     "time":"HH:MM:SS"(not null),
                      "user_who-created":"int(notNull)",
                       "user_who_reserved":"int"}
                       
POST /consultations/createMultiple creates Consultation objects for specified range (for example from 12.00 to 14.00 8 consultations will be created)

 should receive json MultipleConsultations object 
                    {"date":"yyyy-mm-dd"(notNull),
                    "timeFrom":"hh:mm:ss"(notNull), 
                    "timeTo":"hh:mm:ss"(notNull),
                    "user_who_created_is": "int"(notNull)}
                    
DELETE /consultations/{id} deletes consultation object with specified id from the db

PUT /consultations/{id} changes object with specified id from db on received from request

PUT /consultations/{consultationId}/{userId} fast method to reserve already created consultation(adds usersWhoReservedId to the Consultation Object)

##### User-related

GET /users returns all(or filtered) Consultation objects from db, has 3 possible query parameters
                              ?role(ADMIN,STUDENT,MASTER) ?username ?email
                              
POST /users/registration - creates a new user inactive user in the db, sends activation letter by mail
                           
GET /activate/{code} - activates user if the activation code is right

DELETE /users/{id} - deletes user with specified id from db 

GET /me - shows information about currently logged-in user 

##### Authentication

POST /auth/signin authenticates user and returns Bearer token for authentication

should receive Object 
                    {"username": "String",
                    "password" "String"}

 