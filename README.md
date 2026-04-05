# Inventory Management System

A simple, console-based inventory management system developed in Java. This application allows users to manage a list of products (items) and persist data using Excel files.

## Purpose

The main purpose of this project is to provide a lightweight tool for managing small-scale inventory. It demonstrates basic CRUD (Create, Read, Update, Delete) operations, user authentication, multi-threaded password masking, and external data persistence using Excel.

## Features

- **User Authentication**: Secure login system with masked password input.
- **Inventory Management**:
  - **Add Items**: Register new products with name, quantity, and price.
  - **View Items**: Display all current stock in a formatted table.
  - **Update Items**: Modify quantity and price of existing stock.
  - **Remove Items**: Delete products from the database.
- **Data Persistence**: Automatically synchronizes data with an Excel spreadsheet (`MyData.xlsx`).
- **Data Recovery**: Automatically loads previous inventory data on startup.

## Technologies Used

- **Language**: Java 17+
- **Build Tool**: Maven
- **Libraries**:
  - Apache POI (for Excel processing)
  - Apache Commons (for utility functions)
  - Log4j 2 (logging)

## Prerequisites

Before running the application, ensure you have the following installed:

- [Java Development Kit (JDK) 17+](https://www.oracle.com/java/technologies/downloads/)
- [Apache Maven](https://maven.apache.org/download.cgi)

## How to Download and Install

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/FunkoWon/Inventroy-System.git
    cd Inventroy-System
    ```
2.  **Build the Project**:
    Run the following command to download dependencies and compile:
    ```bash
    mvn clean install
    ```
3.  **Run the Application**:
    ```bash
    mvn exec:java -Dexec.mainClass="App"
    ```

## How to Use

1.  **Launch**: Run the application as described above.
2.  **Login**: Use the default credentials or those found in `MyData.xlsx`.
    - **Default Usernames**: `alice`, `bob`, `charlie`, `diana`, `eve`
    - **Default Password Example**: `pass1` (for alice), `pass2` (for bob), etc.
3.  **Main Menu**: Navigate through the numbered options:
    - `1` to add an item.
    - `2` to remove an item.
    - `3` to update stock.
    - `4` to view all items.
    - `5` to logout.
4.  **Automatic Saving**: Any change you make is instantly saved to `MyData.xlsx` in the project root.

---

_Created by [FunkoWon](https://github.com/FunkoWon)_
