package ComJavaPratice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayRoll {
    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayRoll employeePayRoll;
    private EmployeePayRoll(){
    }

    public static EmployeePayRoll getInstance(){
        if (employeePayRoll == null)
            employeePayRoll = new EmployeePayRoll();
        return employeePayRoll;
    }

    public Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName= "root";
        String password = "Chinku@1234";
        Connection connection;
        System.out.println("Connecting to database :" + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successfull!!" + connection);
        return connection;
    }

    public List<PayRollData> readData(){
        String sql = "SELECT * from employee_payroll";
        List<PayRollData> payRollDataList = new ArrayList<>();
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            payRollDataList = this.getEmployeePayrollData(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return payRollDataList;
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';",salary,name);
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public List<PayRollData> getEmployeePayrollData(String name) {
        List<PayRollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<PayRollData> getEmployeePayrollData(ResultSet resultSet) {
        List<PayRollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate start = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new PayRollData(id, name, salary, start));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * from employee_payroll WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
