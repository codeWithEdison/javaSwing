import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckbox;
    private JLabel lockLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton signInButton;
    private JButton signUpButton;
    private JLabel errorMessageLabel;
    private ImageIcon lockedIcon;
    private ImageIcon lockedRedIcon;
    private boolean isPasswordVisible = false;

    public LoginUI() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ImageIcon lockedIcon = new ImageIcon(getClass().getResource("/Lock_icon.png"));
        ImageIcon scaledLockedIcon = new ImageIcon(lockedIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));

        lockedRedIcon = new ImageIcon(getClass().getResource("/Lock_icon.png"));


        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(centerPanel, BorderLayout.CENTER);

        lockLabel = new JLabel(lockedIcon);
        centerPanel.add(lockLabel);

        usernameLabel = new JLabel("Username:");
        centerPanel.add(usernameLabel);

        usernameField = new JTextField();
        centerPanel.add(usernameField);

        passwordLabel = new JLabel("Password:");
        centerPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        centerPanel.add(passwordField);

        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isPasswordVisible = showPasswordCheckbox.isSelected();
                if (isPasswordVisible) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });
        centerPanel.add(showPasswordCheckbox);

        errorMessageLabel = new JLabel();
        errorMessageLabel.setForeground(Color.RED);
        centerPanel.add(errorMessageLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(buttonPanel, BorderLayout.SOUTH);

        signInButton = new JButton("Sign In");
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
        buttonPanel.add(signInButton);

        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSignupPage();
            }
        });
        buttonPanel.add(signUpButton);

        setVisible(true);
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:5050/Assignment", "root", "971040");
            String query = "SELECT * FROM User WHERE Username = ? AND Password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Authentication successful
                dispose(); // Close the login page
                new WelcomePage(username); // Open the welcome page with the username
            } else {
                // Authentication failed
                lockLabel.setIcon(lockedRedIcon);
                errorMessageLabel.setText("Incorrect username or password");
                lockLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
                signUpButton.setVisible(true);
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSignupPage() {
        SignupUI signupUI = new SignupUI();
        signupUI.setVisible(true);
        this.dispose(); // Close the login window
    }

    private ImageIcon createImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
            System.err.println("Error loading image: " + path);
        }
        return icon;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginUI();
            }
        });
    }
}
