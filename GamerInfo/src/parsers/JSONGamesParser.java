package parsers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import entityClasses_Others.GameDetailEntity;
import entityClasses_Others.GameEntity;
import entityClasses_Others.StringLibrary;

public class JSONGamesParser {

	@SuppressLint("SimpleDateFormat")
	public static ArrayList<GameEntity> parseGamesList(String strInput){
		ArrayList<GameEntity> arGameObj = null;
		JSONObject root;
		JSONArray jsonArrPlatform;
		JSONArray jsonGameArr;
		JSONObject jsonPlatform;
		JSONObject jsonImageObj;
		JSONObject jsonGameObj;
		GameEntity gameObj;
		int ID;
		String thumbNailURL;
		String gameName;
		String dateFromAPI;
		String origReleaseDate;
		String detailURL;
		String image;
		StringBuilder platformBuilder;
		SimpleDateFormat dateFormat;
		SimpleDateFormat outputDateFormat;
		Date releaseDate;

		try{
			root = new JSONObject(strInput);

			arGameObj = new ArrayList<GameEntity>();

			jsonGameArr = root.getJSONArray(StringLibrary.RESULTS);

			dateFormat = new SimpleDateFormat(StringLibrary.INPUT_DATE_FORMAT);
			outputDateFormat = new SimpleDateFormat(StringLibrary.OUTPUT_DATE_FORMAT);

			for(int j=0;j<jsonGameArr.length();j++){
				jsonGameObj = jsonGameArr.getJSONObject(j);
				detailURL = jsonGameObj.optString(StringLibrary.API_DETAIL_URL);
				ID = jsonGameObj.optInt(StringLibrary.ID);
				gameName = jsonGameObj.optString(StringLibrary.NAME);

				dateFromAPI = jsonGameObj.optString(StringLibrary.RELEASE_DATE);
				if(dateFromAPI == StringLibrary.NULL_STRING)
					origReleaseDate = StringLibrary.DATE_NOT_FOUND;
				else{
					releaseDate = dateFormat.parse(dateFromAPI);
					origReleaseDate = outputDateFormat.format(releaseDate);
				}

				image = jsonGameObj.optString(StringLibrary.IMAGE);
				if(image != StringLibrary.NULL_STRING){
					jsonImageObj = jsonGameObj.getJSONObject(StringLibrary.IMAGE);
					thumbNailURL = jsonImageObj.optString(StringLibrary.THUMBNAIL_URL);
				}
				else
					thumbNailURL = StringLibrary.NULL_STRING;

				jsonArrPlatform = jsonGameObj.getJSONArray(StringLibrary.PLATFORMS);
				platformBuilder = new StringBuilder();
				for(int i=0;i<jsonArrPlatform.length();i++){
					jsonPlatform = jsonArrPlatform.getJSONObject(i);
					platformBuilder.append(jsonPlatform.optString(StringLibrary.PLATFORM_ABBREVIATION));
					platformBuilder.append(",");
				}
				platformBuilder.deleteCharAt(platformBuilder.length()-1);

				gameObj = new GameEntity(ID, thumbNailURL, gameName, origReleaseDate, platformBuilder.toString(), detailURL);

				arGameObj.add(gameObj);
			}

		}
		catch(Exception Ex){
			Ex.printStackTrace();
		}

		return arGameObj;
	}

