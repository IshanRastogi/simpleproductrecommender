**Simple Product Recommender**

This guide walks you through the process of building a Docker image
for running a Spring Boot API. This application is built
as a solution to the following problem.

_Problem_:
A customer plays a game in one of our web shops. Right after the 
customer has completed the game, a small layer should be shown to 
the customer with at least three other products that she might be 
interested in.

_Requirements_:
1. JDK 1.8 or later
2. Maven 3.0+
3. Docker

_Tools Used_:
* Spring Boot
* Maven
* Docker
* H2 DB
* JUnit

_Solution_:
The API provides 4 routes.
1. The default route ("/") [GET]: and returns status 200. 
2. Route 2 and 3 [GET]:
"/customers/{customerId}/games/recommendations", 
"/recommendations/games/customers/{customerId}" and return:
    2. 404 if user not found.
    2. 404 if recommendations are inactive for the user
    2. Recommendations for the requested user (limited by count 
    parameter, defaults to 5)
   
3. Route 4 ("/recommendations/update") [POST]: endpoint to upload
a CSV file containing priorities and recommendations for all 
users and returns:
    3. 200 if upload is successful
    3. 420 if the CSV file doesn't match the defined format

_Build_: To build the application, simply run
1. Clone this repo using:
`git@github.com:IshanRastogi/simpleproductrecommender.git`
or download the zip:
`https://github.com/IshanRastogi/simpleproductrecommender/archive/master.zip`
2. `mvn clean` to ensure that the working directory is clean
3. `mvn install` this command will download all the libraries
which are required by the API to function, build an executable
jar and build and tag a docker image, ready to be run.

_Run_:
1. You can run the application by either utilising the 
docker-compose file provided, by simply executing:
`docker-compose up`
2. Or you can run it with the following command:
`docker run -p 8080:8080 -t simplerecommender/recommender`
3. Once the application is running, you can upload a CSV file 
by posting it to the upload endpoint:
`curl -F file=@test.csv http://localhost:8080/recommendations/update`
4. You can stop the API, by executing:
`docker-compose down`
