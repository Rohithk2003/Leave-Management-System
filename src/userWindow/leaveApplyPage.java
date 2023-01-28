package userWindow;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTextField;
import datePicker.datePicker;
import imageData.imageData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;

public class leaveApplyPage extends JPanel implements ActionListener, MouseListener {

    protected final JPanel parentComponent;
    protected final JComboBox<String> typeOfLeave;
    protected final FlatButton documentFilePicker;
    protected final JButton saveBtn;
    protected final FlatTextField selectDateFromPicker;
    protected final FlatTextField selectToDatePicker;
    protected final JButton calenderPickerBtnTo;
    protected final JButton calenderPickerBtnFrom;
    protected final String userId;
    protected final JLabel formLayout;
    protected JPanel panel;
    protected boolean fromDatePickerVisible;
    protected boolean toDatePickerVisible;
    protected String documentProofFileLocation;
    protected String[] typeOfLeaveData;
    protected datePicker datepicker;
    protected Connection driver;

    public leaveApplyPage(JPanel parentComponent, String id) {
        this.parentComponent = parentComponent;
        parentComponent.addMouseListener(this);
        userId = id;
        documentProofFileLocation = "";
        toDatePickerVisible = false;
        fromDatePickerVisible = false;
        parentComponent.setLayout(null);

        panel = new JPanel();
        panel.setBounds(220, 60, 700, 600);
        panel.addMouseListener(this);
        panel.setLayout(null);

        formLayout = new JLabel("LEAVE FORM");
        formLayout.setFont(UIManager.getFont("h00.font"));
        formLayout.setBounds(0, 0, 700, 100);
        formLayout.setHorizontalAlignment(JLabel.CENTER);

        JLabel leaveText = new JLabel("Type of leave :");
        leaveText.setBounds(60, 98, 140, 40);
        leaveText.setFont(UIManager.getFont("h2.regular.font"));

        loadData();
        typeOfLeave = new JComboBox<>(typeOfLeaveData);
        typeOfLeave.setBounds(210, 100, 150, 40);
        typeOfLeave.setFont(UIManager.getFont("h2.regular.font"));
        typeOfLeave.addMouseListener(this);

        calenderPickerBtnTo = new JButton();
        calenderPickerBtnTo.addActionListener(this);
        calenderPickerBtnTo.setIcon(imageData.calenderIcon);

        calenderPickerBtnFrom = new JButton();
        calenderPickerBtnFrom.addActionListener(this);
        calenderPickerBtnFrom.setIcon(imageData.calenderIcon);

        JLabel selectFromDate = new JLabel("Select From date:");
        selectFromDate.setBounds(60, 180, 150, 40);
        selectFromDate.setFont(UIManager.getFont("h2.regular.font"));

        selectDateFromPicker = new FlatTextField();
        selectDateFromPicker.setPlaceholderText("Select date");
        selectDateFromPicker.setTrailingComponent(calenderPickerBtnFrom);
        selectDateFromPicker.setFont(UIManager.getFont("h2.regular.font"));
        selectDateFromPicker.setBounds(210, 180, 150, 40);
        selectDateFromPicker.addMouseListener(this);

        JLabel selectToDate = new JLabel("Select To date:");
        selectToDate.setBounds(60, 260, 150, 40);
        selectToDate.setFont(UIManager.getFont("h2.regular.font"));

        selectToDatePicker = new FlatTextField();
        selectToDatePicker.setPlaceholderText("Select date");
        selectToDatePicker.setTrailingComponent(calenderPickerBtnTo);
        selectToDatePicker.setFont(UIManager.getFont("h2.regular.font"));
        selectToDatePicker.setBounds(210, 260, 150, 40);
        selectToDatePicker.addMouseListener(this);

        documentFilePicker = new FlatButton();
        documentFilePicker.setIcon(imageData.uploadIcon);
        documentFilePicker.setText("Upload Proof If required");
        documentFilePicker.setHorizontalTextPosition(FlatButton.RIGHT);
        documentFilePicker.setFont(UIManager.getFont("h2.regular.font"));
        documentFilePicker.addActionListener(this);
        documentFilePicker.setFocusable(false);
        documentFilePicker.setBounds(60, 340, 300, 40);
        documentFilePicker.addMouseListener(this);

        saveBtn = new JButton("Apply for leave");
        saveBtn.setFont(UIManager.getFont("h2.regular.font"));
        saveBtn.addActionListener(this);
        saveBtn.setFocusable(false);
        saveBtn.setBounds(260, 450, 180, 40);
        saveBtn.addMouseListener(this);

        panel.add(typeOfLeave);
        panel.add(leaveText);
        panel.add(formLayout);
        panel.add(selectFromDate);
        panel.add(selectDateFromPicker);
        panel.add(selectToDate);
        panel.add(selectToDatePicker);
        panel.add(documentFilePicker);
        panel.add(saveBtn);

        parentComponent.add(panel);
    }

