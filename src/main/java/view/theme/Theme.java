package view.theme;

import java.awt.*;
import java.util.Map;
import java.util.Objects;

public final class Theme {

    private final String name;
    private final Color background;
    private final Color foreground;
    private final Font font;
    private final Image fieldBackgroundImage;
    private final Image welcomeImage;
    private final Map<Integer, Image> powToImageMap;
    private final Image gameOverImage;

    public Theme(String name, Color background, Color foreground,
                 Font font, Image fieldBackgroundImage, Image welcomeImage,
                 Map<Integer, Image> powToImageMap, Image gameOverImage) {
        this.name = name;
        this.background = background;
        this.foreground = foreground;
        this.font = font;
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

    public Font getFont() {
        return font;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(name, theme.name) &&
                Objects.equals(background, theme.background) &&
                Objects.equals(foreground, theme.foreground) &&
                Objects.equals(font, theme.font) &&
                Objects.equals(fieldBackgroundImage, theme.fieldBackgroundImage) &&
                Objects.equals(welcomeImage, theme.welcomeImage) &&
                Objects.equals(powToImageMap, theme.powToImageMap) &&
                Objects.equals(gameOverImage, theme.gameOverImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, background, foreground, font, fieldBackgroundImage, welcomeImage, powToImageMap, gameOverImage);
    }

}
