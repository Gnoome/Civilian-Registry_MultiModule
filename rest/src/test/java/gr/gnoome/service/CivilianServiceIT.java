package gr.gnoome.service;

import static org.hamcrest.Matchers.endsWith;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import gr.gnoome.Person;
import gr.gnoome.utility.DatabaseManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CivilianServiceIT {
      @BeforeAll
    static void setup(){
       DatabaseManager.deletePerson("99999999");
       RestAssured.baseURI="http://localhost:8080";
       RestAssured.basePath ="/Civilian_REST/api";
    }

    @Test
    @Order(1)
    void addPersonFalse(){
        Person person =  new Person();
        given().contentType(ContentType.JSON).body(person).when().post("/Civilians").then().statusCode(400);
    }

    @Test
    @Order(2)
    void addPersonTrueTest(){
        Person person = new Person();
         person.setId("99999999");
        person.setName("name");
        person.setSurname("surname");
        person.setGender("M");
        person.setBirthdate("22-22-2222");
        person.setTax("111111111");
        person.setAddress("s");

        given()
            .contentType(ContentType.JSON)
            .body(person)
        .when()
            .post("/Civilians")
        .then()
            .statusCode(201)
            .header("Location",endsWith("/Civilians/"+person.getId()));
            

}

    @Test
    @Order(3)
    void updatePersonTrueTest(){
        String id ="99999999";
        String address ="lalala";
        String tax ="999999999";

        given().contentType(ContentType.JSON).pathParam("id", id).queryParam("address", address).queryParam("tax", tax)
        .when().patch("/Civilians/{id}")
        .then().statusCode(200);

    }

    @Test
    @Order(4)
    void viewSelectedPersonTrue(){
         String id ="99999999";

        given().contentType(ContentType.JSON).queryParam("id", id)
        .when().get("/Civilians/search")
        .then().statusCode(200);

    }
    @Test

    @Order(5)
    void deletePersonTrueTest(){

        String id ="99999999";

        given().contentType(ContentType.JSON).pathParam("id", id).when().delete("/Civilians/{id}").then().statusCode(204);
        
    }

    @Test
    void getallCiviliansTest(){

        given().contentType(ContentType.JSON).when().get("/Civilians").then().statusCode(200);
    }

  
}
