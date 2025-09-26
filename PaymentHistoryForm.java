package gymsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PaymentHistoryForm extends JFrame {
    JTable paymentTable;
    DefaultTableModel tableModel;

    public PaymentHistoryForm() {
        setTitle("Payment History");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());



        JButton back = new JButton("Back");
        back.setBounds(100,300,150,30);
        add(back);

        back.addActionListener(e ->{
            dispose();
            new Dashboard();
        });

        JLabel title = new JLabel("All Payment Records", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);



        // Table
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[] {
                "Payment ID", "Member ID", "Member Name", "Amount", "Date"});
        paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBounds(20, 100, 500, 200);
        add(scrollPane);


        loadPaymentData();

        setVisible(true);
    }

     void loadPaymentData() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gymdb", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM payments")) {

            tableModel.setRowCount(0);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading payment history.");
        }
    }

    public static void main(String[] args) {
        new PaymentHistoryForm();
    }
}
