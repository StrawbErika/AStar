import java.util.*;


public class Path {
    public ArrayList<String> actions;
    public ArrayList<State> states;

    public Path(){
      this.states = new ArrayList<State>();
      this.actions = new ArrayList<String>();
    }

    public Path(Path p){
      this.states = new ArrayList<State>();
      for(int i = 0; i<p.states.size(); i++){
        this.states.add(new State(p.states.get(i)));
      }
      this.actions = new ArrayList<String>();
      for(int i = 0; i<p.actions.size(); i++){
        this.actions.add(p.actions.get(i));
      }

    }
    public int getCost(){
      return this.states.get(this.states.size()-1).f;
    }

    public Path resultPath(String action){
      State s = states.get(states.size()-1);
      State result = s.result(s, action);

      Path pathTemp = new Path(this);
      pathTemp.states.add(result);
      pathTemp.actions.add(action);


      return pathTemp;
    }

    public State getLastState(){
      return this.states.get(this.states.size()-1);
    }
}
