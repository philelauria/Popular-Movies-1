package codes.pelauria.popular_movies_1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviePosterImageAdapter extends BaseAdapter {
    private Context mContext;
    public final List<String> urls = new ArrayList<String>();

    public MoviePosterImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(700, 750));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(85, 80, 85, 80);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(getItem(position))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView);

        return imageView;
    }
}
