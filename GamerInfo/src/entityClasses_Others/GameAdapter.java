package entityClasses_Others;

import java.util.ArrayList;
import com.example.gamerinfo.R;
import com.example.gamerinfo.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import asynctask_and_parseHandlers.BitmapDownloadAsyncTask;

public class GameAdapter extends ArrayAdapter<GameEntity> {
    
	Context context;
	ArrayList<GameEntity> objects;
	GameEntity gameObj;
	ImageView imVwThumbNail;
	
	public GameAdapter(Context context, ArrayList<GameEntity> objects) {
		super(context, layout.games_custom_layout, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		GameViewHolder holder;
		BitmapDownloadAsyncTask bmpAsync;
		
		try{
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(layout.games_custom_layout, parent,false);
				
				holder = new GameViewHolder();
				holder.tvGame = (TextView)convertView.findViewById(R.id.tvListGameName);
				holder.tvReleaseDate = (TextView)convertView.findViewById(R.id.tvListRelDateValue);
				holder.tvPlatforms = (TextView)convertView.findViewById(R.id.tvListPlatformValue);
				holder.imVwThumbNail = (ImageView) convertView.findViewById(R.id.imVwThumbNail);
				
				convertView.setTag(holder);
			}
			else
				holder = (GameViewHolder) convertView.getTag();
			
			gameObj = objects.get(position);
			
			holder.tvGame.setText(gameObj.getGameName());
			holder.tvReleaseDate.setText(gameObj.getOrigRelDate());
			holder.tvPlatforms.setText(gameObj.getPlatform());
			if(holder.imVwThumbNail != null){
				bmpAsync = new BitmapDownloadAsyncTask(holder.imVwThumbNail);
				bmpAsync.execute(gameObj.getThumbNailUrl());
			}
				
		}
		catch(Exception Ex){
			Ex.printStackTrace();
		}
		
		return convertView;
	}
	
	static class GameViewHolder{
		TextView tvGame;
		TextView tvReleaseDate;
		TextView tvPlatforms;
		ImageView imVwThumbNail;
	}

}
