import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;

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
            BufferedImage headerImage = ImageIO.read(new File("src/header1.png"));

            // Resize the image to fit the label
            int labelWidth = 270; // Adjust this to fit your desired width
            int labelHeight = 150; // Adjust this to fit your desired height
            Image scaledImage = headerImage.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

            BufferedImage bufferedImage = new BufferedImage(labelWidth, labelHeight, BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

            // Create a JLabel with the scaled image
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

            // Center align the image
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);

            //create spacing for the top and bottom
            headerPanel.add(Box.createVerticalStrut(30), BorderLayout.NORTH);

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
            String message = "<html><body style='width: 300px; font-family: serif; text-align: justify;'>"
                    + "Page replacement is a crucial concept in computer memory management, especially in systems utilizing virtual memory. Virtual memory extends a computer's usable memory beyond physical limits by utilizing disk storage. In such systems, memory is divided into fixed-size blocks known as pages.<br><br>"
                    + "Least Recently Used (LRU) is a widely employed page replacement algorithm in computer operating systems and memory management. LRU prioritizes replacing the page that has remained untouched for the longest duration.<br><br>"
                    + "LRU algorithm ensures optimal memory usage by discarding the least recently accessed pages when new ones need to be loaded. This approach maximizes system performance by retaining frequently accessed data in memory while efficiently managing memory resources."
                    + "</body></html>";
            JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
