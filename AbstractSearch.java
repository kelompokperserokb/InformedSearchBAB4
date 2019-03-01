import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AbstractSearch
{
    protected Pair startNode;
    protected Pair finalNode;
    protected Room[][] rooms = new Room[8][8];
    protected List<Room> visitedList = new ArrayList<Room>();
    private char[][] vmap;

    public AbstractSearch()
    {
        this.startNode = new Pair(0, 0);
    }

    /*
     * Creates maze from the file name. Default file name is maze.txt.
     * sample row of maze is 1001-1100-1001-1010-1000-1010-1010-1100
     * order of each column is N-E-S-W
     * 1 means no move, 0 can move
     */
    public void createRooms()
    {
        this.createRooms(System.getProperty("user.dir")
+"/src/maze.txt");
        this.vmap = new char[(this.rooms.length * 2 + 1)][(this.rooms.length * 2 + 1)];
        this.visualize();
    }

    public void createRooms(String fileName)
    {

        File file = new File(fileName);
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            int row = 0;
            while((line = br.readLine()) != null)
            {
                String[] columns = line.split("-");

                for(int i = 0; i < columns.length; i++)
                {
                    rooms[row][i] = new Room(columns[i], row, i);
                }
                row++;

            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        rooms[finalNode.x][finalNode.y].goal = true;
    }

    private void visualize()
    {
        final char NWE = '╔';
        final char WC = '╠';
        final char P = '╬';
        final char WV = '║';
        final char WH = '═';
        final char NEE = '╗';
        final char EC = '╣';
        final char NEC = '╦';
        final char SEE = '╝';
        final char SEC = '╩';
        final char SWE = '╚';

        //phase 1 : make border
        vmap[0][0] = NWE;
        vmap[0][vmap.length - 1] = NEE;
        vmap[vmap.length - 1][0] = SWE;
        vmap[vmap.length - 1][vmap.length - 1] = SEE;
        //phase 2 : make border pillar
        for(int i = 0, is = vmap[0].length - 1, l = vmap.length - 1; ++i < is; )
        {
            boolean isEven = (i % 2 == 0);
            vmap[0][i] = isEven ? NEC : '\0';
            vmap[l][i] = isEven ? SEC : '\0';
        }
        for(int i = 0, is = vmap.length - 1, l = vmap[0].length - 1; ++i < is; )
        {
            boolean isEven = (i % 2 == 0);
            vmap[i][0] = isEven ? WC : '\0';
            vmap[i][l] = isEven ? EC : '\0';
        }
        //phase 2 : make center pillar
        for(int i = -1, is = rooms.length - 1, ip = 2, js = rooms[0].length - 1; ++i < is; ip += 2)
        {
            for(int j = -1, jp = 2; ++j < js; jp += 2)
            {
                vmap[ip][jp] = P;
            }
        }
        //phase 3 : make content
        for(int i = -1, is = rooms.length, js = rooms[0].length; ++i < is; )
        {
            for(int j = -1; ++j < js; )
            {
                try
                {
                    if(rooms[i][j].n == rooms[i - 1][j].s)
                    {
                        if(!rooms[i][j].n)
                        {
                            vmap[i * 2][j * 2 + 1] = WH;
                        }
                    }
                    else
                    {
                        vmap[i * 2][j * 2 + 1] = '?';
                    }
                }
                catch(ArrayIndexOutOfBoundsException ignored)
                {
                    vmap[i * 2][j * 2 + 1] = WH;
                }
                try
                {
                    if(rooms[i][j].w == rooms[i][j - 1].e)
                    {

                        if(!rooms[i][j].w)
                        {
                            vmap[i * 2 + 1][j * 2] = WV;
                        }
                    }
                    else
                    {
                        vmap[i * 2 + 1][j * 2] = '?';
                    }
                }
                catch(ArrayIndexOutOfBoundsException ignored)
                {
                    vmap[i * 2 + 1][j * 2] = WV;
                }
                try
                {
                    if(rooms[i][j].e == rooms[i][j + 1].w)
                    {
                        if(!rooms[i][j].e)
                        {
                            vmap[i * 2 + 1][j * 2 + 2] = WV;
                        }
                    }
                    else
                    {
                        vmap[i * 2 + 1][j * 2 + 2] = '?';
                    }
                }
                catch(ArrayIndexOutOfBoundsException ignored)
                {
                    vmap[i * 2 + 1][j * 2 + 2] = WV;
                }
                try
                {
                    if(rooms[i][j].s == rooms[i + 1][j].n)
                    {
                        if(!rooms[i][j].s)
                        {
                            vmap[i * 2 + 2][j * 2 + 1] = WH;
                        }
                    }
                    else
                    {
                        vmap[i * 2 + 2][j * 2 + 1] = '?';
                    }
                }
                catch(ArrayIndexOutOfBoundsException ignored)
                {
                    vmap[i * 2 + 2][j * 2 + 1] = WH;
                }
            }
        }
        vmap[finalNode.x * 2 + 1][finalNode.y * 2 + 1] = '￭';
        vmap[startNode.x * 2 + 1][startNode.y * 2 + 1] = '￮';
    }

    /*
     * Prints the output to file.
     * Each algorithm's output place in distinct files.
     * File will place under output/alg.txt file.
     *
     */
    public void printSolution(String alg, Room r)
    {
        File file = new File(System.getProperty("user.dir")+"/src/Output/" + alg + ".txt");
        PrintWriter pw;
        try
        {
            pw = new PrintWriter(file);
            pw.append("Solution Path:\n");
            int totalCost = 1;
            totalCost = printSolutionPath(r, pw, totalCost);
            pw.append("Solution Cost: " + totalCost + "\n");

            pw.append("Expanded Path\n");
            printVisited(pw, visitedList);
            pw.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    private void printVisited(PrintWriter pw, List<Room> visited)
    {
        for(Room room : visited)
        {
            pw.append("x: " + (room.current.x + 1) + "\ty: " + (room.current.y + 1) + "\n");
        }
    }

    public int printSolutionPath(Room r, PrintWriter pw, Integer totalCost)
    {
        pw.append("x: " + (r.current.x + 1) + "\ty: " + (r.current.y + 1) + "\n");
        if(r.parent != null)
        {
            if((r.current.x + 1 == r.parent.x))
            {
                vmap[(r.current.x + 1) * 2][(r.current.y) * 2 + 1] = 'l';
            }
            else if((r.current.x - 1 == r.parent.x))
            {
                vmap[(r.current.x - 1) * 2 + 2][(r.current.y) * 2 + 1] = 'l';
            }
            else
            {
                if((r.current.y + 1 == r.parent.y))
                {
                    vmap[(r.current.x) * 2 + 1][(r.current.y + 1) * 2] = '-';
                }
                else if((r.current.y - 1 == r.parent.y))
                {
                    vmap[(r.current.x) * 2 + 1][(r.current.y - 1) * 2 + 2] = '-';
                }
            }
            if(!r.goal)
            {
                if(vmap[r.current.x * 2 + 1][r.current.y * 2] == '-')
                {
                    if(vmap[r.current.x * 2 + 2][r.current.y * 2 + 1] == 'l')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '+';
                    }
                    else if(vmap[r.current.x * 2][r.current.y * 2 + 1] == 'l')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '+';
                    }
                    else if(vmap[r.current.x * 2 + 1][r.current.y * 2 + 2] == '-')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '-';
                    }
                }
                else if(vmap[r.current.x * 2][r.current.y * 2 + 1] == 'l')
                {
                    if(vmap[r.current.x * 2 + 1][r.current.y * 2 + 2] == '-')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '+';
                    }
                    else if(vmap[r.current.x * 2 + 1][r.current.y * 2] == '-')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '+';
                    }
                    else if(vmap[r.current.x * 2 + 2][r.current.y * 2 + 1] == 'l')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = 'l';
                    }
                }
                else if(vmap[r.current.x * 2 + 1][r.current.y * 2 + 2] == '-')
                {
                    if(vmap[r.current.x * 2][r.current.y * 2 + 1] == 'l')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '+';
                    }
                    else if(vmap[r.current.x * 2 + 2][r.current.y * 2 + 1] == 'l')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '+';
                    }
                    else if(vmap[r.current.x * 2 + 1][r.current.y * 2] == '-')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '-';
                    }
                }
                else if(vmap[r.current.x * 2 + 2][r.current.y * 2 + 1] == 'l')
                {
                    if(vmap[r.current.x * 2 + 1][r.current.y * 2] == '-')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '+';
                    }
                    else if(vmap[r.current.x * 2 + 1][r.current.y * 2] == '-')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = '+';
                    }
                    else if(vmap[r.current.x * 2][r.current.y * 2 + 1] == 'l')
                    {
                        vmap[r.current.x * 2 + 1][r.current.y * 2 + 1] = 'l';
                    }
                }

            }
            totalCost = printSolutionPath(rooms[r.parent.x][r.parent.y], pw, ++totalCost);
        }
        return totalCost;
    }

    public String visualizeMap()
    {
        StringBuilder sb = new StringBuilder(vmap.length * vmap[0].length + vmap.length);
        for(final char[] row : vmap)
        {
            for (char a : row) {
                if (a == '\0') {
                    sb.append(' ');
                } else sb.append(a);
            }

            sb.append('\n');
        }
        return sb.toString();
    }
}
