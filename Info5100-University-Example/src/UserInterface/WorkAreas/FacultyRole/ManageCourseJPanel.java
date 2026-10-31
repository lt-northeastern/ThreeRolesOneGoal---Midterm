/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.FacultyRole;

import Business.Business;
import Business.UserAccounts.UserAccount;
import Business.Validation;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.Department.Department;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;

/**
 *
 * @author luke
 */

  
    
public class ManageCourseJPanel extends javax.swing.JPanel {
    
    private final Business business;
    private final UserAccount useraccount;
    private final JPanel CardSequencePanel;
    
    private final List<CourseOffer> rows = new ArrayList<>();

    /**
     * Creates new form ManageCourseJPanel
     */
    public ManageCourseJPanel(Business business, UserAccount currentUser, JPanel userProcessContainer) {
        initComponents();
        
        this.business = business;
        this.useraccount = currentUser;
        this.CardSequencePanel = userProcessContainer;
        
        tblCourses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCourses.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });
        
        refreshTable();

    }
    // Author: Luke Tomlinson
    // Fills the table with every course section the logged-in professor teaches
    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) tblCourses.getModel();
        model.setRowCount(0);   // clear old rows
        rows.clear();

        Department department = business.getDepartment();
        String myName = useraccount.getAssociatedPersonProfile().getPerson().getName();

        for (String semester : department.getSemesters()) {
            CourseSchedule schedule = department.getCourseSchedule(semester);
            for (CourseOffer co : schedule.getSchedule()) {
                if (co.getTeacherName().equals(myName)) {   // only my sections
                    rows.add(co);
                    model.addRow(new Object[]{
                        co.getCourseNumber(),
                        co.getSubjectCourse().getName(),
                        co.getCreditHours(),
                        semester,
                        co.getTotalSeats() - co.getAvailableSeats()   // enrolled
                    });
                }
            }
        }
    }
    
    // Author: Luke Tomlinson
    // Returns the course section for the selected row, or null if nothing is selected
    private CourseOffer selectedCourse() {
        int row = tblCourses.getSelectedRow();
        return (row < 0 || row >= rows.size()) ? null : rows.get(row);
    }

    // Author: Luke Tomlinson
    // Copies the selected course into the form fields
    private void populateFormFromSelection() {
        CourseOffer co = selectedCourse();
        if (co == null) {
            clearForm();
            return;
        }
        int row = tblCourses.getSelectedRow();
        txtCourseNum.setText(co.getCourseNumber());
        txtCourseName.setText(co.getSubjectCourse().getName());
        txtCredits.setText(String.valueOf(co.getCreditHours()));
        txtSemester.setText(String.valueOf(tblCourses.getValueAt(row, 3)));
        txtEnrolled.setText(String.valueOf(tblCourses.getValueAt(row, 4)));
        
        txtDescription.setText(co.getDescription());
        txtRoom.setText(co.getRoom());
        txtTime.setText(co.getMeetingTime());
        cmbDay.setSelectedItem(co.getMeetingDay().isEmpty() ? "Monday" : co.getMeetingDay());
    }

    // Author: Luke Tomlinson
    // Empties every field and clears the table selection
    private void clearForm() {
        txtCourseNum.setText("");
        txtCourseName.setText("");
        txtCredits.setText("");
        txtSemester.setText("");
        txtEnrolled.setText("");
        txtDescription.setText("");
        txtRoom.setText("");
        txtTime.setText("");
        cmbDay.setSelectedIndex(0);
        tblCourses.clearSelection();
    }
    
     // Author: Luke Tomlinson
    // Checks the editable fields; returns a message describing the first problem, or null if OK
    private String validateForm() {
        if (Validation.isBlank(txtRoom.getText())) {
            return "Please enter a room (for example Snell 108).";
        }
        if (Validation.isBlank(txtTime.getText())) {
            return "Please enter a meeting time (for example 6:00-9:00 PM).";
        }
        return null;
    }

    // Author: Luke Tomlinson
    // Copies the editable fields into the selected course section
    private void applyForm(CourseOffer co) {
        co.setDescription(txtDescription.getText());
        co.setRoom(txtRoom.getText());
        co.setMeetingTime(txtTime.getText());
        co.setMeetingDay((String) cmbDay.getSelectedItem());
    }

    // Author: Luke Tomlinson
    // One string of the editable values, used to tell whether anything actually changed
    private String snapshot(CourseOffer co) {
        return co.getDescription() + "|" + co.getRoom() + "|" + co.getMeetingTime() + "|" + co.getMeetingDay();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleManageCourses = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCourses = new javax.swing.JTable();
        lblCourseNum = new javax.swing.JLabel();
        lblCourseName = new javax.swing.JLabel();
        Semester = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCourseNum = new javax.swing.JTextField();
        txtCourseName = new javax.swing.JTextField();
        txtSemester = new javax.swing.JTextField();
        txtEnrolled = new javax.swing.JTextField();
        txtCredits = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        txtTime = new javax.swing.JTextField();
        txtRoom = new javax.swing.JTextField();
        cmbDay = new javax.swing.JComboBox<>();
        btnBack = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setForeground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        titleManageCourses.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        titleManageCourses.setText("Manage Courses");
        add(titleManageCourses);
        titleManageCourses.setBounds(64, 29, 138, 23);

        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Course #", "Course Name", "Credits", "Semester", "Enrolled"
            }
        ));
        jScrollPane1.setViewportView(tblCourses);

        add(jScrollPane1);
        jScrollPane1.setBounds(50, 70, 668, 185);

        lblCourseNum.setText("Course #");
        add(lblCourseNum);
        lblCourseNum.setBounds(135, 297, 52, 17);

        lblCourseName.setText("Course Name");
        add(lblCourseName);
        lblCourseName.setBounds(108, 338, 79, 17);

        Semester.setText("Semester");
        add(Semester);
        Semester.setBounds(132, 420, 55, 17);

        jLabel4.setText("Credits");
        add(jLabel4);
        jLabel4.setBounds(145, 461, 42, 17);

        jLabel5.setText("Enrolled");
        add(jLabel5);
        jLabel5.setBounds(140, 379, 47, 17);

        txtCourseNum.setEditable(false);
        txtCourseNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCourseNumActionPerformed(evt);
            }
        });
        add(txtCourseNum);
        txtCourseNum.setBounds(215, 294, 168, 23);

        txtCourseName.setEditable(false);
        add(txtCourseName);
        txtCourseName.setBounds(215, 335, 260, 23);

        txtSemester.setEditable(false);
        add(txtSemester);
        txtSemester.setBounds(215, 417, 168, 23);

        txtEnrolled.setEditable(false);
        add(txtEnrolled);
        txtEnrolled.setBounds(215, 376, 168, 23);

        txtCredits.setEditable(false);
        txtCredits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCreditsActionPerformed(evt);
            }
        });
        add(txtCredits);
        txtCredits.setBounds(215, 458, 168, 23);

        jLabel1.setText("Description");
        add(jLabel1);
        jLabel1.setBounds(454, 297, 66, 17);

        jLabel2.setText("Room");
        add(jLabel2);
        jLabel2.setBounds(486, 338, 34, 17);

        jLabel3.setText("Meeting Time");
        add(jLabel3);
        jLabel3.setBounds(442, 420, 78, 17);

        jLabel6.setText("Meeting Day");
        add(jLabel6);
        jLabel6.setBounds(447, 379, 73, 17);

        txtDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescriptionActionPerformed(evt);
            }
        });
        add(txtDescription);
        txtDescription.setBounds(548, 294, 170, 23);

        txtTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimeActionPerformed(evt);
            }
        });
        add(txtTime);
        txtTime.setBounds(548, 417, 170, 23);

        txtRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRoomActionPerformed(evt);
            }
        });
        add(txtRoom);
        txtRoom.setBounds(548, 335, 170, 23);

        cmbDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" }));
        add(cmbDay);
        cmbDay.setBounds(548, 376, 170, 23);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack);
        btnBack.setBounds(50, 544, 80, 23);

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        add(btnUpdate);
        btnUpdate.setBounds(514, 544, 73, 23);

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        add(btnClear);
        btnClear.setBounds(624, 544, 72, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCreditsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCreditsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCreditsActionPerformed

    private void txtCourseNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCourseNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCourseNumActionPerformed

    private void txtDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescriptionActionPerformed

    private void txtTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimeActionPerformed

    private void txtRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoomActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        
         CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).next(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        
         clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        
        CourseOffer co = selectedCourse();
        if (co == null) {
            JOptionPane.showMessageDialog(this, "Please select a row in the table first.", "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String problem = validateForm();
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String before = snapshot(co);
        applyForm(co);
        if (before.equals(snapshot(co))) {
            JOptionPane.showMessageDialog(this, "Nothing was changed.", "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "The changes were saved.", "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnUpdateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Semester;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbDay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCourseName;
    private javax.swing.JLabel lblCourseNum;
    private javax.swing.JTable tblCourses;
    private javax.swing.JLabel titleManageCourses;
    private javax.swing.JTextField txtCourseName;
    private javax.swing.JTextField txtCourseNum;
    private javax.swing.JTextField txtCredits;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtEnrolled;
    private javax.swing.JTextField txtRoom;
    private javax.swing.JTextField txtSemester;
    private javax.swing.JTextField txtTime;
    // End of variables declaration//GEN-END:variables
}
