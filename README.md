Bank Service Management Project

  This project is a simple **Bank Management Web Application** built using Java and Servlet technology.  
It follows a layered architecture and exposes REST-style APIs for basic banking operations.

- Java Web Applications (Servlets)
- Filters in Web.xml
- JDBC with Connection Pool (HikariCP)
- Database versioning using Liquibase
- REST APIs using JSON
- Unit Testing using JUnit & Mockito
- Maven project structure

 Technologies Used

- Java 21  
- Servlets (Jakarta / javax.servlet)  
- MySQL Database  
- JDBC  
- HikariCP – Connection Pool  
- Liquibase – DB migration  
- Jackson – JSON parsing  
- SLF4J + Logback – Logging  
- JUnit 5 – Unit Testing  
- Mockito – Mocking  
- Maven – Build tool  
- Apache Tomcat 9 – Server  

Request Flow (End-to-End)

1. Client sends HTTP request (Postman / Frontend)
2. Request enters **Tomcat**
3. web.xml routes the request:
   - First → RequestLoggingFilter`
   - If URL starts with /account/* → AuthFilter
4. Servlet receives the request
5. Servlet parses JSON using Jackson
6. Servlet calls Service layer
7. Service applies business rules
8. Service calls DAO
9. DAO interacts with DB using JDBC + HikariCP
10. Response is returned as JSON

Database is managed by Liquibase automatically on startup.

Tables:
- users
- accounts
- transactions

Tables :

create database masterdb;
use masterdb;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE,
    password VARCHAR(256)
);

drop table accounts;
CREATE TABLE accounts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50),
  balance DOUBLE
);

CREATE TABLE transactions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  account_id INT,
  type VARCHAR(20),
  amount DOUBLE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Final APIs

  1.Register user
      http://localhost:8080/bank-1.0.0/auth/register
      {
  "username": "rameez",
  "password": "12345678"
}
2.Login User
          http://localhost:8080/bank-1.0.0/auth/login
              {
  "username": "rameez",
  "password": "12345678"
}
3.Open Account 
    http://localhost:8080/bank-1.0.0/account/open
    {
  "name": "rameez",
  "balance": 5000
}
4.Deposit
    http://localhost:8080/bank-1.0.0/account/deposit
    {
  "accountId": 1,
  "amount": 30000
}
5.Withdraw
    http://localhost:8080/bank-1.0.0/account/withdraw
    {
  "accountId": 1,
  "amount": 1000
}

  
          


