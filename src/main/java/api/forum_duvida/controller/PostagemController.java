package api.forum_duvida.controller;

import api.forum_duvida.domain.postagem.DadosDetalhamentoPostagem;
import api.forum_duvida.domain.postagem.DadosPostagem;
import api.forum_duvida.domain.postagem.dto.DadosPostagemDTO;
import api.forum_duvida.domain.postagem.service.PostagemService;
import api.forum_duvida.repository.PostagemRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/topicos")
@RestController
@SecurityRequirement(name = "bearer-key")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    @Autowired
    private PostagemRepository postagemRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoPostagem> criarTopico(@RequestBody @Valid DadosPostagem dados) {
        DadosDetalhamentoPostagem detalhamentoPostagem = postagemService.postar(dados);
        return ResponseEntity.ok(detalhamentoPostagem);
    }

    @GetMapping
    public ResponseEntity<Page<DadosPostagemDTO>> listarPostagem(@PageableDefault(size = 10, sort = "data") Pageable pageable){
        var result = postagemRepository.findAll(pageable)
                .map(DadosPostagemDTO::new);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/top10")
    public ResponseEntity<List<DadosPostagemDTO>> top10Topico(){
        var result = postagemRepository.findTop10ByOrderByDataAsc()
                .stream().map(DadosPostagemDTO::new)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/buscar-ano-data")
    public ResponseEntity<List<DadosPostagemDTO>> buscandoTopicoPorCriteiroDeBusca(@RequestParam String curso, @RequestParam int ano){
        List<DadosPostagemDTO> postagens = postagemService.buscarPostagemPorNomeEAno(curso, ano)
                .stream()
                .map(DadosPostagemDTO::new)
                .toList();

        return ResponseEntity.ok(postagens);
    }


    @GetMapping("/{id}")
    public ResponseEntity topicoEspecifico(@PathVariable Long id){
        var result = postagemService.buscaPorId(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizacaoTopico(@RequestBody @Valid DadosPostagem dados){
        var result = postagemService.atualizarTopico(dados);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id){
        postagemService.deleteTopico(id);
        return ResponseEntity.noContent().build();
    }


}
