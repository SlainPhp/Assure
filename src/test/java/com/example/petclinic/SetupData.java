package com.example.petclinic;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class SetupData {

    protected static RequestSpecification spec;

    protected static int ownerId1 = 1;
    protected static int [] petIds = {1, 2, 3, 4, 5};
    protected static int [] vetIds = {1, 2, 3};

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";

        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();

        System.out.println("Test environment configured.");
    }
}
