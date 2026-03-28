package com.example.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Episodio {
    private String titulo;
    private Integer episodio;
    private Double avaliacao;
    private Integer temporada;
    private LocalDate lancamento;

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

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Temporada %d, Episódio %d: %s (Lançamento: %s, Avaliação: %.1f)",
                temporada, episodio, titulo,
                lancamento != null ? lancamento.format(formato) : "N/A", avaliacao);
    }
}
