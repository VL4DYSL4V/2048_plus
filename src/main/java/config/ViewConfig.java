package config;

import exception.FetchException;
import preferences.UserPreferences;
import dao.theme.FileSystemThemeDao;
import dao.theme.ThemeDao;
import enums.FieldDimension;
import model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import view.EndOfGameDialog;
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
    public UserPreferences userPreferences(){
        return new UserPreferences(Locale.ENGLISH, darkTheme(), FieldDimension.FOUR_AND_FOUR);
    }

    @Bean
    public Theme darkTheme() {
        ThemeDao themeDao = applicationContext.getBean("themeDao", FileSystemThemeDao.class);
        try {
            return themeDao.loadTheme("classpath:theme/dark.properties");
        } catch (FetchException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Theme brightTheme() {
        ThemeDao themeDao = applicationContext.getBean("themeDao", FileSystemThemeDao.class);
        try {
            return themeDao.loadTheme("classpath:theme/bright.properties");
        } catch (FetchException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public EndOfGameDialog endOfGameDialog() {
        GameModel gameModel = applicationContext.getBean("gameModel", GameModel.class);
        return new EndOfGameDialog(gameModel, userPreferences(), new Dimension(270, 180));
    }
}
