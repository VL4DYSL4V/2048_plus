package config;

import command.Command;
import command.ExitCommand;
import command.game.MoveBackCommand;
import command.game.RestartCommand;
import command.game.ShiftFieldCommand;
import command.transition.TransitionCommand;
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
import service.saver.GameSaver;
import service.saver.PeriodicalSavingService;
import handler.CommandHandler;
import handler.UICommandHandler;
import task.SavingTask;
import view.GameFrame;
import view.MainFrame;
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
    public GameDataDao gameDataDao() {
        @SuppressWarnings("unchecked")
        Map<FieldDimension, Path> repositories = (Map<FieldDimension, Path>) applicationContext.getBean("repositories");
        return new FileSystemGameDataDao(repositories);
    }

    @Bean
    public CommandHandler uiCommandHandler() {
        return new UICommandHandler();
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
        GameModel gameModel = applicationContext.getBean("gameModel", GameModel.class);
        return new ShiftFieldCommand(uiCommandHandler(), gameModel);
    }

    @Bean
    public Command moveBackCommand() {
        return new MoveBackCommand(gameModel(), uiCommandHandler());
    }

    @Bean
    public Command restartCommand() {
        Runnable savingTask = applicationContext.getBean("savingTask", Runnable.class);
        return new RestartCommand(gameModel(), savingTask, uiCommandHandler());
    }

    @Bean
    public Command gameFrameToMenuCommand() {
        MainFrame mainFrame = applicationContext.getBean("mainFrame", MainFrame.class);
        GameFrame gameFrame = applicationContext.getBean("gameFrame", GameFrame.class);
        PeriodicalSavingService gameSaver = applicationContext.getBean("gameSaver", GameSaver.class);
        return new TransitionCommand(gameFrame, mainFrame, uiCommandHandler(), gameSaver::stop);
    }

    @Bean
    public Command menuToGameFrameCommand() {
        MainFrame mainFrame = applicationContext.getBean("mainFrame", MainFrame.class);
        GameFrame gameFrame = applicationContext.getBean("gameFrame", GameFrame.class);
        PeriodicalSavingService gameSaver = applicationContext.getBean("gameSaver", GameSaver.class);
        return new TransitionCommand(mainFrame, gameFrame, uiCommandHandler(), gameSaver::start);
    }

    @Bean
    public Command exitCommand(){
        return new ExitCommand(uiCommandHandler());
    }

    @Bean
    public KeyListener fieldMovementListener() {
        return new FieldMovementListener(shiftFieldCommand());
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
