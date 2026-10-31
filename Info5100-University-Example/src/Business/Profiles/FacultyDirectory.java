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

    /**
     * Creates an empty list of faculty profiles.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public FacultyDirectory() {
        facultylist = new ArrayList<>();
    }

    /**
     * Creates a faculty profile with the next generated faculty ID and adds it to the list.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public FacultyProfile newFacultyProfile(Person p) {
        FacultyProfile fp = new FacultyProfile(p, "F" + (nextFacultyNumber++));
        facultylist.add(fp);
        return fp;
    }

    /**
     * Returns the faculty profile for a person ID, or null when none is found.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public FacultyProfile findFaculty(String id) {
        for (FacultyProfile fp : facultylist) {
            if (fp.isMatch(id)) {
                return fp;
            }
        }
        return null; //not found after going through the whole list
    }

    /**
     * Removes a faculty profile from the directory.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void removeFaculty(FacultyProfile fp) {
        facultylist.remove(fp);
    }

    /**
     * Returns every faculty profile.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public ArrayList<FacultyProfile> getFacultyList() {
        return facultylist;
    }
    
}
