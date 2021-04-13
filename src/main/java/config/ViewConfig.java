package config;

import command.ExitAndSaveCommand;
import command.ExitCommand;
import command.MoveBackCommand;
import command.RestartCommand;
import dao.theme.FileSystemThemeDao;
import dao.theme.ThemeDao;
import model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import service.ui.executor.CommandExecutor;
import view.EndOfGameDialog;
import view.GameFrame;
import view.component.StandardButton;
import view.theme.Theme;

import java.awt.*;

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
    public StandardButton moveBackButton() {
        Model model = applicationContext.getBean("model", Model.class);
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new StandardButton(null, theme(), new MoveBackCommand(model, uiCommandExecutor));
    }

    @Bean
    public StandardButton exitAndSaveButton() {
        Runnable savingTask = applicationContext.getBean("savingTask", Runnable.class);
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new StandardButton(null, theme(), new ExitAndSaveCommand(uiCommandExecutor, savingTask));
    }

    @Bean
    public StandardButton exitButton() {
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new StandardButton(null, theme(), new ExitCommand(uiCommandExecutor));
    }

    @Bean
    @Scope("prototype")
    public StandardButton restartButton() {
        Model model = applicationContext.getBean("model", Model.class);
        Runnable savingTask = applicationContext.getBean("savingTask", Runnable.class);
        CommandExecutor uiCommandExecutor = applicationContext.getBean("uiCommandExecutor", CommandExecutor.class);
        return new StandardButton(null, theme(), new RestartCommand(model, savingTask, uiCommandExecutor));
    }

    @Bean
    public EndOfGameDialog endOfGameDialog() {
        GameFrame gameFrame = applicationContext.getBean("gameFrame", GameFrame.class);
        Model model = applicationContext.getBean("model", Model.class);
        return new EndOfGameDialog(gameFrame, model, theme(), new Dimension(270, 180));
    }
}
