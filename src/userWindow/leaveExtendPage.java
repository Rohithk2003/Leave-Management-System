package userWindow;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class leaveExtendPage extends leaveApplyPage {
    private final leaveModel model;

    public leaveExtendPage(JPanel parentComponent, String id, leaveModel model) {
        super(parentComponent, id);

        this.model = model;
        parentComponent.setLayout(new BorderLayout());
        parentComponent.add(super.panel, BorderLayout.CENTER);

        JFrame frame = new JFrame();
        frame.setLocation(600, 300);
        frame.setSize(700, 600);
        frame.add(parentComponent);

        super.panel.setPreferredSize(new Dimension(400, 400));
        super.typeOfLeave.setEditable(false);

        super.typeOfLeave.setSelectedItem(model.getTypeOfLeave());
        super.typeOfLeave.setEditable(false);
        super.typeOfLeave.setEnabled(false);
        super.selectToDatePicker.setText(model.getTo());
        super.selectDateFromPicker.setText(model.getFrom());
        super.formLayout.setText("Modify Leave Request");
        super.typeOfLeave.setEditable(false);
        super.documentFilePicker.setVisible(false);
        super.saveBtn.removeActionListener(this);
        super.saveBtn.addActionListener(e -> extendLeave());
        frame.setVisible(true);
    }

    private void extendLeave() {
        try {
            Date startDate = Date.valueOf(super.selectDateFromPicker.getText());
            Date toDate = Date.valueOf(super.selectToDatePicker.getText());
            if (startDate.compareTo(toDate) < 0) {
                PreparedStatement st = driver.prepareStatement("select count(*) from leave_records");
                ResultSet rs = st.executeQuery();
                st = driver.prepareStatement("update leave_records set start_date = ? ,end_date = ? where leave_id = ? ");
                st.setDate(1, startDate);
                st.setDate(2, toDate);
                st.setInt(3, model.getLeaveId());
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Leave has been extended", "Success", JOptionPane.INFORMATION_MESSAGE);
                st = driver.prepareStatement("update leave_records set status = 'Under Review' where leave_id = ? ");
                st.setInt(1, model.getLeaveId());
                st.executeUpdate();

            } else {
                JOptionPane.showMessageDialog(null, "Invalid start date and end dates");
            }

        } catch (
                SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

}