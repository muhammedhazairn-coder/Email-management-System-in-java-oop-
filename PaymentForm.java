package gymsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class PaymentForm extends JFrame {
    JComboBox<String> memberBox;
    JTextField amountField;
    JButton payBtn, back;

    public PaymentForm() {
        setTitle("Member Payment");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);


        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(105, 163, 220)); // Sea Blue
        panel.setBounds(0, 0, 400, 250);
        add(panel);

        JLabel title = new JLabel("Payment Form", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBounds(90, 10, 200, 30);
        panel.add(title);

        JLabel memberLabel = new JLabel("Select Member:");
        JLabel amountLabel = new JLabel("Amount:");
        memberLabel.setForeground(Color.WHITE);
        amountLabel.setForeground(Color.WHITE);

        memberLabel.setBounds(30, 60, 100, 25);
        amountLabel.setBounds(30, 100, 100, 25);

        memberBox = new JComboBox<>();
        amountField = new JTextField();

        memberBox.setBounds(140, 60, 200, 25);
        amountField.setBounds(140, 100, 200, 25);

        payBtn = new JButton("Make Payment");
        payBtn.setBounds(50, 150, 150, 30);
        back = new JButton("Back");
        back.setBounds(210, 150, 100, 30);

        panel.add(memberLabel);
        panel.add(amountLabel);
        panel.add(memberBox);
        panel.add(amountField);
        panel.add(payBtn);
        panel.add(back);

        back.addActionListener(e -> {
            dispose();
            new Dashboard(); // Make sure Dashboard exists
        });

        payBtn.addActionListener(e -> savePayment());

        loadMembers();

        setVisible(true);
    }

     void loadMembers() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name FROM members")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                memberBox.addItem(id + " - " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading members");
        }
    }

     void savePayment() {
        if (memberBox.getSelectedItem() == null || amountField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        try {
            String selected = memberBox.getSelectedItem().toString();
            int memberId = Integer.parseInt(selected.split(" - ")[0]);
            String memberName = selected.split(" - ")[1];
            double amount = Double.parseDouble(amountField.getText().trim());
            LocalDate today = LocalDate.now();

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO payments (member_id, member_name, amount, payment_date) VALUES (?, ?, ?, ?)");
            ps.setInt(1, memberId);
            ps.setString(2, memberName);
            ps.setDouble(3, amount);
            ps.setDate(4, Date.valueOf(today));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Payment saved successfully!");
            amountField.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving payment.");
        }
    }

    public static void main(String[] args) {
        new PaymentForm();
    }
}
