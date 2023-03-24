# Abn Amro Recipe Assessment

This project is an assignment for a Backend Developer of Abn Amro Bank. This is a Rest API project about recipe. 

# Project Description
This Rest API project requirements are;
- Inserting new recipe
- Updating a recipe
- Deleting a recipe
- Finding recipe with ID
- Getting all recipes
- Searching and filtering recipes

The main classes are as follows;
* RecipeController: It is the only controller which has the above requirements as an endpoint.
* IRecipeService: Interface for services
* RecipeServiceImpl: This is the class implemented the interface.
* RecipeSpecification: This class is generated for Advanced Search on recipes. It implements Specification interface.
* ObjectMapperUtils: This class is used to create a generic mapping for project.

There are 3 tables used for this project. These are:
* Recipe: This table is the parent table. It has an entity called Recipe and dto called RecipeDto.
* Ingredients: This is the one of the child tables. It has an entity called Ingredients and dto called IngredientsDto.
* Instructions: This is the other child table. It has an entity called Instructions and dto called InstructionsDto.

# Technologies Used

To create this project below technologies are being used:

- Java 11
- SpringBoot 2.7.9
- ModelMapper (To map entities to Dto and vice versa)
- Lombok
- Postgresql for DB (Used postgres image version 13)
- Spring-doc OpenApi for API Documentation
- Docker (preparing production-ready app)
- Springboot starter test and Mockito for tests.

# Installation

To install the project;
1. Clone the repository from this url [Recipe Application](https://github.com/polatskiDev/abn_recipe)
2. You should have docker installed in your local machine. You can install from [this](https://docs.docker.com/get-docker/) url.
3. Run docker on your local.
4. locate the project folder where pom.xml and docker-compose.yml exits.
5. run `mvn install` to create jar file.
6. After above install finished, run this command `docker-compose up`
7. After successful run, go to this url `http://localhost:8081/swagger-ui/index.html` to test API endpoints via Swagger UI.

# Usage
There are 6 endpoints in this project. Details of the usage is below.

* Find Recipe By ID: GET /api/v1/recipe/{recipeId} is the endpoint. With the id created before, It can be searched here with it.
* Update Recipe By ID: PUT /api/v1/recipe/{recipeId} is the endpoint. With the id created before and the RecipeDto it can be updated.
* Delete Recipe By ID: DELETE /api/v1/recipe/{recipeId} is the endpoint. With the id created before, it can be deleted.
* Find All Recipes: GET /api/v1/recipe is the endpoint. This api does not have any input and returns all recipes created before.
* Save Recipe: POST /api/v1/recipe is the endpoint. With given details of recipe, it can be saved.
* Search Recipe: GET /api/v1/recipe/search is the endpoint. This endpoint has some parameters. These are;
  * isVegetarian: boolean value true/false
  * servingNumber: search for greater than equal to input value
  * ingredientName: search for ingredient included
  * excludeIngredientName: search for ingredient excluded
  * instructionText: search for instruction included