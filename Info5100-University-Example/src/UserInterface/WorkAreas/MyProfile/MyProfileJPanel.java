/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.MyProfile;

import Business.Person.*;
import Business.Profiles.*;
import Business.UserAccounts.*;
import java.awt.CardLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JPanel;

/**
 *
 * @author ogdenc
 */
public class MyProfileJPanel extends javax.swing.JPanel {
    
    private final JPanel userProcessContainer;
    private final UserAccount account;


    /**
     * Builds the My Profile page for the logged-in user, locks every field and fills it in.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public MyProfileJPanel(UserAccount account, JPanel userProcessContainer) {
        initComponents();
        this.account = account;
        this.userProcessContainer = userProcessContainer;
        makeFieldsReadOnly();
        populateProfile();
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
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
                    .addComponent(jLabel3))
                .addGap(32, 32, 32)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(btnBack)
                .addGap(235, 235, 235)
                .addComponent(lblTitle)
                .addContainerGap(374, Short.MAX_VALUE))
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
                .addContainerGap(68, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Removes this page and returns to the user's work area.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
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
