package view;

import model.Model;
import observer.Subscriber;
import observer.event.EventType;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;

public final class EndOfGameDialog extends JDialog implements Subscriber, StyleVaryingComponent {

    private final Model model;
    private final Dimension dimension;
    private final JLabel iconLabel;

    public EndOfGameDialog(JFrame owner, Model model, Theme theme, Dimension dimension) {
        super(owner);
        this.model = model;
        this.dimension = dimension;
        this.iconLabel = new JLabel(getScaledImageIcon(theme));
        configureJDialog();
        styleJDialog(theme);
        constructDialog();
    }

    public void constructDialog() {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        Component contentPane = getContentPane();
        layout.putConstraint(SpringLayout.NORTH, contentPane, 0, SpringLayout.NORTH, iconLabel);
        layout.putConstraint(SpringLayout.WEST, contentPane, 0, SpringLayout.WEST, iconLabel);
        add(iconLabel);
    }

    private ImageIcon getScaledImageIcon(Theme theme) {
        Image gameOverImage = theme.gameOverImage();
        int width = dimension.width;
        int height = dimension.height;
        return new ImageIcon(gameOverImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    private void configureJDialog() {
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setFocusable(false);
    }

    private void styleJDialog(Theme theme) {
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
        getContentPane().setBackground(theme.getBackground());
        setUndecorated(true);
    }

    @Override
    public void reactOnNotification(EventType eventType) {
        if (model.gameIsOver()) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    @Override
    public void update(Theme neuTheme) {
        SwingUtilities.invokeLater(() -> {
            styleJDialog(neuTheme);
            iconLabel.setIcon(getScaledImageIcon(neuTheme));
            repaint();
        });
    }
}
