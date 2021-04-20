package config;

import command.Command;
import command.DimensionChangeCommand;
import command.ExitCommand;
import command.VolatileCommand;
import command.game.MoveBackCommand;
import command.game.RestartCommand;
import command.game.ShiftFieldCommand;
import command.transition.TransitionCommand;
import preferences.UserPreferences;
import dao.game.GameDataDao;
import enums.FieldDimension;
import exception.FetchException;
import handler.CommandHandler;
import handler.UICommandHandler;
import model.GameData;
import model.GameModel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import saver.GameSaver;
import saver.PeriodicalSavingService;
import task.SavingTask;
import view.GameFrame;
import view.MainFrame;
import view.listener.FieldMovementListener;

import java.awt.event.KeyListener;
import java.util.Locale;

@Configuration
@ComponentScan({"dao", "view", "saver"})
@Import({RepositoryConfig.class, ViewConfig.class})
public class AppConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public CommandHandler uiCommandHandler() {
        return new UICommandHandler();
    }

    @Bean
    public GameModel gameModel() {
        GameModel gameModel = new GameModel();
        GameDataDao gameDataDao = applicationContext.getBean("gameDataDao", GameDataDao.class);
        GameData gameData;
        try {
            gameData = gameDataDao.getByDimension(FieldDimension.FOUR_AND_FOUR);
        } catch (FetchException e) {
            throw new RuntimeException(e);
        }
        gameModel.setGameData(gameData);
        return gameModel;
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
        Runnable savingTask = applicationContext.getBean("savingTask", Runnable.class);
        GameModel gameModel = gameModel();
        return new TransitionCommand(gameFrame, mainFrame, uiCommandHandler(), () -> {
            savingTask.run();
            gameSaver.stop();
            if(gameModel.gameIsOver()) {
                gameModel.setGameData(new GameData(gameModel.getFieldDimension()));
            }
        });
    }

    @Bean
    public Command menuToGameFrameCommand() {
        MainFrame mainFrame = applicationContext.getBean("mainFrame", MainFrame.class);
        GameFrame gameFrame = applicationContext.getBean("gameFrame", GameFrame.class);
        PeriodicalSavingService gameSaver = applicationContext.getBean("gameSaver", GameSaver.class);
        return new TransitionCommand(mainFrame, gameFrame, uiCommandHandler(), gameSaver::start);
    }

    @Bean
    public Command exitCommand() {
        return new ExitCommand(uiCommandHandler());
    }

    @Bean
    public KeyListener fieldMovementListener() {
        return new FieldMovementListener(shiftFieldCommand());
    }

    @Bean
    public VolatileCommand<FieldDimension> dimensionChangeCommand() {
        UserPreferences userPreferences = applicationContext.getBean("userPreferences", UserPreferences.class);
        GameDataDao gameDataDao = applicationContext.getBean("gameDataDao", GameDataDao.class);
        return new DimensionChangeCommand(userPreferences, uiCommandHandler(), gameModel(), gameDataDao);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages" );
        messageSource.setDefaultEncoding("UTF-8" );
        messageSource.setDefaultLocale(Locale.forLanguageTag("en" ));
        return messageSource;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
