package backend.academy.maze.solvers;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepthFirstSearch extends AbstractSolver {

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        // Используем общий метод для проверки стартовой и конечной клетки
        isValidStartAndEnd(maze, start, end);

        boolean[][] visited = new boolean[maze.height()][maze.width()];
        List<Coordinate> path = new ArrayList<>();

        // Начальный вызов DFS
        if (dfs(maze, start, end, visited, path)) {
            return path; // Путь уже в правильном порядке
        }
        return Collections.emptyList(); // Путь не найден
    }

    // Рекурсивный DFS
    private boolean dfs(Maze maze, Coordinate current, Coordinate end, boolean[][] visited, List<Coordinate> path) {
        int row = current.row();
        int col = current.col();

        if (!isInBounds(maze, current) || visited[row][col] || maze.grid()[row][col].type() == Cell.Type.WALL) {
            return false; // Выход за границы или стена
        }

        visited[row][col] = true;
        path.add(current);

        if (current.equals(end)) {
            return true; // Целевая клетка найдена
        }

        for (Coordinate neighbor : getNeighbors(maze, current)) {
            if (dfs(maze, neighbor, end, visited, path)) {
                return true;
            }
        }

        path.remove(path.size() - 1); // Убираем клетку из пути
        return false;
    }

    private boolean isInBounds(Maze maze, Coordinate coord) {
        int row = coord.row();
        int col = coord.col();
        return row >= 0 && row < maze.height() && col >= 0 && col < maze.width();
    }
}

