/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.FacultyRole;

import Business.Business;
import Business.Profiles.StudentProfile;
import Business.UserAccounts.UserAccount;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.Department.Department;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luke Tomlinson (NUID 003989857)
 */
public class ManageStudentsProfileJPanel extends javax.swing.JPanel {
    
    private final Business business;
    private final UserAccount useraccount;
    private final JPanel CardSequencePanel;
    
    private final List<CourseOffer> courseRows = new ArrayList<>();      // section behind each dropdown item
    private final List<SeatAssignment> rows = new ArrayList<>();        // student's seat (grade) behind each table row
    private final List<StudentProfile> studentRows = new ArrayList<>();        // student behind each table row

    /**
     * Builds the Student Profiles screen: the course drop down lists the logged-in professor's
     * sections; picking a course lists its students; clicking a student shows their profile.
     *
     * Luke Tomlinson NUID: 003989857 - Faculty use case.
     */
    public ManageStudentsProfileJPanel(Business business, UserAccount currentUser, JPanel userProcessContainer) {
        initComponents();
        
        this.business = business;
        this.useraccount = currentUser;
        this.CardSequencePanel = userProcessContainer;
        
        tblStudents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblStudents.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });

        fillCourseDropdown();
        cmbCourses.addActionListener(e -> refreshTable());   // reload the table when a different course is picked
        
        refreshTable();
       
       
    }
    // Luke Tomlinson NUID: 003989857 - Faculty use case
    // Fills the Course dropdown with every section the logged-in professor teaches
    private void fillCourseDropdown() {
        cmbCourses.removeAllItems();
        courseRows.clear();

        Department department = business.getDepartment();
        String myName = useraccount.getAssociatedPersonProfile().getPerson().getName();

        for (String semester : department.getSemesters()) {
            CourseSchedule schedule = department.getCourseSchedule(semester);
            for (CourseOffer co : schedule.getSchedule()) {
                if (co.getTeacherName().equals(myName)) {   // only my sections
                    courseRows.add(co);
                    cmbCourses.addItem(co.getCourseNumber() + " - " + semester);
                }
            }
        }
    }
    
    // Luke Tomlinson NUID: 003989857 - Faculty use case
    // Returns the section chosen in the dropdown, or null
    private CourseOffer selectedCourse() {
        int i = cmbCourses.getSelectedIndex();
        return (i < 0 || i >= courseRows.size()) ? null : courseRows.get(i);
    }

    // Luke Tomlinson NUID: 003989857 - Faculty use case
    //Returns the selected table row, or -1 if nothing is selected
    private int selectedRow() {
        int row = tblStudents.getSelectedRow();
        return (row < 0 || row >= rows.size()) ? -1 : row;
    }
    // Luke Tomlinson NUID: 003989857 - Faculty use case
    // Fills the table with the students in the selected section
    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) tblStudents.getModel();
        model.setRowCount(0);
        rows.clear();
        studentRows.clear();
        clearForm();

        CourseOffer co = selectedCourse();
        if (co == null) {
            return;
        }
        
        for (StudentProfile student : business.getStudentDirectory().getStudentList()) {
            info5100.university.example.Persona.StudentProfile record = student.getAcademicRecord();
            if (record == null) {
                continue;   // student has no academic record yet
            }
            for (SeatAssignment sa : record.getTranscript().getCourseList()) {
                if (sa.getCourseOffer() == co) {   // this student sits in my selected section
                    rows.add(sa);
                    studentRows.add(student);
                    model.addRow(new Object[]{
                        student.getPerson().getName(),
                        student.getNuid(),
                        student.getPerson().getEmail(),
                        sa.getLetterGrade()
                    });
                }
            }
        }
    }
    // Luke Tomlinson NUID: 003989857 - Faculty use case
    // Copies the selected student's profile and progress into the form fields
    private void populateFormFromSelection() {
        int row = selectedRow();
        if (row < 0) {
            clearForm();
            return;
        }
        StudentProfile student = studentRows.get(row);
        SeatAssignment sa = rows.get(row);
        float gpa = student.getAcademicRecord().getTranscript().getGPA();

        txtName.setText(student.getPerson().getName());
        txtEmail.setText(student.getPerson().getEmail());
        txtNUID.setText(student.getNuid());
        txtGPA.setText(gpa == 0 ? "No graded courses yet" : String.format("%.2f", gpa));
        txtHobbies.setText(student.getHobbies());
        txtInterests.setText(student.getInterests());
        txtGrade.setText(sa.getLetterGrade());
    }

    // Luke Tomlinson NUID: 003989857 - Faculty use case
    // Empties every field and clears the table selection
    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtNUID.setText("");
        txtGPA.setText("");
        txtHobbies.setText("");
        txtInterests.setText("");
        txtGrade.setText("");
        tblStudents.clearSelection();
    }


        
    
    
    
    
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleManageStudents = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudents = new javax.swing.JTable();
        lblName = new javax.swing.JLabel();
        lblNUID = new javax.swing.JLabel();
        lblHobbies = new javax.swing.JLabel();
        lblGrade = new javax.swing.JLabel();
        lblInterests = new javax.swing.JLabel();
        lblGPA = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtNUID = new javax.swing.JTextField();
        txtHobbies = new javax.swing.JTextField();
        txtGrade = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtGPA = new javax.swing.JTextField();
        txtInterests = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblCourse = new javax.swing.JLabel();
        cmbCourses = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        titleManageStudents.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        titleManageStudents.setText("Manage Students Profiles");
        add(titleManageStudents);
        titleManageStudents.setBounds(15, 16, 230, 23);

        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "NU ID", "Email", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblStudents);

        add(jScrollPane1);
        jScrollPane1.setBounds(26, 91, 692, 185);

        lblName.setText("Name");
        add(lblName);
        lblName.setBounds(53, 294, 34, 20);

        lblNUID.setText("NU ID");
        add(lblNUID);
        lblNUID.setBounds(53, 337, 34, 20);

        lblHobbies.setText("Hobbies");
        add(lblHobbies);
        lblHobbies.setBounds(38, 380, 49, 20);

        lblGrade.setText("Grade");
        add(lblGrade);
        lblGrade.setBounds(51, 421, 36, 20);

        lblInterests.setText("Interests");
        add(lblInterests);
        lblInterests.setBounds(410, 380, 50, 20);

        lblGPA.setText("GPA");
        add(lblGPA);
        lblGPA.setBounds(434, 337, 26, 20);

        lblEmail.setText("Email");
        add(lblEmail);
        lblEmail.setBounds(428, 294, 32, 20);

        txtName.setEditable(false);
        add(txtName);
        txtName.setBounds(132, 287, 170, 30);

        txtNUID.setEditable(false);
        add(txtNUID);
        txtNUID.setBounds(132, 330, 170, 30);

        txtHobbies.setEditable(false);
        txtHobbies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHobbiesActionPerformed(evt);
            }
        });
        add(txtHobbies);
        txtHobbies.setBounds(132, 373, 260, 30);

        txtGrade.setEditable(false);
        add(txtGrade);
        txtGrade.setBounds(132, 414, 140, 30);

        txtEmail.setEditable(false);
        add(txtEmail);
        txtEmail.setBounds(500, 283, 220, 30);

        txtGPA.setEditable(false);
        add(txtGPA);
        txtGPA.setBounds(500, 333, 210, 30);

        txtInterests.setEditable(false);
        add(txtInterests);
        txtInterests.setBounds(500, 373, 220, 30);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack);
        btnBack.setBounds(120, 538, 80, 23);

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        add(btnClear);
        btnClear.setBounds(534, 538, 72, 23);

        lblCourse.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblCourse.setText("Course");
        add(lblCourse);
        lblCourse.setBounds(390, 53, 45, 17);

        cmbCourses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCoursesActionPerformed(evt);
            }
        });
        add(cmbCourses);
        cmbCourses.setBounds(473, 50, 193, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void txtHobbiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHobbiesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHobbiesActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
         CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).next(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    private void cmbCoursesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCoursesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCoursesActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        
       clearForm();
    
    }//GEN-LAST:event_btnClearActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JComboBox<String> cmbCourses;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCourse;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGPA;
    private javax.swing.JLabel lblGrade;
    private javax.swing.JLabel lblHobbies;
    private javax.swing.JLabel lblInterests;
    private javax.swing.JLabel lblNUID;
    private javax.swing.JLabel lblName;
    private javax.swing.JTable tblStudents;
    private javax.swing.JLabel titleManageStudents;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtGPA;
    private javax.swing.JTextField txtGrade;
    private javax.swing.JTextField txtHobbies;
    private javax.swing.JTextField txtInterests;
    private javax.swing.JTextField txtNUID;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables

    
    

}



