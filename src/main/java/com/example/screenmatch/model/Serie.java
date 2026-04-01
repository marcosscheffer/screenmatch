package com.example.screenmatch.model;

import java.util.OptionalDouble;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String sinopse;
    private String atores;
    private String poster;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public Serie() {}

    public Serie(DadosSerie dados) {
        this.titulo = dados.titulo();
        this.totalTemporadas = dados.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dados.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dados.genero().split(",")[0].trim());
        this.sinopse = dados.sinopse();
        this.atores = dados.atores();
        this.poster = dados.poster();
    }

    public void adicionarEpisodio(Episodio episodio) {
        episodios.add(episodio);
        episodio.setSerie(this);
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public String getSinopse() {
        return sinopse;
    }

    public String getAtores() {
        return atores;
    }

    public String getPoster() {
        return poster;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    @Override
    public String toString() {
        return String.format(
                "Título: %s\nTotal de Temporadas: %d\nAvaliação: %.1f\nGênero: %s\nSinopse: %s\nAtores: %s\nPoster: %s",
                titulo, totalTemporadas, avaliacao, genero, sinopse, atores, poster);
    }

}
