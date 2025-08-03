# Mobilise Book Management System

## Objective
Development of a simple RESTful web service using Java and Spring Boot for a 
book management system.

## Features
### CRUD Operations:
- **Create:** To Add a new book to the system.
- **Read:** To Retrieve details of all books or a specific book by ID.
- **Update:** To Modify details of an existing book.
- **Delete:** To Remove a book from the system.

### Search Feature:
- I Provided an endpoint to search books by title, author, ISBN or 
publicationYear.

### Pagination:
- I Implemented pagination for the "Retrieve all books" feature.

## Technical Specifications
- The application was built using Java 21 and Spring Boot version: 3.2.5 .
- For database access, I use Spring Data JPA and MySQl in-memory database for data 
storage.
- I Include validation checks. For instance, a book should have a valid title, 
author, and publication year based on the criteria I defined.
- I Implement proper exception handling. For example, when trying to fetch a 
book by ID that doesn't exist, it should return a Not Found status.
- I Ensure the application is properly structured, separating concerns 
(controllers, services, repositories, etc.).
- I Ensure the code is well documented and explained with Docstring and comments.

## Test (optional)
- I Added unit tests for the service layer.

## API Responses
- The API responses is in JSON format.

## Database Configuration

- Before running the project, create the database manually:

```sql
CREATE DATABASE store_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

- updating -