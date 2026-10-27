
package com.cyco.uni.sortbench.ui;

import com.cyco.uni.sortbench.analysis.ColumnDetector;
import com.cyco.uni.sortbench.csv.CSVReader;
import com.cyco.uni.sortbench.model.Dataset;
import com.cyco.uni.sortbench.model.SortResult;
import com.cyco.uni.sortbench.performance.PerformanceEvaluator;
import com.cyco.uni.sortbench.sorting.*;
import com.cyco.uni.sortbench.validation.NumericValidator;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainFrame extends JFrame {

    // ==================== Theme ====================

    private static final Color BLUE =
            new Color(37, 99, 235);

    private static final Color DARK_BLUE =
            new Color(30, 64, 120);

    private static final Color LIGHT_BLUE =
            new Color(239, 246, 255);

    private static final Color BACKGROUND =
            new Color(246, 248, 252);

    private static final Color WHITE = Color.WHITE;

    private static final Color BORDER =
            new Color(222, 229, 239);

    private static final Color TEXT =
            new Color(31, 41, 55);

    private static final Color MUTED =
            new Color(107, 114, 128);

    private static final Color GREEN =
            new Color(22, 163, 74);

    private static final Color LIGHT_GREEN =
            new Color(220, 252, 231);

    // ==================== Components ====================

    private final JTextField filePathField =
            new JTextField("No file selected");

    private final JComboBox<String> numericColumnCombo =
            new JComboBox<>();

    private final JTable previewTable = new JTable();

    private final JTextArea logArea = new JTextArea();

    private final JLabel datasetStatus =
            new JLabel("Upload a CSV file to begin.");

    private final JLabel bestLabel =
            new JLabel("Waiting for analysis");

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

    private final PerformanceChart performanceChart =
            new PerformanceChart();

    // ==================== Services ====================

    private final CSVReader csvReader = new CSVReader();

    private final ColumnDetector columnDetector =
            new ColumnDetector();

    private final NumericValidator numericValidator =
            new NumericValidator();

    private final PerformanceEvaluator performanceEvaluator =
            new PerformanceEvaluator();

    private Dataset dataset;

    private final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    // ==================== Constructor ====================

    public MainFrame() {
        super("SortBench | Sorting Performance Analyzer");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        getContentPane().setBackground(BACKGROUND);

        buildUI();
        attachEvents();

        log("Application window initialized.");
        log("Upload a CSV file to begin.");
    }

    // ==================== Main UI ====================

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BLUE);
        header.setBorder(new EmptyBorder(12, 25, 12, 25));

        JLabel title = new JLabel("SortBench");
        title.setForeground(WHITE);
        title.setFont(new Font(
                "SansSerif", Font.BOLD, 25));

        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // Main content
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel sidebar = buildSidebar();
        JPanel workspace = buildWorkspace();

        JScrollPane sidebarScroll = new JScrollPane(sidebar);
        sidebarScroll.setBorder(null);
        sidebarScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(16);
        sidebarScroll.getViewport().setBackground(WHITE);

        JSplitPane mainSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                sidebarScroll,
                workspace);

        mainSplit.setDividerLocation(320);
        mainSplit.setResizeWeight(0);
        mainSplit.setDividerSize(6);
        mainSplit.setBorder(null);
        mainSplit.setBackground(BACKGROUND);

        content.add(mainSplit, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        // Compact activity log
        JPanel logPanel = new JPanel(
                new BorderLayout(8, 5));

        logPanel.setBackground(WHITE);
        logPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(6, 14, 6, 14)));

        JLabel logTitle = new JLabel("Activity Log");
        logTitle.setFont(new Font(
                "SansSerif", Font.BOLD, 12));
        logTitle.setForeground(TEXT);

        logArea.setEditable(false);
        logArea.setRows(3);
        logArea.setBackground(new Color(249, 250, 252));
        logArea.setForeground(new Color(55, 65, 81));
        logArea.setFont(new Font(
                "Monospaced", Font.PLAIN, 10));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(new EmptyBorder(6, 8, 6, 8));

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(new LineBorder(BORDER));
        logScroll.setPreferredSize(
                new Dimension(800, 65));
        logScroll.setMinimumSize(
                new Dimension(400, 55));

        logPanel.add(logTitle, BorderLayout.NORTH);
        logPanel.add(logScroll, BorderLayout.CENTER);

        add(logPanel, BorderLayout.SOUTH);
    }

    // ==================== Sidebar ====================

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();

        sidebar.setBackground(WHITE);
        sidebar.setLayout(new BoxLayout(
                sidebar, BoxLayout.Y_AXIS));

        sidebar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(14, 14, 14, 14)));

        sidebar.setPreferredSize(new Dimension(290, 600));

        JLabel heading = new JLabel("Analysis Setup");
        heading.setFont(new Font(
                "SansSerif", Font.BOLD, 18));
        heading.setForeground(TEXT);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Configure your benchmark");

        subtitle.setFont(new Font(
                "SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(heading);
        sidebar.add(Box.createVerticalStrut(3));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(18));

        // Dataset section
        JPanel fileCard = createSection("01  Dataset");

        styleTextField(filePathField);
        styleButton(browseButton);

        browseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        browseButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 36));

        fileCard.add(filePathField);
        fileCard.add(Box.createVerticalStrut(8));
        fileCard.add(browseButton);

        sidebar.add(fileCard);
        sidebar.add(Box.createVerticalStrut(16));

        // Numeric column section
        JPanel columnCard =
                createSection("02  Numeric Column");

        JLabel columnHint = new JLabel("Column to sort");
        columnHint.setFont(new Font(
                "SansSerif", Font.PLAIN, 12));
        columnHint.setForeground(MUTED);

        styleComboBox(numericColumnCombo);

        numericColumnCombo.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 34));
        numericColumnCombo.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        datasetStatus.setFont(new Font(
                "SansSerif", Font.PLAIN, 11));
        datasetStatus.setForeground(MUTED);
        datasetStatus.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        columnCard.add(columnHint);
        columnCard.add(Box.createVerticalStrut(6));
        columnCard.add(numericColumnCombo);
        columnCard.add(Box.createVerticalStrut(7));
        columnCard.add(datasetStatus);

        sidebar.add(columnCard);
        sidebar.add(Box.createVerticalStrut(16));

        // Algorithm section
        JPanel algorithmCard =
                createSection("03  Algorithms");

        JCheckBox[] checks = {
            insertionCheck,
            shellCheck,
            mergeCheck,
            quickCheck,
            heapCheck
        };

        for (JCheckBox check : checks) {
            styleCheckBox(check);
            algorithmCard.add(check);
            algorithmCard.add(Box.createVerticalStrut(8));
        }

        styleButton(analyzeButton);

        analyzeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        analyzeButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42));

        algorithmCard.add(Box.createVerticalStrut(8));
        algorithmCard.add(analyzeButton);

        sidebar.add(algorithmCard);

        return sidebar;
    }

    // ==================== Workspace ====================

    private JPanel buildWorkspace() {
        JPanel workspace = new JPanel(
                new BorderLayout(0, 10));

        workspace.setBackground(BACKGROUND);

        // Dataset preview card
        JPanel previewCard = new JPanel(
                new BorderLayout(0, 8));

        styleCard(previewCard);

        JPanel previewHeader = new JPanel(
                new BorderLayout());

        previewHeader.setOpaque(false);

        JLabel previewTitle =
                new JLabel("Dataset Preview");

        previewTitle.setFont(new Font(
                "SansSerif", Font.BOLD, 16));
        previewTitle.setForeground(TEXT);

        JLabel previewInfo =
                new JLabel("First 100 rows");

        previewInfo.setFont(new Font(
                "SansSerif", Font.PLAIN, 11));
        previewInfo.setForeground(MUTED);

        previewHeader.add(
                previewTitle, BorderLayout.WEST);

        previewHeader.add(
                previewInfo, BorderLayout.EAST);

        styleTable(previewTable);
        previewTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF);

        JScrollPane previewScroll =
                new JScrollPane(previewTable);

        styleScrollPane(previewScroll);

        previewCard.add(
                previewHeader, BorderLayout.NORTH);

        previewCard.add(
                previewScroll, BorderLayout.CENTER);

        // Benchmark card
        JPanel resultsCard = new JPanel(
                new BorderLayout(0, 8));

        styleCard(resultsCard);

        JPanel resultsHeader = new JPanel(
                new BorderLayout());

        resultsHeader.setOpaque(false);

        JLabel resultsTitle =
                new JLabel("Benchmark Results");

        resultsTitle.setFont(new Font(
                "SansSerif", Font.BOLD, 16));
        resultsTitle.setForeground(TEXT);

        JLabel resultsInfo =
                new JLabel("Lower execution time is better");

        resultsInfo.setFont(new Font(
                "SansSerif", Font.PLAIN, 11));
        resultsInfo.setForeground(MUTED);

        resultsHeader.add(
                resultsTitle, BorderLayout.WEST);

        resultsHeader.add(
                resultsInfo, BorderLayout.EAST);

        // Chart heading and legend
        JPanel chartHeading = new JPanel(
                new BorderLayout());

        chartHeading.setOpaque(false);

        JPanel chartTitles = new JPanel();
        chartTitles.setOpaque(false);
        chartTitles.setLayout(new BoxLayout(
                chartTitles, BoxLayout.Y_AXIS));

        JLabel chartTitle =
                new JLabel("Algorithm Performance");

        chartTitle.setFont(new Font(
                "SansSerif", Font.BOLD, 13));
        chartTitle.setForeground(TEXT);

        JLabel chartSubtitle =
                new JLabel("Execution time in milliseconds");

        chartSubtitle.setFont(new Font(
                "SansSerif", Font.PLAIN, 11));
        chartSubtitle.setForeground(MUTED);

        chartTitles.add(chartTitle);
        chartTitles.add(Box.createVerticalStrut(3));
        chartTitles.add(chartSubtitle);

        JPanel legend = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0));

        legend.setOpaque(false);
        legend.add(createLegendItem(BLUE, "Algorithm"));
        legend.add(createLegendItem(GREEN, "Fastest"));

        chartHeading.add(
                chartTitles, BorderLayout.WEST);

        chartHeading.add(
                legend, BorderLayout.EAST);

        JPanel chartContainer = new JPanel(
                new BorderLayout(0, 6));

        chartContainer.setBackground(WHITE);
        chartContainer.add(
                chartHeading, BorderLayout.NORTH);

        chartContainer.add(
                performanceChart, BorderLayout.CENTER);

        // Larger green fastest-algorithm highlight
        bestLabel.setFont(new Font(
                "SansSerif", Font.BOLD, 16));

        bestLabel.setForeground(
                new Color(21, 128, 61));

        bestLabel.setOpaque(true);
        bestLabel.setBackground(LIGHT_GREEN);

        bestLabel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(
                        new Color(134, 239, 172), 1, true),
                new EmptyBorder(10, 12, 10, 12)));

        resultsCard.add(
                resultsHeader, BorderLayout.NORTH);

        resultsCard.add(
                chartContainer, BorderLayout.CENTER);

        resultsCard.add(
                bestLabel, BorderLayout.SOUTH);

        // Split preview and chart vertically
        JSplitPane verticalSplit = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                previewCard,
                resultsCard);

        verticalSplit.setResizeWeight(0.40);
        verticalSplit.setDividerLocation(200);
        verticalSplit.setDividerSize(6);
        verticalSplit.setBorder(null);
        verticalSplit.setBackground(BACKGROUND);

        workspace.add(
                verticalSplit, BorderLayout.CENTER);

        return workspace;
    }

    private JPanel createLegendItem(
            Color color, String text) {

        JPanel item = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 4, 0));

        item.setOpaque(false);

        JPanel swatch = new JPanel();
        swatch.setBackground(color);
        swatch.setPreferredSize(
                new Dimension(10, 10));

        JLabel label = new JLabel(text);
        label.setFont(new Font(
                "SansSerif", Font.PLAIN, 10));
        label.setForeground(MUTED);

        item.add(swatch);
        item.add(label);

        return item;
    }

    private JPanel createSection(String title) {
        JPanel section = new JPanel();

        section.setOpaque(false);
        section.setLayout(new BoxLayout(
                section, BoxLayout.Y_AXIS));

        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(title);
        label.setFont(new Font(
                "SansSerif", Font.BOLD, 13));
        label.setForeground(TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(label);
        section.add(Box.createVerticalStrut(9));

        return section;
    }

    // ==================== Styling helpers ====================

    private void styleCard(JPanel panel) {
        panel.setBackground(WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(12, 12, 12, 12)));
    }

    private void styleTextField(JTextField field) {
        field.setEditable(false);
        field.setFont(new Font(
                "SansSerif", Font.PLAIN, 11));
        field.setForeground(TEXT);
        field.setBackground(new Color(249, 250, 252));

        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(7, 7, 7, 7)));

        field.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 36));

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font(
                "SansSerif", Font.PLAIN, 12));
        combo.setForeground(TEXT);
        combo.setBackground(WHITE);
        combo.setBorder(new LineBorder(BORDER));
    }

    private void styleCheckBox(JCheckBox check) {
        check.setFont(new Font(
                "SansSerif", Font.PLAIN, 12));
        check.setForeground(TEXT);
        check.setBackground(WHITE);
        check.setFocusPainted(false);
        check.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // Both buttons now use blue styling.
    private void styleButton(JButton button) {
        button.setFont(new Font(
                "SansSerif", Font.BOLD, 12));

        button.setBackground(BLUE);
        button.setForeground(WHITE);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);

        button.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR));

        button.setBorder(new EmptyBorder(
                10, 12, 10, 12));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font(
                "SansSerif", Font.PLAIN, 12));

        table.setForeground(TEXT);
        table.setBackground(WHITE);
        table.setGridColor(new Color(235, 239, 245));
        table.setRowHeight(26);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        table.setSelectionBackground(LIGHT_BLUE);
        table.setSelectionForeground(DARK_BLUE);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(new Font(
                "SansSerif", Font.BOLD, 11));

        table.getTableHeader().setForeground(DARK_BLUE);
        table.getTableHeader().setBackground(LIGHT_BLUE);
        table.getTableHeader().setPreferredSize(
                new Dimension(100, 30));

        table.getTableHeader().setReorderingAllowed(false);
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(new LineBorder(BORDER));
        scrollPane.getViewport().setBackground(WHITE);
    }

    // ==================== Event handlers ====================

    private void attachEvents() {
        browseButton.addActionListener(
                e -> chooseCsvFile());

        analyzeButton.addActionListener(
                e -> startAnalysis());

        numericColumnCombo.addActionListener(e -> {
            Object selected =
                    numericColumnCombo.getSelectedItem();

            if (selected != null) {
                log("Selected column: " + selected);
            }
        });
    }

    // ==================== CSV loading ====================

    private void chooseCsvFile() {
        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Select a CSV dataset");

        chooser.setFileFilter(
                new FileNameExtensionFilter(
                        "CSV files (*.csv)", "csv"));

        if (chooser.showOpenDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = chooser.getSelectedFile();

        try {
            log("Reading CSV: " + selectedFile.getName());

            Dataset loaded =
                    csvReader.readCSV(selectedFile.toPath());

            if (loaded.getColumnCount() == 0) {
                throw new IllegalArgumentException(
                        "The CSV file has no columns.");
            }

            dataset = loaded;

            filePathField.setText(
                    selectedFile.getAbsolutePath());

            List<String> numericColumns =
                    columnDetector.detectNumericColumns(dataset);

            numericColumnCombo.removeAllItems();

            for (String column : numericColumns) {
                numericColumnCombo.addItem(column);
            }

            updatePreviewTable();

            performanceChart.clear();

            bestLabel.setText("Waiting for analysis");
            bestLabel.setForeground(
                    new Color(21, 128, 61));
            bestLabel.setBackground(LIGHT_GREEN);

            datasetStatus.setText(
                    dataset.getRowCount() + " rows, "
                    + dataset.getColumnCount() + " columns");

            log("CSV loaded successfully.");
            log("Rows: " + dataset.getRowCount());
            log("Columns: " + dataset.getColumnCount());
            log("Numeric columns: " + numericColumns);

            if (numericColumns.isEmpty()) {
                log("No numeric columns detected.");

                JOptionPane.showMessageDialog(
                        this,
                        "No numeric columns were found.",
                        "Dataset Loaded",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                log("Dataset preview updated.");
            }

        } catch (IOException | IllegalArgumentException ex) {
            dataset = null;

            numericColumnCombo.removeAllItems();
            previewTable.setModel(new DefaultTableModel());

            performanceChart.clear();

            filePathField.setText("No file selected");
            datasetStatus.setText("Failed to load dataset");

            log("CSV loading failed: " + ex.getMessage());

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load CSV:\n" + ex.getMessage(),
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

        DefaultTableModel model =
                new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        int limit = Math.min(
                dataset.getRowCount(), 100);

        for (int i = 0; i < limit; i++) {
            List<String> row = dataset.getRows().get(i);

            model.addRow(row.toArray(new Object[0]));
        }

        previewTable.setModel(model);
        styleTable(previewTable);

        previewTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF);

        log("Preview: displaying " + limit
                + " of " + dataset.getRowCount() + " rows.");
    }

    // ==================== Analysis ====================

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

        List<SortingAlgorithm> algorithms =
                new ArrayList<>();

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
            showWarning(
                    "Select at least one sorting algorithm.");
            return;
        }

        int columnIndex =
                dataset.getColumnNames().indexOf(selectedColumn);

        if (columnIndex < 0) {
            showWarning(
                    "The selected column was not found.");
            return;
        }

        List<Double> numbers = new ArrayList<>();

        for (int row = 0; row < dataset.getRowCount(); row++) {
            String value =
                    dataset.getValueAt(row, columnIndex);

            NumericValidator.Status status =
                    numericValidator.validate(value);

            if (status == NumericValidator.Status.VALID) {
                numbers.add(
                        numericValidator.parseNumber(value));

            } else if (status ==
                    NumericValidator.Status.INVALID) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid number at CSV row "
                        + (row + 2) + ":\n" + value,
                        "Invalid Data",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }
        }

        if (numbers.isEmpty()) {
            showWarning("No numeric values were found.");
            return;
        }

        double[] input = new double[numbers.size()];

        for (int i = 0; i < numbers.size(); i++) {
            input[i] = numbers.get(i);
        }

        log("Starting analysis.");
        log("Selected column: " + selectedColumn);
        log("Numeric values: " + input.length);

        log("Selected algorithms: " + algorithms.stream()
                .map(SortingAlgorithm::getName)
                .toList());

        setControlsEnabled(false);

        performanceChart.clear();

        bestLabel.setText("Benchmark running...");
        bestLabel.setForeground(MUTED);
        bestLabel.setBackground(LIGHT_BLUE);

        new SwingWorker<List<SortResult>, Void>() {

            @Override
            protected List<SortResult> doInBackground() {
                return performanceEvaluator.evaluate(
                        input, algorithms);
            }

            @Override
            protected void done() {
                try {
                    displayResults(get());

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();

                    log("Analysis interrupted.");
                    bestLabel.setText("Analysis interrupted.");

                } catch (
                        java.util.concurrent.ExecutionException ex) {

                    Throwable cause = ex.getCause();

                    String message = cause == null
                            ? ex.getMessage()
                            : cause.getMessage();

                    log("Analysis failed: " + message);

                    bestLabel.setText("Analysis failed.");
                    bestLabel.setForeground(Color.RED);
                    bestLabel.setBackground(
                            new Color(254, 226, 226));

                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            "Analysis failed:\n" + message,
                            "Analysis Error",
                            JOptionPane.ERROR_MESSAGE);

                } finally {
                    setControlsEnabled(true);
                }
            }
        }.execute();
    }

    // ==================== Results ====================

    private void displayResults(List<SortResult> results) {
        performanceChart.setResults(results);

        SortResult best = null;
        int successfulCount = 0;

        for (SortResult result : results) {
            double time = result.getExecutionTimeMs();

            String formattedTime = Double.isFinite(time)
                    ? String.format(Locale.US, "%.6f", time)
                    : "—";

            log(result.getAlgorithmName() + ": "
                    + formattedTime + " ms - "
                    + result.getStatus());

            if ("Success".equalsIgnoreCase(
                    result.getStatus())) {

                successfulCount++;

                if (Double.isFinite(time) && time >= 0
                        && (best == null
                        || time < best.getExecutionTimeMs())) {
                    best = result;
                }
            }
        }

        if (best != null) {
            bestLabel.setText(
                    "FASTEST: " + best.getAlgorithmName()
                    + "  |  "
                    + String.format(
                            Locale.US,
                            "%.6f ms",
                            best.getExecutionTimeMs()));

            bestLabel.setForeground(
                    new Color(21, 128, 61));

            bestLabel.setBackground(LIGHT_GREEN);

            double[] sorted = best.getSortedData();

            if (sorted != null) {
                int count = Math.min(10, sorted.length);

                log("Sorted output preview: "
                        + Arrays.toString(
                                Arrays.copyOf(sorted, count)));
            }

            log("Fastest algorithm: "
                    + best.getAlgorithmName());

        } else {
            bestLabel.setText(
                    "No successful benchmark results.");

            bestLabel.setForeground(Color.RED);
            bestLabel.setBackground(
                    new Color(254, 226, 226));
        }

        log("Successful algorithms: "
                + successfulCount + "/" + results.size());

        log("Analysis completed.");
    }

    private void setControlsEnabled(boolean enabled) {
        browseButton.setEnabled(enabled);
        analyzeButton.setEnabled(enabled);
        numericColumnCombo.setEnabled(enabled);

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
        String timestamp =
                LocalTime.now().format(timeFormatter);

        logArea.append(
                "[" + timestamp + "] "
                + message + System.lineSeparator());

        logArea.setCaretPosition(
                logArea.getDocument().getLength());
    }

    // ==================== Horizontal bar chart ====================

    private static class PerformanceChart extends JPanel {

        private List<String> algorithmNames =
                new ArrayList<>();

        private List<Double> executionTimes =
                new ArrayList<>();

        private String fastestAlgorithm;

        public PerformanceChart() {
            setBackground(WHITE);
            setOpaque(true);
            setPreferredSize(new Dimension(500, 240));
            setMinimumSize(new Dimension(300, 180));
        }

        public void clear() {
            algorithmNames.clear();
            executionTimes.clear();
            fastestAlgorithm = null;
            repaint();
        }

        public void setResults(List<SortResult> results) {
            algorithmNames = new ArrayList<>();
            executionTimes = new ArrayList<>();
            fastestAlgorithm = null;

            double fastestTime = Double.MAX_VALUE;

            for (SortResult result : results) {
                double time = result.getExecutionTimeMs();

                if ("Success".equalsIgnoreCase(
                        result.getStatus())
                        && Double.isFinite(time)
                        && time >= 0) {

                    algorithmNames.add(
                            result.getAlgorithmName());

                    executionTimes.add(time);

                    if (time < fastestTime) {
                        fastestTime = time;
                        fastestAlgorithm =
                                result.getAlgorithmName();
                    }
                }
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g =
                    (Graphics2D) graphics.create();

            try {
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                if (algorithmNames.isEmpty()) {
                    drawEmptyMessage(g, width, height);
                    return;
                }

                int left = 105;
                int right = 85;
                int top = 20;
                int bottom = 30;

                int chartWidth = Math.max(
                        30, width - left - right);

                int chartHeight = Math.max(
                        30, height - top - bottom);

                int count = algorithmNames.size();

                double maxTime = 0;

                for (double time : executionTimes) {
                    maxTime = Math.max(maxTime, time);
                }

                double scaleMax = maxTime > 0
                        ? maxTime * 1.15
                        : 1.0;

                // Chart axis labels
                g.setFont(new Font(
                        "SansSerif", Font.BOLD, 11));

                g.setColor(TEXT);
                g.drawString("Algorithm", 8, 13);

                g.drawString(
                        "Execution time (ms)",
                        left,
                        height - 5);

                // Vertical grid lines and time scale
                g.setFont(new Font(
                        "SansSerif", Font.PLAIN, 9));

                FontMetrics scaleFm = g.getFontMetrics();

                for (int i = 0; i <= 4; i++) {
                    int x = left
                            + chartWidth * i / 4;

                    g.setColor(new Color(229, 231, 235));

                    g.drawLine(
                            x, top, x, top + chartHeight);

                    String label = String.format(
                            Locale.US,
                            "%.2f",
                            scaleMax * i / 4.0);

                    g.setColor(MUTED);

                    g.drawString(
                            label,
                            x - scaleFm.stringWidth(label) / 2,
                            top + chartHeight + 14);
                }

                // Horizontal bars
                int slotHeight = chartHeight / count;

                int barHeight = Math.max(
                        10, Math.min(28, slotHeight / 2));

                for (int i = 0; i < count; i++) {
                    String name = algorithmNames.get(i);
                    double time = executionTimes.get(i);

                    int barWidth = (int) Math.round(
                            chartWidth * time / scaleMax);

                    if (time > 0) {
                        barWidth = Math.max(2, barWidth);
                    }

                    barWidth = Math.min(
                            chartWidth, barWidth);

                    int centerY = top
                            + slotHeight * i
                            + slotHeight / 2;

                    int y = centerY - barHeight / 2;

                    boolean isFastest =
                            name.equals(fastestAlgorithm);

                    // Algorithm name on the left
                    g.setFont(new Font(
                            "SansSerif",
                            isFastest
                                    ? Font.BOLD
                                    : Font.PLAIN,
                            11));

                    g.setColor(isFastest ? GREEN : TEXT);

                    FontMetrics nameFm = g.getFontMetrics();

                    String displayName = name;

                    int nameX = Math.max(
                            5,
                            left - nameFm.stringWidth(displayName) - 10);

                    g.drawString(
                            displayName, nameX, centerY + 4);

                    // Green fastest bar, blue other bars
                    g.setColor(isFastest ? GREEN : BLUE);

                    g.fillRoundRect(
                            left, y,
                            barWidth, barHeight,
                            8, 8);

                    // Exact execution time at the end of the bar
                    String timeLabel = String.format(
                            Locale.US, "%.3f ms", time);

                    g.setFont(new Font(
                            "SansSerif",
                            isFastest
                                    ? Font.BOLD
                                    : Font.PLAIN,
                            10));

                    g.setColor(isFastest ? GREEN : TEXT);

                    int labelX = left + barWidth + 7;

                    // Keep labels visible if a bar reaches the edge.
                    int maxLabelX = width
                            - g.getFontMetrics()
                                    .stringWidth(timeLabel)
                            - 4;

                    labelX = Math.min(labelX, maxLabelX);

                    g.drawString(
                            timeLabel, labelX, centerY + 4);
                }

            } finally {
                g.dispose();
            }
        }

        private void drawEmptyMessage(
                Graphics2D g, int width, int height) {

            g.setFont(new Font(
                    "SansSerif", Font.PLAIN, 12));

            g.setColor(MUTED);

            String message =
                    "Run an analysis to compare algorithms";

            FontMetrics fm = g.getFontMetrics();

            int x = Math.max(
                    8, (width - fm.stringWidth(message)) / 2);

            int y = Math.max(20, height / 2);

            g.drawString(message, x, y);
        }
    }
}
