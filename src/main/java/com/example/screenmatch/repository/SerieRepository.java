package com.example.screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.screenmatch.model.Serie;
import java.util.Optional;
import javax.management.Query;
import java.util.List;
import com.example.screenmatch.model.Categoria;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCase(String nomeSerie);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria genero);

}
