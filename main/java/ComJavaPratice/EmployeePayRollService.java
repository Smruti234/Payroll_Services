package ComJavaPratice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EmployeePayRollService {
    private List<PayRollData> employeePayRollList;
    private EmployeePayRoll employeePayRoll;

    public EmployeePayRollService(){
        employeePayRoll = EmployeePayRoll.getInstance();
    }

    public EmployeePayRollService(List<PayRollData> employeePayRollList){
        this();
        this.employeePayRollList = employeePayRollList;
    }

    public List<PayRollData> readEmployeePayRoll(){
        this.employeePayRollList = EmployeePayRoll.getInstance().readData();
        return employeePayRollList;
    }

    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayRoll.updateEmployeeData(name, salary);
        if(result == 0) return;
        PayRollData employeePayRollData = this.getEmployeePayRollData(name);
        if (employeePayRollData != null)
            employeePayRollData.salary = salary;
    }

    private PayRollData getEmployeePayRollData(String name) {
        PayRollData employeePayrollData;
        employeePayrollData = this.employeePayRollList.stream().filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name)).findFirst().orElse(null);
        return employeePayrollData;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<PayRollData> employeePayrollDataList = employeePayRoll.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayRollData(name));
    }

    public List<PayRollData> readEmployeePayRollForDateRange(LocalDate startDate, LocalDate endDate) {
        return employeePayRoll.getEmployeeForDateRange(startDate, endDate);
    }

    public Map<String, Double> readAverageSalaryByGender() {
        return employeePayRoll.getAverageSalaryByGender();
    }

    public Map<String, Integer> readCountSalaryByGender() {
        return employeePayRoll.getCountByGender();
    }

    public void addEmployeePayroll(String name, double salary, LocalDate start, String gender) {
        employeePayRollList.add(employeePayRoll.addEmployeeToPayroll(name, salary, start, gender));
    }
}

