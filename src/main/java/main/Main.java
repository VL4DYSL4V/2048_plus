package main;

import config.AppConfig;
import entity.Field;
import entity.FieldElement;
import model.GameModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.saver.GameSaver;
import service.saver.PeriodicalSavingService;
import view.EndOfGameDialog;
import view.GameFrame;
import view.MainFrame;

import javax.swing.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MainFrame mainFrame = context.getBean("mainFrame", MainFrame.class);
        GameFrame gameFrame = context.getBean("gameFrame", GameFrame.class);
        EndOfGameDialog endOfGameDialog = context.getBean("endOfGameDialog", EndOfGameDialog.class);
        GameModel gameModel = context.getBean("gameModel", GameModel.class);
        gameModel.subscribe(gameFrame);
        gameModel.subscribe(endOfGameDialog);

        SwingUtilities.invokeLater(() -> gameFrame.setVisible(true));
        PeriodicalSavingService gameSaver = context.getBean("gameSaver", GameSaver.class);
        gameSaver.start();
    }

    private static void printResult(GameModel gameModel) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(gameModel.getScores()).append('\n');
        Field f = gameModel.getField();
        for (int i = 0; i < f.getFieldDimension().getHeight(); i++) {
            List<FieldElement> row = f.getRow(i);
            for (FieldElement fieldElement : row) {
                sb.append(fieldElement.getValue()).append('\t');
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }

}
