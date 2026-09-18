import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String DATA_FILE = "bugtrack.dat";

    private static BugTrackingSystem system;
    private static User loggedInUser;

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    // ============================================================
    // MAIN METHOD
    // ============================================================

    public static void main(String[] args) {

        system = FileStorageService.load(DATA_FILE);

        if (system == null) {
            system = new BugTrackingSystem();
            system.initializeDefaultData();
            FileStorageService.save(system, DATA_FILE);
        }

        System.out.println("\n============================================");
        System.out.println("              BUGTRACK PRO");
        System.out.println("       JAVA BUG TRACKING SYSTEM");
        System.out.println("============================================");

        boolean running = true;

        while (running) {

            try {

                if (loggedInUser == null) {
                    running = showAuthenticationMenu();
                } else {
                    showDashboard();
                }

            } catch (Exception e) {

                System.out.println("\nError: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }

        FileStorageService.save(system, DATA_FILE);

        scanner.close();

        System.out.println("\nThank you for using BugTrack Pro!");
    }


    // ============================================================
    // AUTHENTICATION MENU
    // ============================================================

    private static boolean showAuthenticationMenu() {

        System.out.println("\n========== AUTHENTICATION ==========");
        System.out.println("1. Login");
        System.out.println("2. Register Reporter");
        System.out.println("3. Exit");

        int choice = InputUtil.readInt(
                scanner,
                "Enter choice: "
        );

        switch (choice) {

            case 1:
                login();
                return true;

            case 2:
                registerReporter();
                return true;

            case 3:
                return false;

            default:
                System.out.println("Invalid choice.");
                return true;
        }
    }


    // ============================================================
    // LOGIN
    // ============================================================

    private static void login() {

        System.out.println("\n========== LOGIN ==========");

        String email = InputUtil.readNonEmpty(
                scanner,
                "Email: "
        );

        String password = InputUtil.readNonEmpty(
                scanner,
                "Password: "
        );

        User user = system.login(email, password);

        if (user != null) {

            loggedInUser = user;

            System.out.println(
                    "\nLogin successful. Welcome, "
                            + user.getName()
                            + "!"
            );

        } else {

            System.out.println(
                    "Login failed: Invalid email or password."
            );
        }
    }


    // ============================================================
    // REGISTER REPORTER
    // ============================================================

    private static void registerReporter() {

        System.out.println("\n========== REGISTER REPORTER ==========");

        String name = InputUtil.readNonEmpty(
                scanner,
                "Full name: "
        );

        String email = InputUtil.readNonEmpty(
                scanner,
                "Email: "
        );

        String password = InputUtil.readNonEmpty(
                scanner,
                "Password: "
        );

        try {

            User user = system.registerUser(
                    name,
                    email,
                    password,
                    Role.REPORTER
            );

            FileStorageService.save(
                    system,
                    DATA_FILE
            );

            System.out.println(
                    "Registration successful for "
                            + user.getName()
                            + "."
            );

            System.out.println(
                    "You can now log in."
            );

        } catch (AuthenticationException e) {

            System.out.println(
                    "Registration failed: "
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // MAIN DASHBOARD
    // ============================================================

    private static void showDashboard() {

        while (loggedInUser != null) {

            System.out.println(
                    "\n============================================"
            );

            System.out.println(
                    "                 BUGTRACK PRO"
            );

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "Logged in as: "
                            + loggedInUser.getName()
            );

            System.out.println(
                    "Role: "
                            + loggedInUser.getRole()
            );

            System.out.println(
                    "--------------------------------------------"
            );

            System.out.println(
                    "1. Dashboard Statistics"
            );

            System.out.println(
                    "2. View All Issues"
            );

            System.out.println(
                    "3. Create New Issue"
            );

            System.out.println(
                    "4. View Issue Details"
            );

            System.out.println(
                    "5. Search Issues"
            );

            System.out.println(
                    "6. Filter Issues"
            );

            System.out.println(
                    "7. Update Issue Status"
            );

            System.out.println(
                    "8. Assign Issue"
            );

            System.out.println(
                    "9. Add Comment"
            );

            System.out.println(
                    "10. View Activity History"
            );

            System.out.println(
                    "11. Manage Users"
            );

            System.out.println(
                    "12. Logout"
            );

            int choice = InputUtil.readInt(
                    scanner,
                    "Enter choice: "
            );

            try {

                switch (choice) {

                    case 1:
                        showStatistics();
                        break;

                    case 2:
                        viewAllIssues();
                        break;

                    case 3:
                        createIssue();
                        break;

                    case 4:
                        viewIssueDetails();
                        break;

                    case 5:
                        searchIssues();
                        break;

                    case 6:
                        filterIssues();
                        break;

                    case 7:
                        updateIssueStatus();
                        break;

                    case 8:
                        assignIssue();
                        break;

                    case 9:
                        addComment();
                        break;

                    case 10:
                        viewActivityHistory();
                        break;

                    case 11:
                        manageUsers();
                        break;

                    case 12:
                        logout();
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Select 1-12."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Operation failed: "
                                + e.getMessage()
                );
            }
        }
    }


    // ============================================================
    // DASHBOARD STATISTICS
    // ============================================================

    private static void showStatistics() {

        int total = system.getIssues().size();

        int open = 0;
        int inProgress = 0;
        int resolved = 0;
        int closed = 0;

        for (Issue issue : system.getIssues()) {

            switch (issue.getStatus()) {

                case OPEN:
                    open++;
                    break;

                case IN_PROGRESS:
                    inProgress++;
                    break;

                case RESOLVED:
                    resolved++;
                    break;

                case CLOSED:
                    closed++;
                    break;
            }
        }

        System.out.println(
                "\n========== DASHBOARD STATISTICS =========="
        );

        System.out.println(
                "Total Issues : " + total
        );

        System.out.println(
                "Open         : " + open
        );

        System.out.println(
                "In Progress  : " + inProgress
        );

        System.out.println(
                "Resolved     : " + resolved
        );

        System.out.println(
                "Closed       : " + closed
        );

        System.out.println(
                "Users        : "
                        + system.getUsers().size()
        );
    }


    // ============================================================
    // VIEW ALL ISSUES
    // ============================================================

    private static void viewAllIssues() {

        System.out.println(
                "\n========== ALL ISSUES =========="
        );

        if (system.getIssues().isEmpty()) {

            System.out.println(
                    "No issues found."
            );

            return;
        }

        for (Issue issue : system.getIssues()) {

            printIssueSummary(issue);
        }
    }


    private static void printIssueSummary(
            Issue issue
    ) {

        System.out.println(
                "#" + issue.getId()
                        + " | "
                        + issue.getTitle()
                        + " | Priority: "
                        + issue.getPriority()
                        + " | Status: "
                        + issue.getStatus()
                        + " | Reporter: "
                        + issue.getReporterEmail()
                        + " | Assignee: "
                        + (
                        issue.getAssigneeEmail() == null
                                ? "Unassigned"
                                : issue.getAssigneeEmail()
                )
        );
    }


    // ============================================================
    // CREATE ISSUE
    // ============================================================

    private static void createIssue() {

        System.out.println(
                "\n========== CREATE NEW ISSUE =========="
        );

        String title = InputUtil.readNonEmpty(
                scanner,
                "Title: "
        );

        String description = InputUtil.readNonEmpty(
                scanner,
                "Description: "
        );

        Priority priority = InputUtil.readEnum(
                scanner,
                "Priority (LOW/MEDIUM/HIGH/CRITICAL): ",
                Priority.class
        );

        Issue issue = system.createIssue(
                title,
                description,
                priority,
                loggedInUser.getEmail()
        );

        FileStorageService.save(
                system,
                DATA_FILE
        );

        System.out.println(
                "Issue created successfully."
        );

        System.out.println(
                "Issue ID: "
                        + issue.getId()
        );
    }


    // ============================================================
    // VIEW ISSUE DETAILS
    // ============================================================

    private static void viewIssueDetails() {

        int id = InputUtil.readInt(
                scanner,
                "Enter issue ID: "
        );

        Issue issue = system.findIssue(id);

        if (issue == null) {

            System.out.println(
                    "Issue not found."
            );

            return;
        }

        printIssueDetails(issue);
    }


    private static void printIssueDetails(
            Issue issue
    ) {

        System.out.println(
                "\n========== ISSUE DETAILS =========="
        );

        System.out.println(
                "ID          : "
                        + issue.getId()
        );

        System.out.println(
                "Title       : "
                        + issue.getTitle()
        );

        System.out.println(
                "Description : "
                        + issue.getDescription()
        );

        System.out.println(
                "Priority    : "
                        + issue.getPriority()
        );

        System.out.println(
                "Status      : "
                        + issue.getStatus()
        );

        System.out.println(
                "Reporter    : "
                        + issue.getReporterEmail()
        );

        System.out.println(
                "Assignee    : "
                        + (
                        issue.getAssigneeEmail() == null
                                ? "Unassigned"
                                : issue.getAssigneeEmail()
                )
        );

        System.out.println(
                "Created     : "
                        + issue.getCreatedAt()
        );

        System.out.println(
                "Updated     : "
                        + issue.getUpdatedAt()
        );

        System.out.println(
                "Comments    : "
                        + issue.getComments().size()
        );

        for (Comment comment : issue.getComments()) {

            System.out.println(
                    "  - "
                            + comment.getAuthorEmail()
                            + " ["
                            + comment.getTime()
                            + "]: "
                            + comment.getText()
            );
        }
    }


    // ============================================================
    // SEARCH ISSUES
    // ============================================================

    private static void searchIssues() {

        String keyword = InputUtil.readNonEmpty(
                scanner,
                "Enter title/description keyword: "
        ).toLowerCase();

        boolean found = false;

        for (Issue issue : system.getIssues()) {

            if (
                    issue.getTitle()
                            .toLowerCase()
                            .contains(keyword)

                            ||

                    issue.getDescription()
                            .toLowerCase()
                            .contains(keyword)
            ) {

                printIssueSummary(issue);

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No matching issues found."
            );
        }
    }


    // ============================================================
    // FILTER ISSUES
    // ============================================================

    private static void filterIssues() {

        System.out.println(
                "\n1. Filter by status"
        );

        System.out.println(
                "2. Filter by priority"
        );

        int choice = InputUtil.readInt(
                scanner,
                "Enter choice: "
        );

        boolean found = false;

        if (choice == 1) {

            Status status = InputUtil.readEnum(
                    scanner,
                    "Status (OPEN/IN_PROGRESS/RESOLVED/CLOSED): ",
                    Status.class
            );

            for (Issue issue : system.getIssues()) {

                if (issue.getStatus() == status) {

                    printIssueSummary(issue);

                    found = true;
                }
            }

        } else if (choice == 2) {

            Priority priority = InputUtil.readEnum(
                    scanner,
                    "Priority (LOW/MEDIUM/HIGH/CRITICAL): ",
                    Priority.class
            );

            for (Issue issue : system.getIssues()) {

                if (issue.getPriority() == priority) {

                    printIssueSummary(issue);

                    found = true;
                }
            }

        } else {

            System.out.println(
                    "Invalid choice."
            );

            return;
        }

        if (!found) {

            System.out.println(
                    "No matching issues found."
            );
        }
    }


    // ============================================================
    // UPDATE STATUS
    // ============================================================

    private static void updateIssueStatus() {

        int id = InputUtil.readInt(
                scanner,
                "Issue ID: "
        );

        Issue issue = system.findIssue(id);

        if (issue == null) {

            System.out.println(
                    "Issue not found."
            );

            return;
        }

        Status newStatus = InputUtil.readEnum(
                scanner,
                "New status (OPEN/IN_PROGRESS/RESOLVED/CLOSED): ",
                Status.class
        );

        Status oldStatus = issue.getStatus();

        issue.setStatus(newStatus);

        issue.touch();

        system.addActivity(
                loggedInUser.getEmail(),
                "Changed issue #"
                        + id
                        + " status from "
                        + oldStatus
                        + " to "
                        + newStatus
        );

        FileStorageService.save(
                system,
                DATA_FILE
        );

        System.out.println(
                "Issue status updated successfully."
        );
    }


    // ============================================================
    // ASSIGN ISSUE
    // ============================================================

    private static void assignIssue() {

        if (loggedInUser.getRole() == Role.REPORTER) {

            System.out.println(
                    "Only administrators/developers can assign issues."
            );

            return;
        }

        int id = InputUtil.readInt(
                scanner,
                "Issue ID: "
        );

        Issue issue = system.findIssue(id);

        if (issue == null) {

            System.out.println(
                    "Issue not found."
            );

            return;
        }

        System.out.println(
                "\nAvailable developers:"
        );

        for (User user : system.getUsers()) {

            if (user.getRole() == Role.DEVELOPER) {

                System.out.println(
                        user.getEmail()
                                + " - "
                                + user.getName()
                );
            }
        }

        String email = InputUtil.readNonEmpty(
                scanner,
                "Developer email: "
        );

        User developer = system.findUser(email);

        if (
                developer == null
                        ||
                developer.getRole() != Role.DEVELOPER
        ) {

            System.out.println(
                    "Developer not found."
            );

            return;
        }

        issue.setAssigneeEmail(email);

        issue.touch();

        system.addActivity(
                loggedInUser.getEmail(),
                "Assigned issue #"
                        + id
                        + " to "
                        + email
        );

        FileStorageService.save(
                system,
                DATA_FILE
        );

        System.out.println(
                "Issue assigned successfully."
        );
    }


    // ============================================================
    // ADD COMMENT
    // ============================================================

    private static void addComment() {

        int id = InputUtil.readInt(
                scanner,
                "Issue ID: "
        );

        Issue issue = system.findIssue(id);

        if (issue == null) {

            System.out.println(
                    "Issue not found."
            );

            return;
        }

        String text = InputUtil.readNonEmpty(
                scanner,
                "Comment: "
        );

        issue.getComments().add(
                new Comment(
                        loggedInUser.getEmail(),
                        text
                )
        );

        issue.touch();

        system.addActivity(
                loggedInUser.getEmail(),
                "Added a comment to issue #"
                        + id
        );

        FileStorageService.save(
                system,
                DATA_FILE
        );

        System.out.println(
                "Comment added successfully."
        );
    }


    // ============================================================
    // ACTIVITY HISTORY
    // ============================================================

    private static void viewActivityHistory() {

        System.out.println(
                "\n========== ACTIVITY HISTORY =========="
        );

        if (system.getActivities().isEmpty()) {

            System.out.println(
                    "No activity recorded."
            );

            return;
        }

        for (
                Activity activity :
                system.getActivities()
        ) {

            System.out.println(
                    activity.getTime()
                            + " | "
                            + activity.getUserEmail()
                            + " | "
                            + activity.getAction()
            );
        }
    }


    // ============================================================
    // MANAGE USERS
    // ============================================================

    private static void manageUsers() {

        if (loggedInUser.getRole() != Role.ADMIN) {

            System.out.println(
                    "Only administrators can manage users."
            );

            return;
        }

        System.out.println(
                "\n========== MANAGE USERS =========="
        );

        for (User user : system.getUsers()) {

            System.out.println(
                    user.getName()
                            + " | "
                            + user.getEmail()
                            + " | "
                            + user.getRole()
            );
        }
    }


    // ============================================================
    // LOGOUT
    // ============================================================

    private static void logout() {

        System.out.println(
                "Logged out successfully."
        );

        loggedInUser = null;
    }


    // ============================================================
    // ENUMS
    // ============================================================

    enum Role {

        ADMIN,
        DEVELOPER,
        REPORTER
    }


    enum Status {

        OPEN,
        IN_PROGRESS,
        RESOLVED,
        CLOSED
    }


    enum Priority {

        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }


    // ============================================================
    // USER CLASS
    // ============================================================

    static class User implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String name;
        private final String email;
        private final String password;
        private final Role role;

        User(
                String name,
                String email,
                String password,
                Role role
        ) {

            this.name = name;
            this.email = email;
            this.password = password;
            this.role = role;
        }

        String getName() {
            return name;
        }

        String getEmail() {
            return email;
        }

        String getPassword() {
            return password;
        }

        Role getRole() {
            return role;
        }
    }


    // ============================================================
    // ISSUE CLASS
    // ============================================================

    static class Issue implements Serializable {

        private static final long serialVersionUID = 1L;

        private static int nextId = 1;

        private final int id;

        private final String title;

        private final String description;

        private final String reporterEmail;

        private final Priority priority;

        private Status status;

        private String assigneeEmail;

        private final String createdAt;

        private String updatedAt;

        private final List<Comment> comments =
                new ArrayList<>();


        Issue(
                String title,
                String description,
                Priority priority,
                String reporterEmail
        ) {

            this.id = nextId++;

            this.title = title;

            this.description = description;

            this.priority = priority;

            this.reporterEmail = reporterEmail;

            this.status = Status.OPEN;

            this.createdAt = now();

            this.updatedAt = createdAt;
        }


        int getId() {
            return id;
        }

        String getTitle() {
            return title;
        }

        String getDescription() {
            return description;
        }

        String getReporterEmail() {
            return reporterEmail;
        }

        Priority getPriority() {
            return priority;
        }

        Status getStatus() {
            return status;
        }

        void setStatus(Status status) {
            this.status = status;
        }

        String getAssigneeEmail() {
            return assigneeEmail;
        }

        void setAssigneeEmail(String email) {
            this.assigneeEmail = email;
        }

        String getCreatedAt() {
            return createdAt;
        }

        String getUpdatedAt() {
            return updatedAt;
        }

        List<Comment> getComments() {
            return comments;
        }

        void touch() {
            updatedAt = now();
        }
    }


    // ============================================================
    // COMMENT CLASS
    // ============================================================

    static class Comment implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String authorEmail;

        private final String text;

        private final String time;


        Comment(
                String authorEmail,
                String text
        ) {

            this.authorEmail = authorEmail;

            this.text = text;

            this.time = now();
        }


        String getAuthorEmail() {
            return authorEmail;
        }

        String getText() {
            return text;
        }

        String getTime() {
            return time;
        }
    }


    // ============================================================
    // ACTIVITY CLASS
    // ============================================================

    static class Activity implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String userEmail;

        private final String action;

        private final String time;


        Activity(
                String userEmail,
                String action
        ) {

            this.userEmail = userEmail;

            this.action = action;

            this.time = now();
        }


        String getUserEmail() {
            return userEmail;
        }

        String getAction() {
            return action;
        }

        String getTime() {
            return time;
        }
    }


    // ============================================================
    // BUG TRACKING SYSTEM
    // ============================================================

    static class BugTrackingSystem
            implements Serializable {

        private static final long serialVersionUID = 1L;

        private final List<User> users =
                new ArrayList<>();

        private final List<Issue> issues =
                new ArrayList<>();

        private final List<Activity> activities =
                new ArrayList<>();


        void initializeDefaultData() {

            users.add(
                    new User(
                            "Administrator",
                            "admin@bugtrack.com",
                            "admin123",
                            Role.ADMIN
                    )
            );

            users.add(
                    new User(
                            "Developer",
                            "developer@bugtrack.com",
                            "dev123",
                            Role.DEVELOPER
                    )
            );

            users.add(
                    new User(
                            "Reporter",
                            "reporter@bugtrack.com",
                            "reporter123",
                            Role.REPORTER
                    )
            );

            addActivity(
                    "system",
                    "Default users created"
            );
        }


        User login(
                String email,
                String password
        ) {

            for (User user : users) {

                if (
                        user.getEmail()
                                .equalsIgnoreCase(email)

                                &&

                        user.getPassword()
                                .equals(password)
                ) {

                    return user;
                }
            }

            return null;
        }


        User registerUser(
                String name,
                String email,
                String password,
                Role role
        ) throws AuthenticationException {

            if (findUser(email) != null) {

                throw new AuthenticationException(
                        "Email is already registered."
                );
            }

            User user =
                    new User(
                            name,
                            email,
                            password,
                            role
                    );

            users.add(user);

            addActivity(
                    email,
                    "Registered as " + role
            );

            return user;
        }


        User findUser(String email) {

            for (User user : users) {

                if (
                        user.getEmail()
                                .equalsIgnoreCase(email)
                ) {

                    return user;
                }
            }

            return null;
        }


        Issue createIssue(
                String title,
                String description,
                Priority priority,
                String reporterEmail
        ) {

            Issue issue =
                    new Issue(
                            title,
                            description,
                            priority,
                            reporterEmail
                    );

            issues.add(issue);

            addActivity(
                    reporterEmail,
                    "Created issue #"
                            + issue.getId()
            );

            return issue;
        }


        Issue findIssue(int id) {

            for (Issue issue : issues) {

                if (issue.getId() == id) {

                    return issue;
                }
            }

            return null;
        }


        void addActivity(
                String userEmail,
                String action
        ) {

            activities.add(
                    new Activity(
                            userEmail,
                            action
                    )
            );
        }


        List<User> getUsers() {
            return users;
        }


        List<Issue> getIssues() {
            return issues;
        }


        List<Activity> getActivities() {
            return activities;
        }
    }


    // ============================================================
    // CUSTOM EXCEPTION
    // ============================================================

    static class AuthenticationException
            extends Exception {

        private static final long serialVersionUID = 1L;

        AuthenticationException(
                String message
        ) {

            super(message);
        }
    }


    // ============================================================
    // FILE STORAGE SERVICE
    // ============================================================

    static class FileStorageService {

        static void save(
                BugTrackingSystem system,
                String fileName
        ) {

            try (
                    ObjectOutputStream out =
                            new ObjectOutputStream(
                                    new FileOutputStream(fileName)
                            )
            ) {

                out.writeObject(system);

            } catch (IOException e) {

                System.out.println(
                        "Warning: Could not save data: "
                                + e.getMessage()
                );
            }
        }


        static BugTrackingSystem load(
                String fileName
        ) {

            File file =
                    new File(fileName);

            if (!file.exists()) {

                return null;
            }

            try (
                    ObjectInputStream in =
                            new ObjectInputStream(
                                    new FileInputStream(file)
                            )
            ) {

                return (BugTrackingSystem)
                        in.readObject();

            } catch (Exception e) {

                System.out.println(
                        "Existing data file could not be loaded."
                );

                System.out.println(
                        "Starting with fresh data."
                );

                return null;
            }
        }
    }


    // ============================================================
    // INPUT UTILITY
    // ============================================================

    static class InputUtil {

        static String readNonEmpty(
                Scanner scanner,
                String message
        ) {

            while (true) {

                System.out.print(message);

                String value =
                        scanner.nextLine().trim();

                if (!value.isEmpty()) {

                    return value;
                }

                System.out.println(
                        "Input cannot be empty."
                );
            }
        }


        static int readInt(
                Scanner scanner,
                String message
        ) {

            while (true) {

                System.out.print(message);

                String value =
                        scanner.nextLine().trim();

                try {

                    return Integer.parseInt(value);

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Please enter a valid number."
                    );
                }
            }
        }


        static <E extends Enum<E>> E readEnum(
                Scanner scanner,
                String message,
                Class<E> type
        ) {

            while (true) {

                System.out.print(message);

                String value =
                        scanner.nextLine()
                                .trim()
                                .toUpperCase();

                try {

                    return Enum.valueOf(
                            type,
                            value
                    );

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Invalid value."
                    );

                    System.out.println(
                            "Allowed values: "
                                    + Arrays.toString(
                                    type.getEnumConstants()
                            )
                    );
                }
            }
        }
    }


    // ============================================================
    // CURRENT DATE/TIME
    // ============================================================

    private static String now() {

        return LocalDateTime.now()
                .format(FORMAT);
    }
}