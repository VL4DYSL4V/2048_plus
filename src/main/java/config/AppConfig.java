package config;

import entity.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"config", "dao"})
@Import(RepositoryConfig.class)
public class AppConfig {

    @Bean
    public Model model(){
        return new Model();
    }

}
