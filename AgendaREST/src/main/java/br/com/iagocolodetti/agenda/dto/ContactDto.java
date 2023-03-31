package br.com.iagocolodetti.agenda.dto;

import br.com.iagocolodetti.agenda.validation.ListEmailOneNotDeleted;
import br.com.iagocolodetti.agenda.validation.ListPhoneOneNotDeleted;
import br.com.iagocolodetti.agenda.validation.NotEmptyList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author iagocolodetti
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactDto implements Serializable {

    private static final long serialVersionUID = 2714721884860822017L;
    
    @NotBlank(message = "Defina um nome para o contato")
    @Size(min = 3, max = 45, message = "O nome do contato deve ter de {min} à {max} caracteres")
    private String name;
    @NotBlank(message = "Defina um apelido para o contato")
    @Size(min = 3, max = 45, message = "O apelido do contato deve ter de {min} à {max} caracteres")
    private String alias;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean deleted;
    @NotEmptyList(message = "Adicione ao menos um telefone")
    @ListPhoneOneNotDeleted(message = "Adicione ao menos um telefone")
    @Valid
    @JsonIgnoreProperties("contact")
    private List<PhoneDto> phone;
    @NotEmptyList(message = "Adicione ao menos um e-mail")
    @ListEmailOneNotDeleted(message = "Adicione ao menos um e-mail")
    @Valid
    @JsonIgnoreProperties("contact")
    private List<EmailDto> email;
    @JsonIgnore
    private UserDto user;
    
    @JsonIgnore
    public void addPhone(PhoneDto phone) {
        this.phone.add(phone);
    }
    
    @JsonIgnore
    public void addEmail(EmailDto email) {
        this.email.add(email);
    }
}
