# Courier Management System

A full-stack Courier Management System built with **Java** and **Spring Boot** for managing shipments, couriers, order tracking, and price calculations. The project demonstrates modern backend development practices using a layered architecture and RESTful APIs.

---

## Features

* Create and manage shipments
* Courier management
* Shipment tracking
* Price calculation
* Order management
* Administrative dashboard
* REST API for client-server communication
* Layered architecture (Controller → Service → Repository)

---

## Technologies

### Backend

* Java 21
* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate
* Maven

### Database

* MySQL

### Frontend

* Spring Boot MVC
* Thymeleaf
* HTML5
* CSS3
* Bootstrap
* JavaScript

## Project Structure

├── Project-api/          # REST API
├── ProjectApp/           # Web Application
└── docs/                 # Documentation & Images


## Architecture

The application follows a layered architecture:


Controller
      │
Service
      │
Repository
      │
Database




## Main Modules

* Shipment Management
* Courier Management
* Order Management
* Shipment Tracking
* Price Calculator
* Administration Panel


## REST API

Example endpoints:

| Method | Endpoint                  | Description              |
| ------ | ------------------------- | ------------------------ |
| GET    | `/shipments`              | Get all shipments        |
| POST   | `/shipments`              | Create shipment          |
| GET    | `/track/{trackingNumber}` | Track shipment           |
| GET    | `/couriers`               | List couriers            |
| POST   | `/price/calculate`        | Calculate shipping price |


## Future Improvements

* JWT Authentication
* Role-based authorization
* Email notifications
* PDF shipping labels
* Docker support
* Unit & Integration Tests
* CI/CD Pipeline

---

## Learning Objectives

This project was developed to improve practical experience with:

* Spring Boot
* RESTful API design
* Spring Data JPA
* MVC architecture
* Database design
* Layered software architecture
* Backend and frontend integration

---

## Author

Developed by **Your Name** as a personal portfolio project.
