package backend.academy.maze;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstSearch implements Solver {

    // Метод для запуска поиска пути
    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        boolean[][] visited = new boolean[maze.height()][maze.width()]; // Инициализация массива посещённых клеток
        List<Coordinate> path = new ArrayList<>(); // Инициализация списка для пути

        if (dfs(maze, start.row(), start.col(), end.row(), end.col(), visited, path)) {
            return path; // Возвращаем найденный путь
        }
        return new ArrayList<>(); // Если путь не найден, возвращаем пустой список
    }

    // Рекурсивный метод для поиска пути в глубину
    private boolean dfs(Maze maze, int x, int y, int endX, int endY, boolean[][] visited, List<Coordinate> path) {
        // Проверяем границы и препятствия
        if (!isInBounds(x, y, maze) || visited[y][x] || maze.grid()[y][x].type() == Cell.Type.WALL) {
            return false; // Выход за границы или посещённая клетка, или стена
        }

        // Отмечаем клетку как посещённую
        visited[y][x] = true;
        path.add(new Coordinate(x, y)); // Добавляем текущую клетку в путь

        // Проверяем, достигли ли мы целевой клетки
        if (x == endX && y == endY) {
            return true; // Путь найден
        }

        // Рекурсивно исследуем соседние клетки (вниз, вверх, вправо, влево)
        if (dfs(maze, x + 1, y, endX, endY, visited, path)    // Вправо
            || dfs(maze, x - 1, y, endX, endY, visited, path) // Влево
            || dfs(maze, x, y + 1, endX, endY, visited, path) // Вниз
            || dfs(maze, x, y - 1, endX, endY, visited, path)) { // Вверх
            return true; // Если путь найден в любом направлении
        }

        // Если ни один из путей не сработал, убираем последнюю клетку из пути
        path.remove(path.size() - 1);
        return false; // Путь не найден
    }


    // Метод проверки, находится ли клетка в пределах границ
    private boolean isInBounds(int x, int y, Maze maze) {
        return x >= 0 && x < maze.width() && y >= 0 && y < maze.height();
    }
}
