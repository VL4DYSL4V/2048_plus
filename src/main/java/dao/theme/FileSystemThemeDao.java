package dao.theme;

import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import view.theme.Theme;
import view.theme.ViewTheme;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Repository("themeDao")
public final class FileSystemThemeDao implements ThemeDao{

    private static final Pattern POWER_PATTERN = Pattern.compile("[0-9]+");

    @Override
    public Theme loadTheme(String path) {
        Properties properties = new Properties();
        try (BufferedReader bufferedReader = Files.newBufferedReader(
                ResourceUtils.getFile(path).toPath())) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return encodeIntoTheme(properties);
    }

    private Theme encodeIntoTheme(Properties properties) {
        Color bgColor = new Color(Integer.parseInt(properties.getProperty("bg_red")),
                Integer.parseInt(properties.getProperty("bg_green")), Integer.parseInt(properties.getProperty("bg_blue")));
        Color fgColor = new Color(Integer.parseInt(properties.getProperty("fg_red")),
                Integer.parseInt(properties.getProperty("fg_green")), Integer.parseInt(properties.getProperty("fg_blue")));
        Image fieldBgImage = load(properties.getProperty("field_bg_img_path"));
        Image welcomeImage = load(properties.getProperty("welcome_image"));
        Path powerFolder = Paths.get(properties.getProperty("power_folder"));
        Map<Integer, Image> powToImageMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (POWER_PATTERN.matcher(key).matches()) {
                powToImageMap.put(Integer.valueOf(key), load(powerFolder.resolve((String) properties.get(key)).toString()));
            }
        }
        Image gameOverImage = load(properties.getProperty("game_over_image"));
        return new ViewTheme(bgColor, fgColor, fieldBgImage, welcomeImage, powToImageMap, gameOverImage);
    }

    private Image load(String fileName) {
        return Toolkit.getDefaultToolkit().getImage(fileName);
    }
}
