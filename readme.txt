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
- The requirements I have covered as I have interpreted them...
  > By accessing the root of the server (/) it should be possible to list the cakes currently in the system. This must be presented in an acceptable format for a human to read.
    GET /

  > It must be possible for a human to add a new cake to the server.
    GET /new-cake-details
    POST /new-cake-details

  > By accessing an alternative endpoint (/cakes) with an appropriate client it must be possible to download a list of the cakes currently in the system as JSON data.
    GET /cakes

  > The /cakes endpoint must also allow new cakes to be created.
    POST /cakes

    You can set-up the JSON for the POST using your favourite REST-API Client (Insomnia, Postman etc) or use Intelli and its Endpoints feature
    ###
    POST http://localhost:8080/cakes
    Content-Type: application/json

    {
      "title" : "The Biscoff Cake",
      "description" : "Vanilla sponge topped with Lotus biscuits",
      "imageUrl" : "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"
    }

- For basic styling, I used BootStrap v4.6 with Thymeleaf as the main view templating engine.
- There are some unit tests covering the controllers and service components
- If I were to use MySQL for persistent storage, that would be off a Docker container


https://stackoverflow.com/questions/25010390/dynamic-chaining-thenreturn-in-mockito
https://stackoverflow.com/questions/14845690/mockito-invaliduseofmatchersexception





