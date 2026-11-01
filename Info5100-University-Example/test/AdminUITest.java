/*
 * Administrator use case UI test harness.
 */
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import Business.Business;
import Business.Profiles.StudentProfile;
import Business.UserAccounts.UserAccount;
import info5100.university.example.CourseSchedule.CourseOffer;
import UserInterface.WorkAreas.AdminRole.AdminRoleWorkAreaJPanel;
import UserInterface.WorkAreas.AdminRole.AccountHealthWorkResp.AccountHealthJPanel;
import UserInterface.WorkAreas.AdminRole.AdministerUserAccountsWorkResp.AdminUserAccount;
import UserInterface.WorkAreas.AdminRole.ManagePersonnelWorkResp.ManagePersonsJPanel;
import UserInterface.WorkAreas.AdminRole.ManageStudentsWorkResp.ManageStudentsJPanel;
import UserInterface.WorkAreas.AdminRole.UniversityReportsWorkResp.UniversityReportsJPanel;
import UserInterface.WorkAreas.MyProfile.MyProfileJPanel;

/**
 * Drives the administrator screens like a user would (selects rows, types, clicks, answers the
 * message boxes) and checks the business objects afterwards. Written in the same style as
 * StudentUITest. Needs a display. In NetBeans: right-click the file under Test Packages and
 * choose Run File. From a terminal, in the Info5100-University-Example folder:
 *
 *   javac -d build/test -cp build/classes test/AdminUITest.java
 *   java -cp build/classes:build/test AdminUITest build/admin-screenshots
 *
 * Christopher Ogden NUID: 003959375 Admin use case.
 */
public class AdminUITest {

    static int pass = 0, fail = 0;
    static String out;

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

    /** a private field of a screen, by the variable name the GUI Builder gave it */
    @SuppressWarnings("unchecked")
    static <T> T field(Object screen, String name) {
        try {
            java.lang.reflect.Field f = screen.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(screen);
        } catch (Exception e) {
            throw new RuntimeException("no field " + name, e);
        }
    }

    static int row(JTable t, int column, String value) {
        for (int r = 0; r < t.getRowCount(); r++) if (value.equals(String.valueOf(t.getValueAt(r, column)))) return r;
        return -1;
    }

    static void select(JTable t, int column, String value) {
        int r = row(t, column, value);
        t.setRowSelectionInterval(r, r);
    }

    static String lastDialogText = "";
    static List<String> dialogTexts = new ArrayList<String>();

    /**
     * click; every message box the click opens is read, its password boxes are filled with the
     * given passwords, and it is answered with the given button (or OK, or closed). The clicks
     * themselves run on the Swing event thread: Nimbus can fail when a button is pressed from
     * another thread (ClassCastException in NimbusStyle.getBackgroundPainter).
     */
    static void click(final AbstractButton b, final String answer, final String... passwords) {
        lastDialogText = "";
        dialogTexts.clear();
        final boolean[] done = {false};
        final Set<Window> answered = new HashSet<Window>();
        Thread closer = new Thread(new Runnable() {
            public void run() {
                try {
                    while (!done[0]) {
                        Thread.sleep(100);
                        for (final Window w : Window.getWindows()) {
                            if (w instanceof JDialog && w.isShowing() && answered.add(w)) {
                                StringBuilder sb = new StringBuilder();
                                for (JLabel l : all((Container) w, JLabel.class)) if (l.getText() != null) sb.append(l.getText()).append(" ");
                                lastDialogText = sb.toString();
                                dialogTexts.add(lastDialogText);
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        List<JPasswordField> boxes = all((Container) w, JPasswordField.class);
                                        for (int i = 0; i < boxes.size() && i < passwords.length; i++) boxes.get(i).setText(passwords[i]);
                                        JButton reply = null;
                                        for (JButton x : all((Container) w, JButton.class)) if (x.getText().equals(answer)) reply = x;
                                        if (reply == null) for (JButton x : all((Container) w, JButton.class)) if (x.getText().equals("OK")) reply = x;
                                        if (reply != null) reply.doClick(); else w.dispose();
                                    }
                                });
                            }
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        });
        closer.setDaemon(true);
        closer.start();
        onSwingThread(new Runnable() {
            public void run() {
                b.doClick();   // a message box opened here waits until the closer answers it
            }
        });
        try { Thread.sleep(300); } catch (InterruptedException e) { }
        done[0] = true;
        try { closer.join(1000); } catch (InterruptedException e) { }
    }

    static void click(AbstractButton b) {
        click(b, "OK");
    }

    /** runs a step on the Swing event thread and waits for it (Nimbus paints safely only there) */
    static void onSwingThread(Runnable step) {
        if (SwingUtilities.isEventDispatchThread()) {
            step.run();
            return;
        }
        try {
            SwingUtilities.invokeAndWait(step);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** picks an item in a dropdown on the Swing event thread, which runs the screen's handler */
    static void choose(final JComboBox<String> combo, final String item) {
        onSwingThread(new Runnable() {
            public void run() {
                combo.setSelectedItem(item);
            }
        });
    }

    static void render(final JComponent c, final String name, final int w, final int h) throws Exception {
        onSwingThread(new Runnable() {
            public void run() {
                JFrame f = new JFrame();
                f.setContentPane(c);
                f.setSize(w, h);
                f.addNotify();
                f.validate();
                BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = img.createGraphics();
                c.printAll(g);
                g.dispose();
                try {
                    ImageIO.write(img, "png", new File(out, name + ".png"));
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
                f.dispose();
            }
        });
    }

    static Business business() throws Exception {
        java.lang.reflect.Method m = Class.forName("Business.ConfigureABusiness").getDeclaredMethod("initialize");
        m.setAccessible(true);
        return (Business) m.invoke(null);
    }

    /**
     * The whole test runs on the Swing event thread. Nimbus is not thread safe: building or
     * clicking screens from another thread can corrupt its style cache on a Mac
     * (ClassCastException ... cannot be cast to javax.swing.Painter).
     */
    public static void main(final String[] a) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                try {
                    AdminUITest.run(a);
                } catch (Throwable t) {
                    t.printStackTrace();
                    System.exit(2);
                }
            }
        });
    }

