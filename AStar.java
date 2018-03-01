import java.util.*;


public class AStar {
    public static ArrayList<State> closedList; //HashMap
    public static ArrayList<Path> openList;
    public static ArrayList<String> list;
    public static Path path;
    public static State duplicate;
    public static State s;

    public static Path solve(State initialState){
      openList = new ArrayList<Path>();
      closedList = new ArrayList<State>();
      path = new Path();
      Path pathTemp = new Path(path);

      list = new ArrayList<String>(initialState.getPossibleActions());

//fill up initialState chuchu openlist
      for (int i=0; i!=list.size(); i++){
        State state = new State(initialState, list.get(i)); //state is the state when the action from list is done
        Path open = new Path();
        open.states.add(state);
        openList.add(open);
      }

// System.out.println("openList initialized");
// System.out.println("minF found");

      while(openList.size()>0){ //while openlist has elements
        Path minF = openList.get(0);

//find minF of openList
        for(int j = 1; j!=openList.size(); j++){
          Path temp = openList.get(j);
          if(temp.getCost() < minF.getCost()){
            minF = temp;
          }
        }

        path = minF;
System.out.println("BEFORE REMOVE openList.size() == "+ openList.size());
        openList.remove(minF);
System.out.println("AFTER REMOVE openList.size() == "+ openList.size());

System.out.println("============================================ \n ");
        s = path.states.get(path.states.size()-1);
        if(s.isWin()){ //check if last state is winning state
          return path;
        }
        else {
          for (int a=0; a<list.size(); a++){
            Path newPath = path.resultPath(list.get(a));
            boolean inClosedList;
            boolean inOpenList;

//checks if newState in closedList
            inClosedList = closedList.contains(newPath.getLastState());
            if(inClosedList){
              duplicate = newPath.getLastState();
            }

//checks if newState in openList
            inOpenList = openList.contains(newPath);
            if(inClosedList){
              duplicate = newPath.getLastState();
            }

            if((!inClosedList || !inOpenList) || ((inClosedList || inOpenList) && (newPath.getLastState().g < duplicate.g))){
              if(!openList.contains(newPath)){
                openList.add(newPath);
              }
            }

          }
        }

      }
      System.out.println("i got out of while loop");

      return path;
    }
}
