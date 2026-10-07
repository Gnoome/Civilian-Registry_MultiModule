package gr.gnoome;

public class Person  {
    String id;
    String name;
    String surname;
    String birthdate;
    String gender;
    String address;
    String tax;

    
    public Person(){
        
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getTax() {
        return tax;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String toString() {
        return "-----------------------"+ "\n"+ "ID: " + id + "\nName: " + name + "\nSurname: " + surname + "\nBirthdate: " + birthdate + "\nGender: " + gender + (address != null ? "\nAddress: " + address : "") + (tax != null ? "\nTax: " + tax : "")+ "\n" + "-----------------------"+"\n";
    }
}