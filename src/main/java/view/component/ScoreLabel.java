package view.component;

import preferences.UserPreferences;
import model.GameModel;
import org.springframework.context.MessageSource;
import view.enums.Fonts;
import view.enums.FrameSize;
import view.theme.Theme;

import javax.swing.*;
import java.awt.*;

public final class ScoreLabel extends JLabel
        implements ThemeVaryingComponent, LocaleVaryingComponent{

    private static final String SCORES_DELIMITER = ": ";
    private static final String END_OF_TOO_LONG_SCORES = "*10^";
    private final GameModel gameModel;
    private final MessageSource messageSource;
    private final UserPreferences userPreferences;
    private String scores;

    public ScoreLabel(GameModel gameModel, MessageSource messageSource, UserPreferences userPreferences) {
        this.gameModel = gameModel;
        this.messageSource = messageSource;
        this.userPreferences = userPreferences;
        this.scores = messageSource.getMessage("gameFrame.scores", null, userPreferences.getLocale());
        style();
    }

    private void style() {
        setFont(Fonts.STANDARD_FONT.getFont());
        updateTheme();
        updateLocale();
    }

    private void updateTheme(){
        Theme theme = userPreferences.getTheme();
        setForeground(theme.getForeground());
    }

    private void updateLocale(){
        this.scores = messageSource.getMessage("gameFrame.scores", null, userPreferences.getLocale());
        updateValue();
    }

    public void updateValue() {
        setText(scoresWithDelimiter().concat(representation()));
    }

    private String scoresWithDelimiter() {
        return scores.concat(SCORES_DELIMITER);
    }

    private String representation() {
        StringBuilder scoreRepresentation = new StringBuilder(gameModel.getScores().toString());
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(Fonts.STANDARD_FONT.getFont());
        int initialWidth = fm.stringWidth(scoresWithDelimiter());
        int endOfLineWidth = fm.stringWidth(END_OF_TOO_LONG_SCORES);
        int usedWidth = initialWidth + endOfLineWidth + 10;
        for (int i = 0; i < scoreRepresentation.length() - 1; i++) {
            String digit = scoreRepresentation.substring(i, i + 1);
            int digitWidth = fm.stringWidth(digit);
            String remainder = scoreRepresentation.substring(i);
            int powerWidth = fm.stringWidth(String.valueOf(remainder.length()));
            usedWidth += digitWidth;
            if (usedWidth + digitWidth + powerWidth >= FrameSize.GAME_FRAME.getDimension().width) {
                scoreRepresentation = new StringBuilder(scoreRepresentation.substring(0, i))
                        .append(END_OF_TOO_LONG_SCORES)
                        .append(remainder.length());
                break;
            }
        }
        return scoreRepresentation.toString();
    }

    @Override
    public void applyNewTheme() {
        updateTheme();
    }

    @Override
    public void applyNewLocale() {
        updateLocale();
    }
}
