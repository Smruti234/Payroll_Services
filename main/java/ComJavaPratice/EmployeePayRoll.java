package ComJavaPratice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return this.getEmployeePayrollDataUsingDB(sql);
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

    public List<PayRollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("SELECT * from employee_payroll WHERE START BETWEEN '%s' AND '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    private List<PayRollData> getEmployeePayrollDataUsingDB(String sql) {
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

    public Map<String, Double> getAverageSalaryByGender() {
        String sql = "select gender, AVG(salary) as avg_salary FROM employee_payroll GROUP BY gender";
        Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("avg_salary");
                genderToAverageSalaryMap.put(gender, salary);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }

    public Map<String, Integer> getCountByGender() {
        String sql = "select gender, count(gender) as count from employee_payroll GROUP BY gender";
        Map<String, Integer> genderToAverageSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String gender = resultSet.getString("gender");
                int count = resultSet.getInt("count");
                genderToAverageSalaryMap.put(gender, count);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }

    public PayRollData addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
        int employeeId = -1;
        PayRollData payRollData = null;
        String sql = String.format("INSERT INTO employee_payroll(name, gender, salary,startDate) values ( '%s', '%s', %s, '%s')",name, gender, salary, Date.valueOf(startDate));
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1){
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    employeeId = resultSet.getInt(1);
            }
            payRollData = new PayRollData(employeeId, name, salary, startDate);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return payRollData;
    }
}
