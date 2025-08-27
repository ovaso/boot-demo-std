package x.bv.demo.std.app;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import x.bv.demo.std.ware.EnableWares;

@EnableWares
@SpringBootApplication
public class App {
	public static void main(String[] args) {

		SpringApplication.run(App.class, args);
	}
}
