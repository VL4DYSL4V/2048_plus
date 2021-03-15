package main;

import config.AppConfig;
import dao.FileSystemModelDao;
import entity.Model;
import enums.FieldDimension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) throws Throwable {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Model model = context.getBean("fileSystemModelDao", FileSystemModelDao.class)
                .getByDimension(FieldDimension.FOUR_AND_FOUR);
        System.out.println(model);
    }

}
