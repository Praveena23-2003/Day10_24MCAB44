# 🎓 Elective Course Registration System using Java, Vert.x & MongoDB

This project is a backend system for elective course registration built using **Java**, **Vert.x**, **MongoDB**, and **SMTP email service**. It allows students to register, receive a random password via email, log in, view available courses, and enroll in one.

---

## 🔧 Tech Stack

- **Java 17**
- **Vert.x (Web Framework)**
- **MongoDB (NoSQL Database)**
- **Jakarta Mail (SMTP Email Sending)**
- **Postman / Console App for Testing**

---

## 📌 Features

✅ Student Registration  
✅ Random Password Generation  
✅ Email Password via SMTP  
✅ Student Login  
✅ View Available Courses  
✅ Enroll in a Course  
✅ Seat Count Auto-Decreases  
✅ APIs Tested via Console (No Postman Needed)  
✅ MongoDB Integration

---

## 📂 Project Structure

src/
├── main/
│ ├── java/org/example/
│ │ ├── Main.java # Vert.x server with API routes
│ │ ├── StudentHandler.java # Handles student registration, login, enrollment
│ │ ├── CourseHandler.java # Returns available courses
│ │ ├── EmailUtil.java # SMTP email sender
│ │ ├── MongoDBUtil.java # MongoDB collection access
│ │ └── ClientConsoleApp.java # Interactive console-based API tester

## 🚀 How to Run

### 1️⃣ Prerequisites

- Java 17+
- MongoDB running locally on port `27017`
- Maven installed
- Gmail account (for sending SMTP emails)

### 2️⃣ Clone the Repository


git clone https://github.com/Praveena23-2003/Day10_24MCAB44.git
cd Elective-registration-Day10
3️⃣ Update Email Credentials
Edit EmailUtil.java:


final String fromEmail = "your-email@gmail.com";
final String appPassword = "your-app-password";  // Use App Password if 2FA enabled
4️⃣ Run MongoDB and Insert Sample Courses
Use MongoDB Compass or shell:


use electiveDB

db.courses.insertMany([
  { _id: "C101", name: "AI & ML", availableSeats: 5 },
  { _id: "C102", name: "Cloud Computing", availableSeats: 3 },
  { _id: "C103", name: "Cybersecurity", availableSeats: 4 }
])
5️⃣ Run the Server

Run Main.java
6️⃣ Run Console-Based Client

Run ClientConsoleApp.java
🔗 API Endpoints
Method	Endpoint	Description
POST	/api/students/register	Register with email
POST	/api/students/login	Login with email + password
GET	/api/students/courses	View all courses
POST	/api/students/enroll	Enroll in a course

📸 Sample MongoDB Record After Enrollment

{
  "_id": ObjectId("..."),
  "email": "24mcab44@kristujayanti.com",
  "password": "68c83156",
  "registered": true,
  "registeredCourseId": "C101"
}
📬 SMTP Note
This project uses Gmail SMTP

Make sure to enable "App Passwords" in Gmail

Port used: 587 (TLS)

🏁 Conclusion
This project showcases a real-world registration system backend with API handling, email automation, and MongoDB data persistence — ideal for education-based solutions or academic mini-projects.

🌐 Author
Praveena R
📧 24mcab44@kristujayanti.com
