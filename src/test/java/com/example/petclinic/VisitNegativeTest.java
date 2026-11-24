package com.example.petclinic;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class VisitNegativeTest extends SetupData {

    @Test
    void getVisitsForNonExistingPet() {
        int ownerId = ownerId1;
        int nonExistingPetId = 99999;

        given().spec(spec)
                .get("/api/owners/" + ownerId + "/pets/" + nonExistingPetId + "/visits")
                .then()
                .log().ifValidationFails()
                .statusCode(404);
    }

    @Test
    void createVisitForNonExistingOwner() {
        int nonExistingOwnerId = 99999;
        int[] petId = petIds;

        String visitDate = "2026-12-20";
        String description = "Checkup";

        String body = String.format("""
            {
                "date": "%s",
                "description": "%s"
            }
            """, visitDate, description);

        given().spec(spec)
                .body(body)
                .post("/api/owners/" + nonExistingOwnerId + "/pets/" + petId + "/visits")
                .then()
                .log().ifValidationFails()
                .statusCode(404);
    }

    @Test
    void createVisitWithInvalidDateFormat() {
        int ownerId = ownerId1;
        int[] petId = petIds;

        String invalidDate = "20-12-2026";
        String description = "Checkup";

        String body = String.format("""
            {
                "date": "%s",
                "description": "%s"
            }
            """, invalidDate, description);

        given().spec(spec)
                .body(body)
                .post("/api/owners/" + ownerId + "/pets/" + petId + "/visits")
                .then()
                .log().ifValidationFails()
                .statusCode(400);
    }

    @Test
    void createVisitWithEmptyDescription() {
        int ownerId = ownerId1;
        int[] petId = petIds;

        String visitDate = "2026-12-20";
        String description = "";

        String body = String.format("""
            {
                "date": "%s",
                "description": "%s"
            }
            """, visitDate, description);

        given().spec(spec)
                .body(body)
                .post("/api/owners/" + ownerId + "/pets/" + petId + "/visits")
                .then()
                .log().ifValidationFails()
                .statusCode(400);
    }
}
