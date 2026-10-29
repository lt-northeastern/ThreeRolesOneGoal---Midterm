/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.FacultyRole;

import Business.Business;
import Business.Person.Person;
import Business.Profiles.FacultyProfile;
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

/**
 *
 * @author ogdenc
 */
public class ManageFacultyJPanel extends javax.swing.JPanel {
    
    private Business business;
    private UserAccount currentUser;
    private JPanel userProcessContainer;

    /**
     * Creates new form ManageFacultyJPanel
     */
    public ManageFacultyJPanel(Business business, UserAccount currentUser, JPanel userProcessContainer) {
        initComponents();
        this.business = business;
        this.currentUser = currentUser;
        this.userProcessContainer = userProcessContainer;

        txtEmployeeId.setEditable(false);   // the Faculty ID is generated automatically
        tblFacultyData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblFacultyData.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });
        refreshTable();
        setAddMode(true); 
        }
    private FacultyProfile selectedFaculty() {
        int row = tblFacultyData.getSelectedRow();
        if (row < 0) {
            return null;
        }
        String facultyId = (String) tblFacultyData.getValueAt(row, 1);
        for (FacultyProfile faculty : business.getFacultyDirectory().getFacultyList()) {
            if (faculty.getFacultyId().equals(facultyId)) {
                return faculty;
            }
        }
        return null;
    }
    
    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) tblFacultyData.getModel();
        model.setRowCount(0);
        UserAccountDirectory uad = business.getUserAccountDirectory();
        for (FacultyProfile faculty : business.getFacultyDirectory().getFacultyList()) {
            UserAccount account = uad.findUserAccount(faculty.getPerson().getPersonId());
            model.addRow(new Object[]{
                faculty.getPerson().getName(),
                faculty.getFacultyId(),
                faculty.getDepartment(),
                faculty.getTitle(),
                account == null ? "" : account.getUserLoginName(),
                account == null ? "" : formatTime(account.getLastLoginTime()),
                account == null ? "" : formatTime(account.getLastUpdatedTime())
            });
        }
    }
      private void populateFormFromSelection() {
        FacultyProfile faculty = selectedFaculty();
        if (faculty == null) {
            clearForm();   // nothing selected -> empty form, Add enabled
            return;
        }
        Person person = faculty.getPerson();
        txtName.setText(person.getName());
        txtEmail.setText(person.getEmail());
        txtPhone.setText(person.getPhone());
        txtAddress.setText(person.getAddress());
        txtEmployeeId.setText(faculty.getFacultyId());
        txtDepartment.setText(faculty.getDepartment());
        txtJobTitle.setText(faculty.getTitle());
        UserAccount account = business.getUserAccountDirectory().findUserAccount(person.getPersonId());
        txtUserName.setText(account == null ? "" : account.getUserLoginName());
        pwEntry.setText("");   // never show the password
        setAddMode(false);     // add this line: a row is selected -> Add disabled
    }
     private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtEmployeeId.setText("");
        txtDepartment.setText("");
        txtJobTitle.setText("");
        txtUserName.setText("");
        pwEntry.setText("");
        tblFacultyData.clearSelection();
    }  
    private void setAddMode(boolean addMode) {
        btnAdd.setEnabled(addMode);
    }
    private String formatTime(LocalDateTime t) {
        return t == null ? "Never" : t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
      private String validateForm(FacultyProfile editing) {
        if (Validation.isBlank(txtName.getText())) {
            return "Please enter the faculty member's name.";
        }
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !Validation.isValidEmail(email)) {
            return "Please enter a valid email address (for example name@northeastern.edu).";
        }
        if (!Validation.isValidUsername(txtUserName.getText())) {
            return "The user name must be at least 3 characters and cannot contain spaces.";
        }
        UserAccount existing = editing == null ? null
            : business.getUserAccountDirectory().findUserAccount(editing.getPerson().getPersonId());
        if (business.getUserAccountDirectory().isUsernameTaken(txtUserName.getText(), existing)) {
            return "That user name is already taken. Please choose another one.";
        }
        String password = new String(pwEntry.getPassword());
        boolean passwordRequired = editing == null || existing == null;
        if (passwordRequired && !Validation.isValidPassword(password)) {
            return "The password must be at least 6 characters.";
        }
        if (!password.isEmpty() && !Validation.isValidPassword(password)) {
            return "The new password must be at least 6 characters (leave it empty to keep the old one).";
        }
        return null;
    }

    private void applyForm(FacultyProfile faculty) {
        Person person = faculty.getPerson();
        person.setName(txtName.getText().trim());
        person.setEmail(txtEmail.getText().trim());
        person.setPhone(txtPhone.getText().trim());
        person.setAddress(txtAddress.getText().trim());
        faculty.setDepartment(txtDepartment.getText());
        faculty.setTitle(txtJobTitle.getText());
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblFacultyData = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtEmployeeId = new javax.swing.JTextField();
        txtJobTitle = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtDepartment = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblEmployeeID = new javax.swing.JLabel();
        lblJobTitle = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblDepartment = new javax.swing.JLabel();
        pwEntry = new javax.swing.JPasswordField();
        lblTitle = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();

        tblFacultyData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Faculty ID", "Department", "Title", "User Name", "Last Login", "Account Updated"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblFacultyData);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        lblName.setText("Name");

        lblPhone.setText("Phone");

        lblUserName.setText("User Name");

        lblEmployeeID.setText("Faculty ID");

        lblJobTitle.setText("Job Title");

        lblEmail.setText("Email");

        lblAddress.setText("Address");

        lblPassword.setText("Password");

        lblDepartment.setText("Department");

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        lblTitle.setText("Manage Faculty");

        btnClear.setText("Clear Form");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
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
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(lblJobTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(70, 70, 70)
                                .addComponent(txtJobTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblUserName)
                                    .addComponent(lblEmployeeID))
                                .addGap(65, 65, 65)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPassword)
                                    .addComponent(lblDepartment))
                                .addGap(72, 72, 72)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtDepartment, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(pwEntry)))))
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
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDepartment)
                        .addComponent(txtEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJobTitle)
                    .addComponent(txtJobTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBack)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDelete)
                        .addComponent(btnClear)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        FacultyProfile faculty = selectedFaculty();
        if (faculty == null) {
            JOptionPane.showMessageDialog(this, "Please select a row in the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentUser != null && faculty == currentUser.getAssociatedPersonProfile()) {
            JOptionPane.showMessageDialog(this, "You cannot delete the account you are logged in with.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
            "Delete " + faculty.getPerson().getName() + " and the login account? This cannot be undone.",
            "Please confirm", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        business.deleteProfile(faculty);
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "Faculty member deleted.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        String problem = validateForm(null);
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Person person = business.getPersonDirectory().newPerson(txtName.getText().trim());
        FacultyProfile faculty = business.getFacultyDirectory().newFacultyProfile(person);
        applyForm(faculty);
        business.getUserAccountDirectory().newUserAccount(faculty, txtUserName.getText().trim(),
            new String(pwEntry.getPassword()));

        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "Faculty member added successfully.", "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        FacultyProfile faculty = selectedFaculty();
        if (faculty == null) {
            JOptionPane.showMessageDialog(this, "Please select a row in the table first.", "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String problem = validateForm(faculty);
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        applyForm(faculty);

        UserAccountDirectory uad = business.getUserAccountDirectory();
        UserAccount account = uad.findUserAccount(faculty.getPerson().getPersonId());
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
            uad.newUserAccount(faculty, username, password);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmployeeID;
    private javax.swing.JLabel lblJobTitle;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JPasswordField pwEntry;
    private javax.swing.JTable tblFacultyData;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmployeeId;
    private javax.swing.JTextField txtJobTitle;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
