package config;

import command.ExitAndSaveCommand;
import command.ExitCommand;
import command.MoveBackCommand;
import command.RestartCommand;
import controller.CommandExecutor;
import dao.theme.FileSystemThemeDao;
import dao.theme.ThemeDao;
import model.Model;
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
    public RenderingContext renderingContext() {
        RenderingContext renderingContext = new RenderingContext();
        Theme theme = applicationContext.getBean("theme", Theme.class);
        renderingContext.setTheme(theme);
        return renderingContext;
    }

    @Bean
    public StandardButton moveBackButton() {
        Model model = applicationContext.getBean("model", Model.class);
        ThemeHolder themeHolder = applicationContext.getBean("renderingContext", RenderingContext.class);
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new StandardButton(null, themeHolder, new MoveBackCommand(model, uiCommandExecutor));
    }

    @Bean
    public StandardButton exitAndSaveButton() {
        Runnable savingTask = applicationContext.getBean("savingTask", Runnable.class);
        ThemeHolder themeHolder = applicationContext.getBean("renderingContext", RenderingContext.class);
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new StandardButton(null, themeHolder, new ExitAndSaveCommand(uiCommandExecutor, savingTask));
    }

    @Bean
    public StandardButton exitButton() {
        ThemeHolder themeHolder = applicationContext.getBean("renderingContext", RenderingContext.class);
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new StandardButton(null, themeHolder, new ExitCommand(uiCommandExecutor));
    }

    @Bean
    @Scope("prototype")
    public StandardButton restartButton() {
        ThemeHolder themeHolder = applicationContext.getBean("renderingContext", RenderingContext.class);
        Model model = applicationContext.getBean("model", Model.class);
        Runnable savingTask = applicationContext.getBean("savingTask", Runnable.class);
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new StandardButton(null, themeHolder, new RestartCommand(model, savingTask, uiCommandExecutor));
    }

}
