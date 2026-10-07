package gr.gnoome.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.ServiceUnavailableException;

import gr.gnoome.Person;
import gr.gnoome.PersonValidator;
import gr.gnoome.utility.DatabaseManager;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/Civilians")
public class CivilianService {

    private static void checkDatabaseAvailability() {
        if (!DatabaseManager.existsDatabase()) {

            throw new ServiceUnavailableException(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("SQL database is unavailable").type(MediaType.TEXT_PLAIN).build());

        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(@Context UriInfo uriInfo, Person person) {

        checkDatabaseAvailability();
        if (person == null) {
            throw new BadRequestException("Civilian not provided");

        }

        errorCheck(person,"insert");

        boolean isItHere = DatabaseManager.existsPerson(person.getId());
        if (isItHere) {
            throw new BadRequestException("Civilian alwready in the database");
        } else {
            DatabaseManager.addPerson(person);
        }

        URI location = uriInfo.getAbsolutePathBuilder().path(person.getId()).build();

        return Response.created(location).entity(person.getId()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getAllCivilians() {

        checkDatabaseAvailability();
        return DatabaseManager.viewAllPersons();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getSelectedCivilians(@QueryParam("id") String id, @QueryParam("name") String name,
            @QueryParam("surname") String surname, @QueryParam("birthdate") String birthdate,
            @QueryParam("gender") String gender, @QueryParam("address") String address, @QueryParam("tax") String tax) {

        checkDatabaseAvailability();

        Person person = new Person();

        person.setId(id);
        person.setName(name);
        person.setSurname(surname);
        person.setGender(gender);
        person.setBirthdate(birthdate);
        person.setAddress(address);
        person.setTax(tax);

        errorCheck(person,"search");

        return DatabaseManager.viewSelectedCivilians(person);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCivilian(@PathParam("id") String id) {

        checkDatabaseAvailability();
        Person person = new Person();
        person.setId(id);
        errorCheck(person, "delete");

        if (DatabaseManager.deletePerson(id)) {
            return Response.noContent().build();
        } else {
            throw new NotFoundException();
        }

    }

   @PATCH
   @Path("/{id}")
   public Response updateCivilian(@PathParam("id") String id, @QueryParam("address") String address,@QueryParam("tax") String tax){

        checkDatabaseAvailability();

        Person person = new Person();
        person.setId(id);
        person.setAddress(address);
        person.setTax(tax);

        errorCheck(person,"update");
        
       
        if(DatabaseManager.updatePerson(id,address,tax)){

            return Response.ok().build();
        }
        else{
            throw new BadRequestException();
        }
   }

    private static void errorCheck(Person person,String type) {

        List<String> errors = new ArrayList<>();
        switch(type){
            case "insert":
                errors = PersonValidator.validateForInsert(person);
            break;
            case "search":
                errors = PersonValidator.validateForSearch(person);
            break;
            case "update":
                errors = PersonValidator.validateforUpdate(person);
            break;
            case "delete":
                String ans= PersonValidator.validateForDelete(person.getId());
                if(ans==null){
                    break;
                }
                throw new BadRequestException(ans);  
        }
        
    if(!type.equals("delete")){
          if (errors.size() != 0) {
            StringBuilder builder = new StringBuilder();
            System.out.println("The proccess had the following problems:");
            for (int i = 0; i < errors.size(); i++) {
                builder.append("error" + (i + 1) + ":" + errors.get(i) + "\n");
            }
            throw new BadRequestException(builder.toString());
        }
    }
      
    }
}
