package view.theme;

import java.awt.*;
import java.util.Map;

public final class Theme {

    private final String name;
    private final Color background;
    private final Color foreground;
    private final Image fieldBackgroundImage;
    private final Image welcomeImage;
    private final Map<Integer, Image> powToImageMap;
    private final Image gameOverImage;

    public Theme(String name, Color background, Color foreground,
                 Image fieldBackgroundImage, Image welcomeImage,
                 Map<Integer, Image> powToImageMap, Image gameOverImage) {
        this.name = name;
        this.background = background;
        this.foreground = foreground;
        this.fieldBackgroundImage = fieldBackgroundImage;
        this.welcomeImage = welcomeImage;
        this.powToImageMap = powToImageMap;
        this.gameOverImage = gameOverImage;
    }

    public String getName() {
        return name;
    }

    public Color getBackground() {
        return background;
    }

    public Color getForeground() {
        return foreground;
    }

    public Image fieldBackgroundImage() {
        return fieldBackgroundImage;
    }

    public Image welcomeImage() {
        return welcomeImage;
    }

    public Map<Integer, Image> powerToImageMap() {
        return powToImageMap;
    }

    public Image gameOverImage() {
        return gameOverImage;
    }

}
