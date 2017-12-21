package viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


import butterknife.BindView;
import butterknife.ButterKnife;
import com.votocast.votocast.R;
import com.votocast.votocast.R2;

public class ThumbnailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.iv_list_item_thumbnail)
    ImageView mImageView;

    public ThumbnailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getmImageView() {
        return mImageView;
    }
}