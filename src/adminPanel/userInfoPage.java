package adminPanel;

import com.formdev.flatlaf.extras.components.*;
import imageData.imageData;
import users.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class userInfoPage implements ActionListener {
    JPanel parentComponent;
    Font font = UIManager.getFont("h2.regular.font");
    private String currentUserType;
    private FlatRadioButton studentBtn;
    private FlatRadioButton facultyBtn;
    private FlatRadioButton hodBtn;
    private FlatRadioButton advisorBtn;
    private FlatButton addUser;
    private JPanel buttonLayout;
    private FlatTextField searchField;
    private JPanel userDisplay;
    private int studentsNo;
    private int facultiesNo;
    private int advisorsNo;
    private int hodNo;
    private FlatButton searchBtn;

    public userInfoPage(JPanel parentComponent) {

        this.parentComponent = parentComponent;
        currentUserType = "student";
        setupMetaDataPanel();
        new Thread(this::setupUserDisplayPanel).start();
        parentComponent.revalidate();
    }

    private void loadUsers() {
        Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
        try {
            PreparedStatement st = driver.prepareStatement("Select count(*) from student");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                studentsNo = rs.getInt("count");
            }

            st = driver.prepareStatement("Select count(*) from faculty");
            rs = st.executeQuery();
            while (rs.next()) {
                facultiesNo = rs.getInt("count");
            }

            st = driver.prepareStatement("Select count(*) from advisor");
            rs = st.executeQuery();
            while (rs.next()) {
                advisorsNo = rs.getInt("count");
            }

            st = driver.prepareStatement("Select count(*) from hod");
            rs = st.executeQuery();
            while (rs.next()) {
                hodNo = rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupMetaDataPanel() {
        JPanel userCurrentDetails = new JPanel();
        userCurrentDetails.setBounds(0, 0, 1350, 250);
        userCurrentDetails.setLayout(new FlowLayout(FlowLayout.CENTER, 130, 50));

        //creating 4 panels for displaying the no of students\faculty\advisor\hod
        loadUsers();
        RoundedPanel noStudents = new RoundedPanel(40);
        noStudents.setSize(200, 175);
        noStudents.setPreferredSize(new Dimension(200, 175));
        noStudents.setLayout(null);
        noStudents.setOpaque(false);
        noStudents.setBackground(new Color(255, 255, 255, 60));

        FlatLabel studentText = new FlatLabel();
        studentText.setText("Students:");
        studentText.setFont(font);
        studentText.setBounds(10, 10, 100, 30);
        studentText.setForeground(Color.white);

        FlatLabel noOfStudents = new FlatLabel();
        noOfStudents.setText(String.valueOf(studentsNo));
        noOfStudents.setFont(new Font("Segue Ui", Font.BOLD, 50));
        noOfStudents.setHorizontalAlignment(FlatLabel.CENTER);
        noOfStudents.setBounds(0, 65, noStudents.getWidth(), 50);

        RoundedPanel noFaculty = new RoundedPanel(40);
        noFaculty.setSize(200, 175);
        noFaculty.setPreferredSize(new Dimension(200, 175));
        noFaculty.setOpaque(false);
        noFaculty.setLayout(null);
        noFaculty.setBackground(new Color(255, 255, 255, 60));

        FlatLabel facultyText = new FlatLabel();
        facultyText.setText("Faculties");
        facultyText.setFont(font);
        facultyText.setBounds(10, 10, 100, 30);
        facultyText.setForeground(Color.white);

        FlatLabel noOfFaculties = new FlatLabel();
        noOfFaculties.setText(String.valueOf(facultiesNo));
        noOfFaculties.setFont(new Font("Segue Ui", Font.BOLD, 50));
        noOfFaculties.setHorizontalAlignment(FlatLabel.CENTER);
        noOfFaculties.setBounds(0, 65, noFaculty.getWidth(), 50);

        RoundedPanel noAdvisor = new RoundedPanel(40);
        noAdvisor.setSize(200, 175);
        noAdvisor.setPreferredSize(new Dimension(200, 175));
        noAdvisor.setOpaque(false);
        noAdvisor.setLayout(null);
        noAdvisor.setBackground(new Color(255, 255, 255, 60));

        FlatLabel advisorText = new FlatLabel();
        advisorText.setText("Advisors:");
        advisorText.setFont(font);
        advisorText.setBounds(10, 10, 100, 30);
        advisorText.setForeground(Color.white);

        FlatLabel noOfAdvisor = new FlatLabel();
        noOfAdvisor.setText(String.valueOf(advisorsNo));
        noOfAdvisor.setFont(new Font("Segue Ui", Font.BOLD, 50));
        noOfAdvisor.setHorizontalAlignment(FlatLabel.CENTER);
        noOfAdvisor.setBounds(0, 65, noAdvisor.getWidth(), 50);

        RoundedPanel noHOD = new RoundedPanel(40);
        noHOD.setSize(200, 175);
        noHOD.setPreferredSize(new Dimension(200, 175));
        noHOD.setOpaque(false);
        noHOD.setLayout(null);
        noHOD.setBackground(new Color(255, 255, 255, 60));

        FlatLabel hodText = new FlatLabel();
        hodText.setText("Hod's:");
        hodText.setFont(font);
        hodText.setBounds(10, 10, 100, 30);
        hodText.setForeground(Color.white);

        FlatLabel noOfHOD = new FlatLabel();
        noOfHOD.setText(String.valueOf(hodNo));
        noOfHOD.setFont(new Font("Segue Ui", Font.BOLD, 50));
        noOfHOD.setHorizontalAlignment(FlatLabel.CENTER);
        noOfHOD.setBounds(0, 65, noHOD.getWidth(), 50);

        noStudents.add(studentText);
        noStudents.add(noOfStudents);

        noFaculty.add(facultyText);
        noFaculty.add(noOfFaculties);

        noAdvisor.add(advisorText);
        noAdvisor.add(noOfAdvisor);

        noHOD.add(hodText);
        noHOD.add(noOfHOD);

        userCurrentDetails.add(noStudents);
        userCurrentDetails.add(noFaculty);
        userCurrentDetails.add(noAdvisor);
        userCurrentDetails.add(noHOD);

        parentComponent.add(userCurrentDetails);
    }

    private void searchForUser(String userId) {
        user user = new user();
        try {
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            PreparedStatement st = driver.prepareStatement("select * from " + currentUserType + " where " + currentUserType + "_id = ?");
            st.setString(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    user.setName(rs.getString(currentUserType + "_name"));
                    user.setId(rs.getString(currentUserType + "_id"));
                    user.setAddress(rs.getString("address"));
                    PreparedStatement st1;
                    ResultSet r1;
                    if (currentUserType.equals("student")) {
                        int id = rs.getInt("sec_id");
                        st1 = driver.prepareStatement("select secname from section where secid = ?");
                        st1.setInt(1, id);
                        r1 = st1.executeQuery();
                        while (r1.next()) {
                            user.setSection(r1.getString("secname"));
                        }
                        int depId = rs.getInt("department_id");
                        st1 = driver.prepareStatement("select department_name from department where department_id = ?");
                        st1.setInt(1, depId);
                        r1 = st1.executeQuery();
                        while (r1.next()) {
                            user.setDeptName(r1.getString("department_name"));
                        }
                    }
                    if (currentUserType.equals("faculty") || currentUserType.equals("hod")) {
                        int depId = rs.getInt("department_id");
                        st1 = driver.prepareStatement("select department_name from department where department_id = ?");
                        st1.setInt(1, depId);
                        r1 = st1.executeQuery();
                        while (r1.next()) {
                            user.setDeptName(r1.getString("department_name"));
                        }
                    }
                    if (Objects.equals(currentUserType, "advisor")) {
                        int id = rs.getInt("sec_id");
                        st1 = driver.prepareStatement("select secname from section where secid = ?");
                        st1.setInt(1, id);
                        r1 = st1.executeQuery();
                        while (r1.next())
                            user.setSection(r1.getString("secname"));
                    }
                    user.setCurrentUser(currentUserType);
                    userDisplay.removeAll();
                    userDisplay.add(new userInfoPanel(userDisplay, user));
                    userDisplay.repaint();
                    userDisplay.revalidate();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No " + currentUserType + " with the given id", "Student cannot be found", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void setupUserDisplayPanel() {
        buttonLayout = new JPanel();
        buttonLayout.setLayout(null);
        buttonLayout.setBounds(0, 300, 1340, 70);

        searchBtn = new FlatButton();
        searchBtn.setIcon(imageData.searchIcon);
        searchBtn.setFocusable(false);
        searchBtn.addActionListener(this);
        searchBtn.setBounds(230, 15, 40, 40);

        searchField = new FlatTextField();
        searchField.setPlaceholderText("Search for a student(ID)");
        searchField.setFont(UIManager.getFont("h3.regular.font"));
        searchField.setBounds(30, 15, 200, 40);

        studentBtn = new FlatRadioButton();
        studentBtn.setFont(UIManager.getFont("h3.regular.font"));
        studentBtn.setBounds(510, 17, 100, 30);
        studentBtn.setSelected(true);
        studentBtn.setText("Students");
        studentBtn.addActionListener(this);

        facultyBtn = new FlatRadioButton();
        facultyBtn.setFont(UIManager.getFont("h3.regular.font"));
        facultyBtn.setBounds(620, 17, 100, 30);
        facultyBtn.setText("Faculty");
        facultyBtn.addActionListener(this);

        advisorBtn = new FlatRadioButton();
        advisorBtn.setFont(UIManager.getFont("h3.regular.font"));
        advisorBtn.setBounds(720, 17, 100, 30);
        advisorBtn.setText("Advisor");
        advisorBtn.addActionListener(this);

        hodBtn = new FlatRadioButton();
        hodBtn.setFont(UIManager.getFont("h3.regular.font"));
        hodBtn.setBounds(820, 17, 100, 30);
        hodBtn.setText("HOD");
        hodBtn.addActionListener(this);

        ButtonGroup group = new ButtonGroup();
        group.add(studentBtn);
        group.add(facultyBtn);
        group.add(advisorBtn);
        group.add(hodBtn);

        addUser = new FlatButton();
        addUser.setText("Add " + "Student");
        addUser.addActionListener(this);
        addUser.setFont(UIManager.getFont("h3.regular.font"));
        addUser.setBounds(1200, 17, 120, 30);
        addUser.setFocusable(false);

        FlatButton passwordResetRequest = new FlatButton();
        passwordResetRequest.setText("Reset Password Requests");
        passwordResetRequest.addActionListener(e -> new resetPasswordRequestWindow());
        passwordResetRequest.setFont(UIManager.getFont("h3.regular.font"));
        passwordResetRequest.setBounds(980, 17, 200, 30);
        passwordResetRequest.setFocusable(false);

        buttonLayout.add(searchField);
        buttonLayout.add(searchBtn);
        buttonLayout.add(studentBtn);
        buttonLayout.add(facultyBtn);
        buttonLayout.add(advisorBtn);
        buttonLayout.add(addUser);
        buttonLayout.add(passwordResetRequest);
        buttonLayout.add(hodBtn);

        userDisplay = new JPanel();
        userDisplay.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));
        userDisplay.setPreferredSize(new Dimension(1340, 2000));

        FlatScrollPane scrollablePane = new FlatScrollPane();
        scrollablePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollablePane.setSmoothScrolling(true);
        scrollablePane.setWheelScrollingEnabled(true);
        scrollablePane.setViewportView(userDisplay);
        scrollablePane.setBounds(0, 370, 1340, 390);

        parentComponent.add(buttonLayout);
        parentComponent.add(scrollablePane);
        parentComponent.repaint();
        new Thread(() -> loadData(currentUserType)).start();

    }

    private void loadData(String currentUserType) {
        user user = new user();
        try {
            userInfoPanel[] userPanels = new userInfoPanel[0];
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            PreparedStatement st = driver.prepareStatement("select count(*) from " + currentUserType);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                userPanels = new userInfoPanel[rs.getInt("count")];
            }
            driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            st = driver.prepareStatement("select * from " + currentUserType);
            rs = st.executeQuery();
            int i = 0;
            while (rs.next()) {
                user.setName(rs.getString(currentUserType + "_name"));
                user.setId(rs.getString(currentUserType + "_id"));
                user.setAddress(rs.getString("address"));
                PreparedStatement st1;
                ResultSet r1;
                if (currentUserType.equals("student")) {
                    int id = rs.getInt("sec_id");
                    st1 = driver.prepareStatement("select secname from section where secid = ?");
                    st1.setInt(1, id);
                    r1 = st1.executeQuery();
                    while (r1.next()) {
                        user.setSection(r1.getString("secname"));
                    }
                    int depId = rs.getInt("department_id");
                    st1 = driver.prepareStatement("select department_name from department where department_id = ?");
                    st1.setInt(1, depId);
                    r1 = st1.executeQuery();
                    while (r1.next()) {
                        user.setDeptName(r1.getString("department_name"));
                    }
                }
                if (currentUserType.equals("faculty") || currentUserType.equals("hod")) {
                    int depId = rs.getInt("department_id");
                    st1 = driver.prepareStatement("select department_name from department where department_id = ?");
                    st1.setInt(1, depId);
                    r1 = st1.executeQuery();
                    while (r1.next()) {
                        user.setDeptName(r1.getString("department_name"));
                    }
                }
                if (Objects.equals(currentUserType, "advisor")) {
                    int id = rs.getInt("sec_id");
                    st1 = driver.prepareStatement("select secname from section where secid = ?");
                    st1.setInt(1, id);
                    r1 = st1.executeQuery();
                    while (r1.next()) {
                        user.setSection(r1.getString("secname"));
                    }
                }
                user.setCurrentUser(currentUserType);
                userPanels[i] = new userInfoPanel(userDisplay, user);
                user = new user();
                i++;
            }

            for (i = 0; i < userPanels.length; i++) {
                userDisplay.add(userPanels[i]);
                userDisplay.repaint();
                userDisplay.revalidate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            searchForUser(searchField.getText());
        }
        if (e.getSource() == studentBtn) {
            addUser.setText("Add Student");
            searchField.setPlaceholderText("Search for a student(ID)");
            searchField.setText("");
            userDisplay.removeAll();
            currentUserType = "student";
            new Thread(() -> loadData(currentUserType)).start();

            userDisplay.revalidate();
        }
        if (e.getSource() == advisorBtn) {
            addUser.setText("Add Advisor");
            searchField.setText("");

            searchField.setPlaceholderText("Search for a advisor(ID)");
            userDisplay.removeAll();
            currentUserType = "advisor";
            new Thread(() -> loadData(currentUserType)).start();
            userDisplay.revalidate();

        }
        if (e.getSource() == facultyBtn) {
            addUser.setText("Add Faculty");
            searchField.setText("");

            searchField.setPlaceholderText("Search for a faculty(ID)");
            userDisplay.removeAll();
            currentUserType = "faculty";
            new Thread(() -> loadData(currentUserType)).start();
            userDisplay.revalidate();

        }
        if (e.getSource() == hodBtn) {
            addUser.setText("Add HOD");
            searchField.setText("");

            searchField.setPlaceholderText("Search for an hod(ID)");
            userDisplay.removeAll();
            currentUserType = "hod";
            new Thread(() -> loadData(currentUserType)).start();
            userDisplay.revalidate();

        }
        if (e.getSource() == addUser) {
            if (studentBtn.isSelected()) {
                new addStudentAdvisor("Student");

            }
            if (advisorBtn.isSelected()) {
                new addStudentAdvisor("Advisor");
            }
            if (facultyBtn.isSelected()) {
                new addFacultyHOD("Faculty");
            }
            if (hodBtn.isSelected()) {
                new addFacultyHOD("HOD");
            }

        }
        userDisplay.repaint();
        buttonLayout.repaint();
    }
}
