package view;

import model.Model;
import observer.Subscriber;
import observer.event.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import view.component.StandardButton;
import view.context.ThemeHolder;
import view.enums.Fonts;
import view.enums.FrameSize;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;

@Component
public final class EndOfGameFrame extends JFrame implements Subscriber {

    private static final String END_OF_GAME_MESSAGE
            = "<html><div style='text-align:center; padding-left:40px;'>Game is over!<br> Would you like to play again?</div></html>";
    private final JPanel rootPanel = new JPanel();
    private final JLabel messageLabel = new JLabel();
    private final JButton restartButton;
    private final JButton exitButton;

    private final Model model;
    private final ThemeHolder themeHolder;

    @Autowired
    public EndOfGameFrame(Model model, @Qualifier("renderingContext") ThemeHolder themeHolder,
                          StandardButton exitAndSaveButton, StandardButton restartButton) {
        this.restartButton = restartButton;
        this.exitButton = exitAndSaveButton;
        this.model = model;
        this.themeHolder = themeHolder;
        Theme theme = themeHolder.getTheme();
        styleComponents(theme);
        configComponents();
        constructWindow();
    }

    private void constructWindow() {
        add(rootPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.BOTH;
        rootPanel.add(messageLabel, constraints);
        constraints.gridwidth = 1;
        constraints.gridy = 1;
        rootPanel.add(restartButton, constraints);
        constraints.gridx = 1;
        rootPanel.add(exitButton, constraints);
    }

    private void configComponents() {
        configFrame();
        configRootPanel();
    }

    private void styleComponents(Theme theme) {
        styleFrame();
        styleRootPanel(theme);
        styleMessageLabel(theme);
        styleRestartButton();
    }

    private void styleExitButton(){
        exitButton.setText("Exit");
    }

    private void styleRestartButton(){
        restartButton.setText("Restart");
    }

    private void styleMessageLabel(Theme theme) {
        messageLabel.setForeground(theme.getForeground());
        messageLabel.setFont(Fonts.STANDARD_FONT.getFont());
        messageLabel.setText(END_OF_GAME_MESSAGE);
    }

    private void styleRootPanel(Theme theme) {
        rootPanel.setBackground(theme.getBackground());
        rootPanel.setForeground(theme.getForeground());
        rootPanel.setSize(FrameSize.END_OF_GAME_FRAME.getDimension());
    }

    private void configRootPanel() {
        rootPanel.setLayout(new GridBagLayout());
    }

    private void configFrame() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setFocusable(true);
    }

    private void styleFrame() {
        setTitle("GAME OVER!");
        Dimension dimension = FrameSize.END_OF_GAME_FRAME.getDimension();
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
    }

    @Override
    public void reactOnNotification(EventType eventType) {
        if (eventType == EventType.MODEL_CHANGED) {
            SwingUtilities.invokeLater(() -> {
                boolean gameIsOver = model.gameIsOver();
                if (gameIsOver && !this.isVisible()) {
                    setVisible(true);
                } else if (!model.gameIsOver()) {
                    setVisible(false);
                }
            });
        }
    }

}
