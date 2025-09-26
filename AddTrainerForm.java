package gymsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddTrainerForm extends JFrame {
    JTextField nameField, specializationField, phoneField, emailField;
    JComboBox<String> genderBox;
    JButton saveBtn, back;

    public AddTrainerForm() {
        setTitle("Add Trainer");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(105, 163, 220)); // Sea Blue
        panel.setBounds(0, 0, 400, 400);
        add(panel);

        JLabel heading = new JLabel("Add Trainer", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(Color.WHITE);
        heading.setBounds(100, 10, 200, 30);
        panel.add(heading);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 60, 100, 25);
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 60, 200, 25);
        panel.add(nameField);

        JLabel specializationLabel = new JLabel("Specialization:");
        specializationLabel.setBounds(30, 100, 100, 25);
        specializationLabel.setForeground(Color.WHITE);
        panel.add(specializationLabel);

        specializationField = new JTextField();
        specializationField.setBounds(140, 100, 200, 25);
        panel.add(specializationField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(30, 140, 100, 25);
        phoneLabel.setForeground(Color.WHITE);
        panel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(140, 140, 200, 25);
        panel.add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 180, 100, 25);
        emailLabel.setForeground(Color.WHITE);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(140, 180, 200, 25);
        panel.add(emailField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(30, 220, 100, 25);
        genderLabel.setForeground(Color.WHITE);
        panel.add(genderLabel);

        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setBounds(140, 220, 200, 25);
        panel.add(genderBox);

        saveBtn = new JButton("Save Trainer");
        saveBtn.setBounds(60, 280, 130, 30);
        panel.add(saveBtn);
        saveBtn.addActionListener(e -> saveTrainer());

        back = new JButton("Back");
        back.setBounds(210, 280, 130, 30);
        panel.add(back);
        back.addActionListener(e -> {
            dispose();
            new Dashboard();
        });

        setLayout(null);
        add(panel);
        setVisible(true);
    }

    private void saveTrainer() {
        String name = nameField.getText().trim();
        String specialization = specializationField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();

        if (name.isEmpty() || specialization.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!name.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Name must contain only letters.");
            return;
        }

        if (!specialization.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Specialization must contain only letters.");
            return;
        }

        if (!phone.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Phone must be exactly 11 digits.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO trainers (name, specialization, phone, email, gender) VALUES (?, ?, ?, ?, ?)")) {

            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, gender);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Trainer added successfully!");


            nameField.setText("");
            specializationField.setText("");
            phoneField.setText("");
            emailField.setText("");
            genderBox.setSelectedIndex(0);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddTrainerForm();
    }
}
