package main;

import config.AppConfig;
import controller.FieldShiftController;
import controller.FieldShiftControllerImpl;
import dao.FileSystemModelDao;
import dao.ModelDao;
import entity.Field;
import entity.FieldElement;
import entity.Model;
import enums.Direction;
import enums.FieldDimension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Throwable {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ModelDao modelDao = context.getBean("fileSystemModelDao", FileSystemModelDao.class);
        Model model = modelDao.getByDimension(FieldDimension.FOUR_AND_FOUR);
        FieldShiftController controller = new FieldShiftControllerImpl(model);
        printResult(model);
        controller.shift(Direction.DOWN);
//        Thread.sleep(2000);
        printResult(model);
        model.replaceState(new Model(FieldDimension.FIVE_AND_FIVE));
//        printResult(model);

        controller.shift(Direction.DOWN);
//        Thread.sleep(2000);
        printResult(model);
        controller.shift(Direction.DOWN);
//        Thread.sleep(2000);
        printResult(model);

        System.exit(0);
    }

    private static void printResult(Model model) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(model.getScores()).append('\n');
        Field f = model.getField();
        for(int i = 0; i < f.getFieldDimension().getHeight(); i++){
            List<FieldElement> row = f.getRow(i);
            for (FieldElement fieldElement : row) {
                sb.append(fieldElement.getValue()).append('\t');
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }

}