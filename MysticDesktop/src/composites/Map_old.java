package composites;

import java.io.File;
import java.util.Arrays;
import configData.Config;

public class Map_old {
	public final int BACKGROUND = 0, GROUND = 1, FOREGROUND = 2;
	public Tileset tileset;
	private short width, height;
	private short[][] backgroundData, groundData, foregroundData;
	private String name;
	
	public Map_old(short width, short height) {
		newMap(width, height);
		this.width = width;
		this.height = height;
	}
	
	public Map_old(Config mapData) {
		short[] dimensions = mapData.getShorts("map");
		String[] mapStrings = mapData.getStrings("map");
		name = mapStrings[0];
		width = dimensions[0];
		height = dimensions[1];
		
		loadTileset(Arrays.copyOfRange(mapStrings, 1, mapStrings.length));
		
		backgroundData = loadMapData(mapData.getShorts("background"));
		groundData = loadMapData(mapData.getShorts("ground"));
		foregroundData = loadMapData(mapData.getShorts("foreground"));
	}
	
	private void newMap(short width, short height) {
		backgroundData = new short[width][height];
		groundData = new short[width][height];
		foregroundData = new short[width][height];
	}
	
	private short[][] loadMapData(short[] mapData) {
		short[][] formattedData = new short[width][height];
		for (int yPos = 0; yPos < height; yPos++) {
			for (int xPos = 0; xPos < width; xPos++) {
				formattedData[xPos][height - 1 - yPos] = mapData[xPos + width * yPos];
			}
		}
		return formattedData;
	}
	
	public short[][] getData(int layer) {
		switch (layer) {
			case BACKGROUND:
				return backgroundData;
			case GROUND:
				return groundData;
			case FOREGROUND:
				return foregroundData;
			default:
				System.out.println("Layer does not exist!");
				return new short[0][0];
		}
	}
	
	private void loadTileset(String... path) {
		File cfgFile;
		Config[] tileset = new Config[path.length];
		for (int a = 0; a < path.length; a++) {
			cfgFile = new File(path[a]);
			tileset[a] = new Config(cfgFile);
		}
		this.tileset = new Tileset(tileset);
	}
	
	public short getTile(int layer, int xPos, int yPos) {
		if ((xPos < width) && (yPos < height)) {
			switch (layer) {
				case BACKGROUND:
					return backgroundData[xPos][yPos];
				case GROUND:
					return groundData[xPos][yPos];
				case FOREGROUND:
					return foregroundData[xPos][yPos];
				default:
					System.out.println("Layer does not exist!");
					return 0;
			}
		} else {
			return 0;
		}
	}
	
	public String getTileName(int layer, int xPos, int yPos) {
		if ((xPos < width) && (yPos < height)) {
			switch (layer) {
				case BACKGROUND:
					return tileset.getTileNames()[backgroundData[xPos][yPos]];
				case GROUND:
					return tileset.getTileNames()[groundData[xPos][yPos]];
				case FOREGROUND:
					return tileset.getTileNames()[foregroundData[xPos][yPos]];
				default:
					System.out.println("Layer does not exist!");
			}
		}
		return "";
	}
	
	public void updateTile(int layer, int xPos, int yPos, short tile) {
		if ((xPos < width) && (yPos < height)) {
			switch (layer) {
				case BACKGROUND:
					backgroundData[xPos][yPos] = tile;
					break;
				case GROUND:
					groundData[xPos][yPos] = tile;
					break;
				case FOREGROUND:
					foregroundData[xPos][yPos] = tile;
					break;
				default:
					System.out.println("Layer does not exist!");
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name; 
	}
	
}
