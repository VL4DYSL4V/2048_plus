package config;

import controller.exit.ExitController;
import controller.exit.ExitControllerImpl;
import dao.FileSystemModelDao;
import dao.ModelDao;
import enums.FieldDimension;
import exception.FetchException;
import model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    public Model model(){
        ModelDao modelDao = applicationContext.getBean("fileSystemModelDao", FileSystemModelDao.class);
        try {
            return modelDao.getByDimension(FieldDimension.FOUR_AND_FOUR);
        }catch (FetchException e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ApplicationContext applicationContext(){
        return new AnnotationConfigApplicationContext(getClass());
    }

}
