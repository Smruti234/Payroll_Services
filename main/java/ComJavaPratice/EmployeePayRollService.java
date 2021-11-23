package ComJavaPratice;

import java.util.List;

public class EmployeePayRollService {
    List<PayRollData> employeePayRollList;
    public List<PayRollData> readEmployeePayRoll(){
        this.employeePayRollList = new EmployeePayRoll().readData();
        return employeePayRollList;
    }
}
