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
}
