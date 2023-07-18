package plan3.recruitment.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PersonName implements Comparable<PersonName> {
    @JsonProperty
    private final String firstname;
    @JsonProperty
    private final String lastname;

    public PersonName(@JsonProperty("firstname") final String firstname,
                      @JsonProperty("lastname") final String lastname
    ) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonName)) return false;
        PersonName that = (PersonName) o;
        return Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname);
    }

    @Override
    public int compareTo(PersonName other) {
        return this.lastname.compareTo(other.lastname);
    }
}
