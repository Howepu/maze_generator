package backend.academy.maze;

import java.util.List;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("uncommentedmain")
public class MazeApp {

    private MazeApp() {
        throw new AssertionError("Не удается создать экземпляр служебного класса");
    }

    public static void main(String[] args) {
        boolean validInput = true;

        Scanner scanner = new Scanner(System.in);

        log.info("Введите высоту и ширину лабиринта через пробел:");
        String input = scanner.nextLine().trim();

        // Проверяем ввод размеров лабиринта
        int[] dimensions = validateInput(input);
        if (dimensions == null) {
            return; // Завершаем программу при неверном вводе размеров
        }

        int height = dimensions[0];
        int width = dimensions[1];

        // Генерация лабиринта
        Generator generator1 = new KruskalMazeGenerator(height, width);
        Generator generator2 = new PrimMazeGenerator(height, width);
        Maze maze1 = generator1.generate(height, width);
        Maze maze2 = generator2.generate(height, width);

        // Ввод начальной точки с проверкой
        log.info("Введите стартовую точку (x y): ");
        String startInput = scanner.nextLine().trim();
        Coordinate start = validateCoordinate(startInput, height, width);
        if (start == null) {
            return; // Завершаем программу при неверном вводе стартовой точки
        }

        // Ввод конечной точки с проверкой
        log.info("Введите конечную точку (x y): ");
        String endInput = scanner.nextLine().trim();
        Coordinate end = validateCoordinate(endInput, height, width);
        if (end == null) {
            return; // Завершаем программу при неверном вводе конечной точки
        }

        // Проверка на совпадение стартовой и конечной точки
        if (!validateStartAndEnd(start, end)) {
            return; // Завершаем программу при совпадении стартовой и конечной точки
        }

        // Поиск пути
        Solver bfsSolver = new BreadthFirstSearch();
        List<Coordinate> bfsPath1 = bfsSolver.solve(maze1, start, end);
        List<Coordinate> bfsPath2 = bfsSolver.solve(maze2, start, end);

        Solver dfsSolver = new DepthFirstSearch();
        List<Coordinate> dfsPath1 = dfsSolver.solve(maze1, start, end);
        List<Coordinate> dfsPath2 = dfsSolver.solve(maze2, start, end);

        // Решение с помощью A*
        Solver aStarSolver = new AStarSolver();
        List<Coordinate> aStarPath1 = aStarSolver.solve(maze1, start, end);
        List<Coordinate> aStarPath2 = aStarSolver.solve(maze2, start, end);

        // Отображение лабиринта и пути
        Renderer renderer = new MazeRenderer();
        log.info("Сгенерированный лабиринт 1 по алгоритму Краскаля:\n{}", renderer.render(maze1));
        log.info("Сгенерированный лабиринт 2 по алгоритму Прима:\n{}", renderer.render(maze2));

        // Проверка найденных путей и вывод результатов
        printSolution(renderer, "в ширину", "1", bfsPath1, maze1);
        printSolution(renderer, "в глубину", "1", dfsPath1, maze1);
        printSolution(renderer, "A*", "1", aStarPath1, maze1);

        printSolution(renderer, "BFS", "2", bfsPath2, maze2);
        printSolution(renderer, "DFS", "2", dfsPath2, maze2);
        printSolution(renderer, "A*", "2", aStarPath2, maze2);
    }

    private static boolean isValidNumber(String str) {
        return str.matches("\\d+"); // Проверка на целое число
    }

    private static int[] validateInput(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 2 || !isValidNumber(parts[0]) || !isValidNumber(parts[1])) {
            log.info("Ошибка ввода: введите два корректных числа через пробел.");
            return null;
        }
        int height = Integer.parseInt(parts[0].trim());
        int width = Integer.parseInt(parts[1].trim());
        return new int[]{height, width};
    }

    private static Coordinate validateCoordinate(String input, int maxHeight, int maxWidth) {
        String[] parts = input.split(" ");
        if (parts.length != 2 || !isValidNumber(parts[0]) || !isValidNumber(parts[1])) {
            log.info("Ошибка ввода: координаты должны быть числами.");
            return null;
        }
        int row = Integer.parseInt(parts[0].trim());
        int col = Integer.parseInt(parts[1].trim());
        if (row < 0 || row >= maxHeight || col < 0 || col >= maxWidth) {
            log.info("Ошибка: координаты выходят за пределы лабиринта.");
            return null;
        }
        return new Coordinate(row, col);
    }

    private static boolean validateStartAndEnd(Coordinate start, Coordinate end) {
        if (start.equals(end)) {
            log.info("Ошибка: стартовая и конечная точки не должны совпадать.");
            return false;
        }
        return true;
    }

    private static void printSolution(Renderer renderer,
        String solverType, String mazeNum, List<Coordinate> path, Maze maze) {
        if (path != null && !path.isEmpty()) {
            log.info("Найденный путь {} лабиринта {}:\n{}", solverType, mazeNum, renderer.render(maze, path));
        } else {
            log.info("Путь не найден для лабиринта {} с использованием {}.", mazeNum, solverType);
        }
    }
}
