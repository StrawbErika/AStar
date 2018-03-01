import java.util.ArrayList;
import java.lang.Math;

public class State {
    public static final String UNVISITED = "t";
    public static final String GOAL = "G";
    public static final String WALL = "W";
    public static final String KEEPER = "P";
    public static final String VISITED = "v";

    public int g;
    public int h;
    public int f;
    public String[][] state;
    public State parentState;
    public String action;
    public ArrayList<String> actionsNeeded;

    public Coordinates goalPosition;
    public Coordinates keeperPosition;
    public Coordinates parentKeeperPosition;

    public State(String[][] state, State parentState, String action) {
        this.parentState = parentState;
        this.state = new String[Game.ROWS][Game.COLS];
        this.g = 0;
        this.h = 0;
        this.f = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
              this.state[i][j] = state[i][j];
            }
        }

        this.action = action;

        if (parentState!=null){
          this.actionsNeeded = new ArrayList(parentState.getActionsNeeded());
          actionsNeeded.add(action); // add the action for this state
        }
        else{
          this.actionsNeeded = new ArrayList();
        }

        // find keeper position and save it
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLS; j++) {
                if (this.state[i][j].equals(State.KEEPER)) {
                    this.keeperPosition = new Coordinates(i, j); //position of keeper
                }
            }
        }

        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLS; j++) {
                if (this.state[i][j].equals(State.GOAL)) {
                    this.goalPosition = new Coordinates(i, j); //position of keeper
                }
            }
        }
        if(action!=null){
          if(action.equals("U")){
            this.moveUp();
          }
          else if(action.equals("D")){
            this.moveDown();
          }
          else if(action.equals("L")){
            this.moveLeft();
          }
          else if(action.equals("R")){
            this.moveRight();
          }
        }
    }

    public State (State parentState, String action){
      this(parentState.getState(), parentState, action);
    }

    public State(String[][] state) {
      this(state, null, null); //call constructor
    }

    public State(State state) {
      this(state.getState(), null, null); //call constructor
    }

    public String getValue(int i, int j) {
        return this.state[i][j];
    }

    public ArrayList<String> getActionsNeeded(){
      return this.actionsNeeded;
    }

    public State getParentState(){
      return this.parentState;
    }

    public String[][] getState(){
      return this.state;
    }

    public void move(int y, int x) {
        Coordinates currentCoordinates = new Coordinates(keeperPosition.getY(), keeperPosition.getX());
        String currentValue = state[currentCoordinates.getY()][currentCoordinates.getX()];

        Coordinates nextCoordinates = new Coordinates(keeperPosition.getY() + y, keeperPosition.getX() + x);
        String nextValue;
        try {
          nextValue = state[nextCoordinates.getY()][nextCoordinates.getX()];
        }
        catch(Exception e) {
          nextValue = null;
        }

        if(nextValue!=null){

          if(nextValue.equals(State.UNVISITED)) {
              state[nextCoordinates.getY()][nextCoordinates.getX()] = State.KEEPER;
              state[currentCoordinates.getY()][currentCoordinates.getX()] = State.VISITED;
              this.keeperPosition = nextCoordinates;
          }else if(nextValue.equals(State.GOAL)){
            state[nextCoordinates.getY()][nextCoordinates.getX()] = State.KEEPER;
            state[currentCoordinates.getY()][currentCoordinates.getX()] = State.VISITED;
            this.keeperPosition = nextCoordinates;
          }else if(nextValue.equals(State.VISITED)) {
              state[nextCoordinates.getY()][nextCoordinates.getX()] = State.KEEPER;
              state[currentCoordinates.getY()][currentCoordinates.getX()] = State.VISITED;
              this.keeperPosition = nextCoordinates;
          }
        }
        double h = getH();
        ArrayList<String> act = this.getPossibleActions();
        for(int i = 0; i < act.size(); i++){
          System.out.println(act.get(i));
        }
        g++;
        int h2 = (int) h;
        f = g + h2;
    }

    public double getH(){
      double ans;
      double x = Math.pow(keeperPosition.getX() - goalPosition.getX(), 2);
      double y = Math.pow(keeperPosition.getY() - goalPosition.getY(), 2);
      double inside = x + y;
      ans = Math.sqrt(inside);
      return ans;
    }

    public ArrayList<String> getPossibleActions(){
      ArrayList<String> possibleActions = new ArrayList<String>();

      if(canMove(-1,0)){
        possibleActions.add("U");
      }
      if(canMove(1,0)){
        possibleActions.add("D");
      }
      if(canMove(0,-1)){
        possibleActions.add("L");
      }
      if(canMove(0,1)){
        possibleActions.add("R");
      }

      return possibleActions;
    }

    public boolean canMove(int y, int x){
      Coordinates currentCoordinates = new Coordinates(keeperPosition.getY(), keeperPosition.getX());
      String currentValue = state[currentCoordinates.getY()][currentCoordinates.getX()];

      Coordinates nextCoordinates = new Coordinates(keeperPosition.getY() + y, keeperPosition.getX() + x);
      String nextValue;
      try {
        nextValue = state[nextCoordinates.getY()][nextCoordinates.getX()];
      }
      catch(Exception e) {
        nextValue = null;
      }

      if(nextValue!=null){
        if(nextValue.equals(State.WALL)){
          return false;
        }
        else{
          return true;
        }
      }
      else{
        return true;
      }
    }

    public void moveUp() {
        this.move(-1, 0);
    }

    public void moveDown() {
        this.move(1, 0);
    }

    public void moveLeft() {
        this.move(0, -1);
    }

    public void moveRight() {
        this.move(0, 1);
    }

    public State result(State currentState, String action){
      State result = new State(currentState, action);
      return result;
    }

    public boolean isWin() {
        // it's win if there's no Game.BOX existing
        for(int i = 0; i < Game.ROWS; i++) {
            for(int j = 0; j < Game.COLS; j++) {
                String currentValue = this.state[i][j];

                if (currentValue.equals(State.GOAL)) {
                    return false;
                }
            }
        }

        return true;
    }

    public String toString() {
        String out = "";

        for(int i = 0; i < this.state.length; i++) {
            for(int j = 0; j < this.state.length; j++) {
              if(this.state[i][j].equals(State.UNVISITED)){
                out += " ";
              }
              else{
                out += this.state[i][j];
              }
            }
            out += "\n";
        }

        return out;
    }

    public int pathCost(){
      return this.actionsNeeded.size();
    }

    public Coordinates getKeeper(){
      return this.keeperPosition;
    }
}

// g is the number of moves from the initial state that have been made to reach the current state, and
// h is the heuristic that we will use to estimate the current state’s distance from the goal state.
