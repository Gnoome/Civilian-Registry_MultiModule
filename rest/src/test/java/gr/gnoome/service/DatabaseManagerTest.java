package gr.gnoome.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gr.gnoome.Person;
import gr.gnoome.utility.DatabaseManager;

public class DatabaseManagerTest {

    private Person person;

    @BeforeEach
    void setup(){
         person = new Person();

        person.setId("88888888");
        person.setName("GGGGG");
        person.setSurname("NNNNN");
        person.setGender("M");
        person.setBirthdate("11-11-1111");

        
    }

    @AfterEach 
    void reset(){
        DatabaseManager.deletePerson("99999999");
    }


    @Test
    void existPersonFalseTest(){
        boolean value;
        value = DatabaseManager.existsPerson(person.getId());
        assertFalse(value);
    }

     @Test
    void existPersonTrueTest(){
        DatabaseManager.addPerson(person);
        boolean value;
        value = DatabaseManager.existsPerson(person.getId());
        assertTrue(value);
    }

    @Test
    void addPersonFalseTest(){
        boolean value;
        DatabaseManager.addPerson(person);
        value=DatabaseManager.addPerson(person);
        assertFalse(value);
    }


    @Test
    void addPersonTrueTest(){
        boolean value;
        value=DatabaseManager.addPerson(person);

        assertTrue(value);
    }

    @Test
    void deletePersonTrueTest(){
        DatabaseManager.addPerson(person);
        boolean value;
        value=DatabaseManager.deletePerson(person.getId());
        assertTrue(value);
    }

    @Test
    void deletePersonFalseTest(){
        boolean value;
        value=DatabaseManager.deletePerson(person.getId());
        assertFalse(value);
    }

    @Test 
    void updatePersonTrueTest(){
        DatabaseManager.addPerson(person);
        person.setAddress("lalala");
        person.setTax("123456789");
        boolean value;
        value =DatabaseManager.updatePerson(person.getId(), person.getAddress(), person.getTax());
        assertTrue(value);
    }

    @Test 
    void updatePersonFalseTest(){
        boolean value;
        value =DatabaseManager.updatePerson(person.getId(), person.getAddress(), person.getTax());
        assertFalse(value);
    }

    @Test
    void viewAllPersonTest(){
        List <Person> persons = new ArrayList<>();
        List <Person> persons2 = new ArrayList<>();

        persons= DatabaseManager.viewAllPersons();
        DatabaseManager.addPerson(person);
        persons2 =DatabaseManager.viewAllPersons();
        assertEquals(persons.size()+1,persons2.size());

       
    }

    @Test 
    void viewSelectedCiviliansTest(){
        DatabaseManager.addPerson(person);
        List<Person> persons = DatabaseManager.viewSelectedCivilians(person);
        assertEquals(1, persons.size());
    }

    @Test 
    void existDatabaseOpenTest(){
        boolean value;
        value=DatabaseManager.existsDatabase();
        assertTrue(value);
    }

   

   


}
