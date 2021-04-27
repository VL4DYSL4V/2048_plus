package dao.theme;

import exception.FetchException;
import view.theme.Theme;

public interface ThemeDao {

    Theme loadTheme(String path) throws FetchException;

    Theme loadByName(String name) throws FetchException;

}
