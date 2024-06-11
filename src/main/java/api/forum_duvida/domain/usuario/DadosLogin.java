package api.forum_duvida.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosLogin(
        String login,
        String senha
) {
}
