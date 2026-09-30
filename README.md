# SortBench – Sorting Algorithm Performance Analyzer

SortBench is a Java desktop application designed to analyze and compare the performance of different sorting algorithms using numerical data from CSV files.

The application allows users to upload a CSV dataset, select a numerical column, run multiple sorting algorithms, measure their execution times, and identify the fastest-performing algorithm for the selected dataset.

---

## Features

* Upload CSV datasets
* Automatically read and display CSV columns
* Preview dataset records
* Select a numerical column for sorting
* Implement multiple sorting algorithms:

  * Insertion Sort
  * Shell Sort
  * Merge Sort
  * Quick Sort
  * Heap Sort
* Measure sorting execution time
* Compare algorithm performance
* Identify the shortest execution time
* Graphical User Interface using Java Swing
* Input validation and error handling
* Unit testing using JUnit 5
* Maven-based project management

---

## Sorting Algorithms

SortBench currently implements the following algorithms:

| Algorithm      | General Approach                                |
| -------------- | ----------------------------------------------- |
| Insertion Sort | Builds the sorted portion one element at a time |
| Shell Sort     | Performs insertion sorting with decreasing gaps |
| Merge Sort     | Divide-and-conquer sorting using merging        |
| Quick Sort     | Divide-and-conquer sorting using a pivot        |
| Heap Sort      | Uses a binary heap to perform sorting           |

The application measures the actual execution time of each algorithm using the same input data so that their performance can be compared.

---

## Technology Stack

| Technology                      | Purpose                                          |
| ------------------------------- | ------------------------------------------------ |
| Java 17                         | Main programming language                        |
| Java Swing                      | Graphical User Interface                         |
| Apache Maven                    | Build and dependency management                  |
| JUnit 5                         | Unit testing                                     |
| Git                             | Version control                                  |
| GitHub                          | Source code collaboration and repository hosting |
| Eclipse IDE for Java Developers | Development environment                          |

### Download Links

