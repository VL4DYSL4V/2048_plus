package view;

import model.Model;
import observer.Subscriber;
import observer.event.EventType;
import view.context.ThemeHolder;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;

public final class EndOfGameDialog extends JDialog implements Subscriber {

    private final ThemeHolder themeHolder;
    private final Model model;
    private final Dimension dimension;

    public EndOfGameDialog(JFrame owner, Model model, ThemeHolder themeHolder, Dimension dimension) {
        super(owner);
        this.themeHolder = themeHolder;
        this.model = model;
        this.dimension = dimension;
        configureJDialog();
        styleJDialog();
        constructDialog();
    }

    public void constructDialog() {
        JLabel iconLabel = new JLabel(getScaledImageIcon());
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        Component contentPane = getContentPane();
        layout.putConstraint(SpringLayout.NORTH, contentPane, 0, SpringLayout.NORTH, iconLabel);
        layout.putConstraint(SpringLayout.WEST, contentPane, 0, SpringLayout.WEST, iconLabel);
        add(iconLabel);
    }

    private ImageIcon getScaledImageIcon() {
        Image gameOverImage = themeHolder.getTheme().gameOverImage();
        int width = dimension.width;
        int height = dimension.height;
        return new ImageIcon(gameOverImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    private void configureJDialog() {
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setFocusable(false);
    }

    private void styleJDialog() {
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
        Theme theme = themeHolder.getTheme();
        getContentPane().setBackground(theme.getBackground());
        setUndecorated(true);
    }

    @Override
    public void reactOnNotification(EventType eventType) {
        if(model.gameIsOver()){
            setVisible(true);
        }else{
            setVisible(false);
        }
    }
}
