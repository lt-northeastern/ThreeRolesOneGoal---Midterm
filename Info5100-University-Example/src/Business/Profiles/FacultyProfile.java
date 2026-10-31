/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Profiles;

import Business.Person.Person;

/**
 *
 * @author ogdenc
 */
public class FacultyProfile extends Profile {
    private final String facultyId;
    private String department = "";
    private String title = "";

    /**
     * Creates a faculty profile for a person with a generated faculty ID.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public FacultyProfile(Person p, String facultyId) {
        super(p);
        this.facultyId = facultyId;
    }

    /**
     * Returns "Faculty" so the login screen and tables show the correct role.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    @Override
    public String getRole() {
        return "Faculty";
    }

    /**
     * Returns the generated faculty ID (F1001, F1002, ...).
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getFacultyId() {
        return facultyId;
    }

    /**
     * Returns the faculty member's department.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the faculty member's department, trimming spaces; null becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setDepartment(String department) {
        this.department = department == null ? "" : department.trim();
    }

    /**
     * Returns the faculty member's academic title (for example Professor).
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the academic title, trimming spaces; null becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setTitle(String title) {
        this.title = title == null ? "" : title.trim();
    }
    
}
