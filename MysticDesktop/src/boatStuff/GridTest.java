package boatStuff;

import boatStuff.grid.Grid;

public class GridTest {
	public static void main(String[] args){
		new GridTest();
	}
	
	private Grid grid;
	public GridTest(){
		
		System.out.println(1<<4);
		
		grid = new Grid();
		
		grid.setAt(1, 2, (short) 5);
		
		short[] chunk = grid.getChunkAt(0, 0, 4);
		printShortArray(chunk);
		printChunk(chunk);
		System.out.println(grid.getAtCoord(1, 2));
		
		
		grid.insertChunkAt(1, 1, chunk);
		
		chunk = grid.getChunkAt(0, 0, 4);
		
		printShortArray(chunk);
		printChunk(chunk);
		System.out.println(grid.getAtCoord(1, 2));
		System.out.println(grid.getAtCoord(2, 3));
	}
	
	public static void printShortArray(short[] arr){
		for(int i = 0; i < arr.length; i++){
			System.out.print(arr[i] + ", ");
		}
		System.out.println();
	}
	
	public static void printChunk(short[] chunk){
		int width = (int) Math.sqrt(chunk.length);
		for(int j = 0; j < width; j++){
			for(int i = 0; i < width; i++){
				System.out.print(String.valueOf(chunk[(j*width) + i]) + ", ");
			}
			System.out.println();
		}
	}
}
