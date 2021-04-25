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
public class PaginationTest {

    private String authorization = "";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private EmailRepository emailRepository;
    
    String contactsJson;

    @BeforeAll
    public void setup() {
        emailRepository.deleteAll();
        phoneRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        RestAssured.port = 9090;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "agenda/ws";

        Gson gson = new Gson();

        User user = new User();
        user.setUsername("user");
        user.setPassword("12345");

        given().body(gson.toJson(user))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when().post("/users");

        authorization = given().body(gson.toJson(user))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().extract().header("authorization");

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

        for (int i = 0; i < 10; i++) {
            Contact contact = new Contact();
            contact.setName("Name LastName " + i);
            contact.setAlias("Nickname " + i);
            contact.setPhone(phones);
            contact.setEmail(emails);
            given().body(gson.toJson(contact))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .header("authorization", authorization)
                    .when().post("/contacts");
        }
    }

    @Test
    @Order(0)
    public void getAllContacts() {
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().get("/contacts")
                .then().statusCode(HttpStatus.SC_OK)
                .and()
                .assertThat().body("", Matchers.hasSize(10));
    }
    
    @Test
    @Order(1)
    public void getFirstPageFiveItems() {
        contactsJson = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .queryParams("page", "0", "size", "5")
                .when().get("/contacts")
                .then().statusCode(HttpStatus.SC_OK)
                .and()
                .assertThat().body("", Matchers.hasSize(5))
                .extract().body().asString();
    }
    
    @Test
    @Order(2)
    public void getSecondPageFourItems() {
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .queryParams("page", "1", "size", "4")
                .when().get("/contacts")
                .then().statusCode(HttpStatus.SC_OK)
                .and()
                .assertThat().body("", Matchers.hasSize(4))
                .and()
                .assertThat().body("", Matchers.not(Matchers.equalTo(contactsJson)));
    }
}
