package view;

import org.springframework.stereotype.Component;
import view.enums.FrameSize;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;

@Component
public final class MainFrame extends JFrame {

    //TODO: REPLACE WITH view.component.BackgroundPanel
    private final JPanel rootPanel = new JPanel();
    private final JLabel welcomeImageLabel;

    public MainFrame(Theme theme){
        this.welcomeImageLabel = new JLabel(scaledWelcomeIcon(theme));
        styleComponents();
        configComponents();
        constructWindow();
    }

    private void constructWindow(){
        add(rootPanel);
        rootPanel.add(welcomeImageLabel);
    }

    private void configComponents(){
        config();
    }

    private void styleComponents(){
        style();
    }

    private static ImageIcon scaledWelcomeIcon(Theme theme){
        Dimension dimension = FrameSize.MAIN_FRAME.getDimension();
        int width = dimension.width;
        int height = (int) (dimension.height * 0.95);
        Image img = theme.welcomeImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }



    private void style(){
        Dimension dimension = FrameSize.MAIN_FRAME.getDimension();
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
        setTitle("2048+");
    }

    private void config(){
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }

}
