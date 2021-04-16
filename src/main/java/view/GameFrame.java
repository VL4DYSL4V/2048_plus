package view;

import command.Command;
import context.UserPreferences;
import model.GameModel;
import observer.Subscriber;
import observer.event.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import view.component.FieldCanvas;
import view.component.ScoreLabel;
import view.component.StandardButton;
import view.enums.FrameSize;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Locale;

@Component
//Todo: spot all view-context dependent styling and do it in separate methods
public final class GameFrame extends JFrame implements Subscriber{

    private final JPanel rootPanel = new JPanel();
    private final JPanel controlPanel = new JPanel();
    private final StandardButton moveBackButton;
    private final StandardButton toMenuButton;
    private final StandardButton restartButton;
    private final ScoreLabel scoreLabel;
    private final FieldCanvas fieldCanvas;

    private final MessageSource messageSource;
    private final UserPreferences userPreferences;

    @Autowired
    public GameFrame(GameModel gameModel, MessageSource messageSource,
                     UserPreferences userPreferences, KeyListener fieldMovementListener,
                     Command moveBackCommand,
                     Command restartCommand,
                     @Lazy Command gameFrameToMenuCommand) {
        this.messageSource = messageSource;
        this.userPreferences = userPreferences;
        this.moveBackButton = new StandardButton(userPreferences, moveBackCommand);
        this.toMenuButton = new StandardButton(userPreferences, gameFrameToMenuCommand);
        this.restartButton = new StandardButton(userPreferences, restartCommand);
        this.scoreLabel = new ScoreLabel(gameModel, messageSource, userPreferences);
        this.fieldCanvas = new FieldCanvas(gameModel, userPreferences);
        configComponents();
        styleFrame();
        styleComponents();
        constructWindow();
        addKeyListener(fieldMovementListener);
    }

    private void constructWindow() {
        add(rootPanel);
        SpringLayout layout = new SpringLayout();
        rootPanel.setLayout(layout);
        layout.putConstraint(SpringLayout.WEST, controlPanel, 0, SpringLayout.WEST, rootPanel);
        layout.putConstraint(SpringLayout.NORTH, controlPanel, 0, SpringLayout.NORTH, rootPanel);
        rootPanel.add(controlPanel);
        layout.putConstraint(SpringLayout.WEST, fieldCanvas, 0, SpringLayout.WEST, rootPanel);
        layout.putConstraint(SpringLayout.NORTH, fieldCanvas, 0, SpringLayout.SOUTH, controlPanel);
        rootPanel.add(fieldCanvas, layout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.1;
        constraints.insets = new Insets(3, 3, 3, 3);
        controlPanel.add(toMenuButton, constraints);
        constraints.gridx = 1;
        controlPanel.add(moveBackButton, constraints);
        constraints.gridx = 2;
        controlPanel.add(restartButton, constraints);
        constraints.gridx = 0;
        constraints.gridwidth = 3;
        constraints.gridy = 1;
        controlPanel.add(scoreLabel, constraints);

    }

    private void configComponents() {
        configFrame();
        configRootPanel();
        configControlPanel();
        configFieldCanvas();
    }

    private void styleComponents() {
        Theme theme = userPreferences.getTheme();
        Locale locale = userPreferences.getLocale();
        styleRootPanel(theme);
        styleControlPanel(theme);
        styleFieldCanvas(theme);
        styleToMenuButton(locale);
        styleMoveBackButton(locale);
        styleRestartButton(locale);
    }

    private void styleToMenuButton(Locale locale) {
        String text = messageSource.getMessage("gameFrame.toMenu", null, locale);
        toMenuButton.setText(text);
    }

    private void styleRestartButton(Locale locale) {
        String text = messageSource.getMessage("gameFrame.restart", null, locale);
        restartButton.setText(text);
    }

    private void styleMoveBackButton(Locale locale) {
        String text = messageSource.getMessage("gameFrame.moveBack", null, locale);
        moveBackButton.setText(text);
    }

    private void configFieldCanvas() {
        fieldCanvas.setFocusable(false);
    }

    private void styleFieldCanvas(Theme theme) {
        fieldCanvas.setBackground(theme.getBackground());
        fieldCanvas.setForeground(theme.getForeground());
        Dimension frameDimension = FrameSize.GAME_FRAME.getDimension();
        fieldCanvas.setPreferredSize(new Dimension(frameDimension.width, Math.min(frameDimension.width, frameDimension.height)));
    }

    private void styleControlPanel(Theme theme) {
        controlPanel.setBackground(theme.getBackground());
        controlPanel.setForeground(theme.getForeground());
        controlPanel.setBorder(BorderFactory.createEtchedBorder());
        Dimension frameDimension = FrameSize.GAME_FRAME.getDimension();
        controlPanel.setPreferredSize(new Dimension(frameDimension.width, Math.abs(frameDimension.width - frameDimension.height)));
    }

    private void configControlPanel() {
        controlPanel.setLayout(new GridBagLayout());
    }

    private void styleRootPanel(Theme theme) {
        rootPanel.setBackground(theme.getBackground());
        rootPanel.setForeground(theme.getForeground());
        rootPanel.setSize(FrameSize.GAME_FRAME.getDimension());
    }

    private void configRootPanel() {
        rootPanel.setLayout(new GridBagLayout());
    }

    private void styleFrame() {
        Dimension dimension = FrameSize.GAME_FRAME.getDimension();
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
        setUndecorated(true);
    }

    private void configFrame() {
        setResizable(false);
    }

    @Override
    public void reactOnNotification(EventType eventType) {
        SwingUtilities.invokeLater(computeReaction(eventType));
    }

    private Runnable computeReaction(EventType eventType) {
        switch (eventType) {
            case GAME_DATA_CHANGED:
                return () -> {
                    scoreLabel.updateValue();
                    fieldCanvas.updateField();
                };
            case USER_PREFERENCES_CHANGED:
                return this::updateStyle;
        }
        return () -> {
        };
    }

    private void updateStyle() {
        styleComponents();
        scoreLabel.updateStyle();
        scoreLabel.updateValue();
        fieldCanvas.updateStyle();
        repaint();
    }
}
