package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;

public class FastCash extends JFrame implements ActionListener {

    JButton b1, b2, b3, b4, b5, b6, b7;
    String pin;

    public FastCash(String pin) {
        this.pin = pin;

        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1550, 830);
        add(l3);

        JLabel label = new JLabel("SELECT WITHDRAWL AMOUNT");
        label.setBounds(445, 180, 700, 35);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("System", Font.BOLD, 23));
        l3.add(label);

        b1 = createButton("Rs. 100", 410, 274, l3);
        b2 = createButton("Rs. 500", 700, 274, l3);
        b3 = createButton("Rs. 1000", 410, 318, l3);
        b4 = createButton("Rs. 2000", 700, 318, l3);
        b5 = createButton("Rs. 5000", 410, 362, l3);
        b6 = createButton("Rs. 10000", 700, 362, l3);

        b7 = new JButton("BACK");
        b7.setForeground(Color.WHITE);
        b7.setBackground(new Color(65, 125, 128));
        b7.setBounds(700, 406, 150, 35);
        b7.addActionListener(this);
        l3.add(b7);

        setSize(1550, 1080);
        setLocation(0, 0);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JButton createButton(String text, int x, int y, JLabel parent) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(65, 125, 128));
        btn.setBounds(x, y, 150, 35);
        btn.addActionListener(this);
        parent.add(btn);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b7) {
            setVisible(false);
            new main_Class(pin);
            return;
        }

        try {
            String amountStr = ((JButton) e.getSource()).getText().substring(4);
            int amount = Integer.parseInt(amountStr);

            Connn c = new Connn();

            // Calculate balance
            ResultSet rs = c.statement.executeQuery("SELECT * FROM bank WHERE pin = '" + pin + "'");
            int balance = 0;
            while (rs.next()) {
                String type = rs.getString("type");
                int amt = Integer.parseInt(rs.getString("amount"));
                if ("Deposit".equalsIgnoreCase(type)) {
                    balance += amt;
                } else {
                    balance -= amt;
                }
            }

            if (balance < amount) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance");
                return;
            }

            Timestamp timestamp = new Timestamp(new Date().getTime());

            String query = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, 'Withdrawl', ?)";
            PreparedStatement ps = c.connection.prepareStatement(query);
            ps.setString(1, pin);
            ps.setTimestamp(2, timestamp);
            ps.setInt(3, amount);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Rs. " + amount + " Debited Successfully");

            setVisible(false);
            new main_Class(pin);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Transaction failed: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new FastCash("");
    }
}
