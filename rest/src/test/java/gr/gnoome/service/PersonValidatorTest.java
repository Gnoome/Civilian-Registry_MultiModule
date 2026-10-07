package gr.gnoome.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import gr.gnoome.*;

public class PersonValidatorTest {

    private Person person;
    List<String> errors;

    @BeforeEach
    void setUp() {
        person = new Person();

        person.setId("12345678");
        person.setName("george");
        person.setSurname("nannos");
        person.setGender("M");
        person.setBirthdate("11-12-2000");
        person.setTax("123456789");
        person.setAddress("Salamina");
    }

    @Test
    void validCivilianInsert() {

        errors = PersonValidator.validateForInsert(person); 

        assertTrue(errors.isEmpty(), "" + errors);
    }

     @Test
    void validCivilianUpdate() {

        errors = PersonValidator.validateforUpdate(person); 

        assertTrue(errors.isEmpty(), "" + errors);
    }

     @Test
    void validCivilianDelete() {

        String error =PersonValidator.validateForDelete(person.getId()); 
        assertEquals(null,error);
    }

     @Test
    void validCivilianSearch() {

        errors = PersonValidator.validateForSearch(person); 

        assertTrue(errors.isEmpty(), "" + errors);
    }



    //Id tests

    @ParameterizedTest
    @CsvSource({"123456789","2345678","0abcde"})
    @NullAndEmptySource
    void idParameterTest(String id){
        
        person.setId(id);
        errors = PersonValidator.validateForInsert(person);
        assertEquals(errors.size(),1 );
        
    }

    // Name tests
    @ParameterizedTest
    @CsvSource(value = {"george, 0","Maria, 0","Nikos,0","Jessy,0","null, 1","'',1"}, nullValues= "null")
    void NameParameterTest(String name, int expected){

        person.setName(name);
        errors = PersonValidator.validateForInsert(person);
       assertEquals(expected,errors.size());

    }

// Surname tests
 @ParameterizedTest
    @CsvSource(value = {"nannos, 0","papadopoylos, 0","Eythimioy,0","epitheto,0","null, 1","'',1"}, nullValues= "null")
    void SurnameParameterTest(String surname, int expected){

        person.setSurname(surname);
        errors = PersonValidator.validateForInsert(person);
        assertEquals(expected,errors.size());

    }
//Gender tests
 @ParameterizedTest
    @CsvSource(value = {"m, 0","f, 0","M,0","F,0","null, 1","'',1","test, 1"}, nullValues= "null")
    void GenderParameterTest(String gender, int expected){

        person.setGender(gender);
        errors = PersonValidator.validateForInsert(person);
       assertEquals(expected,errors.size());

    }

//Birthdate test
 @ParameterizedTest
    @CsvSource(value = {"11-11-1111, 0","111-11-1111, 1","11-111-1111,1","11-11-11111,1","null, 1","'',1"}, nullValues= "null")
    void BirthdateParameterTest(String birthdate, int expected){

        person.setBirthdate(birthdate);
        errors = PersonValidator.validateForInsert(person);
        assertEquals(expected,errors.size());

    }

//tax test
 @ParameterizedTest
    @CsvSource(value = {"12345678, 1","23456789, 1","ABCDEF,1","null, 0","'',0"}, nullValues= "null")
    void TaxParameterTest(String tax, int expected){

        person.setTax(tax);
        errors = PersonValidator.validateForInsert(person);
        assertEquals(expected,errors.size());

    }

    
}
