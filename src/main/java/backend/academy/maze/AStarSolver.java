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

public class AStarSolver extends AbstractSolver {

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {

        // Используем общий метод для проверки стартовой и конечной клетки
        isValidStartAndEnd(maze, start, end);

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
