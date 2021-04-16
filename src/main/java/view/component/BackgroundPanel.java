package view.component;

import context.UserPreferences;
import view.theme.Theme;

import javax.swing.*;
import java.awt.*;

public final class BackgroundPanel extends JPanel implements StyleVaryingComponent{

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
        return theme.welcomeImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    @Override
    public void updateStyle() {
        this.image = scaledImageIcon();
    }
}
