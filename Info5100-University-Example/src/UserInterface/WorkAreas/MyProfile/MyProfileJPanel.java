/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 * modified by Christopher Ogden NUID: 003959375 Admin use case.
 */
package UserInterface.WorkAreas.MyProfile;

import Business.Business;
import Business.Person.*;
import Business.Profiles.*;
import Business.UserAccounts.*;
import java.awt.CardLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JPanel;
import Business.Validation;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

/**
 *
 * @author ogdenc
 */
public class MyProfileJPanel extends javax.swing.JPanel {
    
    private final Business business;   // Christopher Ogden NUID: 003959375 Admin use case: null means view only
    private final JPanel userProcessContainer;
    private final UserAccount account;


    /**
     * Builds the My Profile page for the logged-in user, locks every field and fills it in.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     * Christopher Ogden NUID: 003959375 Admin use case: now passes no business to the constructor below (view only).
     */
    public MyProfileJPanel(UserAccount account, JPanel userProcessContainer) {
        this(null, account, userProcessContainer);   // student and faculty: view only, as before
    }

    /**
     * Builds the My Profile page with access to the business, so an administrator can edit
     * their own details. Called from the administrator work area.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    public MyProfileJPanel(Business business, UserAccount account, JPanel userProcessContainer) {
        initComponents();
        this.business = business;
        this.account = account;
        this.userProcessContainer = userProcessContainer;
        makeFieldsReadOnly();
        populateProfile();
        updateButtons(); 
    
    }
    /**
     * Makes every text field non-editable; this page is view-only.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
     private void makeFieldsReadOnly() {
        txtName.setEditable(false);
        txtRole.setEditable(false);
        txtEmployeeID.setEditable(false);
        txtDepartment.setEditable(false);
        txtJobTitle.setEditable(false);
        txtHireDate.setEditable(false);
        txtEmail.setEditable(false);
        txtPhone.setEditable(false);
        txtAddress.setEditable(false);
        txtUserName.setEditable(false);
        txtAccountCreated.setEditable(false);
        txtLastLogin.setEditable(false);
        txtAccountLastUpdated.setEditable(false);
    }
    /**
     * Fills in the name, role, contact details, username and account times, then shows the
     * rows for the user's role: NUID for students; Faculty ID, department and title for
     * faculty; Employee ID, department, job title and hire date for admins.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
 private void populateProfile() {
        Profile profile = account.getAssociatedPersonProfile();
        Person person = profile.getPerson();

        txtName.setText(orDash(person.getName()));
        txtRole.setText(orDash(profile.getRole()));
        txtEmail.setText(orDash(person.getEmail()));
        txtPhone.setText(orDash(person.getPhone()));
        txtAddress.setText(orDash(person.getAddress()));
        txtUserName.setText(orDash(account.getUserLoginName()));
        txtAccountCreated.setText(formatTime(account.getCreatedTime()));
        txtLastLogin.setText(formatTime(account.getLastLoginTime()));
        txtAccountLastUpdated.setText(formatTime(account.getLastUpdatedTime()));

        // rows that only apply to some roles are hidden for the others
        setDepartmentRowVisible(false);
        setJobTitleRowVisible(false);
        setHireDateRowVisible(false);

        if (profile instanceof StudentProfile) {
            lblEmployeeID.setText("NUID:");
            txtEmployeeID.setText(orDash(((StudentProfile) profile).getNuid()));
        } else if (profile instanceof FacultyProfile) {
            FacultyProfile f = (FacultyProfile) profile;
            lblEmployeeID.setText("Faculty ID:");
            txtEmployeeID.setText(orDash(f.getFacultyId()));
            setDepartmentRowVisible(true);
            txtDepartment.setText(orDash(f.getDepartment()));
            setJobTitleRowVisible(true);
            lblJobTitle.setText("Title:");
            txtJobTitle.setText(orDash(f.getTitle()));
        } else if (profile instanceof EmployeeProfile) {
            EmployeeProfile e = (EmployeeProfile) profile;
            lblEmployeeID.setText("Employee ID:");
            txtEmployeeID.setText(orDash(e.getEmployeeId()));
            setDepartmentRowVisible(true);
            txtDepartment.setText(orDash(e.getDepartment()));
            setJobTitleRowVisible(true);
            lblJobTitle.setText("Job Title:");
            txtJobTitle.setText(orDash(e.getJobTitle()));
            setHireDateRowVisible(true);
            txtHireDate.setText(e.getHireDate() == null ? "-" : e.getHireDate().toString());
        }
    }

    /**
     * Shows or hides the Department label and field.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void setDepartmentRowVisible(boolean visible) {
        lblDepartment.setVisible(visible);
        txtDepartment.setVisible(visible);
    }

    /**
     * Shows or hides the Job Title label and field.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void setJobTitleRowVisible(boolean visible) {
        lblJobTitle.setVisible(visible);
        txtJobTitle.setVisible(visible);
    }

    /**
     * Shows or hides the Hire Date label (jLabel1) and field.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void setHireDateRowVisible(boolean visible) {
        jLabel1.setVisible(visible);   // jLabel1 = "Hire Date"
        txtHireDate.setVisible(visible);
    }

     private boolean editing = false;   // Christopher Ogden NUID: 003959375 Admin use case

    /**
     * Only an administrator can edit this page (the student and faculty pages stay view
     * only), using the same check as every other admin screen.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private boolean canEdit() {
        return business != null && business.isAdministrator(account);
    }

    /**
     * Shows Edit Profile (or Save while editing) and Cancel only when the user may edit.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void updateButtons() {
        btnEdit.setVisible(canEdit());
        btnEdit.setText(editing ? "Save" : "Edit Profile");
        btnCancel.setVisible(canEdit() && editing);
        btnChangePassword.setVisible(canEdit());   // Christopher Ogden NUID: 003959375 Admin use case
        btnChangePassword.setEnabled(!editing);    // save or cancel the profile edits first
    }

    /**
     * Turns edit mode on (name, email, phone and address unlock and show their real values,
     * not "-") or off (fields lock and show the saved details again).
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void setEditing(boolean on) {
        editing = on;
        txtName.setEditable(on);
        txtEmail.setEditable(on);
        txtPhone.setEditable(on);
        txtAddress.setEditable(on);
        if (on) {
            Person person = account.getAssociatedPersonProfile().getPerson();
            txtName.setText(orEmpty(person.getName()));
            txtEmail.setText(orEmpty(person.getEmail()));
            txtPhone.setText(orEmpty(person.getPhone()));
            txtAddress.setText(orEmpty(person.getAddress()));
            txtName.requestFocusInWindow();
        } else {
            populateProfile();
        }
        updateButtons();
    }

    /**
     * Checks the edited details with the same rules as the Manage screens: a name is
     * required, the email must be valid and not used by another person, and the phone must be
     * valid. Returns the first problem as a message, or null.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private String validateProfile() {
        if (Validation.isBlank(txtName.getText())) {
            return "Please enter your name.";
        }
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {   // same rule as Manage Personnel: staff always have an email address
            return "Please enter an email address.";
        }
        if (!Validation.isValidEmail(email)) {
            return "Please enter a valid email address (for example name@northeastern.edu).";
        }
        if (business.isEmailTaken(email, account.getAssociatedPersonProfile().getPerson())) {
            return "Another person is already registered with this email address.";
        }
        if (!Validation.isValidPhone(txtPhone.getText())) {
            return Validation.PHONE_RULE;
        }
        return null;
    }
    /**
     * Checks the password change and makes it: the current password must be right, the new
     * one must follow the password rule, match its confirmation and differ from the current
     * one. Returns the first problem as a message, or null once the password is changed.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private String changePassword(String current, String newPassword, String confirm) {
        if (!account.IsValidUser(account.getUserLoginName(), current)) {
            return "The current password is not correct.";
        }
        if (!Validation.isValidPassword(newPassword)) {
            return "The new password must be at least " + Validation.MIN_PASSWORD_LENGTH + " characters.";
        }
        if (!newPassword.equals(confirm)) {
            return "The new password and the confirmation do not match.";
        }
        if (newPassword.equals(current)) {
            return "The new password must be different from the current one.";
        }
        account.setPassword(newPassword);   // also updates the account's last-updated time
        return null;
    }
    /**
     * One line with the editable fields, used to tell whether Save changed anything.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private String snapshot(Person p) {
        return p.getName() + "|" + p.getEmail() + "|" + p.getPhone() + "|" + p.getAddress();
    }

    /**
     * Returns "" for null so a field never shows the word "null".
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private String orEmpty(String s) {
        return s == null ? "" : s;
    }
    /**
     * Returns "-" for blank values so empty fields don't look broken.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private String orDash(String s) {
        return (s == null || s.trim().isEmpty()) ? "-" : s;
    }

    /**
     * Formats a time as yyyy-MM-dd HH:mm:ss, or "Never" when null.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private String formatTime(LocalDateTime t) {
        if (t == null) {
            return "Never";
        }
        return t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtName = new javax.swing.JTextField();
        txtRole = new javax.swing.JTextField();
        txtEmployeeID = new javax.swing.JTextField();
        txtJobTitle = new javax.swing.JTextField();
        txtDepartment = new javax.swing.JTextField();
        txtHireDate = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtAccountCreated = new javax.swing.JTextField();
        txtLastLogin = new javax.swing.JTextField();
        txtAccountLastUpdated = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblEmployeeID = new javax.swing.JLabel();
        lblDepartment = new javax.swing.JLabel();
        lblJobTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblAccountCreated = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnChangePassword = new javax.swing.JButton();

        txtName.setEditable(false);

        txtRole.setEditable(false);

        txtEmployeeID.setEditable(false);

        txtJobTitle.setEditable(false);

        txtDepartment.setEditable(false);

        txtHireDate.setEditable(false);

        txtEmail.setEditable(false);

        txtPhone.setEditable(false);

        txtAddress.setEditable(false);

        txtUserName.setEditable(false);

        txtAccountCreated.setEditable(false);

        txtLastLogin.setEditable(false);

        txtAccountLastUpdated.setEditable(false);

        lblName.setText("Name");

        lblRole.setText("Role");

        lblTitle.setText("My Profile");

        lblEmployeeID.setText("Employee ID");

        lblDepartment.setText("Department");

        lblJobTitle.setText("Job Title");

        jLabel1.setText("Hire Date");

        lblEmail.setText("Email");

        lblPhone.setText("Phone");

        lblAddress.setText("Address");

        lblUserName.setText("User Name");

        lblAccountCreated.setText("Account Created");

        jLabel2.setText("Last Login");

        jLabel3.setText("Account Last Updated");

        btnBack.setText("<<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit Profile");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnChangePassword.setText("Change Password");
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(btnBack)
                .addGap(235, 235, 235)
                .addComponent(lblTitle)
                .addContainerGap(374, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEdit)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblName)
                        .addComponent(lblRole)
                        .addComponent(lblEmployeeID)
                        .addComponent(lblDepartment)
                        .addComponent(lblJobTitle)
                        .addComponent(jLabel1)
                        .addComponent(lblEmail)
                        .addComponent(lblPhone)
                        .addComponent(lblAddress)
                        .addComponent(lblUserName)
                        .addComponent(lblAccountCreated)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtName)
                        .addComponent(txtRole)
                        .addComponent(txtEmployeeID)
                        .addComponent(txtJobTitle)
                        .addComponent(txtDepartment)
                        .addComponent(txtHireDate, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                        .addComponent(txtEmail)
                        .addComponent(txtPhone)
                        .addComponent(txtAddress)
                        .addComponent(txtUserName, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtAccountCreated)
                        .addComponent(txtLastLogin)
                        .addComponent(txtAccountLastUpdated))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChangePassword)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnBack))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRole))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmployeeID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmployeeID))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDepartment))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJobTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblJobTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHireDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPhone))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAddress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUserName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAccountCreated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAccountCreated))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLastLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAccountLastUpdated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnCancel)
                    .addComponent(btnChangePassword))
                .addContainerGap(33, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Removes this page and returns to the user's work area.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        if (editing) {
            int answer = JOptionPane.showConfirmDialog(this,
                "You have unsaved changes to your profile. Leave without saving them?",
                "Unsaved changes", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) {
                return;   // stay on My Profile; Save or Cancel the edits
            }
        }
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed
    
     /**
     * Edit Profile unlocks the name, email, phone and address. Pressed again (now labelled
     * Save) it checks the values, saves them to the person record and locks the fields again.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        if (!canEdit()) {
            return;
        }
        if (!editing) {
            setEditing(true);
            return;
        }
        String problem = validateProfile();
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Please check your details", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Person person = account.getAssociatedPersonProfile().getPerson();
        String before = snapshot(person);
        person.setName(txtName.getText().trim());
        person.setEmail(txtEmail.getText().trim());
        person.setPhone(txtPhone.getText().trim());
        person.setAddress(txtAddress.getText().trim());
        if (before.equals(snapshot(person))) {
            setEditing(false);
            JOptionPane.showMessageDialog(this, "Nothing was changed.");
            return;
        }
        account.touch();   // updates "Account Last Updated"
        setEditing(false);
        JOptionPane.showMessageDialog(this, "Your profile was saved.");

    }//GEN-LAST:event_btnEditActionPerformed
      /**
     * Throws away the unsaved edits, shows the saved details again and locks the fields.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setEditing(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    /**
     * Asks for the current password and the new one twice, then changes the password.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePasswordActionPerformed
        // TODO add your handling code here:
        if (!canEdit()) {
            return;
        }
        JPasswordField current = new JPasswordField();
        JPasswordField newPassword = new JPasswordField();
        JPasswordField confirm = new JPasswordField();
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 4));
        form.add(new JLabel("Current password"));
        form.add(current);
        form.add(new JLabel("New password (at least " + Validation.MIN_PASSWORD_LENGTH + " characters)"));
        form.add(newPassword);
        form.add(new JLabel("Confirm new password"));
        form.add(confirm);
        int answer = JOptionPane.showConfirmDialog(this, form, "Change Password",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (answer != JOptionPane.OK_OPTION) {
            return;
        }
        String problem = changePassword(new String(current.getPassword()),
            new String(newPassword.getPassword()), new String(confirm.getPassword()));
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Password not changed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        populateProfile();   // shows the new "Account Last Updated" time
        JOptionPane.showMessageDialog(this, "Your password was changed.");
    }//GEN-LAST:event_btnChangePasswordActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnChangePassword;
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblAccountCreated;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmployeeID;
    private javax.swing.JLabel lblJobTitle;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JTextField txtAccountCreated;
    private javax.swing.JTextField txtAccountLastUpdated;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmployeeID;
    private javax.swing.JTextField txtHireDate;
    private javax.swing.JTextField txtJobTitle;
    private javax.swing.JTextField txtLastLogin;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtRole;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
