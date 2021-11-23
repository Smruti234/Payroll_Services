package ComJavaPratice;

import java.util.List;

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
}
