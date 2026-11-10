package Homework;

import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SetupTest extends BaseTest {

    private static final long stamp = System.currentTimeMillis();

    @Test
    @Order(1)
    public void setupData() {

        String username = "admin_user_" + stamp;
        given().spec(spec).body("""
                {"username": "%s", "password": "admin123",
                 "enabled": true,
                 "roles": [{"name": "ROLE_ADMIN"}]}
                """.formatted(username))
                .when().post("/api/users")
                .then().log().ifValidationFails()
                .statusCode(anyOf(equalTo(200), equalTo(201)));

        ownerId1 = given().spec(spec).body("""
                {"firstName": "Ivan", "lastName": "Petrov",
                 "address": "Tverskaya street", "city": "Moscow", "telephone": "0123456789"}
                """)
                .when().post("/api/owners")
                .then().log().ifValidationFails()
                .statusCode(anyOf(equalTo(200), equalTo(201)))
                .extract().path("id");

        ownerId2 = given().spec(spec).body("""
                {"firstName": "Fedor", "lastName": "Sidorov",
                 "address": "Arbat street", "city": "Moscow", "telephone": "0987654321"}
                """)
                .when().post("/api/owners")
                .then().log().ifValidationFails()
                .statusCode(anyOf(equalTo(200), equalTo(201)))
                .extract().path("id");

        int catId = findOrCreatePetType("cat");
        int dogId = findOrCreatePetType("dog");
        int parrotId = findOrCreatePetType("parrot");
        petTypeId = catId;

        String[] petNames = {"Murzik", "Barsik", "Kesha", "Musya", "Rex"};
        String[] typeNames = {"cat", "dog", "parrot", "cat", "dog"};
        int[] typeIds = {catId, dogId, parrotId, catId, dogId};

        for (int i = 0; i < 5; i++) {
            petIds[i] = given().spec(spec).body("""
                    {"name": "%s", "birthDate": "2023-0%d-15",
                     "type": {"id": %d, "name": "%s"}}
                    """.formatted(petNames[i], i + 1, typeIds[i], typeNames[i]))
                    .when().post("/api/owners/" + ownerId1 + "/pets")
                    .then().log().ifValidationFails()
                    .statusCode(anyOf(equalTo(200), equalTo(201)))
                    .extract().path("id");
        }

        int surgeryId = findOrCreateSpecialty("surgery");
        int dentistryId = findOrCreateSpecialty("dentistry");
        specialtyId = dentistryId;

        String[] vetFirstNames = {"Anna", "Boris", "Vera"};
        String[] vetLastNames = {"Ivanova", "Orlov", "Smirnova"};

        for (int i = 0; i < 3; i++) {
            int vetId = given().spec(spec).body("""
                    {"firstName": "%s", "lastName": "%s",
                     "specialties": [{"id": %d, "name": "surgery"}]}
                    """.formatted(vetFirstNames[i], vetLastNames[i], surgeryId))
                    .when().post("/api/vets")
                    .then().log().ifValidationFails()
                    .statusCode(anyOf(equalTo(200), equalTo(201)))
                    .extract().path("id");

            if (i == 0) {
                vetId1 = vetId;
            }
        }
    }

    private int findOrCreatePetType(String name) {
        Response response = given().spec(spec).when().get("/api/pettypes");
        List<Map<String, Object>> types = response.jsonPath().getList("");
        for (Map<String, Object> type : types) {
            if (name.equals(type.get("name"))) {
                return (Integer) type.get("id");
            }
        }
        return given().spec(spec).body("{\"name\": \"" + name + "\"}")
                .when().post("/api/pettypes")
                .then().statusCode(anyOf(equalTo(200), equalTo(201)))
                .extract().path("id");
    }

    private int findOrCreateSpecialty(String name) {
        Response response = given().spec(spec).when().get("/api/specialties");
        List<Map<String, Object>> specialties = response.jsonPath().getList("");
        for (Map<String, Object> specialty : specialties) {
            if (name.equals(specialty.get("name"))) {
                return (Integer) specialty.get("id");
            }
        }
        return given().spec(spec).body("{\"name\": \"" + name + "\"}")
                .when().post("/api/specialties")
                .then().statusCode(anyOf(equalTo(200), equalTo(201)))
                .extract().path("id");
    }
}
