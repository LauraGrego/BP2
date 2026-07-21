# BP2 – Fact Table and Bitmap Indexing

A Java project that demonstrates fact-table generation, bitmap indexing, conditional querying and aggregate operations.

The project was developed as part of the **Database Systems 2 (BP2)** course at the **School of Electrical Engineering, University of Belgrade (ETF)**.

## Overview

This application simulates several concepts commonly used in data warehouses and analytical database systems.

It reads data from three dimension tables:

* Students
* Student indexes
* Exams

The application then generates a fact table containing combinations of dimension identifiers and a numerical measure.

Queries can be executed in two ways:

* By scanning the fact table directly
* By using bitmap indexes

The results produced by both approaches can be compared to demonstrate how bitmap indexes can improve filtering over attributes with a limited number of possible values.

## Features

* Reading dimension tables from text files
* Generating a fact table
* Generating up to 10,000 fact-table rows
* Saving the generated fact table to a file
* Creating bitmap indexes
* Filtering data without bitmap indexes
* Filtering data using bitmap indexes
* Parsing conditions containing `AND` and `OR`
* Filtering by student, index and exam identifiers
* Displaying filtered fact-table rows
* Executing aggregate functions
* Comparing indexed and non-indexed query results

## Supported Aggregate Functions

The application supports the following aggregate functions:

* `MIN`
* `MAX`
* `AVG`
* `SUM`
* `COUNT`

These operations are applied to the measure stored in the filtered fact-table rows.

## Technologies

* Java
* Object-oriented programming
* Java Collections Framework
* File input and output
* Bitmap indexing
* IntelliJ IDEA

No external database server or third-party library is required.

## Project Structure

```text
BP2/
└── Baze2/
    ├── src/
    │   ├── klase/
    │   │   ├── BitMapIndexMaker.java
    │   │   ├── Fakat.java
    │   │   ├── FakatTableGenerator.java
    │   │   ├── FakatTableReader.java
    │   │   ├── Pair.java
    │   │   └── TableReader.java
    │   │
    │   ├── Main.java
    │   ├── Student.txt
    │   ├── Indeks.txt
    │   └── Ispit.txt
    │
    ├── TabelaFakata.txt
    ├── Baze2.iml
    └── .gitignore
```

## Main Components

### `Main`

The entry point of the application.

It:

1. Loads the dimension-table files.
2. Generates the fact table.
3. Saves the generated table to a text file.
4. Defines a filtering condition.
5. Executes the condition without bitmap indexes.
6. Creates bitmap indexes.
7. Executes the same condition using bitmap indexes.
8. Applies an aggregate function to the filtered data.

### `Fakat`

Represents one row in the fact table.

A fact-table row contains identifiers that reference the dimension tables and a numerical measure used for aggregate calculations.

### `TableReader`

Reads dimension-table data from text files and converts each row into a key-value representation.

It is used to load:

```text
Student.txt
Indeks.txt
Ispit.txt
```

### `FakatTableGenerator`

Generates the fact table from the loaded dimension data.

The generator:

* Reads student, index and exam records
* Generates fact-table rows
* Stores generated rows in memory
* Produces up to 10,000 rows
* Saves the generated table to `TabelaFakata.txt`

### `BitMapIndexMaker`

Creates bitmap indexes for the fact-table dimensions.

Separate bitmap representations are created for:

* Student identifiers
* Index identifiers
* Exam identifiers

Each bitmap contains:

* `1` when a fact-table row contains the selected identifier
* `0` when it does not

For example:

```text
00101001
```

Each position represents one row in the fact table.

### `FakatTableReader`

Provides query and output functionality for the generated fact table.

It supports:

* Filtering without bitmap indexes
* Filtering using bitmap indexes
* Processing logical conditions
* Printing selected rows
* Executing aggregate functions

### `Pair`

A generic helper class used to store key-value pairs while reading dimension tables.

## Input Files

The project uses three text files as dimension tables.

### `Student.txt`

Contains student identifiers and student information.

### `Indeks.txt`

Contains index identifiers and index information.

### `Ispit.txt`

Contains exam identifiers and exam information.

These files must remain inside the `src` directory unless the file paths in `Main.java` are updated.

