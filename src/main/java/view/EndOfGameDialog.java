package view;

import context.ViewContext;
import model.GameModel;
import observer.Subscriber;
import observer.event.EventType;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;

public final class EndOfGameDialog extends JDialog implements Subscriber {

    private final GameModel gameModel;
    private final Dimension dimension;
    private final JLabel iconLabel;
    private final ViewContext viewContext;

    public EndOfGameDialog(JFrame owner, GameModel gameModel, ViewContext viewContext, Dimension dimension) {
        super(owner);
        this.gameModel = gameModel;
        this.viewContext = viewContext;
        this.dimension = dimension;
        this.iconLabel = new JLabel(getScaledImageIcon());
        configureJDialog();
        styleJDialog();
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

    private ImageIcon getScaledImageIcon() {
        Image gameOverImage = viewContext.getCurrentTheme().gameOverImage();
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

    private void styleJDialog() {
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
        Theme theme = viewContext.getCurrentTheme();
        getContentPane().setBackground(theme.getBackground());
        setUndecorated(true);
    }

    @Override
    public void reactOnNotification(EventType eventType) {
        SwingUtilities.invokeLater(computeReaction(eventType));
    }

    private Runnable computeReaction(EventType eventType) {
        switch (eventType) {
            case GAME_DATA_CHANGED:
                return () -> {
                    if (gameModel.gameIsOver()) {
                        setVisible(true);
                    } else {
                        setVisible(false);
                    }
                };
            case VIEW_CONTEXT_CHANGED:
                return this::updateStyle;
        }
        return () -> {
        };
    }

    private void updateStyle() {
        Theme theme = viewContext.getCurrentTheme();
        getContentPane().setBackground(theme.getBackground());
        iconLabel.setIcon(getScaledImageIcon());
        repaint();
    }
}