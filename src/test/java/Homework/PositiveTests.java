package Homework;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositiveTests extends BaseTest {

    @Test
    @Order(1)
    public void getVetsList() {
        given().spec(spec)
                .when().get("/api/vets")
                .then().statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(3)));
    }

    @Test
    @Order(2)
    public void getOwnerById() {
        given().spec(spec)
                .when().get("/api/owners/" + ownerId1)
                .then().statusCode(200)
                .body("id", equalTo(ownerId1));
    }

    @Test
    @Order(3)
    public void createVisitForPet() {
        String date = "2026-12-15";
        String description = "dentistry + " + vetId1;

        given().spec(spec).body("""
                {"date": "%s", "description": "%s"}
                """.formatted(date, description))
                .when().post("/api/owners/" + ownerId1 + "/pets/" + petIds[0] + "/visits")
                .then().log().ifValidationFails()
                .statusCode(anyOf(equalTo(200), equalTo(201)));
    }

    @Test
    @Order(4)
    public void verifyVisitExists() {
        String date = "2026-12-15";
        String description = "dentistry + " + vetId1;

        given().spec(spec)
                .when().get("/api/owners/" + ownerId1 + "/pets/" + petIds[0])
                .then().statusCode(200)
                .body("visits.find { it.description == '" + description + "' }.date",
                        equalTo(date));
    }
}
