# Tax Gap Detection & Compliance Validation Service

## Overview

This project is a backend service designed for tax auditors to validate financial transactions, detect tax gaps, enforce compliance rules, and generate reports.

---

## Tech Stack

* Java 17
* Spring Boot 3
* Spring Security (JWT)
* MySQL
* JPA/Hibernate
* Mockito (Testing)

##  How to Run

1. Clone repository:
git clone https://github.com/Thilak1999/TaxGapDetection.git

2. Navigate to project:
cd TaxGapDetection


3. Run application:
mvn spring-boot:run

## Database Setup

sql query:
CREATE DATABASE tax_app;

### Update application.properties:

 # put ur DB URL and credentials
spring.datasource.url=jdbc:mysql://localhost:3306/tax_app
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

## Authentication (JWT)

### Login API

POST /api/auth/login

Request:
{
  "username": "admin",
  "password": "password"
}

Response:
{
  "token": "JWT_TOKEN"
}

Use in header:
Authorization: Bearer <token>


## APIs

### 1. Upload Transactions

POST /api/transactions/upload

### 2. Reports

GET /api/reports/customers
GET /api/reports/exceptions/summary
GET /api/reports/exceptions/customer

---

##  Features

* Batch transaction upload
* Validation engine
* Tax calculation engine
* Rule-based compliance system
* Exception tracking
* Audit logging
* Reporting APIs

---

##  Testing

* Unit tests implemented using Mockito
* Coverage: ~75–80%


## Architecture

Layered architecture:

Controller → Service → Repository → Database


##  Notes

* Rule engine is database-driven (configurable via JSON)
* JWT-based authentication ensures secure APIs
* Optimized SQL queries for reporting
