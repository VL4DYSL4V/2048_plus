package config;

import dao.model.FileSystemGameModelDao;
import dao.model.GameModelDao;
import enums.FieldDimension;
import exception.FetchException;
import model.GameModel;
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
        GameModelDao gameModelDao = applicationContext.getBean("modelDao", FileSystemGameModelDao.class);
        try {
            GameModel gameModel = gameModelDao.getByDimension(FieldDimension.FOUR_AND_FOUR);
            Model model = new Model();
            model.setGameModel(gameModel);
            return model;
        } catch (FetchException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ApplicationContext applicationContext() {
        return new AnnotationConfigApplicationContext(getClass());
    }

}