## Generated Fact Table

The application generates a fact table and saves it to:

```text
TabelaFakata.txt
```

The maximum number of generated rows is:

```text
10,000
```

The generated rows combine identifiers from the dimension tables with a measure that can later be filtered and aggregated.

## Query Conditions

Conditions are defined as strings in `Main.java`.

Example:

```java
String condition =
    "(studentID=4 AND indexID=5) OR (studentID=3 AND ispitID=6)";
```

The application supports conditions based on:

```text
studentID
indexID
ispitID
```

Logical operators can be used to combine expressions:

```text
AND
OR
```

Example conditions:

```text
studentID=4
```

```text
studentID=4 AND indexID=5
```

```text
studentID=4 OR ispitID=6
```

```text
(studentID=4 AND indexID=5) OR (studentID=3 AND ispitID=6)
```

## Query Execution

The application executes the same condition using two different approaches.

### Sequential Filtering

The program scans the fact table and checks every row against the requested condition.

```text
Condition
    │
    ▼
Scan every fact-table row
    │
    ▼
Evaluate identifiers
    │
    ▼
Return matching rows
```

### Bitmap-Index Filtering

The program uses the bitmap created for each requested identifier.

Logical operations can then be applied to the bitmap representations to determine which fact-table rows match the condition.

```text
Condition
    │
    ▼
Select relevant bitmaps
    │
    ▼
Apply AND or OR
    │
    ▼
Identify matching positions
    │
    ▼
Return fact-table rows
```

## Requirements

To compile and run the project, install:

* Java Development Kit
* IntelliJ IDEA or another Java IDE, optionally

The project uses only standard Java functionality and does not require Maven, Gradle or external dependencies.

## Cloning the Repository

```bash
git clone https://github.com/LauraGrego/BP2.git
cd BP2
cd Baze2
```

## Running in IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Select **Open**.
3. Open the `Baze2` directory.
4. Configure an installed JDK.
5. Open `src/Main.java`.
6. Run the `Main` class.

The results will be displayed in the Run console.

## Running from the Command Line

Navigate to the project directory:

```bash
cd BP2/Baze2
```

Compile the project:

```bash
javac -d out src/klase/*.java src/Main.java
```

Run the application:

```bash
java -cp out Main
```

Because the application uses paths such as:

```java
"src/Student.txt"
```

the program should be started from the `Baze2` directory.

## Example Workflow

```text
Student.txt ──┐
              │
Indeks.txt ───┼──► Fact Table Generator
              │            │
Ispit.txt ────┘            ▼
                    Generated Fact Table
                            │
                    ┌───────┴────────┐
                    │                │
                    ▼                ▼
             Sequential Scan   Bitmap Indexes
                    │                │
                    └───────┬────────┘
                            ▼
                     Filtered Rows
                            │
                            ▼
                 MIN / MAX / AVG / SUM / COUNT
```

## Changing the Query

To test a different query, change the `condition` variable in `Main.java`:

```java
String condition = "studentID=2 AND ispitID=4";
```

Then rebuild and run the project.

## Selecting an Aggregate Function

The supported aggregate-function names are:

```java
String[] aggregateFunctions = {
    "min",
    "max",
    "avg",
    "sum",
    "count"
};
```

The current implementation randomly selects one of these functions.

To execute a specific function, replace the random selection with a fixed value:

```java
tableReader.ispisTabeleSFunkcjiom("avg", filteredRows);
```

## Educational Purpose

This project was created for educational purposes to demonstrate:

* Fact tables
* Dimension tables
* Data warehouse concepts
* Bitmap indexes
* Low-cardinality indexing
* Query filtering
* Logical query conditions
* Aggregate functions
* File-based data processing
* Java collections
* Object-oriented application design

## Possible Improvements

Potential future improvements include:

* Accepting query conditions through the command line
* Creating an interactive query menu
* Measuring execution time with and without indexes
* Supporting dynamically sized bitmap collections
* Supporting additional comparison operators
* Improving expression parsing
* Adding validation for malformed conditions
* Adding unit tests
* Exporting query results to a file
* Adding Maven or Gradle configuration
* Removing IntelliJ-specific files from version control
* Adding generated data configuration options
