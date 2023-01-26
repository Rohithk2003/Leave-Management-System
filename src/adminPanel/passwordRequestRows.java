package adminPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class passwordRequestRows extends JPanel implements ActionListener {
    private final JButton passwordResetButton;
    private final String userID;
    private final JPanel parent;

    passwordRequestRows(JPanel parentComponent, String id, String dob) {
        this.userID = id;
        parent = parentComponent;
        this.setBackground(new Color(50, 50, 50));
        this.setPreferredSize(new Dimension(460, 50));
        this.setLayout(new GridLayout(1, 3, 0, 0));

        JLabel idLabel = new JLabel(id);
        JLabel toLabel = new JLabel(String.valueOf(dob));
        passwordResetButton = new JButton("Reset Password");
        passwordResetButton.addActionListener(this);

        idLabel.setHorizontalAlignment(JLabel.CENTER);
        toLabel.setHorizontalAlignment(JLabel.CENTER);

        idLabel.setFont(UIManager.getFont("h2.regular.font"));
        toLabel.setFont(UIManager.getFont("h2.regular.font"));
        passwordResetButton.setFont(UIManager.getFont("h3.regular.font"));

        idLabel.setPreferredSize(new Dimension(20, 50));
        toLabel.setPreferredSize(new Dimension(20, 50));
        passwordResetButton.setPreferredSize(new Dimension(50, 50));

        this.add(idLabel);
        this.add(toLabel);
        this.add(passwordResetButton);
        if (dob.equals("Date of birth")) {
            this.remove(passwordResetButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//
        if (e.getSource() == passwordResetButton) {
            try {
                Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
                PreparedStatement st = driver.prepareStatement("update users set password = ? where username = ?");
                st.setString(1, userID);
                st.setString(2, userID);
                st.executeUpdate();
                driver = new JDBCDriver.driverJDBC().getJDBCDriver();
                st = driver.prepareStatement("delete from passwordresetrequest where id = ?");
                st.setString(1, userID);
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Password has been updated to " + userID, "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                this.revalidate();
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}