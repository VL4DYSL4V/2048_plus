package enums;

public enum FieldDimension {

    THREE_AND_THREE("3 x 3", 3, 3),
    FOUR_AND_FOUR("4 x 4", 4, 4),
    FIVE_AND_FIVE("5 x 5", 5, 5),
    SIX_AND_SIX("6 x 6", 6, 6);

    private final String representation;
    private final int width;
    private final int height;

    private final int minX;
    private final int maxX;

    private final int minY;
    private final int maxY;

    FieldDimension(String representation, int width, int height) {
        this.representation = representation;
        this.width = width;
        this.height = height;
        this.minX = 0;
        this.maxX = width - 1;
        this.minY = 0;
        this.maxY = height - 1;
    }

    public String getRepresentation() {
        return representation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }
}
