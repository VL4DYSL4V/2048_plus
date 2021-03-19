package view.component;

import entity.Field;
import entity.FieldElement;
import model.Model;
import enums.FieldDimension;
import view.theme.Theme;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class FieldCanvas extends Canvas {

    private final Model model;
    private final RenderingContext renderingContext;
    private Theme theme;
    private boolean firstRendered = true;

    public FieldCanvas(Model model, Theme theme) {
        this.model = model;
        this.theme = theme;
        this.renderingContext = new RenderingContext(model.getFieldDimension());
    }

    private final class RenderingContext {

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

        public RenderingContext(FieldDimension fieldDimension) {
            this.fieldDimension = fieldDimension;
        }

        private void updateDueToChanges() {
            this.fieldDimension = model.getFieldDimension();
            centerX = FieldCanvas.super.getWidth() / 2;
            centerY = FieldCanvas.super.getHeight() / 2;
            cellWidth = FieldCanvas.super.getWidth() / (fieldDimension.getWidth() + 2);
            cellHeight = FieldCanvas.super.getHeight() / (fieldDimension.getHeight() + 2);
            cellWidthMargin = cellWidth / fieldDimension.getWidth() - 1;
            cellHeightMargin = cellHeight / fieldDimension.getHeight() - 1;
            setupIndexToRectangleX();
            setupIndexToRectangleY();
            setupPowerToScaledImage();
        }

        private void setupPowerToScaledImage(){
            powerToScaledImage.clear();
            Map<Integer, Image> powerToImageMap = theme.powerToImageMap();
            for(Integer power: powerToImageMap.keySet()){
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

        private Image getImage(Integer power){
            return powerToScaledImage.get(power);
        }
    }

    @Override
    public void paint(Graphics g) {
        if(firstRendered){
            firstRendered = false;
            renderingContext.updateDueToChanges();
        }
        super.paint(g);
        drawField(g);
    }

    private void drawField(Graphics g) {
        Field field = model.getField().copy();
        FieldDimension dimension = field.getFieldDimension();
        for (int i = 0; i < dimension.getHeight(); i++) {
            for (FieldElement element : field.getRow(i)) {
                int x = renderingContext.getRectangleX(element.getCoordinates2D().getX());
                int y = renderingContext.getRectangleY(element.getCoordinates2D().getY());
                Image image = renderingContext.getImage(element.getValue());
                g.drawImage(image, x, y, this);
            }
        }
    }

    public void updateField(){
        repaint();
    }

}
