package view.theme;

import java.awt.*;
import java.util.Map;

public interface Theme {

    String getName();

    Color getBackground();

    Color getForeground();

    Image fieldBackgroundImage();

    Image welcomeImage();

    Map<Integer, Image> powerToImageMap();

    Image gameOverImage();

}
