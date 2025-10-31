# 🧩 Skill-Barter

A community-driven platform that enables users to **exchange skills** — learn something new by teaching what you already know. Skill-Swap connects people with complementary abilities to create a space where everyone can teach, learn, and grow together.

---

## 📘 Table of Contents
- [About](#about)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [Usage](#usage)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## 🧠 About

**Skill-Swap** is a web application that allows users to **swap their skills** with others.  
For example — teach coding to someone who can teach you guitar 🎸 or photography 📸.  
The platform promotes peer-to-peer learning and encourages collaboration through shared interests.

---

## ✨ Features

- 👤 User registration and authentication  
- 🧩 Create skill listings — what you can teach & what you want to learn  
- 🔍 Browse and search available skill offers  
- 🤝 Match users based on complementary skills  
- 💬 In-app communication between matched users  
- ⭐ Ratings and reviews after completed swaps  
- 📊 Dashboard to manage your skill offers and learning requests  

---

## 🚀 Getting Started

### 🔧 Prerequisites

Make sure you have the following installed:
- **Java JDK 17+**
- **MySQL** (or any SQL database)
- **JDBC Driver** for database connectivity
- **IDE** such as IntelliJ IDEA / Eclipse / NetBeans

### ⚙️ Installation

1.  Clone the repository:
    ```bash
    git clone [https://github.com/Tanmay2006/Skill-Swap.git](https://github.com/Tanmay2006/Skill-Swap.git)
    ```
2.  Navigate to the project folder:
    ```bash
    cd Skill-Swap
    ```
3.  Open the project in your preferred Java IDE.

### 🧩 Configuration

1.  Set up a database (e.g., MySQL):
    ```sql
    CREATE DATABASE skillswap;
    ```
2.  Update your JDBC connection details in the source code:
    ```java
    String url = "jdbc:mysql://localhost:3306/skillswap";
    String user = "root";
    String password = "your_password";
    ```
3.  Run SQL scripts (if provided) to create required tables.

### ▶️ Running the Application

1.  Compile and run the project from your IDE.
2.  The main entry point is likely the file containing:
    ```java
    public static void main(String[] args)
    ```
3.  The GUI (built using Java Swing) will launch automatically.

---

## 💡 Usage

Sign Up / Log In to your account.

Add your skills — specify what you can teach and what you want to learn.

Browse available users who match your interests.

Connect and start exchanging skills!

After completing a session, leave a review for your partner.

---

## 🛠️ Technology Stack

| Component | Technology |
| :--- | :--- |
| Frontend | Java Swing |
| Backend | Java (Core + JDBC) |
| Database | MySQL |
| IDE | IntelliJ / Eclipse |
| Version Control | Git & GitHub |

---

## 🧩 Architecture

Skill-Swap follows a layered structure:

* **Frontend (Swing UI)** — User interface for registration, login, and dashboard.
* **Backend (Java Logic)** — Handles business logic, matching, and validations.
* **Database (MySQL)** — Stores users, skills, matches, and reviews.
* **JDBC Integration** — Connects Java app to the MySQL database securely.

---

## 🤝 Contributing

Contributions are welcome! 🎉
If you want to improve Skill-Swap:

* Fork the repository
* Create a branch for your feature (`git checkout -b feature/YourFeature`)
* Commit your changes (`git commit -m 'Add new feature'`)
* Push to your branch (`git push origin feature/YourFeature`)
* Open a Pull Request

Please maintain consistent code style and include clear commit messages.

---

## 📜 License

This project is licensed under the MIT License.
See the `LICENSE` file for details.

---

## 📫 Contact

Created by Tanmay Agrawal

GitHub: @Tanmay2006
