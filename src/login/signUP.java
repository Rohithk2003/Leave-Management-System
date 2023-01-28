package login;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatPasswordField;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import imageData.imageData;
import passwordEncryption.Solution;
import users.login_user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class signUP implements ActionListener, MouseListener {
    private final login_user currentUser;
    private final JFrame frame;
    private final FlatPasswordField passInput;
    private final FlatTextField userInput;
    private final FlatButton toLoginBtn;
    private final FlatButton submit;

    public signUP() {
        currentUser = new login_user();
        try {
            FlatMacDarkLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame();
        int panelHeight = 600;
        int panelWidth = 500;

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Sign Up");
        frame.setSize(panelWidth, panelHeight);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        FlatLabel loginTextValue = new FlatLabel();
        loginTextValue.setText("Sign Up");
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
        submit.setText("Proceed");
        submit.setHorizontalTextPosition(SwingConstants.CENTER);
        submit.setVerticalTextPosition(SwingConstants.BOTTOM);
        submit.setFont(UIManager.getFont("h2.regular.font"));
        submit.setBounds(95, loginTextValue.getY() + 240, 300, 50);
        submit.setFocusable(false);

        submit.setButtonType(FlatButton.ButtonType.roundRect);


        toLoginBtn = new FlatButton();
        toLoginBtn.setFont(UIManager.getFont("h2.regular.font"));
        toLoginBtn.setText("Login");
        toLoginBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        toLoginBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        toLoginBtn.setFocusable(false);
        toLoginBtn.setBounds(315, submit.getY() + 70, 80, 50);
        toLoginBtn.setButtonType(FlatButton.ButtonType.roundRect);
        toLoginBtn.addActionListener(this);

        frame.add(loginTextValue);
        frame.add(userInput);
        frame.add(submit);
        frame.add(passInput);
        frame.add(toLoginBtn);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            currentUser.setUsername(userInput.getText());
            currentUser.setPassword(new String(passInput.getPassword()));
            if (currentUser.getPassword().length() < 8) {
                JOptionPane.showMessageDialog(null, "Password must be greater than 8 characters", "Error", JOptionPane.ERROR_MESSAGE);

            } else if (currentUser.getPassword().equals(currentUser.getUsername())) {
                JOptionPane.showMessageDialog(null, "Password must not be equal to username", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (currentUser.getUsername().startsWith("HO") || currentUser.getUsername().startsWith("FA") || currentUser.getUsername().startsWith("ST") || currentUser.getUsername().startsWith("AD")) {
                JOptionPane.showMessageDialog(null, "Only admins can sign up", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
                    PreparedStatement st = driver.prepareStatement("insert into users values(?,?)");
                    st.setString(1, currentUser.getUsername());
                    st.setString(2, Solution.encrypt(currentUser.getPassword(), 10));
                    st.execute();
                    JOptionPane.showMessageDialog(null, "Account has been created ", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    new loginUi();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "User already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == toLoginBtn) {
            frame.dispose();
            new loginUi();
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