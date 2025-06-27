package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientConsoleApp {
    private static final String BASE_URL = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpClient client = vertx.createHttpClient();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n🔹 Elective Course Registration Menu");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Courses");
            System.out.println("4. Enroll in Course");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();
                    sendPostRequest(client, "/api/students/register", "email=" + encode(email));
                }
                case 2 -> {
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();
                    sendPostRequest(client, "/api/students/login", "email=" + encode(email) + "&password=" + encode(password));
                }
                case 3 -> sendGetRequest(client, "/api/students/courses");
                case 4 -> {
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter course ID: ");
                    String courseId = scanner.nextLine();
                    sendPostRequest(client, "/api/students/enroll", "email=" + encode(email) + "&courseId=" + encode(courseId));
                }
                case 5 -> {
                    System.out.println("Goodbye 👋");
                    vertx.close();
                    return;
                }
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }

    private static void sendPostRequest(HttpClient client, String path, String body) {
        client.request(HttpMethod.POST, PORT, BASE_URL, path, ar -> {
            if (ar.succeeded()) {
                HttpClientRequest req = ar.result();
                req.putHeader("Content-Type", "application/x-www-form-urlencoded");
                req.send(Buffer.buffer(body), res -> {
                    if (res.succeeded()) {
                        res.result().body().onComplete(b -> {
                            System.out.println("📥 Response:");
                            System.out.println(b.result().toString());
                        });
                    } else {
                        System.out.println("❌ Request failed: " + res.cause().getMessage());
                    }
                });
            } else {
                System.out.println("❌ Failed to send request: " + ar.cause().getMessage());
            }
        });
    }

    private static void sendGetRequest(HttpClient client, String path) {
        client.request(HttpMethod.GET, PORT, BASE_URL, path, ar -> {
            if (ar.succeeded()) {
                ar.result().send(res -> {
                    if (res.succeeded()) {
                        res.result().body().onComplete(b -> {
                            System.out.println("📥 Courses:");
                            System.out.println(b.result().toString());
                        });
                    } else {
                        System.out.println("❌ Request failed: " + res.cause().getMessage());
                    }
                });
            } else {
                System.out.println("❌ Failed to send request: " + ar.cause().getMessage());
            }
        });
    }

    private static String encode(String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }
}
