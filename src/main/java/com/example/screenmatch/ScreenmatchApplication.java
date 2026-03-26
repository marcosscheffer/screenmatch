package com.example.screenmatch;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.screenmatch.model.DadosSerie;
import com.example.screenmatch.services.ConsumoApi;
import com.example.screenmatch.services.ConverteDados;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoApi consumoApi = new ConsumoApi();
		String response =
				consumoApi.consumirApi("http://www.omdbapi.com/?&apikey=4630a04f&t=gilmore+girls");
		System.out.println(response);

		ConverteDados converteDados = new ConverteDados();
		DadosSerie dadosSerie = converteDados.obterDados(response, DadosSerie.class);
		System.out.println(dadosSerie);

	}

}
