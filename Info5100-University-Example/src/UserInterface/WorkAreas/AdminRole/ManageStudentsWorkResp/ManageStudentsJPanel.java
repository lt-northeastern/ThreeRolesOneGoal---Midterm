/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.AdminRole.ManageStudentsWorkResp;

import Business.Business;
import Business.Person.Person;
import Business.Profiles.StudentProfile;
import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;
import Business.Validation;
import java.awt.CardLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;     // add
import java.util.List; 

/**
 *
 * @author ogdenc
 */public class ManageStudentsJPanel extends javax.swing.JPanel {
     
    private Business business;
    private UserAccount currentUser;
    private JPanel userProcessContainer;
    private final List<StudentProfile> rows = new ArrayList<>();

    /**
     * Builds the Manage Students screen: single-row selection, a selected row loads into the
     * form, then the table is filled and the screen starts in Add mode.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public ManageStudentsJPanel(Business business, UserAccount currentUser, JPanel userProcessContainer) {
        initComponents();
        this.business = business;
        this.currentUser = currentUser;
        this.userProcessContainer = userProcessContainer;

        tblStudentData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblStudentData.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });
        refreshTable();
        setAddMode(true);
    }

    // ---------------------------------------------------------------- helpers

    /**
     * Reloads the student table: name, NUID, username (or "(not signed up yet)"), email, last
     * login and last updated.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) tblStudentData.getModel();
        model.setRowCount(0);
        rows.clear();
        UserAccountDirectory uad = business.getUserAccountDirectory();
        for (StudentProfile student : business.getStudentDirectory().getStudentList()) {
            rows.add(student);
            Person person = student.getPerson();
            UserAccount account = uad.findUserAccount(person.getPersonId());
            model.addRow(new Object[]{
                person.getName(),
                student.getNuid(),
                account == null ? "(not signed up yet)" : account.getUserLoginName(),
                person.getEmail(),
                account == null ? "" : formatTime(account.getLastLoginTime()),
                account == null ? "" : formatTime(account.getLastUpdatedTime())
            });
        }
    }

    /**
     * Returns the student for the selected row (looked up in the row list kept by
     * refreshTable), or null.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private StudentProfile selectedStudent() {
        int row = tblStudentData.getSelectedRow();
        return (row < 0 || row >= rows.size()) ? null : rows.get(row);
    }

    /**
     * Copies the selected student's details and username into the form (the password is left
     * blank) and switches to edit mode.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void populateFormFromSelection() {
         StudentProfile student = selectedStudent();
        if (student == null) {
            clearForm();   // nothing selected -> empty form, Add enabled
            return;
        }
        Person person = student.getPerson();
        txtName.setText(person.getName());
        txtEmail.setText(person.getEmail());
        txtPhone.setText(person.getPhone());
        txtAddress.setText(person.getAddress());
        txtEmployeeId.setText(student.getNuid());
        UserAccount account = business.getUserAccountDirectory().findUserAccount(person.getPersonId());
        txtUserName.setText(account == null ? "" : account.getUserLoginName());
        pwEntry.setText("");   // never show the password
        setAddMode(false);
    }

    /**
     * Empties every field, clears the table selection and returns to Add mode.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtEmployeeId.setText("");
        txtUserName.setText("");
        pwEntry.setText("");
        tblStudentData.clearSelection();
        setAddMode(true);
    }

    /**
     * Formats a time as yyyy-MM-dd HH:mm:ss, or "Never" when null.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private String formatTime(LocalDateTime t) {
        return t == null ? "Never" : t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    /**
     * In Add mode (no row selected) only the name and NUID can be entered; the student fills
     * in the rest on the Sign-up page. In edit mode the other fields unlock and the NUID
     * locks.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void setAddMode(boolean addMode) {
        txtEmail.setEnabled(!addMode);
        txtPhone.setEnabled(!addMode);
        txtAddress.setEnabled(!addMode);
        txtUserName.setEnabled(!addMode);
        pwEntry.setEnabled(!addMode);
        btnAdd.setEnabled(addMode);
        txtEmployeeId.setEditable(addMode);   // the NU ID of an existing student is not changed here
    }

    /**
     * Checks used by Add: the name is required and the NUID must be exactly 9 digits and not
     * already used. Returns the first problem as a message, or null.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private String validateNewStudent() {
        if (Validation.isBlank(txtName.getText())) {
            return "Please enter the student's name.";
        }
        String nuid = txtEmployeeId.getText().trim();
        if (!Validation.isValidNuid(nuid)) {
            return "The NU ID must be exactly 9 digits.";
        }
        if (business.getStudentDirectory().findStudentByNuid(nuid) != null) {
            return "A student with this NU ID already exists.";
        }
        return null;
    }

    /**
     * Checks used by Update; returns the first problem as a message, or null. Checks the name,
     * NUID format and uniqueness, email format, and, unless username and password are both
     * blank (pre-registration only), the username rules, uniqueness and password length.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private String validateForm(StudentProfile editing) {
        if (Validation.isBlank(txtName.getText())) {
            return "Please enter the student's name.";
        }
        String nuid = txtEmployeeId.getText().trim();
        if (!Validation.isValidNuid(nuid)) {
            return "The NU ID must be exactly 9 digits.";
        }
        StudentProfile sameNuid = business.getStudentDirectory().findStudentByNuid(nuid);
        if (sameNuid != null && sameNuid != editing) {
            return "A student with this NU ID already exists.";
        }
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !Validation.isValidEmail(email)) {
            return "Please enter a valid email address (for example name@northeastern.edu).";
        }

        String username = txtUserName.getText().trim();
        String password = new String(pwEntry.getPassword());
        UserAccount existing = editing == null ? null
            : business.getUserAccountDirectory().findUserAccount(editing.getPerson().getPersonId());

        if (existing == null && username.isEmpty() && password.isEmpty()) {
            return null;   // pre-registration only: name + NU ID, the student signs up later
        }
        if (!Validation.isValidUsername(username)) {
            return "The user name must be at least 3 characters and cannot contain spaces.\n"
                + "(Leave both user name and password empty to only pre-register the NU ID.)";
        }
        if (business.getUserAccountDirectory().isUsernameTaken(username, existing)) {
            return "That user name is already taken. Please choose another one.";
        }
        if (existing == null && !Validation.isValidPassword(password)) {
            return "The password must be at least 6 characters.";
        }
        if (!password.isEmpty() && !Validation.isValidPassword(password)) {
            return "The new password must be at least 6 characters (leave it empty to keep the old one).";
        }
        return null;
    }

    /**
     * Copies the form values into the student profile and its person record.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void applyForm(StudentProfile student) {
        Person person = student.getPerson();
        person.setName(txtName.getText().trim());
        person.setEmail(txtEmail.getText().trim());
        person.setPhone(txtPhone.getText().trim());
        person.setAddress(txtAddress.getText().trim());
        student.setNuid(txtEmployeeId.getText());
    }
    /**
     * Builds a one-line string of the student's fields so Update can tell whether anything
     * actually changed.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private String snapshot(StudentProfile x) {
        Person p = x.getPerson();
        return p.getName() + "|" + p.getEmail() + "|" + p.getPhone() + "|" + p.getAddress() + "|" + x.getNuid();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblEmployeeID = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmployeeId = new javax.swing.JTextField();
        lblAddress = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudentData = new javax.swing.JTable();
        txtEmail = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        pwEntry = new javax.swing.JPasswordField();
        btnBack = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        lblName = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        lblPhone = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        lblUserName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnClear1 = new javax.swing.JButton();

        lblEmployeeID.setText("NU ID");

        lblEmail.setText("Email");

        lblAddress.setText("Address");

        lblPassword.setText("Password");

        tblStudentData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Name", "NU ID", "User Name", "Email", "Last Login", "Account Updated"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblStudentData);

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        lblTitle.setText("Manage Students");

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        lblName.setText("Name");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        lblPhone.setText("Phone");

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        lblUserName.setText("User Name");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        btnClear1.setText("Clear Form");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(10, 10, 10)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClear1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblName)
                                .addGap(96, 96, 96)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(lblEmail)
                                .addGap(108, 108, 108)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblPhone)
                                .addGap(94, 94, 94)
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(lblAddress)
                                .addGap(91, 91, 91)
                                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblUserName)
                                    .addComponent(lblEmployeeID))
                                .addGap(65, 65, 65)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addComponent(lblPassword)
                                .addGap(82, 82, 82)
                                .addComponent(pwEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 761, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPhone)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAddress)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUserName)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPassword)
                    .addComponent(pwEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEmployeeID)
                    .addComponent(txtEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBack)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDelete)
                        .addComponent(btnClear1)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    /**
     * Removes this screen and returns to the admin work area.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * Deletes the selected student, their person record and login (if any) after confirmation.
     * You cannot delete the account you are logged in with.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    StudentProfile student = selectedStudent();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Please select a row in the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentUser != null && student == currentUser.getAssociatedPersonProfile()) {
            JOptionPane.showMessageDialog(this, "You cannot delete the account you are logged in with.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
            "Delete " + student.getPerson().getName() + " and the login account (if any)? This cannot be undone.",
            "Please confirm", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        business.deleteProfile(student);
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "Student deleted.", "Information", JOptionPane.INFORMATION_MESSAGE);
    
    
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * Pre-registers a student with a name and NUID only; the student then creates the login on
     * the Sign-up page.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        String problem = validateNewStudent();
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Person person = business.getPersonDirectory().newPerson(txtName.getText().trim());
        business.getStudentDirectory().newStudentProfile(person, txtEmployeeId.getText().trim());

        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this,
            "Student added. The student can now create the account on the Sign-up page using this NU ID.",
            "Information", JOptionPane.INFORMATION_MESSAGE);
    
   
    }//GEN-LAST:event_btnAddActionPerformed

    /**
     * Validates and saves edits to the selected student. Updates or creates the login when a
     * username is given, refreshes the last-updated time only when something changed, and says
     * "Nothing was changed" otherwise.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
           StudentProfile student = selectedStudent();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Please select a row in the table first.", "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String problem = validateForm(student);
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
         String before = snapshot(student);
        applyForm(student);
        boolean changed = !before.equals(snapshot(student));

        UserAccountDirectory uad = business.getUserAccountDirectory();
        UserAccount account = uad.findUserAccount(student.getPerson().getPersonId());
        String username = txtUserName.getText().trim();
        String password = new String(pwEntry.getPassword());
        if (account != null) {
            if (!account.getUserLoginName().equals(username)) {
                account.setUserLoginName(username);
                changed = true;
            }
            if (!password.isEmpty()) {
                account.setPassword(password);
                changed = true;
            }
            if (changed) {
                account.touch();   // profile edits count as an account update too
            }
        } else if (!username.isEmpty()) {
            uad.newUserAccount(student, username, password);
            changed = true;
        }

        if (!changed) {
            JOptionPane.showMessageDialog(this, "Nothing was changed.", "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "The changes were saved.", "Information",
            JOptionPane.INFORMATION_MESSAGE);
    

    }//GEN-LAST:event_btnUpdateActionPerformed


    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    /**
     * Clears the form and returns to Add mode.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnClear1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear1;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmployeeID;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JPasswordField pwEntry;
    private javax.swing.JTable tblStudentData;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmployeeId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
