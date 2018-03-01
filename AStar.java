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

      list = new ArrayList<String>(initialState.getPossibleActions()); //list gets the list of possible actions of initialState

      for (int i=0; i!=list.size(); i++){ //loops on list
        State state = new State(initialState, list.get(i)); //state is the state when the action from list is done
        Path open = new Path(); //new path
        open.states.add(state); //u add the state to the list of states of the new path
        openList.add(open); //u add the path open to openList
      }

      Path minF = openList.get(0); //first path in openList; outside of loop so minF doesnt get reassigned as the first path again
      for(int j = 1; j!=openList.size(); j++){ //loop for openList
        Path temp = openList.get(j); //temp is current path in openList
        if(temp.getCost() < minF.getCost()){ //compares the cost of minF & currentPath
          minF = temp; //if smoller minF is now currentPath
        }
      }

      while(openList.size()>0){ //while openlist has elements
        path = minF; //u get minF & put on path
        openList.remove(minF); //u remove minF
        s = path.states.get(path.states.size()-1); //u get last state of path
        closedList.add(s); //u add last state to the explored list
        if(s.isWin()){ //check if last state is winning state
          return path;
        }
        else {
          for (int a=0; a<list.size(); a++){ //loops till list has been checked
            State newState = s.result(s, list.get(a)); //gets the result if u do the action from list to state s
            boolean inClosedList = false;
            boolean inOpenList = false;

            for(int b = 0; b<closedList.size(); b++){ //checks if newState in closedList
              if(newState.toString().equals(closedList.get(b).toString())){
                inClosedList = true;
                duplicate = closedList.get(b); //if found u make a duplicate of newState
                break;
              }
            }

            for(int j = 0; j<openList.size(); j++){ //checks if newState in openList
              Path temp = openList.get(j);
              for(int i = 0; i<temp.states.size(); i++){
                if(newState.toString().equals(openList.get(j).toString())){
                  inOpenList = true;
                  duplicate = closedList.get(i); //if found u make a duplicate of newState
                  break;
                }
              }
            }


            if((!inClosedList || !inOpenList) || ((inClosedList || inOpenList) && (newState.g < duplicate.g))){
              pathTemp.states.add(newState);
              openList.add(pathTemp);
            }

          }
        }

      }
      return path;
    }
}