    static void run(String[] a) throws Exception {
        out = a.length > 0 ? a[0] : "build/admin-screenshots";
        new File(out).mkdirs();
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
        }
        Business b = business();
        UserAccount admin = b.getUserAccountDirectory().AuthenticateUser("admin", "******");
        UserAccount adam = b.getUserAccountDirectory().AuthenticateUser("adam", "******");
        UserAccount faculty = b.getUserAccountDirectory().AuthenticateUser("faculty", "******");
        UserAccount jack = b.getUserAccountDirectory().AuthenticateUser("jack", "jack1234");
        JPanel upc = new JPanel(new CardLayout());

        // ---- authentication through the real main frame (PR 2)
        JFrame mf = (JFrame) Class.forName("Business.ProfileWorkAreaMainFrame").getConstructor().newInstance();
        mf.addNotify();
        mf.validate();
        JTextField userField = null, pwField = null;
        for (JTextField f : all(mf.getContentPane(), JTextField.class)) {
            if (f instanceof JPasswordField) pwField = f; else if (userField == null) userField = f;
        }
        userField.setText("anna");
        pwField.setText("anna1234");
        click(button(mf.getContentPane(), "Login"));
        check("login: a disabled account is refused with its own message", lastDialogText.contains("has been disabled"));
        userField.setText("admin");
        pwField.setText("******");
        click(button(mf.getContentPane(), "Login"));
        mf.validate();
        check("login: admin lands in the administrator work area", all(mf.getContentPane(), JButton.class).contains(button(mf.getContentPane(), "Administer User Accounts")));
        mf.dispose();

        // ---- authorization (PR 2)
        check("authorization: admin and jack are administrators", b.isAdministrator(admin) && b.isAdministrator(jack));
        check("authorization: a student and a faculty member are not", !b.isAdministrator(adam) && !b.isAdministrator(faculty) && !b.isAdministrator(null));
        jack.setActive(false);
        check("authorization: a disabled admin account is not an administrator", !b.isAdministrator(jack));
        jack.setActive(true);
        AdminRoleWorkAreaJPanel wrongUser = new AdminRoleWorkAreaJPanel(b, adam, upc);
        int cardsBefore = upc.getComponentCount();
        click(button(wrongUser, "Manage Students"));
        check("authorization: the admin work area refuses a student's account", lastDialogText.contains("Only an administrator") && upc.getComponentCount() == cardsBefore);
        AdminRoleWorkAreaJPanel adminArea = new AdminRoleWorkAreaJPanel(b, admin, upc);
        click(button(adminArea, "Manage Students"));
        check("authorization: the same button opens the screen for the admin", upc.getComponentCount() == cardsBefore + 1);

