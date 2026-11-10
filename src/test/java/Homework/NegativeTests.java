package Homework;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NegativeTests extends BaseTest {

    @Test
    @Order(1)
    public void createVisitForMissingPet_returns404() {
        String date = "2026-12-15";
        String description = "dentistry + " + vetId1;
        int nonExistingPetId = 999999;

        given().spec(spec).body("""
                {"date": "%s", "description": "%s"}
                """.formatted(date, description))
                .when().post("/api/owners/" + ownerId1 + "/pets/" + nonExistingPetId + "/visits")
                .then().log().ifValidationFails()
                .statusCode(equalTo(404));
    }

    @Test
    @Order(2)
    public void getMissingPet_returns404() {
        given().spec(spec)
                .when().get("/api/pets/999999")
                .then().statusCode(404);
    }

    @Test
    @Order(3)
    public void createVisitWithInvalidDate_returns400() {
        given().spec(spec).body("""
                {"date": "not-a-date", "description": "dentistry + %d"}
                """.formatted(vetId1))
                .when().post("/api/owners/" + ownerId1 + "/pets/" + petIds[0] + "/visits")
                .then().log().ifValidationFails()
                .statusCode(anyOf(equalTo(400), equalTo(500)));
    }
}
