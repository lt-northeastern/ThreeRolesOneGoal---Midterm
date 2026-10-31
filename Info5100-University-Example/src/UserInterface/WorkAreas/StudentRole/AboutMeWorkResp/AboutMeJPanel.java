package UserInterface.WorkAreas.StudentRole.AboutMeWorkResp;

import Business.Business;
import Business.Profiles.StudentProfile;
import java.awt.CardLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Hobbies and interests the student wants their professors to see. They are kept on the
 * student's profile and shown read-only on the faculty Manage Student Profiles screen.
 *
 * Student use case - written by Hemanth Bezawada (NUID 002059527).
 */
public class AboutMeJPanel extends javax.swing.JPanel {

    static final int MAX_LENGTH = 120;

    JPanel CardSequencePanel;
    Business business;
    StudentProfile student;

    public AboutMeJPanel(Business b, StudentProfile sp, JPanel clp) {
        business = b;
        student = sp;
        CardSequencePanel = clp;
        initComponents();
        lblStudent.setText("Student: " + student.getPerson().getName() + " (NUID " + student.getNuid() + ")");
        showSaved();
    }

    // the boxes always start from what is on the profile
    private void showSaved() {
        txtHobbies.setText(student.getHobbies());
        txtInterests.setText(student.getInterests());
    }

    // plain text, not too long; an empty box is allowed and clears the value
    private String check(String what, String value) {
        if (value.length() > MAX_LENGTH) {
            return what + " can be at most " + MAX_LENGTH + " characters (you typed " + value.length() + ").";
        }
        for (char ch : value.toCharArray()) {
            if (Character.isISOControl(ch)) {
                return what + " contains characters that cannot be shown. Please use plain text.";
            }
        }
        if (!value.isEmpty() && !value.matches(".*[A-Za-z0-9].*")) {
            return what + " should contain some letters or digits, not only punctuation.";
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblStudent = new javax.swing.JLabel();
        lblIntro = new javax.swing.JLabel();
        lblHobbies = new javax.swing.JLabel();
        txtHobbies = new javax.swing.JTextField();
        lblInterests = new javax.swing.JLabel();
        txtInterests = new javax.swing.JTextField();
        lblHint = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnUndo = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        lblTitle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblTitle.setText("About Me");
        add(lblTitle);
        lblTitle.setBounds(21, 14, 400, 32);

        lblStudent.setText("Student:");
        add(lblStudent);
        lblStudent.setBounds(30, 52, 850, 20);

        lblIntro.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblIntro.setText("Tell your professors a little about yourself. They see this on your student profile.");
        add(lblIntro);
        lblIntro.setBounds(30, 88, 850, 22);

        lblHobbies.setText("Hobbies");
        add(lblHobbies);
        lblHobbies.setBounds(30, 134, 140, 20);

        add(txtHobbies);
        txtHobbies.setBounds(180, 131, 600, 26);

        lblInterests.setText("Interests");
        add(lblInterests);
        lblInterests.setBounds(30, 174, 140, 20);

        add(txtInterests);
        txtInterests.setBounds(180, 171, 600, 26);

        lblHint.setText("Up to 120 characters each. Leave a box empty to clear it.");
        add(lblHint);
        lblHint.setBounds(180, 204, 600, 20);

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        add(btnSave);
        btnSave.setBounds(180, 248, 120, 30);

        btnUndo.setText("Undo Changes");
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });
        add(btnUndo);
        btnUndo.setBounds(320, 248, 150, 30);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack);
        btnBack.setBounds(780, 434, 100, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String hobbies = txtHobbies.getText().trim();
        String interests = txtInterests.getText().trim();
        String problem = check("Hobbies", hobbies);
        if (problem == null) {
            problem = check("Interests", interests);
        }
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "About Me", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (hobbies.equals(student.getHobbies()) && interests.equals(student.getInterests())) {
            JOptionPane.showMessageDialog(this, "Nothing was changed.", "About Me", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        student.setHobbies(hobbies);
        student.setInterests(interests);
        showSaved();
        JOptionPane.showMessageDialog(this, "Your hobbies and interests were saved. Your professors can see them now.", "About Me", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
        showSaved();
    }//GEN-LAST:event_btnUndoActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        CardSequencePanel.remove(this);
        ((CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUndo;
    private javax.swing.JLabel lblHint;
    private javax.swing.JLabel lblHobbies;
    private javax.swing.JLabel lblInterests;
    private javax.swing.JLabel lblIntro;
    private javax.swing.JLabel lblStudent;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtHobbies;
    private javax.swing.JTextField txtInterests;
    // End of variables declaration//GEN-END:variables
}
