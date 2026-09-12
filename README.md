# HospitaLine

A console-based hospital management system built in Java, developed as an academic project. It manages user authentication, patient and doctor registration, medical appointments, and medicine inventory, combining a relational database with XML data exchange.

## Features

- **User management**: registration and login for Doctors and Patients, with role-based access.
- **Appointments**: create, query, and manage medical appointments.
- **Medical records**: store and retrieve patient medical history.
- **Medicine inventory**: track medicine supplies and stock.
- **XML import/export**: appointment and medicine data can be exported to XML and transformed via XSLT (see `xml/`).

## Tech Stack

- **Language**: Java 17
- **Persistence**: JPA (EclipseLink) + raw JDBC, backed by SQLite
- **Build tool**: Maven
- **Data exchange**: XML / XSLT, validated against DTDs
- **Security**: Spring Security Crypto (password hashing)

## Project Structure

```
src/
├── hospital/
│   ├── ui/        → Menu.java (application entry point / console UI)
│   ├── jdbc/       → JDBC-based data access layer
│   ├── jpa/        → JPA-based user management
│   ├── xml/        → XML import/export logic
│   └── ifaces/     → Interfaces for managers
├── db/pojos/       → Entity classes (User, Patient, Doctor, Appointment, MedicalRecord, MedicineSupply, Role)
└── META-INF/       → JPA persistence configuration
```


Additional design documentation (ER diagram, UML class diagram, use case diagram, UI mockups, and requirements traceability matrix) is available under `Tables and Diagrams/`.

## Getting Started

### Prerequisites
- Java 17+
- Maven

### Run
```bash
mvn compile
mvn exec:java -Dexec.mainClass="hospital.ui.Menu"
```

The application connects to a local SQLite database (`Database1.db`) included in the repo with sample data for demonstration purposes.

## Documentation

- `Manuals/System Manual.pdf` — technical/system documentation
- `Manuals/Users Manual.pdf` — end-user guide
- `Systems intent and features/Summary of Systems Intent.pdf` — project overview and scope

## Authors

- Diana Bravo
- Teresa Rodríguez
- Guillermo Lozano

Team project developed for the Databases course, Biomedical Engineering degree, CEU San Pablo University.
