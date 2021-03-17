package view;

import controller.FieldShiftController;
import entity.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import view.enums.FrameSize;
import view.theme.Theme;
import view.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;

@Component
public final class GameFrame extends JFrame {

    private final JPanel rootPanel = new JPanel();
    private final JPanel controlPanel = new JPanel();

    private final Model model;
    private final FieldShiftController fieldShiftController;
    private Theme theme;

    @Autowired
    public GameFrame(Model model, FieldShiftController fieldShiftController,
                     @Qualifier("theme") Theme theme) throws HeadlessException {
        this.model = model;
        this.fieldShiftController = fieldShiftController;
        this.theme = theme;
        configComponents();
        styleComponents();
        constructWindow();
    }

    private void constructWindow(){
        add(rootPanel);
        SpringLayout layout = new SpringLayout();
        rootPanel.setLayout(layout);
        layout.putConstraint(SpringLayout.WEST, controlPanel, 0, SpringLayout.WEST, rootPanel);
        layout.putConstraint(SpringLayout.NORTH, controlPanel, 0, SpringLayout.NORTH, rootPanel);
        rootPanel.add(controlPanel);
    }

    private void configComponents() {
        configFrame();
        configRootPanel();
        configControlPanel();
    }

    private void styleComponents() {
        styleFrame();
        styleRootPanel();
        styleControlPanel();
    }

    private void styleControlPanel(){
        controlPanel.setBackground(theme.getBackground());
        controlPanel.setForeground(theme.getForeground());
        controlPanel.setBorder(BorderFactory.createEtchedBorder());
        Dimension frameDimension = FrameSize.GAME_FRAME.getDimension();
        controlPanel.setPreferredSize(new Dimension(frameDimension.width, Math.abs(frameDimension.width - frameDimension.height)));
    }

    private void configControlPanel(){
        controlPanel.setLayout(new GridBagLayout());
    }

    private void styleRootPanel(){
        rootPanel.setBackground(theme.getBackground());
        rootPanel.setForeground(theme.getForeground());
        rootPanel.setSize(FrameSize.GAME_FRAME.getDimension());
    }

    private void configRootPanel(){
        rootPanel.setLayout(new GridBagLayout());
    }

    private void styleFrame() {
        Dimension dimension = FrameSize.GAME_FRAME.getDimension();
        setSize(dimension);
        setLocation((ScreenUtils.getScreenWidth() - dimension.width) / 2,
                (ScreenUtils.getScreenHeight() - dimension.height) / 2);
        setTitle("2048+");
    }

    private void configFrame() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }
}
