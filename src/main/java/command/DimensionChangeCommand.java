package command;

import preferences.UserPreferences;
import dao.game.GameDataDao;
import enums.FieldDimension;
import exception.FetchException;
import handler.CommandHandler;
import model.GameData;
import model.GameModel;

public final class DimensionChangeCommand implements VolatileCommand<FieldDimension> {

    private final UserPreferences userPreferences;
    private final CommandHandler commandHandler;
    private final GameModel gameModel;
    private final GameDataDao gameDataDao;
    private volatile FieldDimension fieldDimension;

    public DimensionChangeCommand(UserPreferences userPreferences,
                                  CommandHandler commandHandler,
                                  GameModel gameModel,
                                  GameDataDao gameDataDao) {
        this.userPreferences = userPreferences;
        this.commandHandler = commandHandler;
        this.gameModel = gameModel;
        this.gameDataDao = gameDataDao;
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> {
            if(userPreferences.setFieldDimension(fieldDimension)){
                try {
                    GameData neuGameData = gameDataDao.getByDimension(fieldDimension);
                    gameModel.setGameData(neuGameData);
                } catch (FetchException e) {
                    e.printStackTrace();
                }
                //TODO: Update file with preferences;
            }
        });
    }

    @Override
    public void setParam(FieldDimension param) {
        this.fieldDimension = param;
    }
}
