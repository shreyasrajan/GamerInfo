package entityClasses_Others;

public class GameDetailEntity {

	int ID;
	String gameName;
	String mediumImageURL;
	String releaseDate;
	String platform;
	String genres;
	String ratings;
	String developers;
	String publishers;

	public GameDetailEntity(int iD, String gameName, String mediumImageURL,
			String releaseDate, String platform, String genres, String ratings,
			String developers, String publishers) {
		super();
		ID = iD;
		this.gameName = gameName;
		this.mediumImageURL = mediumImageURL;
		this.releaseDate = releaseDate;
		this.platform = platform;
		this.genres = genres;
		this.ratings = ratings;
		this.developers = developers;
		this.publishers = publishers;
	}

	public int getID() {
		return ID;
	}

	public String getGameName() {
		return gameName;
	}

	public String getMediumImageURL() {
		return mediumImageURL;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getPlatform() {
		return platform;
	}

	public String getGenres() {
		return genres;
	}

	public String getRatings() {
		return ratings;
	}

	public String getDevelopers() {
		return developers;
	}

	public String getPublishers() {
		return publishers;
	}

}
