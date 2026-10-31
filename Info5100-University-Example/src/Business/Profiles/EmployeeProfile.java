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




    /**
     * Creates an admin/employee profile with a generated employee ID. The hire date defaults
     * to today.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public EmployeeProfile(Person p, String employeeId) {

        super(p); 
        this.employeeId = employeeId;
        this.hireDate = LocalDate.now();

    }
    @Override
    public String getRole(){
        return  "Admin";
    }
    /**
     * Returns the generated employee ID (E1001, E1002, ...).
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Returns the employee's department.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the employee's department, trimming spaces; null becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setDepartment(String department) {
        this.department = department == null ? "" : department.trim();
    }

    /**
     * Returns the employee's job title.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the employee's job title, trimming spaces; null becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle == null ? "" : jobTitle.trim();
    }

    /**
     * Returns the date the employee was hired.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public LocalDate getHireDate() {
        return hireDate;
    }

    /**
     * Sets the date the employee was hired.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

}