package br.com.iagocolodetti.agenda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
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
public class PhoneDto implements Serializable {

    private static final long serialVersionUID = 3814721562860822017L;

    private Integer id;
    @NotBlank(message = "Defina um telefone para o contato")
    @Size(min = 7, max = 20, message = "O telefone deve ter de {min} à {max} dígitos")
    private String phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean deleted;
    private ContactDto contact;
}
