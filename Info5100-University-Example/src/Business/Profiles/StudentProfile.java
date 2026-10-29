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
}
