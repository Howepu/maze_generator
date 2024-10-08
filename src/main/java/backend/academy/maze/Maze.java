package backend.academy.maze;

import lombok.Getter;

public class Maze {
    @Getter
    private final int height;
    @Getter
    private final int width;
    @Getter
    private final Cell[][] grid;

    public Maze(int height, int width, Cell[][] grid) {
        this.height = height;
        this.width = width;
        this.grid = grid;
    }
}
