# Three Roles One Goal – Digital University System with Access Control

INFO 5100 Application Engineering and Development, Fall 2026 – Midterm Project (Group 07)

## Team Information

| Member | NUID | Use case | Responsibilities |
|---|---|---|---|
| Hemanth Bezawada | 002059527 | Student | Course work, class registration, graduation audit, transcript, student profile; integration of the university model with the access control layer |
| Luke Tomlinson | (add NUID) | Faculty | (add responsibilities) |
| Christopher Ogden | 003959375 | Administrator | Access control layer from Assignment 3 (login, roles, user accounts, sign up, My Profile); administrator use case: user accounts, personnel, student and faculty records, own profile; admin UI test harness |

## Project Overview

The project joins two code bases from class into one application:

- the **Digital University Model** (`info5100.university.example`): departments, course catalog, course schedules and course offers with seats, student profiles with transcripts and course loads, faculty, and the degree with its core and elective courses;
- the **Access Control Package** (`Business`, `UserInterface`): persons, profiles (student, faculty, employee), user accounts with NUID validation, sign up, login and logout, a shared My Profile screen and one work area per role.

The access control layer decides *who* can log in and *which* work area they see. The university model is the data the work areas operate on. `Business` owns a university `Department`, and every student profile on the access control side points at its academic record in the university model, so a logged-in student always works on their own transcript.

Key features implemented:

- Login with role-based routing: a student, a faculty member and an administrator each land in their own work area and see only their own functions.
- Student use case (see below): register for and drop classes, hand in course work and track progress, run a graduation audit, review the transcript, tell professors about hobbies and interests.
- Administrator use case (see below): create, update, disable and delete login accounts; register and manage staff, students and faculty; see each student's credits, GPA and current classes; university reports (course enrollment, student standing, faculty load) and an account health report; edit their own profile and change their password.
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
| `anna` | `anna1234` | student whose account is **disabled** (Anna Shnider) – login is refused |
| `faculty` | `******` | faculty (Gina Montana) |
| `laura` | `laura1234` | faculty (Laura Brown) |

## Authentication & Access Control

- **Login:** the user name and password typed on the left of the main frame are checked by `UserAccountDirectory.AuthenticateUser`, which compares them with every `UserAccount`. A wrong user name or password, or an empty field, is refused with a message box and no work area opens. Logout clears the work area, and Sign Up lets a new student create an account against their NUID.
- **Role:** every `UserAccount` is attached to a `Profile`. The subclass of the profile (`StudentProfile`, `FacultyProfile`, `EmployeeProfile`) is the role. The main frame checks the profile type and opens the matching work area in its `CardLayout` container, clearing whatever was there before.
- **Authorization:** each work area only contains the buttons of its role, and each screen is created with the profile of the logged-in user, so a student can only reach the student screens and only sees and changes their own record. There is no way to open another role's screens without logging in with an account of that role.
- **Disabled accounts:** every `UserAccount` has an active flag. An administrator can disable an account on *Administer User Accounts*; the main frame then refuses that login with "This account has been disabled. Please contact an administrator." even when the password is right.
- **Administrator check:** `Business.isAdministrator(UserAccount)` is true only for an active account that still exists in the directory and belongs to an `EmployeeProfile`. Every button of the administrator work area, and the edit buttons on My Profile, call it first, so an admin screen cannot be opened with any other account ("Only an administrator can open this screen.").

## Features Implemented

### Student (Hemanth Bezawada)

All student screens live in `UserInterface.WorkAreas.StudentRole` and are NetBeans GUI Builder forms. The student work area takes the logged-in user account, opens each screen with the business object, the student's profile and the card container, and creates the academic record for a student that does not have one yet (for example a student the administrator just registered, or one who just signed up). The **My Profile** button opens the shared profile screen of the access control layer. Every student screen shows the student's name and NUID in its header.

