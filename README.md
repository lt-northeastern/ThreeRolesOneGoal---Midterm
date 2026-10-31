# Three Roles One Goal – Digital University System with Access Control

INFO 5100 Application Engineering and Development, Fall 2026 – Midterm Project (Group 07)

## Team Information

| Member | NUID | Use case | Responsibilities |
|---|---|---|---|
| Hemanth Bezawada | 002059527 | Student | Course work, class registration, graduation audit, transcript, student profile; integration of the university model with the access control layer |
| Luke Tomlinson | (add NUID) | Faculty | (add responsibilities) |
| Christopher Ogden | 003959375 | Administrator | Authentication layer (login, sign up, My Profile, NUID validation), administrator screens for persons, students, faculty and user accounts, access control checks |

## Project Overview

The project joins two code bases from class into one application:

- the **Digital University Model** (`info5100.university.example`): departments, course catalog, course schedules and course offers with seats, student profiles with transcripts and course loads, faculty, and the degree with its core and elective courses;
- the **Access Control Package** (`Business`, `UserInterface`): persons, profiles (student, faculty, employee), user accounts with NUID validation, sign up, login and logout, a shared My Profile screen and one work area per role.

The access control layer decides *who* can log in and *which* work area they see. The university model is the data the work areas operate on. `Business` owns a university `Department`, and every student profile on the access control side points at its academic record in the university model, so a logged-in student always works on their own transcript.

Key features implemented:

- Login with role-based routing: a student, a faculty member and an administrator each land in their own work area and see only their own functions.
- Student use case (see below): register for and drop classes, hand in course work and track progress, run a graduation audit, review the transcript.
- Administrator use case: register persons, manage student and faculty profiles and their user accounts; a disabled account (`anna` in the seed data) is refused at login, and every administrator screen checks the logged-in account before it opens.
- Faculty use case: work area with My Profile; the Manage Courses, Student Profiles and Performance Reports screens are in progress on the faculty branch.

## Installation & Setup Instructions

Prerequisites: Java 8 or newer (the project is set to Java 8 source level) and Apache NetBeans 12 or newer. No external libraries are needed.

1. Clone the repository:
   `git clone https://github.com/lt-northeastern/ThreeRolesOneGoal---Midterm.git`
2. In NetBeans choose *File → Open Project* and open the `Info5100-University-Example` folder.
3. Run the project (F6). The main class is `Business.ProfileWorkAreaMainFrame`.

Without NetBeans, from the `Info5100-University-Example` folder:

    javac -d build/classes -cp src $(find src -name "*.java")
    java -cp build/classes Business.ProfileWorkAreaMainFrame

Demo accounts from the seed data (`Business.ConfigureABusiness`):

| User name | Password | Role |
|---|---|---|
| `admin` | `******` | administrator (John Smith) |
| `jack` | `jack1234` | administrator (Jack While) |
| `adam` | `******` | student with two graded semesters (Adam Rollen) |
| `jim` | `******` | first-semester student (Jim Dellon) |
| `faculty` | `******` | faculty (Gina Montana) |
| `laura` | `laura1234` | faculty (Laura Brown) |
| `anna` | `anna1234` | student whose account is disabled (Anna Shnider) – the login is refused |

## Authentication & Access Control

- **Login:** the user name and password typed on the left of the main frame are checked by `UserAccountDirectory.AuthenticateUser`, which compares them with every `UserAccount`. A wrong user name or password, or an empty field, is refused with a message box and no work area opens. Logout clears the work area, and Sign Up lets a new student create an account against their NUID.
- **Disabled accounts:** every `UserAccount` has an active flag. A disabled account (`anna` in the seed data) is refused at login with "This account has been disabled", even with the right password.
- **Role:** every `UserAccount` is attached to a `Profile`. The subclass of the profile (`StudentProfile`, `FacultyProfile`, `EmployeeProfile`) is the role. The main frame checks the profile type and opens the matching work area in its `CardLayout` container, clearing whatever was there before.
- **Authorization:** each work area only contains the buttons of its role, and each screen is created with the profile of the logged-in user, so a student can only reach the student screens and only sees and changes their own record. There is no way to open another role's screens without logging in with an account of that role. The administrator screens also ask `Business.isAdministrator` before they open, so only an active employee account reaches them.

## Features Implemented

### Student (Hemanth Bezawada)

All student screens live in `UserInterface.WorkAreas.StudentRole` and are NetBeans GUI Builder forms. The student work area takes the logged-in user account, opens each screen with the business object, the student's profile and the card container, and creates the academic record for a student that does not have one yet (for example a student the administrator just registered, or one who just signed up). The **My Profile** button opens the shared profile screen of the access control layer. Every student screen shows the student's name and NUID in its header.

| Screen | What it does |
|---|---|
| Registration | Pick a semester, see every class offered with professor and seats left, register for a class, drop a class. Rules: only the current semester is open; no duplicate registration; a course already completed, or still in progress from an earlier semester, cannot be registered again; at most four classes a semester; a full class cannot be joined; graded classes cannot be dropped; dropping needs confirmation and frees the seat; dropping the last class of a semester takes the empty semester off the transcript. |
| Course Work | Classes of a semester with assignments submitted out of five, progress percentage, grade and whether the student liked the class. Submit an assignment by title (empty, duplicate, sixth and graded-class submissions are refused), like or unlike a class. |
| Transcript | Every class by semester with credits, letter grade and grade points, a per-semester table with semester GPA, and a summary with credits completed, overall GPA and classes in progress. |
| Graduation Audit | Core courses and electives against the MSIS degree, each completed, in progress or not taken; credits completed against the 32 required; a verdict that says ready or lists what is outstanding. |

