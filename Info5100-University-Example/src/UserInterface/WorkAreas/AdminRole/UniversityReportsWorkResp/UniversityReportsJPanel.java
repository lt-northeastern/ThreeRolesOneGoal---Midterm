/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 * modified by Christopher Ogden NUID: 003959375 Admin use case.
 */
package UserInterface.WorkAreas.AdminRole.UniversityReportsWorkResp;

/*
 * University Reports screen for the administrator.
 * Created by Christopher Ogden NUID: 003959375 Admin use case.
 */

import Business.Business;
import Business.Profiles.StudentProfile;
import Business.UserAccounts.UserAccount;
import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.Degree.Degree;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 * Read-only reports on the university model: course enrollment and faculty load for a
 * semester, and the standing of every student against the degree.
 *
 * Christopher Ogden NUID: 003959375 Admin use case.
 */
public class UniversityReportsJPanel extends javax.swing.JPanel {

    static final String COURSE_ENROLLMENT = "Course enrollment";
    static final String STUDENT_STANDING = "Student standing";
    static final String FACULTY_LOAD = "Faculty load";

    private final Business business;
    private final JPanel userProcessContainer;
    private boolean loading = true;   // ignore the dropdown events while the dropdowns are filled

    /**
     * Builds the University Reports screen: fills the report and semester dropdowns (the
     * latest semester is selected) and shows the first report.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    public UniversityReportsJPanel(Business business, UserAccount currentUser, JPanel userProcessContainer) {
        initComponents();
        this.business = business;
        tblReport.setDefaultEditor(Object.class, null);
        this.userProcessContainer = userProcessContainer;
        cmbReport.removeAllItems();
        cmbReport.addItem(COURSE_ENROLLMENT);
        cmbReport.addItem(STUDENT_STANDING);
        cmbReport.addItem(FACULTY_LOAD);
        cmbSemester.removeAllItems();
        for (String semester : business.getDepartment().getSemesters()) {
            cmbSemester.addItem(semester);
        }
        if (cmbSemester.getItemCount() > 0) {
            cmbSemester.setSelectedIndex(cmbSemester.getItemCount() - 1);   // the current semester
        }
        loading = false;
        refreshReport();
    }

    /**
     * Shows the report chosen in the Report dropdown. The Semester dropdown only applies to
     * the course enrollment and faculty load reports.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void refreshReport() {
        if (loading) {
            return;
        }
        String report = String.valueOf(cmbReport.getSelectedItem());
        boolean bySemester = !STUDENT_STANDING.equals(report);
        lblSemester.setEnabled(bySemester);
        cmbSemester.setEnabled(bySemester);
        if (COURSE_ENROLLMENT.equals(report)) {
            showCourseEnrollment();
        } else if (STUDENT_STANDING.equals(report)) {
            showStudentStanding();
        } else if (FACULTY_LOAD.equals(report)) {
            showFacultyLoad();
        }
    }

    /**
     * Puts a new, read-only table model with these column names on the report table.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private DefaultTableModel newTable(String... columns) {
        DefaultTableModel model = (DefaultTableModel) tblReport.getModel();   // the model set up in the designer
        model.setRowCount(0);                  // clear the previous report's rows
        model.setColumnIdentifiers(columns);   // each report has its own column titles
        return model;
    }

    /**
     * The course schedule of the semester chosen in the Semester dropdown, or null.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private CourseSchedule selectedSchedule() {
        Object semester = cmbSemester.getSelectedItem();
        return semester == null ? null : business.getDepartment().getCourseSchedule(semester.toString());
    }

    /**
     * Course enrollment: every class offered in the semester with its professor, seats taken,
     * how full it is and the tuition revenue of the seats taken.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void showCourseEnrollment() {
        DefaultTableModel model = newTable("Course", "Name", "Professor", "Credits", "Seats Taken", "% Full", "Revenue");
        CourseSchedule schedule = selectedSchedule();
        if (schedule == null) {
            lblSummary.setText("No semester has a course schedule yet.");
            return;
        }
        int taken = 0, total = 0, revenue = 0;
        for (CourseOffer co : schedule.getSchedule()) {
            int seats = co.getTotalSeats();
            int used = seats - co.getAvailableSeats();
            model.addRow(new Object[]{
                co.getCourseNumber(),
                co.getSubjectCourse().getName(),
                co.getTeacherName(),
                co.getCreditHours(),
                used + " of " + seats,
                percent(used, seats),
                money(co.getTotalCourseRevenues())
            });
            taken += used;
            total += seats;
            revenue += co.getTotalCourseRevenues();
        }
        lblSummary.setText(schedule.getSemester() + ": " + schedule.getSchedule().size() + " classes, " + taken + " of "
            + total + " seats taken (" + percent(taken, total) + "), revenue " + money(revenue) + ".");
    }
    
    /**
     * Student standing: credits, GPA and core courses of every student measured against the
     * degree, with the credits still needed and whether the student is ready to graduate.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void showStudentStanding() {
        DefaultTableModel model = newTable("Name", "NU ID", "Credits", "GPA", "Core Done", "Credits to Go", "Ready to Graduate");
        Degree degree = business.getDepartment().getDegree();
        int ready = 0;
        List<StudentProfile> students = business.getStudentDirectory().getStudentList();
        for (StudentProfile student : students) {
            info5100.university.example.Persona.StudentProfile record = student.getAcademicRecord();
            int credits = record == null ? 0 : record.getTranscript().getTotalCreditsCompleted();
            int coreDone = 0;
            if (record != null) {
                for (Course core : degree.getCoreList()) {
                    if (degree.isCoreSatisfied(record.getCourseList(), core)) {
                        coreDone++;
                    }
                }
            }
            boolean isReady = record != null && degree.isStudentReadyToGraduate(record);
            if (isReady) {
                ready++;
            }
            model.addRow(new Object[]{
                student.getPerson().getName(),
                student.getNuid(),
                credits,
                credits == 0 ? "-" : String.format("%.2f", record.getTranscript().getGPA()),
                coreDone + " of " + degree.getCoreList().size(),
                Math.max(0, degree.getRequiredCredits() - credits),
                isReady ? "Yes" : "No"
            });
        }
        lblSummary.setText(students.size() + " students, " + ready + " ready to graduate. The degree needs "
            + degree.getRequiredCredits() + " credits and all " + degree.getCoreList().size() + " core courses.");
    }
    /**
     * Faculty load: for each professor teaching in the semester, the classes they teach, the
     * students in those classes and the tuition revenue.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void showFacultyLoad() {
        DefaultTableModel model = newTable("Professor", "Classes", "Courses", "Students", "Seats", "Revenue");
        CourseSchedule schedule = selectedSchedule();
        if (schedule == null) {
            lblSummary.setText("No semester has a course schedule yet.");
            return;
        }
        Map<String, List<CourseOffer>> byProfessor = new LinkedHashMap<>();
        for (CourseOffer co : schedule.getSchedule()) {
            String professor = co.getTeacherName();
            if (!byProfessor.containsKey(professor)) {
                byProfessor.put(professor, new ArrayList<CourseOffer>());
            }
            byProfessor.get(professor).add(co);
        }
        for (Map.Entry<String, List<CourseOffer>> entry : byProfessor.entrySet()) {
            int students = 0, seats = 0, revenue = 0;
            StringBuilder courses = new StringBuilder();
            for (CourseOffer co : entry.getValue()) {
                students += co.getTotalSeats() - co.getAvailableSeats();
                seats += co.getTotalSeats();
                revenue += co.getTotalCourseRevenues();
                if (courses.length() > 0) {
                    courses.append(", ");
                }
                courses.append(co.getCourseNumber());
            }
            model.addRow(new Object[]{entry.getKey(), entry.getValue().size(), courses.toString(), students, seats, money(revenue)});
        }
        lblSummary.setText(schedule.getSemester() + ": " + byProfessor.size() + " professors teach "
            + schedule.getSchedule().size() + " classes.");
    }
    
    /**
     * part of whole as a whole-number percentage, or "-" when whole is 0.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private String percent(int part, int whole) {
        return whole == 0 ? "-" : Math.round(part * 100.0 / whole) + "%";
    }

    /**
     * An amount in dollars with thousands separators, for example $18,000.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private String money(int amount) {
        return String.format("$%,d", amount);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblReport = new javax.swing.JLabel();
        cmbReport = new javax.swing.JComboBox<>();
        lblSemester = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        lblSummary = new javax.swing.JLabel();

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        lblTitle.setText("University Reports");

        lblReport.setText("Report");

        cmbReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbReportActionPerformed(evt);
            }
        });

        lblSemester.setText("Semester");

        cmbSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSemesterActionPerformed(evt);
            }
        });

        tblReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblReport);

        btnBack.setText("<<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblSummary.setText("Summary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(325, 325, 325)
                        .addComponent(lblTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnBack)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSemester)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblReport)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbReport, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
                            .addComponent(lblSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReport)
                    .addComponent(cmbReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSemester))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblSummary)
                .addGap(41, 41, 41)
                .addComponent(btnBack)
                .addContainerGap(36, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Select a different report. 
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void cmbSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterActionPerformed
        // TODO add your handling code here:
        refreshReport();
    }//GEN-LAST:event_cmbSemesterActionPerformed
    /**
     * Select a different semester. 
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void cmbReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbReportActionPerformed
        // TODO add your handling code here:
        refreshReport();
    }//GEN-LAST:event_cmbReportActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JComboBox<String> cmbReport;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblReport;
    private javax.swing.JLabel lblSemester;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblReport;
    // End of variables declaration//GEN-END:variables
}