| Screen | What it does |
|---|---|
| Registration | Pick a semester, see every class offered with professor and seats left, register for a class, drop a class. Rules: only the current semester is open; no duplicate registration; a course already completed, or still in progress from an earlier semester, cannot be registered again; at most four classes a semester; a full class cannot be joined; graded classes cannot be dropped; dropping needs confirmation and frees the seat; dropping the last class of a semester takes the empty semester off the transcript. |
| Course Work | Classes of a semester with assignments submitted out of five, progress percentage, grade and whether the student liked the class. Submit an assignment by title (empty, duplicate, sixth and graded-class submissions are refused), like or unlike a class. |
| Transcript | Every class by semester with credits, letter grade and grade points, a per-semester table with semester GPA, and a summary with credits completed, overall GPA and classes in progress. |
| Graduation Audit | Core courses and electives against the MSIS degree, each completed, in progress or not taken; credits completed against the 32 required; a verdict that says ready or lists what is outstanding. |
| About Me | Hobbies and interests the student wants their professors to see; they are kept on the student's profile (fields added by the faculty use case) so the faculty Manage Students Profiles screen can show them once that branch merges. Rules: plain text, at most 120 characters each, not only punctuation; an empty box clears the value; saving with nothing changed says so; Undo Changes restores the saved values. |

Changes to the shared model for the student use case, each in a block marked "added for the Student use case": `Business.getDepartment()`, `StudentProfile.getAcademicRecord()`, grade, like and submission helpers on `SeatAssignment`, semester GPA and drop on `CourseLoad`, seat release on `Seat`, ordered course loads, overall GPA, `removeCourseLoad` on `Transcript`, `getPerson()` on the university `StudentProfile` and `FacultyProfile`, name and teacher/seat helpers on `Course`, `CourseOffer` and `CourseSchedule`, getters and the semester list on `Department`, getters on `Degree` plus the 32-credit and graded-core rules in `Degree.isStudentReadyToGraduate`.

### Administrator (Christopher Ogden)

All administrator screens live in `UserInterface.WorkAreas.AdminRole` and are NetBeans GUI Builder forms. The administrator work area opens each screen with the business object, the logged-in account and the card container, after checking `Business.isAdministrator`.

