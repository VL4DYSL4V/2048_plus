package view.component;

import command.Command;
import context.ViewContext;
import view.enums.Fonts;
import view.theme.Theme;

import javax.swing.*;

public final class StandardButton extends JButton implements StyleVaryingComponent {

    private final ViewContext viewContext;

    public StandardButton(ViewContext viewContext, Command command) {
        this.viewContext = viewContext;
        setFocusable(false);
        addActionListener((e) -> command.execute());
        style();
    }

    private void style() {
        Theme theme = viewContext.getCurrentTheme();
        setBackground(theme.getBackground());
        setForeground(theme.getForeground());
        setFont(Fonts.STANDARD_FONT.getFont());
    }

    @Override
    public void updateStyle() {
        style();
    }
}
