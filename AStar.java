package Bab4;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStar extends AbstractSearch
{
    public AStar(Pair startNode)
    {
        this.startNode = startNode;
        super.createRooms();
        System.out.println(super.visualizeMap());
    }

    public void a_star()
    {                
        List<Room> queue = new ArrayList<>();
        queue.add(rooms[startNode.x][startNode.y]);

        while(!queue.isEmpty())
        {
            Collections.sort(queue, (Room o1, Room o2) -> o1.cost>o2.cost?1:-1);
            Room r = queue.remove(0);
            double cost;
            if(r.parent != null){
		cost = rooms[r.parent.x][r.parent.y].cost+1+r.cost;
            }            
            visitedList.add(r);
            if(r.goal)
            {
                printSolution("AStar", r);
                System.out.println("A_Star Search:");
                System.out.println("Solution step look at output/A-star.txt");
                System.out.println(super.visualizeMap());
                return;
            }

            visitedList.add(r);
            if(r.e && !visitedList.contains(rooms[r.current.x][r.current.y + 1]) && !queue.contains(rooms[r.current.x][r.current.y + 1]))
            {
                rooms[r.current.x][r.current.y + 1].parent = new Pair(r.current.x, r.current.y);
                queue.add(rooms[r.current.x][r.current.y + 1]);
            }
            if(r.s && !visitedList.contains(rooms[r.current.x + 1][r.current.y]) && !queue.contains(rooms[r.current.x + 1][r.current.y]))
            {
                rooms[r.current.x + 1][r.current.y].parent = new Pair(r.current.x, r.current.y);
                queue.add(rooms[r.current.x + 1][r.current.y]);
            }
            if(r.w && !visitedList.contains(rooms[r.current.x][r.current.y - 1]) && !queue.contains(rooms[r.current.x][r.current.y - 1]))
            {
                rooms[r.current.x][r.current.y - 1].parent = new Pair(r.current.x, r.current.y);
                queue.add(rooms[r.current.x][r.current.y - 1]);
            }
            if(r.n && !visitedList.contains(rooms[r.current.x - 1][r.current.y]) && !queue.contains(rooms[r.current.x - 1][r.current.y]))
            {
                rooms[r.current.x - 1][r.current.y].parent = new Pair(r.current.x, r.current.y);
                queue.add(rooms[r.current.x - 1][r.current.y]);
            }
        }
    }
}
