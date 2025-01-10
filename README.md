# E-Commerce Store

## Overview
This project is a functional e-commerce application built using **Java Spring Boot** and **Thymeleaf**. It incorporates essential features commonly found in e-commerce platforms, such as user management, product management, and order processing.

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

### 1. Clone the Repository
```bash
git clone https://github.com/TanzimHossain2/Ecom_Store.git
```

### 2. Open the Project
Import the project into your preferred IDE (e.g., IntelliJ IDEA, Eclipse).

### 3. Set Up the Database
- Create a PostgreSQL database.
- Update the database configuration in the `application.properties` file:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/<database_name>
spring.datasource.username=<username>
spring.datasource.password=<password>
```

### 4. Run the Application
Execute the application from your IDE or using the command line:
```bash
./mvnw spring-boot:run
```

### 5. Access the Application
Open a browser and navigate to:  
[http://localhost:8080](http://localhost:8080)

## AWS Deployment Guide

### 1. Create an AWS Account & Login
- Sign up for an AWS account at [aws.amazon.com](https://aws.amazon.com/).
- Log in to the AWS Management Console.

### 2. Set Up IAM User and Role
- Navigate to the IAM (Identity and Access Management) service.
- Create a new user with programmatic access.
- Attach necessary policies to the user.
- Create a role for EC2 with the following policies:
   - `ElasticBeanstalkWebTier`
   - `ElasticBeanstalkWorkerTier`
   - `AWSElasticBeanstalkMulticontainerDocker`

### 3. Configure Access Key & Secret Key
- Generate an access key and secret key for the created user.
- Configure these keys in your local environment or CI/CD pipeline.

### 4. Create S3 Buckets
- Navigate to the S3 service.
- Create three S3 buckets with appropriate naming conventions:
   - Profile Images
   - Categories
   - Products
- Configure bucket permissions and policies.

### 5. Create Database in RDS
- Navigate to the RDS (Relational Database Service).
- Create a new database instance.
- Configure settings such as instance type, storage, and security groups.

### 6. Update Spring Boot Application Configuration
- Update the `application.properties` or `application.yml` file with the RDS database details:
```properties
spring.datasource.url=jdbc:postgresql://<rds-endpoint>:5432/<database_name>
spring.datasource.username=<username>
spring.datasource.password=<password>
```

### 7. Build the Application
- Use Maven to create a JAR file of the application:
```bash
./mvnw clean package
```

### 8. Deploy to Elastic Beanstalk
- Navigate to the Elastic Beanstalk service.
- Create a new environment for the application.
- Upload the generated JAR file and deploy.

### 9. Elastic Beanstalk Role Configuration
- Assign the created EC2 role to the Elastic Beanstalk environment with the following policies:
   - `ElasticBeanstalkWebTier`
   - `ElasticBeanstalkWorkerTier`
   - `AWSElasticBeanstalkMulticontainerDocker`

### 10. Access the Deployed Application
Access your application through the Elastic Beanstalk environment URL.

## Key AWS Services Used
- **S3**: For storing images (Profile, Categories, Products)
- **RDS**: For relational database management
- **Elastic Beanstalk**: For application deployment and scaling