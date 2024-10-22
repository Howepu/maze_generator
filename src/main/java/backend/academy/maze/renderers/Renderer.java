package backend.academy.maze.renderers;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import java.util.List;

public interface Renderer {

    String render(Maze maze);

    String render(Maze maze, List<Coordinate> path);
}
