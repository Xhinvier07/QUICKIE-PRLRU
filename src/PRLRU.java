import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class PRLRU extends JFrame {
    private JLabel streamLabel, framesLabel, resultLabel;
    private JTextField streamField, framesField;
    private JButton calculateButton;
    private JButton clearButton;
    private JButton backButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public PRLRU() {

        ImageIcon icon = new ImageIcon("src/prlru.png");
        setIconImage(icon.getImage());

        setTitle("PR - Least Recently Used Calculator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // make the font bold
            Font buttonFont = new Font("Monaco", Font.BOLD, 10);
            UIManager.put("Button.font", buttonFont);

            Font labelFont = new Font("Monaco", Font.BOLD, 10);
            UIManager.put("Label.font", labelFont);

            Font rowDataFont = new Font("Monaco", Font.PLAIN, 12);
            UIManager.put("Table.font", rowDataFont);

            Font textFont = new Font("Monaco", Font.BOLD, 12);
            UIManager.put("TextField.font", textFont);

            Font tableHeaderFont = new Font("Monaco", Font.BOLD, 12);
            UIManager.put("TableHeader.font", tableHeaderFont);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        addComponents();
    }

    private void initComponents() {
        streamLabel = new JLabel("Enter the incoming stream (space-separated integers):");
        framesLabel = new JLabel("Enter the number of frames:");
        resultLabel = new JLabel();

        streamField = new JTextField(20);
        framesField = new JTextField(5);

        calculateButton = new JButton("Calculate");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back");

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePageReplacement();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                streamField.setText("");
                framesField.setText("");
                resultLabel.setText("");
                tableModel.setRowCount(0);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Main main = new Main();
                main.setVisible(true);
            }
        });

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable cell editing
            }
        };

        // column names
        tableModel.addColumn("PAGE");
        tableModel.addColumn("FRAMES");
        tableModel.addColumn("HIT/FAULT");

    }

    private void addComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(streamLabel, gbc);
        gbc.gridy++;
        panel.add(streamField, gbc);
        gbc.gridy++;
        panel.add(framesLabel, gbc);
        gbc.gridy++;
        panel.add(framesField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        gbc.gridy++;
        panel.add(buttonPanel, gbc);

        gbc.gridy++;
        panel.add(resultLabel, gbc);

        // Create a padded panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Enable auto resize

        // Set custom cell renderer for styling
        DefaultTableCellRenderer renderer = new CustomRenderer(Color.WHITE);
        renderer.setFont(new Font("Monaco", Font.BOLD, 12)); // Set font to Monaco
        table.setDefaultRenderer(Object.class, renderer);

        // Disable row selection and cell selection
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(panel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void calculatePageReplacement() {
        tableModel.setRowCount(0);

        String incomingStreamInput = streamField.getText();

        if (incomingStreamInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an incoming stream.");
            return;
        }

        if (framesField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the number of frames.");
            return;
        }

        // Split input by either spaces or commas
        String[] incomingStreamStringArray = incomingStreamInput.split("[,\\s]+");

        int n = incomingStreamStringArray.length;
        int[] incomingStream = new int[n];
        for (int i = 0; i < n; i++) {
            incomingStream[i] = Integer.parseInt(incomingStreamStringArray[i]);
        }

        int frames = Integer.parseInt(framesField.getText());

        int[] queue = new int[frames];
        int[] distance = new int[frames];
        int occupied = 0;
        int pagefault = 0;
        int pagehits = 0;
        int[] hitCounter = {0}; // Array to hold hit counter
        int[] faultCounter = {0}; // Array to hold fault counter

        for (int i = 0; i < n; i++) {
            if (checkHit(incomingStream[i], queue, occupied)) {
                pagehits++;
                updateTableRow(incomingStream[i], queue, true, hitCounter, faultCounter);
            } else if (occupied < frames) {
                queue[occupied] = incomingStream[i];
                occupied++;
                pagefault++;
                updateTableRow(incomingStream[i], queue, false, hitCounter, faultCounter);
            } else {
                int max = Integer.MIN_VALUE;
                int index = -1;

                for (int j  = 0; j < frames; j++) {
                    distance[j] = 0;
                    for (int k = i - 1; k >= 0; k--) {
                        ++distance[j];
                        if (queue[j] == incomingStream[k])
                            break;
                    }
                    if (distance[j] > max) {
                        max = distance[j];
                        index = j;
                    }
                }
                queue[index] = incomingStream[i];
                pagefault++;
                updateTableRow(incomingStream[i], queue, false, hitCounter, faultCounter);
            }
        }

        double pageHitRate = (double) pagehits / n * 100;
        double pageFaultRate = (double) pagefault / n * 100;

        // 2 decimal places
        String formattedHitRate = String.format("%.2f", pageHitRate);
        String formattedFaultRate = String.format("%.2f", pageFaultRate);

        resultLabel.setText("<html>PAGE HITS: " + pagehits + "<br/>" +
                "HIT RATE: " + formattedHitRate + "%<br/>" +
                "PAGE FAULTS: " + pagefault + "<br/>" +
                "FAULT RATE: " + formattedFaultRate + "%</html>");
    }

    private void updateTableRow(int incomingPage, int[] queue, boolean hit, int[] hitCounter, int[] faultCounter) {
        Object[] rowData = new Object[3];
        rowData[0] = incomingPage;
        rowData[1] = java.util.Arrays.toString(queue);
        rowData[2] = hit ? "HIT " + (++hitCounter[0]) : "FAULT " + (++faultCounter[0]);

        // Add row to the table model
        tableModel.addRow(rowData);
    }

    static boolean checkHit(int incomingPage, int[] queue, int occupied) {
        for (int i = 0; i < occupied; i++) {
            if (incomingPage == queue[i])
                return true;
        }
        return false;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            PRLRU gui = new PRLRU();
            gui.setVisible(true);
        });
    }

    public class CustomRenderer extends DefaultTableCellRenderer {
        private Color backgroundColor;

        public CustomRenderer(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Set background color
            component.setBackground(backgroundColor);

            return component;
        }
    }
}
