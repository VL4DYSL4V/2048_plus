package config;

import command.*;
import dao.game.FileSystemGameDataDao;
import dao.game.GameDataDao;
import enums.FieldDimension;
import exception.FetchException;
import model.GameData;
import model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import service.ui.executor.CommandExecutor;
import task.SavingTask;
import view.listener.FieldMovementListener;

import java.awt.event.KeyListener;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

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
    public GameDataDao gameDataDao(){
        @SuppressWarnings("unchecked")
        Map<FieldDimension, Path> repositories = (Map<FieldDimension, Path>) applicationContext.getBean("repositories");
        return new FileSystemGameDataDao(repositories);
    }

    @Bean
    public GameModel gameModel() {
        try {
            GameModel gameModel = new GameModel();
            GameData gameData = gameDataDao().getByDimension(FieldDimension.FOUR_AND_FOUR);
            gameModel.setGameData(gameData);
            return gameModel;
        } catch (FetchException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ApplicationContext applicationContext() {
        return new AnnotationConfigApplicationContext(getClass());
    }

    @Bean
    public Runnable savingTask() {
        GameDataDao gameDataDao = applicationContext.getBean("gameDataDao", GameDataDao.class);
        return new SavingTask(gameModel(), gameDataDao);
    }

    @Bean
    public ShiftFieldCommand shiftFieldCommand() {
        CommandExecutor commandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        GameModel gameModel = applicationContext.getBean("gameModel", GameModel.class);
        return new ShiftFieldCommand(commandExecutor, gameModel);
    }

    @Bean
    public Command moveBackCommand() {
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new MoveBackCommand(gameModel(), uiCommandExecutor);
    }

    @Bean
    public Command restartCommand() {
        Runnable savingTask = applicationContext.getBean("savingTask", Runnable.class);
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new RestartCommand(gameModel(), savingTask, uiCommandExecutor);
    }

    @Bean
    public Command gameFrameToMenuCommand() {
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new GameFrameToMenuCommand(uiCommandExecutor);
    }

    @Bean
    public KeyListener fieldMovementListener() {
        ShiftFieldCommand filedShiftCommand = applicationContext.getBean("shiftFieldCommand", ShiftFieldCommand.class);
        return new FieldMovementListener(filedShiftCommand);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.forLanguageTag("en"));
        return messageSource;
    }
}
