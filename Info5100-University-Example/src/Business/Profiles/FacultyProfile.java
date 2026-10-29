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

    public FacultyProfile(Person p, String facultyId) {
        super(p);
        this.facultyId = facultyId;
    }

    @Override
    public String getRole() {
        return "Faculty";
    }

    public String getFacultyId() {
        return facultyId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? "" : department.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title.trim();
    }
    
}
