package ComJavaPratice;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class JDBC {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName= "root";
        String password = "Chinku@1234";
        Connection con;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }
        listDrivers();
        try {
            System.out.println("Connecting to database:"+ jdbcURL);
            con = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful!!!!!!"+ con);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void listDrivers(){
        Enumeration<Driver> diverList = DriverManager.getDrivers();
        while (diverList.hasMoreElements()){
            Driver driverClass = (Driver)diverList.nextElement();
            System.out.println("  "+driverClass.getClass().getName());
        }
    }
}
