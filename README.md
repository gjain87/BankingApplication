# Banking Application with Spring Boot

This repository contains a Spring Boot-based banking application with comprehensive features for efficient banking operations. It includes APIs for account management, transactions, and statement generation, along with robust security and email notification functionalities.

## Features

* **Account Management:**
    * Account creation with validation.
* **Transaction Management:**
    * Money deposit.
    * Money withdrawal with balance and transaction limit checks.
    * Money transfer between accounts with validation.
* **Statement Generation:**
    * Retrieve bank statements within a specified date range.
* **Security:**
    * Spring Security for authentication and authorization.
* **Email Notifications:**
    * Email alerts for credit transactions.
    * Email alerts for debit transactions.
    * Email alerts for money transfers.
    * Email alerts for login.
    * Email alerts for statement generation.
* **Validation:**
    * Robust validation checks for all API requests.

## Technologies Used

* Spring Boot
* Spring Security
* Java
* MySQL Database
* JavaMailSender
* Swagger

## Prerequisites

* Java Development Kit (JDK) 17 or higher
* Maven
* Database (configured in `application.properties`
* Email service credentials (configured in `application.properties`)
