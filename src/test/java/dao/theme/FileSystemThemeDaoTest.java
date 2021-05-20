package dao.theme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testUtils.PropertyUtils;
import view.theme.Theme;

import java.awt.*;
import java.util.Iterator;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class FileSystemThemeDaoTest {

    private FileSystemThemeDao themeDao;
    private Properties themeNameToFileNameProperties;

    @BeforeEach
    void setUp() {
        themeNameToFileNameProperties = PropertyUtils.getThemeNameToFileProperties();
        themeDao = new FileSystemThemeDao(themeNameToFileNameProperties);
    }

    @Test
    void nullConstructorArgTest(){
        assertThrows(NullPointerException.class, () -> new FileSystemThemeDao(null));
    }

    @Test
    void loadTheme() {
        Properties properties = PropertyUtils.loadByLocation("theme/dark.properties");
        assertDoesNotThrow(() -> themeDao.loadTheme("theme/dark.properties"));
        Theme theme = themeDao.loadTheme("theme/dark.properties");
        performCheck(properties, theme);
    }

    @Test
    void loadThemeFromWrongLocation(){
        assertThrows(RuntimeException.class, () -> themeDao.loadTheme("bla-bla-bla"));
    }

    @Test
    void loadByName() {
        Properties properties = PropertyUtils.loadByLocation("theme/dark.properties");
        Iterator<String> nameIterator = themeNameToFileNameProperties.stringPropertyNames().iterator();
        assumeTrue(nameIterator.hasNext(), "themeNameToFileNameProperties should not be empty!");
        String name = nameIterator.next();
        assertDoesNotThrow(() -> themeDao.loadByName(name));
        Theme theme = themeDao.loadByName(name);
        performCheck(properties, theme);
    }

    @Test
    void loadWithIncorrectName(){
        assertThrows(RuntimeException.class, () -> themeDao.loadByName("bla-bla-bla"));
    }

    private void performCheck(Properties properties, Theme theme){
        checkName(properties, theme);
        checkBackground(properties, theme);
        checkForeground(properties, theme);
        checkFont(properties, theme);
        assertNotNull(theme.getPowerToImageMap());
        assertNotNull(theme.getFieldBackgroundImage());
        assertNotNull(theme.getGameOverImage());
        assertNotNull(theme.getWelcomeImage());
    }

    private void checkName(Properties properties, Theme theme){
        assertEquals(properties.getProperty("name"), theme.getName());
    }

    private void checkBackground(Properties properties, Theme theme){
        Color color = theme.getBackground();
        String colorString = properties.getProperty("background-color");
        checkColorCorrespondsString(color, colorString);
    }

    private void checkForeground(Properties properties, Theme theme){
        Color color = theme.getForeground();
        String colorString = properties.getProperty("foreground-color");
        checkColorCorrespondsString(color, colorString);
    }

    private void checkColorCorrespondsString(Color color, String colorString){
        String[] rgb = colorString.split("-");
        assertEquals(Integer.parseInt(rgb[0]), color.getRed());
        assertEquals(Integer.parseInt(rgb[1]), color.getBlue());
        assertEquals(Integer.parseInt(rgb[2]), color.getGreen());
    }

    private void checkFont(Properties properties, Theme theme){
        Font font = theme.getFont();
        assertEquals(properties.getProperty("font-name"), font.getName());
        assertEquals(Integer.parseInt(properties.getProperty("font-size")), font.getSize());
        assertEquals(Integer.parseInt(properties.getProperty("font-style")), font.getStyle());
    }
}