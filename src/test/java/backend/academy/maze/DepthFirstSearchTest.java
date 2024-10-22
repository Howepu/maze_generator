package backend.academy.maze;

import backend.academy.maze.solvers.DepthFirstSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DepthFirstSearchTest {

    private Maze maze;
    private DepthFirstSearch dfs;

    @BeforeEach
    public void setUp() {
        dfs = new DepthFirstSearch();

        // Создаем лабиринт 5x5 с учетом стен, проходов, песка и монет
        Cell[][] grid = new Cell[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                grid[row][col] = new Cell(new Coordinate(row, col), Cell.Type.WALL); // Изначально все клетки - стены
            }
        }

        // Определяем проходы и добавляем песок и монеты
        grid[0][0] = new Cell(new Coordinate(0, 0), Cell.Type.PASSAGE); // S
        grid[0][1] = new Cell(new Coordinate(0, 1), Cell.Type.SAND); // Песок
        grid[0][3] = new Cell(new Coordinate(0, 3), Cell.Type.PASSAGE);
        grid[0][4] = new Cell(new Coordinate(0, 4), Cell.Type.PASSAGE); // E

        grid[1][0] = new Cell(new Coordinate(1, 0), Cell.Type.PASSAGE);
        grid[1][2] = new Cell(new Coordinate(1, 2), Cell.Type.PASSAGE); // Добавляем проход
        grid[1][3] = new Cell(new Coordinate(1, 3), Cell.Type.PASSAGE);
        grid[1][4] = new Cell(new Coordinate(1, 4), Cell.Type.COIN); // Монеты

        grid[2][0] = new Cell(new Coordinate(2, 0), Cell.Type.PASSAGE);
        grid[2][1] = new Cell(new Coordinate(2, 1), Cell.Type.PASSAGE);
        grid[2][2] = new Cell(new Coordinate(2, 2), Cell.Type.PASSAGE);
        grid[2][3] = new Cell(new Coordinate(2, 3), Cell.Type.PASSAGE);

        grid[3][1] = new Cell(new Coordinate(3, 1), Cell.Type.PASSAGE);

        grid[4][0] = new Cell(new Coordinate(4, 0), Cell.Type.PASSAGE);
        grid[4][1] = new Cell(new Coordinate(4, 1), Cell.Type.PASSAGE);
        grid[4][2] = new Cell(new Coordinate(4, 2), Cell.Type.PASSAGE);
        grid[4][3] = new Cell(new Coordinate(4, 3), Cell.Type.PASSAGE);
        grid[4][4] = new Cell(new Coordinate(4, 4), Cell.Type.PASSAGE);

        // Создание лабиринта
        maze = new Maze(5, 5, grid);
    }

    @Test
    public void testSolvePathExists() {
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(0, 4);

        List<Coordinate> path = dfs.solve(maze, start, end);

        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");
        assertEquals(start, path.get(0), "Начальная точка должна быть первой в пути");
        assertEquals(end, path.get(path.size() - 1), "Конечная точка должна быть последней в пути");
    }

    @Test
    public void testStartIsEnd() {
        Coordinate start = new Coordinate(2, 2);

        List<Coordinate> path = dfs.solve(maze, start, start);

        assertNotNull(path, "Путь не должен быть null");
        assertEquals(1, path.size(), "Путь должен содержать только одну точку");
        assertEquals(start, path.get(0), "Единственной точкой в пути должна быть стартовая (конечная) точка");
    }

    @Test
    public void testPathInComplexMaze() {
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(4, 4);

        List<Coordinate> path = dfs.solve(maze, start, end);

        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");
    }

    @Test
    public void testPathWithSand() {
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(0, 1);

        List<Coordinate> path = dfs.solve(maze, start, end);

        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");
        assertTrue(path.contains(new Coordinate(0, 1)), "Путь должен содержать клетку с песком");
    }

    @Test
    public void testPathWithCoin() {
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(1, 4);

        List<Coordinate> path = dfs.solve(maze, start, end);

        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");
        assertTrue(path.contains(new Coordinate(1, 4)), "Путь должен содержать клетку с монетами");
    }
}
