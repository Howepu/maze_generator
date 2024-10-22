package backend.academy.maze.solvers;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import java.util.List;

public interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
