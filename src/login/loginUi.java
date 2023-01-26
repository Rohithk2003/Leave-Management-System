package login;

import adminPanel.adminPanelUi;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatPasswordField;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import imageData.imageData;
import userWindow.approvalUser;
import userWindow.mainUi;
import users.login_user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginUi implements ActionListener, MouseListener {
    protected final login_user currentUser;
    private final FlatPasswordField passInput;
    private final FlatTextField userInput;
    private final FlatButton adminPanelButton;
    private final FlatButton submit;
    private final FlatButton forgotPassBtn;
    public JFrame frame;

    public loginUi() {
        currentUser = new login_user();
        try {
            FlatMacDarkLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Sign In");
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        FlatLabel loginTextValue = new FlatLabel();
        loginTextValue.setText("Sign In");
        loginTextValue.setFont(new Font("Segue Ui", Font.PLAIN, 40));
        loginTextValue.setBounds(0, 20, frame.getWidth(), 60);
        loginTextValue.setHorizontalAlignment(SwingConstants.CENTER);
        loginTextValue.setVerticalAlignment(SwingConstants.CENTER);


        UIManager.put("PasswordField.showRevealButton", true);
        UIManager.put("PasswordField.revealIcon", imageData.showIcon);

        userInput = new FlatTextField();
        passInput = new FlatPasswordField();
        passInput.addMouseListener(this);
        userInput.setShowClearButton(true);
        passInput.setShowClearButton(true);
        userInput.setPlaceholderText("Username");
        passInput.setPlaceholderText("Password");
        userInput.setBounds(95, loginTextValue.getY() + 105, 300, 50);
        passInput.setBounds(95, loginTextValue.getY() + 175, 300, 50);
        userInput.setFont(new Font("Segue Ui", Font.PLAIN, 17));
        passInput.setFont(new Font("Segue Ui", Font.PLAIN, 17));

        submit = new FlatButton();
        submit.addActionListener(this);
        submit.setText("Sign in");
        submit.setHorizontalTextPosition(SwingConstants.CENTER);
        submit.setVerticalTextPosition(SwingConstants.BOTTOM);
        submit.setFont(UIManager.getFont("h2.regular.font"));
        submit.setBounds(95, loginTextValue.getY() + 240, 300, 50);
        submit.setFocusable(false);
        submit.setButtonType(FlatButton.ButtonType.roundRect);

        adminPanelButton = new FlatButton();
        adminPanelButton.setFont(UIManager.getFont("h2.regular.font"));
        adminPanelButton.setText("Admin Sign Up");
        adminPanelButton.addActionListener(this);
        adminPanelButton.setHorizontalTextPosition(SwingConstants.CENTER);
        adminPanelButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        adminPanelButton.setFocusable(false);
        adminPanelButton.setBounds(submit.getX() + 52, submit.getY() + 75, 200, 50);
        adminPanelButton.setButtonType(FlatButton.ButtonType.roundRect);

        forgotPassBtn = new FlatButton();
        forgotPassBtn.setFont(UIManager.getFont("h2.regular.font"));
        forgotPassBtn.setText("Forgot Password?");
        forgotPassBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        forgotPassBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        forgotPassBtn.setFocusable(false);
        forgotPassBtn.addActionListener(this);
        forgotPassBtn.setBounds(submit.getX() + 52, submit.getY() + 140, 200, 50);
        forgotPassBtn.setButtonType(FlatButton.ButtonType.roundRect);

        frame.add(loginTextValue);
        frame.add(userInput);
        frame.add(submit);
        frame.add(passInput);
        frame.add(forgotPassBtn);
        frame.add(adminPanelButton);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    public String getName(String id, String userType) {
        try {
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            String column = userType + "_name";
            String column1 = userType + "_id";
            PreparedStatement st = driver.prepareStatement("select " + column + " from " + userType + " where " + column1 + " = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getString(userType + "_name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return " ";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            currentUser.setUsername(userInput.getText());
            currentUser.setPassword(new String(passInput.getPassword()));
            boolean result = currentUser.verifyCredentials();
            if (result) {
                frame.dispose();
                String userType = userInput.getText().substring(0, 2);
                if (userType.equals("ST")) {
                    new mainUi(userInput.getText(), "student");
                } else if (userType.equals("FA")) {
                    new mainUi(userInput.getText(), "faculty");
                } else if (userType.equals("AD")) {
                    new approvalUser(userInput.getText(), "advisor");
                } else if (userType.equals("HO")) {
                    new approvalUser(userInput.getText(), "hod");
                } else {
                    new adminPanelUi(userInput.getText());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Username/Password", "Login Invalid", JOptionPane.PLAIN_MESSAGE);
            }
        }
        if (e.getSource() == adminPanelButton) {
            frame.dispose();
            new signUP();
        }
        if (e.getSource() == forgotPassBtn) {
            new passwordResetRequestFrame();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}