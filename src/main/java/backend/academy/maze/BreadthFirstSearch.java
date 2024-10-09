package backend.academy.maze;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BreadthFirstSearch implements Solver {

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        Deque<Coordinate> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[maze.height()][maze.width()];
        Map<Coordinate, Coordinate> prev = new HashMap<>();
        queue.add(start);
        visited[start.row()][start.col()] = true;

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            if (current.equals(end)) {
                return buildPath(prev, current);
            }
            for (Coordinate closest : getClosest(current, maze)) {
                if (!visited[closest.row()][closest.col()]) {
                    visited[closest.row()][closest.col()] = true;
                    queue.add(closest);
                    prev.put(closest, current);
                }
            }
        }
        return null;
    }

    private List<Coordinate> buildPath(Map<Coordinate, Coordinate> prev, Coordinate end) {
        List<Coordinate> path = new ArrayList<>();
        for (Coordinate at = end; at != null; at = prev.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    private List<Coordinate> getClosest(Coordinate point, Maze maze) {
        List<Coordinate> closest = new ArrayList<>();
        int row = point.row();
        int col = point.col();

        if (row > 0 && maze.grid()[row - 1][col].type() == Cell.Type.PASSAGE) {
            closest.add(new Coordinate(row - 1, col));
        }
        if (row < maze.height() - 1 && maze.grid()[row + 1][col].type() == Cell.Type.PASSAGE) {
            closest.add(new Coordinate(row + 1, col));
        }
        if (col > 0 && maze.grid()[row][col - 1].type() == Cell.Type.PASSAGE) {
            closest.add(new Coordinate(row, col - 1));
        }
        if (col < maze.width() - 1 && maze.grid()[row][col + 1].type() == Cell.Type.PASSAGE) {
            closest.add(new Coordinate(row, col + 1));
        }

        return closest;
    }
}
