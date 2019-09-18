package Response;

import java.util.ArrayList;

import Model.Person;

public class PersonResponse {
    private Person person;
    private String message;
    private Person[] data;

    public PersonResponse(Person person) {
        this.person = person;
    }

    public PersonResponse(String message) {
        this.message = message;
    }

    public PersonResponse(ArrayList<Person> persons) {
        data = new Person[persons.size()];
        for (int i = 0; i < persons.size(); i++) {
            data[i] = persons.get(i);
        }
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }
}
