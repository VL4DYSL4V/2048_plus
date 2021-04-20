package view.component;

import preferences.UserPreferences;
import entity.Field;
import entity.FieldElement;
import enums.FieldDimension;
import model.GameModel;
import view.theme.Theme;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class FieldCanvas extends Canvas implements ThemeVaryingComponent {

    private final GameModel gameModel;
    private final FieldRenderingContext fieldRenderingContext;
    private final UserPreferences userPreferences;
    private Image scaledFieldBcgImage;

    public FieldCanvas(GameModel gameModel, UserPreferences userPreferences, Dimension dimension) {
        this.gameModel = gameModel;
        this.userPreferences = userPreferences;
        this.fieldRenderingContext = new FieldRenderingContext(gameModel.getFieldDimension());
        configure(dimension);
    }

    private final class FieldRenderingContext {

        private FieldDimension fieldDimension;
        private final Map<Integer, Integer> indexToRectangleX = new HashMap<>();
        private final Map<Integer, Integer> indexToRectangleY = new HashMap<>();
        private final Map<Integer, Image> powerToScaledImage = new HashMap<>();
        private int cellWidth;
        private int cellHeight;
        private int cellWidthMargin;
        private int cellHeightMargin;
        private int centerX;
        private int centerY;

        public FieldRenderingContext(FieldDimension fieldDimension) {
            this.fieldDimension = fieldDimension;
        }

        private void updateTheme(Theme theme) {
            setupPowerToScaledImage(theme);
        }

        private void configure(Theme theme) {
            this.fieldDimension = gameModel.getFieldDimension();
            centerX = FieldCanvas.super.getWidth() / 2;
            centerY = FieldCanvas.super.getHeight() / 2;
            cellWidth = FieldCanvas.super.getWidth() / (fieldDimension.getWidth() + 2);
            cellHeight = FieldCanvas.super.getHeight() / (fieldDimension.getHeight() + 2);
            cellWidthMargin = cellWidth / fieldDimension.getWidth() - 1;
            cellHeightMargin = cellHeight / fieldDimension.getHeight() - 1;
            setupIndexToRectangleX();
            setupIndexToRectangleY();
            setupPowerToScaledImage(theme);
        }

        private void setupPowerToScaledImage(Theme theme) {
            powerToScaledImage.clear();
            Map<Integer, Image> powerToImageMap = theme.powerToImageMap();
            for (Integer power : powerToImageMap.keySet()) {
                Image unscaled = powerToImageMap.get(power);
                powerToScaledImage.put(power, unscaled.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH));
            }
        }

        private void setupIndexToRectangleX() {
            indexToRectangleX.clear();
            if (fieldDimension.getWidth() % 2 == 0) {
                double halfMaxX = ((double) fieldDimension.getWidth() - 1) / 2;
                for (int i = 0; i < fieldDimension.getWidth(); i++) {
                    int value = (int) (centerX + (i - halfMaxX) * cellWidthMargin +
                            (i - ((int) halfMaxX + 1)) * cellWidth);
                    indexToRectangleX.put(i, value);
                }
            } else {
                for (int i = 0; i < fieldDimension.getWidth(); i++) {
                    int value = (int) (centerX +
                            (i - ((double) fieldDimension.getWidth() / 2))
                                    * cellWidth
                            + (i - fieldDimension.getMaxX() / 2)
                            * cellWidthMargin);
                    indexToRectangleX.put(i, value);
                }
            }
        }

        private void setupIndexToRectangleY() {
            indexToRectangleY.clear();
            if (fieldDimension.getHeight() % 2 == 0) {
                double halfMaxY = ((double) fieldDimension.getHeight() - 1) / 2;
                for (int i = 0; i < fieldDimension.getHeight(); i++) {
                    int value = (int) (centerY + (i - halfMaxY) * cellHeightMargin +
                            (i - ((int) halfMaxY + 1)) * cellHeight);
                    indexToRectangleY.put(i, value);
                }
            } else {
                for (int i = 0; i < fieldDimension.getHeight(); i++) {
                    int value = (int) (centerY +
                            (i - ((double) fieldDimension.getHeight()) / 2)
                                    * cellHeight
                            + (i - fieldDimension.getMaxY() / 2)
                            * cellHeightMargin);
                    indexToRectangleY.put(i, value);
                }
            }
        }

        private int getRectangleX(int coordX) {
            return indexToRectangleX.get(coordX);
        }

        private int getRectangleY(int coordY) {
            return indexToRectangleY.get(coordY);
        }

        private Image getImage(Integer power) {
            return powerToScaledImage.get(power);
        }
    }

    private void configure(Dimension dimension) {
        setSize(dimension);
        setPreferredSize(dimension);
        setupRenderingContext();
        scaledFieldBcgImage = scaledFieldBcgImage(userPreferences.getTheme().fieldBackgroundImage());
    }

    private void setupRenderingContext() {
        fieldRenderingContext.configure(userPreferences.getTheme());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawFieldBackground(g);
        drawField(g);
    }

    public void updateField() {
        repaint();
    }

    @Override
    public void applyNewTheme() {
        updateTheme();
    }

    public void updateDimension() {
        setupRenderingContext();
    }

    private void updateTheme() {
        Theme theme = userPreferences.getTheme();
        fieldRenderingContext.updateTheme(theme);
        scaledFieldBcgImage = scaledFieldBcgImage(theme.fieldBackgroundImage());
    }

    private void drawFieldBackground(Graphics g) {
        Image image = scaledFieldBcgImage;
        g.drawImage(image, 0, 0, this);
    }

    private Image scaledFieldBcgImage(Image image) {
        return image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
    }

    private void drawField(Graphics g) {
        Field field = gameModel.getField();
        FieldDimension dimension = field.getFieldDimension();
        for (int i = 0; i < dimension.getHeight(); i++) {
            for (FieldElement element : field.getRow(i)) {
                int x = fieldRenderingContext.getRectangleX(element.getCoordinates2D().getX());
                int y = fieldRenderingContext.getRectangleY(element.getCoordinates2D().getY());
                Image image = fieldRenderingContext.getImage(element.getValue());
                g.drawImage(image, x, y, this);
            }
        }
    }

}
