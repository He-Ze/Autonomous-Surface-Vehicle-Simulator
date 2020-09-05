package boatStuff.grid;

import java.awt.Point;

public class PacketThing {
	
	private int xPos;
	private int yPos;
	private short[] chunkData;
	
	public PacketThing(Grid grid, double easting, double northing){
		Point xy = grid.convertToCoord(easting, northing);
		double spread = 0;
		
		int chunkSize = 16;
		
		int xMiddle = xy.x - (chunkSize/2);
		int yMiddle = xy.y - (chunkSize/2);
		
		double distance = Math.random() * spread;
		double theta = Math.random() * Math.PI * 2;
		
		double xOffset = distance * Math.sin(theta);
		double yOffset = distance * Math.cos(theta);
		
		this.xPos = (int) (xMiddle + xOffset);
		this.yPos = (int) (yMiddle + yOffset);
		
		this.chunkData = grid.getChunkAt(xPos, yPos, chunkSize);
		
		//put int xPos.
		//put int yPos.
		//put int arrayLength.
		//put short[] chunkData.
	}
}
