package Bab4;
public class Room
{
    final Pair current;
    Pair parent;
    boolean goal, e, w, s, n;

    double cost;

    public Room(String alias, int x, int y)
    {
        //ESWN
        this.n = (alias.charAt(0) == '0');
        this.e = (alias.charAt(1) == '0');
        this.s = (alias.charAt(2) == '0');
        this.w = (alias.charAt(3) == '0');
        this.current = new Pair(x, y);
        cost = Math.sqrt((Math.pow((7 - x), 2) + Math.pow((7 - y), 2)));
    }
}
