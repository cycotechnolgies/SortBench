package com.cyco.uni.sortbench.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingWorker;
import com.cyco.uni.sortbench.analysis.ColumnDetector;
import com.cyco.uni.sortbench.csv.CSVReader;
import com.cyco.uni.sortbench.model.Dataset;
import com.cyco.uni.sortbench.model.SortResult;
import com.cyco.uni.sortbench.performance.PerformanceEvaluator;
import com.cyco.uni.sortbench.sorting.*;
import com.cyco.uni.sortbench.validation.NumericValidator;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

/**
 * Main window for the SortBench Sorting Algorithm Performance Analyzer.
 *
 * This class implements the initial GUI layout. CSV parsing and sorting
 * benchmark integration can be connected to the controls in a later issue.
 */
public class MainFrame extends JFrame {

    private static final Color BLUE = new Color(28, 112, 205);
    private static final Color DARK_BLUE = new Color(29, 69, 126);
    private static final Color PALE_BLUE = new Color(235, 244, 255);
    private static final Color BORDER = new Color(195, 211, 231);
    private static final Color TEXT = new Color(31, 51, 82);
    private static final Color GREEN = new Color(25, 129, 91);
    private static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font SECTION_FONT = new Font("SansSerif", Font.BOLD, 17);
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private final JTextField filePathField = new JTextField();
    private final JComboBox<String> numericColumnCombo = new JComboBox<>();
    private final JTextArea logArea = new JTextArea();
    private Dataset dataset;
    private final CSVReader csvReader = new CSVReader();
    private final ColumnDetector columnDetector = new ColumnDetector();
    private final NumericValidator numericValidator = new NumericValidator();
    private final PerformanceEvaluator performanceEvaluator = new PerformanceEvaluator();
    private JLabel bestLabel;
    private final JTable previewTable;
    private final JTable resultsTable;
    private final JCheckBox[] algorithmChecks = new JCheckBox[]{
        new JCheckBox("Insertion Sort", true),
        new JCheckBox("Shell Sort", true),
        new JCheckBox("Merge Sort", true),
        new JCheckBox("Quick Sort", true),
        new JCheckBox("Heap Sort", true)
    };

