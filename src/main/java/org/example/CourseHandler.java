package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CourseHandler {
    public static Handler<RoutingContext> getCourses = ctx -> {
        MongoCollection<Document> courses = MongoDBUtil.getCourseCollection();
        List<String> courseJsonList = new ArrayList<>();

        try (MongoCursor<Document> cursor = courses.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                courseJsonList.add(doc.toJson());  // Convert each document to JSON string
            }
        }

        // Join all JSON documents into a single JSON array string
        String jsonArray = "[" + String.join(",", courseJsonList) + "]";

        ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(jsonArray);
    };
}
