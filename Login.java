package gymsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame {

    JTextField userField;
    JPasswordField passField;
    JButton loginButton;

    public Login() {
        setTitle("Gym Management System - Login");
        setSize(1250, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);


        ImageIcon bgIcon = new ImageIcon("C:\\Users\\HP\\OneDrive\\Pictures\\istockphoto-466487479-612x612.jpg");
        Image img = bgIcon.getImage().getScaledInstance(1250, 650, Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(img);
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 1250, 650);
        background.setLayout(null);




        JLabel titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(600, 100, 200, 100);
        background.add(titleLabel);


        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(500, 250, 100, 25);
        userLabel.setForeground(Color.white); // Set foreground color
        background.add(userLabel);

        userField = new JTextField();
        userField.setBounds(600, 250, 180, 25);
        userField.setForeground(Color.blue);
        background.add(userField);


        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(500, 290, 100, 25);
        passLabel.setForeground(Color.white);
        background.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(600, 290, 180, 25);
        passField.setForeground(Color.blue);
        background.add(passField);


        loginButton = new JButton("Login");
        loginButton.setBounds(550, 350, 100, 30);
        background.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = String.valueOf(passField.getPassword());

                if (username.equals("admin") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(Login.this, "Login successful!");
                    dispose();
                    new Dashboard();
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Invalid credentials. Try again.");
                }
            }
        });

        setContentPane(background);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}
