package userWindow;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import passwordEncryption.Solution;
import users.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class modifyDetailsPage extends JFrame implements KeyListener, ActionListener {
    private final String address;
    private final String id;
    private final String name;
    private final Connection driver;
    private final String currentUserType;
    private FlatTextField nameTextField;
    private JPasswordField passwordTextField;
    private FlatButton saveBtn;
    private FlatTextField addressTextField;

    public modifyDetailsPage(student st, faculty f, advisor a, hod h, String currentUserType) {
        this.currentUserType = currentUserType;
        driver = new JDBCDriver.driverJDBC().getJDBCDriver();
        this.name = st.getName() + f.getName() + a.getName() + h.getName();
        this.address = st.getAddress() + f.getAddress() + a.getAddress() + h.getAddress();
        this.id = st.getId() + f.getFacultyId() + a.getAdvisorId() + h.getHodId();
        setupUI();
    }

    public modifyDetailsPage(user user, String currentUserType) {
        this.currentUserType = currentUserType;
        driver = new JDBCDriver.driverJDBC().getJDBCDriver();
        this.name = user.getName();
        this.address = user.getAddress();
        this.id = user.getId();
        setupUI();
    }


    private void setupUI() {
        JFrame userI = new JFrame();
        FlatMacDarkLaf.setup();

        userI.setLayout(null);
        userI.setResizable(false);
        userI.setSize(500, 500);

        JLabel modifyUpdateDetails = new JLabel("Modify Details");
        modifyUpdateDetails.setFont(UIManager.getFont("h0.font"));
        modifyUpdateDetails.setHorizontalAlignment(JLabel.CENTER);
        modifyUpdateDetails.setBounds(0, 0, userI.getWidth(), 50);

        JLabel nameLabel = new JLabel("Name : ");
        nameLabel.setFont(UIManager.getFont("h2.regular.font"));
        nameLabel.setBounds(50, 50, 80, 50);

        nameTextField = new FlatTextField();
        nameTextField.setText(name);
        nameTextField.setFont(UIManager.getFont("h2.regular.font"));
        nameTextField.setBounds(300, 60, 150, 30);

        JLabel idLabel = new JLabel("ID : ");
        idLabel.setFont(UIManager.getFont("h2.regular.font"));
        idLabel.setBounds(50, 100, 80, 50);

        JLabel idTextField = new JLabel();
        idTextField.setText(this.id);
        idTextField.setFont(UIManager.getFont("h2.regular.font"));
        idTextField.setBounds(302, 110, 150, 30);

        JLabel addressLabel = new JLabel("Address : ");
        addressLabel.setFont(UIManager.getFont("h2.regular.font"));
        addressLabel.setBounds(50, 150, 80, 50);

        addressTextField = new FlatTextField();
        addressTextField.setText(this.address);
        addressTextField.setFont(UIManager.getFont("h2.regular.font"));
        addressTextField.setBounds(300, 160, 150, 30);

        JLabel usernameLabel = new JLabel("Username : ");
        usernameLabel.setFont(UIManager.getFont("h2.regular.font"));
        usernameLabel.setBounds(50, 200, 120, 50);

        JLabel userValue = new JLabel(this.id);
        userValue.setFont(UIManager.getFont("h2.regular.font"));
        userValue.setBounds(302, 200, 120, 50);

        JLabel passwordLabel = new JLabel("Password : ");
        passwordLabel.setFont(UIManager.getFont("h2.regular.font"));
        passwordLabel.setBounds(50, 250, 120, 50);

        passwordTextField = new JPasswordField();
        passwordTextField.setFont(UIManager.getFont("h2.regular.font"));
        passwordTextField.setBounds(300, 260, 150, 30);


        saveBtn = new FlatButton();
        saveBtn.setText("Save and Exit");
        saveBtn.setFont(UIManager.getFont("h2.regular.font"));
        saveBtn.addActionListener(this);
        saveBtn.setBounds(150, 380, saveBtn.getText().length() * 15, 40);

        userI.add(modifyUpdateDetails);
        userI.add(nameLabel);
        userI.add(nameTextField);
        userI.add(idLabel);
        userI.add(idTextField);
        userI.add(addressLabel);
        userI.add(addressTextField);
        userI.add(usernameLabel);
        userI.add(userValue);
        userI.add(passwordLabel);
        userI.add(passwordTextField);
        userI.add(saveBtn);
        userI.setVisible(true);
        userI.setLocationRelativeTo(null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            if (!(new String(passwordTextField.getPassword()).equals(""))) {
                try {
                    PreparedStatement st = driver.prepareStatement("update users set password = ? where username = ?");
                    st.setString(1, Solution.encrypt(new String(passwordTextField.getPassword()), 10));
                    st.setString(2, this.id);
                    st.executeUpdate();
                    JOptionPane.showMessageDialog(
                            null, "Password has been successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (!addressTextField.getText().equals("") && !nameTextField.getText().equals("")) {
                try {
                    PreparedStatement st = driver.prepareStatement("update " + currentUserType + " set address = ? , " + currentUserType + "_name = ? where " + currentUserType + "_id = ?");
                    st.setString(1, addressTextField.getText());
                    st.setString(2, nameTextField.getText());
                    st.setString(3, this.id);
                    st.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Details has been successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ignored) {
                }
            } else {
                JOptionPane.showMessageDialog(null, "Details cannot be empty", "Null details", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
