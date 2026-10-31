/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Person.Person;

public class StudentProfile extends Profile {

    private String nuid;

    /**
     * Creates a student profile for a person with an NUID; a null NUID becomes an empty
     * string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public StudentProfile(Person p, String nuid) {
        super(p);
        this.nuid = nuid == null ? "" : nuid.trim();
    }

    @Override
    public String getRole() {
        return "Student";
    }

    /**
     * Returns the student's 9-digit NUID.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getNuid() {
        return nuid;
    }

    /**
     * Sets the student's NUID, trimming spaces; null becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setNuid(String nuid) {
        this.nuid = nuid == null ? "" : nuid.trim();
    }

    /**
     * Returns true when this student's NUID equals the one given. A blank NUID never matches
     * anyone.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
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
}
