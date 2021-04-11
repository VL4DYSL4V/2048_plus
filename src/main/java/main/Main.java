package main;

import config.AppConfig;
import entity.Field;
import entity.FieldElement;
import model.Model;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.saver.GameSaver;
import service.saver.PeriodicalSavingService;
import view.GameFrame;

import javax.swing.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GameFrame gameFrame = context.getBean("gameFrame", GameFrame.class);
//        EndOfGameFrame endOfGameFrame = context.getBean("endOfGameFrame", EndOfGameFrame.class);
        Model model = context.getBean("model", Model.class);
        model.subscribe(gameFrame);
//        model.subscribe(endOfGameFrame);

        SwingUtilities.invokeLater(() -> gameFrame.setVisible(true));
        PeriodicalSavingService gameSaver = context.getBean("gameSaver", GameSaver.class);
        gameSaver.start();
    }

    private static void printResult(Model model) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(model.getScores()).append('\n');
        Field f = model.getField();
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
