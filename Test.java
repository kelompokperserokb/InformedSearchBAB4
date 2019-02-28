package Bab4;

public class Test
{
    public static void main(String[] args)
    {
        Pair startNode = new Pair(2, 1);

        AStar ASS = new AStar(startNode);
        ASS.a_star();
    }
}
