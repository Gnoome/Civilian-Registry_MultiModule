package gr.gnoome;

import java.util.ArrayList;
import java.util.List;

public class PersonValidator {

    public static List<String> validateForInsert(Person person) {

        List<String> errors = new ArrayList<>();

        checkErrors(validateId(person.getId()), errors);
        checkErrors(validateName(person.getName()), errors);
        checkErrors(validateSurname(person.getSurname()), errors);
        checkErrors(validateGender(person.getGender()), errors);
        checkErrors(validateBirthDate(person.getBirthdate()), errors);
        checkErrors(validateTax(person.getTax()), errors);


        return errors;
    }


    public static List<String> validateForSearch(Person person) {

        List<String> errors = new ArrayList<>();
        if(person.getId() !=null &&!person.getId().isEmpty()){
            checkErrors(validateId(person.getId()), errors);
        }
        if(person.getName() !=null &&!person.getName().isEmpty()){
            checkErrors(validateName(person.getName()), errors);
        }
        if(person.getSurname() !=null &&!person.getSurname().isEmpty()){
            checkErrors(validateSurname(person.getSurname()), errors);
        }
        if(person.getGender() !=null &&!person.getGender().isEmpty()){
            checkErrors(validateGender(person.getGender()), errors);
        }
        if(person.getBirthdate() !=null &&!person.getBirthdate().isEmpty()){
            checkErrors(validateBirthDate(person.getBirthdate()), errors);
        }
        checkErrors(validateTax(person.getTax()), errors);

        return errors;
    }

    public static String validateForDelete(String id) {
        return validateId(id);
    }

    public static List<String> validateforUpdate(Person person) {
        
        List<String> errors = new ArrayList<>();

        checkErrors(validateId(person.getId()), errors);
        checkErrors(validateTax(person.getTax()), errors);

        return errors;

    
        
    }


    private static void checkErrors(String error,List<String> errors){ 
        if (error != null) {
            errors.add(error);
        } 
    }
    

    private static String validateId(String id) {

        if (id == null || id.isEmpty()) {
            return "id is required";
        }

        if (!id.matches("\\d{8}")) {
            return "id must be 8 digits";
        }
        return null;
    }

    private static String validateName(String name) {

        if (name == null || name.isEmpty()) {
            return "name is required";
        }

        return null;
    }

    private static String validateSurname(String surname) {

        if (surname == null || surname.isEmpty()) {
            return "surname is required";
        }

        return null;
    }

    private static String validateGender(String gender) {

        if (gender == null || gender.isEmpty()) {
            return "gender is required";
        }

        if (!gender.equalsIgnoreCase("m") && !gender.equalsIgnoreCase("f")) {
            return "gender must be 'm' or 'f'";
        }

        return null;
    }

    private static String validateBirthDate(String birthDate) {

        if (birthDate == null || birthDate.isEmpty()) {
            return "birth date is required";
        }

        if (!birthDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return "birth date must be in format DD-MM-YYYY";
        }

        return null;
    }

    private static String validateTax(String tax) {

        if (tax != null && !tax.isEmpty() && tax.length()!=9) {
            return "tax must be 9 digits";
        }

        return null;
    }

}
