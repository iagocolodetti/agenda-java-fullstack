package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.model.Email;
import br.com.iagocolodetti.agenda.model.Phone;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.repository.ContactRepository;
import br.com.iagocolodetti.agenda.repository.EmailRepository;
import br.com.iagocolodetti.agenda.repository.PhoneRepository;
import br.com.iagocolodetti.agenda.repository.UserRepository;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author iagocolodetti
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {

    private String authorization = "";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private EmailRepository emailRepository;

    private Gson gson;

    private User user;
    private Contact contact;

    @BeforeAll
    public void setup() {
        emailRepository.deleteAll();
        phoneRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        RestAssured.port = 9090;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "agenda/ws";

        gson = new Gson();

        user = new User();
        user.setUsername("user");
        user.setPassword("12345");

        contact = new Contact();
        contact.setName("Name LastName");
        contact.setAlias("Nickname");
        Phone p1 = new Phone();
        p1.setPhone("1111-2222");
        Phone p2 = new Phone();
        p2.setPhone("3333-4444");
        List<Phone> phones = new ArrayList<>();
        phones.add(p1);
        phones.add(p2);
        Email e1 = new Email();
        e1.setEmail("email1@gmail.com");
        Email e2 = new Email();
        e2.setEmail("email2@hotmail.com");
        Email e3 = new Email();
        e3.setEmail("email3@live.com");
        List<Email> emails = new ArrayList<>();
        emails.add(e1);
        emails.add(e2);
        emails.add(e3);
        contact.setPhone(phones);
        contact.setEmail(emails);
    }

    @Test
    @Order(1)
    public void createUser() {
        given().body(gson.toJson(user))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when().post("/users")
                .then().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    @Order(2)
    public void login() {
        authorization = given().body(gson.toJson(user))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().header("authorization");
    }

    @Test
    @Order(3)
    public void createContact() {
        String contactJson = given().body(gson.toJson(contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().body().asString();
        contact = gson.fromJson(contactJson, Contact.class);
    }

    @Test
    @Order(4)
    public void getContacts() {
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().get("/contacts")
                .then().statusCode(HttpStatus.SC_OK)
                .and()
                .assertThat().body("", Matchers.not(Matchers.empty()));
    }

    @Test
    @Order(5)
    public void updateContact() {
        contact.setAlias("NewNickname");
        contact.getEmail().get(0).setDeleted(true);
        contact.getEmail().get(1).setEmail("email2@yahoo.com");
        Email email = new Email();
        email.setEmail("email4@xyz.com");
        contact.getEmail().add(email);
        given().body(gson.toJson(contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().put("/contacts/" + contact.getId())
                .then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @Order(6)
    public void deleteContact() {
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().delete("/contacts/" + contact.getId())
                .then().statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
