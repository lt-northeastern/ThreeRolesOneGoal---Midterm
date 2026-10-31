/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Person;

/**
 *
 * @author kal bugrara
 */
public class Person {

    private final String id;
    private String name;
    private String email;
    private String phone;
    private String address;

    /**
     * Creates a person with a generated ID and a name. Email, phone and address start empty; a
     * null name becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public Person(String id, String name) {

        this.id = id;
        this.name = name == null ? "" : name.trim();
        this.email = "";
        this.phone = "";
        this.address = "";
    }

    public String getPersonId() {
        return id;
    }
    
    /**
     * Returns the person's full name.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the person's full name, trimming spaces; null becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setName(String name) {
        this.name = name == null ? "" : name.trim();
    }

    /**
     * Returns the person's email address.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address, trimming spaces; null becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setEmail(String email) {
        this.email = email == null ? "" : email.trim();
    }

    /**
     * Returns the person's phone number.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number, trimming spaces; null becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? "" : phone.trim();
    }

    /**
     * Returns the person's mailing address.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the mailing address, trimming spaces; null becomes an empty string.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void setAddress(String address) {
        this.address = address == null ? "" : address.trim();
    }

    public boolean isMatch(String id) {
        if (getPersonId().equals(id)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getPersonId();
    }
}
