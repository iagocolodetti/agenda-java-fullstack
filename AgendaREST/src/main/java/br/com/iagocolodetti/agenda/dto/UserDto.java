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
public class UserDto implements Serializable {

    private static final long serialVersionUID = -6277535549786010158L;

    @NotBlank(message = "Defina um nome de usuário")
    @Size(min = 3, max = 45, message = "O nome de usuário deve ter de {min} à {max} caracteres")
    private String username;
    @NotBlank(message = "Defina uma senha")
    @Size(min = 5, max = 60, message = "A senha deve ter de {min} à {max} caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
