package datePicker;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class datePicker extends JPanel {
    private final String date = "";
    public boolean state = false;
    public JDatePanelImpl panel;

    public void displayPicker(JPanel parentComponent, JTextField dateInput, int x, int y) {
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.day", "Day");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        panel = new JDatePanelImpl(model, p);
        panel.setFont(new Font("Segue Ui", Font.PLAIN, 30));
        JDatePickerImpl datepicker = new JDatePickerImpl(panel, new AbstractFormatter() {

            private String date;

            @Override
            public String valueToString(Object value) throws ParseException {
                if (value != null) {
                    Calendar cal = (Calendar) value;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    this.date = format.format(cal.getTime());
                    cal.getTime();
                    dateInput.setText(date);
                }
                return "";
            }

            @Override
            public Object stringToValue(String text) throws ParseException {
                return "DATE:";
            }

        });
        JFormattedTextField dateField = (JFormattedTextField) datepicker.getComponent(0);
        dateField.setFont(new Font("Arial", Font.PLAIN, 18));
        this.setBounds(x, y, 200, 300);
        this.add(panel);

        parentComponent.repaint();
    }
}
