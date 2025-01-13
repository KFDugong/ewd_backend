# Setup project

## JWT Secret Key

The JWT secret key is stored in `/resources` and is supposed to be called `jwt-secret.key` for the application to work properly.
The generation of the JWT secret key is handled by this source: https://jwtsecret.com/generate

## Prerequisite

- The application is using Postgresql as a database and requires access to the public schema and is using the default Postgresql port 5432.
- For now, the table `users` table is getting created automatically as long as the application has access to the database. 
- For the first milestone, the database connection will still be hardcoded in the `application.properties` file but in later stages, this information are supposed to be stored elsewhere.

## Installation

After installing and starting Postgresql the application can be started with `mvn spring-boot:run`. 
An error that might occur is that the application doesn't have permission to access the database. For now, we have to grant the user in the `application.properties` all privileges and access right for the application to work.
This will be fixed soon, but it's not a priority right now.

## Package structure

This application is using a layer orientated file structure where the different entities are stored in their respective layer. 
The first term of a class is determining the entity or component that the application is using. For example, `Jwt**`-`layer` is handling Jwt related business logic.
In later stages where the application might be getting bigger and bigger, I might start creating more sub packages in each layer package to differentiate between the entities.

## First prototypes

For the better understanding of Spring, I've followed a guide to understand how to create the first simple REST calls. 
I have not removed them yet, since I can verify if the generated JWT token is valid by sending a `POST` call to get the resource. 
The prototype has the term `Prototype`-`layer` to let the reader know that these classes are not relevant for the first milestone.

## Tools

To validate the first few REST calls, I've used Postman which can be found in the directory: `postman` which is located in the root directory. 
In later stages, where more entities are added to the project, I will consider adding `http` tests. 

## Testing

Due to time limitation, only some basic spring tests will be implemented. In later stages, where more entities are integrated into the application, more tests will be introduced.
