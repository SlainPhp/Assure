package com.example.petclinic;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class VisitPositiveTest extends SetupData {

    @Test
    void positiveVisitScenario() {
        int ownerId = ownerId1;
        int [] petId = petIds;


        given().spec(spec)
                .get("/api/owners/" + ownerId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("firstName", equalTo("John"));

        String visitDate = "2026-12-20";
        String description = "Surgery checkup";

        String visitBody = String.format("""
            {
                "date": "%s",
                "description": "%s"
            }
            """, visitDate, description);

        int visitId = given().spec(spec)
                .body(visitBody)
                .post("/api/owners/" + ownerId + "/pets/" + petId + "/visits")
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(equalTo(200), equalTo(201)))
                .extract()
                .path("id");

        System.out.println("Created visit with ID: " + visitId);

        given().spec(spec)
                .get("/api/visits/" + visitId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("description", equalTo(description))
                .body("date", notNullValue());
    }
}
