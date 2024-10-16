package backend.academy.maze;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MazeRenderer implements Renderer {

    @Override
    public String render(Maze maze) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maze.height(); i++) {
            for (int j = 0; j < maze.width(); j++) {
                switch (maze.grid()[i][j].type()) {
                    case WALL -> sb.append("#");       // Стены
                    case PASSAGE -> sb.append(" ");    // Проходы
                    case SAND -> sb.append("~");       // Песок
                    case COIN -> sb.append("O");       // Монета
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        StringBuilder sb = new StringBuilder();
        Set<Coordinate> pathSet = new HashSet<>();

        // Проверка на null перед инициализацией pathSet
        if (path != null) {
            pathSet = new HashSet<>(path);
        }

        for (int i = 0; i < maze.height(); i++) {
            for (int j = 0; j < maze.width(); j++) {
                Coordinate coord = new Coordinate(i, j);
                if (pathSet.contains(coord)) {
                    if (coord.equals(path != null ? path.get(0) : null)) {
                        sb.append("A"); // Стартовая точка
                    } else if (coord.equals(path != null ? path.get(path.size() - 1) : null)) {
                        sb.append("B"); // Конечная точка
                    } else {
                        sb.append("*"); // Путь
                    }
                } else {
                    // Отображение в зависимости от типа клетки
                    switch (maze.grid()[i][j].type()) {
                        case WALL -> sb.append("#");      // Стены
                        case PASSAGE -> sb.append(" ");   // Проходы
                        case SAND -> sb.append("~");      // Песок
                        case COIN -> sb.append("O");      // Монета
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
