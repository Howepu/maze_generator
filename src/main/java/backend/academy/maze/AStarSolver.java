package backend.academy.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarSolver implements Solver {

    private final int sand = 3;
    private final int coin = 0;


    // Карта для хранения стоимости разных типов клеток
    private final Map<Cell.Type, Integer> movementCost = new HashMap<>();

    public AStarSolver() {
        // Задаем стоимость движения по разным типам клеток
        movementCost.put(Cell.Type.PASSAGE, 1); // Проход
        movementCost.put(Cell.Type.WALL, Integer.MAX_VALUE); // Стены — непроходимы
        movementCost.put(Cell.Type.SAND, sand); // Песок замедляет
        movementCost.put(Cell.Type.COIN, coin); // Монеты ускоряют
    }


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

        // Множество для хранения уже обработанных клеток
        Set<Coordinate> closedSet = new HashSet<>();
        // Очередь с приоритетом для обработки клеток
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.fCost));

        // Мапы для отслеживания стоимости пути от начальной клетки
        Map<Coordinate, Integer> gCost = new HashMap<>();
        // Мапы для отслеживания кратчайшего пути
        Map<Coordinate, Coordinate> cameFrom = new HashMap<>();

        // Начальная точка имеет нулевую стоимость
        gCost.put(start, 0);
        openSet.add(new Node(start, 0, heuristic(start, end)));

        while (!openSet.isEmpty()) {
            // Получаем клетку с наименьшей оценкой fCost
            Node current = openSet.poll();

            // Если достигли конца — восстанавливаем путь
            if (current.coordinate.equals(end)) {
                return reconstructPath(cameFrom, current.coordinate);
            }

            closedSet.add(current.coordinate);

            // Проходим по соседям текущей клетки
            for (Coordinate neighbor : getNeighbors(maze, current.coordinate)) {
                if (closedSet.contains(neighbor)) {
                    continue; // Пропускаем обработанные клетки
                }

                int tentativeGCost = gCost.get(current.coordinate) + movementCost(maze, neighbor);

                if (tentativeGCost < gCost.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    // Обновляем лучший путь до соседа
                    cameFrom.put(neighbor, current.coordinate);
                    gCost.put(neighbor, tentativeGCost);

                    int fCost = tentativeGCost + heuristic(neighbor, end);
                    openSet.add(new Node(neighbor, tentativeGCost, fCost));
                }
            }
        }

        // Если путь не найден, возвращаем пустой список
        return Collections.emptyList();
    }

    // Функция эвристики (Манхэттенское расстояние)
    private int heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }

    // Восстановление пути из cameFrom
    private List<Coordinate> reconstructPath(Map<Coordinate, Coordinate> cameFrom, Coordinate current) {
        List<Coordinate> path = new ArrayList<>();
        path.add(current);

        // Используем локальную переменную для изменения
        Coordinate temp = current;

        while (cameFrom.containsKey(temp)) {
            temp = cameFrom.get(temp);
            path.add(temp);
        }
        Collections.reverse(path); // Переворачиваем путь
        return path;
    }

    // Получение соседних клеток (с проверкой, чтобы они не были стенами)
    private List<Coordinate> getNeighbors(Maze maze, Coordinate coord) {
        List<Coordinate> neighbors = new ArrayList<>();
        int row = coord.row();
        int col = coord.col();

        if (row > 0 && maze.grid()[row - 1][col].type() != Cell.Type.WALL) {
            neighbors.add(new Coordinate(row - 1, col));
        }
        if (row < maze.height() - 1 && maze.grid()[row + 1][col].type() != Cell.Type.WALL) {
            neighbors.add(new Coordinate(row + 1, col));
        }
        if (col > 0 && maze.grid()[row][col - 1].type() != Cell.Type.WALL) {
            neighbors.add(new Coordinate(row, col - 1));
        }
        if (col < maze.width() - 1 && maze.grid()[row][col + 1].type() != Cell.Type.WALL) {
            neighbors.add(new Coordinate(row, col + 1));
        }

        return neighbors;
    }

    // Получение стоимости передвижения по клетке
    private int movementCost(Maze maze, Coordinate coord) {
        return movementCost.getOrDefault(maze.grid()[coord.row()][coord.col()].type(), 1);
    }

    // Вспомогательный класс для работы с приоритетной очередью
    private static class Node {
        Coordinate coordinate;
        int gCost; // Стоимость пути от начала
        int fCost; // Полная оценка (gCost + эвристика)

        Node(Coordinate coordinate, int gCost, int fCost) {
            this.coordinate = coordinate;
            this.gCost = gCost;
            this.fCost = fCost;
        }
    }
}
