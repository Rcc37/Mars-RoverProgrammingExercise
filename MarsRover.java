import java.util.ArrayList;
import java.util.List;

class Initiate {
    int[] start = {0, 0, 'N'};
    int[] grid = {10, 10};
    List<int[]> obstacleArray = new ArrayList<>();

    public Initiate() {
        obstacleArray.add(new int[]{2, 2});
        obstacleArray.add(new int[]{3, 5});
    }
}

class Rover {
    //Initiating inputs or starting inputs
    int row, col;
    List<String> error = new ArrayList<>();
    int x, y;
    char[][] Grid;
    char[] direction = {'N', 'E', 'S', 'W'};
    int curr_direction;
    String movement;
    int step;

    public Rover(Initiate input) {
        int[] inputs = input.start;
        row = input.grid[0];
        col = input.grid[1];
        x = inputs[0];
        y = inputs[1];
        Grid = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Grid[i][j] = '0';
            }
        }
        for (int[] obstacle : input.obstacleArray) {
            Grid[obstacle[0] - 1][obstacle[1] - 1] = '1';
        }
        //Navigation of the rover 
        Grid[0][0] = 'R';
        direction = new char[]{'N', 'E', 'S', 'W'};
        curr_direction = new String(direction).indexOf((char) inputs[2]);
    }

    public void R() {
        System.out.println("Right");
        curr_direction = (curr_direction + 1) % 4;
    }

    public void L() {
        System.out.println("Left");
        curr_direction = (curr_direction + 3) % 4;
    }

    public void track_change(String vals) {
        String[] valsArr = vals.split(",");
        movement = valsArr[0];
        step = Integer.parseInt(valsArr[1]);
    }

    public void X() {
        System.out.println("X direction move");
        y = (y + step) % col;
    }

    public void Y() {
        System.out.println("Y direction move");
        x = (x + step) % row;
    }

    public void M() {
        System.out.println("Move");
        if (movement != null) {
            String methodName = movement;
            try {
                getClass().getMethod(methodName).invoke(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String directionName = Character.toString(direction[curr_direction]);
            try {
                getClass().getMethod(directionName).invoke(this);
                getClass().getMethod(movement).invoke(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            if (Grid[x][y] == '1') {
                error.add("Found an obstacle at " + x + ", " + y);
                if ("X".equals(movement)) {
                    y = (y - 1 + col) % col;
                } else {
                    x = (x - 1 + row) % row;
                }
            } else {
                Grid[x][y] = 'R';
            }
        } catch (Exception e) {
            error.add(e.getMessage());
            System.out.println(error);
            System.exit(0);
        }
    }

    public String End() {
        // Return the facing direction as a tuple-like string
        return "(" + x + ", " + y + ", '" + direction[curr_direction] + "')";
    }
}

class Move {
    private final Rover rover;

    public Move(Rover rover) {
        this.rover = rover;
    }

    public void N() {
        System.out.println("Move North");
        rover.track_change("Y,-1");
    }

    public void E() {
        System.out.println("Move East");
        rover.track_change("X,1");
    }

    public void S() {
        System.out.println("Move South");
        rover.track_change("Y,1");
    }

    public void W() {
        System.out.println("Move West");
        rover.track_change("X,-1");
    }
}

public class MarsRover {
    public static void main(String[] args) {
        Initiate initiate = new Initiate();
        Rover rover = new Rover(initiate);
        Move move = new Move(rover);

        String[] movements = {"R", "M", "R", "M", "L", "M"};

        for (String movement : movements) {
            try {
                if (!movement.equals("M")) {
                    rover.getClass().getMethod(movement).invoke(rover);
                    move.getClass().getMethod(rover.direction[rover.curr_direction] + "").invoke(move);
                } else {
                    rover.getClass().getMethod(movement).invoke(rover);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String result = rover.End(); // Change the data type to String
        char[][] Grid = rover.Grid;
        String facing = "(" + rover.x + ", " + rover.y + ", '" + rover.direction[rover.curr_direction] + "')";
        List<String> errors = rover.error;

        System.out.println("\n\n----- Output -----");
        displayArray(Grid);
        System.out.println("Facing: " + facing);
        if (!errors.isEmpty()) {
            System.out.println("\nLogs while movement:");
            for (String error : errors) {
                System.out.println(error);
            }
            System.out.println();
        } else {
            System.out.println("\nLogs while movement: NONE\n");
        }
    }

    public static void displayArray(char[][] Arr) {
        for (char[] row : Arr) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