    public MainFrame() {
        super("Sorting Algorithm Performance Analyzer");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 720));
        setSize(1360, 900);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(246, 249, 253));

        previewTable = createPreviewTable();
        resultsTable = createResultsTable();

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createLogPanel(), BorderLayout.SOUTH);

        log("Application window initialized.");
        log("Upload a CSV file to begin.");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(18, 4));
        header.setBackground(DARK_BLUE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(18, 83, 151)),
                BorderFactory.createEmptyBorder(17, 24, 17, 24)));

        JLabel icon = new JLabel("▥");
        icon.setForeground(Color.WHITE);
        icon.setFont(new Font("SansSerif", Font.BOLD, 43));
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setPreferredSize(new Dimension(58, 60));

        JLabel title = new JLabel("Sorting Algorithm Performance Analyzer");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));

        JLabel subtitle = new JLabel(
                "Upload a CSV file, select a numeric column, and compare the performance of different sorting algorithms.");
        subtitle.setForeground(new Color(221, 235, 252));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(7));
        heading.add(subtitle);

        header.add(icon, BorderLayout.WEST);
        header.add(heading, BorderLayout.CENTER);
        return header;
    }

    private JSplitPane createMainContent() {
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createUploadPanel());
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(createColumnPanel());
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(createAlgorithmsPanel());
        leftPanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setOpaque(false);
        rightPanel.add(createPreviewPanel(), BorderLayout.CENTER);
        rightPanel.add(createResultsPanel(), BorderLayout.SOUTH);

        JScrollPane leftScroll = new JScrollPane(leftPanel);
        leftScroll.setBorder(BorderFactory.createEmptyBorder());
        leftScroll.setOpaque(false);
        leftScroll.getViewport().setOpaque(false);
        leftScroll.getVerticalScrollBar().setUnitIncrement(16);
        leftScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightPanel);
        splitPane.setResizeWeight(0.37);
        splitPane.setDividerLocation(450);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        splitPane.setOpaque(false);
        return splitPane;
    }

    private JPanel createUploadPanel() {
        JPanel panel = createSectionPanel("1. Upload CSV File");
        GridBagConstraints gbc = constraints();

        filePathField.setFont(BODY_FONT);
        filePathField.setEditable(false);
        filePathField.setText("No file selected");
        filePathField.setToolTipText("Selected CSV file path");

        JButton browseButton = new JButton("Browse...");
        styleButton(browseButton, false);
        browseButton.addActionListener(event -> chooseCsvFile());

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(filePathField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(6, 8, 6, 0);
        panel.add(browseButton, gbc);

        JLabel status = new JLabel("●  Ready to load a CSV file");
        status.setForeground(GREEN);
        status.setFont(BODY_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 2, 2, 2);
        panel.add(status, gbc);

        JLabel hint = new JLabel("Supported format: .csv");
        hint.setForeground(new Color(95, 111, 132));
        hint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.insets = new Insets(3, 2, 0, 2);
        panel.add(hint, gbc);
        return panel;
    }

    private JPanel createColumnPanel() {
        JPanel panel = createSectionPanel("2. Select Column to Sort");
        GridBagConstraints gbc = constraints();

        JLabel label = new JLabel("Available numeric columns:");
        label.setFont(BODY_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        numericColumnCombo.setFont(BODY_FONT);
        numericColumnCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 2, 10, 2);
        panel.add(numericColumnCombo, gbc);

        JLabel info = new JLabel(
                "<html><body style='width:280px'>ⓘ  Only numeric columns can be selected for sorting.</body></html>");
        info.setOpaque(true);
        info.setBackground(PALE_BLUE);
        info.setForeground(new Color(26, 91, 163));
        info.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(166, 207, 250)),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)));
        info.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(info, gbc);
        return panel;
    }

    private JPanel createAlgorithmsPanel() {
        JPanel panel = createSectionPanel("3. Choose Sorting Algorithms");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (JCheckBox checkBox : algorithmChecks) {
            checkBox.setFont(BODY_FONT);
            checkBox.setForeground(TEXT);
            checkBox.setOpaque(false);
            checkBox.setAlignmentX(LEFT_ALIGNMENT);
            checkBox.setFocusPainted(false);
            checkBox.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            panel.add(checkBox);
        }

        panel.add(Box.createVerticalStrut(8));
        JSeparatorLine separator = new JSeparatorLine();
        separator.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(separator);
        panel.add(Box.createVerticalStrut(12));

        
        JButton startButton = new JButton("▶   Start Analysis");

        // Button text
        startButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        startButton.setForeground(Color.WHITE);

        // Force Swing to use the specified background color
        startButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        startButton.setBackground(BLUE);
        startButton.setOpaque(true);
        startButton.setContentAreaFilled(true);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);

        // Button layout and size
        startButton.setBorder(
                BorderFactory.createEmptyBorder(13, 16, 13, 16)
        );
        startButton.setAlignmentX(LEFT_ALIGNMENT);
        startButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48)
        );

        // Action
        startButton.addActionListener(
                event -> startAnalysis()
        );

        panel.add(startButton);


        return panel;
    }

    private JPanel createPreviewPanel() {
        JPanel panel = createSectionPanel("▦  Dataset Preview");
        GridBagConstraints gbc = constraints();
        JScrollPane tableScroll = new JScrollPane(previewTable);
        tableScroll.setPreferredSize(new Dimension(500, 270));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(tableScroll, gbc);
        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = createSectionPanel("◷  Sorting Performance Results");
        GridBagConstraints gbc = constraints();
        JScrollPane tableScroll = new JScrollPane(resultsTable);
        tableScroll.setPreferredSize(new Dimension(500, 205));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(tableScroll, gbc);

        JPanel bestPanel = new JPanel(new BorderLayout(12, 2));
        bestPanel.setBackground(new Color(234, 249, 238));
        bestPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(157, 218, 177)),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));

        JLabel trophy = new JLabel("🏆");
        trophy.setFont(new Font("SansSerif", Font.PLAIN, 26));
        bestLabel = new JLabel(
                "<html><b>Best Performing Algorithm</b><br>Results will appear after analysis.</html>");
        bestLabel.setForeground(new Color(21, 112, 78));
        bestLabel.setFont(BODY_FONT);
        bestPanel.add(trophy, BorderLayout.WEST);
        bestPanel.add(bestLabel, BorderLayout.CENTER);
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 2, 2, 2);
        panel.add(bestPanel, gbc);
        return panel;
    }

    private JPanel createLogPanel() {
        JPanel panel = createSectionPanel("▤  Log / Messages");
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 12, 10, 12)));
        panel.setLayout(new GridBagLayout());

        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setForeground(new Color(29, 54, 91));
        logArea.setBackground(Color.WHITE);
        logArea.setLineWrap(false);
        logArea.setRows(4);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(700, 95));
        GridBagConstraints gbc = constraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);
        panel.setPreferredSize(new Dimension(900, 155));
        return panel;
    }

    private JTable createPreviewTable() {
        JTable table = new JTable(new DefaultTableModel(new Object[0][0], new Object[0]) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });
        styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return table;
    }

    private JTable createResultsTable() {
        String[] columns = {"Algorithm", "Execution Time (ms)", "Status"};
        Object[][] rows = {
            {"Insertion Sort", "—", "Waiting"},
            {"Shell Sort", "—", "Waiting"},
            {"Merge Sort", "—", "Waiting"},
            {"Quick Sort", "—", "Waiting"},
            {"Heap Sort", "—", "Waiting"}
        };
        JTable table = new JTable(new DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        styleTable(table);
        table.setRowHeight(29);
        return table;
    }

    private void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setForeground(TEXT);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(218, 226, 237));
        table.setRowHeight(31);
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setBackground(new Color(239, 243, 249));
        table.getTableHeader().setPreferredSize(new Dimension(100, 34));
        table.setIntercellSpacing(new Dimension(1, 1));
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        Border outer = BorderFactory.createLineBorder(BORDER);
        Border inner = BorderFactory.createEmptyBorder(13, 14, 13, 14);
        panel.setBorder(BorderFactory.createCompoundBorder(outer, inner));
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel heading = new JLabel(title);
        heading.setFont(SECTION_FONT);
        heading.setForeground(DARK_BLUE);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        GridBagConstraints gbc = constraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(heading, gbc);

        // The remaining controls are added below the heading by each panel method.
        return panel;
    }

    private GridBagConstraints constraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 2, 5, 2);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        return gbc;
    }

    private void chooseCsvFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a CSV dataset");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "CSV files (*.csv)", "csv"));

        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File selectedFile = chooser.getSelectedFile();
        try {
            Dataset loaded = csvReader.readCSV(selectedFile.toPath());
            List<String> numericColumns = columnDetector.detectNumericColumns(loaded);
            if (loaded.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "The CSV contains headers but no data rows.",
                        "Empty dataset", JOptionPane.WARNING_MESSAGE);
            }
            dataset = loaded;
            filePathField.setText(selectedFile.getAbsolutePath());
            numericColumnCombo.removeAllItems();
            for (String column : numericColumns) numericColumnCombo.addItem(column);
            updatePreviewTable();
            resetResultsTable();
            log("Loaded CSV: " + selectedFile.getName() + " (" + dataset.getRowCount()
                    + " rows, " + dataset.getColumnCount() + " columns).");
            log("Detected " + numericColumns.size() + " numeric column(s): " + numericColumns);
            if (numericColumns.isEmpty()) {
                log("No numeric columns were found. Check the CSV values.");
                JOptionPane.showMessageDialog(this,
                        "No numeric columns were detected in this CSV.",
                        "No numeric columns", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "CSV loaded successfully. Select a numeric column and start analysis.",
                        "Dataset loaded", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException | IllegalArgumentException ex) {
            dataset = null;
            numericColumnCombo.removeAllItems();
            filePathField.setText("No file selected");
            log("CSV load failed: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Could not load CSV:\\n" + ex.getMessage(),
                    "CSV error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePreviewTable() {
        DefaultTableModel model = new DefaultTableModel(dataset.getColumnNames().toArray(), 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        int limit = Math.min(dataset.getRowCount(), 100);
        for (int i = 0; i < limit; i++) {
            model.addRow(dataset.getRows().get(i).toArray());
        }
        if (dataset.getRowCount() > limit) {
            log("Preview limited to first " + limit + " rows.");
        }
        previewTable.setModel(model);
        styleTable(previewTable);
        previewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    private void resetResultsTable() {
        DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
        model.setRowCount(0);
        for (String name : Arrays.asList("Insertion Sort", "Shell Sort", "Merge Sort",
                "Quick Sort", "Heap Sort")) {
            model.addRow(new Object[]{name, "—", "Waiting"});
        }
        bestLabel.setText("<html><b>Best Performing Algorithm</b><br>Results will appear after analysis.</html>");
    }

    private void startAnalysis() {
        if (dataset == null) {
            JOptionPane.showMessageDialog(this, "Upload a valid CSV file first.",
                    "No dataset", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String selectedColumn = (String) numericColumnCombo.getSelectedItem();
        if (selectedColumn == null) {
            JOptionPane.showMessageDialog(this, "No numeric column is available to sort.",
                    "No numeric column", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<SortingAlgorithm> algorithms = new ArrayList<>();
        if (algorithmChecks[0].isSelected()) algorithms.add(new InsertionSort());
        if (algorithmChecks[1].isSelected()) algorithms.add(new ShellSort());
        if (algorithmChecks[2].isSelected()) algorithms.add(new MergeSort());
        if (algorithmChecks[3].isSelected()) algorithms.add(new QuickSort());
        if (algorithmChecks[4].isSelected()) algorithms.add(new HeapSort());
        if (algorithms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one sorting algorithm.",
                    "No algorithm selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int columnIndex = dataset.getColumnNames().indexOf(selectedColumn);
        List<Double> numbers = new ArrayList<>();
        for (int row = 0; row < dataset.getRowCount(); row++) {
            String value = dataset.getValueAt(row, columnIndex);
            NumericValidator.Status status = numericValidator.validate(value);
            if (status == NumericValidator.Status.VALID) {
                numbers.add(numericValidator.parseNumber(value));
            } else if (status == NumericValidator.Status.INVALID) {
                JOptionPane.showMessageDialog(this,
                        "Invalid numeric value at CSV row " + (row + 2) + ": " + value,
                        "Invalid data", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Missing cells are ignored; ColumnDetector also ignores empty cells.
        }
        if (numbers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "The selected column contains no numeric values.",
                    "No data", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double[] input = new double[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) input[i] = numbers.get(i);

        log("Starting analysis on column '" + selectedColumn + "' with " + input.length + " values.");
        log("Algorithms: " + algorithms.stream().map(SortingAlgorithm::getName).toList());
        setControlsEnabled(false);

        new SwingWorker<List<SortResult>, Void>() {
            @Override protected List<SortResult> doInBackground() {
                return performanceEvaluator.evaluate(input, algorithms);
            }
            @Override protected void done() {
                try {
                    List<SortResult> results = get();
                    DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
                    model.setRowCount(0);
                    SortResult best = null;
                    for (SortResult result : results) {
                        String time = Double.isFinite(result.getExecutionTimeMs())
                                ? String.format(java.util.Locale.US, "%.6f", result.getExecutionTimeMs())
                                : "—";
                        model.addRow(new Object[]{result.getAlgorithmName(), time, result.getStatus()});
                        log(result.getAlgorithmName() + ": " + time + " ms (" + result.getStatus() + ")");
                        if ("Success".equals(result.getStatus())
                                && (best == null || result.getExecutionTimeMs() < best.getExecutionTimeMs())) {
                            best = result;
                        }
                    }
                    if (best != null) {
                        bestLabel.setText("<html><b>Best Performing Algorithm</b><br>"
                                + best.getAlgorithmName() + " — "
                                + String.format(java.util.Locale.US, "%.6f ms", best.getExecutionTimeMs())
                                + "</html>");
                        log("Best measured result: " + best.getAlgorithmName());
                    } else {
                        bestLabel.setText("<html><b>Best Performing Algorithm</b><br>No successful results.</html>");
                    }
                    SortResult firstSuccessful = results.stream()
                            .filter(r -> "Success".equals(r.getStatus())).findFirst().orElse(null);
                    if (firstSuccessful != null && firstSuccessful.getSortedData() != null) {
                        log("Sorted output preview: " + Arrays.toString(Arrays.copyOf(
                                firstSuccessful.getSortedData(),
                                Math.min(10, firstSuccessful.getSortedData().length))));
                    }
                    log("Analysis completed.");
                } catch (Exception ex) {
                    log("Analysis failed: " + ex.getMessage());
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Analysis failed: " + ex.getMessage(), "Analysis error",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    setControlsEnabled(true);
                }
            }
        }.execute();
    }

    private void setControlsEnabled(boolean enabled) {
        numericColumnCombo.setEnabled(enabled);
        for (JCheckBox checkBox : algorithmChecks) checkBox.setEnabled(enabled);
    }

    private void log(String message) {
        String timestamp = LocalTime.now().format(TIME_FORMAT);
        logArea.append("[" + timestamp + "] " + message + System.lineSeparator());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void styleButton(JButton button, boolean primary) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(primary ? Color.WHITE : DARK_BLUE);
        button.setBackground(primary ? BLUE : new Color(247, 250, 255));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? BLUE : BORDER),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)));
    }

    /**
     * Small divider used in the algorithm selection panel.
     */
    private static class JSeparatorLine extends JPanel {
        JSeparatorLine() {
            setBackground(new Color(221, 229, 239));
            setPreferredSize(new Dimension(200, 1));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        }
    }
}
