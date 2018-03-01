import java.util.*;


public class AStar {
    public static ArrayList<State> closedList; //HashMap
    public static ArrayList<Path> openList;
    public static ArrayList<String> list;
    public static Path path;
    public static State duplicate;
    public static State s;

    public static Path AStar(State initialState){
      openList = new ArrayList<Path>();
      closedList = new ArrayList<State>(); //HashMap
      path = new Path();
      Path pathTemp = new Path(path);
      list = new ArrayList<String>(initialState.getPossibleActions());

      for (int i=0; i!=list.size(); i++){
        State state = new State(initialState, list.get(i));
        Path open = new Path();
        open.states.add(state);
        openList.add(open);
      }

      State currentState = null;
      Path minF = openList.get(0);

      for(int j = 1; j!=openList.size(); j++){
        Path temp = openList.get(j);
        if(temp.getCost() < minF.getCost()){
          minF = temp;
        }
      }

      while(openList.size()>0){
        path = minF;
        openList.remove(minF); //lol
        s = path.states.get(path.states.size()-1);
        closedList.add(s);
        if(currentState.isWin()){
          return path;
        }
        else {
          for (int a=0; a!=list.size(); a++){
            State newState = currentState.result(currentState, list.get(a));
            boolean inClosedList = false;
            boolean inOpenList = false;

            for(int b = 0; b!=closedList.size(); b++){
              if(newState.toString().equals(closedList.get(b).toString())){
                inClosedList = true;
                duplicate = closedList.get(b);
                break;
              }
            }

            for(int j = 0; j!=openList.size(); j++){
              Path temp = openList.get(j);
              for(int i = 0; i!=temp.states.size(); i++){
                if(newState.toString().equals(openList.get(j).toString())){
                  inOpenList = true;
                  duplicate = closedList.get(i);
                  break;
                }
              }
            }


            if((!inClosedList || !inOpenList) || ((inClosedList || inClosedList) && (newState.g < duplicate.g))){
              pathTemp.states.add(newState);
              openList.add(pathTemp);
            }

          }
        }

      }
      return path;
    }
}
