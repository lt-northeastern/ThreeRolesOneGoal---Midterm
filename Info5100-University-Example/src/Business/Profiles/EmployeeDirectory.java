/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Business;
import Business.Person.Person;

import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class EmployeeDirectory {

    Business business;
    ArrayList<EmployeeProfile> employeelist;
    private int nextEmployeeNumber = 1001;

    public EmployeeDirectory(Business d) {

        business = d;
        employeelist = new ArrayList();

    }

    /**
     * Creates an employee profile with the next generated employee ID and adds it to the list.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public EmployeeProfile newEmployeeProfile(Person p) {

        EmployeeProfile sp = new EmployeeProfile(p, "E" + (nextEmployeeNumber++));
        employeelist.add(sp);
        return sp;
    }

    public EmployeeProfile findEmployee(String id) {

        for (EmployeeProfile sp : employeelist) {

            if (sp.isMatch(id)) {
                return sp;
            }
        }
            return null; //not found after going through the whole list
         }
    
    /**
     * Removes an employee profile from the directory.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void removeEmployee(EmployeeProfile ep) {
        employeelist.remove(ep);
    }

    /**
     * Returns every employee profile.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public ArrayList<EmployeeProfile> getEmployeeList() {
        return employeelist;
    }
    
}
