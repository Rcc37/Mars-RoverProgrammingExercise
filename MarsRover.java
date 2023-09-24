import java.util.*;

// Class to initialize the simulation
class Initiate {
    public Initiate() {
        // Initialize the dictionary with starting position, grid size, and obstacle array
        dictionary.put("start", new Object[]{0, 0, 'N'});
        dictionary.put("grid", new Object[]{10, 10});

        // Convert the obstacle array to an array of Object[] before storing it
        List<int[]> obstacleList = Arrays.asList(new int[]{2, 2}, new int[]{3, 5});
        Object[] obstacleArray = obstacleList.toArray();
        dictionary.put("obstacleArray", obstacleArray);
    }

    public final HashMap<String, Object[]> dictionary = new HashMap<>();
}

// Class to represent the Mars Rover
class Rover {
    private int row, col; // Grid size
    private final List<String> error = new ArrayList<>(); // Error messages
    private int x, y; // Rover's position (x, y)
    private char point; // Rover's direction (N, E, S, W)
    private final char[][] grid; // Grid to represent the terrain

    // Constructor
    public Rover(Initiate input) {
        Object[] inputs = input.dictionary.get("start");

        // Initialize grid size and starting position
        row = (int) ((Object[]) input.dictionary.get("grid"))[0];
        col = (int) ((Object[]) input.dictionary.get("grid"))[1];
        x = (int) inputs[0];
        y = (int) inputs[1];
        point = (char) inputs[2];

        // Initialize the grid
        grid = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = '0';
            }
        }

        // Retrieve the obstacle array as an Object[] and cast it to List<int[]>
        Object[] obstacleArrayObject = (Object[]) input.dictionary.get("obstacleArray");
        List<int[]> obstacleArray = Arrays.asList(Arrays.copyOf(obstacleArrayObject, obstacleArrayObject.length, int[][].class));

        // Initialize the obstacle array
        for (int[] obstacle : obstacleArray) {
            grid[obstacle[0] - 1][obstacle[1] - 1] = '1';
        }
        grid[0][0] = 'R';

        // Define the possible directions
        char[] direction = {'N', 'E', 'S', 'W'};
        for (int i = 0; i < 4; i++) {
            if (point == direction[i]) {
                curr_direction = i;
                break;
            }
        }
    }

    private final char[] direction = {'N', 'E', 'S', 'W'};
    private int curr_direction; // Current direction index
    private String movement; // Current movement direction (X or Y)
    private int step; // Positive or negative step size

    // Method for turning right
    public char R() {
        System.out.println("Right");
        curr_direction = (curr_direction + 1) % 4;
        return direction[curr_direction];
    }

    // Method for turning left
    public char L() {
        System.out.println("Left");
        curr_direction = (curr_direction + 3) % 4; // To turn left, subtract 1 from right turn
        return direction[curr_direction];
    }

    // Method to track changes in movement direction and step size
    public void track_change(String[] vals) {
        movement = vals[0];
        step = Integer.parseInt(vals[1]);
    }

    // Method for moving in the X direction
    public void X() {
        System.out.println("X direction move");
        y = (y + step) % row;
        x = x;
    }

    // Method for moving in the Y direction
    public void Y() {
        System.out.println("Y direction move");
        x = (x + step) % col;
        y = y;
    }

    // Method for moving forward
    public void M() {
        System.out.println("Move");

        if (movement != null) {
            if (movement.equals("X")) {
                X();
            } else {
                Y();
            }
        } else {
            // Executed only once
            if (direction[curr_direction] == 'N') {
                Y();
            } else if (direction[curr_direction] == 'E') {
                X();
            } else if (direction[curr_direction] == 'S') {
                Y();
            } else if (direction[curr_direction] == 'W') {
                X();
            }
        }

        // Implement moving the rover
        try {
            if (grid[x][y] == '1') {
                error.add("Found an obstacle at " + x + ", " + y);
                if (movement.equals("X")) {
                    y = (y - 1 + col) % col;
                } else {
                    x = (x - 1 + row) % row;
                }
            } else {
                grid[x][y] = 'R';
            }
        } catch (Exception e) {
            error.add(e.getMessage());
            System.out.println(error);
            System.exit(0);
        }
    }

    // Method to retrieve the final state of the Rover
    public Object[] End() {
        return new Object[]{grid, direction[curr_direction], x, y, error};
    }
}

// Class to represent movement commands
class Move {

    private final Rover rover;

    // Constructor
    public Move(Rover rover) {
        this.rover = rover;
    }

    // Method for moving North
    public void N() {
        System.out.println("Move North");
        rover.track_change(new String[]{"Y", "-1"});
    }

    // Method for moving East
    public void E() {
        System.out.println("Move East");
        rover.track_change(new String[]{"X", "1"});
    }

    // Method for moving South
    public void S() {
        System.out.println("Move South");
        rover.track_change(new String[]{"Y", "1"});
    }

    // Method for moving West
    public void W() {
        System.out.println("Move West");
        rover.track_change(new String[]{"X", "-1"});
    }
}

public class MarsRover {
    public static void main(String[] args) {
        Initiate initiate = new Initiate();
        Rover rover = new Rover(initiate);
        Move move = new Move(rover);

        String[] movements = {"R", "M", "R", "M", "L", "M"};

        for (String movement : movements) {
            if (!movement.equals("M")) {
                if (movement.equals("R")) {
                    rover.R();
                } else if (movement.equals("L")) {
                    rover.L();
                }
                String direction = rover.End()[1].toString();
                if (direction.equals("N")) {
                    move.N();
                } else if (direction.equals("E")) {
                    move.E();
                } else if (direction.equals("S")) {
                    move.S();
                } else if (direction.equals("W")) {
                    move.W();
                }
            } else {
                rover.M();
            }
        }

        Object[] result = rover.End();
        char[][] grid = (char[][]) result[0];
        char facing = (char) result[1];
        int x = (int) result[2];
        int y = (int) result[3];
        List<String> errors = (List<String>) result[4];

        // Display results
        System.out.println("\nOutput\n");
        displayArray(grid);
        System.out.println("\nFacing: " + facing + " at (" + x + ", " + y + ")\n");

        if (!errors.isEmpty()) {
            System.out.println("Logs while movement: ");
            for (String error : errors) {
                System.out.println(error);
            }
        } else {
            System.out.println("Logs while movement: NONE");
        }
    }

    // Method to display the grid
    private static void displayArray(char[][] arr) {
        for (char[] row : arr) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
