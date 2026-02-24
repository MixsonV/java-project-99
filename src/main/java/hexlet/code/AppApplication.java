package hexlet.code;

import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);   //http://localhost:8080
    }

    @Bean
    public Faker getFaker() {
        return new Faker();
    }
}
