# College Student Portal

A combined capstone web application that unifies three mini-projects — **Authentication**, **Student CRUD Management**, and **File/Media Handling** — into a single role-based portal built with Java Servlets, JSP, JDBC, and MySQL.

Students can register, log in, manage their own academic profile, and upload a profile photo. Administrators can search, sort, paginate, edit, and delete any student record, and promote or demote user roles.

---

## Features

### Authentication
- Registration with hashed passwords (BCrypt)
- Session-based login with session-fixation protection
- "Remember me" via a persistent, HttpOnly token cookie
- Logout clears session and remember-me token

### Student CRUD (Create, Read, Update, Delete)
- Student profile auto-created on registration
- Students view and edit their own profile
- Admins view every student in a searchable, sortable, paginated table
- Admins can edit any student's full record (including roll number) or delete a record
- Search by name, roll number, or department
- Sortable columns (Name, Roll Number, Department, Semester), SQL-injection-safe via a column allow-list
- Pagination via SQL `LIMIT`/`OFFSET`
- Admin can promote/demote a user's role (self-demotion blocked for safety)

### File Management
- Profile photo upload (JPEG, PNG, WebP), capped at 2MB
- **Real MIME validation** — file type is verified by inspecting binary file-signature bytes, not by trusting the filename extension or browser-supplied `Content-Type`
- Safe storage: files saved outside the web root with randomly generated filenames (no path traversal, no filename collisions)
- Photos served through a dedicated servlet, never as a direct file path
- Replace/remove photo, with automatic cleanup of the previous file

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| Web layer | Jakarta Servlets 6.0 |
| View layer | JSP + JSTL |
| Data access | JDBC (`PreparedStatement`) |
| Database | MySQL 8.0 |
| Build tool | Maven |
| Server | Apache Tomcat 10.1 |
| IDE | Eclipse IDE |

See [`requirements.txt`](./requirements.txt) for the full dependency and version list, and [`MYSQL_SETUP.md`](./MYSQL_SETUP.md) for database setup instructions.

---

## Project Structure

```
com.collegeportal
├── controller/    Servlets — one per feature/action
├── dao/           UserDAO, StudentDAO — all JDBC/SQL logic
├── model/         User, Student — plain data objects
├── filter/        AuthenticationFilter
└── util/          Constants, DBConnectionUtil, PasswordUtil,
                    TokenUtil, FileStorageUtil

src/main/webapp/
├── *.jsp                    Views (login, register, dashboard, profile, admin pages)
├── navbar.jsp                Shared navigation include
├── resources/css/portal.css  Shared design system
└── WEB-INF/web.xml           Welcome file + deployment descriptor
```

---

## Getting Started

### Prerequisites
- JDK 17+
- Apache Tomcat 10.1+ (Jakarta EE / Servlet 6.0)
- MySQL 8.0+
- Maven
- Eclipse IDE (or any IDE with Tomcat + Maven support)

### 1. Clone and import
```bash
git clone <your-repo-url>
```
Import as an existing Maven project into Eclipse.

### 2. Set up the database
Follow [`MYSQL_SETUP.md`](./MYSQL_SETUP.md) to create the database and tables.

### 3. Configure the database connection
Update the connection details in `DBConnectionUtil.java` (URL, username, password) to match your local MySQL instance.

### 4. Create the upload directory
The file-upload feature stores photos on disk. Create the folder referenced in `Constants.java`:
```
UPLOAD_DIR = "C:/collegeportal-uploads/students/"
```
Adjust this path for your OS/environment before running the project.

### 5. Run
Deploy to Tomcat from Eclipse ("Run on Server"), or build and deploy the WAR manually. Once running, visit:
```
http://localhost:9090/college-student-portal
```
The app redirects to the login page automatically (configured via `web.xml`'s welcome file).

### 6. Create an admin account
All new registrations default to the `STUDENT` role. To test admin features, promote an account manually once:
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
```
Log out and back in for the role change to take effect in your session. From then on, admins can promote/demote other users directly from the UI.

---

## Security Notes

- All SQL queries use `PreparedStatement` with bound parameters — no string-concatenated queries against user input.
- The one exception (`ORDER BY` column names, which SQL doesn't allow as bind parameters) is restricted to a fixed allow-list of known-safe column names.
- Passwords are hashed with BCrypt; plaintext is never stored.
- Uploaded files are renamed to a random UUID and stored outside the web root, so they can never be executed as server code.
- File type is validated by binary signature, not filename or client-supplied metadata.
- Delete and role-change actions require `POST`, not `GET`.

---

## License

Built as an academic capstone project. Add a license of your choice if publishing publicly.
