# Lab 7 – Database Connection with JDBC in Java Swing

A Java Swing desktop application that performs full **CRUD** (Create, Read,
Update, Delete) operations on a **MySQL** database over **JDBC**.

The app has two views hosted in a single window (swapped with a `CardLayout`):

| View | Class | What it does |
|------|-------|--------------|
| **Form** (Create) | `ui.FormJPanel` | Register a new student with **all fields** (see below), validate the input, and `INSERT` into the DB. |
| **View** (Read / Update / Delete) | `ui.ViewJPanel` | List every student in a **`JTable`** (with a photo thumbnail); select a row to **Edit**, **Submit** the update, or **Delete** it. |

### Student fields captured
First Name, Last Name, **Gender**, **Date of Birth** (calendar picker), **Age**
(auto-calculated from the DOB), **Phone** (masked `###-###-####`), **Email**
(validated), College, **Experience**, Hobbies, and a **Photo** (uploaded from
disk and stored in the database as a `LONGBLOB`).

---

## Tech stack
- **Java** (Swing GUI)
- **JDBC** for database connectivity
- **MySQL 8+** as the RDBMS
- **MySQL Connector/J 9.1.0** – JDBC driver (bundled in [`lib/`](lib))
- **JCalendar 0.8** (`de.wannawork.jcalendar.JCalendarComboBox`) – the Date-of-Birth
  calendar component (bundled in [`lib/`](lib))

## Project structure
```
Lab 7/
├── src/
│   ├── model/User.java                 # POJO for one student record (incl. byte[] photo)
│   ├── utility/DatabaseConnector.java  # all JDBC + CRUD SQL lives here
│   └── ui/
│       ├── MainJFrame.java/.form       # window + Form/View navigation (CardLayout)
│       ├── FormJPanel.java             # Create view (all fields + validation)
│       └── ViewJPanel.java             # Read / Update / Delete view (JTable)
├── lib/mysql-connector-j-9.1.0.jar     # JDBC driver
├── lib/jcalendar-0.8.jar               # calendar (date of birth) component
├── DatabaseQueries.sql                 # run this once to create the DB + table
├── Screenshots/                        # screenshots of the running app
├── build.xml, manifest.mf, nbproject/  # NetBeans / Ant project files
└── README.md
```

> **Note:** `FormJPanel` and `ViewJPanel` are now authored directly in Java (no
> `.form` GUI-builder file) because they use custom components such as the
> calendar picker and the photo panel. `MainJFrame` still has its `.form`.

---

## Setup & run

### 1. Create the database
Open **MySQL Workbench** (or the `mysql` client) and run the script
[`DatabaseQueries.sql`](DatabaseQueries.sql). It creates the `customerdb`
database and the `student` table (with all the new columns) and inserts a couple
of sample rows.

> If you ran an **earlier** version of this lab, the old `student` table has
> fewer columns. The script begins with `DROP TABLE IF EXISTS student;` so it
> rebuilds the table with the new schema.

### 2. Point the app at your MySQL server
Open `src/utility/DatabaseConnector.java` and set the three constants to match
**your** MySQL instance:

```java
static final String DB_URL =
    "jdbc:mysql://localhost:3306/customerdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
static final String DB_USERNAME = "root";
static final String DB_PASSWORD = "my-secret-pw";   // <-- change to your password
```

### 3. Open & run in NetBeans
1. **File → Open Project** → select this `Lab 7` folder.
2. Both JARs in `lib/` (Connector/J **and** JCalendar) are already referenced on
   the project classpath, so no extra setup is needed.
3. Right-click the project → **Run** (main class is **`ui.MainJFrame`**).

### Run from the command line (optional)
```bash
# compile (note the classpath separator: ';' on Windows, ':' on macOS/Linux)
javac -cp "lib/mysql-connector-j-9.1.0.jar;lib/jcalendar-0.8.jar" -d build \
    src/model/*.java src/utility/*.java src/ui/*.java
# run
java  -cp "build;lib/mysql-connector-j-9.1.0.jar;lib/jcalendar-0.8.jar" ui.MainJFrame
```

---

## How to use
1. **Form** view – fill in every field, pick a **Date of Birth** on the calendar
   (the **Age** fills in automatically), type a phone number in the
   `123-456-7890` mask, enter a valid email, and click **Upload Photo** to attach
   a picture → **Submit**.
   - Missing name, bad phone/email, or no date of birth each show a clear warning.
   - On success the app shows a confirmation (with your photo) and jumps to **View**.
2. **View** view – the `JTable` lists all students from the database, including a
   photo thumbnail.
   - Click a row, then **Edit** to load it into the fields (fields unlock).
   - Change values (optionally **Change Photo**) and click **Submit Edit** to `UPDATE`.
   - Click a row and **Delete** to remove it.
   - Use the top **Form / View** buttons to switch screens any time.

## CRUD → SQL mapping (`utility.DatabaseConnector`)
| Action | Method | SQL |
|--------|--------|-----|
| Create | `addUser` | `INSERT INTO student (...) VALUES (...)` |
| Read   | `getUsers` | `SELECT * FROM student ORDER BY student_id` |
| Update | `updateUser` | `UPDATE student SET ... WHERE student_id = ?` |
| Delete | `deleteUser` | `DELETE FROM student WHERE student_id = ?` |

The photo column is written with `PreparedStatement.setBytes(...)` and read back
with `ResultSet.getBytes("photo")`, then shown as an `ImageIcon`.
