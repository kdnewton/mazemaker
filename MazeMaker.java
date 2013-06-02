
import java.util.Random;

public class MazeMaker {
	
	private Random rand = new Random();
	protected int[][] rooms;
	
	public MazeMaker(int xx, int yy) {
		int xstart, ystart;
		xstart = getRand(1,xx-3);
		ystart = getRand(1,yy-3);
		setDimensions(xx,yy);
		zeroMaze();
		carveMaze(xstart,ystart);
		placeExits();
	}
	
	/**************
	** SET ~ GET **
	**************/
	
	public void setDimensions(int xx, int yy) {
		rooms = new int[xx][yy];
	}
	public int getXDim() { return rooms.length; }
	public int getYDim() { return rooms[0].length; }
	
	public int getRand(int low, int hi) {
		if (low == 0)
			return Math.abs(rand.nextInt()%(hi+1))+low;
		else
			return Math.abs(rand.nextInt()%hi)+low;
	}
	
	public void setRoom(int xx, int yy, int val) {
		rooms[xx][yy] = val;
	}
	public int getRoom(int xx, int yy) { return rooms[xx][yy]; }
	
	/******************
	*******************/
	
	public void zeroMaze() {
		for (int y = 0; y < getYDim(); y++) {
			for (int x = 0; x < getXDim(); x++) {
				setRoom(x,y,0);
			}
		}
	}
	
	public void carveMaze(int xx, int yy) {
		if (xx < 1 || yy < 1 || xx > (getXDim()-2) || yy > (getYDim()-2) || !canBuildAt(xx,yy)) {
			return;
		} else {
			carveRoom(xx,yy,1);
			//~ System.out.println (toString());
			boolean canCarve = false;
			while (canBuildAt(xx-1,yy) || canBuildAt(xx+1,yy) ||
					canBuildAt(xx,yy-1) || canBuildAt(xx,yy+1)) {
				switch (getRand(1,4)) {
					case 1: carveMaze(xx-1,yy); break;
					case 2: carveMaze(xx+1,yy); break;
					case 3: carveMaze(xx,yy-1); break;
					case 4: carveMaze(xx,yy+1); break;
					default: System.out.println ("Whoops"); break;
				}
			}
		}
	}
	
	public void carveRoom(int xx, int yy, int val) {
		int ymin = (yy < 2) ? 1 : yy-1;
		int xmin = (xx < 2) ? 1 : xx-1;
		int ymax = (yy > (getYDim()-3)) ? (getYDim()-2) : yy+1;
		int xmax = (xx > (getXDim()-3)) ? (getXDim()-2) : xx+1;
		if (getRoom(xx,yy) != 0) {
			return;
		}
		setRoom(xx,yy,val);
	}
	
	public boolean canBuildAt(int xx, int yy) {
		//~ Count how many rooms have already been carved.
		//~ Greater than 3 means this room is solid
		if (xx < 1 || yy < 1 || xx > (getXDim()-2) || yy > (getYDim()-2) || getRoom(xx,yy) != 0) {
			return false;
		}
		int ymin = (yy < 1) ? 0 : yy-1;
		int xmin = (xx < 1) ? 0 : xx-1;
		int ymax = (yy > (getYDim()-2)) ? (getYDim()-1) : yy+1;
		int xmax = (xx > (getXDim()-2)) ? (getXDim()-1) : xx+1;
		int count = 0;
		if (getRoom(xx,yy-1) != 0) {
			count++;
		}
		if (getRoom(xx+1,yy) != 0) {
			count++;
		}
		if (getRoom(xx,yy+1) != 0) {
			count++;
		}
		if (getRoom(xx-1,yy) != 0) {
			count++;
		}
		if (count > 1) {
			return false;
		} else {
			return true;
		}
		
	}
	
	public void placeExits() {
		int entrance, exit;
		do {
			entrance = getRand(1,getYDim()-2); // Choose the left wall entrance
		} while (getRoom(1,entrance) != 1);
		do {
			exit = getRand(1,getYDim()-2);
		} while (getRoom(getXDim()-2,exit) != 1);
		setRoom(0,entrance,1);
		setRoom(getXDim()-1,exit,1);
	}
	
	public String toString() {
		String out = "";
		for (int y = 0; y < getYDim(); y++) {
			for (int x = 0; x < getXDim(); x++) {
				if (rooms[x][y] == 0)
					out += "X";
				else
					out += " ";
				if (x == getXDim()-1 && y != getYDim()-1) {
					out += "\n";
				}
			}
		}
		return out;
	}
	
	/*********
	** MAIN **
	*********/
	
	public static void main(String[] args) {
		MazeMaker mm;
		if (null == args || args.length < 1) {
			System.out.println ("Defaulting to 25x25 size maze");
			mm = new MazeMaker(25,25);
		} else {
			int width = Integer.parseInt(args[0]);
			int height = Integer.parseInt(args[1]);
			mm = new MazeMaker(width,height);
		}
		System.out.print (mm.toString());
	}
	
	/********************
	** PRIVATE CLASSES **
	********************/
}