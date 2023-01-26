package login;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class passwordResetRequestFrame implements ActionListener {
    private final FlatTextField idTextField;
    private final FlatTextField DOBTextField;
    private final JFrame frame;


    passwordResetRequestFrame() {
        FlatMacDarkLaf.setup();
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);


        JLabel passwordResetLabel = new JLabel("Reset Password");
        passwordResetLabel.setBounds(0, 40, 400, 50);
        passwordResetLabel.setHorizontalAlignment(JLabel.CENTER);
        passwordResetLabel.setFont(UIManager.getFont("h00.font"));

        JLabel idLabel = new JLabel("ID : ");
        idLabel.setBounds(10, 140, 80, 60);
        idLabel.setFont(UIManager.getFont("h2.regular.font"));

        JLabel dateOfBirthLabel = new JLabel("DOB(YYYY-MM-DD) : ");
        dateOfBirthLabel.setBounds(10, 210, 180, 60);
        dateOfBirthLabel.setFont(UIManager.getFont("h2.regular.font"));

        idTextField = new FlatTextField();
        idTextField.setPlaceholderText("Enter your id");
        idTextField.setFont(UIManager.getFont("h2.regular.font"));
        idTextField.setBounds(210, 150, 150, 40);

        DOBTextField = new FlatTextField();
        DOBTextField.setPlaceholderText("Enter your DOB");
        DOBTextField.setFont(UIManager.getFont("h2.regular.font"));
        DOBTextField.setBounds(210, 220, 150, 40);

        FlatButton requestBtn = new FlatButton();
        requestBtn.setButtonType(FlatButton.ButtonType.roundRect);
        requestBtn.setText("Request");
        requestBtn.setFont(UIManager.getFont("h2.regular.font"));
        requestBtn.setBounds(150, 300, 100, 40);
        requestBtn.addActionListener(this);

        frame.add(passwordResetLabel);
        frame.add(dateOfBirthLabel);
        frame.add(idLabel);
        frame.add(idTextField);
        frame.add(DOBTextField);
        frame.add(requestBtn);
    }

    private boolean checkIdExists() {
        try {
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            PreparedStatement st = driver.prepareStatement("select password from users where username = ?");
            st.setString(1, idTextField.getText());
            ResultSet rs = st.executeQuery();
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "ID doesnt Exists", "Error", JOptionPane.PLAIN_MESSAGE);
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            if (checkIdExists()) {
                PreparedStatement st = driver.prepareStatement("insert into passwordResetRequest values(?,?)");
                st.setString(1, idTextField.getText());
                st.setDate(2, Date.valueOf(DOBTextField.getText()));
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Request has been successfully created", "Success", JOptionPane.PLAIN_MESSAGE);
                frame.dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Request already exists", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
