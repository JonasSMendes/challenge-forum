package api.forum_duvida.repository;

import api.forum_duvida.domain.postagem.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Postagem p WHERE p.autor.id = :autorId AND p.titulo = :titulo AND p.mensagem = :mensagem")
    boolean existsByTituloAndMensagemAndAutorId(@Param("titulo") String titulo, @Param("mensagem") String mensagem, @Param("autorId") Long autorId);

    List<Postagem> findTop10ByOrderByDataAsc();


    @Query("SELECT p FROM Postagem p WHERE p.curso = :curso AND EXTRACT(YEAR FROM p.data) = :ano")
    List<Postagem> findByCursoAndAno(@Param("curso") String curso, @Param("ano") int ano);
}
