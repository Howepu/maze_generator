package backend.academy.maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimMazeGenerator implements Generator {

    // Высота и ширина лабиринта
    private final int height;
    private final int width;
    // Генератор случайных чисел
    private final Random random = new Random();

    // Конструктор для инициализации высоты и ширины лабиринта
    public PrimMazeGenerator(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public Maze generate(int height, int width) {
        // Создание двумерного массива клеток для представления лабиринта
        Cell[][] grid = new Cell[height][width];

        // Инициализация всех клеток как стен
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL);
            }
        }

        // Список стен, которые будут добавлены в лабиринт
        List<Edge> walls = new ArrayList<>();
        // Выбор случайной стартовой точки в лабиринте
        int startX = random.nextInt(height - 2) + 1;
        int startY = random.nextInt(width - 2) + 1;
        // Установка стартовой клетки как прохода
        grid[startX][startY] = new Cell(startX, startY, Cell.Type.PASSAGE);

        // Добавление стен вокруг стартовой клетки
        addWalls(startX, startY, walls, grid);

        // Генерация лабиринта, пока есть стены в списке
        while (!walls.isEmpty()) {
            // Выбор случайной стены из списка
            Edge wall = walls.remove(random.nextInt(walls.size()));
            int x = wall.x2(); // Координата x второй клетки стены
            int y = wall.y2(); // Координата y второй клетки стены

            // Если вторая клетка является стеной и имеет только одного соседа-прохода
            if (grid[x][y].type() == Cell.Type.WALL && hasSinglePassageNeighbor(grid, wall)) {
                // Превращаем стену в проход
                grid[x][y] = new Cell(x, y, Cell.Type.PASSAGE);
                // Добавляем стены вокруг новой проходной клетки
                addWalls(x, y, walls, grid);
            }
        }

        // Обрамление лабиринта стеной
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (col == 0 || row == 0 || col == width - 1 || row == height - 1) {
                    grid[row][col] = new Cell(row, col, Cell.Type.WALL);
                }
            }
        }

        // Присваиваем редкие типы клеткам-проходам
        assignRareTiles(grid);

        // Возвращаем сгенерированный лабиринт
        return new Maze(height, width, grid);
    }

    // Проверка, имеет ли клетка только одного соседа-прохода
    private boolean hasSinglePassageNeighbor(Cell[][] grid, Edge wall) {
        int passages = 0; // Счетчик проходов
        int x = wall.x2(); // Координата x второй клетки стены
        int y = wall.y2(); // Координата y второй клетки стены

        // Проверка соседей на наличие проходов
        // Сосед сверху
        if (x > 0 && grid[x - 1][y].type() == Cell.Type.PASSAGE) {
            passages++; // Сосед сверху
        }
        // Сосед снизу
        if (x < grid.length - 1 && grid[x + 1][y].type() == Cell.Type.PASSAGE) {
            passages++;
        }
        // Сосед слева
        if (y > 0 && grid[x][y - 1].type() == Cell.Type.PASSAGE) {
            passages++;
        }
        // Сосед справа
        if (y < grid[0].length - 1 && grid[x][y + 1].type() == Cell.Type.PASSAGE) {
            passages++;
        }

        // Возвращаем true, если есть ровно один проход
        return passages == 1;
    }

    // Добавление стен вокруг заданной клетки в список стен
    private void addWalls(int x, int y, List<Edge> walls, Cell[][] grid) {
        // Добавление стены сверху
        if (x > 0) {
            walls.add(new Edge(x, y, x - 1, y));
        }
        // Добавление стены снизу
        if (x < grid.length - 1) {
            walls.add(new Edge(x, y, x + 1, y));
        }
        // Добавление стены слева
        if (y > 0) {
            walls.add(new Edge(x, y, x, y - 1));
        }
        // Добавление стены справа
        if (y < grid[0].length - 1) {
            walls.add(new Edge(x, y, x, y + 1));
        }
    }

    // Присваиваем редкие типы клеткам-проходам
    private void assignRareTiles(Cell[][] grid) {
        // Вероятности появления SAND и COIN
        double sandProbability = 0.05; // 5% вероятность для песка
        double coinProbability = 0.02; // 2% вероятность для монет

        for (int row = 1; row < grid.length - 1; row++) {
            for (int col = 1; col < grid[0].length - 1; col++) {
                if (grid[row][col].type() == Cell.Type.PASSAGE) {
                    double rand = random.nextDouble();
                    if (rand < coinProbability) {
                        grid[row][col] = new Cell(row, col, Cell.Type.COIN);
                    } else if (rand < sandProbability + coinProbability) {
                        grid[row][col] = new Cell(row, col, Cell.Type.SAND);
                    }
                }
            }
        }
    }
}
