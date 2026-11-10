package Homework;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    protected static RequestSpecification spec;
    protected static int ownerId1;
    protected static int ownerId2;
    protected static int petTypeId;
    protected static int specialtyId;
    protected static int vetId1;
    protected static int[] petIds = new int[5];

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:9966/petclinic";
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }
}
