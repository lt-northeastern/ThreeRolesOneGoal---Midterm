package UserInterface.WorkAreas.StudentRole.ManageProfileWorkResp;

import Business.Business;
import Business.Profiles.StudentProfile;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.Department.Department;
import java.awt.CardLayout;
import javax.swing.JPanel;
import Business.UserAccounts.UserAccount;

/**
 * The student's own profile: identity, account and a summary of the academic record.
 *
 * Student use case - written by Hemanth Bezawada (NUID 002059527).
 */
public class StudentProfileJPanel extends javax.swing.JPanel {

    JPanel CardSequencePanel;
    Business business;
    StudentProfile student;
    info5100.university.example.Persona.StudentProfile record;   // the academic record in the university model
    Department department;

    public StudentProfileJPanel(Business b, StudentProfile sp, JPanel clp) {
        business = b;
        student = sp;
        record = sp.getAcademicRecord();
        department = b.getDepartment();
        CardSequencePanel = clp;
        initComponents();
        showProfile();
    }

    private void showProfile() {
        txtName.setText(student.getPerson().getPersonId());
        txtRole.setText(student.getRole());
        UserAccount account = business.getUserAccountDirectory().findUserAccount(student.getPerson().getPersonId());
        txtUsername.setText(account == null ? "-" : account.getUserLoginName());
        txtDegree.setText(department.getDegree().getTitle() + " - " + department.getName());
        CourseLoad current = record.getCurrentCourseLoad();
        int classes = current == null ? 0 : current.getSeatAssignments().size();
        txtSemester.setText(current == null ? "Not registered yet" : current.getSemester() + " (" + classes + (classes == 1 ? " class)" : " classes)"));
        txtCredits.setText(String.valueOf(record.getTranscript().getTotalCreditsCompleted()));
        txtGpa.setText(record.getTranscript().getTotalCreditsCompleted() == 0 ? "-" : String.format("%.2f", record.getTranscript().getGPA()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblRole = new javax.swing.JLabel();
        txtRole = new javax.swing.JTextField();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblDegree = new javax.swing.JLabel();
        txtDegree = new javax.swing.JTextField();
        lblSemester = new javax.swing.JLabel();
        txtSemester = new javax.swing.JTextField();
        lblCredits = new javax.swing.JLabel();
        txtCredits = new javax.swing.JTextField();
        lblGpa = new javax.swing.JLabel();
        txtGpa = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        lblTitle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblTitle.setText("My Profile");
        add(lblTitle);
        lblTitle.setBounds(21, 14, 400, 32);

        lblName.setText("Name");
        add(lblName);
        lblName.setBounds(30, 72, 140, 20);

        txtName.setEditable(false);
        add(txtName);
        txtName.setBounds(180, 70, 260, 26);

        lblRole.setText("Role");
        add(lblRole);
        lblRole.setBounds(30, 107, 140, 20);

        txtRole.setEditable(false);
        add(txtRole);
        txtRole.setBounds(180, 105, 260, 26);

        lblUsername.setText("Username");
        add(lblUsername);
        lblUsername.setBounds(30, 142, 140, 20);

        txtUsername.setEditable(false);
        add(txtUsername);
        txtUsername.setBounds(180, 140, 260, 26);

        lblDegree.setText("Degree program");
        add(lblDegree);
        lblDegree.setBounds(30, 177, 140, 20);

        txtDegree.setEditable(false);
        add(txtDegree);
        txtDegree.setBounds(180, 175, 260, 26);

        lblSemester.setText("Current semester");
        add(lblSemester);
        lblSemester.setBounds(30, 212, 140, 20);

        txtSemester.setEditable(false);
        add(txtSemester);
        txtSemester.setBounds(180, 210, 260, 26);

        lblCredits.setText("Credits completed");
        add(lblCredits);
        lblCredits.setBounds(30, 247, 140, 20);

        txtCredits.setEditable(false);
        add(txtCredits);
        txtCredits.setBounds(180, 245, 260, 26);

        lblGpa.setText("Overall GPA");
        add(lblGpa);
        lblGpa.setBounds(30, 282, 140, 20);

        txtGpa.setEditable(false);
        add(txtGpa);
        txtGpa.setBounds(180, 280, 260, 26);

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
    private javax.swing.JLabel lblCredits;
    private javax.swing.JLabel lblDegree;
    private javax.swing.JLabel lblGpa;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSemester;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTextField txtCredits;
    private javax.swing.JTextField txtDegree;
    private javax.swing.JTextField txtGpa;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtRole;
    private javax.swing.JTextField txtSemester;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
