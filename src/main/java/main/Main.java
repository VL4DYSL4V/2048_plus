package main;

import config.AppConfig;
import preferences.UserPreferences;
import model.GameModel;
import observer.Subscriber;
import observer.event.ModelEvent;
import observer.event.UserPreferencesEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import saver.GameSaver;
import saver.PeriodicalSavingService;
import view.EndOfGameDialog;
import view.GameFrame;
import view.MainFrame;

import javax.swing.*;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MainFrame mainFrame = context.getBean("mainFrame", MainFrame.class);
        GameFrame gameFrame = context.getBean("gameFrame", GameFrame.class);
        EndOfGameDialog endOfGameDialog = context.getBean("endOfGameDialog", EndOfGameDialog.class);
        GameModel gameModel = context.getBean("gameModel", GameModel.class);

        Subscriber<ModelEvent> gameFrameModelEventSubscriber = gameFrame.new ModelListener();
        gameModel.subscribe(gameFrameModelEventSubscriber);
        Subscriber<ModelEvent> endOfGameModelEventSubscriber = endOfGameDialog.new ModelListener();
        gameModel.subscribe(endOfGameModelEventSubscriber);
        UserPreferences userPreferences = context.getBean("userPreferences", UserPreferences.class);
        UserPreferences viewContextImpl = (UserPreferences) userPreferences;

        Subscriber<UserPreferencesEvent> gameFramePreferencesEventSubscriber = gameFrame.new PreferencesListener();
        viewContextImpl.subscribe(gameFramePreferencesEventSubscriber);
        Subscriber<UserPreferencesEvent> endOfGamePreferencesEventSubscriber = endOfGameDialog.new PreferencesListener();
        viewContextImpl.subscribe(endOfGamePreferencesEventSubscriber);
        viewContextImpl.subscribe(mainFrame);

        SwingUtilities.invokeLater(() -> {
            mainFrame.setVisible(true);
        });
        PeriodicalSavingService gameSaver = context.getBean("gameSaver", GameSaver.class);
        gameSaver.start();

        new Thread(() -> {
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            viewContextImpl.setLocale(new Locale("ru" ));
        }).start();
    }

}
