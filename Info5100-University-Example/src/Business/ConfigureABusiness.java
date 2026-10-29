/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

*/
package Business;

import Business.Person.Person;
import Business.Person.PersonDirectory;
import Business.Profiles.EmployeeDirectory;
import Business.Profiles.EmployeeProfile;
import Business.Profiles.StudentDirectory;
import Business.Profiles.StudentProfile;
import Business.Profiles.FacultyDirectory;
import Business.Profiles.FacultyProfile;

import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;


/**
 *
 * @author kal bugrara
 */
class ConfigureABusiness {

    static Business initialize() {
        Business business = new Business("Information Systems");

// Create Persons
      PersonDirectory persondirectory = business.getPersonDirectory();
// person representing sales organization        
        Person person001 = persondirectory.newPerson("John Smith");
        Person person002 = persondirectory.newPerson("Gina Montana");
        Person person003 = persondirectory.newPerson("Adam Rollen");
 
        Person person005 = persondirectory.newPerson("Jim Dellon");
        Person person006 = persondirectory.newPerson("Anna Shnider");
        Person person007 = persondirectory.newPerson("Laura Brown");
        Person person008 = persondirectory.newPerson("Jack While");
        Person person009 = persondirectory.newPerson("Fidelity"); //we use this as customer
        
        person001.setEmail("john.smith@northeastern.edu");
        person002.setEmail("gina.montana@northeastern.edu");
        person003.setEmail("adam.rollen@northeastern.edu");
        person005.setEmail("jim.dellon@northeastern.edu");
        person006.setEmail("anna.shnider@northeastern.edu");
        person007.setEmail("laura.brown@northeastern.edu");
        person008.setEmail("jack.while@northeastern.edu");

// Create Admins to manage the business

// Create Employees (admins) to manage the business
        EmployeeDirectory employeedirectory = business.getEmployeeDirectory();
        EmployeeProfile employeeprofile0 = employeedirectory.newEmployeeProfile(person001);   // John Smith
        EmployeeProfile employeeprofile1 = employeedirectory.newEmployeeProfile(person008);   // Jack While

        employeeprofile0.setDepartment("Information Technology");
        employeeprofile0.setJobTitle("System Administrator");
        employeeprofile1.setDepartment("Human Resources");
        employeeprofile1.setJobTitle("HR Coordinator");

    // Create Faculty
        FacultyDirectory facultydirectory = business.getFacultyDirectory();
        FacultyProfile facultyprofile0 = facultydirectory.newFacultyProfile(person002);       // Gina Montana
        facultyprofile0.setDepartment("Information Systems");
        facultyprofile0.setTitle("Professor");
        FacultyProfile facultyprofile1 = facultydirectory.newFacultyProfile(person007);       // Laura Brown
        facultyprofile1.setDepartment("Computer Science");
        facultyprofile1.setTitle("Associate Professor");

    // Create Students
        StudentDirectory studentdirectory = business.getStudentDirectory();
        StudentProfile studentprofile0 = studentdirectory.newStudentProfile(person003, "912568888");   // Adam Rollen
        StudentProfile studentprofile1 = studentdirectory.newStudentProfile(person005, "912564001");   // Jim Dellon
        StudentProfile studentprofile2 = studentdirectory.newStudentProfile(person006, "912572002");   // Anna Shnider

    // Create User accounts that link to specific profiles
        UserAccountDirectory uadirectory = business.getUserAccountDirectory();
        UserAccount ua3 = uadirectory.newUserAccount(employeeprofile0, "admin", "******");
        UserAccount ua4 = uadirectory.newUserAccount(studentprofile0, "adam", "******");
        UserAccount ua5 = uadirectory.newUserAccount(facultyprofile0, "faculty", "******");
        UserAccount ua6 = uadirectory.newUserAccount(employeeprofile1, "jack", "jack1234");    // Jack While
        UserAccount ua7 = uadirectory.newUserAccount(facultyprofile1, "laura", "laura1234");   // Laura Brown

  
    return business;
        
        
    }

}
