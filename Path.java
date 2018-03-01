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
      return this.states.get(this.states.size()).f;
    }

}
