package view.component;

import context.ViewContext;
import model.GameModel;
import view.enums.Fonts;
import view.enums.FrameSize;
import view.theme.Theme;

import javax.swing.*;
import java.awt.*;

public final class ScoreLabel extends JLabel implements StyleVaryingComponent {

    private static final String SCORES = "Scores: ";
    private static final String END_OF_TOO_LONG_SCORES = "*10^";
    private final GameModel gameModel;
    private final ViewContext viewContext;

    public ScoreLabel(GameModel gameModel, ViewContext viewContext) {
        this.gameModel = gameModel;
        this.viewContext = viewContext;
        updateValue();
        style();
    }

    private void style() {
        Theme theme = viewContext.getCurrentTheme();
        setForeground(theme.getForeground());
        setFont(Fonts.STANDARD_FONT.getFont());
    }

    public final void updateValue() {
        setText(SCORES.concat(representation()));
    }

    private String representation() {
        StringBuilder scoreRepresentation = new StringBuilder(gameModel.getScores().toString());
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(Fonts.STANDARD_FONT.getFont());
        int initialWidth = fm.stringWidth(SCORES);
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
    public void updateStyle() {
        style();
    }
}
