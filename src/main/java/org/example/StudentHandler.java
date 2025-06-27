package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.bson.Document;

import java.util.UUID;

public class StudentHandler {

    // 📩 Register Student & Send Email with Password
    public static Handler<RoutingContext> registerStudent = ctx -> {
        ctx.request().setExpectMultipart(true); // Needed for formAttributes to work

        ctx.request().endHandler(v -> {
            String email = ctx.request().formAttributes().get("email");

            if (email == null || email.isEmpty()) {
                ctx.response().setStatusCode(400).end("Email is required");
                return;
            }

            MongoCollection<Document> students = MongoDBUtil.getStudentCollection();
            Document existing = students.find(Filters.eq("email", email)).first();
            if (existing != null) {
                ctx.response().setStatusCode(400).end("Email already registered");
                return;
            }

            String generatedPassword = UUID.randomUUID().toString().substring(0, 8);

            Document newStudent = new Document("email", email)
                    .append("password", generatedPassword)
                    .append("registered", false)
                    .append("registeredCourseId", null);
            students.insertOne(newStudent);

            // Logging
            System.out.println("📧 Sending email to " + email);
            System.out.println("Generated password for " + email + ": " + generatedPassword);

            // Send email
            EmailUtil.sendEmail(
                    email,
                    "Your Elective Registration Password",
                    "Welcome to the Elective Registration System!\n\n" +
                            "Your generated password is: " + generatedPassword +
                            "\n\nUse this to log in and register for a course."
            );

            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end("{\"message\": \"Student registered with email: " + email + "\"}");
        });
    };

    // 🔐 Login Student
    public static Handler<RoutingContext> login = ctx -> {
        ctx.request().setExpectMultipart(true);
        ctx.request().endHandler(v -> {
            String email = ctx.request().formAttributes().get("email");
            String password = ctx.request().formAttributes().get("password");

            if (email == null || password == null) {
                ctx.response().setStatusCode(400).end("Email and password are required");
                return;
            }

            MongoCollection<Document> students = MongoDBUtil.getStudentCollection();
            Document student = students.find(Filters.eq("email", email)).first();

            if (student != null && password.equals(student.getString("password"))) {
                ctx.response()
                        .putHeader("Content-Type", "text/plain")
                        .end("✅ Login successful!");
            } else {
                ctx.response()
                        .setStatusCode(401)
                        .putHeader("Content-Type", "text/plain")
                        .end("❌ Invalid credentials");
            }
        });
    };

    // 📝 Enroll in Course
    public static Handler<RoutingContext> enroll = ctx -> {
        ctx.request().setExpectMultipart(true);
        ctx.request().endHandler(v -> {
            String email = ctx.request().formAttributes().get("email");
            String courseId = ctx.request().formAttributes().get("courseId");

            if (email == null || courseId == null || email.isEmpty() || courseId.isEmpty()) {
                ctx.response().setStatusCode(400).end("Email and courseId are required");
                return;
            }

            MongoCollection<Document> students = MongoDBUtil.getStudentCollection();
            MongoCollection<Document> courses = MongoDBUtil.getCourseCollection();

            Document course = courses.find(Filters.eq("_id", courseId)).first();
            if (course == null) {
                ctx.response().setStatusCode(404).end("Course not found");
                return;
            }

            int availableSeats = course.getInteger("availableSeats", 0);
            if (availableSeats <= 0) {
                ctx.response().setStatusCode(400).end("No seats available");
                return;
            }

            Document student = students.find(Filters.eq("email", email)).first();
            if (student == null) {
                ctx.response().setStatusCode(404).end("Student not found");
                return;
            }

            if (student.getBoolean("registered", false)) {
                ctx.response().setStatusCode(400).end("Student already enrolled in a course");
                return;
            }

            // Perform updates
            courses.updateOne(Filters.eq("_id", courseId), Updates.inc("availableSeats", -1));
            students.updateOne(Filters.eq("email", email),
                    Updates.combine(
                            Updates.set("registered", true),
                            Updates.set("registeredCourseId", courseId)
                    ));

            ctx.response()
                    .putHeader("Content-Type", "text/plain")
                    .end("✅ Student enrolled in course: " + courseId);
        });
    };
}
