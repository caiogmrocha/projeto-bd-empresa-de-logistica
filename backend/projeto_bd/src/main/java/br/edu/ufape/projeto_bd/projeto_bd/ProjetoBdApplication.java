package br.edu.ufape.projeto_bd.projeto_bd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjetoBdApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoBdApplication.class, args);
	}

}
