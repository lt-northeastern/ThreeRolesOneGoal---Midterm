/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Person;

import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class PersonDirectory {
    
      ArrayList<Person> personlist ;
      private int nextId = 1001;
    
      public PersonDirectory (){
          
       personlist = new ArrayList();

    }

    /**
     * Creates a person with the next generated ID (P1001, P1002, ...) and adds it to the
     * directory. The parameter is used as the person's name.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public Person newPerson(String id) {

        Person p = new Person("P" + (nextId++), id);
        personlist.add(p);
        return p;
    }

    public Person findPerson(String id) {

        for (Person p : personlist) {

            if (p.isMatch(id)) {
                return p;
            }
        }
            return null; //not found after going through the whole list
         }
    /**
     * Removes a person from the directory.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void removePerson(Person p) {
        personlist.remove(p);
    }

    /**
     * Returns every person in the directory.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public ArrayList<Person> getPersonList() {
        return personlist;
    }
}
