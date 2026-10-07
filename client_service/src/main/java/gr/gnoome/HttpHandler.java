package gr.gnoome;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.client.Client;

public class HttpHandler {
    static ObjectMapper mapper = new ObjectMapper();
    private static final String URL = "http://localhost:8080/Civilian_REST/api/Civilians";
    private final Client client = ClientBuilder.newClient();

    public void close() {
        client.close();
    }

    public void getAllCivilian() {

        Response response = client.target(URL).request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() < 300) {
            System.out.println("Your request was successfull");
            String value = response.readEntity(String.class);
            List<Person> persons = new ArrayList<>();
            try {
                persons = mapper.readValue(value, new TypeReference<List<Person>>() {
                });
                for (Person p : persons) {
                    System.out.println(p.toString());

                }
            } catch (JsonProcessingException e) {
                System.out.println(e);
            }

        } else {
            System.out.println("Your request was unsuccefull");
            System.out.println("Status error code: " + response.getStatus());
        }

    }

    public void deleteCivilian(String id) {
        Response response = client.target(URL).path(id).request(MediaType.APPLICATION_JSON).delete();

        if (response.getStatus() < 300) {
            System.out.println("Your request was successfull");
            System.out.println("The civilian with id " + id + " was deleted succefully");

        } else {
            System.out.println("Your request was unsuccefull");
            if (response.getStatus() == 404) {
                System.out.println("There is no Civilian with Id " + id + " in the database");
            } else {
                System.out.println("Status error code: " + response.getStatus());
            }
        }

    }

    public void addCivilian(Person person) {
        try {

            String value = mapper.writeValueAsString(person);
            Response response = client.target(URL).request(MediaType.APPLICATION_JSON).post(Entity.json(value));

            if (response.getStatus() < 300) {
                System.out.println("Your request was successfull");
                System.out.println("The Civilian with id " + person.getId() + " was succefully added to the database");
            } else {
                System.out.println("Your request was unsuccefull");
                if (response.getStatus() == 400) {
                    System.out.println("The Civilian with id " + person.getId() + " is already in the database");
                } else {
                    System.out.println("Status error code: " + response.getStatus());
                }

            }

        } catch (JsonProcessingException e) {
            System.out.println(e);
        }

    }

    public void updateCivilian(Person person) {
        
            WebTarget target = client.target(URL).path(person.getId());

            if (person.getAddress() != null && !person.getAddress().isBlank()) {
                target = target.queryParam("address", person.getAddress());
            }
            if (person.getTax() != null && !person.getTax().isBlank()) {
                target = target.queryParam("tax", person.getTax());
            }
            Response response = target.request(MediaType.APPLICATION_JSON).put(Entity.text(""));

            if (response.getStatus() < 300) {
                System.out.println("Your request was successfull");
                System.out.println("The Civilian with id " + person.getId() + " was succefully updated");

            } else {
                System.out.println("Your request was unsuccefull");
                if (response.getStatus() == 404) {
                    System.out.println("There is no Civilian with Id " + person.getId() + " in the database");
                } else {
                    System.out.println("Status error code: " + response.getStatus());
                }
            }
    }

    public void searchCivilian(Person person) {
        WebTarget target = client.target(URL).path("search");

        if (person.getId() != null && !person.getId().isBlank()) {
            target = target.queryParam("id", person.getId());
        }
        if (person.getName() != null && !person.getName().isBlank()) {
            target = target.queryParam("name", person.getName());
        }
        if (person.getSurname() != null && !person.getSurname().isBlank()) {
            target = target.queryParam("surname", person.getSurname());
        }
        if (person.getBirthdate() != null && !person.getBirthdate().isBlank()) {
            target = target.queryParam("birthdate", person.getBirthdate());
        }
        if (person.getGender() != null && !person.getGender().isBlank()) {
            target = target.queryParam("gender", person.getGender());
        }
        if (person.getAddress() != null && !person.getAddress().isBlank()) {
            target = target.queryParam("address", person.getAddress());
        }
        if (person.getTax() != null && !person.getTax().isBlank()) {
            target = target.queryParam("tax", person.getTax());
        }

        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() < 300) {
            System.out.println("Your request was successfull");
            String value = response.readEntity(String.class);
            List<Person> persons = new ArrayList<>();
            try {
                persons = mapper.readValue(value, new TypeReference<List<Person>>() {
                });
                if (persons.isEmpty()) {
                    System.out.println("There was no matches for the search");

                } else {
                    for (Person p : persons) {
                        System.out.println(p.toString());

                    }
                }

            } catch (JsonProcessingException e) {
                System.out.println(e);
            }

        } else {
            System.out.println("Your request was unsuccefull");
            System.out.println("Status error code: " + response.getStatus());
        }

    }

}
