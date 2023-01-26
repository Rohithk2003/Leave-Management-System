package adminPanel;

import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addStudentAdvisor extends addUser implements ActionListener, KeyListener {
    private final FlatTextField sectionTextField;
    private final String userType;
    private final JComboBox<String> departmentTextField;

    addStudentAdvisor(String text) {
        setupUI();

        this.userType = text;
        ui.addKeyListener(this);
        usernameTextField.addKeyListener(this);
        passwordTextField.addKeyListener(this);
        modifyUpdateDetails.setText("Add " + text);

        JLabel departmentLabel = new JLabel("Department : ");
        departmentLabel.setFont(UIManager.getFont("h2.regular.font"));
        departmentLabel.setBounds(50, 200, 130, 50);

        super.loadData();
        departmentTextField = new JComboBox<>(departments);
        departmentTextField.setBorder(null);
        departmentTextField.setFont(UIManager.getFont("h2.regular.font"));
        departmentTextField.setBounds(300, 210, 150, 30);
        departmentTextField.addKeyListener(this);
        if (userType.equals("advisor")) {
            departmentLabel.setVisible(false);
            departmentTextField.setVisible(false);

        }
        JLabel sectionLabel = new JLabel("Section : ");
        sectionLabel.setFont(UIManager.getFont("h2.regular.font"));
        sectionLabel.setBounds(50, 250, 80, 50);

        sectionTextField = new FlatTextField();
        sectionTextField.setPlaceholderText("Enter  section");
        sectionTextField.setFont(UIManager.getFont("h2.regular.font"));
        sectionTextField.setBounds(300, 260, 150, 30);
        sectionTextField.addKeyListener(this);

        ui.add(departmentLabel);
        ui.add(departmentTextField);
        ui.add(sectionLabel);
        ui.add(sectionTextField);
        saveBtn.addActionListener(this);
        System.out.println(userType);
        if (userType.equals("Advisor") || userType.equals("Hod")) {
            System.out.println("ho");
            departmentLabel.setVisible(false);
            departmentTextField.setVisible(false);
            sectionLabel.setBounds(50, 200, 80, 50);
            sectionTextField.setBounds(300, 210, 150, 30);
            usernameLabel.setBounds(50, 250, 120, 50);
            usernameTextField.setBounds(300, 260, 150, 30);
            passwordLabel.setBounds(50, 300, 120, 50);
            passwordTextField.setBounds(300, 310, 150, 30);
            saveBtn.setBounds(150, 400, saveBtn.getText().length() * 15, 40);
            this.revalidate();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        changesMade = true;
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            PreparedStatement st = null;
            try {

                if (userType.equals("Student")) {
                    st = driver.prepareStatement("select * from student where student_id = ?");
                    st.setString(1, idTextField.getText());
                    ResultSet rs = st.executeQuery();
                    if (rs.isBeforeFirst()) {
                        JOptionPane.showMessageDialog(null, "Student already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        st = driver.prepareStatement("insert into student values(?,?,?,?,?)");
                        st.setString(1, idTextField.getText());
                        st.setString(2, nameTextField.getText());
                        st.setString(3, addressTextField.getText());
                        PreparedStatement st1 = driver.prepareStatement("select secid from section where secname = ?");
                        st1.setString(1, sectionTextField.getText());
                        rs = st1.executeQuery();
                        while (rs.next()) {
                            st.setInt(4, rs.getInt("secid"));
                        }
                        st1 = driver.prepareStatement("select department_id from department where department_name = ?");
                        st1.setString(1, (String) departmentTextField.getSelectedItem());
                        rs = st1.executeQuery();
                        while (rs.next()) {
                            st.setInt(5, rs.getInt("department_id"));
                        }
                        JOptionPane.showMessageDialog(null, "Details of the " + userType + " has been created", "Success", JOptionPane.INFORMATION_MESSAGE);
                        ui.dispose();
                        st.executeUpdate();
                        st = driver.prepareStatement("insert into users values (?,?)");
                        st.setString(1, usernameTextField.getText());
                        st.setString(2, passwordTextField.getText());
                        st.executeUpdate();

                    }
                } else {
                    st = driver.prepareStatement("select * from advisor where advisor_id = ?");
                    st.setString(1, idTextField.getText());
                    ResultSet rs = st.executeQuery();
                    if (rs.isBeforeFirst()) {
                        JOptionPane.showMessageDialog(null, "Advisor already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        st = driver.prepareStatement("insert into advisor values(?,?,?,?)");
                        st.setString(1, idTextField.getText());
                        st.setString(2, nameTextField.getText());
                        st.setString(3, addressTextField.getText());
                        PreparedStatement st1 = driver.prepareStatement("select secid from section where secname = ?");
                        st1.setString(1, sectionTextField.getText());
                        rs = st1.executeQuery();
                        while (rs.next()) {
                            st.setInt(4, rs.getInt("secid"));
                        }
                        st.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Details of the " + userType + " has been created", "Success", JOptionPane.INFORMATION_MESSAGE);
                        ui.dispose();
                        st = driver.prepareStatement("insert into users values (?,?)");
                        st.setString(1, usernameTextField.getText());
                        st.setString(2, passwordTextField.getText());
                        st.executeUpdate();
                    }
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
