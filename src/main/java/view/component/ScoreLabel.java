package view.component;

import entity.Model;
import view.enums.Fonts;
import view.enums.FrameSize;
import view.theme.Theme;

import javax.swing.*;
import java.awt.*;

public final class ScoreLabel extends JLabel {

    private static final String SCORES = "Scores: ";
    private static final String END_OF_TOO_LONG_SCORES = "*10^";
    private final Model model;

    public ScoreLabel(Model model, Theme theme) {
        this.model = model;
        updateValue();
        setForeground(theme.getForeground());
        setOpaque(false);
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
