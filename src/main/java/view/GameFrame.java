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

    private Theme theme;

    @Autowired
    public GameFrame(Model model, KeyListener fieldMovementListener,
                     StandardButton moveBackButton,
                     @Qualifier("theme") Theme theme) {
        this.theme = theme;
        this.moveBackButton = moveBackButton;
        this.toMenuButton = new StandardButton("Menu", theme, null);
        this.restartButton = new StandardButton("Restart", theme, null);
        this.scoreLabel = new ScoreLabel(model, theme);
        this.fieldCanvas = new FieldCanvas(model, theme);
        configComponents();
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
        configBackButton();
        configToMenuButton();
        configRestartButton();
        configFieldCanvas();
    }

    private void styleComponents() {
        styleFrame();
        styleRootPanel();
        styleControlPanel();
        styleFieldCanvas();
    }

    private void configFieldCanvas() {
        fieldCanvas.setFocusable(false);
    }

    private void styleFieldCanvas() {
        fieldCanvas.setBackground(theme.getBackground());
        fieldCanvas.setForeground(theme.getForeground());
        Dimension frameDimension = FrameSize.GAME_FRAME.getDimension();
        fieldCanvas.setPreferredSize(new Dimension(frameDimension.width, Math.min(frameDimension.width, frameDimension.height)));
    }

    private void configRestartButton() {
        restartButton.addActionListener(e -> {

        });
    }

    private void configToMenuButton() {
        toMenuButton.addActionListener(e -> {

        });
    }

    private void configBackButton() {
        moveBackButton.addActionListener(e -> {
        });
    }

    private void styleControlPanel() {
        controlPanel.setBackground(theme.getBackground());
        controlPanel.setForeground(theme.getForeground());
        controlPanel.setBorder(BorderFactory.createEtchedBorder());
        Dimension frameDimension = FrameSize.GAME_FRAME.getDimension();
        controlPanel.setPreferredSize(new Dimension(frameDimension.width, Math.abs(frameDimension.width - frameDimension.height)));
    }

    private void configControlPanel() {
        controlPanel.setLayout(new GridBagLayout());
    }

    private void styleRootPanel() {
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
            });
        }
    }
}
