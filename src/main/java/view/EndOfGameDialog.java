package view;

import view.context.ThemeHolder;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;

final class EndOfGameDialog extends JDialog {

    private final ThemeHolder themeHolder;
    private final Dimension dimension;

    public EndOfGameDialog(JFrame owner, ThemeHolder themeHolder, String title, Dimension dimension) {
        super(owner, title);
        this.themeHolder = themeHolder;
        this.dimension = dimension;
        configureJDialog();
        styleJDialog();
        constructDialog();
    }

    public void constructDialog() {
        JLabel iconLabel = new JLabel(getScaledImageIcon());
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        Component contentPane = getContentPane();
        layout.putConstraint(SpringLayout.NORTH, contentPane, 0, SpringLayout.NORTH, iconLabel);
        layout.putConstraint(SpringLayout.WEST, contentPane, 0, SpringLayout.WEST, iconLabel);
        add(iconLabel);
    }

    private ImageIcon getScaledImageIcon() {
        Image gameOverImage = themeHolder.getTheme().gameOverImage();
        int width = dimension.width;
        int height = dimension.height * 8 / 10;
        return new ImageIcon(gameOverImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    private void configureJDialog() {
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setFocusable(false);
    }

    private void styleJDialog() {
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
        Theme theme = themeHolder.getTheme();
        getContentPane().setBackground(theme.getBackground());
    }
}
