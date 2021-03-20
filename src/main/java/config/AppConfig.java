package config;

import dao.model.FileSystemModelDao;
import dao.model.ModelDao;
import enums.FieldDimension;
import exception.FetchException;
import model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan({"controller", "dao", "view"})
@Import({RepositoryConfig.class, ViewConfig.class})
public class AppConfig {

    private final ApplicationContext applicationContext;

    @Autowired
    public AppConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public Model model() {
        ModelDao modelDao = applicationContext.getBean("modelDao", FileSystemModelDao.class);
        try {
            return modelDao.getByDimension(FieldDimension.FOUR_AND_FOUR);
        } catch (FetchException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ApplicationContext applicationContext() {
        return new AnnotationConfigApplicationContext(getClass());
    }

}
