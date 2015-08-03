package item;

public abstract class Item {
	private String title;
	private String desc;
	private int xStart;
	private int yStart;
	
	public Item(String t, String d, int x, int y) {
		this.title = t;
		this.desc = d;
		this.xStart = x;
		this.yStart = y;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public int getX() {
		return xStart; 
	}
	
	public int getY() {
		return yStart;
	}
}
