package backend.academy.maze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DepthFirstSearchTest {

    private boolean[][] maze;

    @BeforeEach
    public void setUp() {

        // S . # . E
        // . # # . .
        // . . . . #
        // # . # # #
        // . . . . .

        maze = new boolean[][] {
            {false, false, true,  false, false}, // S - start (0, 0), E - end (0, 4)
            {false, true,  true,  false, false},
            {false, false, false, false, true },
            {true,  false, true,  true,  true },
            {false, false, false, false, false}
        };
    }

    @Test
    public void testFindPathExists() {
        DepthFirstSearch dfs = new DepthFirstSearch(maze);
        List<int[]> path = dfs.findPath(0, 0, 0, 4); // Из точки (0,0) в точку (0,4)

        // Проверяем, что путь найден
        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");

        // Проверка первой и последней клетки пути
        assertArrayEquals(new int[]{0, 0}, path.get(0), "Начальная точка должна быть первой в пути");
        assertArrayEquals(new int[]{0, 4}, path.get(path.size() - 1), "Конечная точка должна быть последней в пути");
    }

    @Test
    public void testNoPath() {
        // Строим лабиринт без возможного пути к цели
        boolean[][] noPathMaze = {
            {false, true, true,  true,  false}, // Все пути к конечной точке заблокированы
            {false, true, true,  true,  false},
            {false, false, false, true,  true },
            {true,  true,  true,  true,  true },
            {false, false, false, true,  true }
        };

        DepthFirstSearch dfs = new DepthFirstSearch(noPathMaze);
        List<int[]> path = dfs.findPath(0, 0, 0, 4); // Попытка найти путь

        // Проверка, что путь не найден
        assertNotNull(path, "Путь не должен быть null");
        assertTrue(path.isEmpty(), "Путь должен быть пустым, если нет доступного пути");
    }

    @Test
    public void testStartIsEnd() {
        DepthFirstSearch dfs = new DepthFirstSearch(maze);
        List<int[]> path = dfs.findPath(2, 2, 2, 2); // Старт и конец в одной точке

        // Проверяем, что путь найден и состоит только из одной точки
        assertNotNull(path, "Путь не должен быть null");
        assertEquals(1, path.size(), "Путь должен содержать только одну клетку");
        assertArrayEquals(new int[]{2, 2}, path.get(0), "Единственная клетка в пути должна быть стартовой (конечной) точкой");
    }

    @Test
    public void testPathInComplexMaze() {
        // Более сложный лабиринт с дополнительными проходами
        boolean[][] complexMaze = {
            {false, false, true,  false, false},
            {false, true,  false, false, false},
            {false, false, false, false, true },
            {true,  false, true,  true,  true },
            {false, false, false, false, false}
        };

        DepthFirstSearch dfs = new DepthFirstSearch(complexMaze);
        List<int[]> path = dfs.findPath(0, 0, 4, 4); // Из (0,0) в (4,4)

        // Проверяем, что путь найден
        assertNotNull(path, "Путь не должен быть null");
        assertFalse(path.isEmpty(), "Путь не должен быть пустым");

        // Проверка первой и последней клетки пути
        assertArrayEquals(new int[]{0, 0}, path.get(0), "Начальная точка должна быть первой в пути");
        assertArrayEquals(new int[]{4, 4}, path.get(path.size() - 1), "Конечная точка должна быть последней в пути");
    }
}

