package level;

import java.util.ArrayList;
import java.util.List;


import game.entities.Entity;
import gfx.Screen;
import level.tiles.Tile;

public class Level {
	
	public int height;
	public int width;
	private byte[] tiles;
	public List<Entity> entities = new ArrayList<Entity>();
	
	public Level(int height, int width)
	{
		tiles = new byte[height * width];
		this.height = height;
		this.width = width;
		this.generateLevel();
	}
	
	public void renderTiles(Screen screen, double xOffset, double yOffset){
		if(xOffset<=0)	xOffset = 0;
		if(xOffset>((width<<3) - screen.width))	xOffset = ((width<<3) - screen.width);
		if(yOffset<=0)	yOffset = 0;
		if(yOffset>((height<<3) - screen.height))	yOffset = ((height<<3) - screen.height);
		
		screen.setOffset(xOffset, yOffset);
		
		for(int y=0; y<height; y++)
		{
			for(int x=0; x<width; x++)
			{
				getTile(x,y).render(screen, this, x<<3, y<<3);
			}
		}
		
	}

	private void generateLevel()
	{
		for(int i=0;i<height;++i)
		{
			for(int j=0;j<width;j++)
			{
				if(j * i % 10 < 9) {		//change the numbers of stones (<9)
					tiles[j+i*width] = Tile.FLOOR.getID();
				}
				else {
					tiles[j+i*width] = Tile.STONE.getID();
				}
			}
		}
	}
	
	public Tile getTile(int x, int y)
	{
		if(x<0||x>=width||y<0||y>=height)	return Tile.VOID;		
		return Tile.tiles[tiles[x+y*width]];
	}

	public void tick() {
		for(Entity e: entities) {
			e.tick();
		}
	}
	
	public void renderEntities(Screen screen) {
		for(Entity e: entities) {
			e.render(screen);
		}
	}
	
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}
}
