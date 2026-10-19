package com.cyco.uni.sortbench.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.cyco.uni.sortbench.model.Dataset;

/**
 * Reads CSV files and converts their contents into Dataset objects.
 * Field values are preserved as strings for separate numeric validation.
 * @author RMCN Rathnayaka
 */
public class CSVReader {

    /**
     * Reads a CSV file using its first record as the column headers.
     */
    public Dataset readCSV(Path filePath) throws IOException {
        Objects.requireNonNull(filePath, "File path cannot be null.");

        if (!Files.isRegularFile(filePath)) {
            throw new IOException(
                    "File does not exist or is not a regular file: " + filePath);
        }

        List<List<String>> records;

        try (BufferedReader bufferedReader = Files.newBufferedReader(
                    filePath, StandardCharsets.UTF_8);
             PushbackReader reader = new PushbackReader(bufferedReader, 1)) {
            records = parseRecords(reader);
        }

        if (records.isEmpty()) {
            throw new IOException("CSV file is empty.");
        }

        List<String> headers = new ArrayList<>(records.get(0));

        // Remove a UTF-8 BOM if it appears at the start of the first header.
        if (!headers.isEmpty() && !headers.get(0).isEmpty()
                && headers.get(0).charAt(0) == '\uFEFF') {
            headers.set(0, headers.get(0).substring(1));
        }

        List<List<String>> rows = new ArrayList<>(
                records.subList(1, records.size()));

        try {
            return new Dataset(headers, rows);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new IOException(
                    "Invalid CSV structure: " + exception.getMessage(),
                    exception);
        }
    }

    /**
     * Parses CSV records, supporting quoted commas, escaped quotes,
     * and line breaks inside quoted fields.
     */
    private List<List<String>> parseRecords(PushbackReader reader)
            throws IOException {

        List<List<String>> records = new ArrayList<>();
        List<String> record = new ArrayList<>();
        StringBuilder field = new StringBuilder();

        boolean insideQuotes = false;
        boolean afterClosingQuote = false;
        boolean fieldStarted = false;
        boolean recordStarted = false;

        int value;

        while ((value = reader.read()) != -1) {
            char current = (char) value;

            if (insideQuotes) {
                if (current == '"') {
                    int next = reader.read();

                    if (next == '"') {
                        field.append('"');
                    } else {
                        insideQuotes = false;
                        afterClosingQuote = true;

                        if (next != -1) {
                            reader.unread(next);
                        }
                    }
                } else {
                    field.append(current);
                }
                continue;
            }

            if (afterClosingQuote
                    && current != ','
                    && current != '\r'
                    && current != '\n') {
                throw new IOException(
                        "Unexpected character after a closing quote.");
            }

            if (current == '"') {
                if (fieldStarted || field.length() > 0) {
                    throw new IOException(
                            "Unexpected quote inside an unquoted field.");
                }

                insideQuotes = true;
                fieldStarted = true;
                recordStarted = true;

            } else if (current == ',') {
                record.add(field.toString());
                field.setLength(0);

                fieldStarted = false;
                afterClosingQuote = false;
                recordStarted = true;

            } else if (current == '\r' || current == '\n') {
                if (current == '\r') {
                    int next = reader.read();

                    if (next != '\n' && next != -1) {
                        reader.unread(next);
                    }
                }

                record.add(field.toString());
                records.add(record);

                record = new ArrayList<>();
                field.setLength(0);
                fieldStarted = false;
                afterClosingQuote = false;
                recordStarted = false;

            } else {
                field.append(current);
                fieldStarted = true;
                recordStarted = true;
            }
        }

        if (insideQuotes) {
            throw new IOException(
                    "CSV file ends before a quoted field is closed.");
        }

        if (recordStarted || !record.isEmpty()
                || field.length() > 0 || afterClosingQuote) {
            record.add(field.toString());
            records.add(record);
        }

        return records;
    }
}
