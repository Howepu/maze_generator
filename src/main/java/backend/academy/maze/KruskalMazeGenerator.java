package backend.academy.maze;

import java.util.*;

public class KruskalMazeGenerator implements Generator {

    private final int height;
    private final int width;

    public KruskalMazeGenerator(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public Maze generate(int height, int width) {
        List<Edge> edges = new ArrayList<>();

        // Создаем рёбра между клетками
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (x < height - 1) {
                    edges.add(new Edge(x, y, x + 1, y)); // вертикальное ребро
                }
                if (y < width - 1) {
                    edges.add(new Edge(x, y, x, y + 1)); // горизонтальное ребро
                }
            }
        }

        Collections.shuffle(edges); // Перемешиваем рёбра
        UnionFind unionFind = new UnionFind(height * width);

        // Инициализируем сетку клеток
        Cell[][] grid = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL); // Изначально все клетки - стены
            }
        }

        // Генерация лабиринта
        for (Edge edge : edges) {
            int cell1 = edge.x1() * width + edge.y1();
            int cell2 = edge.x2() * width + edge.y2();

            if (unionFind.find(cell1) != unionFind.find(cell2)) {
                unionFind.union(cell1, cell2);
                if (edge.x1() == edge.x2()) { // Вертикальное соединение
                    int wallY = Math.min(edge.y1(), edge.y2());
                    grid[edge.x1()][wallY] = new Cell(edge.x1(), wallY, Cell.Type.PASSAGE);
                } else { // Горизонтальное соединение
                    int wallX = Math.min(edge.x1(), edge.x2());
                    grid[wallX][edge.y1()] = new Cell(wallX, edge.y1(), Cell.Type.PASSAGE);
                }
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

        return new Maze(height, width, grid);
    }
}
