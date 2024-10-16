package backend.academy.maze;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstSearch implements Solver {

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {

        // Если стартовая клетка - стена, делаем её проходом
        if (maze.grid()[start.row()][start.col()].type() == Cell.Type.WALL) {
            maze.grid()[start.row()][start.col()] = new Cell(start.row(), start.col(), Cell.Type.PASSAGE);
        }

        // Если конечная клетка - стена, делаем её проходом
        if (maze.grid()[end.row()][end.col()].type() == Cell.Type.WALL) {
            maze.grid()[end.row()][end.col()] = new Cell(end.row(), end.col(), Cell.Type.PASSAGE);
        }

        boolean[][] visited = new boolean[maze.height()][maze.width()];
        List<Coordinate> path = new ArrayList<>();

        // Начальный вызов DFS
        if (dfs(maze, start.row(), start.col(), end.row(), end.col(), visited, path)) {
            return path;
        }
        return new ArrayList<>(); // Путь не найден
    }

    // Рекурсивный DFS с учётом стоимости
    private boolean dfs(Maze maze, int x, int y, int endX, int endY, boolean[][] visited, List<Coordinate> path) {
        if (!isInBounds(x, y, maze) || visited[y][x] || maze.grid()[y][x].type() == Cell.Type.WALL) {
            return false; // Выход за границы или стена
        }

        visited[y][x] = true;
        path.add(new Coordinate(x, y));

        if (x == endX && y == endY) {
            return true; // Целевая клетка найдена
        }

        // Учитываем стоимость движения
        int currentCost = getCost(maze.grid()[y][x]);

        // Рекурсивно проверяем соседние клетки
        if (dfs(maze, x + currentCost, y, endX, endY, visited, path) || // Вправо
            dfs(maze, x - currentCost, y, endX, endY, visited, path) || // Влево
            dfs(maze, x, y + currentCost, endX, endY, visited, path) || // Вниз
            dfs(maze, x, y - currentCost, endX, endY, visited, path)) {  // Вверх
            return true;
        }

        path.remove(path.size() - 1); // Убираем клетку из пути
        return false;
    }

    // Получаем стоимость клетки
    private int getCost(Cell cell) {
        switch (cell.type()) {
            case SAND:
                return 3; // Песок
            case COIN:
                return 0; // Монеты
            default:
                return 1; // Обычные клетки
        }
    }

    private boolean isInBounds(int x, int y, Maze maze) {
        return x >= 0 && x < maze.width() && y >= 0 && y < maze.height();
    }
}
