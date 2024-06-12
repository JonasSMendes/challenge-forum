package api.forum_duvida.domain.postagem.service;

import api.forum_duvida.domain.ValidacaoException;
import api.forum_duvida.domain.postagem.DadosDetalhamentoPostagem;
import api.forum_duvida.domain.postagem.DadosPostagem;
import api.forum_duvida.domain.postagem.Postagem;
import api.forum_duvida.domain.postagem.dto.DadosPostagemDTO;
import api.forum_duvida.domain.postagem.validation.ValidadorPostagem;
import api.forum_duvida.repository.PostagemRepository;
import api.forum_duvida.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public DadosDetalhamentoPostagem agendar(DadosPostagem dados) {

        if (!usuarioRepository.existsById(dados.id_autor())){
            throw new ValidacaoException("Id do usuario não encontratado");
        }

        validador.forEach(v -> v.validador(dados));

        var usuario = usuarioRepository.getReferenceById(dados.id_autor());
        var postagem = new Postagem(dados.titulo(), dados.mensagem(), usuario, dados.curso());

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