* **Java JDK 17:** [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/?utm_source=chatgpt.com)
* **Eclipse IDE for Java Developers:** [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/packages/release/2026-06/r/eclipse-ide-java-developers?utm_source=chatgpt.com)
* **Apache Maven:** [Apache Maven Downloads](https://maven.apache.org/download.cgi?source=post_page-----20eefce259ea---------------------------------------&utm_source=chatgpt.com)
* **Git:** [Git Downloads](https://git-scm.com/downloads?utm_source=chatgpt.com)
* **GitHub:** [GitHub](https://github.com/?utm_source=chatgpt.com)
* **JUnit 5:** [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/?utm_source=chatgpt.com)

Eclipse IDE for Java Developers provides Java development tools along with Git and Maven integration.

---

## Recommended Development Environment

For consistent development across the project, use:

```text
Java JDK        : 17
IDE             : Eclipse IDE for Java Developers
Build Tool      : Apache Maven
Testing         : JUnit 5
Version Control : Git
Repository      : GitHub
GUI Framework   : Java Swing
```

All developers should use Java 17 to avoid compatibility differences between development environments.

---

## Project Architecture

The project follows a simple layered structure:

```text
CSV Input
   │
   ▼
CSV Reader
   │
   ▼
Dataset Model
   │
   ▼
Column Selection
   │
   ▼
Sorting Algorithms
   │
   ├── Insertion Sort
   ├── Shell Sort
   ├── Merge Sort
   ├── Quick Sort
   └── Heap Sort
   │
   ▼
Performance Evaluator
   │
   ▼
Performance Results
   │
   ▼
Swing GUI
```

---

## Project Structure

```text
SortBench/
│
├── pom.xml
├── README.md
├── LICENSE
├── .gitignore
│
├── datasets/
│   └── sample_students.csv
│
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── sortbench/
    │               │
    │               ├── Main.java
    │               │
    │               ├── csv/
    │               │   └── CSVReader.java
    │               │
    │               ├── model/
    │               │   ├── Dataset.java
    │               │   └── SortResult.java
    │               │
    │               ├── performance/
    │               │   └── PerformanceEvaluator.java
    │               │
    │               ├── sorting/
    │               │   ├── SortingAlgorithm.java
    │               │   ├── InsertionSort.java
    │               │   ├── ShellSort.java
    │               │   ├── MergeSort.java
    │               │   ├── QuickSort.java
    │               │   └── HeapSort.java
    │               │
    │               └── ui/
    │                   └── MainFrame.java
    │
    └── test/
        └── java/
            └── com/
                └── sortbench/
                    └── sorting/
                        └── SortingAlgorithmTest.java
```

---

## Requirements

Before running the project, install:

1. Java JDK 17
2. Eclipse IDE for Java Developers
3. Apache Maven
4. Git

Verify the installations using:

```bash
java -version
mvn -version
git --version
```

---

## Installation and Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
```

Navigate into the project:

```bash
cd SortBench
```

### 2. Import into Eclipse

Open Eclipse and select:

```text
File
→ Import
→ Maven
→ Existing Maven Projects
```

Select the `SortBench` project directory and finish the import.

Eclipse should automatically recognize the Maven configuration from `pom.xml`.

### 3. Build the Project

From the project directory:

```bash
mvn clean install
```

---

## Running the Application

The application can be launched from Eclipse by running:

```text
Main.java
```

Alternatively, Maven can be used to run the application if the project is configured with the Maven Exec plugin.

---

## Application Workflow

The general application workflow is:

```text
1. Launch SortBench
        ↓
2. Upload CSV File
        ↓
3. Load Dataset
        ↓
4. Display Dataset Preview
        ↓
5. Select Numerical Column
        ↓
6. Select Sorting Algorithms
        ↓
7. Start Performance Analysis
        ↓
8. Execute Sorting Algorithms
        ↓
9. Measure Execution Times
        ↓
10. Display Performance Results
```

---

## User Interface

The planned interface contains the following sections:

---

<img width="1312" height="1199" alt="ChatGPT Image Sep 29, 2026, 03_06_19 PM" src="https://github.com/user-attachments/assets/c39eba57-307e-4a7a-b812-469dc7483901" />

---

## Performance Measurement

Each sorting algorithm receives the same input dataset to provide a fair performance comparison.

Execution time is measured before and after the sorting operation using Java's timing facilities.

Conceptually:

```text
Start Timer
    ↓
Execute Sorting Algorithm
    ↓
Stop Timer
    ↓
Calculate Execution Time
    ↓
Store Result
    ↓
Display Result
```

The results are displayed in the GUI for comparison.

---

## Testing

JUnit 5 is used for testing the sorting algorithms and core functionality.

Tests should cover cases such as:

* Random numerical data
* Already sorted data
* Reverse-sorted data
* Duplicate values
* Single-element arrays
* Empty arrays
* Large datasets
* Negative numbers

Run the test suite using:

```bash
mvn test
```

---

## Error Handling

The application should handle invalid input gracefully.

Examples include:

* Invalid CSV file
* Empty CSV file
* CSV without valid numerical columns
* No column selected
* Invalid numerical values
* Empty dataset
* Unsupported file format
* Sorting operation without loaded data

Instead of terminating unexpectedly, the application should display a meaningful error message to the user.

---

## Git Workflow

The project uses Git and GitHub for source-code management.

Recommended branch structure:

```text
main
  │
  └── develop
       │
       ├── feature/member1-csv
       ├── feature/member2-sorting
       └── feature/member3-gui
```

### Main Branches

**main**

Contains stable versions of the project.

**develop**

Used for integrating completed features before merging them into `main`.

### Feature Branches

Create a separate branch for each major feature.

Example:

```bash
git checkout develop
git pull origin develop
git checkout -b feature/member1-csv
```

After completing the work:

```bash
git add .
git commit -m "feat: implement CSV reader"
git push origin feature/member1-csv
```

Create a Pull Request and merge the feature into `develop`.

---

## Commit Convention

Use clear and descriptive commit messages.

Examples:

```text
feat: implement CSV reader
feat: add merge sort algorithm
feat: create sorting performance evaluator
feat: implement dataset preview
fix: handle empty CSV files
test: add sorting algorithm tests
refactor: improve sorting interface
docs: update README
```

---

## Development Guidelines

* Follow Java naming conventions.
* Keep classes focused on a single responsibility.
* Avoid unnecessary duplicated code.
* Write meaningful method and variable names.
* Add comments where the logic is not immediately obvious.
* Test new functionality before merging.
* Pull the latest `develop` branch before starting new work.
* Avoid directly pushing unfinished features to `main`.
* Keep commits small and meaningful.

---

## Future Improvements

Potential future improvements include:

* Graphical performance charts
* Additional sorting algorithms
* Dataset statistics
* Multiple-column sorting
* Exporting performance results
* Larger dataset benchmarking
* Configurable benchmark repetitions
* Algorithm complexity visualization
* Dark mode
* Improved dataset filtering and analysis

---

## License

This project is licensed under the **MIT License**.

See the [LICENSE](LICENSE) file for the complete license text.

---

## Project Status

SortBench is an actively developed Java desktop application for sorting algorithm performance analysis.

The project architecture is designed to allow individual components to be developed and tested independently while maintaining a clean and maintainable codebase.
