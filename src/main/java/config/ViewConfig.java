package config;

import command.ExitAndSaveCommand;
import command.ExitCommand;
import command.MoveBackCommand;
import command.RestartCommand;
import controller.exit.ExitController;
import controller.moveBack.MoveBackController;
import controller.restart.RestartController;
import controller.restart.RestartControllerImpl;
import dao.theme.FileSystemThemeDao;
import dao.theme.ThemeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import view.component.StandardButton;
import view.context.RenderingContext;
import view.context.ThemeHolder;
import view.theme.Theme;

@Configuration
public class ViewConfig {

    private final ApplicationContext applicationContext;

    @Autowired
    public ViewConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public Theme theme() {
        return darkTheme();
    }

    @Bean
    public Theme darkTheme() {
        ThemeDao themeDao = applicationContext.getBean("themeDao", FileSystemThemeDao.class);
        return themeDao.loadTheme("classpath:theme/dark.properties");
    }

    @Bean
    public RenderingContext renderingContext(){
        RenderingContext renderingContext = new RenderingContext();
        Theme theme = applicationContext.getBean("theme", Theme.class);
        renderingContext.setTheme(theme);
        return renderingContext;
    }

    @Bean
    public StandardButton moveBackButton() {
        MoveBackController moveBackController = applicationContext.getBean("moveBackController", MoveBackController.class);
        ThemeHolder themeHolder = applicationContext.getBean("renderingContext", RenderingContext.class);
        return new StandardButton(null, themeHolder, new MoveBackCommand(moveBackController));
    }

    @Bean
    public StandardButton exitAndSaveButton() {
        ExitController exitController = applicationContext.getBean("exitController", ExitController.class);
        ThemeHolder themeHolder = applicationContext.getBean("renderingContext", RenderingContext.class);
        Runnable savingTask = applicationContext.getBean("savingTask", Runnable.class);
        return new StandardButton(null, themeHolder, new ExitAndSaveCommand(exitController, savingTask));
    }

    @Bean
    public StandardButton exitButton() {
        ExitController exitController = applicationContext.getBean("exitController", ExitController.class);
        ThemeHolder themeHolder = applicationContext.getBean("renderingContext", RenderingContext.class);
        return new StandardButton(null, themeHolder, new ExitCommand(exitController));
    }

    @Bean
    @Scope("prototype")
    public StandardButton restartButton() {
        RestartController restartController = applicationContext.getBean("restartController", RestartControllerImpl.class);
        ThemeHolder themeHolder = applicationContext.getBean("renderingContext", RenderingContext.class);
        return new StandardButton(null, themeHolder, new RestartCommand(restartController));
    }

}
