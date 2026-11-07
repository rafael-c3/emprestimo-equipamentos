package br.edu.prometheus.msemprestimo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsemprestimoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsemprestimoApplication.class, args);
	}

}
