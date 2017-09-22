package asynctask_and_parseHandlers;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.gamerinfo.R;

import entityClasses_Others.StringLibrary;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapDownloadAsyncTask extends AsyncTask<String, Void, Bitmap>{

	ImageView imVwThumbnail;
	
	public BitmapDownloadAsyncTask(ImageView imVwThumbnail){
		this.imVwThumbnail = imVwThumbnail;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		if(params[0].equals(StringLibrary.NULL_STRING))
			return null;
		else{
			Bitmap bmp = GetImageBitmap(params[0]);
			return bmp;
		}
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);

		if(result != null)
			this.imVwThumbnail.setImageBitmap(result);
		else
			this.imVwThumbnail.setImageResource(R.drawable.poster_not_found);
	}

	private Bitmap GetImageBitmap(String strPath){

		URL url;
		HttpURLConnection conn;
		InputStream inputStream;
		Bitmap bmp = null;

		try{
			url = new URL(strPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			inputStream = conn.getInputStream();
			bmp = BitmapFactory.decodeStream(inputStream);
		}
		catch(Exception Ex){
			Ex.printStackTrace();
		}

		return bmp;
	}
}
