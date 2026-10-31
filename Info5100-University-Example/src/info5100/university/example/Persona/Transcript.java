/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.Persona;

import java.util.Comparator;
import java.util.Collections;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.SeatAssignment;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kal bugrara
 */
public class Transcript {

    StudentProfile student;
    HashMap<String, CourseLoad> courseloadlist;

    CourseLoad currentcourseload;

    public Transcript(StudentProfile sp) {
        student = sp;
        courseloadlist = new HashMap();

    }

    public int getStudentSatisfactionIndex() {
        //for each courseload 
        //get seatassigmnets; 
        //for each seatassignment add 1 if like =true;
        return 0;
    }

    public CourseLoad newCourseLoad(String sem) {

        currentcourseload = new CourseLoad(sem);
        courseloadlist.put(sem, currentcourseload);
        return currentcourseload;
    }

    public CourseLoad getCurrentCourseLoad() {

        return currentcourseload;

    }

    public CourseLoad getCourseLoadBySemester(String semester) {

        return courseloadlist.get(semester);

    }

    public float getStudentTotalScore() {

        float sum = 0;

        for (CourseLoad cl : courseloadlist.values()) {
            sum = sum + cl.getSemesterScore();

        }
        return sum;
    }
    //sat index means student rated their courses with likes;
    public int getStudentSatifactionIndex() {
        ArrayList<SeatAssignment> courseregistrations = getCourseList();
        int sum = 0;
        for (SeatAssignment sa : courseregistrations) {

            if (sa.getLike()) {
                sum = sum + 1;
            }
        }
        return sum;
    }
    //generate a list of all courses taken so far (seetassignments) 
    //from multiple semesters (course loads)
    //from seat assignments we will be able to access the course offers

    public ArrayList<SeatAssignment> getCourseList() {
        ArrayList temp2;
        temp2 = new ArrayList();

        for (CourseLoad cl : courseloadlist.values()) { //extract cl list as objects --ignore label
            temp2.addAll(cl.getSeatAssignments()); //merge one array list to another
        }

        return temp2;

    }

    // ---- added for the Student use case by Hemanth Bezawada (NUID 002059527) ----
    // course loads oldest semester first ("Fall 2025" before "Spring 2026")
    public ArrayList<CourseLoad> getCourseLoads() {
        ArrayList<CourseLoad> loads = new ArrayList<CourseLoad>(courseloadlist.values());
        Collections.sort(loads, new Comparator<CourseLoad>() {
            public int compare(CourseLoad a, CourseLoad b) {
                return semesterOrder(a.getSemester()) - semesterOrder(b.getSemester());
            }
        });
        return loads;
    }

    // "Spring 2026" -> 20261, "Summer 2026" -> 20262, "Fall 2026" -> 20263
    public static int semesterOrder(String semester) {
        String[] parts = semester.trim().split("\\s+");
        int year = 0;
        int season = 0;
        for (String part : parts) {
            if (part.matches("\\d{4}")) {
                year = Integer.parseInt(part);
            } else if (part.equalsIgnoreCase("Spring")) {
                season = 1;
            } else if (part.equalsIgnoreCase("Summer")) {
                season = 2;
            } else if (part.equalsIgnoreCase("Fall")) {
                season = 3;
            }
        }
        return year * 10 + season;
    }

    public int getTotalCreditsCompleted() {
        int sum = 0;
        for (CourseLoad cl : courseloadlist.values()) {
            sum = sum + cl.getGradedCredits();
        }
        return sum;
    }

    public float getGPA() {
        int credits = getTotalCreditsCompleted();
        if (credits == 0) {
            return 0;
        }
        return getStudentTotalScore() / credits;
    }

    // a semester with no classes left (its last class was dropped) comes off the transcript
    public void removeCourseLoad(String semester) {
        CourseLoad removed = courseloadlist.remove(semester);
        if (removed != null && removed == currentcourseload) {
            currentcourseload = null;
        }
    }

    // frees every seat the student still holds in an ungraded class; used when the administrator removes the student
    public void releaseSeats() {
        for (CourseLoad cl : courseloadlist.values()) {
            for (SeatAssignment sa : new ArrayList<SeatAssignment>(cl.getSeatAssignments())) {
                if (!sa.isGraded()) {
                    cl.dropSeatAssignment(sa);
                }
            }
        }
    }
}
