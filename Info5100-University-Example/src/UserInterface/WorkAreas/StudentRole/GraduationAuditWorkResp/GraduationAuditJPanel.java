package UserInterface.WorkAreas.StudentRole.GraduationAuditWorkResp;

import Business.Business;
import Business.Profiles.StudentProfile;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Department.Department;
import java.awt.CardLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.Degree.Degree;

/**
 * Checks the student's record against the degree: core courses, electives and total credits.
 *
 * Student use case - written by Hemanth Bezawada (NUID 002059527).
 */
public class GraduationAuditJPanel extends javax.swing.JPanel {

    JPanel CardSequencePanel;
    Business business;
    StudentProfile student;
    info5100.university.example.Persona.StudentProfile record;   // the academic record in the university model
    Department department;

    public GraduationAuditJPanel(Business b, StudentProfile sp, JPanel clp) {
        business = b;
        student = sp;
        record = sp.getAcademicRecord();
        department = b.getDepartment();
        CardSequencePanel = clp;
        initComponents();
        lblStudent.setText("Student: " + student.getPerson().getPersonId() + "   -   Degree: " + department.getDegree().getTitle());
        refreshAudit();
    }

    private String statusOf(SeatAssignment sa) {
        if (sa == null) {
            return "Not taken";
        }
        return sa.isGraded() ? "Completed (" + sa.getLetterGrade() + ")" : "In progress";
    }

    public void refreshAudit() {
        Degree degree = department.getDegree();
        DefaultTableModel model = (DefaultTableModel) tblRequirements.getModel();
        model.setRowCount(0);

        int coreDone = 0;
        for (Course c : degree.getCoreList()) {
            SeatAssignment sa = degree.findCourseOnTranscript(record, c);
            if (sa != null && sa.isGraded()) {
                coreDone++;
            }
            Object[] row = new Object[5];
            row[0] = "Core";
            row[1] = c.getCOurseNumber();
            row[2] = c.getName();
            row[3] = c.getCredits();
            row[4] = statusOf(sa);
            model.addRow(row);
        }
        int electivesDone = 0;
        for (Course c : degree.getElectiveList()) {
            SeatAssignment sa = degree.findCourseOnTranscript(record, c);
            if (sa == null) {
                continue;   // electives only show up once the student has picked them
            }
            if (sa.isGraded()) {
                electivesDone++;
            }
            Object[] row = new Object[5];
            row[0] = "Elective";
            row[1] = c.getCOurseNumber();
            row[2] = c.getName();
            row[3] = c.getCredits();
            row[4] = statusOf(sa);
            model.addRow(row);
        }

        int credits = degree.getCompletedCredits(record);
        int required = degree.getRequiredCredits();
        lblCore.setText("Core courses completed: " + coreDone + " of " + degree.getCoreList().size());
        lblElectives.setText("Electives completed: " + electivesDone);
        lblCreditsDone.setText("Credits completed: " + credits + " of " + required + " required");
        boolean ready = degree.isStudentReadyToGraduate(record);
        if (ready) {
            lblVerdict.setText("Audit result: you meet the requirements and are ready to graduate.");
        } else {
            StringBuilder missing = new StringBuilder();
            if (coreDone < degree.getCoreList().size()) {
                missing.append((degree.getCoreList().size() - coreDone)).append(" core course(s) outstanding");
            }
            if (credits < required) {
                if (missing.length() > 0) {
                    missing.append(", ");
                }
                missing.append((required - credits)).append(" more credit(s) needed");
            }
            lblVerdict.setText("Audit result: not ready yet - " + missing + ".");
        }
        int[] widths = { 70, 90, 360, 60, 140 };
        for (int i = 0; i < widths.length; i++) {
            tblRequirements.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblStudent = new javax.swing.JLabel();
        lblReq = new javax.swing.JLabel();
        spRequirements = new javax.swing.JScrollPane();
        tblRequirements = new javax.swing.JTable();
        lblCore = new javax.swing.JLabel();
        lblElectives = new javax.swing.JLabel();
        lblCreditsDone = new javax.swing.JLabel();
        lblVerdict = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        lblTitle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblTitle.setText("Graduation Audit");
        add(lblTitle);
        lblTitle.setBounds(21, 14, 400, 32);

        lblStudent.setText("Student:");
        add(lblStudent);
        lblStudent.setBounds(30, 52, 850, 20);

        lblReq.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblReq.setText("Degree requirements against your transcript");
        add(lblReq);
        lblReq.setBounds(30, 80, 500, 22);

        tblRequirements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Type", "Course No", "Course Name", "Credits", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spRequirements.setViewportView(tblRequirements);

        add(spRequirements);
        spRequirements.setBounds(30, 104, 850, 190);

        lblCore.setText("Core courses completed:");
        add(lblCore);
        lblCore.setBounds(30, 306, 400, 20);

        lblElectives.setText("Electives completed:");
        add(lblElectives);
        lblElectives.setBounds(30, 330, 400, 20);

        lblCreditsDone.setText("Credits completed:");
        add(lblCreditsDone);
        lblCreditsDone.setBounds(30, 354, 400, 20);

        lblVerdict.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblVerdict.setText("Audit result:");
        add(lblVerdict);
        lblVerdict.setBounds(30, 386, 850, 24);

        btnRefresh.setText("Run Audit Again");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        add(btnRefresh);
        btnRefresh.setBounds(30, 434, 160, 30);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack);
        btnBack.setBounds(780, 434, 100, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refreshAudit();
        JOptionPane.showMessageDialog(this, lblVerdict.getText(), "Graduation Audit", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        CardSequencePanel.remove(this);
        ((CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel lblCore;
    private javax.swing.JLabel lblCreditsDone;
    private javax.swing.JLabel lblElectives;
    private javax.swing.JLabel lblReq;
    private javax.swing.JLabel lblStudent;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVerdict;
    private javax.swing.JScrollPane spRequirements;
    private javax.swing.JTable tblRequirements;
    // End of variables declaration//GEN-END:variables
}
