package org.example;

import io.restassured.path.json.JsonPath;

import static files.Payload.coursePrice;

public class ComplexJsonParse {
    public static void main(String[] args) {

        JsonPath jsonPath = new JsonPath(coursePrice());
        jsonPath.getInt("courses.size()");
    }
}
