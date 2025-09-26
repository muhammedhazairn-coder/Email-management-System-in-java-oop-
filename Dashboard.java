package gymsystem;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Gym Management Dashboard");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        ImageIcon bgIcon = new ImageIcon("C:\\Users\\HP\\OneDrive\\Pictures\\maxresdefault.jpg");
        Image img = bgIcon.getImage().getScaledInstance(1000, 650, Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(img);
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 1000, 500);
        background.setLayout(null);
        add(background); // Add background first

        JLabel title = new JLabel("Admin ", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(80, 20, 250, 50);
        background.add(title);

        JButton addMemberBtn = new JButton("Add Member");
        addMemberBtn.setBounds(150, 80, 200, 50);
        background.add(addMemberBtn);

        JButton addTrainerBtn = new JButton("Add Trainer");
        addTrainerBtn.setBounds(400, 80, 200, 50);
        background.add(addTrainerBtn);

        JButton manageMemberBtn = new JButton("Manage Member");
        manageMemberBtn.setBounds(150, 160, 200, 50);
        background.add(manageMemberBtn);

        JButton manageTrainerBtn = new JButton("Manage Trainer");
        manageTrainerBtn.setBounds(400, 160, 200, 50);
        background.add(manageTrainerBtn);

        JButton paymentBtn = new JButton("Payment");
        paymentBtn.setBounds(150, 240, 200, 50);
        background.add(paymentBtn);

        JButton paymentHistoryBtn = new JButton("Payment History");
        paymentHistoryBtn.setBounds(400, 240, 200, 50);
        background.add(paymentHistoryBtn);

        JButton addEmployeeBtn = new JButton("Add Employee");
        addEmployeeBtn.setBounds(150, 320, 200, 50);
        background.add(addEmployeeBtn);

        JButton manageEmployeeBtn = new JButton("Manage Employee");
        manageEmployeeBtn.setBounds(400, 320, 200, 50);
        background.add(manageEmployeeBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(30, 400, 150, 30);
        background.add(backBtn);

        addMemberBtn.addActionListener(e -> {
            dispose();
            new AddMemberForm();
        });

        addTrainerBtn.addActionListener(e -> {
            dispose();
            new AddTrainerForm();
        });

        manageMemberBtn.addActionListener(e -> {
            dispose();
            new ManageMemberForm();
        });

        manageTrainerBtn.addActionListener(e -> {
            dispose();
            new ManageTrainerForm();
        });

        paymentBtn.addActionListener(e -> {
            dispose();
            new PaymentForm();
        });

        paymentHistoryBtn.addActionListener(e -> {
            dispose();
            new PaymentHistoryForm();
        });

        addEmployeeBtn.addActionListener(e -> {
            dispose();
            new AddEmployee();
        });

        manageEmployeeBtn.addActionListener(e -> {
            dispose();
            new ManageEmployee(); // You should create this class separately
        });

        backBtn.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}
