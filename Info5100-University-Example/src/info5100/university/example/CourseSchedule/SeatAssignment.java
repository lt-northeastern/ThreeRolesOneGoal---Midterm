/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.CourseSchedule;

import java.util.ArrayList;
import info5100.university.example.CourseCatalog.Course;

/**
 *
 * @author kal bugrara
 */
public class SeatAssignment {
    float grade; //(Letter grade mappings: A=4.0, A-=3.7, B+=3.3, B=3.0, )
    Seat seat;
    boolean like; //true means like and false means not like
    CourseLoad courseload;
    public SeatAssignment(CourseLoad cl, Seat s){
        seat = s;
        courseload = cl;
    }
     
    public boolean getLike(){
        return like;
    }
    public void assignSeatToStudent(CourseLoad cl){
        courseload = cl;
    }
    
    public int getCreditHours(){
        return seat.getCourseCredits();
       
    }
    public Seat getSeat(){
        return seat;
    }
    public CourseOffer getCourseOffer(){
        
        return seat.getCourseOffer();
    }
    public Course getAssociatedCourse(){
        
        return getCourseOffer().getSubjectCourse();
    }
    public float GetCourseStudentScore(){
        return getCreditHours()*grade;
    }
    
    
    

    // ---- added for the Student use case by Hemanth Bezawada (NUID 002059527) ----
    ArrayList<String> submissions;   // titles of the assignments the student handed in
    static final int ASSIGNMENTS_PER_COURSE = 5;

    public float getGrade() {
        return grade;
    }

    public void setGrade(float g) {
        grade = g;
    }

    // a grade of 0 means the course is still in progress
    public boolean isGraded() {
        return grade > 0;
    }

    // Northeastern style letter for the grade points
    public String getLetterGrade() {
        if (!isGraded()) {
            return "In progress";
        }
        if (grade >= 4.0f) return "A";
        if (grade >= 3.7f) return "A-";
        if (grade >= 3.3f) return "B+";
        if (grade >= 3.0f) return "B";
        if (grade >= 2.7f) return "B-";
        if (grade >= 2.3f) return "C+";
        if (grade >= 2.0f) return "C";
        return "F";
    }

    public void setLike(boolean l) {
        like = l;
    }

    public ArrayList<String> getSubmissions() {
        if (submissions == null) {
            submissions = new ArrayList<String>();
        }
        return submissions;
    }

    public int getAssignmentsDue() {
        return ASSIGNMENTS_PER_COURSE;
    }

    // returns false when every assignment for the course is already in
    public boolean submitAssignment(String title) {
        if (getSubmissions().size() >= ASSIGNMENTS_PER_COURSE) {
            return false;
        }
        getSubmissions().add(title);
        return true;
    }

    public int getProgressPercent() {
        return getSubmissions().size() * 100 / ASSIGNMENTS_PER_COURSE;
    }
}
