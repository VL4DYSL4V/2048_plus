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

    public Image getFieldBackgroundImage() {
        return fieldBackgroundImage;
    }

    public Image getWelcomeImage() {
        return welcomeImage;
    }

    public Map<Integer, Image> getPowerToImageMap() {
        return powToImageMap;
    }

    public Image getGameOverImage() {
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
                Objects.equals(font, theme.font);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, background, foreground, font);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "name='" + name + '\'' +
                ", background=" + background +
                ", foreground=" + foreground +
                ", font=" + font +
                '}';
    }
}
