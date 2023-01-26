package userWindow;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import imageData.imageData;
import login.loginUi;
import users.advisor;
import users.faculty;
import users.hod;
import users.student;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class approvalUser implements MouseListener, ActionListener {
    private final advisor Advisor;
    private final String currentUserType;
    private final String id;
    private final hod Hod;
    private JButton currentUserName;
    private JPanel hoverPanel;
    private JPanel mainInfoPanel;
    private JFrame frame;
    private Popup popup;
    private PopupFactory pf;

    public approvalUser(String id, String currentUserType) {
        this.id = id;
        this.currentUserType = currentUserType;
        Advisor = new advisor();
        Hod = new hod();
        loadDetailsOfUser();
        this.setupUi();
        this.setupTopPanel();
        this.setupMainInfoPanel();
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new approvalUser("AD21049", "advisor");
    }

    private void setupTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(frame.getWidth() - 20, 100));
        topPanel.addMouseListener(this);

        JPanel edgePanel = new JPanel();
        edgePanel.setPreferredSize(new Dimension(300, 100));
        edgePanel.setLayout(null);
        JPanel leftIconPanel = new JPanel();
        leftIconPanel.setPreferredSize(new Dimension(250, 128));
        leftIconPanel.setLayout(null);

        JLabel clmsLabel = new JLabel();
        clmsLabel.setIcon(imageData.CLMS);
        clmsLabel.setBounds(30, 0, 128, 128);
        leftIconPanel.add(clmsLabel);

        topPanel.add(leftIconPanel, BorderLayout.WEST);
        currentUserName = new JButton();// current username display label
        currentUserName.setText("<html><p>Name:&nbsp;&nbsp;&nbsp;&nbsp;" +
                Advisor.getName() + Hod.getName() + "<br>ID:&nbsp;&nbsp;&nbsp;&nbsp;" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + id + "</p></html>");
        currentUserName.setIcon(imageData.profileIcon);
        currentUserName.setIconTextGap(20);
        currentUserName.setContentAreaFilled(false);
        currentUserName.setHorizontalTextPosition(FlatLabel.LEFT);
        currentUserName.setBounds(30, 10, 300, 80);
        currentUserName.setFont(UIManager.getFont("h2.regular.font"));
        currentUserName.addMouseListener(this);
        currentUserName.addActionListener(this);

        hoverPanel = new JPanel();
        hoverPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        hoverPanel.setSize(160, 150);
        hoverPanel.setPreferredSize(new DimensionUIResource(160, 140));

        FlatButton editDetails = new FlatButton();
        editDetails.setText("Edit Details");
        editDetails.addActionListener(e -> {
            new modifyDetailsPage(new student(), new faculty(), Advisor, Hod, currentUserType);
        });
        editDetails.setFont(UIManager.getFont("h2.regular.font"));
        editDetails.setPreferredSize(new Dimension(160, 40));
        editDetails.setBorderPainted(true);

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
        hoverPanel.add(editDetails);
        hoverPanel.add(logOut);

        pf = new PopupFactory();
        this.popup = pf.getPopup(frame, hoverPanel, frame.getWidth() + 105, 170);

        edgePanel.add(currentUserName);
        topPanel.add(edgePanel, BorderLayout.EAST);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.revalidate();

    }

    private void loadDetailsOfUser() {
        if (currentUserType.equals("advisor")) {
            this.Advisor.setAdvisorId(id);
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            try {
                PreparedStatement queryExecute = driver.prepareStatement("select * from advisor where advisor_id = ?");
                queryExecute.setString(1, this.id);
                ResultSet rs = queryExecute.executeQuery();
                while (rs.next()) {
                    this.Advisor.setName(rs.getString("advisor_name"));
                    this.Advisor.setAddress(rs.getString("address"));
                    this.Advisor.setSecId(rs.getInt("sec_id"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (currentUserType.equals("hod")) {
            Hod.setHodId(id);
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            try {
                PreparedStatement queryExecuter = driver.prepareStatement("select * from hod where hod_id = ?");
                queryExecuter.setString(1, this.id);
                ResultSet rs = queryExecuter.executeQuery();
                while (rs.next()) {
                    this.Hod.setName(rs.getString("hod_name"));
                    this.Hod.setAddress(rs.getString("address"));
                    this.Hod.setDepId(rs.getInt("department_id"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setupMainInfoPanel() {
        mainInfoPanel = new JPanel();
        mainInfoPanel.setLayout(new BorderLayout());
        mainInfoPanel.setSize(1030, 750);
        mainInfoPanel.setPreferredSize(new Dimension(1030, 750));
        mainInfoPanel.addMouseListener(this);
        new leaveApprovalPage(mainInfoPanel, this.id);
        mainInfoPanel.revalidate();
        frame.add(mainInfoPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void setupUi() {
        System.setProperty("sun.java2d.uiScale", "1.01");
        FlatMacDarkLaf.setup();
        UIManager.put("Button.disabledText", new ColorUIResource(Color.white));
        frame = new JFrame("User Panel");
        frame.addMouseListener(this);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1350, 900);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
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
            } catch (NullPointerException ignored) {
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
