package config;

import command.Command;
import command.VolatileCommand;
import command.game.MoveBackCommand;
import command.game.RestartCommand;
import command.game.ShiftFieldCommand;
import command.menu.DimensionChangeCommand;
import command.menu.ExitCommand;
import command.settings.LocaleChangeCommand;
import command.settings.ThemeChangeCommand;
import command.transition.TransitionCommand;
import dao.game.GameDataDao;
import dao.preferences.PreferencesDAO;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import preferences.UserPreferences;
import saver.GameSaver;
import saver.PeriodicalSavingService;
import task.SavingTask;
import view.GameFrame;
import view.MainFrame;
import view.SettingsFrame;
import view.listener.FieldMovementListener;
import view.theme.Theme;

import java.awt.event.KeyListener;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

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
        UserPreferences userPreferences = applicationContext.getBean("userPreferences", UserPreferences.class);
        GameData gameData;
        try {
            gameData = gameDataDao.getByDimension(userPreferences.getFieldDimension());
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
            if (gameModel.gameIsOver()) {
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
    public Command menuToSettingsCommand() {
        MainFrame mainFrame = applicationContext.getBean("mainFrame", MainFrame.class);
        SettingsFrame settingsFrame = applicationContext.getBean("settingsFrame", SettingsFrame.class);
        return new TransitionCommand(mainFrame, settingsFrame, uiCommandHandler());
    }

    @Bean
    public Command settingsToMenuCommand() {
        MainFrame mainFrame = applicationContext.getBean("mainFrame", MainFrame.class);
        SettingsFrame settingsFrame = applicationContext.getBean("settingsFrame", SettingsFrame.class);
        return new TransitionCommand(settingsFrame, mainFrame, uiCommandHandler());
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
        PreferencesDAO preferencesDAO = applicationContext.getBean("preferencesDao", PreferencesDAO.class);
        return new DimensionChangeCommand(userPreferences, preferencesDAO, uiCommandHandler(), gameModel(), gameDataDao);
    }

    @Bean
    public VolatileCommand<Locale> languageChangeCommand() {
        UserPreferences userPreferences = applicationContext.getBean("userPreferences", UserPreferences.class);
        CommandHandler commandHandler = applicationContext.getBean("uiCommandHandler", CommandHandler.class);
        PreferencesDAO preferencesDAO = applicationContext.getBean("preferencesDao", PreferencesDAO.class);
        return new LocaleChangeCommand(commandHandler, userPreferences, preferencesDAO);
    }

    @Bean
    public VolatileCommand<Theme> themeChangeCommand() {
        UserPreferences userPreferences = applicationContext.getBean("userPreferences", UserPreferences.class);
        CommandHandler commandHandler = applicationContext.getBean("uiCommandHandler", CommandHandler.class);
        PreferencesDAO preferencesDAO = applicationContext.getBean("preferencesDao", PreferencesDAO.class);
        return new ThemeChangeCommand(commandHandler, userPreferences, preferencesDAO);
    }

    @Bean
    public Map<String, FieldDimension> supportedDimensions() {
        Map<String, FieldDimension> dimensionMap = new LinkedHashMap<>();
        dimensionMap.put("3 x 3", FieldDimension.THREE_AND_THREE);
        dimensionMap.put("4 x 4", FieldDimension.FOUR_AND_FOUR);
        dimensionMap.put("5 x 5", FieldDimension.FIVE_AND_FIVE);
        dimensionMap.put("6 x 6", FieldDimension.SIX_AND_SIX);
        return dimensionMap;
    }

    @Bean
    public Map<String, Locale> supportedLocales() {
        Map<String, Locale> localeMap = new LinkedHashMap<>();
        localeMap.put("English", Locale.ENGLISH);
        localeMap.put("Русский", Locale.forLanguageTag("ru"));
        localeMap.put("Українська", Locale.forLanguageTag("ua"));
        return localeMap;
    }

    @Bean
    public Map<String, Theme> supportedThemes() {
        Map<String, Theme> themeMap = new LinkedHashMap<>();
        Theme darkTheme = applicationContext.getBean("darkTheme", Theme.class);
        themeMap.put(darkTheme.getName(), darkTheme);
        Theme brightTheme = applicationContext.getBean("brightTheme", Theme.class);
        themeMap.put(brightTheme.getName(), brightTheme);
        return themeMap;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.forLanguageTag("en"));
        return messageSource;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
