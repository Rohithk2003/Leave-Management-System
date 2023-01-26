package adminPanel;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class addUser extends JFrame implements DocumentListener {
    protected JFrame ui;
    protected boolean changesMade;
    protected JLabel usernameTextField;
    protected FlatTextField nameTextField;
    protected FlatTextField passwordTextField;
    protected FlatTextField idTextField;
    protected FlatButton saveBtn;
    protected JLabel usernameLabel;
    protected JLabel passwordLabel;
    protected JLabel modifyUpdateDetails;
    protected FlatTextField addressTextField;

    protected String[] departments;
    protected Connection driver;

    protected void loadData() {
        try {
            this.driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            PreparedStatement st = driver.prepareStatement("select count(*) from department");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                departments = new String[rs.getInt(1)];
            }
            st = driver.prepareStatement("select department_name from department");
            rs = st.executeQuery();
            int i = 0;
            while (rs.next()) {
                departments[i] = rs.getString("department_name");
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setupUI() {
        ui = new JFrame();
        FlatMacDarkLaf.setup();

        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (changesMade) {
                    int a = JOptionPane.showConfirmDialog(ui, "Did u save your changes?");
                    if (a == JOptionPane.YES_OPTION) {
                        ui.dispose();
                    }
                } else {
                    ui.dispose();
                }
            }
        });

        ui.setLayout(null);
        ui.setResizable(false);
        ui.setVisible(true);
        ui.setSize(500, 600);
        ui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ui.setLocationRelativeTo(null);

        modifyUpdateDetails = new JLabel("Add" + " Student");
        modifyUpdateDetails.setFont(UIManager.getFont("h0.font"));
        modifyUpdateDetails.setHorizontalAlignment(JLabel.CENTER);
        modifyUpdateDetails.setBounds(0, 0, ui.getWidth(), 50);

        JLabel nameLabel = new JLabel("Name : ");
        nameLabel.setFont(UIManager.getFont("h2.regular.font"));
        nameLabel.setBounds(50, 50, 80, 50);

        nameTextField = new FlatTextField();
        nameTextField.setPlaceholderText("Enter name");
        nameTextField.setFont(UIManager.getFont("h2.regular.font"));
        nameTextField.setBounds(300, 60, 150, 30);
        nameTextField.getDocument().addDocumentListener(this);

        JLabel idLabel = new JLabel("ID : ");
        idLabel.setFont(UIManager.getFont("h2.regular.font"));
        idLabel.setBounds(50, 100, 80, 50);

        idTextField = new FlatTextField();
        idTextField.setPlaceholderText("Enter ID");
        idTextField.setFont(UIManager.getFont("h2.regular.font"));
        idTextField.setBounds(300, 110, 150, 30);
        idTextField.getDocument().addDocumentListener(this);

        JLabel addressLabel = new JLabel("Address : ");
        addressLabel.setFont(UIManager.getFont("h2.regular.font"));
        addressLabel.setBounds(50, 150, 80, 50);

        addressTextField = new FlatTextField();
        addressTextField.setPlaceholderText("Enter Address");
        addressTextField.setFont(UIManager.getFont("h2.regular.font"));
        addressTextField.setBounds(300, 160, 150, 30);

        saveBtn = new FlatButton();
        saveBtn.setText("Save and Exit");
        saveBtn.setFont(UIManager.getFont("h2.regular.font"));
        saveBtn.setBounds(150, 480, saveBtn.getText().length() * 15, 40);

        usernameLabel = new JLabel("Username : ");
        usernameLabel.setFont(UIManager.getFont("h2.regular.font"));
        usernameLabel.setBounds(50, 300, 120, 50);

        usernameTextField = new JLabel();
        usernameTextField.setFont(UIManager.getFont("h2.regular.font"));
        usernameTextField.setBounds(300, 310, 150, 30);

        passwordLabel = new JLabel("Password : ");
        passwordLabel.setFont(UIManager.getFont("h2.regular.font"));
        passwordLabel.setBounds(50, 350, 120, 50);

        passwordTextField = new FlatTextField();
        passwordTextField.setPlaceholderText("Enter  password");
        passwordTextField.setFont(UIManager.getFont("h2.regular.font"));
        passwordTextField.setBounds(300, 360, 150, 30);

        ui.add(modifyUpdateDetails);
        ui.add(nameLabel);
        ui.add(nameTextField);
        ui.add(idLabel);
        ui.add(idTextField);
        ui.add(addressLabel);
        ui.add(addressTextField);

        ui.add(usernameLabel);
        ui.add(usernameTextField);
        ui.add(passwordLabel);
        ui.add(passwordTextField);
        ui.add(saveBtn);

    }


    @Override
    public void insertUpdate(DocumentEvent e) {
        usernameTextField.setText(idTextField.getText());
        ui.revalidate();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
