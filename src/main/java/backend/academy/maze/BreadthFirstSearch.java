package backend.academy.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BreadthFirstSearch implements Solver {

    private final int sand = 3;
    private final int coin = 0;


    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        // Если стартовая клетка - стена, делаем её проходом.
        if (maze.grid()[start.row()][start.col()].type() == Cell.Type.WALL) {
            maze.grid()[start.row()][start.col()] = new Cell(start.row(), start.col(), Cell.Type.PASSAGE);
        }

        // Если конечная клетка - стена, делаем её проходом
        if (maze.grid()[end.row()][end.col()].type() == Cell.Type.WALL) {
            maze.grid()[end.row()][end.col()] = new Cell(end.row(), end.col(), Cell.Type.PASSAGE);
        }

        Queue<Coordinate> queue = new LinkedList<>();
        boolean[][] visited = new boolean[maze.height()][maze.width()];
        Map<Coordinate, Coordinate> prev = new HashMap<>();

        queue.add(start);
        visited[start.row()][start.col()] = true;

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            if (current.equals(end)) {
                return buildPath(prev, current);
            }

            for (Coordinate neighbor : getNeighbors(current, maze)) {
                if (!visited[neighbor.row()][neighbor.col()]) {
                    visited[neighbor.row()][neighbor.col()] = true;
                    queue.add(neighbor);
                    prev.put(neighbor, current);
                }
            }
        }
        return null; // Путь не найден
    }

    // Метод построения пути
    private List<Coordinate> buildPath(Map<Coordinate, Coordinate> prev, Coordinate end) {
        List<Coordinate> path = new ArrayList<>();
        for (Coordinate at = end; at != null; at = prev.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    // Метод для нахождения соседних клеток
    private List<Coordinate> getNeighbors(Coordinate point, Maze maze) {
        List<Coordinate> neighbors = new ArrayList<>();
        int row = point.row();
        int col = point.col();

        // Добавляем соседние клетки, если они проходимы
        addIfPassable(neighbors, maze, row - 1, col); // Вверх
        addIfPassable(neighbors, maze, row + 1, col); // Вниз
        addIfPassable(neighbors, maze, row, col - 1); // Влево
        addIfPassable(neighbors, maze, row, col + 1); // Вправо

        return neighbors;
    }

    // Добавляем клетку, если она проходима
    private void addIfPassable(List<Coordinate> list, Maze maze, int row, int col) {
        if (isInBounds(row, col, maze) && maze.grid()[row][col].type() != Cell.Type.WALL) {
            list.add(new Coordinate(row, col));
        }
    }

    // Метод для получения стоимости клетки
    private int getCost(Cell cell) {
        switch (cell.type()) {
            case SAND:
                return sand; // Песок требует больше шагов (условная высокая стоимость)
            case COIN:
                return coin; // Монета делает движение легче (условная низкая стоимость)
            default:
                return 1; // Обычные клетки
        }
    }

    // Проверка, находится ли клетка в пределах лабиринта
    private boolean isInBounds(int row, int col, Maze maze) {
        return row >= 0 && row < maze.height() && col >= 0 && col < maze.width();
    }

    // Класс для узлов в очереди с приоритетом
    private static class Node {
        Coordinate coordinate;
        int cost;

        Node(Coordinate coordinate, int cost) {
            this.coordinate = coordinate;
            this.cost = cost;
        }
    }
}
