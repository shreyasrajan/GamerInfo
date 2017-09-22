package entityClasses_Others;

public class GameEntity {

	int ID;
	String thumbNailUrl;
	String gameName;
	String origRelDate;
	String platform;
	String detailURL;
	
	public GameEntity(String thumbNailUrl, String gameName, String origRelDate,
			          String platform, String detailURL) {
		super();
		this.thumbNailUrl = thumbNailUrl;
		this.gameName = gameName;
		this.origRelDate = origRelDate;
		this.platform = platform;
		this.detailURL = detailURL;
	}

	public GameEntity(int ID, String thumbNailUrl, String gameName,
			          String origRelDate, String platform, String detailURL) {
		super();
		this.ID = ID;
		this.thumbNailUrl = thumbNailUrl;
		this.gameName = gameName;
		this.origRelDate = origRelDate;
		this.platform = platform;
		this.detailURL = detailURL;
	}

	public int getID() {
		return ID;
	}

	public String getThumbNailUrl() {
		return thumbNailUrl;
	}

	public String getGameName() {
		return gameName;
	}

	public String getOrigRelDate() {
		return origRelDate;
	}

	public String getPlatform() {
		return platform;
	}

	public String getDetailURL() {
		return detailURL;
	}
	
}
