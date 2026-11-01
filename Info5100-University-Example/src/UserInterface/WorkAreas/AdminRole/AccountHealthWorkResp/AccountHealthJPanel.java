/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 * modified by Christopher Ogden NUID: 003959375 Admin use case.
 */
/*
 * Account Health screen for the administrator.
 * Created by Christopher Ogden NUID: 003959375 Admin use case.
 */
package UserInterface.WorkAreas.AdminRole.AccountHealthWorkResp;

import Business.Business;
import Business.Person.Person;
import Business.Profiles.Profile;
import Business.Profiles.StudentProfile;
import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;
import Business.Validation;
import java.awt.CardLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 * Lists accounts and people that need an administrator's attention: disabled accounts,
 * logins that were never used, people without a login and people without an email address.
 *
 * Christopher Ogden NUID: 003959375 Admin use case.
 */
public class AccountHealthJPanel extends javax.swing.JPanel {

    static final String ALL = "All issues";
    static final String DISABLED = "Disabled account";
    static final String NEVER_LOGGED_IN = "Never logged in";
    static final String NO_LOGIN = "No login account";
    static final String NO_EMAIL = "No email address";

    private final Business business;
    private final JPanel userProcessContainer;
    private boolean loading = true;   // ignore the dropdown event while it is filled
    private int disabled, neverLoggedIn, noLogin, noEmail;   // issue counts for the summary line

    /**
     * Builds the Account Health screen: fills the Show dropdown and lists every issue.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    public AccountHealthJPanel(Business business, UserAccount currentUser, JPanel userProcessContainer) {
        initComponents();
        this.business = business;
        this.userProcessContainer = userProcessContainer;
        cmbShow.removeAllItems();
        cmbShow.addItem(ALL);
        cmbShow.addItem(DISABLED);
        cmbShow.addItem(NEVER_LOGGED_IN);
        cmbShow.addItem(NO_LOGIN);
        cmbShow.addItem(NO_EMAIL);
        loading = false;
        refreshTable();
    }

    /**
     * Rebuilds the table with the issues chosen in the Show dropdown and updates the summary
     * line, which always counts every issue.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void refreshTable() {
        if (loading) {
            return;
        }
         
        DefaultTableModel model = (DefaultTableModel) tblHealth.getModel();
        model.setRowCount(0);  
        
        tblHealth.setModel(model);
        disabled = neverLoggedIn = noLogin = noEmail = 0;
        String show = String.valueOf(cmbShow.getSelectedItem());
        UserAccountDirectory uad = business.getUserAccountDirectory();

        // logins: disabled, or created but never used
        for (UserAccount ua : uad.getUserAccountList()) {
            Profile profile = ua.getAssociatedPersonProfile();
            if (!ua.isActive()) {
                disabled++;
                addRow(model, show, DISABLED, profile, ua.getUserLoginName(), "Login is refused until an administrator enables it");
            }
            if (ua.getLastLoginTime() == null) {
                neverLoggedIn++;
                addRow(model, show, NEVER_LOGGED_IN, profile, ua.getUserLoginName(), "Created " + formatTime(ua.getCreatedTime()) + ", not used since");
            }
        }

        // people: no login account, or no email address
        for (Profile profile : everyone()) {
            Person person = profile.getPerson();
            UserAccount ua = uad.findUserAccount(person.getPersonId());
            if (ua == null) {
                noLogin++;
                String details = profile instanceof StudentProfile
                    ? "Pre-registered; can sign up with NU ID " + ((StudentProfile) profile).getNuid()
                    : "Create a login on Administer User Accounts";
                addRow(model, show, NO_LOGIN, profile, "-", details);
            }
            
            if (Validation.isBlank(person.getEmail())) {
                noEmail++;
                addRow(model, show, NO_EMAIL, profile, ua == null ? "-" : ua.getUserLoginName(), "Add an email address on the Manage screen for this role");
            }
        }

        int total = disabled + neverLoggedIn + noLogin + noEmail;
        lblSummary.setText(total + " issues: " + disabled + " disabled, " + neverLoggedIn + " never logged in, "
            + noLogin + " without a login, " + noEmail + " without an email address.");
    }

    /**
     * Adds one issue to the table when the Show dropdown is on "All issues" or on that issue.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private void addRow(DefaultTableModel model, String show, String issue, Profile profile, String username, String details) {
        if (ALL.equals(show) || issue.equals(show)) {
            model.addRow(new Object[]{profile.getPerson().getName(), profile.getRole(), username, issue, details});
        }
    }
    /**
     * Every employee, faculty and student profile.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private List<Profile> everyone() {
        List<Profile> all = new ArrayList<>();
        all.addAll(business.getEmployeeDirectory().getEmployeeList());
        all.addAll(business.getFacultyDirectory().getFacultyList());
        all.addAll(business.getStudentDirectory().getStudentList());
        return all;
    }
    /**
     * Formats a time as yyyy-MM-dd HH:mm, or "unknown" when null.
     *
     * Christopher Ogden NUID: 003959375 Admin use case.
     */
    private String formatTime(LocalDateTime t) {
        return t == null ? "unknown" : t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblShow = new javax.swing.JLabel();
        cmbShow = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHealth = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        lblSummary = new javax.swing.JLabel();

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        lblTitle.setText("Account Health");

        lblShow.setText("Show");

        cmbShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbShowActionPerformed(evt);
            }
        });

        tblHealth.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Name", "Role", "User Name", "Issue", "Details"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblHealth);
        if (tblHealth.getColumnModel().getColumnCount() > 0) {
            tblHealth.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblHealth.getColumnModel().getColumn(1).setPreferredWidth(20);
            tblHealth.getColumnModel().getColumn(2).setPreferredWidth(35);
            tblHealth.getColumnModel().getColumn(3).setPreferredWidth(75);
            tblHealth.getColumnModel().getColumn(4).setPreferredWidth(250);
        }

        btnBack.setText("<<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblSummary.setText("Summary");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblShow)
                        .addGap(18, 18, 18)
                        .addComponent(cmbShow, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBack)
                    .addComponent(lblSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 727, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitle)
                .addGap(571, 571, 571))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShow)
                    .addComponent(cmbShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(lblSummary)
                .addGap(116, 116, 116)
                .addComponent(btnBack)
                .addGap(67, 67, 67))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShowActionPerformed
        // TODO add your handling code here:
        refreshTable();
    }//GEN-LAST:event_cmbShowActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JComboBox<String> cmbShow;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblShow;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblHealth;
    // End of variables declaration//GEN-END:variables
}
