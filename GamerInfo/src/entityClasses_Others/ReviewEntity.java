package entityClasses_Others;

public class ReviewEntity {

	String detailURL;
	String gameName;
	String review;
	String platforms;
	String score;
	
	public ReviewEntity(String detailURL, String gameName, String review,
			String platforms, String score) {
		super();
		this.detailURL = detailURL;
		this.gameName = gameName;
		this.review = review;
		this.platforms = platforms;
		this.score = score;
	}

	public String getDetailURL() {
		return detailURL;
	}
	
	public String getGameName() {
		return gameName;
	}

	public String getReview() {
		return review;
	}

	public String getPlatforms() {
		return platforms;
	}

	public String getScore() {
		return score;
	}
	
}
