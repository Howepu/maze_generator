package backend.academy.maze;

import backend.academy.maze.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrimMazeGeneratorTest {

    private Maze maze;
    private int height;
    private int width;

    @BeforeEach
    public void setUp() {
        height = 5;
        width = 5;
        PrimMazeGenerator generator = new PrimMazeGenerator(height, width);
        maze = generator.generate(height, width);
    }

    @Test
    public void testMazeDimensions() {
        assertEquals(height, maze.height(), "Высота лабиринта должна совпадать");
        assertEquals(width, maze.width(), "Ширина лабиринта должна совпадать");
    }

    @Test
    public void testMazeGridNotNull() {
        Cell[][] grid = maze.grid();

        // Проверяем, что все клетки лабиринта не равны null
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                assertNotNull(grid[row][col], "Клетка не должна быть null");
            }
        }
    }

    @Test
    public void testMazePerimeterWalls() {
        Cell[][] grid = maze.grid();

        // Проверяем стены по периметру лабиринта, кроме стартовой точки
        for (int row = 0; row < height; row++) {
            if (grid[row][0].type() != Cell.Type.PASSAGE) {
                assertEquals(Cell.Type.WALL, grid[row][0].type(), "Левая стена должна быть стеной");
            }
            if (grid[row][width - 1].type() != Cell.Type.PASSAGE) {
                assertEquals(Cell.Type.WALL, grid[row][width - 1].type(), "Правая стена должна быть стеной");
            }
        }
        for (int col = 0; col < width; col++) {
            if (grid[0][col].type() != Cell.Type.PASSAGE) {
                assertEquals(Cell.Type.WALL, grid[0][col].type(), "Верхняя стена должна быть стеной");
            }
            if (grid[height - 1][col].type() != Cell.Type.PASSAGE) {
                assertEquals(Cell.Type.WALL, grid[height - 1][col].type(), "Нижняя стена должна быть стеной");
            }
        }
    }


    @Test
    public void testMazeHasPassages() {
        Cell[][] grid = maze.grid();
        boolean hasPassages = false;

        // Проверяем, что хотя бы одна клетка внутри лабиринта является проходом
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                if (grid[row][col].type() == Cell.Type.PASSAGE) {
                    hasPassages = true;
                    break;
                }
            }
        }
        assertTrue(hasPassages, "Лабиринт должен содержать хотя бы один проход");
    }

    @Test
    public void testRandomStartPointIsPassage() {
        Cell[][] grid = maze.grid();
        boolean foundStart = false;

        // Проверяем, что случайная стартовая клетка в лабиринте является проходом
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid[row][col].type() == Cell.Type.PASSAGE) {
                    foundStart = true;
                    break;
                }
            }
        }
        assertTrue(foundStart, "Стартовая клетка должна быть проходом");
    }
}
