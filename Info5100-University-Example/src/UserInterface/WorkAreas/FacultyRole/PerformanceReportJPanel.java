/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.FacultyRole;

import Business.Business;
import Business.UserAccounts.UserAccount;
import Business.Profiles.StudentProfile;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Department.Department;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luke Tomlinson (003989857)
 */
public class PerformanceReportJPanel extends javax.swing.JPanel {

    private final Business business;
    private final UserAccount useraccount;
    private final JPanel CardSequencePanel;

    private final List<CourseOffer> courseRows = new ArrayList<>();   // section behind each dropdown item

    /**
     * Creates new form PerformanceReportJPanel Generate Report lists each
     * student's grade with the class summary.
     *
     * Luke Tomlinson
     */
    public PerformanceReportJPanel(Business business, UserAccount currentUser, JPanel userProcessContainer) {
        initComponents();

        this.business = business;
        this.useraccount = currentUser;
        this.CardSequencePanel = userProcessContainer;

        tblPerformanceReport.setDefaultEditor(Object.class, null);   // report is read-only

        fillCourseDropdown();
        cmbCoursesPR.addActionListener(e -> clearReport());   // a new course clears the old report
        clearReport();

    }

    // Author: Luke Tomlinson
    // Fills the Course dropdown with every section the logged-in professor teaches
    private void fillCourseDropdown() {
        cmbCoursesPR.removeAllItems();
        courseRows.clear();

        Department department = business.getDepartment();
        String myName = useraccount.getAssociatedPersonProfile().getPerson().getName();

        for (String semester : department.getSemesters()) {
            CourseSchedule schedule = department.getCourseSchedule(semester);
            for (CourseOffer co : schedule.getSchedule()) {
                if (co.getTeacherName().equals(myName)) {   // only my sections
                    courseRows.add(co);
                    cmbCoursesPR.addItem(co.getCourseNumber() + " - " + semester);
                }
            }
        }
    }

    // Author: Luke Tomlinson
    // Returns the section chosen in the dropdown, or null
    private CourseOffer selectedCourse() {
        int i = cmbCoursesPR.getSelectedIndex();
        return (i < 0 || i >= courseRows.size()) ? null : courseRows.get(i);
    }

    // Author: Luke Tomlinson
    // Empties the table and the summary fields
    private void clearReport() {
        ((DefaultTableModel) tblPerformanceReport.getModel()).setRowCount(0);
        txtNumberofStudents.setText("");
        txtAverage.setText("");
        txtHighestScore.setText("");
        txtLowestScore.setText("");
    }

