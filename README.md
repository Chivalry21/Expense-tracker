# Expense Tracker
## Project Overview

The Expense Tracker is a Spring Boot-based application designed to help users efficiently manage and track their expenses. It offers features such as user authentication,expense logging, categorisation, and report generation.
This application uses Spring Security for authentication,Data JPA for database operations and provides a good robust backend system for managing financial data
## Features
- *User Authentication*: Secure registration, login,and logout using Spring Security.

- *Expense Logging*: User can login expenses with details including amount ,date,description,and category.

- *Expense Categorisation*: Expense can be categorised into predefined groups for easy and better management and analysis.

- *Report Generation*: Users can generate detailed reports based on the date range, category, and expense type. Report are generated in CSV format and saved in Resource folder.

- *Data Validation*: Comprehensive validation and error handling to ensure data integrity and meaningful feedback.

## Technologies

- *Spring Boot*: for the backend framework.
- *Spring Security*: for authentication and authorisation.
- *Spring DataJPA*: for database operations/integration.
- *Database*:MySql or PostgresSql for data storage.
- *Other Dependencies*: Lombok, Apache Commons CSV,etc.

## Getting Started

### Dependencies

- *JDK17*
- *Spring Boot Version*: 3.2.2
- *Maven*
- *PostgresSQL*
### Setup
1. *Clone the repository*: git clone  https://github.com/Chivalry21/ExpenseTracker
2. *Configure the Database*: Create a database in my PostgresSQL and update "src/main/resources/application.properties".
3. *Run the Application*: Navigate to the project directory and execute. mvn spring-boot:run
4. *Access the Application*, Open any API testing system like Postman and go to "http://localhost:8090" to start using the expense tracker API.

## Project Structure
- src/main/java/com.chivalrycode.expensetracker: Application source files.
    - config: Configuration classes (e.g., security configuration).
    - controller: REST API controllers.
    - model: Entity definitions.
    - repository: Spring Data JPA repositories.
    - service: Service layer classes.
    - validation: Validation classes and logic.
- src/main/resources: Configuration files and resources.
    - application.properties: Spring Boot configuration file,jdbc:postgresql://localhost:5432/expense_tracker

## API Endpoints

- *User Authentication*
    - "/api/auth/signup": Register a new user(POST Request.
    - "/api/auth/login": Authenticate a user.
    - "/api/auth/logout": Logout a user.
- *Expenses*
    - "/api/expenses": Create and get all expenses(POST Request).
    - "/api/expenses/{id}": Update and delete an expense (PUT and DELETE Request.
    - "/api/expenses/category/{id}": Get expense by category(GET Request).
- *Categories*
    - /api/categories: Create, get, update, and delete categories.(POST,PUT,DELETE,GET Request)
## Contributing and Correction 
Contributions are welcome!For major changes, please open an issue first to discuss what you would like to change.
## Contact
Email-chiomaaugustus@gmail.com

Project Link: https://github.com/Chivalry21/ExpenseTracker