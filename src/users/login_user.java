package users;

import JDBCDriver.driverJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class login_user {
    private String username;
    private String password;
    // private String position;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean verifyCredentials() {
        try {
            Connection driver = new driverJDBC().getJDBCDriver();
            String query = "select * from users where username = ? and password = ? ";
            PreparedStatement st = driver.prepareStatement(query);
            st.setString(1, this.username);
            st.setString(2, this.password);
            ResultSet r = st.executeQuery();
            if (r.next()) {
                return true;
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
