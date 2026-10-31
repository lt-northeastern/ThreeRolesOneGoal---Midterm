/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Business.Person.Person;
import Business.Person.PersonDirectory;
import Business.Profiles.EmployeeDirectory;
import Business.Profiles.StudentDirectory;
import Business.Profiles.*;
import Business.UserAccounts.*;
import info5100.university.example.Department.Department;

import Business.UserAccounts.UserAccountDirectory;

/**
 *
 * @author kal bugrara
 */
public class Business {

    String name;
    PersonDirectory persondirectory; //all people profiles regardless of the role

    EmployeeDirectory employeedirectory;
    UserAccountDirectory useraccountdirectory;
    StudentDirectory studentdirectory;
    FacultyDirectory facultydirectory;
    Department department; // the university model: courses, schedules, transcripts, degree
    


    /**
     * Creates the business and all of its directories, including the FacultyDirectory added
     * for faculty profiles.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public Business(String n) {
        name = n;

        persondirectory = new PersonDirectory();
        employeedirectory = new EmployeeDirectory(this);
        useraccountdirectory = new UserAccountDirectory();
        facultydirectory = new FacultyDirectory();
        studentdirectory = new StudentDirectory();
        department = new Department(n);


    }

    public PersonDirectory getPersonDirectory() {
        return persondirectory;
    }
    /**
     * Returns the directory that holds all faculty profiles.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
     public FacultyDirectory getFacultyDirectory() {
        return facultydirectory;
    }

    public UserAccountDirectory getUserAccountDirectory() {
        return useraccountdirectory;
    }


    public EmployeeDirectory getEmployeeDirectory() {
        return employeedirectory;
    }

    public StudentDirectory getStudentDirectory(){
        return studentdirectory;
    }
    
    /**
     * Registers a student from the Sign-up page. Validates every field and rejects a username
     * that is already taken. If an administrator pre-registered this NUID, the account is
     * linked to that student (the name must match); otherwise a new person and student profile
     * are created. Then the login account is created. Throws IllegalArgumentException with a
     * user-facing message when anything is wrong.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
       public UserAccount signUpStudent(String fullName, String nuid, String username, String password) {
        if (Validation.isBlank(fullName)) {
            throw new IllegalArgumentException("Please enter your full name.");
        }
        if (!Validation.isValidNuid(nuid)) {
            throw new IllegalArgumentException("The NUID must be exactly " + Validation.NUID_LENGTH + " digits.");
        }
        if (!Validation.isValidUsername(username)) {
            throw new IllegalArgumentException("The username must be at least " + Validation.MIN_USERNAME_LENGTH
                    + " characters long and cannot contain spaces.");
        }
        if (!Validation.isValidPassword(password)) {
            throw new IllegalArgumentException("The password must be at least " + Validation.MIN_PASSWORD_LENGTH
                    + " characters long.");
        }
        if (useraccountdirectory.isUsernameTaken(username, null)) {
            throw new IllegalArgumentException("That username is already taken. Please choose another one.");
        }

        StudentProfile student = studentdirectory.findStudentByNuid(nuid);
        if (student != null && useraccountdirectory.findUserAccount(student.getPerson().getPersonId()) != null) {
            throw new IllegalArgumentException("An account already exists for this NUID. "
                    + "Please log in, or contact an administrator if you forgot your credentials.");
        }
        if (student == null) {
            Person person = persondirectory.newPerson(fullName.trim());
            student = studentdirectory.newStudentProfile(person, nuid.trim());
        }   else {
            String nameOnFile = student.getPerson().getName();
            if (nameOnFile.isEmpty()) {
                student.getPerson().setName(fullName);      // admin left no name: use the student's
            } else if (!sameName(nameOnFile, fullName)) {
                throw new IllegalArgumentException("The name you entered does not match our records for this NUID.\n"
                        + "Please check both, or contact an administrator.");
            }
        }
        return useraccountdirectory.newUserAccount(student, username.trim(), password);
    }
    /**
     * Compares two names ignoring case and extra spaces, so "adam  rollen" matches "Adam
     * Rollen".
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
       private static boolean sameName(String a, String b) {
        String x = a == null ? "" : a.trim().replaceAll("\\s+", " ");
        String y = b == null ? "" : b.trim().replaceAll("\\s+", " ");
        return x.equalsIgnoreCase(y);
    }
    /**
     * Deletes a person completely: removes the login account, the role profile (student,
     * faculty or employee) and the person record.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
         public void deleteProfile(Profile profile) {
        Person person = profile.getPerson();
        UserAccount ua = useraccountdirectory.findUserAccount(person.getPersonId());
        if (ua != null) {
            useraccountdirectory.removeUserAccount(ua);
        }
        if (profile instanceof StudentProfile) {
            studentdirectory.removeStudent((StudentProfile) profile);
        } else if (profile instanceof FacultyProfile) {
            facultydirectory.removeFaculty((FacultyProfile) profile);
        } else if (profile instanceof EmployeeProfile) {
            employeedirectory.removeEmployee((EmployeeProfile) profile);
        }
        persondirectory.removePerson(person);
    }

    // ---- added for the Student use case by Hemanth Bezawada (NUID 002059527) ----
    public Department getDepartment() {
        return department;
    }
}
