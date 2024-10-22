package backend.academy.maze;

public record Cell(Coordinate coordinate, Type type) {
    public enum Type { WALL, PASSAGE, SAND, COIN }

}
