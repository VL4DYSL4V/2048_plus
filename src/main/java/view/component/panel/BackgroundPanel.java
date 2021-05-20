package view.component.panel;

import preferences.UserPreferences;
import view.component.ThemeVaryingComponent;
import view.theme.Theme;

import javax.swing.*;
import java.awt.*;

public final class BackgroundPanel extends JPanel implements ThemeVaryingComponent {

    private final Dimension dimension;
    private final UserPreferences userPreferences;
    private Image image;

    public BackgroundPanel(Dimension dimension, UserPreferences userPreferences) {
        this.dimension = dimension;
        this.userPreferences = userPreferences;
        this.image = scaledImageIcon();
        setSize(dimension);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, this);
        paintChildren(g);
    }

    private Image scaledImageIcon() {
        int width = dimension.width;
        int height = (int) (dimension.height * 0.95);
        Theme theme = userPreferences.getTheme();
        return theme.getWelcomeImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    @Override
    public void applyNewTheme() {
        this.image = scaledImageIcon();
    }
}
