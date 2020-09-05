package boatStuff;

import boatStuff.grid.TexGrid;

public class TexGridTest {
	public static void main(String[] args){
		new TexGridTest();
	}
	
	private TexGrid grid;
	public TexGridTest(){
		
		System.out.println(1<<4);
		
		System.out.println();
		
		grid = new TexGrid();
		printTexGridIds(grid);
		
		System.out.println();
		
		grid.setAt(1, 2, (short) 5);
		
		short[] chunk = grid.getChunkAt(0, 0, 20);
		printShortArray(chunk);
		printChunk(chunk);
		System.out.println(grid.getAtCoord(1, 2));
		
		
		grid.insertChunkAt(57, 57, chunk);
		
		chunk = grid.getChunkAt(0, 0, 4);
		
		printShortArray(chunk);
		printChunk(chunk);
		System.out.println(grid.getAtCoord(1, 2));
		System.out.println(grid.getAtCoord(2, 3));
		
		System.out.println();
		printTexGridIds(grid);
		System.out.println();
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
	
	public void printTexGridIds(TexGrid grid){
		for(int y = 0; y < TexGrid.TEXTURE_COUNT_HEIGHT; y++){
			for(int x = 0; x < TexGrid.TEXTURE_COUNT_WIDTH; x++){
				System.out.print(grid.textures[x][y].getTextureId() + ", ");
			}
			System.out.println();
		}
	}
}
