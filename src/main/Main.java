/**
 * @code main and single packedge of the program
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * here local DB named @code test
 * code shows how works "SQL Injection"
 * and how to prewent it with using
 * PreparedStatement
 */
public final class Main {

    /**
     * private constructor.
     */
    private Main() { }

    /**
     *  @code main method.
     */
    public static void main(final String[] args) {

        String drv = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test"
                + "?useUnicode=true"
                + "&useJDBCCompliantTimezoneShift=true"
                + "&useLegacyDatetimeCode=false"
                + "&serverTimezone=UTC";
        Properties acces = new Properties();
            acces.put("user", "root");
            acces.put("password", "1");

        //loading MySQL driver
        try {
            Class.forName(drv);
            System.out.println("Connection success.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //connecting to DB and make requests
        try (Connection c = DriverManager.getConnection(url, acces);
            Statement st = c.createStatement();
            PreparedStatement pst = c.prepareStatement(
                    "select * from user where id = ?")) {

            // making Query with hacked code
            ResultSet rs = st.executeQuery(
                    "select * from user where id ='1' or 1 = '1'");

            // output results
            while (rs.next()) {
                System.out.println(rs.getString("name") + "\t"
                        + rs.getInt("age"));
            }
            System.out.println("------------------");

            // another case - using PreparedStatement
            String userId = "1' or 1 = '1";
            pst.setString(1, userId);

            ResultSet prs = pst.executeQuery();

            // output results for PreparedStatement
            while (prs.next()) {
                System.out.println(prs.getString("name") + "\t"
                        + prs.getInt("age"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
