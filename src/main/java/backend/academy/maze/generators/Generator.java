package backend.academy.maze.generators;

import backend.academy.maze.Maze;

public interface Generator {
    Maze generate(int height, int width);
}
