import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class WelcomePage extends JFrame {
    private JLabel welcomeLabel;
    private JLabel userImageLabel;
    private JButton logoutButton;
    private JButton displayDataButton;
    private String username;

    public WelcomePage(String username) {
        this.username = username;

        setTitle("Welcome");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        userImageLabel = new JLabel(new ImageIcon("user_image.png"));
        userImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(userImageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(buttonPanel, BorderLayout.SOUTH);

        displayDataButton = new JButton("Display Data");
        displayDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement action to display more data
                JOptionPane.showMessageDialog(WelcomePage.this, "Displaying more data...", "Display Data", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(displayDataButton);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement logout action
                int option = JOptionPane.showConfirmDialog(WelcomePage.this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Logout
                    dispose(); // Close the welcome page
                    new LoginUI(); // Open the login page
                }
            }
        });
        buttonPanel.add(logoutButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomePage("JohnDoe"); // Example username, replace with actual username retrieved from database
            }
        });
    }
}
