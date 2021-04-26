package view;

import command.Command;
import command.VolatileCommand;
import observer.Subscriber;
import observer.event.UserPreferencesEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import preferences.UserPreferences;
import view.component.button.StandardButton;
import view.component.combo_box.StandardJComboBox;
import view.component.label.StandardLabel;
import view.component.panel.BackgroundPanel;
import view.enums.FrameSize;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Map;

@Component
public final class SettingsFrame extends JFrame implements Subscriber<UserPreferencesEvent> {

    private final BackgroundPanel rootPanel;
    private final JPanel toMenuHolder = new JPanel();
    private final JPanel settingsHolder = new JPanel();
    private final StandardButton toMenuButton;
    private final StandardLabel languageLabel;
    private final StandardJComboBox<Locale> languageJComboBox;
    private final StandardLabel themeLabel;
    private final StandardJComboBox<Theme> themeJComboBox;

    private final MessageSource messageSource;
    private final UserPreferences userPreferences;

    public SettingsFrame(MessageSource messageSource, UserPreferences userPreferences,
                         @Lazy Command settingsToMenuCommand,
                         @Qualifier("supportedLocales") Map<String, Locale> supportedLocales,
                         VolatileCommand<Locale> languageChangeCommand,
                         @Qualifier("supportedThemes") Map<String, Theme> supportedThemes,
                         VolatileCommand<Theme> themeChangeCommand) {
        this.messageSource = messageSource;
        this.userPreferences = userPreferences;
        this.rootPanel = new BackgroundPanel(FrameSize.SETTINGS_FRAME.getDimension(), userPreferences);
        this.toMenuButton = new StandardButton(userPreferences, settingsToMenuCommand);
        this.languageLabel = new StandardLabel(messageSource, userPreferences, "settings.language");
        this.languageJComboBox = new StandardJComboBox<>(supportedLocales, userPreferences, languageChangeCommand);
        this.themeLabel = new StandardLabel(messageSource, userPreferences, "settings.theme");
        this.themeJComboBox = new StandardJComboBox<>(supportedThemes, userPreferences, themeChangeCommand);
        styleFrame();
        styleComponents();
        configComponents();
        constructWindow();
    }

    private void constructWindow() {
        add(rootPanel);
        constructToMenuHolder();
        constructSettingsHolder();
        SpringLayout layout = new SpringLayout();
        rootPanel.setLayout(layout);
        layout.putConstraint(SpringLayout.WEST, toMenuHolder, 5, SpringLayout.WEST, rootPanel);
        layout.putConstraint(SpringLayout.NORTH, toMenuHolder, 5, SpringLayout.NORTH, rootPanel);
        rootPanel.add(toMenuHolder);
        layout.putConstraint(SpringLayout.WEST, settingsHolder, 5, SpringLayout.WEST, rootPanel);
        layout.putConstraint(SpringLayout.NORTH, settingsHolder, 5, SpringLayout.SOUTH, toMenuHolder);
        rootPanel.add(settingsHolder);
    }

    private void constructSettingsHolder() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);
        settingsHolder.add(languageLabel, constraints);
        constraints.gridx = 1;
        settingsHolder.add(languageJComboBox, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        settingsHolder.add(themeLabel, constraints);
        constraints.gridx = 1;
        settingsHolder.add(themeJComboBox, constraints);
    }

    private void constructToMenuHolder() {
        toMenuHolder.add(toMenuButton, BorderLayout.WEST);
    }

    private void configComponents() {
        configFrame();
        configSettingsHolder();
    }

    private void styleComponents() {
        updateLocale();
        styleToMenuHolder();
        styleSettingsHolder();
    }

    private void configSettingsHolder() {
        settingsHolder.setLayout(new GridBagLayout());
    }

    private void styleSettingsHolder() {
        settingsHolder.setOpaque(false);
    }

    private void styleToMenuHolder() {
        toMenuHolder.setOpaque(false);
    }

    private void updateLocale() {
        Locale locale = userPreferences.getLocale();
        toMenuButton.setText(messageSource.getMessage("settings.toMenu", null, locale));
        languageLabel.applyNewLocale();
        themeLabel.applyNewLocale();
    }

    private void updateTheme() {
        rootPanel.applyNewTheme();
        toMenuButton.applyNewTheme();
        languageLabel.applyNewTheme();
        languageJComboBox.applyNewTheme();
        themeLabel.applyNewTheme();
        themeJComboBox.applyNewTheme();
    }

    private void configFrame() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }

    private void styleFrame() {
        Dimension dimension = FrameSize.SETTINGS_FRAME.getDimension();
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
        setTitle("2048+");
    }

    @Override
    public void reactOnNotification(UserPreferencesEvent userPreferencesEvent) {
        SwingUtilities.invokeLater(computeReaction(userPreferencesEvent));
    }

    private Runnable computeReaction(UserPreferencesEvent userPreferencesEvent) {
        switch (userPreferencesEvent) {
            case LOCALE_CHANGED:
                return () -> {
                    updateLocale();
                    repaint();
                };
            case THEME_CHANGED:
                return () -> {
                    updateTheme();
                    repaint();
                };
        }
        return () -> {
        };
    }
}
