import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main extends JFrame {

    public Main() {
        setTitle("PR-LRU Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("src/prlru.png");
        setIconImage(icon.getImage());

        try {
            // Set dark theme
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Set dark theme font
            Font darkFont = new Font("Monaco", Font.BOLD, 18);
            UIManager.put("Button.font", darkFont);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());

        try {
            // Load the header image
            BufferedImage headerImage = ImageIO.read(new File("src/header.png"));

            // Resize the image to fit the label
            int labelWidth = 250; // Adjust this to fit your desired width
            int labelHeight = 100; // Adjust this to fit your desired height
            Image scaledImage = headerImage.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

            // Create a JLabel with the scaled image
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

            // Center align the image
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);

            //create spacing for the top and bottom
            headerPanel.add(Box.createVerticalStrut(60), BorderLayout.NORTH);

            headerPanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton calculateButton = new JButton("Calculate");
        JButton aboutButton = new JButton("About");
        JButton exitButton = new JButton("Exit");

        // Set preferred size for buttons
        Dimension buttonSize = new Dimension(200, 50);
        calculateButton.setPreferredSize(buttonSize);
        aboutButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        // Set font for buttons
        Font buttonFont = new Font("Dialog", Font.BOLD, 17);
        calculateButton.setFont(buttonFont);
        aboutButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalStrut(50)); // Spacing
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

        calculateButton.addActionListener(e -> {
            // go in PRLRU class
            dispose();
            PRLRU prlru = new PRLRU();
            prlru.setVisible(true);

        });

        aboutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Page Replacement (LRU) is bla bla bla", "About", JOptionPane.INFORMATION_MESSAGE);
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
