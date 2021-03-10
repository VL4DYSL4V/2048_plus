package enums;

public enum FieldDimension {

    FOUR_AND_FOUR("4 x 4", 4, 4),
    FIVE_AND_FIVE("5 x 5", 5, 5),
    SIX_AND_SIX("6 x 6", 6, 6),
    SEVEN_AND_SEVEN("7 x 7", 7, 7),
    EIGHT_AND_EIGHT("8 x 8", 8, 8);

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
