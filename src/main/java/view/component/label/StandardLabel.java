package view.component.label;

import org.springframework.context.MessageSource;
import preferences.UserPreferences;
import view.component.LocaleVaryingComponent;
import view.component.ThemeVaryingComponent;
import view.theme.Theme;

import javax.swing.*;

public class StandardLabel extends JLabel implements ThemeVaryingComponent, LocaleVaryingComponent {

    protected final UserPreferences userPreferences;
    protected final MessageSource messageSource;
    protected final String messageSourceKey;

    public StandardLabel(MessageSource messageSource, UserPreferences userPreferences, String messageSourceKey) {
        this.userPreferences = userPreferences;
        this.messageSource = messageSource;
        this.messageSourceKey = messageSourceKey;
        updateTheme();
        updateLocale();
    }

    private void updateLocale(){
        setText(messageSource.getMessage(messageSourceKey, null, userPreferences.getLocale()));
    }

    private void updateTheme(){
        Theme theme = userPreferences.getTheme();
        setForeground(theme.getForeground());
        setFont(theme.getFont());
    }

    @Override
    public void applyNewLocale() {
        updateLocale();
    }

    @Override
    public void applyNewTheme() {
       updateTheme();
    }
}
