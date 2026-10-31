package UserInterface.WorkAreas.StudentRole.CourseWorkResp;

import Business.Business;
import Business.Profiles.StudentProfile;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Department.Department;
import java.awt.CardLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Track the classes of a semester: hand in assignments, watch progress, rate the course.
 *
 * Student use case - written by Hemanth Bezawada (NUID 002059527).
 */
public class CourseWorkJPanel extends javax.swing.JPanel {

    JPanel CardSequencePanel;
    Business business;
    StudentProfile student;
    info5100.university.example.Persona.StudentProfile record;   // the academic record in the university model
    Department department;
    ArrayList<SeatAssignment> rows = new ArrayList<SeatAssignment>();   // mirrors tblCourses

    public CourseWorkJPanel(Business b, StudentProfile sp, JPanel clp) {
        business = b;
        student = sp;
        record = sp.getAcademicRecord();
        department = b.getDepartment();
        CardSequencePanel = clp;
        initComponents();
        lblStudent.setText("Student: " + student.getPerson().getName() + " (NUID " + student.getNuid() + ")");
        populateSemesters();
        refreshTable();
    }

    private void populateSemesters() {
        cmbSemester.removeAllItems();
        String current = null;
        for (CourseLoad cl : record.getTranscript().getCourseLoads()) {
            cmbSemester.addItem(cl.getSemester());
            current = cl.getSemester();   // the last one is the most recent
        }
        if (record.getCurrentCourseLoad() != null) {
            current = record.getCurrentCourseLoad().getSemester();
        }
        if (current != null) {
            cmbSemester.setSelectedItem(current);
        }
    }

    private CourseLoad selectedLoad() {
        Object item = cmbSemester.getSelectedItem();
        return item == null ? null : record.getCourseLoadBySemester(item.toString());
    }

