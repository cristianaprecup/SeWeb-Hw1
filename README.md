# Recipe Recommender System - Semantic Web HW1

## Project Description
This application is an intelligent recipe management and recommendation system, developed for the Semantic Web course. It utilizes XML-based technologies to store, validate, and query data, combined with a web interface for end-users. The system allows creating user profiles, adding new recipes, generating personalized recommendations, and visualizing data through a dynamically transformed catalog.

## Core Technologies (Project Requirements Met)
As per the project constraints, all data storage and querying are handled exclusively using Semantic Web technologies:
* **XML**: Used as the primary database to store all recipes and user profiles.
* **DTD / XML Schema (XSD)**: Used to enforce strict data structure and validation rules when adding new recipes or users.
* **XSL (XSLT)**: Used to transform the raw XML data into a styled HTML catalog, applying conditional logic for UI rendering based on user skills.
* **XPATH / XQUERY**: Used extensively to traverse the XML DOM, query specific nodes, and filter recipes based on complex conditions (e.g., skill level and cuisine type).

## Public Repository Link
Repository URL: https://github.com/cristianaprecup/SeWeb-Hw1

## Team Members and Detailed Contributions

The project workload was divided into two main focus areas: Data & Backend and Forms, Display & UI.

### 1. Cristiana Precup (Data & Backend Focus)
* **Task 1**: Scraped recipes from BBC Good Food and generated the initial XML data structure, including randomized cuisine types and difficulty levels.
* **Task 2**: Created the DTD/XSD schema to enforce strict data validation rules for both recipes and users.
* **Task 3**: Implemented the logic to parse the XML into memory and expose the structured data to the User Interface.
* **Task 6**: Developed the XPath/XQuery logic for recommending recipes based on the user's cooking skill level.
* **Task 7**: Developed the advanced XPath/XQuery logic for recommending recipes that match both the user's skill level and their preferred cuisine type simultaneously.
* **Task 9**: Implemented XPath/XQuery filtering mechanisms to retrieve and display specific details for a single selected recipe.

### 2. Gabriela Nitu (Forms, Display & UI Focus)
* **Task 4**: Built the frontend form to add a new recipe, connected it to the XSD validation, and implemented the logic to save the valid recipe back into the XML file.
* **Task 5**: Created the user settings form to insert user profile data and save it persistently to the XML structure.
* **Task 8**: Authored the XSL transformation script to display the recipe catalog, incorporating conditional logic to highlight recipes with yellow or green backgrounds based on the active user's skill level.
* **Task 10**: Implemented the cuisine type filter, integrating a frontend dropdown with backend XPath/XQuery retrieval.
* **Task 11**: Handled the overall UI/UX polish, layout consistency, navigation structure, and the logic for switching between saved user profiles. 

## Setup Instructions
1. Clone the repository from GitHub.
2. Import the project into your preferred IDE (e.g., IntelliJ IDEA).
3. Run the application via the Spring Boot main class.
4. Access the web interface at the local address: http://localhost:8081/index.html.
