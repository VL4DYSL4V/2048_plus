package preferences;

import enums.FieldDimension;
import observer.Subscriber;
import observer.event.UserPreferencesEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testUtils.LocaleUtils;
import testUtils.ThemeUtils;
import view.theme.Theme;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPreferencesTest {

    private static final FieldDimension FIELD_DIMENSION = FieldDimension.FOUR_AND_FOUR;
    private Subscriber<UserPreferencesEvent> subscriber1;
    private Subscriber<UserPreferencesEvent> subscriber2;
    private UserPreferences userPreferences;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        userPreferences = spy(new UserPreferences(
                LocaleUtils.getDefaultLocale(),
                ThemeUtils.getDefaultTheme(),
                FIELD_DIMENSION));
        subscriber1 = (Subscriber<UserPreferencesEvent>) mock(Subscriber.class);
        subscriber2 = (Subscriber<UserPreferencesEvent>) mock(Subscriber.class);
    }

    @Nested
    class SubscribeTest{

        @Test
        void subscribeNull() {
            assertThrows(NullPointerException.class, () -> userPreferences.subscribe(null));
        }

        @Test
        void subscribeNormally(){
            assertDoesNotThrow(() -> userPreferences.subscribe(subscriber2));
            userPreferences.notifySubscribers(any());
            verify(subscriber2).reactOnNotification(any());
        }

        @Test
        void subscribeRepeatedly(){
            userPreferences.subscribe(subscriber1);
            assertDoesNotThrow(() -> userPreferences.subscribe(subscriber1));
        }
    }

    @Nested
    class UnsubscribeTest{

        @Test
        void unsubscribeNull() {
            assertDoesNotThrow(() -> userPreferences.unsubscribe(null));
        }

        @Test
        void unsubscribeNormally(){
            userPreferences.subscribe(subscriber2);
            assertDoesNotThrow(() -> userPreferences.unsubscribe(subscriber2));
            userPreferences.notifySubscribers(any());
            verify(subscriber2, times(0)).reactOnNotification(any());
        }

        @Test
        void subscribeRepeatedly(){
            userPreferences.subscribe(subscriber1);
            userPreferences.unsubscribe(subscriber1);
            assertDoesNotThrow(() -> userPreferences.subscribe(subscriber1));
        }
    }

    @Test
    void notifySubscribers() {
        userPreferences.subscribe(subscriber1);
        userPreferences.subscribe(subscriber2);

        UserPreferencesEvent event = null;
        userPreferences.notifySubscribers(event);
        verify(subscriber1).reactOnNotification(event);
        verify(subscriber2).reactOnNotification(event);
    }

    @Nested
    class FieldDimensionTest {

        @Test
        void getFieldDimension() {
            assertNotNull(userPreferences.getFieldDimension());
        }

        @Test
        void setNull() {
            assertFalse(userPreferences.setFieldDimension(null));
        }

        @Test
        void setSameFieldDimension() {
            FieldDimension expected = userPreferences.getFieldDimension();
            boolean setResult = userPreferences.setFieldDimension(expected);
            assertFalse(setResult);
            assertEquals(expected, userPreferences.getFieldDimension());
        }

        @Test
        void setDifferentFieldDimension() {
            FieldDimension expected = userPreferences.getFieldDimension() == FIELD_DIMENSION
                    ? FieldDimension.FIVE_AND_FIVE
                    : FIELD_DIMENSION;
            boolean setResult = userPreferences.setFieldDimension(expected);
            assertEquals(expected, userPreferences.getFieldDimension());
            assertTrue(setResult);
        }

    }

    @Nested
    class LocaleTest {

        @Test
        void getLocale() {
            assertNotNull(userPreferences.getLocale());
        }

        @Test
        void setNull() {
            assertFalse(userPreferences.setLocale(null));
        }

        @Test
        void setSameLocale() {
            Locale expected = LocaleUtils.getDefaultLocale();
            boolean setResult = userPreferences.setLocale(expected);
            assertFalse(setResult);
            assertEquals(expected, userPreferences.getLocale());
        }

        @Test
        void setDifferentLocale() {
            Locale expected = Locale.GERMAN;
            boolean setResult = userPreferences.setLocale(expected);
            assertEquals(expected, userPreferences.getLocale());
            assertTrue(setResult);
        }

    }

    @Nested
    class ThemeTest {

        @Test
        void getTheme() {
            assertNotNull(userPreferences.getTheme());
        }

        @Test
        void setNull() {
            assertFalse(userPreferences.setTheme(null));
        }

        @Test
        void setSameTheme() {
            Theme expected = userPreferences.getTheme();
            boolean setResult = userPreferences.setTheme(expected);
            assertFalse(setResult);
            assertEquals(expected, userPreferences.getTheme());
        }

        @Test
        void setDifferentTheme() {
            Theme expected = ThemeUtils.getRandomTheme();
            boolean setResult = userPreferences.setTheme(expected);
            assertEquals(expected, userPreferences.getTheme());
            assertTrue(setResult);
        }
    }
}