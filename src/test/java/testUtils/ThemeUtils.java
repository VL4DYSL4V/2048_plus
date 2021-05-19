package testUtils;

import view.theme.Theme;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class ThemeUtils {

    private ThemeUtils(){

    }

    public static Theme getDefaultTheme(){
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        Image image = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
        return new Theme("Default", Color.DARK_GRAY, Color.WHITE, font, image, image, new HashMap<>(), image);
    }

    public static Theme getRandomTheme(){
        Random random = ThreadLocalRandom.current();
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 1 + random.nextInt(20));
        Image image = new BufferedImage( 1 + random.nextInt(100), 1 + random.nextInt(100),
                BufferedImage.TYPE_3BYTE_BGR);
        Color bgColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Color fgColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return new Theme("Random", bgColor, fgColor, font, image, image, new HashMap<>(), image);
    }
}
