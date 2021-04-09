package config;

import command.ShiftFieldCommand;
import service.ui.executor.CommandExecutor;
import dao.model.FileSystemGameModelDao;
import dao.model.GameModelDao;
import enums.FieldDimension;
import exception.FetchException;
import model.GameModel;
import model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import task.SavingTask;

@Configuration
@ComponentScan({"dao", "view", "service"})
@Import({RepositoryConfig.class, ViewConfig.class})
public class AppConfig {

    private final ApplicationContext applicationContext;

    @Autowired
    public AppConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public Model model() {
        GameModelDao gameModelDao = applicationContext.getBean("gameModelDao", FileSystemGameModelDao.class);
        try {
            Model model = new Model();
            GameModel gameModel = gameModelDao.getByDimension(FieldDimension.FOUR_AND_FOUR);
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

    @Bean
    public Runnable savingTask(){
        Model model = applicationContext.getBean("model", Model.class);
        GameModelDao gameModelDao = applicationContext.getBean("gameModelDao", GameModelDao.class);
        return new SavingTask(model, gameModelDao);
    }

    @Bean
    public ShiftFieldCommand shiftFieldCommand(){
        CommandExecutor commandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        Model model = applicationContext.getBean("model", Model.class);
        return new ShiftFieldCommand(commandExecutor, model);
    }
}
