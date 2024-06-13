package api.forum_duvida.domain.postagem.validation;

import api.forum_duvida.domain.ValidacaoException;
import api.forum_duvida.domain.postagem.DadosPostagem;
import api.forum_duvida.domain.usuario.Usuario;
import api.forum_duvida.repository.PostagemRepository;
import api.forum_duvida.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidadorTituloMensagemRepetidasDoMesmoUsuario implements ValidadorPostagem {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validador(DadosPostagem dados) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Optional) {
            Optional<?> optionalPrincipal = (Optional<?>) principal;
            principal = optionalPrincipal.orElseThrow(() -> new ValidacaoException("Principal não contém um usuário válido: " + optionalPrincipal));
        }

        Usuario autor;
        if (principal instanceof Usuario) {
            autor = (Usuario) principal;
        } else if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            autor = usuarioRepository.findByLogin(username)
                    .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));
        } else {
            throw new IllegalStateException("Não foi possível determinar o nome de usuário a partir do principal: " + principal);
        }

        if (!isValid(dados, autor.getId())) {
            throw new ValidacaoException("não é permitido tópicos repetidos");
        }
    }

    public Boolean isValid(DadosPostagem dados, Long idAutor) {
        return !postagemRepository.existsByTituloAndMensagemAndAutorId(dados.titulo(), dados.mensagem(), idAutor);
    }


}
