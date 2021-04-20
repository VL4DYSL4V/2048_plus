package view;

import preferences.UserPreferences;
import model.GameModel;
import observer.Subscriber;
import observer.event.ModelEvent;
import observer.event.UserPreferencesEvent;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;

public final class EndOfGameDialog extends JDialog {

    private final GameModel gameModel;
    private final Dimension dimension;
    private final JLabel iconLabel;
    private final UserPreferences userPreferences;

    public EndOfGameDialog(GameModel gameModel, UserPreferences userPreferences, Dimension dimension) {
        this.gameModel = gameModel;
        this.userPreferences = userPreferences;
        this.dimension = dimension;
        this.iconLabel = new JLabel(getScaledImageIcon());
        configureJDialog();
        styleJDialog();
        constructDialog();
    }

    public final class ModelListener implements Subscriber<ModelEvent> {

        @Override
        public void reactOnNotification(ModelEvent modelEvent) {
            SwingUtilities.invokeLater(() -> {
                if (gameModel.gameIsOver()) {
                    setVisible(true);
                } else if(isVisible()){
                    setVisible(false);
                }
            });
        }
    }

    public final class PreferencesListener implements Subscriber<UserPreferencesEvent> {

        @Override
        public void reactOnNotification(UserPreferencesEvent userPreferencesEvent) {
            SwingUtilities.invokeLater(EndOfGameDialog.this::updateTheme);
        }
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
        Image gameOverImage = userPreferences.getTheme().gameOverImage();
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
        Theme theme = userPreferences.getTheme();
        getContentPane().setBackground(theme.getBackground());
        setUndecorated(true);
    }

    private void updateTheme() {
        Theme theme = userPreferences.getTheme();
        getContentPane().setBackground(theme.getBackground());
        iconLabel.setIcon(getScaledImageIcon());
        repaint();
    }
}
