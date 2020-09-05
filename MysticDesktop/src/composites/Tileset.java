package composites;

import configData.Config;

public class Tileset {
	
	private Config tileData = new Config();
	
	public Tileset(Config... tileData) {
		this.tileData.newSet("null");
		
		for (int a = 0; a < tileData.length; a++) {
			for (String key : tileData[a].getKeys()) {
				this.tileData.addValues(key, tileData[a].getStrings(key));
				this.tileData.addValues(key, tileData[a].getShorts(key));
			}
		}
		
		this.tileData.printOptions();
		
	}
	
	public int getTileIndex(String name) {
		return this.tileData.setExists(name);
	}
	
	public String[] getTileNames() {
		return this.tileData.getKeys();
	}
	
	public String getTileImage(String name) {
		return this.tileData.getStrings(name)[0];
	}
	
	public short[] getFrameDelays(String name) {
		return this.tileData.getShorts(name);
	}
}