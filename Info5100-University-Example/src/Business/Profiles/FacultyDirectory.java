/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Profiles;
import Business.Person.*;
import java.util.ArrayList;

/**
 *
 * @author ogdenc
 */
public class FacultyDirectory {
    
    ArrayList<FacultyProfile> facultylist;
    private int nextFacultyNumber = 1001;

    public FacultyDirectory() {
        facultylist = new ArrayList<>();
    }

    public FacultyProfile newFacultyProfile(Person p) {
        FacultyProfile fp = new FacultyProfile(p, "F" + (nextFacultyNumber++));
        facultylist.add(fp);
        return fp;
    }

    public FacultyProfile findFaculty(String id) {
        for (FacultyProfile fp : facultylist) {
            if (fp.isMatch(id)) {
                return fp;
            }
        }
        return null; //not found after going through the whole list
    }

    public void removeFaculty(FacultyProfile fp) {
        facultylist.remove(fp);
    }

    public ArrayList<FacultyProfile> getFacultyList() {
        return facultylist;
    }
    
}
