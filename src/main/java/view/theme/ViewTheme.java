package view.theme;

import java.awt.*;
import java.util.Map;

public final class ViewTheme implements Theme {

    private final Color background;
    private final Color foreground;
    private final Image fieldBackgroundImage;
    private final Image welcomeImage;
    private final Map<Integer, Image> powToImageMap;
    private final Image gameOverImage;


    public ViewTheme(Color background, Color foreground,
                     Image fieldBackgroundImage, Image welcomeImage,
                     Map<Integer, Image> powToImageMap, Image gameOverImage) {
        this.background = background;
        this.foreground = foreground;
        this.fieldBackgroundImage = fieldBackgroundImage;
        this.welcomeImage = welcomeImage;
        this.powToImageMap = powToImageMap;
        this.gameOverImage = gameOverImage;
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public Color getForeground() {
        return foreground;
    }

    @Override
    public Image fieldBackgroundImage() {
        return fieldBackgroundImage;
    }

    @Override
    public Image welcomeImage() {
        return welcomeImage;
    }

    @Override
    public Map<Integer, Image> powerToImageMap() {
        return powToImageMap;
    }

    @Override
    public Image gameOverImage() {
        return gameOverImage;
    }

}
