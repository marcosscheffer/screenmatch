package com.example.screenmatch.principal;

import java.util.Scanner;
import java.util.stream.Collectors;
import com.example.screenmatch.model.DadosSerie;
import com.example.screenmatch.model.DadosTemporada;
import com.example.screenmatch.model.Episodio;
import com.example.screenmatch.services.ConsumoApi;
import com.example.screenmatch.services.ConverteDados;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public class Principal {
    private final String URL_BASE = "http://www.omdbapi.com";
    private final String API_KEY = "4630a04f";

    public void ExibirMenu() {
        Scanner scanner = new Scanner(System.in);
        ConsumoApi consumoApi = new ConsumoApi();
        ConverteDados converteDados = new ConverteDados();

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Buscar série");
            System.out.println("2. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.print("Digite o nome da série: ");
                    String nomeSerie = scanner.nextLine();
                    String response =
                            consumoApi.consumirApi("http://www.omdbapi.com/?&apikey=4630a04f&t="
                                    + nomeSerie.replace(" ", "+"));
                    DadosSerie dadosSerie = converteDados.obterDados(response, DadosSerie.class);
                    List<DadosTemporada> temporadas = new ArrayList<>();

                    for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
                        String urlTemporada = URL_BASE + "?apikey=" + API_KEY + "&t="
                                + nomeSerie.replace(" ", "+") + "&season=" + i;
                        String temporada = consumoApi.consumirApi(urlTemporada);
                        DadosTemporada dadosTemporada =
                                converteDados.obterDados(temporada, DadosTemporada.class);
                        temporadas.add(dadosTemporada);
                    }

                    List<Episodio> episodios = temporadas.stream().flatMap(
                            t -> t.episodios().stream().map(e -> new Episodio(t.temporada(), e)))
                            .toList();

                    episodios.forEach(System.out::println);

                    System.out.print("Apartir de ano: ");
                    int ano = Integer.parseInt(scanner.nextLine());
                    LocalDate dataFiltro = LocalDate.of(ano, 1, 1);
                    episodios.stream().filter(
                            e -> e.getLancamento() != null && e.getLancamento().isAfter(dataFiltro))
                            .forEach(System.out::println);;

                    System.out.print("Digite um episodio: ");
                    String episodioBusca = scanner.nextLine();

                    Optional<Episodio> episodioEncontrado = episodios.stream().filter(
                            e -> e.getTitulo().toUpperCase().contains(episodioBusca.toUpperCase()))
                            .findFirst();

                    System.out.println(episodioEncontrado.isPresent() ? episodioEncontrado.get()
                            : "Episódio não encontrado.");

                    Map<Integer, Double> avaliacoesPorTemporada =
                            episodios.stream().filter(e -> e.getAvaliacao() != 0)
                                    .collect(Collectors.groupingBy(Episodio::getTemporada,
                                            Collectors.averagingDouble(Episodio::getAvaliacao)));

                    System.out.println(avaliacoesPorTemporada);

                    break;
                case "2":
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }
}
