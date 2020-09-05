package boatStuff.grid;

import java.awt.Point;

public class Grid {
	
	public static final int SIZE = 16 * (1<<2); //16 * 4 == 1<<6
	public static final int MASK = SIZE - 1; //0b 0000 0000 0011 1111
	public static final int WIDTH = SIZE;
	public static final int HEIGHT = SIZE;
	
	public static final int GRID_SIZE = WIDTH*HEIGHT;
	
	public static final double SQUARE_SIZE = 0.25;
	
	protected short[] grid;
	
	public Grid(){
		grid = new short[GRID_SIZE];
	}
	
	/*
	 * Inefficient
	public short getAtCoord(int x, int y){
		int xOffset = x&MASK;
		int yOffset = (y&MASK) * WIDTH;
		return grid[yOffset + xOffset];
	}
	*/
	public short getAtCoord(int x, int y){
		return grid[((y&MASK) * WIDTH) + (x&MASK)];
	}
	
	public void setAt(int x, int y, short newValue){
		grid[((y&MASK) * WIDTH) + (x&MASK)] = newValue;
	}
	
	public void insertChunkAt(int x, int y, short[] chunk){
		/*
		 * Assume that:
		 The grid is a square.
		 |----|
		 |    |
		 |    |
		 |----|
		 */
		double chunkWidth = Math.sqrt(chunk.length);
		if(chunkWidth != Math.floor(chunkWidth)){
			return; //Invalid Grid Chunk (Not Square).
		}
		int cWidth = (int) chunkWidth;
		for(int i = 0; i < cWidth; i++){
			for(int j = 0; j < cWidth; j++){
				setAt((x+i), (y+j), chunk[j*cWidth + i]);
			}
		}
	}
	
	public short[] getChunkAt(int x, int y){
		return getChunkAt(x, y, 16);
	}
	
	public short[] getChunkAt(int x, int y, int width){
		short[] chunk = new short[width*width];
		for(int i = 0; i < width; i++){
			for(int j = 0; j < width; j++){
				chunk[(j*width) + i] = getAtCoord((x+i), (y+j));
			}
		}
		return chunk;
	}
	
	public Point convertToCoord(double easting, double northing){
		int xPos = (int) (easting/SQUARE_SIZE);
		int yPos = (int) (northing/SQUARE_SIZE);
		Point result = new Point(xPos, yPos);
		return result;
	}
}
