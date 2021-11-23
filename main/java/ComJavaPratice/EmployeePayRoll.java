package ComJavaPratice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayRoll {
    public Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName= "root";
        String password = "Chinku@1234";
        Connection connection;
        System.out.println("Connecting to database :" + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successfull!!!!" + connection);
        return connection;
    }

    public List<PayRollData> readData(){
        String sql = "SELECT * from employee_payroll";
        List<PayRollData> payRollData = new ArrayList<>();
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Double salary = resultSet.getDouble("salary");
                LocalDate date = resultSet.getDate("start").toLocalDate();
                payRollData.add(new PayRollData(id, name, salary, date));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return payRollData;
    }
}
