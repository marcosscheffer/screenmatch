package com.example.screenmatch.principal;

import java.util.Scanner;
import com.example.screenmatch.model.DadosSerie;
import com.example.screenmatch.model.DadosTemporada;
import com.example.screenmatch.model.Episodio;
import com.example.screenmatch.services.ConsumoApi;
import com.example.screenmatch.services.ConverteDados;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.screenmatch.model.Serie;
import com.example.screenmatch.repository.SerieRepository;
import com.example.screenmatch.model.Categoria;

public class Principal {
    private final String URL_BASE = "http://www.omdbapi.com";
    private final String API_KEY = "4630a04f";
    private SerieRepository serieRepository;
    private String nome;
    private Serie serie;
    Scanner scanner = new Scanner(System.in);
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados converteDados = new ConverteDados();

    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void listarSeries(List<Serie> series) {
        if (series.isEmpty()) {
            System.out.println("Nenhuma série encontrada.");
        } else {
            series.forEach(System.out::println);
        }
    }

    public void listarEpisodios(List<Episodio> episodios) {
        if (episodios.isEmpty()) {
            System.out.println("Nenhum episódio encontrado para esta série.");
        } else {
            episodios.forEach(System.out::println);
        }
    }

    public List<Episodio> obterEpisodios(Serie serie) {
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
            String urlTemporada = URL_BASE + "?apikey=" + API_KEY + "&t="
                    + serie.getTitulo().replace(" ", "+") + "&season=" + i;
            String temporada = consumoApi.consumirApi(urlTemporada);
            DadosTemporada dadosTemporada = converteDados.obterDados(temporada, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream().map(e -> new Episodio(t.temporada(), e)))
                .toList();
        return episodios;
    }

    public Optional<Episodio> buscarEpisodio(List<Episodio> episodios, String episodioBusca) {
        Optional<Episodio> episodioEncontrado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(episodioBusca.toUpperCase()))
                .findFirst();

        return episodioEncontrado;
    }

    public Serie buscarSerie(String nome) {
        String resposta = consumoApi
                .consumirApi(URL_BASE + "?apikey=" + API_KEY + "&t=" + nome.replace(" ", "+"));
        DadosSerie dados = converteDados.obterDados(resposta, DadosSerie.class);
        Serie serie = new Serie(dados);
        serie.setEpisodios(obterEpisodios(serie));
        Optional<Serie> serieExistente = serieRepository.findByTituloContainingIgnoreCase(serie.getTitulo());

        if (serieExistente.isEmpty()) {
            serieRepository.save(serie);
        }
        return serie;
    }

    public List<Serie> buscarAtor(String ator) {
        List<Serie> series = serieRepository.findByAtoresContainingIgnoreCase(ator);
        return series;
    }

    public void ExibirMenu() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("Menu:");
            System.out.println("1. Buscar série");
            System.out.println("2. Buscar episodio");
            System.out.println("3. Series buscadas");
            System.out.println("4. Buscar series por ator");
            System.out.println("5. Listar top 5");
            System.out.println("6. Listar por categoria");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 0:
                    System.out.println("Saindo...");
                case 1:
                    System.out.print("Digite o nome da série: ");
                    nome = scanner.nextLine();
                    serie = buscarSerie(nome);
                    System.out.println(serie);
                    break;
                case 2:
                    System.out.print("Digite o nome da série: ");
                    nome = scanner.nextLine();
                    serie = buscarSerie(nome);
                    List<Episodio> episodios = serie.getEpisodios();
                    listarEpisodios(episodios);

                    System.out.print("Digite um episodio: ");
                    String episodioBusca = scanner.nextLine();
                    Optional<Episodio> episodio = buscarEpisodio(episodios, episodioBusca);

                    if (episodio.isPresent()) {
                        System.out.println(episodio.get());
                    } else {
                        System.out.println("Episódio não encontrado.");
                    }

                    break;
                case 3:
                    List<Serie> series = serieRepository.findAll();
                    listarSeries(series);
                    break;

                case 4:
                    System.out.print("Digite o nome do ator: ");
                    String ator = scanner.nextLine();
                    List<Serie> seriesAtor = buscarAtor(ator);
                    listarSeries(seriesAtor);
                    break;

                case 5:
                    List<Serie> top5 = serieRepository.findTop5ByOrderByAvaliacaoDesc();
                    listarSeries(top5);
                    break;

                case 6:
                    System.out.print("Digite a categoria: ");
                    String categoria = scanner.nextLine();
                    List<Serie> seriesCategoria = serieRepository
                            .findByGenero(Categoria.fromPortugues(categoria));
                    listarSeries(seriesCategoria);
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }

        }
        scanner.close();
    }
}
