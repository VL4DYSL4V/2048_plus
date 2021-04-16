package main;

import config.AppConfig;
import context.UserPreferences;
import context.UserPreferencesImpl;
import model.GameModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.saver.GameSaver;
import service.saver.PeriodicalSavingService;
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
        gameModel.subscribe(gameFrame);
        gameModel.subscribe(endOfGameDialog);
        UserPreferences userPreferences = context.getBean("viewContext", UserPreferences.class);
        UserPreferencesImpl viewContextImpl = (UserPreferencesImpl) userPreferences;
        viewContextImpl.subscribe(gameFrame);
        viewContextImpl.subscribe(endOfGameDialog);
        viewContextImpl.subscribe(mainFrame);

        SwingUtilities.invokeLater(() -> mainFrame.setVisible(true));
        PeriodicalSavingService gameSaver = context.getBean("gameSaver", GameSaver.class);
        gameSaver.start();

        new Thread(() -> {
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            viewContextImpl.setLocale(new Locale("ru"));
        }).start();
    }

}
