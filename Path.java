import java.util.*;


public class Path {
    public static ArrayList<String> actions;
    public static ArrayList<State> states;

    public Path(){
      this.states = new ArrayList<State>();
      this.actions = new ArrayList<String>();
    }

    public Path(Path p){
      this.states = p.states;
      this.actions = p.actions;
    }

    public int getCost(){
      return this.states.get(this.states.size()-1).f;
    }

    public Path resultPath(String action){
      State s = states.get(states.size()-1);
      State result = s.result(s, action);

      Path pathTemp = new Path(this);
      pathTemp.states.add(result);

      return pathTemp;
    }

    public State getLastState(){
      return this.states.get(this.states.size()-1);
    }
}
