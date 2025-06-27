package org.example;

// ✅ Make sure these imports are present
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

// ✅ Also import your handlers
import org.example.StudentHandler;
import org.example.CourseHandler;

public class Main {
    public static void main(String[] args) {
        // Initialize Vertx instance
        Vertx vertx = Vertx.vertx();

        // Create router
        Router router = Router.router(vertx);

        // Register API routes
        router.post("/api/students/register").handler(StudentHandler.registerStudent);
        router.post("/api/students/login").handler(StudentHandler.login);
        router.post("/api/students/enroll").handler(StudentHandler.enroll);
        router.get("/api/students/courses").handler(CourseHandler.getCourses);

        // Start the server
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        System.out.println("✅ Server started at http://localhost:8080");
                    } else {
                        System.out.println("❌ Server failed to start: " + http.cause().getMessage());
                    }
                });
    }
}
