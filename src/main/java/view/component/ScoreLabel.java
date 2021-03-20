package view.component;

import model.Model;
import view.context.ThemeHolder;
import view.enums.Fonts;
import view.enums.FrameSize;
import view.theme.Theme;

import javax.swing.*;
import java.awt.*;

public final class ScoreLabel extends JLabel {

    private static final String SCORES = "Scores: ";
    private static final String END_OF_TOO_LONG_SCORES = "*10^";
    private final ThemeHolder themeHolder;
    private final Model model;

    public ScoreLabel(Model model, ThemeHolder themeHolder) {
        this.model = model;
        this.themeHolder = themeHolder;
        updateValue();
        style(themeHolder.getTheme());
    }

    private void style(Theme theme){
        setForeground(theme.getForeground());
        setFont(Fonts.STANDARD_FONT.getFont());
    }

    public final void updateValue(){
        setText(SCORES.concat(representation()));
    }

    private String representation(){
        StringBuilder scoreRepresentation = new StringBuilder(model.getScores().toString());
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(Fonts.STANDARD_FONT.getFont());
        int initialWidth = fm.stringWidth(SCORES);
        int endOfLineWidth = fm.stringWidth(END_OF_TOO_LONG_SCORES);
        int usedWidth = initialWidth + endOfLineWidth + 10;
        for(int i = 0; i < scoreRepresentation.length() - 1; i++){
            String digit = scoreRepresentation.substring(i, i + 1);
            int digitWidth = fm.stringWidth(digit);
            String remainder = scoreRepresentation.substring(i);
            int powerWidth = fm.stringWidth(String.valueOf(remainder.length()));
            usedWidth += digitWidth;
            if(usedWidth + digitWidth + powerWidth >= FrameSize.GAME_FRAME.getDimension().width){
                scoreRepresentation = new StringBuilder(scoreRepresentation.substring(0, i))
                        .append(END_OF_TOO_LONG_SCORES)
                        .append(remainder.length());
                break;
            }
        }
        return scoreRepresentation.toString();
    }
}
