package parsers;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import entityClasses_Others.ReviewEntity;
import entityClasses_Others.StringLibrary;

public class JSONReviewsParser {

	public static ArrayList<ReviewEntity> parseReviews(String strInput){
		ArrayList<ReviewEntity> revArr = null;
		JSONObject root;
		JSONArray jsonRevArr;
		JSONObject jsonRevGameObj;
		JSONObject jsonReviewObj;
		ReviewEntity revObj;
		String detailURL;
		String gameName;
		String review;
		String platforms;
		String score;
		
		try{
			root = new JSONObject(strInput);
			
			revArr = new ArrayList<ReviewEntity>();
			
			jsonRevArr = root.getJSONArray(StringLibrary.RESULTS);
			for(int i=0;i<jsonRevArr.length();i++){
				jsonReviewObj = jsonRevArr.getJSONObject(i);
				
				review = jsonReviewObj.optString(StringLibrary.REVIEW_DECK);
				
				jsonRevGameObj = jsonReviewObj.getJSONObject(StringLibrary.REVIEW_GAME);
				gameName = jsonRevGameObj.optString(StringLibrary.NAME);
				detailURL = jsonRevGameObj.optString(StringLibrary.REVIEW_DETAIL_URL);
				
				score = jsonReviewObj.optString(StringLibrary.REVIEW_SCORE);
				
				platforms = jsonReviewObj.optString(StringLibrary.REVIEW_PLATFORMS);
				
				revObj = new ReviewEntity(detailURL, gameName, review, platforms, score);
				
				revArr.add(revObj);
			}
		}
		catch(Exception Ex){
			Ex.printStackTrace();
		}
		
		return revArr;
	}
}