        // ---- University Reports
        UniversityReportsJPanel reports = new UniversityReportsJPanel(b, admin, upc);
        JComboBox<String> report = field(reports, "cmbReport");
        JComboBox<String> semester = field(reports, "cmbSemester");
        JTable tr = field(reports, "tblReport");
        JLabel summary = field(reports, "lblSummary");
        check("reports: the current semester is selected and lists its 5 classes", "Fall 2026".equals(semester.getSelectedItem()) && tr.getRowCount() == 5);
        int r6350 = row(tr, 0, "INFO 6350");
        check("reports: INFO 6350 is 1 of 1 seats, 100% full, $6,000", "1 of 1".equals(tr.getValueAt(r6350, 4)) && "100%".equals(tr.getValueAt(r6350, 5)) && "$6,000".equals(tr.getValueAt(r6350, 6)));
        check("reports: the enrollment summary adds up the semester", summary.getText().contains("5 classes, 2 of 58 seats taken (3%), revenue $12,000"));
        render(reports, "05_course_enrollment", 900, 500);
        choose(semester, "Spring 2026");
        check("reports: changing the semester shows Spring 2026's 3 classes", tr.getRowCount() == 3 && row(tr, 0, "INFO 6205") >= 0);
        choose(report, "Student standing");
        int rAdam = row(tr, 0, "Adam Rollen");
        check("reports: student standing ignores the semester", !semester.isEnabled() && tr.getRowCount() == 3);
        check("reports: Adam has 16 credits, GPA 3.50, 3 of 3 core, 16 to go, not ready", "16".equals(String.valueOf(tr.getValueAt(rAdam, 2))) && "3.50".equals(tr.getValueAt(rAdam, 3)) && "3 of 3".equals(tr.getValueAt(rAdam, 4)) && "16".equals(String.valueOf(tr.getValueAt(rAdam, 5))) && "No".equals(tr.getValueAt(rAdam, 6)));
        choose(report, "Faculty load");
        choose(semester, "Fall 2026");
        int rLaura = row(tr, 0, "Laura Brown");
        check("reports: Laura Brown teaches 3 classes in Fall 2026", semester.isEnabled() && "3".equals(String.valueOf(tr.getValueAt(rLaura, 1))) && "INFO 7250, INFO 7375, INFO 5100".equals(tr.getValueAt(rLaura, 2)));

        // ---- Administer User Accounts (PR 3)
        AdminUserAccount accounts = new AdminUserAccount(b, admin, upc);
        JTable ta = field(accounts, "tblUserAccounts");
        JComboBox<?> people = field(accounts, "cmbPerson");
        check("accounts: 7 seeded logins with a Status column", ta.getRowCount() == 7 && "Status".equals(ta.getColumnName(6)));
        check("accounts: anna is Disabled, admin is Active", "Disabled".equals(ta.getValueAt(row(ta, 0, "anna"), 6)) && "Active".equals(ta.getValueAt(row(ta, 0, "admin"), 6)));
        check("accounts: everyone has a login, so the person list is empty and Create is off", people.getItemCount() == 0 && !((JButton) field(accounts, "btnCreate")).isEnabled());
        select(ta, 0, "admin");
        click(field(accounts, "btnEnableDisable"), "Yes");
        check("accounts: the admin cannot disable their own account", lastDialogText.contains("cannot disable the account you are logged in with") && admin.isActive());
        select(ta, 0, "jim");
        click(field(accounts, "btnEnableDisable"), "No");
        UserAccount jim = b.getUserAccountDirectory().AuthenticateUser("jim", "******");
        check("accounts: answering No leaves jim active", jim.isActive());
        select(ta, 0, "jim");
        click(field(accounts, "btnEnableDisable"), "Yes");
        check("accounts: Disable -> Yes disables jim", !jim.isActive() && "Disabled".equals(ta.getValueAt(row(ta, 0, "jim"), 6)) && lastDialogText.contains("was disabled"));
        select(ta, 0, "jim");
        click(field(accounts, "btnEnableDisable"), "Yes");
        check("accounts: Enable -> Yes makes jim active again", jim.isActive() && "Active".equals(ta.getValueAt(row(ta, 0, "jim"), 6)));
        render(accounts, "10_user_accounts", 900, 600);

