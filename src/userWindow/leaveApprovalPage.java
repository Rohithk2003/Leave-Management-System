package userWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class leaveApprovalPage implements ActionListener {
    private final JPanel parentComponent;
    private final String id;
    private final JPanel requestPanel;
    private individualRequestPanel[] panels;


    leaveApprovalPage(JPanel parentComponent, String id) {
        this.parentComponent = parentComponent;
        this.id = id;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(150, 50, 1030, 800);
        JLabel leaveRecordsText = new JLabel("Leave Request");
        leaveRecordsText.setFont(UIManager.getFont("h00.font"));
        leaveRecordsText.setBounds(30, 0, panel.getWidth(), 50);
        leaveRecordsText.setHorizontalAlignment(SwingConstants.LEFT);

        requestPanel = new JPanel();
        requestPanel.setPreferredSize(new Dimension(1030, 1100));
        requestPanel.add(new individualRequestPanel(requestPanel, -1));
        loadLeaveRequests();
        JScrollPane pane = new JScrollPane();
        pane.setBounds(30, 50, 1000, 500);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setViewportView(requestPanel);
        JScrollBar verticalBar = pane.getVerticalScrollBar();
        verticalBar.setUnitIncrement(20);

        panel.add(pane);
        panel.add(leaveRecordsText);


        panel.revalidate();
        parentComponent.setLayout(null);
        parentComponent.add(panel);
        parentComponent.revalidate();
        parentComponent.repaint();
    }

    private void loadLeaveRequests() {
        Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
        try {
            if (this.id.startsWith("H")) {
                PreparedStatement st = driver.prepareStatement("""
                        select count(*)
                        from leave_records
                        where id in (select faculty_id from faculty where department_id in (select department_id from hod where hod_id = ?)) and status = 'Under Review'""");
                st.setString(1, this.id);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    panels = new individualRequestPanel[10];
                }
                st = driver.prepareStatement("""
                        select leave_id
                        from leave_records
                        where id in (select faculty_id from faculty where department_id in (select department_id from hod where hod_id = ?)) and status = 'Under Review'""");
                st.setString(1, this.id);
                rs = st.executeQuery();
                int i = 0;
                while (rs.next()) {
                    panels[i] = new individualRequestPanel(requestPanel, rs.getInt("leave_id"));
                    requestPanel.add(panels[i]);
                    i++;
                }
            } else {
                PreparedStatement st = driver.prepareStatement("""
                        select count(*)
                        from leave_records
                        where id in (select student_id from student where sec_id in (select sec_id from advisor where advisor_id = ?)) and status = 'Under Review'""");
                st.setString(1, this.id);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    panels = new individualRequestPanel[10];
                }
                st = driver.prepareStatement("""
                        select leave_id
                        from leave_records
                        where id in (select student_id from student where sec_id in (select sec_id from advisor where advisor_id = ?)) and status = 'Under Review'""");
                st.setString(1, this.id);
                rs = st.executeQuery();
                int i = 0;
                while (rs.next()) {
                    panels[i] = new individualRequestPanel(requestPanel, rs.getInt("leave_id"));
                    requestPanel.add(panels[i]);
                    i++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
