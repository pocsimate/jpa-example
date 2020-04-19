package person;

import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;
import java.util.List;

@Log4j2
public class Main {

    private static Faker faker = new Faker();
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    public static Person randomPerson(){

        Address pAddress = new Address(faker.address().country(), faker.address().state(), faker.address().city(),
                faker.address().streetAddress(), faker.address().zipCode());

        Person person = Person.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .gender(faker.options().option(Person.Gender.values()))
                .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .address(pAddress)
                .build();

        return person;
    }

    public static void addPerson(){
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(randomPerson());
            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }

    public static void deletePersons(){
        EntityManager em = emf.createEntityManager();

        try {
          em.getTransaction().begin();
          long n = em.createQuery("DELETE FROM Person").executeUpdate();
          log.info("Deleted {} Persons", n);
          em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static List<Person> listPersons(){

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {

        for (int i=0; i<100; i++) {
            addPerson();
        }

        listPersons().forEach(log::info);
        deletePersons();

    }
}