        // a pre-registered student shows up in the person list as soon as the screen opens
        b.getStudentDirectory().newStudentProfile(b.getPersonDirectory().newPerson("Priya Shah"), "912599999");
        accounts = new AdminUserAccount(b, admin, upc);
        people = field(accounts, "cmbPerson");
        JTextField newUser = field(accounts, "txtUserName");
        JPasswordField newPw = field(accounts, "pwEntry");
        check("accounts: a pre-registered student is listed right away", people.getItemCount() == 1 && String.valueOf(people.getItemAt(0)).startsWith("Priya Shah"));
        newUser.setText("pr");
        newPw.setText("priya123");
        click(field(accounts, "btnCreate"));
        check("accounts: a 2-letter user name is refused", lastDialogText.contains("at least 3 characters"));
        newUser.setText("adam");
        click(field(accounts, "btnCreate"));
        check("accounts: a user name that is taken is refused", lastDialogText.contains("already taken"));
        newUser.setText("priya");
        newPw.setText("abc");
        click(field(accounts, "btnCreate"));
        check("accounts: a short password is refused", lastDialogText.contains("at least 6 characters"));
        newPw.setText("priya123");
        click(field(accounts, "btnCreate"));
        check("accounts: the login is created and works", lastDialogText.contains("was created for Priya Shah") && b.getUserAccountDirectory().AuthenticateUser("priya", "priya123") != null && people.getItemCount() == 0);

        // ---- Manage Personnel (PR 4)
        ManagePersonsJPanel staff = new ManagePersonsJPanel(b, admin, upc);
        check("personnel: the hire date starts as today", LocalDate.now().toString().equals(((JTextField) field(staff, "txtHireDate")).getText()));
        ((JTextField) field(staff, "txtName")).setText("Maria Lopez");
        ((JTextField) field(staff, "txtEmail")).setText("maria.lopez@northeastern.edu");
        ((JTextField) field(staff, "txtPhone")).setText("abc");
        ((JTextField) field(staff, "txtDepartment")).setText("Registrar");
        ((JTextField) field(staff, "txtJobTitle")).setText("Registrar Officer");
        ((JTextField) field(staff, "txtUserName")).setText("maria");
        ((JPasswordField) field(staff, "pwEntry")).setText("maria123");
        click(field(staff, "btnAdd"));
        check("personnel: a bad phone number is refused", lastDialogText.contains("Use 7-20 digits"));
        ((JTextField) field(staff, "txtPhone")).setText("617-555-0199");
        ((JTextField) field(staff, "txtEmail")).setText("Adam.Rollen@northeastern.edu");
        click(field(staff, "btnAdd"));
        check("personnel: another person's email is refused (any case)", lastDialogText.contains("already registered with this email"));
        ((JTextField) field(staff, "txtEmail")).setText("maria.lopez@northeastern.edu");
        click(field(staff, "btnAdd"));
        check("personnel: a valid employee is added with a working login", lastDialogText.contains("added successfully") && b.getUserAccountDirectory().AuthenticateUser("maria", "maria123") != null);

        // ---- Account Health (after Priya and Maria were added; adam has logged in once)
        adam.recordLogin();
        AccountHealthJPanel health = new AccountHealthJPanel(b, admin, upc);
        JTable th = field(health, "tblHealth");
        JComboBox<String> show = field(health, "cmbShow");
        JLabel healthSummary = field(health, "lblSummary");
        check("health: anna is listed as a disabled account", row(th, 0, "Anna Shnider") >= 0 && healthSummary.getText().contains("1 disabled"));
        boolean adamListed = false, jimNever = false, priyaNoEmail = false;
        for (int r = 0; r < th.getRowCount(); r++) {
            if ("Adam Rollen".equals(th.getValueAt(r, 0))) adamListed = true;
            if ("Jim Dellon".equals(th.getValueAt(r, 0)) && "Never logged in".equals(th.getValueAt(r, 3))) jimNever = true;
            if ("Priya Shah".equals(th.getValueAt(r, 0)) && "No email address".equals(th.getValueAt(r, 3))) priyaNoEmail = true;
        }
        check("health: jim (never logged in) is listed, adam (has logged in) is not", jimNever && !adamListed);
        check("health: Priya (pre-registered without an email) is listed", priyaNoEmail);
        choose(show, "Disabled account");
        check("health: Show = Disabled account lists only anna", th.getRowCount() == 1 && "Anna Shnider".equals(th.getValueAt(0, 0)));
        choose(show, "No login account");
        check("health: everyone has a login now, so that list is empty", th.getRowCount() == 0 && healthSummary.getText().contains("0 without a login"));
        choose(show, "All issues");
        render(health, "15_account_health", 900, 500);

