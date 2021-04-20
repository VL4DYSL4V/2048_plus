package view;

import command.Command;
import command.VolatileCommand;
import preferences.UserPreferences;
import enums.FieldDimension;
import observer.Subscriber;
import observer.event.UserPreferencesEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import view.component.BackgroundPanel;
import view.component.DimensionJComboBox;
import view.component.StandardButton;
import view.enums.FrameSize;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

@Component
public final class MainFrame extends JFrame
        implements Subscriber<UserPreferencesEvent> {

    private final BackgroundPanel rootPanel;
    private final JPanel controlPanel;
    private final StandardButton toGameButton;
    private final StandardButton exitButton;
    private final StandardButton settingsButton;
    private final DimensionJComboBox dimensionJComboBox;

    private final MessageSource messageSource;
    private final UserPreferences userPreferences;

    public MainFrame(MessageSource messageSource, UserPreferences userPreferences,
                     Command exitCommand,
                     @Lazy Command menuToGameFrameCommand,
                     VolatileCommand<FieldDimension> dimensionChangeCommand) {
        this.rootPanel = new BackgroundPanel(FrameSize.MAIN_FRAME.getDimension(), userPreferences);
        this.toGameButton = new StandardButton(userPreferences, menuToGameFrameCommand);
        this.exitButton = new StandardButton(userPreferences, exitCommand);
        this.settingsButton = new StandardButton(userPreferences, null);
        this.dimensionJComboBox = new DimensionJComboBox(userPreferences, dimensionChangeCommand);
        this.controlPanel = new JPanel();
        this.messageSource = messageSource;
        this.userPreferences = userPreferences;
        initialStyle();
        styleComponents();
        configComponents();
        constructWindow();
        dimensionJComboBox.setSelectedIndex(1);
    }

    private void constructWindow() {
        add(rootPanel);
        rootPanel.add(controlPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 5;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets = new Insets(5, 0, 0, 0);
        controlPanel.add(toGameButton, constraints);
        constraints.gridy = 1;
        controlPanel.add(dimensionJComboBox, constraints);
        constraints.gridy = 2;
        controlPanel.add(settingsButton, constraints);
        constraints.gridy = 3;
        controlPanel.add(exitButton, constraints);
    }

    private void configComponents() {
        config();
        configRootPanel();
        configControlPanel();
    }

    private void styleComponents() {
        updateLocale();
        updateTheme();
        styleControlPanel();
    }

    private void updateLocale() {
        Locale locale = userPreferences.getLocale();
        localeStyleToGameButton(locale);
        localeStyleSettingsButton(locale);
        localeStyleExitButton(locale);
    }

    private void localeStyleToGameButton(Locale locale) {
        String text = messageSource.getMessage("mainFrame.toGame", null, locale);
        toGameButton.setText(text);
    }

    private void localeStyleSettingsButton(Locale locale) {
        String text = messageSource.getMessage("mainFrame.settings", null, locale);
        settingsButton.setText(text);
    }

    private void localeStyleExitButton(Locale locale) {
        String text = messageSource.getMessage("mainFrame.exit", null, locale);
        exitButton.setText(text);
    }

    private void updateTheme() {
        toGameButton.applyNewTheme();
        exitButton.applyNewTheme();
        settingsButton.applyNewTheme();
        dimensionJComboBox.applyNewTheme();
    }

    private void styleControlPanel() {
        controlPanel.setOpaque(false);
    }

    private void configControlPanel() {
        controlPanel.setLayout(new GridBagLayout());
    }

    private void configRootPanel() {
        rootPanel.setLayout(new GridBagLayout());
    }

    private void initialStyle() {
        Dimension dimension = FrameSize.MAIN_FRAME.getDimension();
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
        setTitle("2048+" );
    }

    private void config() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }

    @Override
    public void reactOnNotification(UserPreferencesEvent userPreferencesEvent) {
        SwingUtilities.invokeLater(computeReaction(userPreferencesEvent));
    }

    @SuppressWarnings("Duplicates")
    private Runnable computeReaction(UserPreferencesEvent eventType) {
        switch (eventType){
            case THEME_CHANGED:
                return () -> {
                    updateTheme();
                    repaint();
                };
            case LOCALE_CHANGED:
                return () -> {
                    updateLocale();
                    repaint();
                };
            default:
                return () -> {
                };
        }
    }

}
