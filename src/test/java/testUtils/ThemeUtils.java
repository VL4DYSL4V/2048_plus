package testUtils;

import view.theme.Theme;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public final class ThemeUtils {

    private ThemeUtils(){

    }

    public static Theme getDefaultTheme(){
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        Image image = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
        return new Theme("Default", Color.DARK_GRAY, Color.WHITE, font, image, image, new HashMap<>(), image);
    }
}
