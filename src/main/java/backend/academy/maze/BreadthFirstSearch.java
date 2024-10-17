package backend.academy.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BreadthFirstSearch extends AbstractSolver {

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        // Используем общий метод для проверки стартовой и конечной клетки
        isValidStartAndEnd(maze, start, end);

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

            for (Coordinate neighbor : getNeighbors(maze, current)) {
                if (!visited[neighbor.row()][neighbor.col()]) {
                    visited[neighbor.row()][neighbor.col()] = true;
                    queue.add(neighbor);
                    prev.put(neighbor, current);
                }
            }
        }
        return Collections.emptyList(); // Путь не найден
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
}
