package adminPanel;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import imageData.imageData;
import login.loginUi;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class adminPanelUi implements MouseListener, ActionListener {
    private final String usernameUser;
    private JButton currentUserName;
    private JPanel hoverPanel;
    private JPanel mainInfoPanel;
    private JFrame frame;
    private Popup popup;
    private PopupFactory pf;
    private FlatButton deleteAccount;

    public adminPanelUi(String username) {
        System.setProperty("sun.java2d.uiScale", "1.01");
        usernameUser = username;
        this.setupUi();
        this.setupTopPanel();
        this.setupMainInfoPanel();
        frame.repaint();
        frame.revalidate();
    }

    public static void main(String[] args) {
        new adminPanelUi("admin");
    }

    public void setupMainInfoPanel() {
        mainInfoPanel = new JPanel();
        mainInfoPanel.setSize(1350, 750);
        mainInfoPanel.setPreferredSize(new Dimension(1350, 750));
        mainInfoPanel.addMouseListener(this);
        mainInfoPanel.setLayout(null);
        new userInfoPage(mainInfoPanel);
        frame.add(mainInfoPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void setupUi() {
        System.setProperty("sun.java2d.uiScale", "1.01");
        FlatMacDarkLaf.setup();
        UIManager.put("Button.disabledText", new ColorUIResource(Color.white));
        frame = new JFrame("Admin Panel");
        frame.addMouseListener(this);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1350, 900);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
    }

    private void setupTopPanel() {

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(frame.getWidth() - 20, 100));
        topPanel.addMouseListener(this);
        JPanel leftIconPanel = new JPanel();
        leftIconPanel.setPreferredSize(new Dimension(250, 128));
        leftIconPanel.setLayout(null);

        JLabel clmsLabel = new JLabel();
        clmsLabel.setIcon(imageData.CLMS);
        clmsLabel.setBounds(30, 0, 128, 128);
        leftIconPanel.add(clmsLabel);

        topPanel.add(leftIconPanel, BorderLayout.WEST);
        JPanel edgePanel = new JPanel();
        edgePanel.setPreferredSize(new Dimension(250, 100));
        edgePanel.setLayout(null);

        JButton refreshButton = new JButton();
        refreshButton.setFocusable(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setIcon(imageData.refreshIcon);
        refreshButton.addActionListener(e -> {
            frame.remove(mainInfoPanel);
            setupMainInfoPanel();
            frame.revalidate();
        });
        refreshButton.setBounds(45, 36, 32, 32);

        currentUserName = new JButton();// current username display label
        currentUserName.setText(usernameUser);
        currentUserName.setIcon(imageData.profileIcon);
        currentUserName.setIconTextGap(10);
        currentUserName.setContentAreaFilled(false);
        currentUserName.setHorizontalTextPosition(FlatLabel.LEFT);
        currentUserName.setBounds(80, 10, 150, 80);
        currentUserName.setFont(UIManager.getFont("h1.regular.font"));
        currentUserName.addMouseListener(this);
        currentUserName.addActionListener(this);

        hoverPanel = new JPanel();
        hoverPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        hoverPanel.setSize(160, 95);
        hoverPanel.setPreferredSize(new Dimension(160, 95));
        hoverPanel.setOpaque(false);

        deleteAccount = new FlatButton();
        deleteAccount.setText("Delete Account");
        deleteAccount.setFont(UIManager.getFont("h2.regular.font"));
        deleteAccount.setBorderPainted(true);
        deleteAccount.setPreferredSize(new Dimension(160, 40));
        deleteAccount.addActionListener(this);

        FlatButton logOut = new FlatButton();
        logOut.setText("Log out");
        logOut.setFont(UIManager.getFont("h2.regular.font"));
        logOut.addActionListener(e -> {
            frame.dispose();
            new loginUi();
        });
        logOut.setPreferredSize(new Dimension(160, 40));
        logOut.setBorderPainted(true);

        // adding all buttons
        hoverPanel.add(deleteAccount);
        hoverPanel.add(logOut);

        pf = PopupFactory.getSharedInstance();
        this.popup = pf.getPopup(frame, hoverPanel, frame.getWidth() + 105, 170);

        edgePanel.add(currentUserName);
        edgePanel.add(refreshButton);

        topPanel.add(edgePanel, BorderLayout.EAST);
        frame.add(topPanel, BorderLayout.NORTH);
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
        if (e.getSource() == currentUserName || e.getSource() == hoverPanel) {
            popup.show();
        }
        if (e.getSource() == mainInfoPanel || e.getSource() == frame) {
            try {
                popup.hide();
                this.popup = pf.getPopup(frame, hoverPanel, frame.getWidth() + 105, 170);
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteAccount) {
            try {
                Connection c = new JDBCDriver.driverJDBC().getJDBCDriver();
                PreparedStatement st = c.prepareStatement("delete from users where username = ?");
                st.setString(1, usernameUser.strip());
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Your account has been deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new loginUi();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