	@SuppressLint("SimpleDateFormat")
	public static GameDetailEntity parseGame(String strInput){
		GameDetailEntity gameDetailObj = null;
		JSONObject root;
		JSONObject jsonGameDetObj;
		JSONArray jsonArrPlatform;
		JSONArray jsonGenres;
		JSONArray jsonRatings;
		JSONArray jsonDevelopers;
		JSONArray jsonPublishers;
		JSONObject jsonImageObj;
		int ID;
		String gameName;
		String image;
		String mediumURL;
		String ratings;
		String ratingsNew;
		String platforms;
		String platformsNew;
		String genres;
		String genresNew;
		String developers;
		String developersNew;
		String publishers;
		String publishersNew;
		String dateFromAPI;
		String origReleaseDate;
		SimpleDateFormat dateFormat;
		SimpleDateFormat outputDateFormat;
		Date releaseDate;
		
		try{
			root = new JSONObject(strInput);
            
			jsonGameDetObj = root.getJSONObject(StringLibrary.RESULTS);
			
			ID = jsonGameDetObj.optInt(StringLibrary.ID);
            
			gameName = jsonGameDetObj.optString(StringLibrary.NAME);
			
			image = jsonGameDetObj.optString(StringLibrary.IMAGE);
			if(image != StringLibrary.NULL_STRING){
				jsonImageObj = jsonGameDetObj.getJSONObject(StringLibrary.IMAGE);
				mediumURL = jsonImageObj.optString(StringLibrary.MEDIUM_URL);
			}
			else
				mediumURL = StringLibrary.NULL_STRING;
            
			dateFormat = new SimpleDateFormat(StringLibrary.INPUT_DATE_FORMAT);
			outputDateFormat = new SimpleDateFormat(StringLibrary.OUTPUT_DATE_FORMAT);
			dateFromAPI = jsonGameDetObj.optString(StringLibrary.RELEASE_DATE);
			if(dateFromAPI == StringLibrary.NULL_STRING)
				origReleaseDate = StringLibrary.DATE_NOT_FOUND;
			else{
				releaseDate = dateFormat.parse(dateFromAPI);
				origReleaseDate = outputDateFormat.format(releaseDate);
			}
			
			ratings = jsonGameDetObj.optString(StringLibrary.GAME_RATINGS);
			if(ratings != StringLibrary.NULL_STRING){
				jsonRatings = jsonGameDetObj.getJSONArray(StringLibrary.GAME_RATINGS);
				ratingsNew = getString(jsonRatings,StringLibrary.NAME);
			}
			else
				ratingsNew = StringLibrary.DATE_NOT_FOUND;
			
			platforms = jsonGameDetObj.optString(StringLibrary.PLATFORMS);
			if(platforms != StringLibrary.NULL_STRING){
				jsonArrPlatform = jsonGameDetObj.getJSONArray(StringLibrary.PLATFORMS);
				platformsNew = getString(jsonArrPlatform,StringLibrary.PLATFORM_ABBREVIATION);
			}
			else
				platformsNew = StringLibrary.DATE_NOT_FOUND;
			
			developers = jsonGameDetObj.optString(StringLibrary.DEVELOPERS);
			if(developers != StringLibrary.NULL_STRING){
				jsonDevelopers = jsonGameDetObj.getJSONArray(StringLibrary.DEVELOPERS);
				developersNew = getString(jsonDevelopers,StringLibrary.NAME);
			}
			else
				developersNew = StringLibrary.DATE_NOT_FOUND;
			
			genres = jsonGameDetObj.optString(StringLibrary.GENRES);
			if(genres != StringLibrary.NULL_STRING){
				jsonGenres = jsonGameDetObj.getJSONArray(StringLibrary.GENRES);
				genresNew = getString(jsonGenres,StringLibrary.NAME);
			}
			else
				genresNew = StringLibrary.DATE_NOT_FOUND;
			
			publishers = jsonGameDetObj.optString(StringLibrary.PUBLISHERS);
			if(publishers != StringLibrary.NULL_STRING){
				jsonPublishers = jsonGameDetObj.getJSONArray(StringLibrary.PUBLISHERS);
				publishersNew = getString(jsonPublishers,StringLibrary.NAME);
			}
			else
				publishersNew = StringLibrary.DATE_NOT_FOUND;
			
			gameDetailObj = new GameDetailEntity(ID, gameName, mediumURL, origReleaseDate, platformsNew, 
					                             genresNew, ratingsNew, developersNew, publishersNew);
		}
		catch(Exception Ex){
			Ex.printStackTrace();
		}

		return gameDetailObj;
	}

	private static String getString(JSONArray jsonArray,String value){
		StringBuilder stringBuilder = null;
		
		try{
			stringBuilder = new StringBuilder();
			for(int i=0;i<jsonArray.length();i++){
				stringBuilder.append(jsonArray.getJSONObject(i).optString(value));
				stringBuilder.append(",");
			}
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
		}
		catch(Exception E){
			E.printStackTrace();
		}
		
		return stringBuilder.toString();
	}
}
