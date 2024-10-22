package backend.academy.maze.generators;


import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalMazeGenerator implements Generator {

    @Override
    public Maze generate(int height, int width) {
        List<Edge> edges = createEdges(height, width);
        Collections.shuffle(edges); // Перемешиваем рёбра
        UnionFind unionFind = new UnionFind(height * width);
        Cell[][] grid = initializeGrid(height, width); // Инициализируем сетку клеток

        // Генерация лабиринта
        for (Edge edge : edges) {
            Coordinate cell1 = new Coordinate(edge.x1(), edge.y1());
            Coordinate cell2 = new Coordinate(edge.x2(), edge.y2());

            if (unionFind.find(cell1.row() * width + cell1.col()) != unionFind
                .find(cell2.row() * width + cell2.col())) {
                unionFind.union(cell1.row() * width + cell1.col(), cell2.row() * width + cell2.col());
                createPassage(edge, grid);
            }
        }

        // Обрамление лабиринта стеной
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (col == 0 || row == 0 || col == width - 1 || row == height - 1) {
                    grid[row][col] = new Cell(new Coordinate(row, col), Cell.Type.WALL);
                }
            }
        }

        return new Maze(height, width, grid);
    }

    private List<Edge> createEdges(int height, int width) {
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
        return edges;
    }

    private Cell[][] initializeGrid(int height, int width) {
        Cell[][] grid = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(new Coordinate(row, col), Cell.Type.WALL); // Изначально все клетки - стены
            }
        }
        return grid;
    }

    private void createPassage(Edge edge, Cell[][] grid) {
        Coordinate passageCoordinate;
        if (edge.x1() == edge.x2()) { // Вертикальное соединение
            int wallY = Math.min(edge.y1(), edge.y2());
            passageCoordinate = new Coordinate(edge.x1(), wallY);
        } else { // Горизонтальное соединение
            int wallX = Math.min(edge.x1(), edge.x2());
            passageCoordinate = new Coordinate(wallX, edge.y1());
        }
        grid[passageCoordinate.row()][passageCoordinate.col()] = new Cell(passageCoordinate, Cell.Type.PASSAGE);
    }
}
