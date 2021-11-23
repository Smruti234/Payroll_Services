package ComJavaPratice;

import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

public class EmployeePayRollTest {
    @Test
    public void givenDataBaseConnection_ReturnDataFromDataBase(){
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        List<PayRollData> list = employeePayRollService.readEmployeePayRoll();
        System.out.println(list);
        Assert.assertEquals(3, list.size());
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB(){
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        List<PayRollData> payRollDataList = employeePayRollService.readEmployeePayRoll();
        employeePayRollService.updateEmployeeSalary("Chinku",40000000);
        boolean result = employeePayRollService.checkEmployeePayrollInSyncWithDB("Chinku");
        Assert.assertTrue(result);
    }
}
