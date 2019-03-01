public class InformedSearch
{
    public static void main(String[] args)
    {
        Pair startNode = new Pair(2, 1);
        Pair finalNode = new Pair(7, 4);

        AStar ASS = new AStar(startNode , finalNode);
        ASS.a_star();
    }
}
