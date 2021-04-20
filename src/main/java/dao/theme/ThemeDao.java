package dao.theme;

import exception.FetchException;
import view.theme.Theme;

import java.util.Collection;

public interface ThemeDao {

    Theme loadTheme(String path) throws FetchException;

    Theme loadByName(String name) throws FetchException;

    Collection<Theme> loadAll() throws FetchException;

}
