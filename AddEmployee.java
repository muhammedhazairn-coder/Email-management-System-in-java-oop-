package gymsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddEmployee extends JFrame {
    JTextField nameField, salaryField, phoneField, emailField, workField;
    JComboBox<String> genderBox;
    JButton saveBtn, back;

    public AddEmployee() {
        setTitle("Add Employee");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(105, 163, 220));
        panel.setBounds(0, 0, 400, 450);
        add(panel);

        JLabel heading = new JLabel("Add Employee", JLabel.CENTER);
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

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(30, 100, 100, 25);
        salaryLabel.setForeground(Color.WHITE);
        panel.add(salaryLabel);

        salaryField = new JTextField();
        salaryField.setBounds(140, 100, 200, 25);
        panel.add(salaryField);

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

        JLabel workLabel = new JLabel("Work:");
        workLabel.setBounds(30, 260, 100, 25);
        workLabel.setForeground(Color.WHITE);
        panel.add(workLabel);

        workField = new JTextField();
        workField.setBounds(140, 260, 200, 25);
        panel.add(workField);

        saveBtn = new JButton("Save Employee");
        saveBtn.setBounds(60, 320, 130, 30);
        panel.add(saveBtn);

        back = new JButton("Back");
        back.setBounds(210, 320, 130, 30);
        panel.add(back);

        saveBtn.addActionListener(e -> saveEmployee());
        back.addActionListener(e -> {
            dispose();
            new Dashboard();
        });

        setVisible(true);
    }

    private void saveEmployee() {
        String name = nameField.getText().trim();
        String salaryStr = salaryField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();
        String work = workField.getText().trim();


        if (name.isEmpty() || salaryStr.isEmpty() || phone.isEmpty() || email.isEmpty() || work.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!phone.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 11 digits.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryStr);
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary cannot be negative.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salary must be a valid number.");
            return;
        }


        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO employees (name, salary, phone, email, gender, work) VALUES (?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, name);
            ps.setDouble(2, salary);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, gender);
            ps.setString(6, work);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee added successfully!");


            nameField.setText("");
            salaryField.setText("");
            phoneField.setText("");
            emailField.setText("");
            genderBox.setSelectedIndex(0);
            workField.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AddEmployee();
    }
}
