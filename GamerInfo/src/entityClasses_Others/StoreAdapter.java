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

public class StoreAdapter extends ArrayAdapter<StoreEntity> {
	Context context;
	ArrayList<StoreEntity> objects;
	StoreEntity storeObj;
	ImageView imVwStThumb;
	
	public StoreAdapter(Context context,ArrayList<StoreEntity> objects) {
		super(context, layout.store_custom_layout, objects);
		this.context = context;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BitmapDownloadAsyncTask bmpAsync;
		StoreViewHolder holder;
		
		try{
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(layout.store_custom_layout, parent,false);
				
				holder = new StoreViewHolder();
				holder.tvStoreName = (TextView)convertView.findViewById(R.id.tvStoreName);
				holder.tvAddress1 = (TextView)convertView.findViewById(R.id.tvStoreAddress1);
				holder.tvCity = (TextView)convertView.findViewById(R.id.tvStoreCity);
				holder.tvState = (TextView)convertView.findViewById(R.id.tvStoreState);
				holder.tvZip = (TextView)convertView.findViewById(R.id.tvStoreZip);
				holder.tvPhone = (TextView)convertView.findViewById(R.id.tvStorePhone);
				holder.tvRating = (TextView)convertView.findViewById(R.id.tvStoreRatingVal);
				holder.imVwThumbNail = (ImageView) convertView.findViewById(R.id.imVwStoreThumb);
				
				convertView.setTag(holder);
			}
			else
				holder = (StoreViewHolder) convertView.getTag();
			
			storeObj = objects.get(position);
			
			holder.tvStoreName.setText(storeObj.getStoreName());
			holder.tvAddress1.setText(storeObj.getAddress1());
			holder.tvCity.setText(storeObj.getCity());
			holder.tvState.setText(storeObj.getState());
			holder.tvZip.setText(storeObj.getZip());
			holder.tvPhone.setText(storeObj.getPhone());
			holder.tvRating.setText(storeObj.getRating());
			if(holder.imVwThumbNail != null){
				bmpAsync = new BitmapDownloadAsyncTask(holder.imVwThumbNail);
				bmpAsync.execute(storeObj.getThumbUrl());
			}
		}
		catch(Exception Ex){
			Ex.printStackTrace();
		}
		
		return convertView;
	}
	
	static class StoreViewHolder{
		TextView tvStoreName;
		TextView tvAddress1;
		TextView tvCity;
		TextView tvState;
		TextView tvZip;
		TextView tvPhone;
		TextView tvRating;
		ImageView imVwThumbNail;
	}
}
