package backend.academy.maze;

public enum CellType {
    WALL("#"), PASSAGE(" "), SAND("~"), COIN("O");

    private final String representation;

    CellType(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }
}
