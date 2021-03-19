package config;

import command.ExitAndSaveCommand;
import command.ExitCommand;
import command.MoveBackCommand;
import command.RestartCommand;
import controller.exit.ExitController;
import controller.moveBack.MoveBackController;
import controller.restart.RestartController;
import controller.restart.RestartControllerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ResourceUtils;
import view.EndOfGameFrame;
import view.component.StandardButton;
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

@Configuration
public class ViewConfig {

    private static final Pattern POWER_PATTERN = Pattern.compile("[0-9]+");
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
        return loadTheme("classpath:theme/dark.properties");
    }

    @Bean
    public StandardButton moveBackButton() {
        MoveBackController moveBackController = applicationContext.getBean("moveBackController", MoveBackController.class);
        Theme theme = applicationContext.getBean("theme", Theme.class);
        return new StandardButton("Move back", theme, new MoveBackCommand(moveBackController));
    }

    @Bean
    public StandardButton exitAndSaveButton(){
        ExitController exitController = applicationContext.getBean("exitController", ExitController.class);
        Theme theme = applicationContext.getBean("theme", Theme.class);
        return new StandardButton("Exit", theme, new ExitAndSaveCommand(exitController));
    }

    @Bean
    public StandardButton exitButton() {
        ExitController exitController = applicationContext.getBean("exitController", ExitController.class);
        Theme theme = applicationContext.getBean("theme", Theme.class);
        return new StandardButton("Exit", theme, new ExitCommand(exitController));
    }

    @Bean
    @Scope("prototype")
    public StandardButton restartButton(){
        RestartController restartController = applicationContext.getBean("restartController", RestartControllerImpl.class);
        Theme theme = applicationContext.getBean("theme", Theme.class);
        return new StandardButton("Restart", theme, new RestartCommand(restartController));
    }

    private Theme loadTheme(String path) {
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
        return new ViewTheme(bgColor, fgColor, fieldBgImage, welcomeImage, powToImageMap);
    }

    private Image load(String fileName) {
        return Toolkit.getDefaultToolkit().getImage(fileName);
    }
}
