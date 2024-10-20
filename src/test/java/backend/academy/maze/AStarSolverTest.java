package backend.academy.maze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AStarSolverTest {

    private Maze maze;
    private AStarSolver aStarSolver;

    @BeforeEach
    public void setUp() {
        aStarSolver = new AStarSolver();

        // Создание лабиринта с песком и монетами
        // S . S . .
        // . # # . .
        // . . . . #
        // # S # # #
        // E C . . .

        Cell[][] grid = new Cell[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL); // Изначально все клетки - стены
            }
        }

        // Определение проходов в лабиринте
        grid[0][0] = new Cell(0, 0, Cell.Type.PASSAGE); // start
        grid[0][1] = new Cell(0, 1, Cell.Type.SAND);
        grid[0][3] = new Cell(0, 3, Cell.Type.PASSAGE);
        grid[0][4] = new Cell(0, 4, Cell.Type.PASSAGE);

        grid[1][0] = new Cell(1, 0, Cell.Type.PASSAGE);
        grid[1][3] = new Cell(1, 3, Cell.Type.PASSAGE);
        grid[1][4] = new Cell(1, 4, Cell.Type.PASSAGE);

        grid[2][0] = new Cell(2, 0, Cell.Type.PASSAGE);
        grid[2][1] = new Cell(2, 1, Cell.Type.PASSAGE);
        grid[2][2] = new Cell(2, 2, Cell.Type.PASSAGE);
        grid[2][3] = new Cell(2, 3, Cell.Type.PASSAGE);

        grid[3][1] = new Cell(3, 1, Cell.Type.SAND);
        grid[4][0] = new Cell(4, 0, Cell.Type.PASSAGE); // end
        grid[4][1] = new Cell(4, 1, Cell.Type.COIN);
        grid[4][2] = new Cell(4, 2, Cell.Type.PASSAGE);
        grid[4][3] = new Cell(4, 3, Cell.Type.PASSAGE);
        grid[4][4] = new Cell(4, 4, Cell.Type.PASSAGE);

        // Создание лабиринта
        maze = new Maze(5, 5, grid);
    }

    @Test
    public void testSolvePathExistsWithCoinAndSand() {
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(4, 0);

        List<Coordinate> path = aStarSolver.solve(maze, start, end);

        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");
        assertEquals(start, path.get(0), "Начальная точка должна быть первой в пути");
        assertEquals(end, path.get(path.size() - 1), "Конечная точка должна быть последней в пути");

        // Проверяем, что путь содержит монету
        boolean hasCoin = path.stream().anyMatch(coord -> maze.grid()[coord.row()][coord.col()].type() == Cell.Type.COIN);

        assertTrue(hasCoin, "Путь должен содержать клетку с монетой");
        boolean hasSand = path.stream().anyMatch(coord -> maze.grid()[coord.row()][coord.col()].type() == Cell.Type.SAND);

        assertTrue(hasSand, "Путь должен содержать клетку с песком");
    }


    @Test
    public void testStartIsEnd() {
        Coordinate start = new Coordinate(2, 2);

        List<Coordinate> path = aStarSolver.solve(maze, start, start);

        assertNotNull(path, "Путь не должен быть null");
        assertEquals(1, path.size(), "Путь должен содержать только одну точку");
        assertEquals(start, path.get(0), "Единственной точкой в пути должна быть стартовая (конечная) точка");
    }

    @Test
    public void testPathInComplexMaze() {
        Cell[][] grid = maze.grid();
        grid[1][2] = new Cell(1, 2, Cell.Type.PASSAGE); // Добавляем проход
        maze = new Maze(5, 5, grid);

        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(4, 4);

        List<Coordinate> path = aStarSolver.solve(maze, start, end);

        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");
    }
}
