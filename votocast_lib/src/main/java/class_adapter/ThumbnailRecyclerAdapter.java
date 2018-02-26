package class_adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import com.votocast.votocast.R;
import viewHolder.ThumbnailViewHolder;

public class ThumbnailRecyclerAdapter extends RecyclerView.Adapter<ThumbnailViewHolder> {

    private List<Bitmap> bitmapList;
    int width;
    long duration;

    public ThumbnailRecyclerAdapter(List<Bitmap> bitmapList,int width,long duration) {
        this.bitmapList = bitmapList;
        this.width = width;
        this.duration = duration+1;
//        Log.i("duration thumb recycler",this.duration+"");
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vc_list_item_thumbnail, parent, false);
        return new ThumbnailViewHolder(v);
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
        notifyDataSetChanged();
    }

    public void addBitmap(Bitmap bitmap) {
        bitmapList.add(bitmap);
        notifyDataSetChanged();

//        Log.i("size",bitmapList.size() +"-"+getItemCount());


//        for(int index=0; index < bitmapList.size(); index++) {
//            if(index == duration) {
//                //currElement is the last element
////                Log.i("last","last item");
//                Bitmap bmp = Bitmap.createBitmap(width/30, 100, Bitmap.Config.ARGB_4444);
//                bitmapList.add(bitmapList.size()-1,bmp);
//                bitmapList.add(bitmapList.size()-2,bmp);
//                notifyDataSetChanged();
//            }
//        }
    }

    @Override
    public void onBindViewHolder(final ThumbnailViewHolder holder, int position) {

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((width/30),100);
//        holder.getmImageView().setLayoutParams(layoutParams);
//        holder.getmImageView().post(new Runnable() {
//            @Override
//            public void run() {
////                Log.i("TEST", "inner layout width : " + holder.getmImageView().getWidth());
//            }
//        });

        holder.getmImageView().setImageBitmap(bitmapList.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    public void clear() {
        bitmapList.clear();
        notifyDataSetChanged();
    }
}