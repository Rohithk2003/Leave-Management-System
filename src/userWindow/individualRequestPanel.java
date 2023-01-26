package userWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class individualRequestPanel extends JPanel implements ActionListener {
    private final JCheckBox yesBox;
    private final JCheckBox noBox;
    private final ButtonGroup group;
    private final JButton reviewProof;
    private final JPanel parentComponent;
    private final int leave_id;
    private leaveModel modelLeave;

    individualRequestPanel(JPanel parentComponent, int leave_id) {
        this.parentComponent = parentComponent;
        this.leave_id = leave_id;
        modelLeave = new leaveModel();
        loadData();
        this.setBackground(new Color(50, 50, 50));
        this.setPreferredSize(new Dimension(1030, 50));
        this.setLayout(new GridLayout(1, 7, 0, 0));

        JLabel idLabel = new JLabel(String.valueOf(this.modelLeave.getUserId()));
        JLabel toLabel = new JLabel(this.modelLeave.getTo());
        JLabel fromLabel = new JLabel(this.modelLeave.getFrom());
        JLabel typeOfLeaveLabel = new JLabel(this.modelLeave.getTypeOfLeave());
        reviewProof = new JButton("<html>Review<br>&#160;Proof</br></html>");
        reviewProof.addActionListener(this);

        idLabel.setHorizontalAlignment(JLabel.CENTER);
        toLabel.setHorizontalAlignment(JLabel.CENTER);
        fromLabel.setHorizontalAlignment(JLabel.CENTER);
        typeOfLeaveLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setBackground(this.getBackground());
        checkBoxPanel.setLayout(null);
        checkBoxPanel.setPreferredSize(new Dimension(100, 50));

        yesBox = new JCheckBox("Accept");
        noBox = new JCheckBox("Reject");
        yesBox.setFont(UIManager.getFont("h2.regular.font"));
        noBox.setFont(UIManager.getFont("h2.regular.font"));
        yesBox.setBounds(10, 0, 100, 25);
        noBox.setBounds(10, 25, 100, 25);
        checkBoxPanel.add(yesBox);
        checkBoxPanel.add(noBox);

        yesBox.addActionListener(this);
        noBox.addActionListener(this);

        group = new ButtonGroup();
        group.add(yesBox);
        group.add(noBox);

        idLabel.setFont(UIManager.getFont("h2.regular.font"));
        toLabel.setFont(UIManager.getFont("h2.regular.font"));
        fromLabel.setFont(UIManager.getFont("h2.regular.font"));
        typeOfLeaveLabel.setFont(UIManager.getFont("h2.regular.font"));
        reviewProof.setFont(UIManager.getFont("h2.regular.font"));

        idLabel.setPreferredSize(new Dimension(20, 50));
        toLabel.setPreferredSize(new Dimension(20, 50));
        fromLabel.setPreferredSize(new Dimension(20, 50));
        typeOfLeaveLabel.setPreferredSize(new Dimension(20, 50));
        reviewProof.setPreferredSize(new Dimension(50, 50));

        if (leave_id == -1) {
            idLabel.setText("User Id");
            toLabel.setText("End date");
            fromLabel.setText("Start date");
            typeOfLeaveLabel.setText("Type of leave");
            reviewProof.setVisible(false);
            yesBox.setVisible(false);
            noBox.setVisible(false);
        }

        this.add(idLabel);
        this.add(toLabel);
        this.add(fromLabel);
        this.add(typeOfLeaveLabel);
        this.add(reviewProof);
        this.add(checkBoxPanel);

    }

    private void loadData() {
        Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
        try {
            PreparedStatement st = driver.prepareStatement("select * from leave_records where leave_id = ?");
            st.setInt(1, leave_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                modelLeave = new leaveModel();
                modelLeave.setUserId(rs.getString("id"));
                modelLeave.setTo(rs.getString("start_date"));
                modelLeave.setFrom(rs.getString("end_date"));
                modelLeave.setTypeOfLeave(rs.getString("type_of_leave"));
                modelLeave.setProofLocation(rs.getString("submission_of_proof"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == yesBox) {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure  ?", "Accept Request", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                parentComponent.remove(this);
                try {
                    Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
                    PreparedStatement st = driver.prepareStatement("update leave_records set status = 'Accepted' where leave_id = ?");
                    st.setInt(1, leave_id);
                    st.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Done", "Done", JOptionPane.INFORMATION_MESSAGE);
                    parentComponent.remove(this);
                    parentComponent.revalidate();
                    parentComponent.repaint();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                parentComponent.revalidate();
            } else if (result == 1) {
                group.clearSelection();
            }
        }
        if (e.getSource() == noBox) {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure  ?", "Reject Request", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                parentComponent.remove(this);
                try {
                    Connection driver = new JDBCDriver.driverJDBC().getJDBCDriver();
                    PreparedStatement st = driver.prepareStatement("update leave_records set status = 'Rejected' where leave_id = ?");
                    st.setInt(1, leave_id);
                    st.executeUpdate();
                    parentComponent.remove(this);
                    parentComponent.revalidate();
                    parentComponent.repaint();
                    JOptionPane.showMessageDialog(null, "Done", "Done", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                parentComponent.repaint();
            } else if (result == 1) {
                group.clearSelection();
            }
        }
        if (e.getSource() == reviewProof) {
            try {
                Desktop.getDesktop().open(new File(this.modelLeave.getProofLocation()));
            } catch (Exception ignored) {
                JOptionPane.showMessageDialog(null, "No proof was uploaded", "Proof", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}