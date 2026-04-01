package com.example.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String titulo;
    private Integer episodio;
    private Double avaliacao;
    private Integer temporada;
    private LocalDate lancamento;

    @ManyToOne
    @JoinColumn(name = "serie_id")
    private Serie serie;

    public Episodio() {
    }

    public Episodio(Integer temporada, DadosEpisodio dadosEpisodio) {
        this.temporada = temporada;
        this.titulo = dadosEpisodio.titulo();
        this.episodio = dadosEpisodio.episodio();

        try {
            this.lancamento = LocalDate.parse(dadosEpisodio.lancamento());
        } catch (DateTimeParseException e) {
            this.lancamento = null;
        }

        try {
            this.avaliacao = Double.parseDouble(dadosEpisodio.avaliacao());
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getEpisodio() {
        return episodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public LocalDate getLancamento() {
        return lancamento;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setEpisodio(Integer episodio) {
        this.episodio = episodio;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public void setLancamento(LocalDate lancamento) {
        this.lancamento = lancamento;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Temporada %d, Episódio %d: %s (Lançamento: %s, Avaliação: %.1f)",
                temporada, episodio, titulo,
                lancamento != null ? lancamento.format(formato) : "N/A", avaliacao);
    }
}
