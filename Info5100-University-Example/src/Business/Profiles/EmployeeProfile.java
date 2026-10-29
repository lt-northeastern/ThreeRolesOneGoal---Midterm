/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Person.Person;
import java.time.LocalDate;

/**
 *
 * @author kal bugrara
 */
public class EmployeeProfile extends Profile {
    
    private final String employeeId;
    private String department = "";
    private String jobTitle = "";
    private LocalDate hireDate;




    public EmployeeProfile(Person p, String employeeId) {

        super(p); 
        this.employeeId = employeeId;
        this.hireDate = LocalDate.now();

    }
    @Override
    public String getRole(){
        return  "Admin";
    }
    public String getEmployeeId() {
        return employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? "" : department.trim();
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle == null ? "" : jobTitle.trim();
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

}