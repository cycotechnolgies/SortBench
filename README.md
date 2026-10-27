# SortBench – Sorting Algorithm Performance Analyzer

SortBench is a Java desktop application designed to analyze and compare the performance of different sorting algorithms using numerical data from CSV files.

The application allows users to upload a CSV dataset, select a numerical column, run multiple sorting algorithms, measure their execution times, and compare their performance.

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

| Algorithm      | General Approach                                 |
| -------------- | ------------------------------------------------ |
| Insertion Sort | Builds the sorted portion one element at a time  |
| Shell Sort     | Performs insertion sorting using decreasing gaps |
| Merge Sort     | Divide-and-conquer sorting using merging         |
| Quick Sort     | Divide-and-conquer sorting using a pivot         |
| Heap Sort      | Uses a binary heap to perform sorting            |

Each algorithm receives the same input data so that their execution performance can be compared consistently.

---

## Technology Stack

| Technology      | Purpose                                          |
| --------------- | ------------------------------------------------ |
| Java 25 LTS     | Main programming language                        |
| Java Swing      | Graphical User Interface                         |
| Apache Maven    | Build and dependency management                  |
| JUnit 5         | Unit testing                                     |
| Git             | Version control                                  |
| GitHub          | Source code collaboration and repository hosting |
| Apache NetBeans | Development environment                          |

### Download Links

* **Java JDK 25:** [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/?utm_source=chatgpt.com)
* **Apache NetBeans:** [Apache NetBeans Downloads](https://netbeans.apache.org/front/main/download/?utm_source=chatgpt.com)
* **Apache Maven:** [Apache Maven Downloads](https://maven.apache.org/download.cgi?utm_source=chatgpt.com)
* **Git:** [Git Downloads](https://git-scm.com/downloads?utm_source=chatgpt.com)
* **GitHub:** [GitHub](https://github.com/?utm_source=chatgpt.com)
* **JUnit 5:** [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/?utm_source=chatgpt.com)

---

## Recommended Development Environment

All developers should use the same Java version and development tools to avoid compatibility issues.

```text
Java JDK        : 25 LTS
IDE             : Apache NetBeans
Build Tool      : Apache Maven
Testing         : JUnit 5
Version Control : Git
Repository      : GitHub
GUI Framework   : Java Swing
```

### Java Version

The project uses **Java 25 LTS**.

Java 25 is an LTS release, making it suitable for a new project that requires a stable and supported Java version.

Check the installed Java version:

```bash
java -version
```

The output should indicate Java 25.

---

## Project Architecture

The project follows a simple layered architecture:

<img width="912" height="367" alt="Untitled Diagram" src="https://github.com/user-attachments/assets/fdc0b8ca-86e8-46ac-aa21-c1f5840a97e4" />

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

1. Java JDK 25 LTS
2. Apache NetBeans
3. Apache Maven
4. Git
5. GitHub account

Verify the installations:

```bash
java -version
mvn -version
git --version
```

The Java version should report **Java 25**.

---

## Installation and Setup

### 1. Clone the Repository

Clone the project from GitHub:

```bash
git clone <repository-url>
```

Navigate into the project:

```bash
cd SortBench
```

---

### 2. Open the Project in Apache NetBeans

Open Apache NetBeans and select:

```text
File
→ Open Project
```

Select the `SortBench` project directory.

Because SortBench is a Maven project, NetBeans will recognize the `pom.xml` configuration.

---

### 3. Build the Project

From the project directory:

```bash
mvn clean install
```

Alternatively, the project can be built directly through Apache NetBeans.

---

## Running the Application

From Apache NetBeans, run:

```text
Main.java
```

The SortBench graphical user interface will open.

---

## Application Workflow

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

<img width="1366" height="702" alt="image" src="https://github.com/user-attachments/assets/0b75e8ba-2408-437a-85d1-d0b623eb3e23" />

---

## Performance Measurement

Each sorting algorithm receives the same input dataset.

The application measures the time required to complete each sorting operation.

The general process is:

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

The measured results are displayed in the GUI for comparison.

---

## Testing

JUnit 5 is used to test the sorting algorithms and core functionality.

Tests should cover:

* Random numerical data
* Already sorted data
* Reverse-sorted data
* Duplicate values
* Single-element arrays
* Empty arrays
* Large datasets
* Negative numbers

Run the test suite:

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

The application should display meaningful error messages instead of terminating unexpectedly.

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

### Main Branch

`main` contains stable versions of the project.

### Develop Branch

`develop` is used to integrate completed features before they are merged into `main`.

### Feature Branches

Each major feature should be developed using a separate feature branch.

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

Then create a Pull Request and merge the feature into `develop`.

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

* Use Java 25 LTS.
* Follow standard Java naming conventions.
* Keep classes focused on a single responsibility.
* Avoid unnecessary duplicated code.
* Use meaningful method and variable names.
* Add comments where the logic is not immediately obvious.
* Test new functionality before merging.
* Pull the latest `develop` branch before starting new work.
* Do not directly push unfinished features to `main`.
* Keep commits small and meaningful.
* Maintain the existing project structure.
* Ensure all team members use the same Java version.

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

See the `LICENSE` file for the complete license text.

---

## Project Status

SortBench is an actively developed Java desktop application for sorting algorithm performance analysis.

The project architecture is designed to allow individual components to be developed and tested independently while maintaining a clean and maintainable codebase.