    private SeatAssignment selectedCourse() {
        if (rows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You are not registered for any class in this semester. Use Registration to enroll first.", "No Classes", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int row = tblCourses.getSelectedRow();
        if (row < 0 || row >= rows.size()) {
            JOptionPane.showMessageDialog(this, "Please select a class from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return rows.get(row);
    }

    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) tblCourses.getModel();
        model.setRowCount(0);
        rows.clear();
        CourseLoad load = selectedLoad();
        int submitted = 0;
        int due = 0;
        if (load != null) {
            for (SeatAssignment sa : load.getSeatAssignments()) {
                Object[] row = new Object[7];
                row[0] = sa.getCourseOffer().getCourseNumber();
                row[1] = sa.getAssociatedCourse().getName();
                row[2] = sa.getCourseOffer().getTeacherName();
                row[3] = sa.getSubmissions().size() + " of " + sa.getAssignmentsDue();
                row[4] = sa.getProgressPercent() + "%";
                row[5] = sa.getLetterGrade();
                row[6] = sa.getLike() ? "Yes" : "No";
                model.addRow(row);
                rows.add(sa);
                submitted = submitted + sa.getSubmissions().size();
                due = due + sa.getAssignmentsDue();
            }
        }
        int percent = due == 0 ? 0 : submitted * 100 / due;
        if (record.getTranscript().getCourseLoads().isEmpty()) {
            lblSummary.setText("You are not registered for any classes yet - use Registration first.");
        } else {
            lblSummary.setText("Semester progress: " + submitted + " of " + due + " assignments submitted (" + percent + "%)"
                    + "   -   courses liked so far: " + record.getTranscript().getStudentSatifactionIndex());
        }
        int[] widths = { 90, 280, 120, 90, 80, 90, 60 };
        for (int i = 0; i < widths.length; i++) {
            tblCourses.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblStudent = new javax.swing.JLabel();
        lblSemester = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        lblCourses = new javax.swing.JLabel();
        spCourses = new javax.swing.JScrollPane();
        tblCourses = new javax.swing.JTable();
        lblSummary = new javax.swing.JLabel();
        lblAssignment = new javax.swing.JLabel();
        txtAssignment = new javax.swing.JTextField();
        btnSubmit = new javax.swing.JButton();
        btnLike = new javax.swing.JButton();
        lblHint = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        lblTitle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblTitle.setText("Course Work");
        add(lblTitle);
        lblTitle.setBounds(21, 14, 500, 32);

        lblStudent.setText("Student:");
        add(lblStudent);
        lblStudent.setBounds(560, 22, 320, 20);

        lblSemester.setText("Semester");
        add(lblSemester);
        lblSemester.setBounds(30, 56, 80, 22);

        cmbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {  }));
        cmbSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSemesterActionPerformed(evt);
            }
        });
        add(cmbSemester);
        cmbSemester.setBounds(110, 54, 160, 26);

        lblCourses.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblCourses.setText("My classes");
        add(lblCourses);
        lblCourses.setBounds(30, 86, 300, 22);

        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Course No", "Course Name", "Professor", "Submitted", "Progress", "Grade", "Liked"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spCourses.setViewportView(tblCourses);

        add(spCourses);
        spCourses.setBounds(30, 110, 850, 150);

        lblSummary.setText("Semester progress:");
        add(lblSummary);
        lblSummary.setBounds(30, 268, 850, 20);

        lblAssignment.setText("Assignment title");
        add(lblAssignment);
        lblAssignment.setBounds(30, 304, 120, 22);

        add(txtAssignment);
        txtAssignment.setBounds(150, 302, 300, 26);

        btnSubmit.setText("Submit Assignment");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        add(btnSubmit);
        btnSubmit.setBounds(460, 300, 170, 30);

        btnLike.setText("Like / Unlike Course");
        btnLike.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLikeActionPerformed(evt);
            }
        });
        add(btnLike);
        btnLike.setBounds(640, 300, 170, 30);

        lblHint.setText("Select a class, type the assignment title and click Submit Assignment. Progress counts the assignments handed in out of five per class.");
        add(lblHint);
        lblHint.setBounds(30, 340, 850, 20);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack);
        btnBack.setBounds(780, 434, 100, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterActionPerformed
        refreshTable();
    }//GEN-LAST:event_cmbSemesterActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        SeatAssignment sa = selectedCourse();
        if (sa == null) {
            return;
        }
        String title = txtAssignment.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please type the title of the assignment you are submitting.", "Missing Title", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (sa.isGraded()) {
            JOptionPane.showMessageDialog(this, sa.getCourseOffer().getCourseNumber() + " is already graded (" + sa.getLetterGrade() + "). No more work can be submitted.", "Course Completed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (title.length() > 60) {
            JOptionPane.showMessageDialog(this, "Please keep the assignment title under 60 characters.", "Title Too Long", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (String earlier : sa.getSubmissions()) {
            if (earlier.trim().equalsIgnoreCase(title)) {
                JOptionPane.showMessageDialog(this, "You already submitted \"" + earlier + "\" for this class.", "Duplicate Submission", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        if (!sa.submitAssignment(title)) {
            JOptionPane.showMessageDialog(this, "All " + sa.getAssignmentsDue() + " assignments for " + sa.getCourseOffer().getCourseNumber() + " are already submitted.", "Nothing Due", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        txtAssignment.setText("");
        refreshTable();
        JOptionPane.showMessageDialog(this, "\"" + title + "\" was submitted for " + sa.getCourseOffer().getCourseNumber() + ". Progress: " + sa.getProgressPercent() + "%.", "Submitted", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnLikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLikeActionPerformed
        SeatAssignment sa = selectedCourse();
        if (sa == null) {
            return;
        }
        sa.setLike(!sa.getLike());
        refreshTable();
        JOptionPane.showMessageDialog(this, (sa.getLike() ? "You liked " : "You removed your like from ") + sa.getCourseOffer().getCourseNumber() + ".", "Course Rating", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnLikeActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        CardSequencePanel.remove(this);
        ((CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnLike;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JLabel lblAssignment;
    private javax.swing.JLabel lblCourses;
    private javax.swing.JLabel lblHint;
    private javax.swing.JLabel lblSemester;
    private javax.swing.JLabel lblStudent;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane spCourses;
    private javax.swing.JTable tblCourses;
    private javax.swing.JTextField txtAssignment;
    // End of variables declaration//GEN-END:variables
}
