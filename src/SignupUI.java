import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class SignupUI extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField telField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private ButtonGroup genderGroup;
    private JComboBox<String> userTypeComboBox;
    private JButton photoIdButton;
    private JButton signUpButton;
    private JLabel photoIdLabel;
    private File photoIdFile;

    public SignupUI() {
        setTitle("Sign Up");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(12, 2, 5, 5));

        JLabel firstNameLabel = new JLabel("First Name:");
        add(firstNameLabel);

        firstNameField = new JTextField();
        add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        add(lastNameLabel);

        lastNameField = new JTextField();
        add(lastNameField);

        JLabel emailLabel = new JLabel("Email:");
        add(emailLabel);

        emailField = new JTextField();
        add(emailField);

        JLabel telLabel = new JLabel("Telephone:");
        add(telLabel);

        telField = new JTextField();
        add(telField);

        JLabel usernameLabel = new JLabel("Username:");
        add(usernameLabel);

        usernameField = new JTextField();
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel);

        passwordField = new JPasswordField();
        add(passwordField);

        JLabel genderLabel = new JLabel("Gender:");
        add(genderLabel);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        add(genderPanel);

        JLabel userTypeLabel = new JLabel("User Type:");
        add(userTypeLabel);

        String[] userTypes = {"Regular", "Admin"};
        userTypeComboBox = new JComboBox<>(userTypes);
        add(userTypeComboBox);

        JLabel photoIdUploadLabel = new JLabel("Upload Photo ID:");
        add(photoIdUploadLabel);

        photoIdButton = new JButton("Choose File");
        photoIdButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(SignupUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    photoIdFile = fileChooser.getSelectedFile();
                    photoIdLabel.setText(photoIdFile.getName());
                }
            }
        });
        add(photoIdButton);

        photoIdLabel = new JLabel();
        add(photoIdLabel);

        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        add(new JLabel());
        add(signUpButton);

        setVisible(true);
    }

    private void signUp() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String tel = telField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String gender = maleRadioButton.isSelected() ? "Male" : "Female";
        String userType = (String) userTypeComboBox.getSelectedItem();
        String photoIdFileName = photoIdFile != null ? photoIdFile.getName() : "";

        // Database connection and insertion
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:5050/Assignment", "root", "971040");
            String query = "INSERT INTO User(FirstName, LastName, Email, Tel, Username, Password, Gender, UserType, Photo_Id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, tel);
            preparedStatement.setString(5, username);
            preparedStatement.setString(6, password);
            preparedStatement.setString(7, gender);
            preparedStatement.setString(8, userType);
            preparedStatement.setString(9, photoIdFileName);
            preparedStatement.executeUpdate();

            // Upload photo ID file to a directory
            if (photoIdFile != null) {
                File destination = new File("photo_ids/" + photoIdFile.getName());
                Files.copy(photoIdFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            JOptionPane.showMessageDialog(this, "Sign Up Successful!");
            connection.close();
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SignupUI();
            }
        });
    }
}
