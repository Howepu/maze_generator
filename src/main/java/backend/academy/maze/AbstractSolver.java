package backend.academy.maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractSolver implements Solver {

    protected final int sand = 3;
    protected final int coin = 0;

    // Карта для хранения стоимости разных типов клеток
    protected final Map<Cell.Type, Integer> movementCost = new HashMap<>();

    public AbstractSolver() {
        // Задаем стоимость движения по разным типам клеток
        movementCost.put(Cell.Type.PASSAGE, 1); // Проход
        movementCost.put(Cell.Type.WALL, Integer.MAX_VALUE); // Стены — непроходимы
        movementCost.put(Cell.Type.SAND, sand); // Песок замедляет
        movementCost.put(Cell.Type.COIN, coin); // Монеты ускоряют
    }

    // Проверка и изменение стартовой и конечной клетки
    protected void isValidStartAndEnd(Maze maze, Coordinate start, Coordinate end) {
        if (maze.grid()[start.row()][start.col()].type() == Cell.Type.WALL) {
            maze.grid()[start.row()][start.col()] = new Cell(start.row(), start.col(), Cell.Type.PASSAGE);
        }
        if (maze.grid()[end.row()][end.col()].type() == Cell.Type.WALL) {
            maze.grid()[end.row()][end.col()] = new Cell(end.row(), end.col(), Cell.Type.PASSAGE);
        }
    }

    // Получение стоимости передвижения по клетке
    protected int movementCost(Maze maze, Coordinate coord) {
        return movementCost.getOrDefault(maze.grid()[coord.row()][coord.col()].type(), 1);
    }

    // Получение соседних клеток (с проверкой, чтобы они не были стенами)
    protected List<Coordinate> getNeighbors(Maze maze, Coordinate coord) {
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
}
