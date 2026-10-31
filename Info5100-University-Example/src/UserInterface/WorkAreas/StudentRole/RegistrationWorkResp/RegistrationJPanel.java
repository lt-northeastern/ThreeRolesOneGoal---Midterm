package UserInterface.WorkAreas.StudentRole.RegistrationWorkResp;

import Business.Business;
import Business.Profiles.StudentProfile;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Department.Department;
import java.awt.CardLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import info5100.university.example.CourseSchedule.CourseSchedule;
import java.util.ArrayList;

/**
 * Register for classes in the current semester and drop them again.
 *
 * Student use case - written by Hemanth Bezawada (NUID 002059527).
 */
public class RegistrationJPanel extends javax.swing.JPanel {

    JPanel CardSequencePanel;
    Business business;
    StudentProfile student;
    info5100.university.example.Persona.StudentProfile record;   // the academic record in the university model
    Department department;
    static final int MAX_COURSES_PER_SEMESTER = 4;
    ArrayList<CourseOffer> offerRows = new ArrayList<CourseOffer>();        // mirrors tblOffers
    ArrayList<SeatAssignment> myRows = new ArrayList<SeatAssignment>();     // mirrors tblMine

    public RegistrationJPanel(Business b, StudentProfile sp, JPanel clp) {
        business = b;
        student = sp;
        record = sp.getAcademicRecord();
        department = b.getDepartment();
        CardSequencePanel = clp;
        initComponents();
        lblStudent.setText("Student: " + student.getPerson().getName() + " (NUID " + student.getNuid() + ")");
        populateSemesters();
        refreshTables();
    }

    // the latest semester with a schedule is the one open for registration
    private String currentSemester() {
        ArrayList<String> semesters = department.getSemesters();
        return semesters.isEmpty() ? null : semesters.get(semesters.size() - 1);
    }

    private String selectedSemester() {
        Object item = cmbSemester.getSelectedItem();
        return item == null ? null : item.toString();
    }

    private void populateSemesters() {
        cmbSemester.removeAllItems();
        for (String semester : department.getSemesters()) {
            cmbSemester.addItem(semester);
        }
        if (currentSemester() != null) {
            cmbSemester.setSelectedItem(currentSemester());
        }
    }

    // the course load for the chosen semester, or null if the student has none there yet
    private CourseLoad loadFor(String semester) {
        return semester == null ? null : record.getCourseLoadBySemester(semester);
    }

