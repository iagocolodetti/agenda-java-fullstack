package br.com.iagocolodetti.agenda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class EmailDto implements Serializable {

    private static final long serialVersionUID = 2718421884860822017L;

    private Integer id;
    @NotBlank(message = "Defina um e-mail para o contato")
    @Email(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", message = "Esse não é um e-mail válido")
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean deleted;
    private ContactDto contact;
}
