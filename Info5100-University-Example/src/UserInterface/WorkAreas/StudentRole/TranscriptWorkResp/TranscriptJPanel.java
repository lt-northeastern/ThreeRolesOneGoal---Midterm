package UserInterface.WorkAreas.StudentRole.TranscriptWorkResp;

import Business.Business;
import Business.Profiles.StudentProfile;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Department.Department;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 * The student's academic history: every class by semester, semester GPAs and the overall GPA.
 *
 * Student use case - written by Hemanth Bezawada (NUID 002059527).
 */
public class TranscriptJPanel extends javax.swing.JPanel {

    JPanel CardSequencePanel;
    Business business;
    StudentProfile student;
    info5100.university.example.Persona.StudentProfile record;   // the academic record in the university model
    Department department;

    public TranscriptJPanel(Business b, StudentProfile sp, JPanel clp) {
        business = b;
        student = sp;
        record = sp.getAcademicRecord();
        department = b.getDepartment();
        CardSequencePanel = clp;
        initComponents();
        lblStudent.setText("Student: " + student.getPerson().getPersonId() + "   -   Degree: " + department.getDegree().getTitle() + " (" + department.getName() + ")");
        refreshTables();
    }

    public void refreshTables() {
        DefaultTableModel courses = (DefaultTableModel) tblCourses.getModel();
        courses.setRowCount(0);
        DefaultTableModel semesters = (DefaultTableModel) tblSemesters.getModel();
        semesters.setRowCount(0);
        int inProgress = 0;
        for (CourseLoad cl : record.getTranscript().getCourseLoads()) {
            for (SeatAssignment sa : cl.getSeatAssignments()) {
                Object[] row = new Object[6];
                row[0] = cl.getSemester();
                row[1] = sa.getCourseOffer().getCourseNumber();
                row[2] = sa.getAssociatedCourse().getName();
                row[3] = sa.getCreditHours();
                row[4] = sa.getLetterGrade();
                row[5] = sa.isGraded() ? String.format("%.1f", sa.GetCourseStudentScore()) : "-";
                courses.addRow(row);
                if (!sa.isGraded()) {
                    inProgress++;
                }
            }
            Object[] srow = new Object[4];
            srow[0] = cl.getSemester();
            srow[1] = cl.getSeatAssignments().size();
            srow[2] = cl.getGradedCredits() + " of " + cl.getTotalCredits();
            srow[3] = cl.getGradedCredits() == 0 ? "-" : String.format("%.2f", cl.getSemesterGPA());
            semesters.addRow(srow);
        }
        lblSummary.setText("Credits completed: " + record.getTranscript().getTotalCreditsCompleted()
                + "   -   Overall GPA: " + (record.getTranscript().getTotalCreditsCompleted() == 0 ? "-" : String.format("%.2f", record.getTranscript().getGPA()))
                + "   -   Classes in progress: " + inProgress);
        int[] w1 = { 100, 90, 330, 60, 90, 80 };
        for (int i = 0; i < w1.length; i++) {
            tblCourses.getColumnModel().getColumn(i).setPreferredWidth(w1[i]);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblStudent = new javax.swing.JLabel();
        lblCourses = new javax.swing.JLabel();
        spCourses = new javax.swing.JScrollPane();
        tblCourses = new javax.swing.JTable();
        lblSemesters = new javax.swing.JLabel();
        spSemesters = new javax.swing.JScrollPane();
        tblSemesters = new javax.swing.JTable();
        lblSummary = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        lblTitle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblTitle.setText("Transcript");
        add(lblTitle);
        lblTitle.setBounds(21, 14, 300, 32);

        lblStudent.setText("Student:");
        add(lblStudent);
        lblStudent.setBounds(30, 52, 850, 20);

        lblCourses.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblCourses.setText("Classes taken");
        add(lblCourses);
        lblCourses.setBounds(30, 80, 300, 22);

        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Semester", "Course No", "Course Name", "Credits", "Grade", "Points"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spCourses.setViewportView(tblCourses);

        add(spCourses);
        spCourses.setBounds(30, 104, 850, 170);

        lblSemesters.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblSemesters.setText("By semester");
        add(lblSemesters);
        lblSemesters.setBounds(30, 284, 300, 22);

        tblSemesters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Semester", "Classes", "Credits Completed", "Semester GPA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spSemesters.setViewportView(tblSemesters);

        add(spSemesters);
        spSemesters.setBounds(30, 308, 500, 90);

        lblSummary.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblSummary.setText("Credits completed:");
        add(lblSummary);
        lblSummary.setBounds(30, 408, 850, 22);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack);
        btnBack.setBounds(780, 434, 100, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        CardSequencePanel.remove(this);
        ((CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel lblCourses;
    private javax.swing.JLabel lblSemesters;
    private javax.swing.JLabel lblStudent;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane spCourses;
    private javax.swing.JScrollPane spSemesters;
    private javax.swing.JTable tblCourses;
    private javax.swing.JTable tblSemesters;
    // End of variables declaration//GEN-END:variables
}
