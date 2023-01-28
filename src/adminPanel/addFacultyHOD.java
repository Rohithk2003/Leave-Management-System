package adminPanel;

import passwordEncryption.Solution;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addFacultyHOD extends addUser implements ActionListener, KeyListener {
    private final String userType;
    protected JComboBox<String> departmentTextField;


    addFacultyHOD(String text) {
        setupUI();
        this.userType = text;
        modifyUpdateDetails.setText("Add " + text);
        ui.addKeyListener(this);
        usernameTextField.addKeyListener(this);
        passwordTextField.addKeyListener(this);
        usernameLabel.setBounds(50, 250, 120, 50);
        usernameTextField.setBounds(300, 260, 150, 30);
        passwordLabel.setBounds(50, 300, 120, 50);
        passwordTextField.setBounds(300, 310, 150, 30);
        saveBtn.setBounds(150, 430, saveBtn.getText().length() * 15, 40);

        JLabel departmentLabel = new JLabel("Department : ");
        departmentLabel.setFont(UIManager.getFont("h2.regular.font"));
        departmentLabel.setBounds(50, 200, 130, 50);
        super.loadData();
        departmentTextField = new JComboBox<>(departments);
        departmentTextField.setBorder(null);
        departmentTextField.setFont(UIManager.getFont("h2.regular.font"));
        departmentTextField.setBounds(300, 210, 150, 30);
        departmentTextField.addKeyListener(this);

        ui.add(departmentLabel);
        ui.add(departmentTextField);
        saveBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            try {
                PreparedStatement st = driver.prepareStatement("select * from " + userType + " where " + userType + "_id = ?");
                st.setString(1, idTextField.getText());
                ResultSet rs = st.executeQuery();
                if (rs.isBeforeFirst()) {
                    JOptionPane.showMessageDialog(null, userType + " already exists", "Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    st = super.driver.prepareStatement("insert into " + userType + " values(?,?,?,?)");
                    st.setString(1, idTextField.getText());
                    st.setString(2, nameTextField.getText());
                    st.setString(3, addressTextField.getText());
                    PreparedStatement st1 = driver.prepareStatement("select department_id from department where department_name = ?");
                    st1.setString(1, (String) departmentTextField.getSelectedItem());
                    rs = st1.executeQuery();
                    while (rs.next()) {
                        st.setInt(4, rs.getInt("department_id"));
                    }
                    st.executeUpdate();
                    st = driver.prepareStatement("insert into users values (?,?)");
                    st.setString(1, usernameTextField.getText());
                    st.setString(2, Solution.encrypt(passwordTextField.getText(), 10));
                    st.executeUpdate();
                    rs.next();
                    JOptionPane.showMessageDialog(null, "Details of the " + userType + " has been created", "Success", JOptionPane.INFORMATION_MESSAGE);
                    ui.dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
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
