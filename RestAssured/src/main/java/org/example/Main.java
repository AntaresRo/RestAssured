package org.example;

import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static files.Payload.addPlace;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Main {
    public static void main(String[] args) {


        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(addPlace())
        .when()
                .post("maps/api/place/add/json")
        .then()
                .log().all()
                .assertThat()
                    .statusCode(200)
                .body("scope",  equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)")
                    .extract().response().asString();

        JsonPath jsonPath = new JsonPath(response);
        String placeId = jsonPath.getString("place_id");
        System.out.println(response);

        //update Place
        String newAddress = "Summer Walk Africa";
                given().log().all().queryParam("key", "qaclick123").header("Content-type", "application/json")
                .body("""
                        {
                        "place_id":"%s",
                        "address":"%s",
                        "key":"qaclick123"
                        }
                        """.formatted(placeId, newAddress))
                .when().put("maps/api/place/update/json")
                .then()
                    .assertThat()
                    .statusCode(200)
                    .body("msg", equalTo("Address successfully updated"));


        String getPlaceResponse = given()
                .log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
        .when().get("maps/api/place/get/json")
        .then().assertThat().log().all().statusCode(200).extract().response().asString();


        jsonPath = ReusableMethods.rawToJson(getPlaceResponse);
        String extractedAddress = jsonPath.getString("address");
        Assert.assertEquals(extractedAddress, newAddress);

    }

}