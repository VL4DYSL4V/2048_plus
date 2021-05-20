package command.menu;

import command.VolatileCommand;
import dao.preferences.PreferencesDao;
import preferences.UserPreferences;
import dao.game.GameDataDao;
import enums.FieldDimension;
import exception.FetchException;
import handler.CommandHandler;
import model.GameData;
import model.GameModel;

import java.util.Objects;

public final class DimensionChangeCommand implements VolatileCommand<FieldDimension> {

    private final UserPreferences userPreferences;
    private final PreferencesDao preferencesDAO;
    private final CommandHandler commandHandler;
    private final GameModel gameModel;
    private final GameDataDao gameDataDao;
    private volatile FieldDimension fieldDimension;

    public DimensionChangeCommand(UserPreferences userPreferences,
                                  PreferencesDao preferencesDAO,
                                  CommandHandler commandHandler,
                                  GameModel gameModel,
                                  GameDataDao gameDataDao) {
        Objects.requireNonNull(userPreferences);
        Objects.requireNonNull(preferencesDAO);
        Objects.requireNonNull(commandHandler);
        Objects.requireNonNull(gameModel);
        Objects.requireNonNull(gameDataDao);
        this.userPreferences = userPreferences;
        this.preferencesDAO = preferencesDAO;
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
                    preferencesDAO.saveOrUpdate(userPreferences);
                } catch (FetchException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setParam(FieldDimension param) {
        Objects.requireNonNull(param);
        this.fieldDimension = param;
    }
}
