package userWindow;

import calender.calenderUi;
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
import java.util.Date;

public class mainUi implements MouseListener, ActionListener {
    private final String currentUserType;
    private final String id;
    private final student Student;
    private final faculty Faculty;
    private JButton currentUserName;
    private JPanel hoverPanel;
    private JPanel mainInfoPanel;
    private FlatButton dashboard;
    private FlatButton applyLeaveBtn;
    private FlatButton displayLeaveRecords;
    private JFrame frame;
    private Popup popup;
    private PopupFactory pf;
    private Date[][] leaveDates;
    private String[][] completeData;

    public mainUi(String id, String currentUserType) {
        this.id = id;
        this.currentUserType = currentUserType;
        Student = new student();
        Faculty = new faculty();
        loadDetailsOfUser(Student, Faculty);
        this.setupUi();
        this.setupSidePanel();
        this.setupTopPanel();
        this.setupMainInfoPanel();
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new mainUi("ST21049", "student");
    }

    private void loadDetailsOfUser(student Student, faculty Faculty) {
        if (currentUserType.equals("student")) {
            Student.setId(id);
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            try {
                PreparedStatement queryExecuter = driver.prepareStatement("select * from student where student_id = ?");
                queryExecuter.setString(1, this.id);
                ResultSet rs = queryExecuter.executeQuery();
                while (rs.next()) {
                    Student.setName(rs.getString("student_name"));
                    Student.setAddress(rs.getString("address"));
                    Student.setSecId(rs.getInt("sec_id"));
                    Student.setDepId(rs.getInt("department_id"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (currentUserType.equals("faculty")) {
            Faculty.setFacultyId(id);
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            try {
                PreparedStatement queryExecutes = driver.prepareStatement("select * from faculty where faculty_id = ?");
                queryExecutes.setString(1, this.id);
                ResultSet rs = queryExecutes.executeQuery();
                while (rs.next()) {
                    Faculty.setName(rs.getString("faculty_name"));
                    Faculty.setAddress(rs.getString("address"));
                    Faculty.setDepId(rs.getInt("department_id"));
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
        int i = 0;
        Date[][] leaveDates = new Date[10][10];
        String[][] completeData = new String[10][10];
        try {
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            PreparedStatement st = driver.prepareStatement("select count(*) from leave_records where id = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                leaveDates = new Date[rs.getInt("count")][2];
                completeData = new String[rs.getInt("count")][3];
            }
            driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            st = driver.prepareStatement("select start_date,end_date,status from leave_records where id = ?");
            st.setString(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                Date[] temp = new Date[2];
                String[] temp1 = new String[3];
                temp[0] = rs.getDate("start_date");
                temp[1] = rs.getDate("end_date");
                leaveDates[i] = temp;
                temp1[0] = String.valueOf(rs.getDate("start_date"));
                temp1[1] = String.valueOf(rs.getDate("end_date"));
                temp1[2] = String.valueOf(rs.getString("status"));
                completeData[i] = temp1;
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.leaveDates = leaveDates;
        this.completeData = completeData;
        new calenderUi(mainInfoPanel, 0, leaveDates, completeData);
        mainInfoPanel.revalidate();
        frame.add(mainInfoPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public void setupSidePanel() {

        JPanel sidePanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        sidePanel.setLayout(layout);
        sidePanel.setPreferredSize(new Dimension(300, 650));

        dashboard = new FlatButton();
        dashboard.setText("Dashboard");
        dashboard.setButtonType(FlatButton.ButtonType.roundRect);
        dashboard.setHorizontalTextPosition(FlatButton.RIGHT);
        dashboard.setBorderPainted(false);
        dashboard.setIcon(imageData.dashboardIcon);
        dashboard.addActionListener(this);
        dashboard.setIconTextGap(10);
        dashboard.setFont(UIManager.getFont("h2.regular.font"));
        dashboard.setPreferredSize(new Dimension(200, 60));
        dashboard.addMouseListener(this);

        applyLeaveBtn = new FlatButton();
        applyLeaveBtn.setText("Apply For Leave  ");
        applyLeaveBtn.setButtonType(FlatButton.ButtonType.roundRect);
        applyLeaveBtn.setHorizontalTextPosition(FlatButton.RIGHT);
        applyLeaveBtn.addActionListener(this);
        applyLeaveBtn.setFont(UIManager.getFont("h2.regular.font"));
        applyLeaveBtn.setPreferredSize(new Dimension(200, 60));
        applyLeaveBtn.setBorderPainted(false);
        applyLeaveBtn.addMouseListener(this);

        displayLeaveRecords = new FlatButton();
        displayLeaveRecords.setText("Leave Records");
        displayLeaveRecords.setButtonType(FlatButton.ButtonType.roundRect);
        displayLeaveRecords.setHorizontalTextPosition(FlatButton.RIGHT);
        displayLeaveRecords.addActionListener(this);
        displayLeaveRecords.setFont(UIManager.getFont("h2.regular.font"));
        displayLeaveRecords.setPreferredSize(new Dimension(200, 60));
        displayLeaveRecords.setBorderPainted(false);
        displayLeaveRecords.addMouseListener(this);

        layout.putConstraint(SpringLayout.NORTH, dashboard, 80, SpringLayout.NORTH, sidePanel);
        layout.putConstraint(SpringLayout.WEST, dashboard, 50, SpringLayout.WEST, sidePanel);
        layout.putConstraint(SpringLayout.NORTH, applyLeaveBtn, 80, SpringLayout.NORTH, dashboard);
        layout.putConstraint(SpringLayout.WEST, applyLeaveBtn, 50, SpringLayout.WEST, sidePanel);
        layout.putConstraint(SpringLayout.NORTH, displayLeaveRecords, 80, SpringLayout.NORTH, applyLeaveBtn);
        layout.putConstraint(SpringLayout.WEST, displayLeaveRecords, 50, SpringLayout.WEST, sidePanel);

        sidePanel.add(dashboard);
        sidePanel.add(applyLeaveBtn);
        sidePanel.add(displayLeaveRecords);

        frame.add(sidePanel, BorderLayout.WEST);

    }

    private void setupUi() {
        System.setProperty("sun.java2d.uiScale", "1.01");
        FlatMacDarkLaf.setup();
        UIManager.put("Button.disabledText", new ColorUIResource(Color.white));
        frame = new JFrame("Admin Panel");
        frame.addMouseListener(this);
        frame.setVisible(true);
        frame.setResizable(false);
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
        edgePanel.setPreferredSize(new Dimension(300, 100));
        edgePanel.setLayout(null);

        currentUserName = new JButton();// current username display label
        currentUserName.setText("<html><p>Name:&nbsp;&nbsp;&nbsp;&nbsp;" + Student.getName() + Faculty.getName() + "<br>ID:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + id + "</p></html>");
        currentUserName.setIcon(imageData.profileIcon);
        currentUserName.setIconTextGap(50);
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
            new modifyDetailsPage(Student, Faculty, new advisor(), new hod(), currentUserType);
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
        if (e.getSource() == dashboard) {
            dashboard.setBackground(new Color(100, 100, 100));
        }
        if (e.getSource() == applyLeaveBtn) {
            applyLeaveBtn.setBackground(new Color(100, 100, 100));
        }
        if (e.getSource() == displayLeaveRecords) {
            displayLeaveRecords.setBackground(new Color(100, 100, 100));
        }
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
        if (e.getSource() == dashboard) {
            dashboard.setBackground(new Color(86, 86, 86));
        }
        if (e.getSource() == applyLeaveBtn) {
            applyLeaveBtn.setBackground(new Color(86, 86, 86));
        }
        if (e.getSource() == displayLeaveRecords) {
            displayLeaveRecords.setBackground(new Color(86, 86, 86));
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dashboard) {
            mainInfoPanel.setLayout(new BorderLayout());
            mainInfoPanel.removeAll();
            new calenderUi(mainInfoPanel, 0, leaveDates, completeData);
            mainInfoPanel.repaint();
            mainInfoPanel.revalidate();
        } else if (e.getSource() == applyLeaveBtn) {
            mainInfoPanel.removeAll();
            new leaveApplyPage(mainInfoPanel, id);
            mainInfoPanel.revalidate();
            mainInfoPanel.repaint();


        } else if (e.getSource() == displayLeaveRecords) {
            mainInfoPanel.removeAll();
            new leaveRecordsPage(mainInfoPanel, id);
            mainInfoPanel.repaint();
            mainInfoPanel.revalidate();
        }
    }
}
