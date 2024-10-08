package backend.academy.maze;

import java.util.List;

public interface Renderer {
    String render(Maze maze);
    String render(Maze maze, List<Coordinate> path);
}