Changes to the shared model for the student use case, each in a block marked "added for the Student use case": `Business.getDepartment()`, `StudentProfile.getAcademicRecord()`, grade, like and submission helpers on `SeatAssignment`, semester GPA and drop on `CourseLoad`, seat release on `Seat`, ordered course loads, overall GPA, `removeCourseLoad` and `releaseSeats` on `Transcript`, `removeStudent` on the university `StudentDirectory`, `getPerson()` on the university `StudentProfile` and `FacultyProfile`, name and teacher/seat helpers on `Course`, `CourseOffer` and `CourseSchedule`, getters and the semester list on `Department`, getters on `Degree` plus the 32-credit and graded-core rules in `Degree.isStudentReadyToGraduate`. `Business.deleteProfile` releases every seat the student held and removes the university record together with the profile.

### Administrator ((add name))

(add)

### Faculty ((add name))

(add)

## Usage Instructions

Student scenario:

1. Log in as `adam` / `******`. The student work area opens.
2. Click **Registration**. Fall 2026 is selected. Select *INFO 7375 Advanced Program Structures* and click *Register for Selected Class*: it moves to *My classes this semester* and the seats left go down by one. Try *INFO 6350*: it is full and is refused. Select *INFO 5100*: it was completed in Fall 2025 and is refused.
3. Click **Course Work**. Select *INFO 7250*, type an assignment title and click *Submit Assignment*: the submitted count and progress rise. Click *Like / Unlike Course* to rate the class.
4. Click **Transcript** to see the two graded semesters, the semester GPAs and the overall GPA of 3.50.
5. Click **Graduation Audit**: all three core courses are done but only 16 of 32 credits, so the verdict lists the missing credits.
6. Click **My Profile** for the account and profile details, then **Logout**.
7. Log in as `jim` / `******` for a first-semester student with nothing graded yet.

(Admin scenario – add)

(Faculty scenario – add)

## Testing Guide

The student screens were tested with a harness that drives the real panels (selects rows, types text, clicks buttons, reads the message boxes) and then checks the university model: 63 checks in total. It is in the repository as `Info5100-University-Example/test/StudentUITest.java`; after compiling the project run it with a folder for its screenshots:

    javac -d build/test -cp build/classes test/StudentUITest.java
    java -cp build/classes:build/test StudentUITest screenshots

Sample cases to repeat by hand:

| Test | Steps | Expected |
|---|---|---|
| Wrong password | log in as `adam` with `xxxx` | "Invalid user name or password" message, no work area opens |
| Register with nothing selected | Registration → Register | message asking to select a class |
| Register twice | select the class already marked Registered → Register | "already registered" message |
| Full class | select INFO 6350 (0 of 1 seats) → Register | "class is full" message |
| Retake | select INFO 5100 → Register | "already completed" message |
| Empty login | leave both fields blank → Login | "enter your user name and password" message |
| Past semester | choose Fall 2025 → Register | "registration is closed" message |
| Drop a graded class | Fall 2025 → select a class → Drop | "cannot be dropped" message |
| Drop a current class | Fall 2026 → select a class → Drop → Yes | class removed, seat count goes back up |
| Empty assignment title | Course Work → select class → Submit with blank title | message asking for a title |
| Sixth submission | submit five titles then a sixth | "already submitted" message |
| Audit | Graduation Audit for `adam` and for `jim` | 16 of 32 credits vs 0 of 32 and three core courses outstanding |
| Disabled account | log in as `anna` / `anna1234` | "account has been disabled" message, no work area opens |
| Drop the last class | register a new student for one class → select it → Drop → Yes | class removed, seat count back up, the semester disappears from the Transcript |
| Delete a student | log in as `admin` → Manage Students → select Jim Dellon → Delete → Yes; log out and try `jim` / `******`; log in as `adam` → Registration | "Student deleted" message; `jim` gets "Invalid user name or password"; INFO 6350 shows 1 of 1 seats and Open |

Authentication and authorization check: log in with each demo account and confirm that only that role's buttons are available and that a student only sees their own transcript.

## Challenges & Solutions

- The two packages each have a `StudentProfile` and a `Person` class. We kept both and linked them: the access control profile holds a reference to the university record, using the fully qualified name where both are in scope.
- The university model stores course loads in a `HashMap`, so semesters came out in random order. A semester sort key (year, then Spring/Summer/Fall) orders every table oldest first.
- Dropping a class had no model support. `Seat.release()` and `CourseLoad.dropSeatAssignment()` free the seat so another student can take it.
- The main branch was replaced with the new authentication layer after the student use case had been merged, so the student screens disappeared from `main`. Instead of forcing the old history back, the old `main` was merged into a branch of the new one: the authentication files were kept as they are and the student hooks were re-added on top (`Business.getDepartment`, `StudentProfile.getAcademicRecord`, the university seed data and the work area handlers, now built from the logged-in `UserAccount`).
- Deleting a student from the administrator screen left the seats occupied and the university record in place. `Business.deleteProfile` now releases every seat the student held and removes the record, so another student can take the seat.

## Future Enhancements

- Grades entered by faculty through the faculty use case feeding the student's transcript live.
- Waiting lists for full classes and prerequisites in the catalog.
- Persisting the department to a file so data survives a restart.
- Changing the password from the profile screen.

## Contribution Breakdown

| Member | Coding | Documentation | Testing | Other |
|---|---|---|---|---|
| Hemanth Bezawada | Student use case (4 screens, 10 pull requests), model integration, university seed data, empty-login check, post-merge fixes (name headers, empty semesters, seat release on delete) | this README's shared sections and the student sections | student UI harness (63 checks) | restoring the student work onto the new authentication layer, reviewing the team's pull requests |
| Luke Tomlinson | (add) | (add) | (add) | (add) |
| Christopher Ogden | (add) | (add) | (add) | (add) |
