package api.forum_duvida.domain.postagem.service;

import api.forum_duvida.domain.ValidacaoException;
import api.forum_duvida.domain.postagem.DadosDetalhamentoPostagem;
import api.forum_duvida.domain.postagem.DadosPostagem;
import api.forum_duvida.domain.postagem.Postagem;
import api.forum_duvida.domain.postagem.dto.DadosPostagemDTO;
import api.forum_duvida.domain.postagem.validation.ValidadorPostagem;
import api.forum_duvida.domain.usuario.AuthenticationLogin;
import api.forum_duvida.domain.usuario.Usuario;
import api.forum_duvida.repository.PostagemRepository;
import api.forum_duvida.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private List<ValidadorPostagem> validador;

    @Autowired
    private AuthenticationLogin authenticationLogin;


    public DadosDetalhamentoPostagem postar(DadosPostagem dados) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Optional) {
            Optional<?> optionalPrincipal = (Optional<?>) principal;
            System.out.println("Conteúdo do Optional principal: " + optionalPrincipal);
        }

        Usuario usuario;
        if (principal instanceof Optional) {
            Optional<?> optionalPrincipal = (Optional<?>) principal;
            if (optionalPrincipal.isPresent() && optionalPrincipal.get() instanceof Usuario) {
                usuario = (Usuario) optionalPrincipal.get();
            } else {
                throw new IllegalStateException("Principal não contém um usuário válido: " + optionalPrincipal);
            }
        } else if (principal instanceof Usuario) {
            usuario = (Usuario) principal;
        } else if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            usuario = usuarioRepository.findByLogin(username)
                    .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));
        } else {
            throw new IllegalStateException("Não foi possível determinar o nome de usuário a partir do principal: " + principal);
        }

        validador.forEach(v -> v.validador(dados));
        var postagem = new Postagem(dados, usuario);
        postagemRepository.save(postagem);

        return new DadosDetalhamentoPostagem(postagem);
    }




    public List<Postagem> buscarPostagemPorNomeEAno(String curso, int ano){
        return postagemRepository.findByCursoAndAno(curso,ano);
    }

    public DadosPostagemDTO buscaPorId(Long id) {
        var postagemId = postagemRepository.getReferenceById(id);

        if (postagemId == null){
            throw new ValidacaoException("essa postagem não existe");
        }
        return new DadosPostagemDTO(postagemId);
    }

    public DadosPostagem atualizarTopico(DadosPostagem dados) {
        var postagem = postagemRepository.getReferenceById(dados.id());
        if (postagem == null){
            throw new ValidacaoException("essa postagem não existe");
        }

        validador.forEach(v -> v.validador(dados));
        postagem.atualizaInfo(dados);

        return new DadosPostagem(postagem);
    }

    public void deleteTopico(Long id) {
        var resul = postagemRepository.findById(id);
        if (!resul.isPresent()){
            throw new ValidacaoException("id não encontrado");
        }

        postagemRepository.deleteById(id);
    }
}
