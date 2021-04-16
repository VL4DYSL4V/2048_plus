package config;

import context.UserPreferences;
import context.UserPreferencesImpl;
import dao.theme.FileSystemThemeDao;
import dao.theme.ThemeDao;
import model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import view.EndOfGameDialog;
import view.GameFrame;
import view.theme.Theme;

import java.awt.*;
import java.util.Locale;

@Configuration
public class ViewConfig {

    private final ApplicationContext applicationContext;

    @Autowired
    public ViewConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public UserPreferences viewContext(){
        return new UserPreferencesImpl(Locale.ENGLISH, darkTheme());
    }

    @Bean
    public Theme darkTheme() {
        ThemeDao themeDao = applicationContext.getBean("themeDao", FileSystemThemeDao.class);
        return themeDao.loadTheme("classpath:theme/dark.properties");
    }

    @Bean
    public EndOfGameDialog endOfGameDialog() {
        GameFrame gameFrame = applicationContext.getBean("gameFrame", GameFrame.class);
        GameModel gameModel = applicationContext.getBean("gameModel", GameModel.class);
        return new EndOfGameDialog(gameFrame, gameModel, viewContext(), new Dimension(270, 180));
    }
}
