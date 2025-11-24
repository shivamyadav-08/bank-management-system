package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class mini extends JFrame implements ActionListener {
    String pin;
    JButton button;

    public mini(String pin) {
        this.pin = pin;
        getContentPane().setBackground(new Color(255, 204, 204));
        setSize(400, 600);
        setLocation(20, 20);
        setLayout(null);

        // Label to accumulate transaction text
        JLabel label1 = new JLabel();
        label1.setBounds(20, 140, 360, 300);
        label1.setVerticalAlignment(JLabel.TOP);
        add(label1);

        JLabel label2 = new JLabel("SUS Bank Of India");
        label2.setFont(new Font("System", Font.BOLD, 15));
        label2.setBounds(150, 20, 200, 20);
        add(label2);

        JLabel label3 = new JLabel();
        label3.setBounds(20, 80, 350, 20);
        add(label3);

        JLabel label4 = new JLabel();
        label4.setBounds(20, 460, 350, 20);
        label4.setFont(new Font("System", Font.BOLD, 14));
        add(label4);

        try {
            Connn c = new Connn();
            ResultSet resultSet = c.statement.executeQuery("SELECT * FROM login WHERE pin = '" + pin + "'");
            if (resultSet.next()) {
                String cardNum = resultSet.getString("card_number");
                String maskedCard = cardNum.substring(0, 4) + "XXXXXXXX" + cardNum.substring(12);
                label3.setText("Card Number: " + maskedCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            int balance = 0;
            Connn c = new Connn();
            ResultSet resultSet = c.statement.executeQuery("SELECT * FROM bank WHERE pin = '" + pin + "'");
            StringBuilder transactions = new StringBuilder("<html>");

            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String type = resultSet.getString("type");
                String amount = resultSet.getString("amount");

                transactions.append(date).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
                            .append(type).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
                            .append(amount).append("<br><br>");

                if ("Deposit".equalsIgnoreCase(type)) {
                    balance += Integer.parseInt(amount);
                } else {
                    balance -= Integer.parseInt(amount);
                }
            }

            transactions.append("</html>");
            label1.setText(transactions.toString());

            label4.setText("Your Total Balance is Rs " + balance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        button = new JButton("Exit");
        button.setBounds(20, 520, 100, 25);
        button.addActionListener(this);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        add(button);

        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        new mini(""); // Pass valid PIN for real use
    }
}
