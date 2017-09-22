package entityClasses_Others;

public class StoreEntity {

	String storeName;
	String address1;
	String city;
	String state;
	String zip;
	String phone;
	String rating;
	String thumbUrl;
	String url;
	
	public StoreEntity(String storeName, String address1, String city,
			String state, String zip, String phone, String rating,
			String thumbUrl,String url) {
		super();
		this.storeName = storeName;
		this.address1 = address1;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
		this.rating = rating;
		this.thumbUrl = thumbUrl;
		this.url = url;
	}

	public String getStoreName() {
		return storeName;
	}

	public String getAddress1() {
		return address1;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}
	
	public String getPhone() {
		return phone;
	}

	public String getRating() {
		return rating;
	}

	public String getUrl() {
		return url;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}
}
