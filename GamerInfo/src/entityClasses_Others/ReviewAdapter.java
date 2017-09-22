package entityClasses_Others;

import java.util.ArrayList;
import com.example.gamerinfo.R;
import com.example.gamerinfo.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReviewAdapter extends ArrayAdapter<ReviewEntity> {

	Context context;
	ArrayList<ReviewEntity> objects;
	ReviewEntity reviewObj;
	
	public ReviewAdapter(Context context, ArrayList<ReviewEntity> objects) {
		super(context, layout.reviews_custom_layout, objects);
		this.context = context;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ReviewHolder holder;
		
		try{
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(layout.reviews_custom_layout, parent,false);
				
				holder = new ReviewHolder();
				holder.tvGame = (TextView)convertView.findViewById(R.id.tvListGameName);
				holder.tvReview = (TextView)convertView.findViewById(R.id.tvReviewVal);
				holder.tvPlatforms = (TextView)convertView.findViewById(R.id.tvRevPlatformVal);
				holder.tvScore = (TextView)convertView.findViewById(R.id.tvRevScoreVal);
				
				convertView.setTag(holder);
			}
			else
				holder = (ReviewHolder) convertView.getTag();
			
			reviewObj = objects.get(position);
			
			holder.tvGame.setText(reviewObj.getGameName());
			holder.tvReview.setText(reviewObj.getReview());
			holder.tvPlatforms.setText(reviewObj.getPlatforms());
			holder.tvScore.setText(reviewObj.getScore());
				
		}
		catch(Exception Ex){
			Ex.printStackTrace();
		}
		
		return convertView;
	}
	
	static class ReviewHolder{
		TextView tvGame;
		TextView tvReview;
		TextView tvPlatforms;
		TextView tvScore;
	}
}
