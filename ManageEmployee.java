package gymsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageEmployee extends JFrame {
    JTable employeeTable;
    DefaultTableModel model;
    JTextField nameField, salaryField, phoneField, workField, emailField;
    JComboBox<String> genderBox;
    JButton updateBtn, deleteBtn, back;
    int selectedEmployeeId = -1;

    public ManageEmployee() {
        setTitle("Manage Employee");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 900, 550);
        panel.setBackground(new Color(105, 163, 220)); // Light Blue
        add(panel);

        JLabel heading = new JLabel("Manage Employee", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBounds(250, 10, 400, 30);
        panel.add(heading);


        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Salary", "Phone", "Email", "Gender", "Work"});
        employeeTable = new JTable(model);
        JScrollPane pane = new JScrollPane(employeeTable);
        pane.setBounds(20, 60, 850, 200);
        panel.add(pane);


        JLabel nameLbl = new JLabel("Name:");
        JLabel salaryLbl = new JLabel("Salary:");
        JLabel phoneLbl = new JLabel("Phone:");
        JLabel emailLbl = new JLabel("Email:");
        JLabel genderLbl = new JLabel("Gender:");
        JLabel workLbl = new JLabel("Work:");

        nameLbl.setBounds(50, 270, 100, 25);
        salaryLbl.setBounds(50, 310, 100, 25);
        phoneLbl.setBounds(50, 350, 100, 25);
        emailLbl.setBounds(400, 270, 100, 25);
        genderLbl.setBounds(400, 310, 100, 25);
        workLbl.setBounds(400, 350, 100, 25);


        nameField = new JTextField();
        salaryField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        workField = new JTextField();
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        nameField.setBounds(150, 270, 200, 25);
        salaryField.setBounds(150, 310, 200, 25);
        phoneField.setBounds(150, 350, 200, 25);
        emailField.setBounds(500, 270, 200, 25);
        genderBox.setBounds(500, 310, 200, 25);
        workField.setBounds(500, 350, 200, 25);


        panel.add(nameLbl);      panel.add(nameField);
        panel.add(salaryLbl);    panel.add(salaryField);
        panel.add(phoneLbl);     panel.add(phoneField);
        panel.add(emailLbl);     panel.add(emailField);
        panel.add(genderLbl);    panel.add(genderBox);
        panel.add(workLbl);      panel.add(workField);


        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        back = new JButton("Back");

        updateBtn.setBounds(200, 400, 130, 30);
        deleteBtn.setBounds(350, 400, 130, 30);
        back.setBounds(500, 400, 130, 30);

        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(back);

        back.addActionListener(e -> {
            dispose();
            new Dashboard();
        });


        loadEmployees();


        employeeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = employeeTable.getSelectedRow();
                selectedEmployeeId = Integer.parseInt(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                salaryField.setText(model.getValueAt(row, 2).toString());
                phoneField.setText(model.getValueAt(row, 3).toString());
                emailField.setText(model.getValueAt(row, 4).toString());
                genderBox.setSelectedItem(model.getValueAt(row, 5).toString());
                workField.setText(model.getValueAt(row, 6).toString());
            }
        });

        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());

        setVisible(true);
    }

    private void loadEmployees() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("salary"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("work")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateEmployee() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee first.");
            return;
        }

        String name = nameField.getText().trim();
        String salaryStr = salaryField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();
        String work = workField.getText().trim();


        if (name.isEmpty() || salaryStr.isEmpty() || phone.isEmpty() || email.isEmpty() || work.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
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
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Salary must be a valid number.");
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE employees SET name=?, salary=?, phone=?, email=?, gender=?, work=? WHERE id=?")) {

            ps.setString(1, name);
            ps.setDouble(2, salary);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, gender);
            ps.setString(6, work);
            ps.setInt(7, selectedEmployeeId);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee updated successfully.");
            loadEmployees();
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

     void deleteEmployee() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
                 PreparedStatement ps = con.prepareStatement("DELETE FROM employees WHERE id=?")) {
                ps.setInt(1, selectedEmployeeId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee deleted.");
                loadEmployees();
                clearFields();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

     void clearFields() {
        nameField.setText("");
        salaryField.setText("");
        phoneField.setText("");
        emailField.setText("");
        workField.setText("");
        genderBox.setSelectedIndex(0);
        selectedEmployeeId = -1;
    }

    public static void main(String[] args) {
        new ManageEmployee();
    }
}
