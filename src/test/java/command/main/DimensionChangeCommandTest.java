package command.main;

import mock.ThisThreadCommandHandler;
import command.menu.DimensionChangeCommand;
import dao.game.GameDataDao;
import dao.preferences.PreferencesDao;
import enums.FieldDimension;
import exception.FetchException;
import handler.CommandHandler;
import model.GameData;
import model.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import preferences.UserPreferences;
import testUtils.LocaleUtils;
import testUtils.ThemeUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DimensionChangeCommandTest {

    private static final FieldDimension fieldDimension = FieldDimension.FOUR_AND_FOUR;
    private UserPreferences userPreferences;
    private PreferencesDao preferencesDAO;
    private CommandHandler commandHandler;
    private GameDataDao gameDataDao;
    private GameModel gameModel;
    private DimensionChangeCommand dimensionChangeCommand;

    @BeforeEach
    void setup() {
        gameModel = spy(new GameModel(fieldDimension));
        userPreferences = spy(
                new UserPreferences(LocaleUtils.getDefaultLocale(),
                        ThemeUtils.getDefaultTheme(), fieldDimension));
        preferencesDAO = spy(PreferencesDao.class);
        commandHandler = spy(ThisThreadCommandHandler.class);
        gameDataDao = spy(new GameDataImpl());
        dimensionChangeCommand = new DimensionChangeCommand(userPreferences,
                preferencesDAO, commandHandler, gameModel, gameDataDao);
    }

    @Test
    void testWithSameDimension() throws FetchException {
        FieldDimension neuFieldDimension = FieldDimension.FOUR_AND_FOUR;
        dimensionChangeCommand.setParam(neuFieldDimension);
        dimensionChangeCommand.execute();

        verify(commandHandler).execute(any());
        verify(userPreferences).setFieldDimension(neuFieldDimension);
        verify(gameDataDao, times(0)).getByDimension(neuFieldDimension);
        verify(gameModel, times(0)).setGameData(any());
        verify(preferencesDAO, times(0)).saveOrUpdate(any());

        assertEquals(neuFieldDimension, userPreferences.getFieldDimension());
    }

    @Test
    void testWithAnotherDimension() throws FetchException {
        FieldDimension neuFieldDimension = FieldDimension.SIX_AND_SIX;
        dimensionChangeCommand.setParam(neuFieldDimension);
        dimensionChangeCommand.execute();

        verify(commandHandler).execute(any());
        verify(userPreferences).setFieldDimension(neuFieldDimension);
        verify(gameDataDao).getByDimension(neuFieldDimension);
        verify(gameModel).setGameData(any());
        verify(preferencesDAO).saveOrUpdate(userPreferences);

        assertEquals(neuFieldDimension, userPreferences.getFieldDimension());
    }

    @Test
    void testWithNullDimension() {
        assertThrows(NullPointerException.class, () -> dimensionChangeCommand.setParam(null));
    }

    @Test
    void nullConstructorArgTest() {
        assertThrows(NullPointerException.class, () -> new DimensionChangeCommand(null,
                preferencesDAO, commandHandler, gameModel, gameDataDao));
        assertThrows(NullPointerException.class, () -> new DimensionChangeCommand(userPreferences,
                null, commandHandler, gameModel, gameDataDao));
        assertThrows(NullPointerException.class, () -> new DimensionChangeCommand(userPreferences,
                preferencesDAO, null, gameModel, gameDataDao));
        assertThrows(NullPointerException.class, () -> new DimensionChangeCommand(userPreferences,
                preferencesDAO, commandHandler, null, gameDataDao));
        assertThrows(NullPointerException.class, () -> new DimensionChangeCommand(userPreferences,
                preferencesDAO, commandHandler, gameModel, null));
    }

    private static class GameDataImpl implements GameDataDao {

        @Override
        public void save(GameData gameData, FieldDimension fieldDimension) {

        }

        @Override
        public GameData getByDimension(FieldDimension fieldDimension) {
            return new GameData(fieldDimension);
        }
    }
}