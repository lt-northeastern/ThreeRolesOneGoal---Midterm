/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Person.Person;

import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class StudentDirectory {


    ArrayList<StudentProfile> studentlist;

    public StudentDirectory() {

     studentlist = new ArrayList();

    }

     public StudentProfile newStudentProfile(Person p, String nuid) {

        StudentProfile sp = new StudentProfile(p, nuid);
        studentlist.add(sp);
        return sp;
    }

    public StudentProfile findStudent(String id) {

        for (StudentProfile sp : studentlist) {

            if (sp.isMatch(id)) {
                return sp;
            }
        }
            return null; //not found after going through the whole list
         }
    
       public StudentProfile findStudentByNuid(String nuid) {
        for (StudentProfile sp : studentlist) {
            if (sp.hasNuid(nuid)) {
                return sp;
            }
        }
        return null;
    }
      public void removeStudent(StudentProfile sp) {
        studentlist.remove(sp);
    }
       
    public ArrayList<StudentProfile> getStudentList() {
        return studentlist;
    }

}