    // Author: Luke Tomlinson
    // Lists every student in the selected section with their grade, then the class summary.
    // Average, highest and lowest use graded students only ("In progress" has no grade yet).
    private void generateReport() {
        clearReport();
        CourseOffer co = selectedCourse();
        if (co == null) {
            JOptionPane.showMessageDialog(this, "Please select a course first.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblPerformanceReport.getModel();
        int students = 0;
        int graded = 0;
        float total = 0;
        float highest = 0;
        float lowest = 0;

        for (StudentProfile student : business.getStudentDirectory().getStudentList()) {
            info5100.university.example.Persona.StudentProfile record = student.getAcademicRecord();
            if (record == null) {
                continue;   // student has no academic record yet
            }
            for (SeatAssignment sa : record.getTranscript().getCourseList()) {
                if (sa.getCourseOffer() == co) {   // this student is in the selected section
                    students++;
                    model.addRow(new Object[]{
                        student.getPerson().getName(),
                        student.getNuid(),
                        sa.getLetterGrade(),
                        sa.isGraded() ? String.format("%.1f", sa.getGrade()) : "-"
                    });
                    if (sa.isGraded()) {
                        float g = sa.getGrade();
                        if (graded == 0 || g > highest) {
                            highest = g;
                        }
                        if (graded == 0 || g < lowest) {
                            lowest = g;
                        }
                        total += g;
                        graded++;
                    }
                }
            }
        }

        txtNumberofStudents.setText(String.valueOf(students));
        if (students == 0) {
            JOptionPane.showMessageDialog(this, "No students are enrolled in this course yet.", "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (graded == 0) {
            txtAverage.setText("No grades yet");
            txtHighestScore.setText("-");
            txtLowestScore.setText("-");
            return;
        }
        txtAverage.setText(String.format("%.2f", total / graded));
        txtHighestScore.setText(String.format("%.1f", highest));
        txtLowestScore.setText(String.format("%.1f", lowest));
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtAverage = new javax.swing.JTextField();
        txtLowestScore = new javax.swing.JTextField();
        lblLowestScore = new javax.swing.JLabel();
        lblCourse = new javax.swing.JLabel();
        titlePerformanceReports = new javax.swing.JLabel();
        lblAverage = new javax.swing.JLabel();
        cmbCoursesPR = new javax.swing.JComboBox<>();
        txtNumberofStudents = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPerformanceReport = new javax.swing.JTable();
        txtHighestScore = new javax.swing.JTextField();
        lblStudentCount = new javax.swing.JLabel();
        lblHighestScore = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnGenerate = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        txtAverage.setEditable(false);
        add(txtAverage);
        txtAverage.setBounds(573, 307, 170, 30);

        txtLowestScore.setEditable(false);
        add(txtLowestScore);
        txtLowestScore.setBounds(573, 350, 170, 30);

        lblLowestScore.setText("Lowest");
        add(lblLowestScore);
        lblLowestScore.setBounds(487, 357, 42, 20);

        lblCourse.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblCourse.setText("Course");
        add(lblCourse);
        lblCourse.setBounds(231, 52, 45, 17);

        titlePerformanceReports.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        titlePerformanceReports.setText("Performance Reports");
        add(titlePerformanceReports);
        titlePerformanceReports.setBounds(16, 14, 173, 23);

        lblAverage.setText("Average");
        add(lblAverage);
        lblAverage.setBounds(482, 314, 47, 20);

        add(cmbCoursesPR);
        cmbCoursesPR.setBounds(314, 49, 193, 23);

        txtNumberofStudents.setEditable(false);
        add(txtNumberofStudents);
        txtNumberofStudents.setBounds(224, 307, 170, 30);

        tblPerformanceReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "NU ID", "Letter Grade", "Grade Points"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPerformanceReport);

        add(jScrollPane1);
        jScrollPane1.setBounds(52, 90, 692, 185);

        txtHighestScore.setEditable(false);
        add(txtHighestScore);
        txtHighestScore.setBounds(224, 350, 170, 30);

        lblStudentCount.setText("Number of Students");
        add(lblStudentCount);
        lblStudentCount.setBounds(62, 314, 117, 20);

        lblHighestScore.setText("Highest");
        add(lblHighestScore);
        lblHighestScore.setBounds(135, 357, 44, 20);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack);
        btnBack.setBounds(83, 528, 80, 23);

        btnGenerate.setText("Generate Report");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });
        add(btnGenerate);
        btnGenerate.setBounds(570, 49, 126, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:

        CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).next(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        // TODO add your handling code here:

        generateReport();
    }//GEN-LAST:event_btnGenerateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JComboBox<String> cmbCoursesPR;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAverage;
    private javax.swing.JLabel lblCourse;
    private javax.swing.JLabel lblHighestScore;
    private javax.swing.JLabel lblLowestScore;
    private javax.swing.JLabel lblStudentCount;
    private javax.swing.JTable tblPerformanceReport;
    private javax.swing.JLabel titlePerformanceReports;
    private javax.swing.JTextField txtAverage;
    private javax.swing.JTextField txtHighestScore;
    private javax.swing.JTextField txtLowestScore;
    private javax.swing.JTextField txtNumberofStudents;
    // End of variables declaration//GEN-END:variables
}
