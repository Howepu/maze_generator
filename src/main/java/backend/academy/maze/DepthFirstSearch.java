package backend.academy.maze;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstSearch {
    private final boolean[][] maze; // Лабиринт
    private final boolean[][] visited; // Посещённые клетки
    private final List<int[]> path; // Путь от старта до цели
    private final int height; // Высота лабиринта
    private final int width; // Ширина лабиринта

    public DepthFirstSearch(boolean[][] maze) {
        this.maze = maze;
        this.height = maze.length;
        this.width = maze[0].length;
        this.visited = new boolean[height][width]; // Инициализация массива посещённых клеток
        this.path = new ArrayList<>(); // Инициализация списка для пути
    }

    // Метод для запуска поиска пути
    public List<int[]> findPath(int startX, int startY, int endX, int endY) {
        if (dfs(startX, startY, endX, endY)) {
            return path; // Возвращаем найденный путь
        }
        return new ArrayList<>(); // Если путь не найден, возвращаем пустой список
    }

    // Рекурсивный метод для поиска пути в глубину
    private boolean dfs(int x, int y, int endX, int endY) {
        // Проверяем границы и препятствия
        if (!isInBounds(x, y) || visited[y][x] || maze[y][x]) {
            return false; // Выход за границы или посещённая клетка, или стена
        }

        // Отмечаем клетку как посещённую
        visited[y][x] = true;
        path.add(new int[]{x, y}); // Добавляем текущую клетку в путь

        // Проверяем, достигли ли мы целевой клетки
        if (x == endX && y == endY) {
            return true; // Путь найден
        }

        // Рекурсивно исследуем соседние клетки (вниз, вверх, вправо, влево)
        if (dfs(x + 1, y, endX, endY)    // Вправо
            || dfs(x - 1, y, endX, endY) // Влево
            || dfs(x, y + 1, endX, endY) // Вниз
            || dfs(x, y - 1, endX, endY)) { // Вверх
            return true; // Если путь найден в любом направлении
        }

        // Если ни один из путей не сработал, убираем последнюю клетку из пути
        path.remove(path.size() - 1);
        return false; // Путь не найден
    }

    // Метод проверки, находится ли клетка в пределах границ
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
