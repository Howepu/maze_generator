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
        Scanner scanner = new Scanner(System.in);

        log.info("Введите высоту и ширину лабиринта через пробел:");

        String input = scanner.nextLine().trim(); // Убираем лишние пробелы
        String[] parts = input.split(" ");

        // Проверка на наличие двух элементов и корректность введённых данных
        if (parts.length != 2 || !isValidNumber(parts[0]) || !isValidNumber(parts[1])) {
            log.info("Ошибка ввода: введите два корректных числа через пробел.");
            return; // Завершаем программу, если ввод некорректен
        }

        int height = Integer.parseInt(parts[0].trim());
        int width = Integer.parseInt(parts[1].trim());

        // Генерация лабиринта
        Generator generator1 = new KruskalMazeGenerator(height, width);
        Generator generator2 = new PrimMazeGenerator(height, width);
        Maze maze1 = generator1.generate(height, width);
        Maze maze2 = generator2.generate(height, width);

        // Ввод начальной и конечной точек
        log.info("Введите стартовую точку (x y): ");
        int startX = scanner.nextInt();
        int startY = scanner.nextInt();

        log.info("Введите конечную точку (x y): ");
        int endX = scanner.nextInt();
        int endY = scanner.nextInt();

        // Поиск пути
        Solver bfsSolver = new BreadthFirstSearch();
        List<Coordinate> bfsPath1 = bfsSolver.solve(maze1, new Coordinate(startX, startY), new Coordinate(endX, endY));
        List<Coordinate> bfsPath2 = bfsSolver.solve(maze2, new Coordinate(startX, startY), new Coordinate(endX, endY));

        Solver dfsSolver = new DepthFirstSearch(); // Используйте ваш класс для DFS
        List<Coordinate> dfsPath1 = dfsSolver.solve(maze1, new Coordinate(startX, startY), new Coordinate(endX, endY));
        List<Coordinate> dfsPath2 = dfsSolver.solve(maze2, new Coordinate(startX, startY), new Coordinate(endX, endY));

        // Решение с помощью A*
        Solver aStarSolver = new AStarSolver(); // Создаем экземпляр A*
        List<Coordinate> aStarPath1 = aStarSolver.solve(maze1, new Coordinate(startX, startY), new Coordinate(endX, endY));
        List<Coordinate> aStarPath2 = aStarSolver.solve(maze2, new Coordinate(startX, startY), new Coordinate(endX, endY));

        // Отображение лабиринта и пути
        Renderer renderer = new MazeRenderer();
        log.info("Сгенерированный лабиринт 1 по алгоритму Краскаля:\n{}", renderer.render(maze1));
        log.info("Сгенерированный лабиринт 2 по алгоритму Прима:\n{}", renderer.render(maze2));

        // Проверка найденных путей и вывод результатов
        printSolution(renderer, "BFS", "1", bfsPath1, maze1);
        printSolution(renderer, "DFS", "1", dfsPath1, maze1);
        printSolution(renderer, "A*", "1", aStarPath1, maze1);

        printSolution(renderer, "BFS", "2", bfsPath2, maze2);
        printSolution(renderer, "DFS", "2", dfsPath2, maze2);
        printSolution(renderer, "A*", "2", aStarPath2, maze2);
    }

    // Метод для проверки, является ли строка числом
    private static boolean isValidNumber(String str) {
        return str.matches("\\d+"); // Проверка на целое число
    }

    // Универсальный метод для печати результата
    private static void printSolution(Renderer renderer, String solverType, String mazeNum, List<Coordinate> path, Maze maze) {
        if (path != null && !path.isEmpty()) {
            log.info("Найденный путь {} лабиринта {}:\n{}", solverType, mazeNum, renderer.render(maze, path));
        } else {
            log.info("Путь не найден для лабиринта {} с использованием {}.", mazeNum, solverType);
        }
    }
}
