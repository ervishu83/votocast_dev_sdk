package class_adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import db.NewestVideo;
import fragments.ExploreFragment;

import com.squareup.picasso.Picasso;
import com.votocast.votocast.R;

/**
 * Created by Chirag on 4/28/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int actionbarHeight;
    ArrayList<NewestVideo> database;

    public ImageAdapter(Context c, int actionbarHeight, ArrayList<NewestVideo> database) {
        mContext = c;
        this.actionbarHeight = actionbarHeight;
        this.database = database;
    }

    public int getCount() {
        return database.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(actionbarHeight / 3, actionbarHeight / 3));
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setAdjustViewBounds(true);
//            imageView.setPadding(0, 0, 0, 0);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
////        imageView.setImageResource(mThumbIds[position]);
//
//        if (database.get(position).getImagePath() != null && !database.get(position).getImagePath().equals(""))
//            Picasso.with(mContext).load(database.get(position).getImagePath()).into(imageView);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Holder holder = new Holder();
        View rowView;

        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vc_newest_video_layout, parent, false);
        holder.newestVideoImageview = (ImageView) rowView.findViewById(R.id.newestVideoImageview);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(actionbarHeight / 3, actionbarHeight / 3);

        holder.newestVideoImageview.setLayoutParams(lp);
//        holder.newestVideoImageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        holder.newestVideoImageview.setAdjustViewBounds(true);
//        holder.newestVideoImageview.setPadding(0, 0, 0, 0);
        holder.newestVideoImageview.setClickable(true);

        if (database.get(position).getImagePath() != null && !database.get(position).getImagePath().equals(""))
            Picasso.with(mContext).load(database.get(position).getImagePath()).placeholder(R.drawable.backimage).into(holder.newestVideoImageview);
//        holder.newestVideoImageview.setScaleType(ImageView.ScaleType.FIT_XY);

        holder.newestVideoImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExploreFragment fragment = new ExploreFragment();
                Bundle b1 = new Bundle();
                b1.putString("vidId", database.get(position).getId());
                b1.putString("list_type",database.get(position).getFrom());
                b1.putString("list_id", String.valueOf(database.get(position).getTypeId()));
                b1.putString("videotype","newest");
                b1.putString("pos", String.valueOf(database.get(position).getPosition()));
                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();
            }
        });

        return rowView;
    }

    public class Holder {
        ImageView newestVideoImageview;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.event1, R.drawable.event2,
            R.drawable.event3, R.drawable.event4,
            R.drawable.event5, R.drawable.event6,
            R.drawable.event2, R.drawable.event3,
            R.drawable.event1
    };
}
