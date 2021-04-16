package view.component;

import command.VolatileCommand;
import context.UserPreferences;
import enums.FieldDimension;
import view.enums.Fonts;
import view.theme.Theme;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DimensionJComboBox extends JComboBox<String> implements StyleVaryingComponent {

    private static final Map<String, FieldDimension> DIMENSION_MAP = new LinkedHashMap<>();
    private final UserPreferences userPreferences;

    static {
        DIMENSION_MAP.put("3 x 3", FieldDimension.THREE_AND_THREE);
        DIMENSION_MAP.put("4 x 4", FieldDimension.FOUR_AND_FOUR);
        DIMENSION_MAP.put("5 x 5", FieldDimension.FIVE_AND_FIVE);
        DIMENSION_MAP.put("6 x 6", FieldDimension.SIX_AND_SIX);
    }

    public DimensionJComboBox(UserPreferences userPreferences,
                              VolatileCommand<FieldDimension> dimensionChangeCommand) {
        super(DIMENSION_MAP.keySet().toArray(new String[0]));
        this.userPreferences = userPreferences;
        style();
        config(dimensionChangeCommand);
    }

    private void style() {
        themeSpecificStyle();
        setFont(Fonts.STANDARD_FONT.getFont());
    }

    private void themeSpecificStyle() {
        Theme theme = userPreferences.getTheme();
        setBackground(theme.getBackground());
        setForeground(theme.getForeground());
    }

    private void config(VolatileCommand<FieldDimension> dimensionChangeCommand){
        addActionListener(e -> {
            JComboBox<?> box = (JComboBox<?>) e.getSource();
            String selected = (String) box.getSelectedItem();
            dimensionChangeCommand.setParam(DIMENSION_MAP.get(selected));
            dimensionChangeCommand.execute();
        });
    }

    @Override
    public void updateStyle() {
        themeSpecificStyle();
    }
}

