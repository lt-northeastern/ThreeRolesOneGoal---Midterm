/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Person.Person;

public class StudentProfile extends Profile {

    private String nuid;

    public StudentProfile(Person p, String nuid) {
        super(p);
        this.nuid = nuid == null ? "" : nuid.trim();
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public String getNuid() {
        return nuid;
    }

    public void setNuid(String nuid) {
        this.nuid = nuid == null ? "" : nuid.trim();
    }

    public boolean hasNuid(String other) {
        if (other == null || other.trim().isEmpty() || nuid.isEmpty()) {
            return false;   // a blank NUID never matches anyone
        }
        return nuid.equals(other.trim());
    }

    // ---- added for the Student use case by Hemanth Bezawada (NUID 002059527) ----
    // the student's academic record in the university model (transcript, course loads)
    info5100.university.example.Persona.StudentProfile academicRecord;

    public info5100.university.example.Persona.StudentProfile getAcademicRecord() {
        return academicRecord;
    }

    public void setAcademicRecord(info5100.university.example.Persona.StudentProfile record) {
        academicRecord = record;
    }
    
    // ---- Added for the Faculty use case by Luke Tomlinson ----
    // Hobbies and interests a professor can view on Student Profiles
    private String hobbies = "";
    private String interests = "";

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies == null ? "" : hobbies.trim();
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests == null ? "" : interests.trim();
    }
}
