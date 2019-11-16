
public class LeaderBoardEntry {

	private String name;
	private int level;
	private int seconds;
	
	public LeaderBoardEntry(String name, int level, int seconds) {
		this.name = name;
		this.level = level;
		this.seconds = seconds;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
