# E-Commerce Store

## Overview
This project is a simple yet functional e-commerce application built using **Java Spring Boot** and **Thymeleaf**. It incorporates key features commonly found in e-commerce platforms, including user management, product management, and order processing.

## Features

### User Features
- User Login and Registration
- Browse and Search Products
- Add Products to Cart
- Place Orders
- View and Cancel Orders
- View and Update User Profile
- Change Password
- Forgot Password Recovery

### Admin Features
- Add and Manage Products (CRUD Operations)
- Manage Categories (CRUD Operations)
- User Account Blocking and Unblocking
- Admin Dashboard for Insights and Control

### Additional Features
- Authentication and Authorization using Spring Security
- Email Notifications
- Pagination for Product Listings
- Search Functionality

## Technologies Used

### Backend
- **Java 23**
- **Spring Boot 3**
- **Spring Security**
- **Spring Data JPA**

### Frontend
- **Thymeleaf**
- **Bootstrap**
- **jQuery**

### Database
- **PostgreSQL**

## How to Run

1. **Clone the Repository**
   ```bash  
   git clone https://github.com/TanzimHossain2/Ecom_Store.git  
   ```  

2. **Open the Project**  
   Import the project into your preferred IDE (e.g., IntelliJ IDEA, Eclipse).

3. **Set Up the Database**
    - Create a PostgreSQL database.
    - Update the database configuration in the `application.properties` file:
      ```properties  
      spring.datasource.url=jdbc:postgresql://localhost:5432/<database_name>  
      spring.datasource.username=<username>  
      spring.datasource.password=<password>  
      ```  

4. **Run the Application**  
   Execute the application from your IDE or using the command line:
   ```bash  
   ./mvnw spring-boot:run  
   ```  

5. **Access the Application**  
   Open a browser and navigate to:  
   [http://localhost:8080](http://localhost:8080)

## Screenshots