    protected void loadData() {
        try {
            this.driver = new JDBCDriver.driverJDBC().getJDBCDriver();
            PreparedStatement st;
            if (userId.substring(0, 2).equals("ST")) {
                st = driver
                        .prepareStatement("select count(*) from typeofleave where availability = ?");
                st.setString(1, "SF");
            } else {
                st = driver.prepareStatement(
                        "select count(*) from typeofleave where availability = ? or availability = ?");
                st.setString(1, "F");
                st.setString(2, "SF");
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                typeOfLeaveData = new String[rs.getInt(1)];
            }

            if (userId.substring(0, 2).equals("ST")) {
                st = driver
                        .prepareStatement(
                                "select leave_type from typeofleave where availability = ? order by leave_type ");
                st.setString(1, "SF");
            } else {
                st = driver.prepareStatement(
                        "select leave_type from typeofleave where availability = ? or availability = ? order by leave_type ");
                st.setString(1, "F");
                st.setString(2, "SF");
            }

            rs = st.executeQuery();
            int i = 0;
            while (rs.next()) {
                typeOfLeaveData[i] = rs.getString("leave_type");
                i++;
            }
            st.close();
            rs.close();
        } catch (

        Exception e) {
            e.printStackTrace();
        }
    }

    protected String checkLeaveExists(java.sql.Date d1, java.sql.Date d2) {
        try {
            PreparedStatement st = driver
                    .prepareStatement("select start_date,end_date,status from leave_records where id = ?");
            st.setString(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (d1.equals(rs.getDate("start_date"))) {
                    return rs.getString("status");
                } else if (d2.equals(rs.getDate("end_date"))) {
                    return rs.getString("status");

                }
            }
            return "";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calenderPickerBtnTo) {
            if (fromDatePickerVisible) {
                panel.remove(datepicker);
                panel.revalidate();
                panel.repaint();
                fromDatePickerVisible = false;
            }
            if (toDatePickerVisible) {
                panel.remove(datepicker);
                panel.revalidate();
                panel.repaint();
                toDatePickerVisible = false;
            } else {
                toDatePickerVisible = true;
                datepicker = new datePicker();
                datepicker.displayPicker(panel, selectToDatePicker, 361, 255);
                panel.add(datepicker);
            }
            parentComponent.revalidate();

        }
        if (e.getSource() == calenderPickerBtnFrom) {
            if (toDatePickerVisible) {
                panel.remove(datepicker);
                panel.revalidate();
                panel.repaint();
                toDatePickerVisible = false;
            }
            if (fromDatePickerVisible) {
                panel.remove(datepicker);
                panel.revalidate();
                panel.repaint();
                fromDatePickerVisible = false;
            } else {
                fromDatePickerVisible = true;
                datepicker = new datePicker();
                datepicker.displayPicker(panel, selectDateFromPicker, 361, 175);
                panel.add(datepicker);
            }
            parentComponent.revalidate();
        }
        if (e.getSource() == documentFilePicker) {
            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(parentComponent);
            if (response == JFileChooser.APPROVE_OPTION) {
                this.documentProofFileLocation = fileChooser.getSelectedFile().getAbsolutePath();
            }

        }
        if (e.getSource() == saveBtn) {
            try {
                Date startDate = Date.valueOf(selectDateFromPicker.getText());
                Date toDate = Date.valueOf(selectToDatePicker.getText());
                String leaveExists = checkLeaveExists(startDate, toDate);
                System.out.println(leaveExists);
                if (!leaveExists.equals("")) {
                    if (leaveExists.equals("Under Review")) {
                        JOptionPane.showMessageDialog(null, "A leave request already exists under the given dates",
                                "Leave request exists", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "A leave was already approved  under the given dates",
                                " Duplicate requests", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (startDate.compareTo(toDate) < 0) {
                    PreparedStatement st = driver
                            .prepareStatement("select proof_required from typeofleave where leave_type = ?");
                    boolean fileUploaded = documentProofFileLocation.equals("");
                    st.setString(1, (String) typeOfLeave.getSelectedItem());
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        if (fileUploaded) {
                            if (fileUploaded != rs.getBoolean("proof_required")) {
                                st = driver.prepareStatement("select max(leave_id) from leave_records");
                                rs = st.executeQuery();
                                int leaveId = 0;
                                while (rs.next()) {
                                    leaveId = rs.getInt(1);
                                }

                                st = driver.prepareStatement("insert into leave_records values (?,?,?,?,?,?,?)");
                                st.setInt(1, leaveId + 1);
                                st.setString(2, userId);
                                st.setString(3, (String) typeOfLeave.getSelectedItem());
                                st.setDate(4, startDate);
                                st.setDate(5, toDate);
                                st.setString(6, documentProofFileLocation);
                                st.setString(7, "Under Review");
                                st.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Leave has been requested", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Proof is required", "Upload Proof",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            st = driver.prepareStatement("select max(leave_id) from leave_records");
                            rs = st.executeQuery();
                            int leaveId = 0;
                            while (rs.next()) {
                                leaveId = rs.getInt(1);
                            }
                            st = driver.prepareStatement("insert into leave_records values (?,?,?,?,?,?,?)");
                            st.setInt(1, leaveId + 1);
                            st.setString(2, userId);
                            st.setString(3, (String) typeOfLeave.getSelectedItem());
                            st.setDate(4, startDate);
                            st.setDate(5, toDate);
                            st.setString(6, documentProofFileLocation);
                            st.setString(7, "Under Review");
                            st.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Leave has been requested", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);

                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid start date and end dates");
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == parentComponent || e.getSource() == panel
                || e.getSource() == saveBtn || e.getSource() == documentFilePicker
                || e.getSource() == selectDateFromPicker || e.getSource() == selectToDatePicker
                || e.getSource() == typeOfLeave) {
            try {
                panel.remove(datepicker);
                panel.revalidate();
                panel.repaint();
                toDatePickerVisible = false;
                fromDatePickerVisible = false;
                parentComponent.revalidate();
            } catch (NullPointerException ignored) {
            }
        }
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
