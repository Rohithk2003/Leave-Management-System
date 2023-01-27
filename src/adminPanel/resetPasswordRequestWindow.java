package adminPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class resetPasswordRequestWindow implements ActionListener {

    private final JPanel requestPanel;

    resetPasswordRequestWindow() {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);

        JPanel textPanel = new JPanel();
        textPanel.setPreferredSize(new Dimension(440, 50));

        JLabel leaveRecordsText = new JLabel("Password Reset Request");
        leaveRecordsText.setFont(UIManager.getFont("h2.font"));
        leaveRecordsText.setPreferredSize(new Dimension(440, 50));
        leaveRecordsText.setHorizontalAlignment(SwingConstants.CENTER);

        requestPanel = new JPanel();
        requestPanel.setPreferredSize(new Dimension(440, 500));
        requestPanel.add(new passwordRequestRows(requestPanel, "User Id", "Date of birth"));

        loadLeaveRequests();
        JScrollPane pane = new JScrollPane();
        pane.setPreferredSize(new Dimension(440, 500));
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setViewportView(requestPanel);

        frame.add(pane, BorderLayout.CENTER);
        textPanel.add(leaveRecordsText);
        frame.add(textPanel, BorderLayout.NORTH);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }


    private void loadLeaveRequests() {
        Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
        try {
            PreparedStatement st = driver.prepareStatement("select * from passwordresetrequest");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                requestPanel.add(new passwordRequestRows(requestPanel, rs.getString("id"), String.valueOf(rs.getDate("dateofbirth"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
