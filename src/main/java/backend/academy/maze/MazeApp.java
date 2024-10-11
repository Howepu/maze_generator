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
        Generator generator = new KruskalMazeGenerator(height, width);
        Generator generator1 = new PrimMazeGenerator(height, width);
        Maze maze = generator.generate(height, width);
        Maze maze1 = generator1.generate(height, width);

        // Ввод начальной и конечной точек
        log.info("Введите стартовую точку (x y): ");
        int startX = scanner.nextInt();
        int startY = scanner.nextInt();

        log.info("Введите конечную точку (x y): ");
        int endX = scanner.nextInt();
        int endY = scanner.nextInt();

        // Поиск пути
        Solver solver = new BreadthFirstSearch();
        List<Coordinate> path = solver.solve(maze, new Coordinate(startX, startY), new Coordinate(endX, endY));

        Solver solver1 = new BreadthFirstSearch();
        List<Coordinate> path1 = solver.solve(maze1, new Coordinate(startX, startY), new Coordinate(endX, endY));

        // Отображение лабиринта и пути
        Renderer renderer = new MazeRenderer();
        log.info("Сгенерированный лабиринт 1 по алгоритму Краскаля:\n{}", renderer.render(maze));
        log.info("Сгенерированный лабиринт 2 по алгоритму Прима:\n{}", renderer.render(maze1));

        if (path != null) {
            log.info("Найденный путь в ширину лабиринта 1:\n{}", renderer.render(maze, path));
            log.info("Найденный путь в ширину лабиринта 2:\n{}", renderer.render(maze1, path));
            log.info("Найденный путь в глубину лабиринта 1:\n{}", renderer.render(maze, path1));
            log.info("Найденный путь в глубину лабиринта 2:\n{}", renderer.render(maze1, path1));
        } else {
            log.info("Путь не найден.");
        }
    }

    // Метод для проверки, является ли строка числом
    private static boolean isValidNumber(String str) {
        return str.matches("\\d+"); // Проверка на целое число
    }
}
