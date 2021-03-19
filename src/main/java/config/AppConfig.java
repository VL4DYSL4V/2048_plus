package config;

import model.Model;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan({"controller", "dao", "view"})
@Import({RepositoryConfig.class, ViewConfig.class})
public class AppConfig {

    @Bean
    public Model model(){
        return new Model();
    }

    @Bean
    public ApplicationContext applicationContext(){
        return new AnnotationConfigApplicationContext(getClass());
    }

}
