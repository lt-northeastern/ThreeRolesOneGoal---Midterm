/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

*/
package Business;

import info5100.university.example.Persona.Faculty.FacultyProfile;
import info5100.university.example.Department.Department;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseCatalog.Course;
import Business.Person.Person;
import Business.Person.PersonDirectory;
import Business.Profiles.EmployeeDirectory;
import Business.Profiles.EmployeeProfile;
import Business.Profiles.StudentDirectory;
import Business.Profiles.StudentProfile;

import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;


/**
 *
 * @author kal bugrara
 */
class ConfigureABusiness {

    static Business initialize() {
        Business business = new Business("Information Systems");

// Create Persons
      PersonDirectory persondirectory = business.getPersonDirectory();
// person representing sales organization        
        Person person001 = persondirectory.newPerson("John Smith");
        Person person002 = persondirectory.newPerson("Gina Montana");
        Person person003 = persondirectory.newPerson("Adam Rollen");
 
        Person person005 = persondirectory.newPerson("Jim Dellon");
        Person person006 = persondirectory.newPerson("Anna Shnider");
        Person person007 = persondirectory.newPerson("Laura Brown");
        Person person008 = persondirectory.newPerson("Jack While");
        Person person009 = persondirectory.newPerson("Fidelity"); //we use this as customer

// Create Admins to manage the business
        EmployeeDirectory employeedirectory = business.getEmployeeDirectory();
        EmployeeProfile employeeprofile0 = employeedirectory.newEmployeeProfile(person001);
        
        StudentDirectory studentdirectory = business.getStudentDirectory();
        StudentProfile studentprofile0 = studentdirectory.newStudentProfile(person003);
        


   
// Create User accounts that link to specific profiles
        UserAccountDirectory uadirectory = business.getUserAccountDirectory();
        UserAccount ua3 = uadirectory.newUserAccount(employeeprofile0, "admin", "****"); /// order products for one of the customers and performed by a sales person
        UserAccount ua4 = uadirectory.newUserAccount(studentprofile0, "adam", "****"); /// order products for one of the customers and performed by a sales person


        // a second student with a shorter record, so the graduation audit has both outcomes
        StudentProfile studentprofile1 = studentdirectory.newStudentProfile(person008);
        UserAccount ua5 = uadirectory.newUserAccount(studentprofile1, "jack", "****");

        setUpUniversity(business, studentprofile0, studentprofile1);
        return business;

    }

    // ---- added for the Student use case by Hemanth Bezawada (NUID 002059527) ----
    // Courses, degree requirements, three semesters of classes and the students'
    // academic records live in the university model behind the business.
    static void setUpUniversity(Business business, StudentProfile adam, StudentProfile jack) {
        Department dept = business.getDepartment();

        Course c5100 = dept.newCourse("Application Engineering and Development", "INFO 5100", 4);
        Course c6150 = dept.newCourse("Web Design and User Experience", "INFO 6150", 4);
        Course c6205 = dept.newCourse("Program Structures and Algorithms", "INFO 6205", 4);
        Course c6250 = dept.newCourse("Web Development Tools and Methods", "INFO 6250", 4);
        Course c6105 = dept.newCourse("Data Science Engineering Methods", "INFO 6105", 4);
        Course c7250 = dept.newCourse("Big Data Architecture and Governance", "INFO 7250", 4);
        Course c6350 = dept.newCourse("Smartphones Based Web Development", "INFO 6350", 4);
        Course c7375 = dept.newCourse("Advanced Program Structures", "INFO 7375", 4);

        // the MSIS degree: three core courses, everything else counts as an elective
        dept.addCoreCourse(c5100);
        dept.addCoreCourse(c6150);
        dept.addCoreCourse(c6205);
        dept.addElectiveCourse(c6250);
        dept.addElectiveCourse(c6105);
        dept.addElectiveCourse(c7250);
        dept.addElectiveCourse(c6350);
        dept.addElectiveCourse(c7375);

        // professors who teach the classes (same names as the people on the access control side)
        FacultyProfile laura = dept.getFacultyDirectory().newFacultyProfile(dept.getPersonDirectory().newPerson("Laura Brown"));
        FacultyProfile anna = dept.getFacultyDirectory().newFacultyProfile(dept.getPersonDirectory().newPerson("Anna Shnider"));

        // two past semesters and the current one
        CourseSchedule fall25 = dept.newCourseSchedule("Fall 2025");
        offer(fall25, "INFO 5100", laura, 20);
        offer(fall25, "INFO 6150", anna, 20);

        CourseSchedule spring26 = dept.newCourseSchedule("Spring 2026");
        offer(spring26, "INFO 6205", laura, 20);
        offer(spring26, "INFO 6250", anna, 20);
        offer(spring26, "INFO 6105", anna, 20);

        CourseSchedule fall26 = dept.newCourseSchedule("Fall 2026");
        offer(fall26, "INFO 7250", laura, 10);
        offer(fall26, "INFO 6350", anna, 1);    // one seat: shows the "class is full" rule
        offer(fall26, "INFO 7375", laura, 15);
        offer(fall26, "INFO 6105", anna, 12);
        offer(fall26, "INFO 5100", laura, 20);

        // Adam: two graded semesters behind him and one class in progress this fall
        info5100.university.example.Persona.StudentProfile adamRecord =
                dept.getStudentDirectory().newStudentProfile(dept.getPersonDirectory().newPerson(adam.getPerson().getPersonId()));
        CourseLoad load = adamRecord.newCourseLoad("Fall 2025");
        grade(load.newSeatAssignment(fall25.getCourseOfferByNumber("INFO 5100")), 4.0f, true);
        grade(load.newSeatAssignment(fall25.getCourseOfferByNumber("INFO 6150")), 3.3f, false);
        load = adamRecord.newCourseLoad("Spring 2026");
        grade(load.newSeatAssignment(spring26.getCourseOfferByNumber("INFO 6205")), 3.7f, true);
        grade(load.newSeatAssignment(spring26.getCourseOfferByNumber("INFO 6250")), 3.0f, false);
        load = adamRecord.newCourseLoad("Fall 2026");   // created last, so it is the current course load
        SeatAssignment bigData = load.newSeatAssignment(fall26.getCourseOfferByNumber("INFO 7250"));
        bigData.submitAssignment("Assignment 1 - Hadoop setup");
        bigData.submitAssignment("Assignment 2 - MapReduce word count");
        adam.setAcademicRecord(adamRecord);

        // Jack: first semester, holding the single Smartphones seat
        info5100.university.example.Persona.StudentProfile jackRecord =
                dept.getStudentDirectory().newStudentProfile(dept.getPersonDirectory().newPerson(jack.getPerson().getPersonId()));
        load = jackRecord.newCourseLoad("Fall 2026");
        load.newSeatAssignment(fall26.getCourseOfferByNumber("INFO 6350"));
        jack.setAcademicRecord(jackRecord);
    }

    static void offer(CourseSchedule schedule, String courseNumber, FacultyProfile teacher, int seats) {
        info5100.university.example.CourseSchedule.CourseOffer co = schedule.newCourseOffer(courseNumber);
        if (co == null) {
            return;
        }
        co.generatSeats(seats);
        co.AssignAsTeacher(teacher);
    }

    static void grade(SeatAssignment sa, float points, boolean liked) {
        if (sa == null) {
            return;
        }
        sa.setGrade(points);
        sa.setLike(liked);
        for (int i = 1; i <= sa.getAssignmentsDue(); i++) {
            sa.submitAssignment("Assignment " + i);
        }
    }
}
