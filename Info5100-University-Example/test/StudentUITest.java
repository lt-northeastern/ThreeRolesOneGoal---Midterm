import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import Business.Business;
import Business.Profiles.StudentProfile;
import Business.UserAccounts.UserAccount;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.SeatAssignment;

/**
 * Drives the student screens like a user would (selects rows, types, clicks, reads the message
 * boxes) and checks the university model afterwards. Needs a display; run it with a folder for
 * its screenshots: java -cp build/classes:build/test StudentUITest screenshots
 *
 * Student use case - Hemanth Bezawada (NUID 002059527).
 */
public class StudentUITest {

    static int pass = 0, fail = 0;
    static String out;
    static String stage;

    static void check(String what, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + what);
        if (ok) pass++; else fail++;
    }

    static <T extends Component> List<T> all(Container c, Class<T> type) {
        List<T> found = new ArrayList<T>();
        for (Component child : c.getComponents()) {
            if (type.isInstance(child)) found.add(type.cast(child));
            if (child instanceof Container) found.addAll(all((Container) child, type));
        }
        return found;
    }

    static JButton button(Container c, String text) {
        for (JButton b : all(c, JButton.class)) if (text.equals(b.getText())) return b;
        throw new RuntimeException("no button " + text);
    }

    static JLabel label(Container c, String prefix) {
        for (JLabel l : all(c, JLabel.class)) if (l.getText() != null && l.getText().startsWith(prefix)) return l;
        throw new RuntimeException("no label " + prefix);
    }

    static String lastDialogText = "";

    /** click; a message box opened by the click is read and dismissed after a moment */
    static void click(AbstractButton b) {
        lastDialogText = "";
        Thread closer = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 40; i++) {
                        Thread.sleep(100);
                        for (Window w : Window.getWindows()) {
                            if (w instanceof JDialog && w.isShowing()) {
                                StringBuilder sb = new StringBuilder();
                                for (JLabel l : all((Container) w, JLabel.class)) if (l.getText() != null) sb.append(l.getText()).append(" ");
                                lastDialogText = sb.toString();
                                w.dispose();
                                return;
                            }
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        });
        closer.setDaemon(true);
        closer.start();
        b.doClick();
        try { closer.join(4500); } catch (InterruptedException e) { }
    }

    static void render(JComponent c, String name, int w, int h) throws Exception {
        JFrame f = new JFrame();
        f.setContentPane(c);
        f.setSize(w, h);
        f.addNotify();
        f.validate();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        c.printAll(g);
        g.dispose();
        ImageIO.write(img, "png", new File(out, name + ".png"));
        f.dispose();
    }

    static Business business() throws Exception {
        java.lang.reflect.Method m = Class.forName("Business.ConfigureABusiness").getDeclaredMethod("initialize");
        m.setAccessible(true);
        return (Business) m.invoke(null);
    }

    public static void main(String[] a) {
        try {
            run(a);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(2);
        }
    }

    static void run(String[] a) throws Exception {
        out = a[0];
        stage = a.length > 1 ? a[1] : "all";
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
        }
        Business b = business();
        UserAccount ua = b.getUserAccountDirectory().AuthenticateUser("adam", "******");
        StudentProfile adam = (StudentProfile) ua.getAssociatedPersonProfile();
        info5100.university.example.Persona.StudentProfile rec = adam.getAcademicRecord();
        JPanel upc = new JPanel(new java.awt.CardLayout());

        // ---- the real main frame: size and the student work area
        JFrame mf = (JFrame) Class.forName("Business.ProfileWorkAreaMainFrame").getConstructor().newInstance();
        mf.addNotify();   // keep the size the frame gives itself; pack() would shrink it to the login panel
        mf.validate();
        System.out.println("main frame size: " + mf.getWidth() + "x" + mf.getHeight());
        BufferedImage img = new BufferedImage(mf.getWidth(), mf.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        mf.getContentPane().printAll(g);
        g.dispose();
        ImageIO.write(img, "png", new File(out, "00_mainframe.png"));
        // log in through the real frame and open Registration from the work area
        JTextField userField = null, pwField = null;   // the user name box and the password box
        for (JTextField f : all(mf.getContentPane(), JTextField.class)) {
            if (f instanceof JPasswordField) pwField = f; else if (userField == null) userField = f;
        }
        userField.setText("");
        pwField.setText("");
        click(button(mf.getContentPane(), "Login"));
        check("login: empty fields give a message", lastDialogText.contains("enter your user name"));
        userField.setText("adam");
        pwField.setText("wrong");
        click(button(mf.getContentPane(), "Login"));
        check("login: wrong password gives an Invalid message", lastDialogText.contains("Invalid user name or password"));
        userField.setText("adam");
        pwField.setText("******");
        click(button(mf.getContentPane(), "Login"));
        mf.validate();
        check("frame: student work area opens after login", all(mf.getContentPane(), JButton.class).size() > 3 && button(mf.getContentPane(), "Registration") != null);
        click(button(mf.getContentPane(), "Registration"));
        mf.validate();
        img = new BufferedImage(mf.getWidth(), mf.getHeight(), BufferedImage.TYPE_INT_RGB);
        g = img.createGraphics();
        mf.getContentPane().printAll(g);
        g.dispose();
        ImageIO.write(img, "png", new File(out, "01_frame_registration.png"));
        JButton back = button(mf.getContentPane(), "<< Back");
        check("frame: Back button is fully inside the card panel", back.getParent().isVisible() && back.getParent().getHeight() >= back.getY() + back.getHeight() && back.getParent().getWidth() >= back.getX() + back.getWidth());
        System.out.println("card panel size: " + back.getParent().getWidth() + "x" + back.getParent().getHeight());
        Container cards = back.getParent().getParent();   // grab it before Back removes the screen
        click(back);
        mf.validate();
        Component visibleCard = null;
        for (Component c : cards.getComponents()) if (c.isVisible()) visibleCard = c;
        check("frame: Back removes the screen and shows the work area again", cards.getComponentCount() == 1 && visibleCard != null && all((Container) visibleCard, JButton.class).contains(button(mf.getContentPane(), "Registration")));
        mf.dispose();

        // ---- Registration
        Class<?> regCls = Class.forName("UserInterface.WorkAreas.StudentRole.RegistrationWorkResp.RegistrationJPanel");
        JPanel reg = (JPanel) regCls.getConstructor(Business.class, StudentProfile.class, JPanel.class).newInstance(b, adam, upc);
        upc.add("reg", reg);
        JComboBox combo = all(reg, JComboBox.class).get(0);
        JTable offers = all(reg, JTable.class).get(0), mine = all(reg, JTable.class).get(1);
        check("registration: semesters listed oldest first and current one selected", combo.getItemCount() == 3 && "Fall 2026".equals(String.valueOf(combo.getSelectedItem())));
        check("registration: Fall 2026 shows 5 classes offered", offers.getRowCount() == 5);
        check("registration: Adam's registered class is marked Registered", "Registered".equals(offers.getValueAt(0, 5)) && "INFO 7250".equals(offers.getValueAt(0, 0)));
        check("registration: the one-seat class is marked Full", "Full".equals(offers.getValueAt(1, 5)));
        check("registration: my classes table shows the in-progress class", mine.getRowCount() == 1 && "In progress".equals(mine.getValueAt(0, 3)));
        render(reg, "10_registration", 900, 480);

        click(button(reg, "Register for Selected Class"));
        check("registration: no selection is refused", lastDialogText.contains("select a class"));
        offers.setRowSelectionInterval(0, 0);
        click(button(reg, "Register for Selected Class"));
        check("registration: already registered is refused", lastDialogText.contains("already registered"));
        offers.setRowSelectionInterval(1, 1);
        click(button(reg, "Register for Selected Class"));
        check("registration: full class is refused", lastDialogText.contains("full"));
        offers.setRowSelectionInterval(4, 4);   // INFO 5100, completed in Fall 2025
        click(button(reg, "Register for Selected Class"));
        check("registration: completed course cannot be retaken", lastDialogText.contains("already completed"));
        CourseOffer adv = b.getDepartment().getCourseSchedule("Fall 2026").getCourseOfferByNumber("INFO 7375");
        int seatsBefore = adv.getAvailableSeats();
        offers.setRowSelectionInterval(2, 2);   // INFO 7375
        click(button(reg, "Register for Selected Class"));
        check("registration: open class registers, seat taken, row marked Registered", lastDialogText.contains("registered for INFO 7375") && adv.getAvailableSeats() == seatsBefore - 1 && mine.getRowCount() == 2 && "Registered".equals(offers.getValueAt(2, 5)));
        offers.setRowSelectionInterval(3, 3);   // INFO 6105
        click(button(reg, "Register for Selected Class"));
        check("registration: third class registers (3 of 4)", mine.getRowCount() == 3);
        // a fourth and then a fifth: the fifth must be refused by the course load limit
        combo.setSelectedItem("Fall 2026");
        CourseOffer extra = b.getDepartment().getCourseSchedule("Fall 2026").newCourseOffer("INFO 6250");
        extra.generatSeats(5);
        combo.setSelectedItem("Fall 2026");   // refresh via the combo handler
        int extraRow = -1;
        for (int i = 0; i < offers.getRowCount(); i++) if ("INFO 6250".equals(offers.getValueAt(i, 0))) extraRow = i;
        check("registration: the new offer appears after refresh", extraRow >= 0);
        CourseOffer extra2 = b.getDepartment().getCourseSchedule("Fall 2026").newCourseOffer("INFO 6150");
        extra2.generatSeats(5);
        combo.setSelectedItem("Fall 2026");
        int row6150 = -1;
        for (int i = 0; i < offers.getRowCount(); i++) if ("INFO 6150".equals(offers.getValueAt(i, 0))) row6150 = i;
        offers.setRowSelectionInterval(row6150, row6150);
        click(button(reg, "Register for Selected Class"));
        check("registration: a second completed core course is also refused", lastDialogText.contains("already completed"));
        offers.setRowSelectionInterval(extraRow, extraRow);   // INFO 6250 was completed too (Spring 2026)
        click(button(reg, "Register for Selected Class"));
        check("registration: completed elective refused as well", lastDialogText.contains("already completed"));
        // past semester is closed
        combo.setSelectedItem("Fall 2025");
        check("registration: past semester shows its two classes and the closed note", offers.getRowCount() == 2 && label(reg, "Classes this semester").getText().contains("closed"));
        offers.setRowSelectionInterval(0, 0);
        click(button(reg, "Register for Selected Class"));
        check("registration: registering in a past semester is refused", lastDialogText.contains("closed"));
        mine.setRowSelectionInterval(0, 0);
        click(button(reg, "Drop Selected Class"));
        check("drop: a graded past class cannot be dropped", lastDialogText.contains("cannot be dropped") || lastDialogText.contains("already completed"));
        combo.setSelectedItem("Fall 2026");
        click(button(reg, "Drop Selected Class"));
        check("drop: no selection is refused", lastDialogText.contains("select"));
        int myRow7375 = -1;
        for (int i = 0; i < mine.getRowCount(); i++) if ("INFO 7375".equals(mine.getValueAt(i, 0))) myRow7375 = i;
        mine.setRowSelectionInterval(myRow7375, myRow7375);
        click(button(reg, "Drop Selected Class"));   // the confirm dialog is dismissed = No
        check("drop: dismissing the confirmation keeps the class", mine.getRowCount() == 3 && adv.getAvailableSeats() == seatsBefore - 1);
        render(reg, "11_registration_after", 900, 480);

        if (stage.equals("registration")) { finish(); return; }

        // ---- Course Work (Adam now has 7250 with 2 submissions, 7375 and 6105 this fall)
        JPanel cw = (JPanel) Class.forName("UserInterface.WorkAreas.StudentRole.CourseWorkResp.CourseWorkJPanel").getConstructor(Business.class, StudentProfile.class, JPanel.class).newInstance(b, adam, upc);
        upc.add("cw", cw);
        JComboBox cwCombo = all(cw, JComboBox.class).get(0);
        JTable cwTable = all(cw, JTable.class).get(0);
        JTextField cwTitle = all(cw, JTextField.class).get(0);
        check("course work: current semester selected with 3 classes", "Fall 2026".equals(String.valueOf(cwCombo.getSelectedItem())) && cwTable.getRowCount() == 3);
        check("course work: Big Data shows 2 of 5 submitted, 40%, in progress, not liked", "2 of 5".equals(cwTable.getValueAt(0, 3)) && "40%".equals(cwTable.getValueAt(0, 4)) && "In progress".equals(cwTable.getValueAt(0, 5)) && "No".equals(cwTable.getValueAt(0, 6)));
        click(button(cw, "Submit Assignment"));
        check("course work: no selection is refused", lastDialogText.contains("select a class"));
        cwTable.setRowSelectionInterval(0, 0);
        cwTitle.setText("   ");
        click(button(cw, "Submit Assignment"));
        check("course work: empty title is refused", lastDialogText.contains("title"));
        cwTitle.setText("Assignment 3 - Spark streaming");
        click(button(cw, "Submit Assignment"));
        SeatAssignment bigData = rec.getCourseLoadBySemester("Fall 2026").getSeatAssignments().get(0);
        check("course work: submission recorded, 3 of 5 and 60%", bigData.getSubmissions().size() == 3 && "3 of 5".equals(cwTable.getValueAt(0, 3)) && "60%".equals(cwTable.getValueAt(0, 4)) && cwTitle.getText().isEmpty());
        cwTable.setRowSelectionInterval(0, 0);
        cwTitle.setText("Assignment 3 - Spark streaming");
        click(button(cw, "Submit Assignment"));
        check("course work: duplicate title refused", lastDialogText.contains("already submitted") && bigData.getSubmissions().size() == 3);
        cwTable.setRowSelectionInterval(0, 0);
        int likesBefore = rec.getTranscript().getStudentSatifactionIndex();
        click(button(cw, "Like / Unlike Course"));
        check("course work: like toggles on and the satisfaction index grows", bigData.getLike() && "Yes".equals(cwTable.getValueAt(0, 6)) && rec.getTranscript().getStudentSatifactionIndex() == likesBefore + 1);
        cwTable.setRowSelectionInterval(0, 0);
        for (int i = 4; i <= 5; i++) { cwTitle.setText("Assignment " + i); cwTable.setRowSelectionInterval(0, 0); click(button(cw, "Submit Assignment")); }
        check("course work: all five submitted shows 100%", "100%".equals(cwTable.getValueAt(0, 4)));
        cwTable.setRowSelectionInterval(0, 0);
        cwTitle.setText("Assignment 6");
        click(button(cw, "Submit Assignment"));
        check("course work: a sixth submission is refused", lastDialogText.contains("already submitted") && bigData.getSubmissions().size() == 5);
        render(cw, "20_coursework", 900, 500);
        cwCombo.setSelectedItem("Fall 2025");
        check("course work: past semester shows graded classes", cwTable.getRowCount() == 2 && "A".equals(cwTable.getValueAt(0, 5)));
        cwTable.setRowSelectionInterval(0, 0);
        cwTitle.setText("Late work");
        click(button(cw, "Submit Assignment"));
        check("course work: graded class takes no more submissions", lastDialogText.contains("already graded"));
        if (stage.equals("coursework")) { finish(); return; }

        // ---- Transcript
        JPanel tr = (JPanel) Class.forName("UserInterface.WorkAreas.StudentRole.TranscriptWorkResp.TranscriptJPanel").getConstructor(Business.class, StudentProfile.class, JPanel.class).newInstance(b, adam, upc);
        JTable trCourses = all(tr, JTable.class).get(0), trSem = all(tr, JTable.class).get(1);
        check("transcript: 7 classes over 3 semesters, oldest first", trCourses.getRowCount() == 7 && "Fall 2025".equals(trCourses.getValueAt(0, 0)) && "Fall 2026".equals(trCourses.getValueAt(6, 0)) && trSem.getRowCount() == 3);
        check("transcript: letter grades and points shown", "A".equals(trCourses.getValueAt(0, 4)) && "16.0".equals(trCourses.getValueAt(0, 5)) && "-".equals(trCourses.getValueAt(6, 5)));
        check("transcript: semester GPA for Fall 2025 is 3.65", "3.65".equals(trSem.getValueAt(0, 3)));
        check("transcript: summary shows 16 credits, GPA 3.50, 3 in progress", label(tr, "Credits completed").getText().contains("16") && label(tr, "Credits completed").getText().contains("3.50") && label(tr, "Credits completed").getText().contains("progress: 3"));
        render(tr, "30_transcript", 900, 500);
        if (stage.equals("transcript")) { finish(); return; }

        // ---- Graduation audit
        JPanel au = (JPanel) Class.forName("UserInterface.WorkAreas.StudentRole.GraduationAuditWorkResp.GraduationAuditJPanel").getConstructor(Business.class, StudentProfile.class, JPanel.class).newInstance(b, adam, upc);
        JTable auTable = all(au, JTable.class).get(0);
        check("audit: core courses all completed, 16 of 32 credits, not ready", label(au, "Core courses completed").getText().contains("3 of 3") && label(au, "Credits completed").getText().contains("16 of 32") && label(au, "Audit result").getText().contains("not ready") && label(au, "Audit result").getText().contains("16 more credit"));
        check("audit: table lists 3 core + 4 electives touched", auTable.getRowCount() == 7);
        render(au, "40_audit", 900, 500);
        // grade this fall's three classes and add one more graded class: 28 then 32 credits
        for (SeatAssignment sa : rec.getCourseLoadBySemester("Fall 2026").getSeatAssignments()) sa.setGrade(3.7f);
        click(button(au, "Run Audit Again"));
        check("audit: after grading, 28 of 32 and still not ready", label(au, "Credits completed").getText().contains("28 of 32") && label(au, "Audit result").getText().contains("4 more credit"));
        CourseOffer extraOffer = b.getDepartment().getCourseSchedule("Fall 2026").getCourseOfferByNumber("INFO 6250");
        SeatAssignment extraSa = rec.getCourseLoadBySemester("Fall 2026").newSeatAssignment(extraOffer);
        extraSa.setGrade(3.0f);
        click(button(au, "Run Audit Again"));
        check("audit: at 32 credits with all core done the student is ready", label(au, "Audit result").getText().contains("ready to graduate") && b.getDepartment().getDegree().isStudentReadyToGraduate(rec));
        // Jim: nothing graded yet
        UserAccount jua = b.getUserAccountDirectory().AuthenticateUser("jim", "******");
        StudentProfile jim = (StudentProfile) jua.getAssociatedPersonProfile();
        JPanel auJ = (JPanel) Class.forName("UserInterface.WorkAreas.StudentRole.GraduationAuditWorkResp.GraduationAuditJPanel").getConstructor(Business.class, StudentProfile.class, JPanel.class).newInstance(b, jim, upc);
        check("audit: Jim has 3 core outstanding and 32 credits to go", label(auJ, "Audit result").getText().contains("3 core course") && label(auJ, "Audit result").getText().contains("32 more credit"));
        JPanel trJ = (JPanel) Class.forName("UserInterface.WorkAreas.StudentRole.TranscriptWorkResp.TranscriptJPanel").getConstructor(Business.class, StudentProfile.class, JPanel.class).newInstance(b, jim, upc);
        check("transcript: Jim shows GPA '-' with 0 credits", label(trJ, "Credits completed").getText().contains("Credits completed: 0") && label(trJ, "Credits completed").getText().contains("GPA: -"));
        if (stage.equals("audit")) { finish(); return; }

        // ---- a brand-new student with no academic record yet
        StudentProfile newKid = b.getStudentDirectory().newStudentProfile(b.getPersonDirectory().newPerson("New Kid"), "912570001");
        UserAccount newKidAccount = b.getUserAccountDirectory().newUserAccount(newKid, "newkid", "******");
        check("new student: starts without an academic record", newKid.getAcademicRecord() == null);
        JPanel wa = (JPanel) Class.forName("UserInterface.WorkAreas.StudentRole.StudentWorkAreaJPanel").getConstructor(Business.class, UserAccount.class, JPanel.class).newInstance(b, newKidAccount, upc);
        upc.add("wa", wa);
        int cardsBefore = upc.getComponentCount();
        click(button(wa, "Registration"));
        check("new student: Registration creates the record and opens the screen", newKid.getAcademicRecord() != null && upc.getComponentCount() == cardsBefore + 1);
        click(button(wa, "Transcript"));
        click(button(wa, "Graduation Audit"));
        click(button(wa, "Course Work"));
        click(button(wa, "My Profile"));
        check("new student: every screen opens without error for an empty record", upc.getComponentCount() == cardsBefore + 5);
        JPanel regN = (JPanel) regCls.getConstructor(Business.class, StudentProfile.class, JPanel.class).newInstance(b, newKid, upc);
        JTable offersN = all(regN, JTable.class).get(0);
        for (int i = 0; i < offersN.getRowCount(); i++) if ("Full".equals(offersN.getValueAt(i, 5))) offersN.setRowSelectionInterval(i, i);
        click(button(regN, "Register for Selected Class"));
        check("new student: a refused first registration leaves no empty course load behind", lastDialogText.contains("full") && newKid.getAcademicRecord().getCourseLoadBySemester("Fall 2026") == null);
        for (int i = 0; i < offersN.getRowCount(); i++) if ("INFO 7250".equals(offersN.getValueAt(i, 0))) offersN.setRowSelectionInterval(i, i);
        click(button(regN, "Register for Selected Class"));
        check("new student: first accepted class creates the course load", newKid.getAcademicRecord().getCourseLoadBySemester("Fall 2026") != null && newKid.getAcademicRecord().getCourseLoadBySemester("Fall 2026").getSeatAssignments().size() == 1);
        b.getDepartment().newCourseSchedule("Spring 2027").newCourseOffer("INFO 7250").generatSeats(5);
        JPanel regN2 = (JPanel) regCls.getConstructor(Business.class, StudentProfile.class, JPanel.class).newInstance(b, newKid, upc);
        JTable offersN2 = all(regN2, JTable.class).get(0);
        check("new student: the next semester is now the open one", "Spring 2027".equals(String.valueOf(all(regN2, JComboBox.class).get(0).getSelectedItem())) && offersN2.getRowCount() == 1);
        offersN2.setRowSelectionInterval(0, 0);
        click(button(regN2, "Register for Selected Class"));
        check("new student: a course still in progress cannot be registered again", lastDialogText.contains("already taking") && newKid.getAcademicRecord().getCourseLoadBySemester("Spring 2027") == null);
        StudentProfile emptyKid = b.getStudentDirectory().newStudentProfile(b.getPersonDirectory().newPerson("Empty Kid"), "912570002");
        UserAccount emptyKidAccount = b.getUserAccountDirectory().newUserAccount(emptyKid, "emptykid", "******");
        JPanel waE = (JPanel) Class.forName("UserInterface.WorkAreas.StudentRole.StudentWorkAreaJPanel").getConstructor(Business.class, UserAccount.class, JPanel.class).newInstance(b, emptyKidAccount, upc);
        click(button(waE, "Course Work"));
        JPanel cwE = (JPanel) upc.getComponent(upc.getComponentCount() - 1);
        check("course work: an empty record says to register first", label(cwE, "You are not registered").getText().contains("Registration"));
        click(button(cwE, "Submit Assignment"));
        check("course work: submit with no classes explains what to do", lastDialogText.contains("Registration"));
        finish();
    }

    static void finish() {
        System.out.println("\n" + pass + " passed, " + fail + " failed");
        System.exit(fail == 0 ? 0 : 1);
    }
}
