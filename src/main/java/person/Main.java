package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;

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



    public static void main(String[] args) {

        Person p1 = new Person();
        p1.setGender(faker.options().option(Person.Gender.values()));

        System.out.println(p1.getGender());

        for (int i=0; i<100; i++) {
            addPerson();
        }


    }
}
