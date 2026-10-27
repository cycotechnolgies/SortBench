
package com.cyco.uni.sortbench.ui;

import com.cyco.uni.sortbench.analysis.ColumnDetector;
import com.cyco.uni.sortbench.csv.CSVReader;
import com.cyco.uni.sortbench.model.Dataset;
import com.cyco.uni.sortbench.model.SortResult;
import com.cyco.uni.sortbench.performance.PerformanceEvaluator;
import com.cyco.uni.sortbench.sorting.HeapSort;
import com.cyco.uni.sortbench.sorting.InsertionSort;
import com.cyco.uni.sortbench.sorting.MergeSort;
import com.cyco.uni.sortbench.sorting.QuickSort;
import com.cyco.uni.sortbench.sorting.ShellSort;
import com.cyco.uni.sortbench.sorting.SortingAlgorithm;
import com.cyco.uni.sortbench.validation.NumericValidator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {

    private final JTextField filePathField =
            new JTextField("No file selected");

    private final JComboBox<String> numericColumnCombo =
            new JComboBox<>();

    private final JTable previewTable = new JTable();
    private final JTable resultsTable = new JTable();

    private final JTextArea logArea = new JTextArea(5, 40);

    private final JLabel datasetStatus =
            new JLabel("Upload a CSV file to begin.");

    private final JLabel bestLabel =
            new JLabel("Best algorithm: Waiting for analysis");

    private final JButton browseButton =
            new JButton("Browse CSV");

    private final JButton analyzeButton =
            new JButton("Start Analysis");

    private final JCheckBox insertionCheck =
            new JCheckBox("Insertion Sort", true);

    private final JCheckBox shellCheck =
            new JCheckBox("Shell Sort", true);

    private final JCheckBox mergeCheck =
            new JCheckBox("Merge Sort", true);

    private final JCheckBox quickCheck =
            new JCheckBox("Quick Sort", true);

    private final JCheckBox heapCheck =
            new JCheckBox("Heap Sort", true);

    private final CSVReader csvReader = new CSVReader();
    private final ColumnDetector columnDetector = new ColumnDetector();
    private final NumericValidator numericValidator =
            new NumericValidator();
    private final PerformanceEvaluator performanceEvaluator =
            new PerformanceEvaluator();

    private Dataset dataset;

    private final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public MainFrame() {
        super("SortBench - Sorting Algorithm Performance Analyzer");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 850);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);

        buildUI();
        attachEvents();

        log("Application window initialized.");
        log("Upload a CSV file to begin.");
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel(
                "  SortBench - Sorting Algorithm Performance Analyzer");
        heading.setFont(heading.getFont().deriveFont(22f));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        add(heading, BorderLayout.NORTH);

        // LEFT SIDE: file, column and algorithm controls.
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(
                new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        filePathField.setEditable(false);

        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.setBorder(BorderFactory.createTitledBorder(
                "1. Upload CSV Dataset"));
        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(browseButton, BorderLayout.SOUTH);

        controlsPanel.add(filePanel);
        controlsPanel.add(Box.createVerticalStrut(12));

        JPanel columnPanel = new JPanel(new BorderLayout(5, 5));
        columnPanel.setBorder(BorderFactory.createTitledBorder(
                "2. Select Numeric Column"));
        columnPanel.add(
                new JLabel("Column to sort:"), BorderLayout.NORTH);
        columnPanel.add(numericColumnCombo, BorderLayout.CENTER);
        columnPanel.add(datasetStatus, BorderLayout.SOUTH);

        controlsPanel.add(columnPanel);
        controlsPanel.add(Box.createVerticalStrut(12));

        JPanel algorithmsPanel = new JPanel();
        algorithmsPanel.setLayout(
                new BoxLayout(algorithmsPanel, BoxLayout.Y_AXIS));
        algorithmsPanel.setBorder(BorderFactory.createTitledBorder(
                "3. Select Sorting Algorithms"));

        algorithmsPanel.add(insertionCheck);
        algorithmsPanel.add(shellCheck);
        algorithmsPanel.add(mergeCheck);
        algorithmsPanel.add(quickCheck);
        algorithmsPanel.add(heapCheck);

        analyzeButton.setAlignmentX(LEFT_ALIGNMENT);
        algorithmsPanel.add(Box.createVerticalStrut(12));
        algorithmsPanel.add(analyzeButton);

        controlsPanel.add(algorithmsPanel);
        controlsPanel.add(Box.createVerticalGlue());

        // RIGHT SIDE: preview and results.
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder(
                "Dataset Preview (first 100 rows)"));
        previewPanel.add(
                new JScrollPane(previewTable), BorderLayout.CENTER);

        JPanel resultsPanel = new JPanel(new BorderLayout(5, 5));
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
                "Sorting Performance Results"));
        resultsPanel.add(
                new JScrollPane(resultsTable), BorderLayout.CENTER);
        resultsPanel.add(bestLabel, BorderLayout.SOUTH);

        JSplitPane rightSplit = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, previewPanel, resultsPanel);
        rightSplit.setResizeWeight(0.58);

        JSplitPane mainSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(controlsPanel),
                rightSplit);
        mainSplit.setResizeWeight(0.30);

        add(mainSplit, BorderLayout.CENTER);

        // BOTTOM: log/messages.
        logArea.setEditable(false);
        logArea.setLineWrap(false);
        logArea.setFont(
                new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder(
                "Log / Messages"));
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        logPanel.setPreferredSize(new Dimension(900, 145));

        add(logPanel, BorderLayout.SOUTH);

        resetResultsTable();
    }

    private void attachEvents() {
        browseButton.addActionListener(event -> chooseCsvFile());
        analyzeButton.addActionListener(event -> startAnalysis());

        numericColumnCombo.addActionListener(event -> {
            Object selected = numericColumnCombo.getSelectedItem();

            if (selected != null) {
                log("Selected column: " + selected);
            }
        });
    }

    private void chooseCsvFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a CSV dataset");
        chooser.setFileFilter(
                new FileNameExtensionFilter("CSV files (*.csv)", "csv"));

        if (chooser.showOpenDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = chooser.getSelectedFile();

        try {
            log("Reading CSV: " + selectedFile.getName());

            Dataset loaded = csvReader.readCSV(selectedFile.toPath());

            if (loaded.getColumnCount() == 0) {
                throw new IllegalArgumentException(
                        "The CSV file has no columns.");
            }

            // Store the dataset only after it has loaded successfully.
            dataset = loaded;
            filePathField.setText(selectedFile.getAbsolutePath());

            // Populate the dropdown.
            List<String> numericColumns =
                    columnDetector.detectNumericColumns(dataset);

            numericColumnCombo.removeAllItems();

            for (String column : numericColumns) {
                numericColumnCombo.addItem(column);
            }

            // Display headers and rows in the preview.
            updatePreviewTable();

            // Reset previous analysis.
            resetResultsTable();

            datasetStatus.setText(
                    dataset.getRowCount() + " rows, "
                    + dataset.getColumnCount() + " columns");

            log("CSV loaded successfully.");
            log("Rows: " + dataset.getRowCount());
            log("Columns: " + dataset.getColumnCount());
            log("Numeric columns: " + numericColumns);

            if (numericColumns.isEmpty()) {
                log("No numeric columns were detected.");

                JOptionPane.showMessageDialog(
                        this,
                        "The file loaded, but no numeric columns were found.",
                        "No numeric columns",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                log("Preview updated successfully.");

                JOptionPane.showMessageDialog(
                        this,
                        "Dataset loaded successfully.\n"
                        + "Detected " + numericColumns.size()
                        + " numeric column(s).",
                        "CSV Loaded",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException | IllegalArgumentException ex) {
            dataset = null;
            numericColumnCombo.removeAllItems();

            previewTable.setModel(new DefaultTableModel());
            resetResultsTable();

            filePathField.setText("No file selected");
            datasetStatus.setText("Failed to load dataset");

            log("CSV loading failed: " + ex.getMessage());

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load the CSV file:\n" + ex.getMessage(),
                    "CSV Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePreviewTable() {
        if (dataset == null) {
            return;
        }

        String[] headers = dataset.getColumnNames()
                .toArray(new String[0]);

        DefaultTableModel model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int previewLimit = Math.min(dataset.getRowCount(), 100);

        for (int rowIndex = 0; rowIndex < previewLimit; rowIndex++) {
            List<String> row = dataset.getRows().get(rowIndex);

            model.addRow(row.toArray(new Object[0]));
        }

        previewTable.setModel(model);
        previewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        previewTable.setRowHeight(24);

        log("Preview: displaying " + previewLimit
                + " of " + dataset.getRowCount() + " rows.");
    }

    private void resetResultsTable() {
        String[] columns = {
            "Algorithm",
            "Execution Time (ms)",
            "Status"
        };

        Object[][] rows = {
            {"Insertion Sort", "—", "Waiting"},
            {"Shell Sort", "—", "Waiting"},
            {"Merge Sort", "—", "Waiting"},
            {"Quick Sort", "—", "Waiting"},
            {"Heap Sort", "—", "Waiting"}
        };

        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable.setModel(model);
        resultsTable.setRowHeight(25);

        bestLabel.setText("Best algorithm: Waiting for analysis");
    }

    private void startAnalysis() {
        if (dataset == null) {
            showWarning("Please upload a CSV file first.");
            return;
        }

        String selectedColumn =
                (String) numericColumnCombo.getSelectedItem();

        if (selectedColumn == null) {
            showWarning("Please select a numeric column.");
            return;
        }

        List<SortingAlgorithm> algorithms = new ArrayList<>();

        if (insertionCheck.isSelected()) {
            algorithms.add(new InsertionSort());
        }
        if (shellCheck.isSelected()) {
            algorithms.add(new ShellSort());
        }
        if (mergeCheck.isSelected()) {
            algorithms.add(new MergeSort());
        }
        if (quickCheck.isSelected()) {
            algorithms.add(new QuickSort());
        }
        if (heapCheck.isSelected()) {
            algorithms.add(new HeapSort());
        }

        if (algorithms.isEmpty()) {
            showWarning("Select at least one sorting algorithm.");
            return;
        }

        int columnIndex =
                dataset.getColumnNames().indexOf(selectedColumn);

        if (columnIndex < 0) {
            showWarning("The selected column was not found.");
            return;
        }

        // Extract numeric values from the selected column.
        List<Double> numbers = new ArrayList<>();

        for (int rowIndex = 0;
                rowIndex < dataset.getRowCount();
                rowIndex++) {

            String value = dataset.getValueAt(rowIndex, columnIndex);

            NumericValidator.Status status =
                    numericValidator.validate(value);

            if (status == NumericValidator.Status.VALID) {
                numbers.add(numericValidator.parseNumber(value));

            } else if (status == NumericValidator.Status.INVALID) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid numeric value at CSV row "
                        + (rowIndex + 2) + ": " + value,
                        "Invalid Data",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Empty cells are ignored.
        }

        if (numbers.isEmpty()) {
            showWarning(
                    "The selected column contains no numeric values.");
            return;
        }

        double[] input = new double[numbers.size()];

        for (int i = 0; i < numbers.size(); i++) {
            input[i] = numbers.get(i);
        }

        log("Starting analysis.");
        log("Selected column: " + selectedColumn);
        log("Numeric values: " + input.length);
        log("Algorithms: " + algorithms.stream()
                .map(SortingAlgorithm::getName)
                .toList());

        analyzeButton.setEnabled(false);
        browseButton.setEnabled(false);
        numericColumnCombo.setEnabled(false);
        setAlgorithmControlsEnabled(false);

        DefaultTableModel waitingModel =
                (DefaultTableModel) resultsTable.getModel();
        waitingModel.setRowCount(0);

        for (SortingAlgorithm algorithm : algorithms) {
            waitingModel.addRow(new Object[] {
                algorithm.getName(), "Running...", "In progress"
            });
        }

        // Benchmark runs off the Swing UI thread.
        SwingWorker<List<SortResult>, Void> worker =
                new SwingWorker<>() {

            @Override
            protected List<SortResult> doInBackground() {
                return performanceEvaluator.evaluate(input, algorithms);
            }

            @Override
            protected void done() {
                try {
                    List<SortResult> results = get();
                    displayResults(results);

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    log("Analysis interrupted.");

                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    String message = cause == null
                            ? ex.getMessage()
                            : cause.getMessage();

                    log("Analysis failed: " + message);

                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            "Analysis failed:\n" + message,
                            "Analysis Error",
                            JOptionPane.ERROR_MESSAGE);

                } finally {
                    analyzeButton.setEnabled(true);
                    browseButton.setEnabled(true);
                    numericColumnCombo.setEnabled(true);
                    setAlgorithmControlsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void displayResults(List<SortResult> results) {
        DefaultTableModel model =
                (DefaultTableModel) resultsTable.getModel();

        model.setRowCount(0);

        SortResult best = null;

        for (SortResult result : results) {
            String time = Double.isFinite(result.getExecutionTimeMs())
                    ? String.format(
                            Locale.US,
                            "%.6f",
                            result.getExecutionTimeMs())
                    : "—";

            model.addRow(new Object[] {
                result.getAlgorithmName(),
                time,
                result.getStatus()
            });

            log(result.getAlgorithmName() + ": "
                    + time + " ms - " + result.getStatus());

            if ("Success".equals(result.getStatus())
                    && (best == null
                    || result.getExecutionTimeMs()
                    < best.getExecutionTimeMs())) {
                best = result;
            }
        }

        if (best != null) {
            bestLabel.setText(
                    "Best algorithm: " + best.getAlgorithmName()
                    + " (" + String.format(
                            Locale.US,
                            "%.6f ms",
                            best.getExecutionTimeMs()) + ")");

            double[] sorted = best.getSortedData();

            if (sorted != null) {
                int count = Math.min(10, sorted.length);

                log("Sorted output preview (first " + count + " values): "
                        + Arrays.toString(
                                Arrays.copyOf(sorted, count)));
            }

            log("Best algorithm: " + best.getAlgorithmName());

        } else {
            bestLabel.setText(
                    "Best algorithm: No successful results");
        }

        log("Analysis completed.");
    }

    private void setAlgorithmControlsEnabled(boolean enabled) {
        insertionCheck.setEnabled(enabled);
        shellCheck.setEnabled(enabled);
        mergeCheck.setEnabled(enabled);
        quickCheck.setEnabled(enabled);
        heapCheck.setEnabled(enabled);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "SortBench",
                JOptionPane.WARNING_MESSAGE);
    }

    private void log(String message) {
        String timestamp = LocalTime.now().format(timeFormatter);

        logArea.append(
                "[" + timestamp + "] " + message
                + System.lineSeparator());

        logArea.setCaretPosition(
                logArea.getDocument().getLength());
    }
}
