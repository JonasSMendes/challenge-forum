package api.forum_duvida.controller;

import api.forum_duvida.domain.postagem.DadosPostagem;
import api.forum_duvida.domain.postagem.service.PostagemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/topicos")
@RestController
@SecurityRequirement(name = "bearer-key")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    @PostMapping
    @Transactional
    public ResponseEntity postagemForum(@RequestBody @Valid DadosPostagem dados){
        var dto = postagemService.agendar(dados);
        return ResponseEntity.ok(dto);
    }
}