        // ---- Manage Students (PR 5)
        ManageStudentsJPanel students = new ManageStudentsJPanel(b, admin, upc);
        JTable ts = field(students, "tblStudentData");
        check("students: Credits, GPA and Classes columns", ts.getColumnCount() == 9 && "Credits".equals(ts.getColumnName(6)) && "GPA".equals(ts.getColumnName(7)));
        int adamRow = row(ts, 0, "Adam Rollen"), jimRow = row(ts, 0, "Jim Dellon");
        check("students: Adam has 16 credits, GPA 3.50, INFO 7250 this semester", "16".equals(String.valueOf(ts.getValueAt(adamRow, 6))) && "3.50".equals(ts.getValueAt(adamRow, 7)) && "INFO 7250".equals(ts.getValueAt(adamRow, 8)));
        check("students: Jim has 0 credits, GPA -, INFO 6350", "0".equals(String.valueOf(ts.getValueAt(jimRow, 6))) && "-".equals(ts.getValueAt(jimRow, 7)) && "INFO 6350".equals(ts.getValueAt(jimRow, 8)));
        render(students, "20_manage_students", 1000, 600);
        CourseOffer smartphones = b.getDepartment().getCourseSchedule("Fall 2026").getCourseOfferByNumber("INFO 6350");
        check("students: INFO 6350 is full while Jim holds its seat", smartphones.getAvailableSeats() == 0);
        select(ts, 0, "Jim Dellon");
        click(field(students, "btnDelete"), "No");
        check("students: the delete confirmation warns about Jim's class", lastDialogText.contains("enrolled in 1 class(es) this semester (INFO 6350)") && row(ts, 0, "Jim Dellon") >= 0);
        select(ts, 0, "Jim Dellon");
        click(field(students, "btnDelete"), "Yes");
        check("students: deleting Jim removes him and his login", lastDialogText.contains("Student deleted") && row(ts, 0, "Jim Dellon") < 0 && b.getUserAccountDirectory().AuthenticateUser("jim", "******") == null);
        check("students: Jim's seat in INFO 6350 is free again", smartphones.getAvailableSeats() == 1);

        // ---- My Profile (PR 6)
        MyProfileJPanel profile = new MyProfileJPanel(b, admin, upc);
        JTextField name = field(profile, "txtName"), email = field(profile, "txtEmail"), phone = field(profile, "txtPhone");
        JButton edit = field(profile, "btnEdit");
        check("profile: admin sees Edit Profile and Change Password, fields locked", edit.isVisible() && ((JButton) field(profile, "btnChangePassword")).isVisible() && !name.isEditable());
        click(edit);
        check("profile: Edit Profile unlocks the contact fields and becomes Save", name.isEditable() && email.isEditable() && "Save".equals(edit.getText()));
        email.setText("adam.rollen@northeastern.edu");
        click(edit);
        check("profile: another person's email is refused", lastDialogText.contains("already registered with this email") && name.isEditable());
        email.setText("john.smith@northeastern.edu");
        phone.setText("617-555-0100");
        click(edit);
        check("profile: valid details are saved to the person", lastDialogText.contains("Your profile was saved") && "617-555-0100".equals(admin.getAssociatedPersonProfile().getPerson().getPhone()) && !name.isEditable());
        click(edit);
        name.setText("Somebody Else");
        click(field(profile, "btnCancel"));
        check("profile: Cancel throws the edit away", "John Smith".equals(name.getText()) && "John Smith".equals(admin.getAssociatedPersonProfile().getPerson().getName()));
        click(field(profile, "btnChangePassword"), "OK", "wrong", "newpass1", "newpass1");
        check("profile: a wrong current password is refused", lastDialogText.contains("current password is not correct"));
        click(field(profile, "btnChangePassword"), "OK", "******", "newpass1", "newpass2");
        check("profile: a confirmation that does not match is refused", lastDialogText.contains("do not match"));
        click(field(profile, "btnChangePassword"), "OK", "******", "newpass1", "newpass1");
        check("profile: the password is changed", lastDialogText.contains("Your password was changed") && b.getUserAccountDirectory().AuthenticateUser("admin", "newpass1") == admin && b.getUserAccountDirectory().AuthenticateUser("admin", "******") == null);
        render(profile, "30_my_profile", 900, 600);
        MyProfileJPanel studentView = new MyProfileJPanel(adam, upc);
        check("profile: a student's My Profile stays view only", !((JButton) field(studentView, "btnEdit")).isVisible() && !((JButton) field(studentView, "btnChangePassword")).isVisible());
        finish();
    }

    static void finish() {
        System.out.println("\n" + pass + " passed, " + fail + " failed");
        System.exit(fail == 0 ? 0 : 1);
    }
}
