package gymsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageTrainerForm extends JFrame {
    JTable trainerTable;
    DefaultTableModel model;
    JTextField nameField, specField, phoneField, emailField;
    JComboBox<String> genderBox;
    JButton updateBtn, deleteBtn, back;

    int selectedTrainerId = -1;

    public ManageTrainerForm() {
        setTitle("Manage Trainers");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(105, 163, 220));
        backgroundPanel.setBounds(0, 0, 900, 550);
        backgroundPanel.setLayout(null);
        add(backgroundPanel);

        JLabel heading = new JLabel("Manage Trainer", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBounds(250, 10, 300, 30);
        backgroundPanel.add(heading);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Specialization", "Phone", "Email", "Gender"});
        trainerTable = new JTable(model);
        JScrollPane pane = new JScrollPane(trainerTable);
        pane.setBounds(30, 60, 850, 200);
        backgroundPanel.add(pane);

        JLabel nameLbl = new JLabel("Name:");
        JLabel specLbl = new JLabel("Specialization:");
        JLabel phoneLbl = new JLabel("Phone:");
        JLabel emailLbl = new JLabel("Email:");
        JLabel genderLbl = new JLabel("Gender:");

        nameLbl.setBounds(50, 270, 100, 25);
        specLbl.setBounds(50, 310, 100, 25);
        phoneLbl.setBounds(50, 370, 100, 25);
        emailLbl.setBounds(400, 270, 100, 25);
        genderLbl.setBounds(400, 310, 100, 25);

        nameField = new JTextField();
        specField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        nameField.setBounds(150, 270, 200, 25);
        specField.setBounds(150, 310, 200, 25);
        phoneField.setBounds(150, 370, 200, 25);
        emailField.setBounds(500, 270, 200, 25);
        genderBox.setBounds(500, 310, 200, 25);

        backgroundPanel.add(nameLbl);
        backgroundPanel.add(specLbl);
        backgroundPanel.add(phoneLbl);
        backgroundPanel.add(emailLbl);
        backgroundPanel.add(genderLbl);
        backgroundPanel.add(nameField);
        backgroundPanel.add(specField);
        backgroundPanel.add(phoneField);
        backgroundPanel.add(emailField);
        backgroundPanel.add(genderBox);

        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        back = new JButton("Back");

        updateBtn.setBounds(200, 420, 120, 30);
        deleteBtn.setBounds(350, 420, 120, 30);
        back.setBounds(500, 420, 120, 30);

        backgroundPanel.add(updateBtn);
        backgroundPanel.add(deleteBtn);
        backgroundPanel.add(back);

        back.addActionListener(e -> {
            dispose();
            new Dashboard();
        });

        trainerTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = trainerTable.getSelectedRow();
                selectedTrainerId = Integer.parseInt(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                specField.setText(model.getValueAt(row, 2).toString());
                phoneField.setText(model.getValueAt(row, 3).toString());
                emailField.setText(model.getValueAt(row, 4).toString());
                genderBox.setSelectedItem(model.getValueAt(row, 5).toString());
            }
        });

        updateBtn.addActionListener(e -> updateTrainer());
        deleteBtn.addActionListener(e -> deleteTrainer());

        loadTrainers();
        setVisible(true);
    }

     void loadTrainers() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM trainers")) {
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("gender")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

     void updateTrainer() {
        if (selectedTrainerId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a trainer first.");
            return;
        }

        String name = nameField.getText().trim();
        String spec = specField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();

        if (name.isEmpty() || spec.isEmpty() || phone.isEmpty() || email.isEmpty()) {
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

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE trainers SET name=?, specialization=?, phone=?, email=?, gender=? WHERE id=?")) {

            ps.setString(1, name);
            ps.setString(2, spec);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, gender);
            ps.setInt(6, selectedTrainerId);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Trainer updated successfully.");
            loadTrainers();
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

     void deleteTrainer() {
        if (selectedTrainerId == -1) {
            JOptionPane.showMessageDialog(this, "Select a trainer to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
                 PreparedStatement ps = con.prepareStatement("DELETE FROM trainers WHERE id=?")) {
                ps.setInt(1, selectedTrainerId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Trainer deleted.");
                loadTrainers();
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

     void clearFields() {
        nameField.setText("");
        specField.setText("");
        phoneField.setText("");
        emailField.setText("");
        genderBox.setSelectedIndex(0);
        selectedTrainerId = -1;
    }

    public static void main(String[] args) {
        new ManageTrainerForm();
    }
}
