package com.example.screenmatch.model;

public enum Categoria {
    ACAO("action", "acao"), AVENTURA("adventure", "aventura"), COMEDIA("comedy", "comedia"), DRAMA(
            "drama", "drama"), FICCAO_CIENTIFICA("sci-fi", "ficção cientifica"), TERROR("horror",
                    "terror"), ROMANCE("romance", "romance"), ANIMACAO("animation",
                            "animação"), DOCUMENTARIO("documentary", "documentário");

    private String categoriaombd;
    private String categoriaPortugues;

    Categoria(String categoriaombd, String categoriaPortugues) {
        this.categoriaombd = categoriaombd;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromPortugues(String categoria) {
        for (Categoria c : Categoria.values()) {
            if (c.categoriaPortugues.equalsIgnoreCase(categoria)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria desconhecida: " + categoria);

    }

    public static Categoria fromString(String categoria) {
        for (Categoria c : Categoria.values()) {
            if (c.categoriaombd.equalsIgnoreCase(categoria)) {
                return c;
            }
        }

        throw new IllegalArgumentException("Categoria desconhecida: " + categoria);
    }
}
