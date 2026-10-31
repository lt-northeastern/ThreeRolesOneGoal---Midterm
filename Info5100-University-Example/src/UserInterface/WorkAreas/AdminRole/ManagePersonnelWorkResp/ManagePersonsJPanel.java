/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface.WorkAreas.AdminRole.ManagePersonnelWorkResp;

import Business.Business;
import Business.Person.*;
import Business.Profiles.*;
import Business.UserAccounts.*;
import java.awt.CardLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import Business.Validation;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author kal bugrara
 */
public class ManagePersonsJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ManageSuppliersJPanel
     */
    private final Business business;
    private final UserAccount currentUser;
    private final JPanel userProcessContainer;
    private final List<EmployeeProfile> rows = new ArrayList<>();


    /**
     * Builds the employee (HR) screen: Employee ID is read-only, single-row selection, a
     * selected row loads into the form, then the table is filled and the screen starts in Add
     * mode.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
 public ManagePersonsJPanel(Business business, UserAccount currentUser, JPanel userProcessContainer)  {
        initComponents();
        this.business = business;
        this.currentUser = currentUser;
        this.userProcessContainer = userProcessContainer;
        
        txtEmployeeId.setEditable(false);
        tblEmployeeData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblEmployeeData.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedIntoForm();
            }
        });
        refreshTable();
        setAddMode(true);

    }
    /**
     * Reloads the employee table: name, employee ID, department, job title, username, last
     * login and last updated.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) tblEmployeeData.getModel();
        model.setRowCount(0);
        rows.clear();
        for (EmployeeProfile e : business.getEmployeeDirectory().getEmployeeList()) {
            rows.add(e);
            Person person = e.getPerson();
            UserAccount ua = business.getUserAccountDirectory().findUserAccount(person.getPersonId());
            Object[] row = new Object[7];
            row[0] = person.getName();
            row[1] = e.getEmployeeId();
            row[2] = e.getDepartment();
            row[3] = e.getJobTitle();
            row[4] = ua == null ? "(no account)" : ua.getUserLoginName();
            row[5] = ua == null ? "-" : formatTime(ua.getLastLoginTime());
            row[6] = ua == null ? "-" : formatTime(ua.getLastUpdatedTime());
            model.addRow(row);
        }
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
     * Returns the employee for the selected row, or null.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
     private EmployeeProfile selectedEmployee() {
        int r = tblEmployeeData.getSelectedRow();
        return (r < 0 || r >= rows.size()) ? null : rows.get(r);
    }
    /**
     * Copies the selected employee's person, job and login details into the form (the password
     * is left blank) and switches to edit mode.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
        private void loadSelectedIntoForm() {
        EmployeeProfile e = selectedEmployee();
        if (e == null) {
            clearForm();   // nothing selected -> empty form, Add enabled
            return;
        }
        Person person = e.getPerson();
        UserAccount ua = business.getUserAccountDirectory().findUserAccount(person.getPersonId());
        txtName.setText(person.getName());
        txtEmail.setText(person.getEmail());
        txtPhone.setText(person.getPhone());
        txtAddress.setText(person.getAddress());
        txtUserName.setText(ua == null ? "" : ua.getUserLoginName());
        pwEntry.setText("");
        txtEmployeeId.setText(e.getEmployeeId());
        txtDepartment.setText(e.getDepartment());
        txtJobTitle.setText(e.getJobTitle());
        txtHireDate.setText(e.getHireDate() == null ? "" : e.getHireDate().toString());
        setAddMode(false);
    }
    /**
     * Clears the table selection and every field, then re-enables Add.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
            private void clearForm() {
        tblEmployeeData.clearSelection();
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtUserName.setText("");
        pwEntry.setText("");
        txtEmployeeId.setText("");
        txtDepartment.setText("");
        txtJobTitle.setText("");
        txtHireDate.setText("");
        setAddMode(true); 
    }
    /**
     * Enables the Add button only when no row is selected.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
         private void setAddMode(boolean addMode) {
        btnAdd.setEnabled(addMode);
    }
    /**
     * Checks the form and returns the first problem as a message, or null when everything is
     * valid: name, email format, phone characters, department, job title, hire date, username
     * rules, a password for new logins, and a unique username.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private String validateForm(EmployeeProfile existing) {
        UserAccountDirectory uad = business.getUserAccountDirectory();
        UserAccount account = existing == null ? null : uad.findUserAccount(existing.getPerson().getPersonId());

        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String username = txtUserName.getText().trim();
        String password = new String(pwEntry.getPassword());

        if (name.isEmpty()) {
            return "Please enter the name.";
        }
        if (email.isEmpty()) {
            return "Please enter an email address.";
        }
        if (!Validation.isValidEmail(email)) {
            return "The email address is not valid (example: name@northeastern.edu).";
        }
        if (!Validation.isValidPhone(phone)) {   // Christopher Ogden NUID: 003959375 Admin use case: shared rule
            return Validation.PHONE_RULE;
        }
        if (txtDepartment.getText().trim().isEmpty()) {
            return "Please enter the department.";
        }
        if (txtJobTitle.getText().trim().isEmpty()) {
            return "Please enter the job title.";
        }
        if (Validation.parseDate(txtHireDate.getText()) == null) {
            return "The hire date must be a valid date in the format yyyy-mm-dd (example: 2026-09-01).";
        }

        // login credentials: an employee always needs a login
        if (!Validation.isValidUsername(username)) {
            return "The user name must be at least " + Validation.MIN_USERNAME_LENGTH
                    + " characters long and cannot contain spaces.";
        }
        if (account == null && password.isEmpty()) {
            return "Please enter a password for the login.";
        }
        if (!password.isEmpty() && !Validation.isValidPassword(password)) {
            return "The password must be at least " + Validation.MIN_PASSWORD_LENGTH + " characters long.";
        }
        if (uad.isUsernameTaken(username, account)) {
            return "The user name \"" + username + "\" is already taken. Please choose another one.";
        }
        return null;
    }
    /**
     * Copies the validated form values into the employee and its person record.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void applyForm(EmployeeProfile e) {
        Person person = e.getPerson();
        person.setName(txtName.getText());
        person.setEmail(txtEmail.getText());
        person.setPhone(txtPhone.getText());
        person.setAddress(txtAddress.getText());
        e.setDepartment(txtDepartment.getText());
        e.setJobTitle(txtJobTitle.getText());
        e.setHireDate(Validation.parseDate(txtHireDate.getText()));
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBack = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmployeeData = new javax.swing.JTable();
        txtName = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtEmployeeId = new javax.swing.JTextField();
        txtJobTitle = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtDepartment = new javax.swing.JTextField();
        txtHireDate = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblEmployeeID = new javax.swing.JLabel();
        lblJobTitle = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblDepartment = new javax.swing.JLabel();
        lblHireDate = new javax.swing.JLabel();
        pwEntry = new javax.swing.JPasswordField();
        btnClear = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 204));
        setLayout(null);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack);
        btnBack.setBounds(30, 540, 80, 23);

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        add(btnDelete);
        btnDelete.setBounds(310, 540, 80, 23);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel2.setText("Manage Personnel (HR)");
        add(jLabel2);
        jLabel2.setBounds(21, 20, 550, 28);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setLayout(null);

        jLabel3.setText("Name");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 60, 190, 17);

        jLabel4.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel4.setText("Manage Personnel (HR)");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(21, 20, 550, 28);

        add(jPanel1);
        jPanel1.setBounds(0, 0, 0, 0);

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        add(btnAdd);
        btnAdd.setBounds(120, 540, 80, 23);

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        add(btnUpdate);
        btnUpdate.setBounds(220, 540, 80, 23);

        tblEmployeeData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Employee ID", "Department", "Job Title", "User Name", "Last Login", "Account Updated"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblEmployeeData);

        add(jScrollPane1);
        jScrollPane1.setBounds(30, 80, 730, 220);

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        add(txtName);
        txtName.setBounds(200, 330, 180, 23);
        add(txtPhone);
        txtPhone.setBounds(200, 370, 180, 23);
        add(txtUserName);
        txtUserName.setBounds(200, 410, 180, 23);
        add(txtEmployeeId);
        txtEmployeeId.setBounds(200, 450, 180, 23);
        add(txtJobTitle);
        txtJobTitle.setBounds(200, 490, 180, 23);

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        add(txtEmail);
        txtEmail.setBounds(560, 330, 180, 23);
        add(txtAddress);
        txtAddress.setBounds(560, 370, 180, 23);
        add(txtDepartment);
        txtDepartment.setBounds(560, 450, 180, 23);
        add(txtHireDate);
        txtHireDate.setBounds(560, 490, 180, 23);

        lblName.setText("Name");
        add(lblName);
        lblName.setBounds(70, 330, 34, 17);

        lblPhone.setText("Phone");
        add(lblPhone);
        lblPhone.setBounds(70, 370, 36, 17);

        lblUserName.setText("User Name");
        add(lblUserName);
        lblUserName.setBounds(70, 410, 65, 17);

        lblEmployeeID.setText("Employee ID");
        add(lblEmployeeID);
        lblEmployeeID.setBounds(70, 450, 74, 17);

        lblJobTitle.setText("Job Title");
        add(lblJobTitle);
        lblJobTitle.setBounds(70, 490, 60, 17);

        lblEmail.setText("Email");
        add(lblEmail);
        lblEmail.setBounds(420, 330, 32, 17);

        lblAddress.setText("Address");
        add(lblAddress);
        lblAddress.setBounds(420, 370, 49, 17);

        lblPassword.setText("Password");
        add(lblPassword);
        lblPassword.setBounds(420, 410, 58, 17);

        lblDepartment.setText("Department");
        add(lblDepartment);
        lblDepartment.setBounds(420, 450, 68, 17);

        lblHireDate.setText("Hire Date (yyyy-mm-dd)");
        add(lblHireDate);
        lblHireDate.setBounds(420, 490, 140, 17);
        add(pwEntry);
        pwEntry.setBounds(560, 410, 180, 23);

        btnClear.setText("Clear Form");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        add(btnClear);
        btnClear.setBounds(400, 540, 100, 23);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Removes this screen and returns to the admin work area.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        userProcessContainer.remove(this);
        ((java.awt.CardLayout) userProcessContainer.getLayout()).next(userProcessContainer);
 //       ((java.awt.CardLayout)CardSequencePanel.getLayout()).show(CardSequencePanel, "IdentifyEventTypes");

    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * Deletes the selected employee, their person record and login after confirmation. You
     * cannot delete the account you are logged in with.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        
        EmployeeProfile employee = selectedEmployee();
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Please select a row in the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentUser != null && employee == currentUser.getAssociatedPersonProfile()) {
            JOptionPane.showMessageDialog(this, "You cannot delete the account you are logged in with.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Delete " + employee.getPerson().getName() + " and the login account? This cannot be undone.",
                "Please confirm", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        business.deleteProfile(employee);
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "Employee deleted.", "Information", JOptionPane.INFORMATION_MESSAGE);
    
    

    }//GEN-LAST:event_btnDeleteActionPerformed



    /**
     * Validates the form, then creates the person, employee profile and login, and confirms
     * with a message.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        String problem = validateForm(null);
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Person person = business.getPersonDirectory().newPerson(txtName.getText().trim());
        EmployeeProfile employee = business.getEmployeeDirectory().newEmployeeProfile(person);
        applyForm(employee);
        business.getUserAccountDirectory().newUserAccount(employee, txtUserName.getText().trim(),
                new String(pwEntry.getPassword()));

        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "Employee added successfully.", "Information",
                JOptionPane.INFORMATION_MESSAGE);
    

    }//GEN-LAST:event_btnAddActionPerformed

    /**
     * Validates and saves edits to the selected employee. Updates the username and/or password
     * when changed (or creates a login if there is none) and refreshes the last-updated time.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        EmployeeProfile employee = selectedEmployee();
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Please select a row in the table first.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String problem = validateForm(employee);
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        applyForm(employee);

        UserAccountDirectory uad = business.getUserAccountDirectory();
        UserAccount account = uad.findUserAccount(employee.getPerson().getPersonId());
        String username = txtUserName.getText().trim();
        String password = new String(pwEntry.getPassword());
        if (account != null) {
            if (!account.getUserLoginName().equals(username)) {
                account.setUserLoginName(username);
            }
            if (!password.isEmpty()) {
                account.setPassword(password);
            }
            account.touch();
        } else {
            uad.newUserAccount(employee, username, password);
        }

        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "The changes were saved.", "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnUpdateActionPerformed


    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed


    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    /**
     * Clears the form and returns to Add mode.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmployeeID;
    private javax.swing.JLabel lblHireDate;
    private javax.swing.JLabel lblJobTitle;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JPasswordField pwEntry;
    private javax.swing.JTable tblEmployeeData;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmployeeId;
    private javax.swing.JTextField txtHireDate;
    private javax.swing.JTextField txtJobTitle;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables

}
