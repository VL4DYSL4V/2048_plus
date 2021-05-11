package config;

import dao.preferences.PreferencesDAO;
import dao.theme.FileSystemThemeDao;
import dao.theme.ThemeDao;
import exception.FetchException;
import model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import preferences.UserPreferences;
import view.EndOfGameDialog;
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
    public UserPreferences userPreferences() {
        PreferencesDAO preferencesDAO = applicationContext.getBean("preferencesDao", PreferencesDAO.class);
        return preferencesDAO.getUserPreferences();
    }

    @Bean
    public Theme darkTheme() {
        ThemeDao themeDao = applicationContext.getBean("themeDao", FileSystemThemeDao.class);
        try {
            return themeDao.loadTheme("theme/dark.properties");
        } catch (FetchException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Theme brightTheme() {
        ThemeDao themeDao = applicationContext.getBean("themeDao", FileSystemThemeDao.class);
        try {
            return themeDao.loadTheme("theme/bright.properties");
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
