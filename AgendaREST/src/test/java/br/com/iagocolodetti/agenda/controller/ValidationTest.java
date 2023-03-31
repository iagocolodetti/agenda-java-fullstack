package br.com.iagocolodetti.agenda.controller;

import br.com.iagocolodetti.agenda.dto.ContactDto;
import br.com.iagocolodetti.agenda.dto.EmailDto;
import br.com.iagocolodetti.agenda.dto.PhoneDto;
import br.com.iagocolodetti.agenda.dto.UserDto;
import br.com.iagocolodetti.agenda.model.Contact;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author iagocolodetti
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ValidationTest {
    
    private String authorization = "";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private EmailRepository emailRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private Gson gson;
    
    private ContactDto contact;

    @BeforeAll
    public void setup() {
        emailRepository.deleteAll();
        phoneRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        RestAssured.port = 9090;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "agenda/ws";

        UserDto user = new UserDto();
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

        PhoneDto p1 = new PhoneDto();
        p1.setPhone("1111-2222");
        PhoneDto p2 = new PhoneDto();
        p2.setPhone("3333-4444");
        List<PhoneDto> phones = new ArrayList<>();
        phones.add(p1);
        phones.add(p2);
        
        EmailDto e1 = new EmailDto();
        e1.setEmail("email1@gmail.com");
        EmailDto e2 = new EmailDto();
        e2.setEmail("email2@hotmail.com");
        EmailDto e3 = new EmailDto();
        e3.setEmail("email3@live.com");
        List<EmailDto> emails = new ArrayList<>();
        emails.add(e1);
        emails.add(e2);
        emails.add(e3);
        
        contact = new ContactDto();
        contact.setName("Name LastName");
        contact.setAlias("Nickname");
        contact.setPhone(phones);
        contact.setEmail(emails);
    }
    
    private <T extends Object> T copyObject(Object src, Class<T> classOfT) {
        return gson.fromJson(gson.toJson(src), classOfT);
    }

    @Test
    @Order(0)
    public void createContactShouldFailNameEmpty() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.setName("");
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(1)
    public void createContactShouldFailNameMinLength() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.setName("aa");
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(2)
    public void createContactShouldFailNameMaxLength() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        String name = "";
        for (int i = 0; i < 46; i++) {
            name += "a";
        }
        _contact.setName(name);
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(3)
    public void createContactShouldFailAliasEmpty() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.setAlias("");
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(4)
    public void createContactShouldFailAliasMinLength() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.setAlias("aa");
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(5)
    public void createContactShouldFailAliasMaxLength() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        String alias = "";
        for (int i = 0; i < 46; i++) {
            alias += "a";
        }
        _contact.setAlias(alias);
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(6)
    public void createContactShouldFailListPhoneEmpty() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.setPhone(new ArrayList<>());
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(7)
    public void createContactShouldFailPhoneEmpty() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.getPhone().get(0).setPhone("");
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(8)
    public void createContactShouldFailPhoneMinLength() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.getPhone().get(0).setPhone("123456");
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(9)
    public void createContactShouldFailPhoneMaxLength() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        String phoneNumber = "";
        for (int i = 0; i < 21; i++) {
            phoneNumber += "1";
        }
        _contact.getPhone().get(0).setPhone(phoneNumber);
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(10)
    public void createContactShouldFailListEmailEmpty() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.setEmail(new ArrayList<>());
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(11)
    public void createContactShouldFailEmailEmpty() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.getEmail().get(0).setEmail("");
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(12)
    public void createContactShouldFailNotValidEmail() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        _contact.getEmail().get(0).setEmail("aaa@aaaa");
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(13)
    public void createContactShouldFailEmailMaxLength() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        String emailText = "";
        for (int i = 0; i < 65; i++) {
            emailText += "a";
        }
        emailText += "@gmail.com";
        _contact.getEmail().get(0).setEmail(emailText);
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(14)
    public void updateContactShouldFailAllPhonesDeleted() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        Contact __contact = given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts").as(Contact.class);
        _contact = modelMapper.map(__contact, ContactDto.class);
        for (PhoneDto phone : _contact.getPhone()) {
            phone.setDeleted(true);
        }
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().put("/contacts/" + __contact.getId())
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    @Order(15)
    public void updateContactShouldFailAllEmailsDeleted() {
        ContactDto _contact = copyObject(contact, ContactDto.class);
        Contact __contact = given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().post("/contacts").as(Contact.class);
        _contact = modelMapper.map(__contact, ContactDto.class);
        for (EmailDto email : _contact.getEmail()) {
            email.setDeleted(true);
        }
        given().body(gson.toJson(_contact))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", authorization)
                .when().put("/contacts/" + __contact.getId())
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
