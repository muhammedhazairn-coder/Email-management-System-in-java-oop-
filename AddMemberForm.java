package gymsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddMemberForm extends JFrame {
    JTextField nameField, fatherField, contactField, emailField;
    JRadioButton maleRadio, femaleRadio, otherRadio;
    JComboBox<String> timingBox;
    JButton saveButton, back;
    private final ButtonGroup genderGroup = new ButtonGroup();

    public AddMemberForm() {
        setTitle("Add Member");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(105, 163, 220)); // Sea Green
        add(panel);

        JLabel title = new JLabel("Add New Member");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE); // Optional: white text
        title.setBounds(120, 10, 200, 30);
        panel.add(title);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(30, 60, 100, 25);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 60, 200, 25);
        panel.add(nameField);

        JLabel fatherLabel = new JLabel("Father Name:");
        fatherLabel.setForeground(Color.WHITE);
        fatherLabel.setBounds(30, 100, 100, 25);
        panel.add(fatherLabel);

        fatherField = new JTextField();
        fatherField.setBounds(150, 100, 200, 25);
        panel.add(fatherField);

        JLabel contactLabel = new JLabel("Contact No:");
        contactLabel.setForeground(Color.WHITE);
        contactLabel.setBounds(30, 140, 100, 25);
        panel.add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(150, 140, 200, 25);
        panel.add(contactField);


        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(30, 180, 100, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 180, 200, 25);
        panel.add(emailField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(Color.WHITE);
        genderLabel.setBounds(30, 220, 100, 25);
        panel.add(genderLabel);

        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        otherRadio = new JRadioButton("Other");

        maleRadio.setBounds(150, 220, 60, 25);
        femaleRadio.setBounds(210, 220, 80, 25);
        otherRadio.setBounds(290, 220, 80, 25);

        maleRadio.setBackground(panel.getBackground());
        femaleRadio.setBackground(panel.getBackground());
        otherRadio.setBackground(panel.getBackground());

        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);

        panel.add(maleRadio);
        panel.add(femaleRadio);
        panel.add(otherRadio);

        JLabel timingLabel = new JLabel("Timing:");
        timingLabel.setForeground(Color.WHITE);
        timingLabel.setBounds(30, 260, 100, 25);
        panel.add(timingLabel);

        timingBox = new JComboBox<>(new String[]{"Morning", "Evening"});
        timingBox.setBounds(150, 260, 200, 25);
        panel.add(timingBox);

        saveButton = new JButton("Save");
        saveButton.setBounds(100, 320, 100, 30);
        panel.add(saveButton);

        back = new JButton("Back");
        back.setBounds(210, 320, 100, 30);
        panel.add(back);

        saveButton.addActionListener(e -> saveMember());
        back.addActionListener(e -> {
            dispose();
            new Dashboard();
        });

        setVisible(true);
    }

    private void saveMember() {
        String name = nameField.getText();
        String father = fatherField.getText();
        String contact = contactField.getText();
        String email = emailField.getText();
        String gender = maleRadio.isSelected() ? "Male" :
                femaleRadio.isSelected() ? "Female" : otherRadio.isSelected() ? "Other" : "";
        String timing = (String) timingBox.getSelectedItem();

        if (name.isEmpty() || father.isEmpty() || contact.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        if (!name.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Name must contain only letters.");
            return;
        }

        if (!father.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Father Name must contain only letters.");
            return;
        }

        if (!contact.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Contact must contain only digits.");
            return;
        }

        if (contact.length() != 11) {
            JOptionPane.showMessageDialog(this, "Contact number must be 11 digits.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO members(name, father_name, contact, email, gender, timing) VALUES (?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, name);
            ps.setString(2, father);
            ps.setString(3, contact);
            ps.setString(4, email);
            ps.setString(5, gender);
            ps.setString(6, timing);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member saved successfully!");
            clearForm();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving member: " + ex.getMessage());
        }
    }

    private void clearForm() {
        nameField.setText("");
        fatherField.setText("");
        contactField.setText("");
        emailField.setText("");
        genderGroup.clearSelection();
        timingBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new AddMemberForm();
    }
}
