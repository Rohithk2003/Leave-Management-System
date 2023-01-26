package userWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class leaveRecordsPage extends DefaultTableModel implements ActionListener {
    private final JTable table;
    private final JButton deleteLeave;
    private final JButton updateLeave;
    private final JPanel parentComponent;
    private final String userId;
    private final boolean tableDataEditable = false;
    private leaveModel[] leaveModels;
    private String[][] data;

    leaveRecordsPage(JPanel parentComponent, String userId) {
        this.userId = userId;
        this.parentComponent = parentComponent;
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(100, 50, parentComponent.getWidth(), 800);

        JLabel leaveRecordsText = new JLabel("LEAVE RECORDS");
        leaveRecordsText.setFont(UIManager.getFont("h0.font"));
        leaveRecordsText.setBounds(30, 0, panel.getWidth(), 50);
        leaveRecordsText.setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        loadLeaveRecords();
        String[] columns = {"Leave Id", "Type Of Leave", "From", "To", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return tableDataEditable;
            }
        };
        table = new JTable(data, columns);
        table.setModel(tableModel);
        table.setFocusable(false);
        table.setFont(UIManager.getFont("h2.regular.font"));
        table.setSize(300, 500);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.getTableHeader().setFont(UIManager.getFont("h2.regular.font"));
        table.setShowGrid(true);
        table.setRowHeight(59);

        JScrollPane pane = new JScrollPane();
        pane.setBounds(30, 50, 800, 500);
        pane.setViewportView(table);

        panel.add(pane);
        panel.add(leaveRecordsText);

        deleteLeave = new JButton("<html>Remove Leave request/<br>record</br></html>");
        deleteLeave.setFont(UIManager.getFont("h2.regular.font"));
        deleteLeave.setBounds(30, 600, 220, 60);
        deleteLeave.addActionListener(this);

        panel.add(deleteLeave);

        updateLeave = new JButton("Update Leave");
        updateLeave.setFont(UIManager.getFont("h2.regular.font"));
        updateLeave.setBounds(310, 600, 220, 60);
        updateLeave.addActionListener(this);

        panel.add(updateLeave);

        parentComponent.setLayout(null);
        parentComponent.add(panel);
    }

    private void loadLeaveRecords() {
        Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
        try {
            PreparedStatement st = driver.prepareStatement("select count(*) from leave_records where  id = ?");
            st.setString(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                leaveModels = new leaveModel[rs.getInt("count")];
                data = new String[rs.getInt("count")][5];
            }

            st = driver.prepareStatement("select * from leave_records where id = ?");
            st.setString(1, userId);
            rs = st.executeQuery();
            int i = 0;
            while (rs.next()) {
                leaveModels[i] = new leaveModel();
                leaveModels[i].setLeaveId(rs.getInt("leave_id"));
                data[i][0] = String.valueOf(leaveModels[i].getLeaveId());
                leaveModels[i].setTypeOfLeave(rs.getString("type_of_leave"));
                data[i][1] = leaveModels[i].getTypeOfLeave();
                leaveModels[i].setFrom(rs.getString("start_date"));
                data[i][2] = leaveModels[i].getFrom();
                leaveModels[i].setTo(rs.getString("end_date"));
                data[i][3] = leaveModels[i].getTo();
                leaveModels[i].setStatus(rs.getString("status"));
                data[i][4] = leaveModels[i].getStatus();
                i++;
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteLeave) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int selectedRowIndex = table.getSelectedRow();
            int selectedRowModelIndex = table.convertRowIndexToModel(selectedRowIndex);
            if (selectedRowIndex != -1)
                model.removeRow(selectedRowModelIndex);
            Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            try {
                PreparedStatement st = driver.prepareStatement("delete from leave_records where leave_id = ?");
                st.setInt(1, leaveModels[selectedRowIndex].getLeaveId());
                st.executeUpdate();
                parentComponent.revalidate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == updateLeave) {
            int selectedRowIndex = table.getSelectedRow();
            if (selectedRowIndex != -1) {
                if (leaveModels[selectedRowIndex].getStatus().equals("Rejected")) {
                    JOptionPane.showMessageDialog(null,
                            "Leave record cannot be updated since it was rejected");
                } else {

                    new leaveExtendPage(new JPanel(), userId, leaveModels[selectedRowIndex]);
                    parentComponent.revalidate();
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Please select a record");
            }
        }

    }
}
