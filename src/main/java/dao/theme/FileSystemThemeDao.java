package dao.theme;

import exception.FetchException;
import org.springframework.stereotype.Repository;
import view.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Repository("themeDao")
public final class FileSystemThemeDao implements ThemeDao {

    private static final Pattern DIGIT = Pattern.compile("\\d+");
    private final Properties themeNameToFileNameProperties;

    public FileSystemThemeDao(Properties themeNameToFileNameProperties) {
        this.themeNameToFileNameProperties = themeNameToFileNameProperties;
    }

    @Override
    public Theme loadTheme(String location) throws FetchException {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(location)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new FetchException(e);
        }
        return decodeTheme(properties);
    }

    @Override
    public Theme loadByName(String name) throws FetchException {
        return loadTheme(themeNameToFileNameProperties.getProperty(name));
    }

    private Theme decodeTheme(Properties properties) {
        String name = properties.getProperty("name");
        Color bgColor = parseColor(properties.getProperty("background-color"));
        Color fgColor = parseColor(properties.getProperty("foreground-color"));
        Font font = parseFont(properties);
        Image fieldBgImage = loadFieldBgImage(properties);
        Image welcomeImage = loadWelcomeImage(properties);
        Image gameOverImage = loadGameOverImage(properties);
        Map<Integer, Image> powToImageMap = loadPowerToImageMap(properties);
        return new Theme(name, bgColor, fgColor, font, fieldBgImage, welcomeImage, powToImageMap, gameOverImage);
    }

    private Image loadGameOverImage(Properties properties) {
        URL url = getClass().getResource("/" + properties.getProperty("game-over-image"));
        return load(url);
    }

    private Image loadWelcomeImage(Properties properties) {
        URL url = getClass().getResource("/" + properties.getProperty("welcome-image"));
        return load(url);
    }

    private Image loadFieldBgImage(Properties properties) {
        URL url = getClass().getResource("/" + properties.getProperty("field-bg-img-path"));
        return load(url);
    }

    private Map<Integer, Image> loadPowerToImageMap(Properties properties) {
        Map<Integer, Image> powToImageMap = new HashMap<>();
        String powerFolderPath = "/" + properties.getProperty("power-folder") + "/";
        for (String key : properties.stringPropertyNames()) {
            if (DIGIT.matcher(key).matches()) {
                String resourceLocation = powerFolderPath + properties.getProperty(key);
                URL url = getClass().getResource(resourceLocation);
                powToImageMap.put(Integer.valueOf(key), load(url));
            }
        }
        return powToImageMap;
    }

    private Font parseFont(Properties properties) {
        String fontName = properties.getProperty("font-name");
        String fontStyle = properties.getProperty("font-style");
        String fontSize = properties.getProperty("font-size");
        return new Font(fontName, Integer.parseInt(fontStyle), Integer.parseInt(fontSize));
    }

    private Color parseColor(String s) {
        String[] rgb = s.split("-");
        return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }

    private Image load(URL url) {
        return new ImageIcon(url).getImage();
    }
}
