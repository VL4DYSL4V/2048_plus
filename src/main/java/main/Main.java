package main;

import config.AppConfig;
import model.GameModel;
import observer.Subscriber;
import observer.event.ModelEvent;
import observer.event.UserPreferencesEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import preferences.UserPreferences;
import saver.GameSaver;
import saver.PeriodicalSavingService;
import view.EndOfGameDialog;
import view.GameFrame;
import view.MainFrame;
import view.SettingsFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MainFrame mainFrame = context.getBean("mainFrame", MainFrame.class);
        GameFrame gameFrame = context.getBean("gameFrame", GameFrame.class);
        EndOfGameDialog endOfGameDialog = context.getBean("endOfGameDialog", EndOfGameDialog.class);
        GameModel gameModel = context.getBean("gameModel", GameModel.class);
        SettingsFrame settingsFrame = context.getBean("settingsFrame", SettingsFrame.class);

        Subscriber<ModelEvent> gameFrameModelEventSubscriber = gameFrame.new ModelListener();
        gameModel.subscribe(gameFrameModelEventSubscriber);
        Subscriber<ModelEvent> endOfGameModelEventSubscriber = endOfGameDialog.new ModelListener();
        gameModel.subscribe(endOfGameModelEventSubscriber);
        UserPreferences userPreferences = context.getBean("userPreferences", UserPreferences.class);

        Subscriber<UserPreferencesEvent> gameFramePreferencesEventSubscriber = gameFrame.new PreferencesListener();
        userPreferences.subscribe(gameFramePreferencesEventSubscriber);
        Subscriber<UserPreferencesEvent> endOfGamePreferencesEventSubscriber = endOfGameDialog.new PreferencesListener();
        userPreferences.subscribe(endOfGamePreferencesEventSubscriber);
        userPreferences.subscribe(mainFrame);
        userPreferences.subscribe(settingsFrame);

        SwingUtilities.invokeLater(() -> mainFrame.setVisible(true));
        PeriodicalSavingService gameSaver = context.getBean("gameSaver", GameSaver.class);
        gameSaver.start();

    }

}
