package backend.academy.maze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BreadthFirstSearchTest {

    private Maze maze;
    private BreadthFirstSearch bfs;

    @BeforeEach
    public void setUp() {
        bfs = new BreadthFirstSearch();

        // S . # . E
        // . # # . .
        // . . . . #
        // # . # # #
        // . . . . .

        Cell[][] grid = new Cell[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL); // Изначально все клетки - стены
            }
        }

        // Определение проходов в лабиринте
        grid[0][0] = new Cell(0, 0, Cell.Type.PASSAGE); // S
        grid[0][1] = new Cell(0, 1, Cell.Type.PASSAGE);
        grid[0][3] = new Cell(0, 3, Cell.Type.PASSAGE);
        grid[0][4] = new Cell(0, 4, Cell.Type.PASSAGE); // E

        grid[1][0] = new Cell(1, 0, Cell.Type.PASSAGE);
        grid[1][3] = new Cell(1, 3, Cell.Type.PASSAGE);
        grid[1][4] = new Cell(1, 4, Cell.Type.PASSAGE);

        grid[2][0] = new Cell(2, 0, Cell.Type.PASSAGE);
        grid[2][1] = new Cell(2, 1, Cell.Type.PASSAGE);
        grid[2][2] = new Cell(2, 2, Cell.Type.PASSAGE);
        grid[2][3] = new Cell(2, 3, Cell.Type.PASSAGE);

        grid[3][1] = new Cell(3, 1, Cell.Type.PASSAGE);

        grid[4][0] = new Cell(4, 0, Cell.Type.PASSAGE);
        grid[4][1] = new Cell(4, 1, Cell.Type.PASSAGE);
        grid[4][2] = new Cell(4, 2, Cell.Type.PASSAGE);
        grid[4][3] = new Cell(4, 3, Cell.Type.PASSAGE);
        grid[4][4] = new Cell(4, 4, Cell.Type.PASSAGE);

        // Создание лабиринта
        maze = new Maze(5, 5, grid);
    }

    @Test
    public void testSolvePathExists() {
        // Стартовая и конечная точки
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(0, 4);

        // Решение лабиринта
        List<Coordinate> path = bfs.solve(maze, start, end);

        // Проверка, что путь найден и содержит правильные координаты
        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");
        assertEquals(start, path.get(0), "Начальная точка должна быть первой в пути");
        assertEquals(end, path.get(path.size() - 1), "Конечная точка должна быть последней в пути");
    }

    @Test
    public void testNoPath() {
        // Создание лабиринта с недостижимой конечной точкой
        Cell[][] grid = maze.grid();
        grid[0][4] = new Cell(0, 4, Cell.Type.WALL); // E - теперь стена
        maze = new Maze(5, 5, grid);

        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(0, 4);

        // Решение лабиринта
        List<Coordinate> path = bfs.solve(maze, start, end);

        // Проверка, что путь не найден (null)
        assertNull(path, "Путь должен быть null, если нет доступного пути");
    }

    @Test
    public void testStartIsEnd() {
        // Если стартовая точка совпадает с конечной
        Coordinate start = new Coordinate(2, 2);

        // Решение лабиринта
        List<Coordinate> path = bfs.solve(maze, start, start);

        // Проверка, что путь найден и содержит только одну точку
        assertNotNull(path, "Путь не должен быть null");
        assertEquals(1, path.size(), "Путь должен содержать только одну точку");
        assertEquals(start, path.get(0), "Единственной точкой в пути должна быть стартовая (конечная) точка");
    }

    @Test
    public void testPathInComplexMaze() {
        // Создаем сложный лабиринт, где BFS должен найти более длинный путь
        Cell[][] grid = maze.grid();
        grid[1][2] = new Cell(1, 2, Cell.Type.PASSAGE); // Добавляем ещё проход
        maze = new Maze(5, 5, grid);

        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(4, 4);

        // Решение лабиринта
        List<Coordinate> path = bfs.solve(maze, start, end);

        // Проверка, что путь найден
        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");
    }
}