| Screen | What it does |
|---|---|
| Administer User Accounts | Every login with name, role, created, last login, last updated and **Status** (Active/Disabled). Change a user name or password, delete a login (the person's profile is kept), **disable or enable** an account, and **create a login** for a person who has none yet (for example a student who was only pre-registered). Rules: user name at least 3 characters without spaces and not taken, password at least 6 characters, the admin cannot disable or delete their own account. |
| Manage Personnel (HR) | Register, update and delete staff (employee profiles) with department, job title, hire date (today by default) and their login. |
| Manage Students | Pre-register a student by name and NUID, update their details and login, delete them. The table shows **credits completed, GPA and this semester's classes**; deleting an enrolled student warns which classes they are in, and the delete frees their seats and removes their academic record. |
| Manage Faculty | Register, update and delete faculty with department, title and their login. |
| University Reports | Read-only reports on the university model in one table, chosen in a *Report* dropdown: **Course enrollment** (each class of the chosen semester with professor, credits, seats taken, % full and tuition revenue, plus a semester total), **Student standing** (each student's credits, GPA, core courses done, credits still needed and whether they are ready to graduate) and **Faculty load** (each professor's classes, students, seats and revenue in the chosen semester). |
| Account Health | One table of everything that needs attention: **disabled accounts**, logins that were **never used**, people with **no login account** and people with **no email address**, with a *Show* filter and a summary line that counts each issue. |
| My Profile | The shared profile screen; for an administrator it adds **Edit Profile** (name, email, phone, address) and **Change Password** (current password, new password twice). Student and faculty pages stay view only. |

Rules shared by the staff, student and faculty forms: name required, a valid email address (required for staff), **one email address per person** (`Business.isEmailTaken`, any case), phone 7–20 digits, spaces, `+ ( ) -` (`Validation.isValidPhone`), 9-digit unique NUID for students.

Changes to the shared code for the administrator use case, each commented "Christopher Ogden NUID: 003959375 Admin use case": the active flag on `UserAccount` and the disabled-login check in `ProfileWorkAreaMainFrame`; `Business.isAdministrator`, `Business.isEmailTaken` and freeing a student's seats in `Business.deleteProfile`; `Validation.isValidPhone`; `StudentDirectory.removeStudent` in the university model; a second `MyProfileJPanel` constructor that takes the business (the old one is unchanged).

### Faculty ((add name))

(add)

## Usage Instructions

Student scenario:

1. Log in as `adam` / `******`. The student work area opens.
2. Click **Registration**. Fall 2026 is selected. Select *INFO 7375 Advanced Program Structures* and click *Register for Selected Class*: it moves to *My classes this semester* and the seats left go down by one. Try *INFO 6350*: it is full and is refused. Select *INFO 5100*: it was completed in Fall 2025 and is refused.
3. Click **Course Work**. Select *INFO 7250*, type an assignment title and click *Submit Assignment*: the submitted count and progress rise. Click *Like / Unlike Course* to rate the class.
4. Click **Transcript** to see the two graded semesters, the semester GPAs and the overall GPA of 3.50.
5. Click **Graduation Audit**: all three core courses are done but only 16 of 32 credits, so the verdict lists the missing credits.
6. Click **About Me**, type a hobby and an interest and click *Save* (the faculty Manage Students Profiles screen will show them). Click **My Profile** for the account and profile details, then **Logout**.
7. Log in as `jim` / `******` for a first-semester student with nothing graded yet.

Administrator scenario:

1. Log in as `anna` / `anna1234`: refused, the account is disabled. Log in as `admin` / `******`. The administrator work area opens.
2. Click **Administer User Accounts**. Select `anna` and click *Enable / Disable* → *Yes*: her status becomes Active and she can log in.
3. Click **Manage Students**. Adam shows 16 credits, GPA 3.50 and INFO 7250. Add *Priya Shah* with NUID `912599999` (name and NUID only).
4. Back to **Administer User Accounts**: Priya is in *Person without a login*. Type user name `priya`, password `priya123` and click *Create Account*.
5. In **Manage Students**, select *Jim Dellon* and click *Delete*: the confirmation says he is enrolled in INFO 6350 and that the seat will be freed → *Yes*. A student logging in now sees INFO 6350 with 1 of 1 seats.
6. Click **Manage Personnel (HR)** and add a staff member; the hire date is already today. Try another person's email to see it refused.
7. Click **University Reports**: Fall 2026 shows 5 classes with 2 of 58 seats taken and $12,000 revenue; INFO 6350 is 100% full. Choose *Student standing* to see Adam with 16 credits, GPA 3.50, all 3 core courses and 16 credits to go, and *Faculty load* to see Laura Brown teaching 3 classes.
8. Click **Account Health**: anna is a disabled account and the logins nobody has used yet are listed; choose *Show → Disabled account* to see only anna.
9. Click **My Profile** → *Edit Profile*, change the phone and *Save*; then *Change Password*. Log out and log in with the new password.

(Faculty scenario – add)

## Testing Guide

The student screens were tested with a harness that drives the real panels (selects rows, types text, clicks buttons, reads the message boxes) and then checks the university model: 76 checks in total. It is in the repository as `Info5100-University-Example/test/StudentUITest.java`; after compiling the project run it with a folder for its screenshots:

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
| Long hobbies text | About Me → 121 characters in Hobbies → Save | "at most 120 characters" message, nothing saved |
| Save hobbies | About Me → type a hobby and an interest → Save, then Save again | "saved" message, then "Nothing was changed" |
| Disabled account | log in as `anna` / `anna1234` | "account has been disabled" message, no work area opens |
| Drop the last class | register a new student for one class → select it → Drop → Yes | class removed, seat count back up, the semester disappears from the Transcript |
| Delete a student | log in as `admin` → Manage Students → select Jim Dellon → Delete → Yes; log out and try `jim` / `******`; log in as `adam` → Registration | "Student deleted" message; `jim` gets "Invalid user name or password"; INFO 6350 shows 1 of 1 seats and Open |

Authentication and authorization check: log in with each demo account and confirm that only that role's buttons are available and that a student only sees their own transcript.

The administrator screens have their own harness in the same style, `Info5100-University-Example/test/AdminUITest.java`: 51 checks covering login of a disabled account, the administrator check, the university reports, accounts (status, disable/enable, create a login), personnel, account health, students (credits/GPA, delete warning, freed seat) and My Profile (edit, password). In NetBeans right-click it under *Test Packages* and choose *Run File*, or from the `Info5100-University-Example` folder:

    javac -d build/test -cp build/classes test/AdminUITest.java
    java -cp build/classes:build/test AdminUITest build/admin-screenshots

Administrator cases to repeat by hand:

| Test | Steps | Expected |
|---|---|---|
| Disabled login | log in as `anna` / `anna1234` | "This account has been disabled" message, no work area opens |
| Wrong role | (harness) open the admin work area with a student's account and click a button | "Only an administrator can open this screen." |
| Disable own account | Administer User Accounts → select `admin` → Enable / Disable | "You cannot disable the account you are logged in with." |
| Disable and enable | select `jim` → Enable / Disable → Yes, twice | status Disabled, then Active again |
| Create a login | pre-register a student in Manage Students, then Administer User Accounts → pick them → user name and password → Create Account | "A login account … was created", the new login works |
| Taken user name | Create Account with user name `adam` | "already taken" message |
| Bad phone | Manage Personnel → Add with phone `abc` | the phone rule message |
| Duplicate email | Manage Students → select Adam → email `john.smith@northeastern.edu` → Update | "Another person is already registered with this email address." |
| Delete an enrolled student | Manage Students → Jim → Delete | warning names INFO 6350; after Yes the class has 1 free seat |
| Enrollment report | University Reports → Course enrollment, Fall 2026 | 5 classes; INFO 6350 "1 of 1", 100%; summary "2 of 58 seats taken (3%), revenue $12,000" |
| Student standing | University Reports → Student standing | Semester dropdown greyed out; Adam 16 credits, 3.50, 3 of 3 core, 16 to go, not ready |
| Account health filter | Account Health → Show → Disabled account | only anna is listed |
| Edit own profile | My Profile → Edit Profile → blank name → Save | "Please enter your name."; valid values save, Cancel discards |
| Change password | My Profile → Change Password with a wrong current password / a mismatch / valid values | refused / refused / "Your password was changed." and only the new password works |

## Challenges & Solutions

- The two packages each have a `StudentProfile` and a `Person` class. We kept both and linked them: the access control profile holds a reference to the university record, using the fully qualified name where both are in scope.
- The university model stores course loads in a `HashMap`, so semesters came out in random order. A semester sort key (year, then Spring/Summer/Fall) orders every table oldest first.
- Dropping a class had no model support. `Seat.release()` and `CourseLoad.dropSeatAssignment()` free the seat so another student can take it.
- Deleting a student removed the login and the profile but left their seats taken, so a full class stayed full forever. `Business.deleteProfile` now drops every seat assignment of the student's academic record (each seat is released) and removes the record from the department.
- Compiled `.class` files in `build/` were committed, so after a pull NetBeans ran stale classes (`NoSuchMethodError`). The `build/`, `dist/` and `nbproject/private/` folders and `.DS_Store` are now in `.gitignore` and no longer tracked.
- My Profile is shared by all three roles. Editing was added through a second constructor that takes the business and is only used by the administrator work area, so the student and faculty pages did not change.
- The administrator pull requests were stacked (each branch built on the previous one) so work could continue while reviews were pending; each was merged in order with a merge commit and the next one retargeted to `main`.
- The main branch was replaced with the new authentication layer after the student use case had been merged, so the student screens disappeared from `main`. Instead of forcing the old history back, the old `main` was merged into a branch of the new one: the authentication files were kept as they are and the student hooks were re-added on top (`Business.getDepartment`, `StudentProfile.getAcademicRecord`, the university seed data and the work area handlers, now built from the logged-in `UserAccount`).

## Future Enhancements

- Grades entered by faculty through the faculty use case feeding the student's transcript live.
- Waiting lists for full classes and prerequisites in the catalog.
- Persisting the department to a file so data survives a restart.
- Profile editing and password change for students and faculty (administrators can already do both on My Profile).
- An audit log of administrator actions and of every login attempt (who disabled, created or deleted which account, failed logins), shown as another Account Health issue.
- Exporting the university reports to CSV.
- Storing passwords as salted hashes instead of plain text, and locking an account after several failed logins.

## Contribution Breakdown

| Member | Coding | Documentation | Testing | Other |
|---|---|---|---|---|
| Hemanth Bezawada | Student use case (5 screens, 11 pull requests), model integration, university seed data, empty-login check, post-merge fixes (name headers, empty semesters) | this README's shared sections and the student sections | student UI harness (76 checks) | restoring the student work onto the new authentication layer, reviewing the team's pull requests |
| Luke Tomlinson | (add) | (add) | (add) | (add) |
| Christopher Ogden | Access control layer from Assignment 3 (login, roles, user accounts, sign up, My Profile); administrator use case: admin check and disabled accounts, Administer User Accounts (status, disable/enable, create login), shared phone and email rules, Manage Students credits/GPA and seat release on delete, University Reports and Account Health screens, edit own profile and change password (10 pull requests) | Javadoc comments across the access control code; this README's administrator sections | admin UI harness (51 checks); manual test tables above | removed compiled `build/` files from git and added `.gitignore` |
