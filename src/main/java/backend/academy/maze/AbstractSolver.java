package backend.academy.maze;

public class AbstractSolver {

    private void abstractSolver() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    protected static void isValidStartAndEnd(Maze maze, Coordinate start, Coordinate end) {
        if (maze.grid()[start.row()][start.col()].type() == Cell.Type.WALL) {
            maze.grid()[start.row()][start.col()] = new Cell(start.row(), start.col(), Cell.Type.PASSAGE);
        }
        if (maze.grid()[end.row()][end.col()].type() == Cell.Type.WALL) {
            maze.grid()[end.row()][end.col()] = new Cell(end.row(), end.col(), Cell.Type.PASSAGE);
        }
    }
}
