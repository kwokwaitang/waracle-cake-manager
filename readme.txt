My notes
--------

I had a brief look at the original Cake Manager, just some observations:
- 404 isssed with http://localhost:8282/cakes but you do get "Hello World!" with http://localhost:8282/
- Application based on JSP and Servlet technology with Hibernate (a bit of an "overkill" for the task at hand) to handle the back-end
- Wrong column names in CakeEntity.java
- No Unit tests

This version...
- I'll add the JWT token today and over the weekend in a separate GIT branch but for the now, the "main" branch deals with the requirements and some unit tests
- SpringBoot (v2.5.5) is used and developed on Intellij IDEA Ultimate edition
- Runs off Java 11+
- I tend to use the H2 Embedded database for "dev" work (which is accessible on the browser via /h2-console with connection details in application-dev.properties)
- I've placed the project on my own GITHUB account (set as public) so to download & to run...
  $ git clone https://github.com/kwokwaitang/waracle-cake-manager.git
  $ mvn spring-boot:run
- Instead of littering the source code with log outputs, I tend to use some AOP and apply it where necessary
- For basic styling, I used BootStrap v4.6 (via webjars) with Thymeleaf as the main view templating engine.
- There are some unit tests covering the controllers and service components
- If I were to use MySQL for persistent storage, that would be off a Docker container

JWT branch
----------

I've tackled the "Authentication via OAUTH2" as a separate branch as I wasn't too sure if I should set it using GITHUB as
the authorisation server or to have it as a localised set-up, so I opted for the latter.

What I've done is based on this article...
https://www.javainuse.com/spring/boot-jwt

To test, I used Insomnia as my REST API Client (but you can use POSTMAN etc.) and the key steps are...
[1] Register a username and password (up to you to alter the JSON)
[2] Authenticate the same username and password to receive a JWT
[3] Use the JWT in all further interactions by changing the value of the Authorization header with the received JWT in the format "Bearer <JWT>"

I have included the Insomnia tests (see Insomnia_2021-10-04.json)

The only caveat in this set-up is that the username must be "user", as I made a choice to make the CakeController to use
the following calls to the CakeService; getAvailableCakesViaRestApi() and addCakeViaRestApi() instead of getAvailableCakes()
and addCake() as an interpretation of "Would be good to change the application to implement proper client-server separation via REST API"

Both getAvailableCakesViaRestApi() and addCakeViaRestApi() will make HTTP requests to the CakeRestApiController


