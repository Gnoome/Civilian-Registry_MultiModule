package gr.gnoome;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import jakarta.ws.rs.ProcessingException;

public class Menu {

    private static Scanner scan = new Scanner(System.in);
    private static String input;
    static HttpHandler handler = new HttpHandler();
    

    private static void searchCivilian(){
        Person person = new Person();

        System.out.println("\n You selected to search the database for entries");
        System.out.println("please type your selected criteria or press enter to ignore\n");

        System.out.println("Civilian's Id (8 characters)");
        input = scan.nextLine();
        person.id = input;

        System.out.print("Name: ");
        input = scan.nextLine();
        person.name = input;

        System.out.print("Surname: ");
        input = scan.nextLine();
        person.surname = input;

        System.out.print("Gender(M or F): ");
        input = scan.nextLine();
        person.gender = input;

        System.out.print("Birthdate(dd-mm-yyyy): ");
        input =scan.nextLine();
        person.birthdate = input;

        System.out.print("Address(optional): ");
        input = scan.nextLine();
        person.address = input;

        System.out.print("Tax_Nummber(optional 9 digits): ");
        input = scan.nextLine();
        person.tax = input;

          List<String> valid = new ArrayList<>();
        valid = PersonValidator.validateForSearch(person);

        if (valid.isEmpty()) {
           System.out.println("You typed correctly all the critiria. Procceding with the search....\n");
           handler.searchCivilian(person);

        } else {
            System.out.println("you type incorrectly the following elements: \n");

            for (String error : valid) {
                System.out.println(error);
            }
        }
    }

    private static void addCivilian()  {
        Person person = new Person();

        System.out.println("\nYou have selected to add a new Civilian");
        System.out.println("Please provide the following...");

        System.out.print("Civilian ID Number (8 characters): ");
        input = scan.nextLine();
        person.setId(input); 

        System.out.print("Name: ");
        input = scan.nextLine();
        person.setName(input);

        System.out.print("Surname: ");
        input = scan.nextLine();
        person.setSurname(input);

        System.out.print("Gender(M or F): ");
        input = scan.nextLine();
        person.setGender(input);

        System.out.print("Birthdate(dd-mm-yyyy): ");
        input=scan.nextLine();
        person.setBirthdate(input);

        System.out.print("Address(optional): ");
        input = scan.nextLine();
        person.setAddress(input);

        System.out.print("Tax_Nummber(optional 9 digits): ");
        input = scan.nextLine();
        person.setTax(input);

        List<String> valid = new ArrayList<>();
        valid = PersonValidator.validateForInsert(person);

        if (valid.isEmpty()) {
             System.out.println("You typed correctly all the critiria. Procceding with the add....\n");
           handler.addCivilian(person);
        } else {
            System.out.println("you type incorrectly the following elements: \n");

            for (String error : valid) {
                System.out.println(error);
            }
        }

    }

    private static void removeCivilian() {
        System.out.println("you selected the option to remove a civilian:\n");

        System.out.print("please type the Id for the civilian you want to remove: ");
        input = scan.nextLine();

        String ans = PersonValidator.validateForDelete(input);
        if (ans!=null) {
            System.out.println(ans);
            return;
        } else {
            System.out.println("The id format is correct. Procceding with the deletion... ");
            handler.deleteCivilian(input);

        }

    }

    private static void updateCivilian() {
        Person person =new Person();

        System.out.println("You have selected to update a civilian");

        System.out.println("Please type the Civilian's Id you want to update: ");
        input= scan.nextLine();
        person.setId(input);
       
         System.out.println("Please type the Civilian's Address leave empty if you don't want to update: ");
        input= scan.nextLine();
        person.setAddress(input);

         System.out.println("Please type the Civilian's Tax id leave empty if you don't want to update: ");
        input= scan.nextLine();
        person.setTax(input);

        
        List<String> valid = new ArrayList<>();
        valid = PersonValidator.validateforUpdate(person);

        if (valid.isEmpty()) {
        System.out.println("You typed correctly all the critiria. Procceding with the update....\n");
           handler.updateCivilian(person);
           
        } else {
            System.out.println("you type incorrectly the following elements: \n");

            for (String error : valid) {
                System.out.println(error);
            }
        }

    }

    public static void viewAllCivilians(){
        handler.getAllCivilian();
    }

    private static void start() {

        System.out.println("Welcome to the Database");
        System.out.println("Please select one of the following options:");

        System.out.println("1. Add a new civilian");
        System.out.println("2. View all civilians");
        System.out.println("3. Update a civilian");
        System.out.println("4. Delete a civilian");
        System.out.println("5. Search for a civilian");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");


        try{
        switch (scan.nextLine()) {

        case "1":
                addCivilian();
            break;
        case "2":
            viewAllCivilians();
            break;
        case "3":
            updateCivilian();
            break;
        case "4":
            removeCivilian();
            break;
        case "5":
            searchCivilian();
            break;
        default:
            scan.close();
            handler.close();
            System.exit(0);
            break;
        }
    }catch(ProcessingException e){
        System.out.println("Could not connect to the server. Please make sure it is running.");
    }

    }

    public static void main(String[] args) {

        boolean flag = true;
        while (flag) {
            start();
        }
       

    }
}
