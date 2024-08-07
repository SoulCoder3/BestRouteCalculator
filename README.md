# Best Route Calculator

## Overview

The **Best Route Calculator** is a desktop application developed in Java using the Swing framework. This project is designed to demonstrate the process of the Floyd-Warshall Algorithm, a well-known algorithm for finding the shortest paths in a weighted graph with positive or negative edge weights (but no negative cycles). The application features a user-friendly graphical interface that allows users to input and edit data and then display the calculation results. Data is stored in a local MySQL database using the C3P0 library, ensuring efficient handling of concurrency issues.

## Features

- **Graphical Interface**: Developed using Java Swing, the interface allows users to:
  - Input nodes and edges for a graph.
  - Edit the data directly within the interface.
  - Visualize the shortest path calculations.

- **Floyd-Warshall Algorithm Implementation**: 
  - The core functionality is based on the Floyd-Warshall algorithm, which computes shortest paths between all pairs of nodes in a graph.

- **Database Integration**: 
  - Data storage is handled by a local MySQL database.
  - The C3P0 library is utilized to manage database connections and handle concurrency issues effectively.

## Technologies Used

- **Java**: The core programming language used to build the application.
- **Swing**: Used for creating the graphical user interface (GUI).
- **MySQL**: Database system used for storing and retrieving data.
- **C3P0 Library**: Employed to manage database connections and ensure smooth handling of concurrent operations.

## Installation

### Prerequisites

- **Java Development Kit (JDK)**: Ensure you have JDK 8 or later installed.
- **MySQL**: A MySQL database should be set up and running locally or on a server.
- **C3P0 Library**: Included as part of the project dependencies.

### Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/SoulCoder3/BestRouteCalculator.git
