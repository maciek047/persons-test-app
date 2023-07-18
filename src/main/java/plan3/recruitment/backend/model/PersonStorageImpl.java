package plan3.recruitment.backend.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersonStorageImpl implements PersonStorage {

    private final HashSet<Person> storage;

    public PersonStorageImpl() {
        this.storage = new HashSet<>();
    }

    @Override
    public Optional<Person> fetch(String email) {
        return storage.stream()
                .filter(person -> person.hashCode() == Objects.hash(email))
                .findFirst();
    }

    @Override
    public void save(Person person) {
        boolean personAdded = storage.add(person);
        if (!personAdded) {
            throw new IllegalArgumentException("Person with this email already exists");
        }
    }

    @Override
    public void update(Person person) {
        boolean personExists = storage.removeIf(p -> p.hashCode() == person.hashCode());
        if (!personExists) {
            throw new IllegalArgumentException("Person with this email does not exist");
        }
        storage.add(person);
    }

    @Override
    public boolean remove(Person person) {
        return storage.remove(person);
    }

    @Override
    public List<Person> list() {
        return storage.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
