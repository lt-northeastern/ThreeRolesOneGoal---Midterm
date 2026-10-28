/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Person.Person;

/**
 *
 * @author kal bugrara
 */
public class StudentProfile extends Profile {

    Person person;
//    Transcript transcript;
    //   EmploymentHistroy employmenthistory;

    public StudentProfile(Person p) {
        super(p);

//        transcript = new Transcript(this);
//        employmenthistory = new EmploymentHistroy();
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public boolean isMatch(String id) {
        return person.getPersonId().equals(id);
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
