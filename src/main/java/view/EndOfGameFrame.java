package view;

import observer.Subscriber;
import observer.event.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import view.component.StandardButton;
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

    private Theme theme;

    @Autowired
    public EndOfGameFrame(Theme theme, StandardButton exitAndSaveButton) {
        this.theme = theme;
        this.restartButton = new StandardButton("Restart", theme, null);
        this.exitButton = exitAndSaveButton;
        styleComponents();
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

    private void styleComponents() {
        styleFrame();
        styleRootPanel();
        styleMessageLabel();
    }

    private void configExitButton() {
        exitButton.addActionListener((e) -> {

        });
    }

    private void configRestartButton() {
        restartButton.addActionListener((e) -> {

        });
    }

    private void styleMessageLabel() {
        messageLabel.setForeground(theme.getForeground());
        messageLabel.setFont(Fonts.STANDARD_FONT.getFont());
        messageLabel.setText(END_OF_GAME_MESSAGE);
    }

    private void styleRootPanel() {
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
        if (eventType == EventType.GAME_OVER) {
            this.setVisible(true);
        }
    }

}
