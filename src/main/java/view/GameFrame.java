package view;

import model.Model;
import observer.Subscriber;
import observer.event.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import view.component.FieldCanvas;
import view.component.ScoreLabel;
import view.component.StandardButton;
import view.context.ThemeHolder;
import view.enums.FrameSize;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

@Component
public final class GameFrame extends JFrame implements Subscriber {

    private final JPanel rootPanel = new JPanel();
    private final JPanel controlPanel = new JPanel();
    private final JButton moveBackButton;
    private final JButton toMenuButton;
    private final JButton restartButton;
    private final ScoreLabel scoreLabel;
    private final FieldCanvas fieldCanvas;

    private final Model model;
    private final ThemeHolder themeHolder;
    private JDialog gameOverMessage = null;

    @Autowired
    public GameFrame(Model model, KeyListener fieldMovementListener,
                     StandardButton moveBackButton,
                     StandardButton restartButton,
                     @Qualifier("renderingContext") ThemeHolder themeHolder) {
        this.themeHolder = themeHolder;
        this.moveBackButton = moveBackButton;
        Theme theme = themeHolder.getTheme();
        this.toMenuButton = new StandardButton("Menu", themeHolder, null);
        this.restartButton = restartButton;
        this.model = model;
        this.scoreLabel = new ScoreLabel(model, themeHolder);
        this.fieldCanvas = new FieldCanvas(model, themeHolder);
        configComponents();
        styleComponents(theme);
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

    private void styleComponents(Theme theme) {
        styleFrame();
        styleRootPanel(theme);
        styleControlPanel(theme);
        styleFieldCanvas(theme);
        styleMoveBackButton();
        styleRestartButton();
    }

    private void styleRestartButton() {
        restartButton.setText("Restart");
    }

    private void styleMoveBackButton() {
        moveBackButton.setText("Move back");
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
        setTitle("2048+");
    }

    private void configFrame() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }

    @Override
    public void reactOnNotification(EventType eventType) {
        if (eventType == EventType.MODEL_CHANGED) {
            SwingUtilities.invokeLater(() -> {
                scoreLabel.updateValue();
                fieldCanvas.updateField();
                if (model.gameIsOver()) {
                    assignAndShowGameOver();
                } else {
                    disposeAndDiscardGameOver();
                }
            });
        }
    }

    private void assignAndShowGameOver() {
        if (this.gameOverMessage == null) {
            this.gameOverMessage = new EndOfGameDialog(this, themeHolder,
                    "Game is over!",
                    new Dimension(270, 180));
        }
        this.gameOverMessage.setVisible(true);
    }

    private void disposeAndDiscardGameOver() {
        if (this.gameOverMessage != null) {
            this.gameOverMessage.dispose();
            this.gameOverMessage = null;
        }
    }
}
