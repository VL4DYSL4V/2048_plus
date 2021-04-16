package command;

import context.UserPreferences;
import enums.FieldDimension;
import handler.CommandHandler;

public final class DimensionChangeCommand implements VolatileCommand<FieldDimension> {

    private final UserPreferences userPreferences;
    private final CommandHandler commandHandler;
    private volatile FieldDimension fieldDimension;

    public DimensionChangeCommand(UserPreferences userPreferences, CommandHandler commandHandler) {
        this.userPreferences = userPreferences;
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> {
            if(userPreferences.setFieldDimension(fieldDimension)){
                //TODO: Update file with preferences;
                System.out.println(fieldDimension);
            }
        });
    }

    @Override
    public void setParam(FieldDimension param) {
        this.fieldDimension = param;
    }
}
