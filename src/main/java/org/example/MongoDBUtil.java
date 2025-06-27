package org.example;

import com.mongodb.client.*;
import org.bson.Document;

public class MongoDBUtil {
    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "electiveDB";

    // Create a Mongo client
    public static MongoClient mongoClient = MongoClients.create(URI);

    // Get database
    public static MongoDatabase database = mongoClient.getDatabase(DB_NAME);

    // Get students collection
    public static MongoCollection<Document> getStudentCollection() {
        return database.getCollection("students");
    }

    // Get courses collection
    public static MongoCollection<Document> getCourseCollection() {
        return database.getCollection("courses");
    }
}
