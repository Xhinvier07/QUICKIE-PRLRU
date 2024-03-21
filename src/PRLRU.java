import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;

public class PRLRU extends JFrame {
    private JLabel streamLabel, framesLabel, resultLabel;
    private JTextField streamField, framesField;
    private JButton calculateButton;
    private JButton clearButton;
    private JButton backButton;
    private JTable table;
    private DefaultTableModel tableModel;


    public PRLRU() {
        setTitle("Page Replacement Algorithm");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
        

        // Initialize table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("PAGE");
        // Add columns for frames dynamically based on input


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


        table = new JTable(tableModel);
    }

    private void addComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the panel
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

        // Add panel to frame
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int columns = table.getColumnModel().getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int preferredWidth = column.getMinWidth();
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, i);
                Component c = table.prepareRenderer(cellRenderer, row, i);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);
            }
            column.setPreferredWidth(preferredWidth);
        }

    }

    private void calculatePageReplacement() {
        // Clear previous data
        tableModel.setRowCount(0);
        resultLabel.setText("");

        String incomingStreamInput = streamField.getText();
        String[] incomingStreamStringArray = incomingStreamInput.split(" ");
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

        Object[] rowData = new Object[frames + 1];
        rowData[0] = "HIT/FAULT";
        for (int i = 1; i <= frames; i++)
            rowData[i] = "F" + (i - 1);
        tableModel.addRow(rowData);

        for (int i = 0; i < n; i++) {
            rowData = new Object[frames + 1];
            rowData[0] = incomingStream[i];

            if (checkHit(incomingStream[i], queue, occupied)) {
                pagehits++;
                rowData[1] = "HIT";
                tableModel.addRow(rowData);
            } else if (occupied < frames) {
                queue[occupied] = incomingStream[i];
                occupied++;
                pagefault++;
                rowData[1] = "FAULT";
                for (int j = 0; j < occupied; j++) {
                    rowData[j + 2] = queue[j];
                }
                tableModel.addRow(rowData);
            } else {
                int max = Integer.MIN_VALUE;
                int index = -1;

                for (int j = 0; j < frames; j++) {
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
                rowData[1] = "FAULT";
                for (int j = 0; j < frames; j++) {
                    rowData[j + 2] = queue[j];
                }
                tableModel.addRow(rowData);
            }
        }

        double pageHitRate = (double) pagehits / n * 100;
        double pageFaultRate = (double) pagefault / n * 100;

        resultLabel.setText("<html>PAGE HITS: " + pagehits + "<br/>" +
                "HIT RATE: " + pageHitRate + "%<br/>" +
                "PAGE FAULTS: " + pagefault + "<br/>" +
                "FAULT RATE: " + pageFaultRate + "%</html>");
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
             PRLRU gui = new  PRLRU();
            gui.setVisible(true);
        });
    }
}
