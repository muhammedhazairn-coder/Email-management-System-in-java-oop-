package gymsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageMemberForm extends JFrame {
    JTable memberTable;
    DefaultTableModel tableModel;

    JTextField nameField, fatherField, contactField, emailField;
    JComboBox<String> genderBox, timingBox;
    JButton updateBtn, deleteBtn, back;

    int selectedMemberId = -1;

    public ManageMemberForm() {
        setTitle("Manage Members");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 900, 500);
        panel.setBackground(new Color(105, 163, 220)); // Light blue
        add(panel);

        JLabel title = new JLabel("Member Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(300, 10, 300, 30);
        panel.add(title);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Name", "Father Name", "Contact", "Email", "Gender", "Timing"});
        memberTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBounds(20, 50, 850, 200);
        panel.add(scrollPane);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 270, 100, 25);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(100, 270, 130, 25);
        panel.add(nameField);

        JLabel fatherLabel = new JLabel("Father Name:");
        fatherLabel.setBounds(250, 270, 100, 25);
        panel.add(fatherLabel);

        fatherField = new JTextField();
        fatherField.setBounds(340, 270, 130, 25);
        panel.add(fatherField);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setBounds(500, 270, 100, 25);
        panel.add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(580, 270, 130, 25);
        panel.add(contactField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 310, 100, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 310, 130, 25);
        panel.add(emailField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(250, 310, 100, 25);
        panel.add(genderLabel);

        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setBounds(340, 310, 130, 25);
        panel.add(genderBox);

        JLabel timingLabel = new JLabel("Timing:");
        timingLabel.setBounds(500, 310, 100, 25);
        panel.add(timingLabel);

        timingBox = new JComboBox<>(new String[]{"Morning", "Evening"});
        timingBox.setBounds(580, 310, 130, 25);
        panel.add(timingBox);

        updateBtn = new JButton("Update");
        updateBtn.setBounds(200, 360, 150, 30);
        panel.add(updateBtn);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(400, 360, 150, 30);
        panel.add(deleteBtn);

        back = new JButton("Back");
        back.setBounds(600, 360, 150, 30);
        panel.add(back);

        loadMemberData();
        updateBtn.addActionListener(e -> updateMember());
        deleteBtn.addActionListener(e -> deleteMember());
        back.addActionListener(e -> {
            dispose();
            new Dashboard();
        });


        memberTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = memberTable.getSelectedRow();
                if (row >= 0) {
                    selectedMemberId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    nameField.setText(tableModel.getValueAt(row, 1).toString());
                    fatherField.setText(tableModel.getValueAt(row, 2).toString());
                    contactField.setText(tableModel.getValueAt(row, 3).toString());
                    emailField.setText(tableModel.getValueAt(row, 4).toString());
                    genderBox.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                    timingBox.setSelectedItem(tableModel.getValueAt(row, 6).toString());
                }
            }
        });

        setVisible(true);
    }

     void loadMemberData() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM members")) {

            tableModel.setRowCount(0);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("father_name"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("timing")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

     void updateMember() {
        if (selectedMemberId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to update.");
            return;
        }

        String name = nameField.getText().trim();
        String father = fatherField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();
        String timing = timingBox.getSelectedItem().toString();

        if (name.isEmpty() || father.isEmpty() || contact.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
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
            JOptionPane.showMessageDialog(this, "Contact number must be exactly 11 digits.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE members SET name=?, father_name=?, contact=?, email=?, gender=?, timing=? WHERE id=?")) {

            ps.setString(1, name);
            ps.setString(2, father);
            ps.setString(3, contact);
            ps.setString(4, email);
            ps.setString(5, gender);
            ps.setString(6, timing);
            ps.setInt(7, selectedMemberId);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member updated successfully!");
            loadMemberData();
            clearForm();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage());
        }
    }

     void deleteMember() {
        if (selectedMemberId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this member?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             PreparedStatement ps = con.prepareStatement("DELETE FROM members WHERE id=?")) {

            ps.setInt(1, selectedMemberId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member deleted successfully!");
            loadMemberData();
            clearForm();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting member: " + ex.getMessage());
        }
    }

     void clearForm() {
        selectedMemberId = -1;
        nameField.setText("");
        fatherField.setText("");
        contactField.setText("");
        emailField.setText("");
        genderBox.setSelectedIndex(0);
        timingBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new ManageMemberForm();
    }
}
