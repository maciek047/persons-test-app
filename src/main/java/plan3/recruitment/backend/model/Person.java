package plan3.recruitment.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Person implements Comparable<Person> {

    @JsonProperty
    private final String email;
    @JsonProperty
    private final PersonName name;

    public Person(@JsonProperty("firstname") final String firstname,
                  @JsonProperty("lastname") final String lastname,
                  @JsonProperty("email") final String email) {
        this.name = new PersonName(firstname, lastname);
        this.email = email;
    }

    @Override
    public String toString() {
        return "Person{" +
                "email='" + hideEmail(email) + '\'' +
                '}';
    }

    public static Person valueOf(final String firstname, final String lastname, final String email) {
        return new Person(firstname, lastname, email);
    }

    private String hideEmail(String email) {
        return email.substring(0, 2) + "..." + email.substring(email.indexOf('@'));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(email, person.email) && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }
}
