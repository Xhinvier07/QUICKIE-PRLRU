import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        setTitle("PR-LRU Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("src/prlru.png"); // Change "logo.png" to the path of your icon image
        setIconImage(icon.getImage());

        // Set dark background color
        Color darkBackgroundColor = new Color(34, 34, 34);

        // Set dark foreground color
        Color darkForegroundColor = new Color(240, 240, 240);

        // Set dark theme font
        Font darkFont = new Font("Arial", Font.BOLD, 18); // Customize font as needed

        // Set dark theme for UIManager
        UIManager.put("Panel.background", darkBackgroundColor);
        UIManager.put("Button.background", darkBackgroundColor);
        UIManager.put("Button.foreground", darkForegroundColor);
        UIManager.put("Button.font", darkFont);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));


        JButton calculateButton = new JButton("Calculate");
        JButton aboutButton = new JButton("About");
        JButton exitButton = new JButton("Exit");

        // Set preferred size for buttons
        Dimension buttonSize = new Dimension(200, 50); // Width and height of the buttons
        calculateButton.setPreferredSize(buttonSize);
        aboutButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        // Set font for buttons
        Font buttonFont = new Font("Monaco", Font.BOLD, 18); // Change font style as needed
        calculateButton.setFont(buttonFont);
        aboutButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action listeners...

        buttonPanel.add(Box.createVerticalStrut(100)); // Spacing
        buttonPanel.add(calculateButton);
        buttonPanel.add(Box.createVerticalStrut(20)); // Spacing
        buttonPanel.add(aboutButton);
        buttonPanel.add(Box.createVerticalStrut(20)); // Spacing
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        JLabel bottomLabel = new JLabel("Developed by Group QUICKIE");
        bottomPanel.add(bottomLabel);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Add action listeners

        calculateButton.addActionListener(e -> {
           // go in PRLRU class
            dispose();
            PRLRU prlru = new PRLRU();
            prlru.setVisible(true);

        });

        aboutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "This program was developed by Group 1 for the course IT 2202 - Data Structures and Algorithms.\n\nGroup 1 members:\n- Member 1\n- Member 2\n- Member 3\n- Member 4\n- Member 5", "About", JOptionPane.INFORMATION_MESSAGE);
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });




    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
