package util;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertyUtilsTest {

    @Test
    void nullLocationTest(){
        assertThrows(NullPointerException.class, () -> PropertyUtils.loadByLocation(null));
    }

    @Test
    void unexistingLocationTest(){
        assertThrows(RuntimeException.class, () -> PropertyUtils.loadByLocation("bla-bla-bla"));
    }

    @Test
    void loadExistingProperties(){
        Properties expected = getExpectedProperties();
        Properties actual = PropertyUtils.loadByLocation("repository/saved_games.properties");
        assertEquals(expected, actual);
    }

    private Properties getExpectedProperties(){
        Properties out = new Properties();
        out.setProperty("saved-games-directory-name", "savedGames");
        out.setProperty("file.three-and-three", "three_and_three.txt");
        out.setProperty("file.four-and-four", "four_and_four.txt");
        out.setProperty("file.five-and-five", "five_and_five.txt");
        out.setProperty("file.six-and-six", "six_and_six.txt");
        return out;
    }
}