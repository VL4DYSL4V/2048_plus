package view.component.combo_box;

import command.VolatileCommand;
import preferences.UserPreferences;
import view.component.ThemeVaryingComponent;
import view.util.ThemeUtils;

import javax.swing.*;
import java.util.Map;
import java.util.Objects;

public final class StandardJComboBox<T> extends JComboBox<String> implements ThemeVaryingComponent {

    private final Map<String, T> contentMap;
    private final UserPreferences userPreferences;

    public StandardJComboBox(Map<String, T> contentMap, UserPreferences userPreferences,
                             VolatileCommand<T> contentChangeCommand) {
        super(contentMap.keySet().toArray(new String[0]));
        this.contentMap = contentMap;
        this.userPreferences = userPreferences;
        applyNewTheme();
        config(contentChangeCommand);
    }

    private void config(VolatileCommand<T> themeChangeCommand) {
        addActionListener(e -> {
            JComboBox<?> box = (JComboBox<?>) e.getSource();
            String selected = (String) box.getSelectedItem();
            themeChangeCommand.setParam(contentMap.get(selected));
            themeChangeCommand.execute();
        });
    }

    public void selectItem(T object) {
        for (Map.Entry<String, T> entry : contentMap.entrySet()) {
            if (Objects.equals(object, entry.getKey())) {
                setSelectedItem(entry.getKey());
                break;
            }
        }
    }

    @Override
    public void applyNewTheme() {
        ThemeUtils.style(this, userPreferences.getTheme());
    }
}
