package com.sortbench.csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.cyco.uni.sortbench.csv.CSVReader;
import com.cyco.uni.sortbench.model.Dataset;

class CSVReaderTest {

    @TempDir
    Path tempDir;

    private final CSVReader csvReader = new CSVReader();

    @Test
    void readsHeadersAndRows() throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.writeString(file, "ID,Name,Marks\n1,Amara,85\n2,Nimal,72\n");

        Dataset dataset = csvReader.readCSV(file);

        assertEquals(3, dataset.getColumnCount());
        assertEquals(2, dataset.getRowCount());
        assertEquals("Name", dataset.getColumnNames().get(1));
        assertEquals("Amara", dataset.getValueAt(0, 1));
        assertEquals("72", dataset.getValueAt(1, 2));
    }

    @Test
    void removesUtf8BomFromFirstHeader() throws IOException {
        Path file = tempDir.resolve("bom.csv");
        Files.writeString(file, "\uFEFFID,Name\n1,Amara\n");

        Dataset dataset = csvReader.readCSV(file);

        assertEquals("ID", dataset.getColumnNames().get(0));
    }


    @Test
    void readsQuotedFieldsContainingCommas() throws IOException {
        Path file = tempDir.resolve("quoted.csv");
        Files.writeString(file, "ID,Name\n1,\"Amara, Silva\"\n");

        Dataset dataset = csvReader.readCSV(file);

        assertEquals("Amara, Silva", dataset.getValueAt(0, 1));
    }

    @Test
    void readsEscapedQuotes() throws IOException {
        Path file = tempDir.resolve("quotes.csv");
        Files.writeString(file, "ID,Comment\n1,\"She said \"\"Hello\"\"\"\n");

        Dataset dataset = csvReader.readCSV(file);

        assertEquals("She said \"Hello\"", dataset.getValueAt(0, 1));
    }

    @Test
    void preservesEmptyCells() throws IOException {
        Path file = tempDir.resolve("empty-cell.csv");
        Files.writeString(file, "ID,Name,Marks\n1,,85\n");

        Dataset dataset = csvReader.readCSV(file);

        assertEquals("", dataset.getValueAt(0, 1));
        assertEquals("85", dataset.getValueAt(0, 2));
    }

    @Test
    void rejectsRowsWithIncorrectColumnCounts() throws IOException {
        Path file = tempDir.resolve("invalid.csv");
        Files.writeString(file, "ID,Name,Marks\n1,Amara\n");

        assertThrows(IOException.class, () -> csvReader.readCSV(file));
    }

    @Test
    void preservesTrailingEmptyCells() throws IOException {
        Path file = tempDir.resolve("trailing-empty-cell.csv");
        Files.writeString(file, "ID,Name,Marks\n1,Amara,\n");

        Dataset dataset = csvReader.readCSV(file);

        assertEquals("", dataset.getValueAt(0, 2));
    }

     @Test
    void rejectsBlankLinesThatDoNotMatchHeaderWidth() throws IOException {
        Path file = tempDir.resolve("blank-line.csv");
        Files.writeString(file, "ID,Name\n\n");

        assertThrows(IOException.class, () -> csvReader.readCSV(file));
    }

    @Test
    void rejectsUnclosedQuotedFields() throws IOException {
        Path file = tempDir.resolve("unclosed.csv");
        Files.writeString(file, "ID,Name\n1,\"Amara\n");

        assertThrows(IOException.class, () -> csvReader.readCSV(file));
    }

    @Test
    void rejectsEmptyFiles() throws IOException {
        Path file = tempDir.resolve("empty.csv");
        Files.writeString(file, "");

        assertThrows(IOException.class, () -> csvReader.readCSV(file));
    }

    @Test
    void rejectsNonexistentFiles() {
        Path file = tempDir.resolve("missing.csv");

        assertThrows(IOException.class, () -> csvReader.readCSV(file));
    }


    @Test
    void readsQuotedFieldsContainingLineBreaks() throws IOException {
        Path file = tempDir.resolve("multiline.csv");
        Files.writeString(file,
                "ID,Comment\n1,\"First line\nSecond line\"\n");

        Dataset dataset = csvReader.readCSV(file);

        assertEquals("First line\nSecond line",
                dataset.getValueAt(0, 1));
    }


             @Test
        void readsTheRealUniversityDataset() throws IOException {
        Path datasetPath = Paths.get(
                "datasets",
                "global_university_students_performance_habits_10000.csv");

        Dataset dataset = csvReader.readCSV(datasetPath);

        assertEquals(10000, dataset.getRowCount(),
                "Dataset should contain 10,000 data rows.");
        assertEquals(27, dataset.getColumnCount(),
                "Dataset should contain 27 columns.");
        assertEquals("student_id", dataset.getColumnNames().get(0));
        assertEquals("assignment_score",
                dataset.getColumnNames().get(26));
    }



}
