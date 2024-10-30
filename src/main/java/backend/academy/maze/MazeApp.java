package backend.academy.maze;

import backend.academy.maze.generators.Generator;
import backend.academy.maze.generators.KruskalMazeGenerator;
import backend.academy.maze.generators.PrimMazeGenerator;
import backend.academy.maze.renderers.MazeRenderer;
import backend.academy.maze.renderers.Renderer;
import backend.academy.maze.solvers.AStarSolver;
import backend.academy.maze.solvers.BreadthFirstSearch;
import backend.academy.maze.solvers.DepthFirstSearch;
import backend.academy.maze.solvers.Solver;
import java.util.List;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("uncommentedmain")
public class MazeApp {

    private MazeApp() {
        throw new AssertionError("Не удается создать экземпляр служебного класса!");
    }

    public static void main(String[] args) {
        boolean validInput = true; // Флаг для проверки корректности ввода

        Scanner scanner = new Scanner(System.in);

        log.info("Введите высоту и ширину лабиринта через пробел:");
        String input = scanner.nextLine().trim(); // Убираем лишние пробелы

        // Проверяем ввод с помощью отдельного метода
        int[] dimensions = validateInput(input);
        if (dimensions == null) {
            validInput = false;
        }

        Coordinate start = null;
        Coordinate end = null;

        if (validInput) {
            int height = dimensions[0];
            int width = dimensions[1];

            // Генерация лабиринта
            Generator generator1 = new KruskalMazeGenerator();
            Generator generator2 = new PrimMazeGenerator(height, width);
            Maze maze1 = generator1.generate(height, width);
            Maze maze2 = generator2.generate(height, width);

            // Ввод начальной и конечной точек с проверкой на то, что это числа
            log.info("Введите стартовую точку (x y): ");
            String startInput = scanner.nextLine().trim();
            start = validateCoordinate(startInput, height, width);
            if (start == null) {
                validInput = false;
            }

            log.info("Введите конечную точку (x y): ");
            String endInput = scanner.nextLine().trim();
            end = validateCoordinate(endInput, height, width);
            if (end == null) {
                validInput = false;
            }

            // Проверка на одинаковость стартовой и конечной точки
            if (validInput && !validateStartAndEnd(start, end)) {
                validInput = false;
            }

            if (validInput) {
                // Поиск пути
                Solver bfsSolver = new BreadthFirstSearch();
                List<Coordinate> bfsPath1 = bfsSolver.solve(maze1, start, end);
                List<Coordinate> bfsPath2 = bfsSolver.solve(maze2, start, end);

                Solver dfsSolver = new DepthFirstSearch(); // Используйте ваш класс для DFS
                List<Coordinate> dfsPath1 = dfsSolver.solve(maze1, start, end);
                List<Coordinate> dfsPath2 = dfsSolver.solve(maze2, start, end);

                // Решение с помощью A*
                Solver aStarSolver = new AStarSolver(); // Создаем экземпляр A*
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
        }

        if (!validInput) {
            log.info("Программа завершена из-за некорректного ввода.");
        }
    }

    // Метод для проверки, является ли строка числом
    private static boolean isValidNumber(String str) {
        return str.matches("\\d+"); // Проверка на целое число
    }

    // Метод для проверки ввода высоты и ширины лабиринта
    private static int[] validateInput(String input) {
        String[] parts = input.split(" ");

        if (parts.length != 2 || !isValidNumber(parts[0]) || !isValidNumber(parts[1])) {
            log.info("Ошибка ввода: введите два корректных числа через пробел.");
            return null; // Возвращаем null, если ввод некорректен
        }

        int height = Integer.parseInt(parts[0].trim());
        int width = Integer.parseInt(parts[1].trim());

        return new int[] {height, width}; // Возвращаем массив с высотой и шириной
    }

    // Метод для проверки корректности ввода координат
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

    // Метод для проверки совпадения стартовой и конечной точки
    private static boolean validateStartAndEnd(Coordinate start, Coordinate end) {
        if (start.equals(end)) {
            log.info("Ошибка: стартовая и конечная точки не должны совпадать.");
            return false;
        }
        return true;
    }

    // Универсальный метод для печати результата
    private static void printSolution(
        Renderer renderer, String solverType, String mazeNum, List<Coordinate> path, Maze maze
    ) {
        if (path != null && !path.isEmpty()) {
            log.info("Найденный путь {} лабиринта {}:\n{}", solverType, mazeNum, renderer.render(maze, path));
        } else {
            log.info("Путь не найден для лабиринта {} с использованием {}.", mazeNum, solverType);
        }
    }
}
