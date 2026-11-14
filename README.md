# Lab 7 – Database Connection with JDBC in Java Swing

A Java Swing desktop application that performs full **CRUD** (Create, Read,
Update, Delete) operations on a **MySQL** database over **JDBC**.

The app has two views hosted in a single window (swapped with a `CardLayout`):

| View | Class | What it does |
|------|-------|--------------|
| **Form** (Create) | `ui.FormJPanel` | Add a new student (name, age, college, hobbies) with input validation, then insert into the DB. |
| **View** (Read / Update / Delete) | `ui.ViewJPanel` | List every student in a **`JTable`**; select a row to **Edit**, **Submit** the update, or **Delete** it. |

---

## Tech stack
- **Java** (Swing GUI, built with the NetBeans GUI builder – `.form` files included)
- **JDBC** for database connectivity
- **MySQL 8** as the RDBMS
- **MySQL Connector/J 9.1.0** (bundled in [`lib/`](lib) and already on the project classpath)

## Project structure
```
Lab 7/
├── src/
│   ├── model/User.java                 # POJO for one student record
│   ├── utility/DatabaseConnector.java  # all JDBC + CRUD SQL lives here
│   └── ui/
│       ├── MainJFrame.java/.form       # window + Form/View navigation (CardLayout)
│       ├── FormJPanel.java/.form       # Create view
│       └── ViewJPanel.java/.form       # Read / Update / Delete view (JTable)
├── lib/mysql-connector-j-9.1.0.jar     # JDBC driver
├── DatabaseQueries.sql                 # run this once to create the DB + table
├── Screenshots/                        # screenshots of the running app
├── build.xml, manifest.mf, nbproject/  # NetBeans / Ant project files
└── README.md
```

---

## Setup & run

### 1. Create the database
Open **MySQL Workbench** (or the `mysql` client) and run the script
[`DatabaseQueries.sql`](DatabaseQueries.sql). It creates the `customerdb`
database, the `student` table, and a couple of sample rows.

### 2. Point the app at your MySQL server
Open `src/utility/DatabaseConnector.java` and set the three constants to match
**your** MySQL instance:

```java
static final String DB_URL      = "jdbc:mysql://localhost:3306/customerdb";
static final String DB_USERNAME = "root";
static final String DB_PASSWORD = "my-secret-pw";   // <-- change to your password
```

> **Connection tip:** if MySQL 8 rejects the connection over the default secure
> channel, append parameters to the URL, e.g.
> `jdbc:mysql://localhost:3306/customerdb?useSSL=false&allowPublicKeyRetrieval=true`

### 3. Open & run in NetBeans
1. **File → Open Project** → select this `Lab 7` folder.
2. The Connector/J JAR in `lib/` is already referenced, so no extra setup is needed.
3. Right-click the project → **Run** (main class is **`ui.MainJFrame`**).

### Run from the command line (optional)
```bash
# compile
javac -cp "lib/mysql-connector-j-9.1.0.jar" -d build \
    src/model/*.java src/utility/*.java src/ui/*.java
# run (note the classpath separator: ';' on Windows, ':' on macOS/Linux)
java  -cp "build;lib/mysql-connector-j-9.1.0.jar" ui.MainJFrame
```

---

## How to use
1. **Form** view – fill in Name, Age, pick a College, add Hobbies → **Submit**.
   - Blank name, bad age, or no college selected each show a clear warning dialog.
   - On success the app pops a confirmation and jumps to the **View**.
2. **View** view – the `JTable` lists all students from the database.
   - Click a row, then **Edit** to load it into the fields (fields unlock).
   - Change values and click **Submit Edit** to `UPDATE` the row.
   - Click a row and **Delete** to remove it.
   - Use the top **Form / View** buttons to switch screens any time.

## CRUD → SQL mapping (`utility.DatabaseConnector`)
| Action | Method | SQL |
|--------|--------|-----|
| Create | `addUser` | `INSERT INTO student ...` |
| Read   | `getUsers` | `SELECT * FROM student` |
| Update | `updateUser` | `UPDATE student SET ... WHERE student_id = ?` |
| Delete | `deleteUser` | `DELETE FROM student WHERE student_id = ?` |