    public void refreshTables() {
        String semester = selectedSemester();
        CourseLoad load = loadFor(semester);

        DefaultTableModel offers = (DefaultTableModel) tblOffers.getModel();
        offers.setRowCount(0);
        offerRows.clear();
        CourseSchedule schedule = semester == null ? null : department.getCourseSchedule(semester);
        if (schedule != null) {
            for (CourseOffer co : schedule.getSchedule()) {
                String status;
                if (load != null && load.findSeatAssignment(co) != null) {
                    status = "Registered";
                } else if (co.getAvailableSeats() == 0) {
                    status = "Full";
                } else {
                    status = "Open";
                }
                Object[] row = new Object[6];
                row[0] = co.getCourseNumber();
                row[1] = co.getSubjectCourse().getName();
                row[2] = co.getCreditHours();
                row[3] = co.getTeacherName();
                row[4] = co.getAvailableSeats() + " of " + co.getTotalSeats();
                row[5] = status;
                offers.addRow(row);
                offerRows.add(co);
            }
        }

        DefaultTableModel mine = (DefaultTableModel) tblMine.getModel();
        mine.setRowCount(0);
        myRows.clear();
        int credits = 0;
        if (load != null) {
            for (SeatAssignment sa : load.getSeatAssignments()) {
                Object[] row = new Object[4];
                row[0] = sa.getCourseOffer().getCourseNumber();
                row[1] = sa.getAssociatedCourse().getName();
                row[2] = sa.getCreditHours();
                row[3] = sa.getLetterGrade();
                mine.addRow(row);
                myRows.add(sa);
                credits = credits + sa.getCreditHours();
            }
        }
        String note;
        if (currentSemester() == null) {
            note = "no semester has been scheduled yet";
        } else if (semester != null && semester.equals(currentSemester())) {
            note = "registration is open";
        } else {
            note = "registration is closed for this semester";
        }
        int classes = load == null ? 0 : load.getSeatAssignments().size();
        lblCredits.setText("Classes this semester: " + classes + " of " + MAX_COURSES_PER_SEMESTER + " (credits: " + credits + ")   -   " + note);
        int[] w1 = { 90, 300, 60, 130, 90, 90 };
        for (int i = 0; i < w1.length; i++) {
            tblOffers.getColumnModel().getColumn(i).setPreferredWidth(w1[i]);
        }
        int[] w2 = { 90, 380, 60, 110 };
        for (int i = 0; i < w2.length; i++) {
            tblMine.getColumnModel().getColumn(i).setPreferredWidth(w2[i]);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblStudent = new javax.swing.JLabel();
        lblSemester = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        lblOffers = new javax.swing.JLabel();
        spOffers = new javax.swing.JScrollPane();
        tblOffers = new javax.swing.JTable();
        btnRegister = new javax.swing.JButton();
        lblMine = new javax.swing.JLabel();
        spMine = new javax.swing.JScrollPane();
        tblMine = new javax.swing.JTable();
        lblCredits = new javax.swing.JLabel();
        btnDrop = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        lblTitle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblTitle.setText("Course Registration");
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

        lblOffers.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblOffers.setText("Classes offered");
        add(lblOffers);
        lblOffers.setBounds(30, 86, 300, 22);

        tblOffers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Course No", "Course Name", "Credits", "Professor", "Seats Left", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spOffers.setViewportView(tblOffers);

        add(spOffers);
        spOffers.setBounds(30, 110, 850, 125);

        btnRegister.setText("Register for Selected Class");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        add(btnRegister);
        btnRegister.setBounds(30, 243, 220, 30);

        lblMine.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblMine.setText("My classes this semester");
        add(lblMine);
        lblMine.setBounds(30, 283, 300, 22);

        tblMine.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Course No", "Course Name", "Credits", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spMine.setViewportView(tblMine);

        add(spMine);
        spMine.setBounds(30, 307, 850, 95);

        lblCredits.setText("Classes this semester:");
        add(lblCredits);
        lblCredits.setBounds(30, 408, 700, 20);

        btnDrop.setText("Drop Selected Class");
        btnDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDropActionPerformed(evt);
            }
        });
        add(btnDrop);
        btnDrop.setBounds(30, 434, 180, 30);

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
        refreshTables();
    }//GEN-LAST:event_cmbSemesterActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        String semester = selectedSemester();
        if (semester == null) {
            JOptionPane.showMessageDialog(this, "There is no semester to register for yet.", "Registration", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!semester.equals(currentSemester())) {
            JOptionPane.showMessageDialog(this, "Registration for " + semester + " is closed. You can only register for " + currentSemester() + ".", "Registration Closed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int row = tblOffers.getSelectedRow();
        if (row < 0 || row >= offerRows.size()) {
            JOptionPane.showMessageDialog(this, "Please select a class from the list of classes offered.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CourseOffer co = offerRows.get(row);

        CourseLoad load = loadFor(semester);   // stays null until the first class of the semester is accepted
        if (load != null && load.findSeatAssignment(co) != null) {
            JOptionPane.showMessageDialog(this, "You are already registered for " + co.getCourseNumber() + " this semester.", "Already Registered", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // a course that is already on the transcript cannot be taken again: completed, or still being taken
        SeatAssignment earlier = department.getDegree().findCourseOnTranscript(record, co.getSubjectCourse());
        if (earlier != null && earlier.isGraded()) {
            JOptionPane.showMessageDialog(this, "You have already completed " + co.getCourseNumber() + " with a grade of " + earlier.getLetterGrade() + ".", "Course Completed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (earlier != null) {
            JOptionPane.showMessageDialog(this, "You are already taking " + co.getCourseNumber() + " (" + earlier.getCourseLoad().getSemester() + "). It has not been graded yet, so you cannot register for it again.", "Course In Progress", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int classesNow = load == null ? 0 : load.getSeatAssignments().size();
        if (classesNow >= MAX_COURSES_PER_SEMESTER) {
            JOptionPane.showMessageDialog(this, "You can take at most " + MAX_COURSES_PER_SEMESTER + " classes in a semester.", "Course Load Full", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (co.getAvailableSeats() == 0) {
            JOptionPane.showMessageDialog(this, co.getCourseNumber() + " is full. There are no seats left.", "Class Full", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (load == null) {
            load = record.newCourseLoad(semester);   // first class this semester
        }
        SeatAssignment sa = load.newSeatAssignment(co);
        if (sa == null) {
            JOptionPane.showMessageDialog(this, "No seat could be assigned. Please try another class.", "Registration", JOptionPane.ERROR_MESSAGE);
            return;
        }
        refreshTables();
        JOptionPane.showMessageDialog(this, "You are registered for " + co.getCourseNumber() + " " + co.getSubjectCourse().getName() + " with " + co.getTeacherName() + ".", "Registered", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDropActionPerformed
        String semester = selectedSemester();
        int row = tblMine.getSelectedRow();
        if (row < 0 || row >= myRows.size()) {
            JOptionPane.showMessageDialog(this, "Please select one of your classes to drop.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        SeatAssignment sa = myRows.get(row);
        if (sa.isGraded()) {
            JOptionPane.showMessageDialog(this, sa.getCourseOffer().getCourseNumber() + " is already completed and graded. It cannot be dropped.", "Cannot Drop", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (semester == null || !semester.equals(currentSemester())) {
            JOptionPane.showMessageDialog(this, "Only classes in the current semester (" + currentSemester() + ") can be dropped.", "Cannot Drop", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Drop " + sa.getCourseOffer().getCourseNumber() + " " + sa.getAssociatedCourse().getName() + "?\nYour submitted work for it will be lost.", "Confirm Drop", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        CourseLoad load = loadFor(semester);
        load.dropSeatAssignment(sa);   // the seat opens up again for other students
        refreshTables();
        JOptionPane.showMessageDialog(this, "You dropped " + sa.getCourseOffer().getCourseNumber() + ".", "Dropped", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnDropActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        CardSequencePanel.remove(this);
        ((CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDrop;
    private javax.swing.JButton btnRegister;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JLabel lblCredits;
    private javax.swing.JLabel lblMine;
    private javax.swing.JLabel lblOffers;
    private javax.swing.JLabel lblSemester;
    private javax.swing.JLabel lblStudent;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane spMine;
    private javax.swing.JScrollPane spOffers;
    private javax.swing.JTable tblMine;
    private javax.swing.JTable tblOffers;
    // End of variables declaration//GEN-END:variables
}
