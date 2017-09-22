package parsers;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import entityClasses_Others.StoreEntity;
import entityClasses_Others.StringLibrary;

public class JSONStoresParser {

	public static ArrayList<StoreEntity> parseStoreList(String strInput){
		ArrayList<StoreEntity> arStoreObj = null;
		StoreEntity storeObj;
		JSONObject root;
		JSONArray jsonArrResults;
		JSONObject jsonStoreObj;
		String storeName;
		String address1;
		String city;
		String state;
		String zip;
		String phone;
		String rating;
		String thumbURL;
		String url;
		
		try{
			root = new JSONObject(strInput);
			
			arStoreObj = new ArrayList<StoreEntity>();
			
			jsonArrResults = root.getJSONArray(StringLibrary.YELP_BUSINESSES);
			for(int i=0;i<jsonArrResults.length();i++){
				jsonStoreObj = jsonArrResults.getJSONObject(i);
				
				storeName = jsonStoreObj.optString(StringLibrary.YELP_STORE_NAME);
				address1 = jsonStoreObj.optString(StringLibrary.YELP_STORE_ADDRESS1);
				city = jsonStoreObj.optString(StringLibrary.YELP_STORE_CITY);
				state = jsonStoreObj.optString(StringLibrary.YELP_STORE_STATE);
				zip = jsonStoreObj.optString(StringLibrary.YELP_STORE_ZIP);
				phone = jsonStoreObj.optString(StringLibrary.YELP_STORE_PHONE);
				
				rating = jsonStoreObj.optString(StringLibrary.YELP_STORE_RATING);
				thumbURL = jsonStoreObj.optString(StringLibrary.YELP_STORE_THUMB_URL);
				url = jsonStoreObj.optString(StringLibrary.YELP_STORE_DETAIL_URL);
				
				storeObj = new StoreEntity(storeName, address1, city + ", ", state + " ", zip, phone, rating + "/5", thumbURL, url);
				
				arStoreObj.add(storeObj);
			}
		}
		catch(Exception Ex){
			Ex.printStackTrace();
		}
		
		return arStoreObj;
	}
}
