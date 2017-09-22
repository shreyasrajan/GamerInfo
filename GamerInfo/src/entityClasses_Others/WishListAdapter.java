package entityClasses_Others;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import asynctask_and_parseHandlers.BitmapDownloadAsyncTask;
import com.example.gamerinfo.R;
import com.example.gamerinfo.R.layout;

public class WishListAdapter extends ArrayAdapter<GameEntity> {
    
	Context context;
	ArrayList<GameEntity> objects;
	GameEntity gameObj;
	ImageView imVwThumbNail;
	
	public WishListAdapter(Context context, ArrayList<GameEntity> objects) {
		super(context, layout.wishlist_custom_layout, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		WishListViewHolder holder;
		BitmapDownloadAsyncTask bmpAsync;
		
		try{
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(layout.wishlist_custom_layout, parent,false);
				
				holder = new WishListViewHolder();
				holder.tvGame = (TextView)convertView.findViewById(R.id.tvWLListGameName);
				holder.tvReleaseDate = (TextView)convertView.findViewById(R.id.tvWLListRelDateValue);
				holder.tvPlatforms = (TextView)convertView.findViewById(R.id.tvWLListPlatformValue);
				holder.imVwThumbNail = (ImageView) convertView.findViewById(R.id.imWLVwThumbNail);
				
				convertView.setTag(holder);
			}
			else
				holder = (WishListViewHolder) convertView.getTag();
			
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
	
	public ArrayList<GameEntity> getObjects(){
		return this.objects;
	}
	
	static class WishListViewHolder{
		TextView tvGame;
		TextView tvReleaseDate;
		TextView tvPlatforms;
		ImageView imVwThumbNail;
	}

}
