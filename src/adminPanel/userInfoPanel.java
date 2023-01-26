package adminPanel;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;
import imageData.imageData;
import userWindow.modifyDetailsPage;
import users.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class userInfoPanel extends JPanel implements ActionListener {
    private final JPanel parent;
    private final user uSER;
    private final String currentUser;
    private final String userId;
    private FlatButton deleteUser;
    private FlatButton resetPassword;
    private FlatButton modifyDetails;

    userInfoPanel(JPanel parentComponent, user user) {
        parent = parentComponent;
        this.uSER = user;
        currentUser = user.getCurrentUser();
        userId = user.getId();
        setupPanel();
    }

    void setupPanel() {
        this.setPreferredSize(new Dimension(1300, 200));
        this.setBackground(new Color(30, 30, 30));
        this.setBorder(BorderFactory.createLineBorder(Color.white));
        this.setLayout(null);

        JLabel userIcon = new JLabel();
        userIcon.setIcon(imageData.userIcon);
        userIcon.setBounds(0, 0, 200, 200);
        userIcon.setVerticalAlignment(JLabel.CENTER);
        userIcon.setHorizontalAlignment(JLabel.CENTER);
        String displayTextDeptSec = "";
        if (uSER.getCurrentUser().equals("hod") || uSER.getCurrentUser().equals("faculty"))
            displayTextDeptSec = "Dept : ";
        else if (uSER.getCurrentUser().equals("advisor"))
            displayTextDeptSec = "Section : ";
        else
            displayTextDeptSec = "Class :";
        JLabel nameText = new JLabel("Name:" + uSER.getName());
        nameText.setFont(UIManager.getFont("h2.font"));
        nameText.setBounds(220, 20, 200, 70);

        JLabel idText = new JLabel("ID:" + uSER.getId());
        idText.setFont(UIManager.getFont("h2.font"));
        idText.setBounds(220, 50, 200, 70);

        JLabel addressText = new JLabel("Address: " + uSER.getAddress());
        addressText.setFont(UIManager.getFont("h2.font"));
        addressText.setBounds(220, 80, ((addressText.getText().length()) * 9) + 100, 70);

        FlatLabel currentClass = new FlatLabel();
        currentClass.setText(displayTextDeptSec + uSER.getDeptName() + " " + uSER.getSection());
        currentClass.setFont(UIManager.getFont("h2.font"));
        currentClass.setBounds(220, 110, 200, 70);

        modifyDetails = new FlatButton();
        modifyDetails.setText("Modify Details");
        modifyDetails.setIcon(imageData.editIcon);
        modifyDetails.addActionListener(this);
        modifyDetails.setHorizontalTextPosition(FlatButton.RIGHT);
        modifyDetails.setFont(UIManager.getFont("h2.font"));
        modifyDetails.setFocusable(false);
        modifyDetails.setBounds(950, 15, 200, 50);

        deleteUser = new FlatButton();
        deleteUser.setText("Delete " + uSER.getCurrentUser());
        deleteUser.setFocusable(false);
        deleteUser.addActionListener(this);
        deleteUser.setIcon(imageData.deleteIcon);
        deleteUser.setHorizontalTextPosition(FlatButton.RIGHT);
        deleteUser.setFont(UIManager.getFont("h2.font"));
        deleteUser.setBounds(950, 75, 200, 50);

        resetPassword = new FlatButton();
        resetPassword.setText("Reset Password");
        resetPassword.setIcon(imageData.resetIcon);
        resetPassword.setHorizontalTextPosition(FlatButton.RIGHT);
        resetPassword.setFont(UIManager.getFont("h2.font"));
        resetPassword.setFocusable(false);
        resetPassword.addActionListener(this);
        resetPassword.setBounds(950, 135, 200, 50);

        this.add(userIcon);
        this.add(nameText);
        this.add(idText);
        this.add(addressText);
        this.add(currentClass);
        this.add(modifyDetails);
        this.add(deleteUser);
        this.add(resetPassword);
        parent.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteUser) {
            parent.remove(this);
            parent.repaint();
            try {
                Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
                PreparedStatement st = driver.prepareStatement("delete from " + currentUser + " where " + currentUser + "_id = ?");
                st.setString(1, userId);
                st.executeUpdate();
                st = driver.prepareStatement("delete from leave_records where id = ?");
                st.setString(1, userId);
                st.executeUpdate();
                st = driver.prepareStatement("delete from users where username = ?");
                st.setString(1, userId);
                st.executeUpdate();

                JOptionPane.showMessageDialog(null, "User's details has been removed from the database");
                parent.revalidate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == resetPassword) {
            try {
                Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
                PreparedStatement st = driver.prepareStatement("update users set password = ? where username = ?");
                st.setString(1, userId);
                st.setString(2, userId);
                st.executeUpdate();
                System.out.println(uSER.getId());
                JOptionPane.showMessageDialog(null, "Password is now " + userId);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == modifyDetails) {
            new modifyDetailsPage(uSER, currentUser);

        }
    }
}
