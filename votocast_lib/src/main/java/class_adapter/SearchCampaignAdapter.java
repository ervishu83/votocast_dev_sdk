package class_adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.votocast.votocast.R;

import java.util.ArrayList;

import db.SearchCampaign;
import fragments.CampaignFragment;

/**
 * Created by Admin on 5/26/2017.
 */

public class SearchCampaignAdapter extends BaseAdapter {

    ArrayList<SearchCampaign> datalist;
    Context context;
    Holder holder;

    public SearchCampaignAdapter(Context context, ArrayList<SearchCampaign> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        public TextView tvSearchCompaignFragmentVideoCount,  tvSearchCompaignFragmentProducersCount,  tvSearchCompaignFragmentFollowingCount;
        public TextView tvSearchFragmentTitle1,tvSearchFragmentTitle2,tvSearchFragmentTitle3;
        public ImageView ivSearchCampaignMainImage;
        public Button btnSearchCampaignFragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
        holder = new Holder();

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_campaign_list_item, parent, false);

        holder.ivSearchCampaignMainImage = (ImageView) convertView.findViewById(R.id.ivSearchCampaignMainImage);
        holder.btnSearchCampaignFragment = (Button) convertView.findViewById(R.id.btnSearchCampaignFragment);
        holder.tvSearchCompaignFragmentVideoCount = (TextView) convertView.findViewById(R.id.tvSearchCompaignFragmentVideoCount);
        holder.tvSearchCompaignFragmentProducersCount = (TextView) convertView.findViewById(R.id.tvSearchCompaignFragmentProducersCount);
        holder.tvSearchCompaignFragmentFollowingCount = (TextView) convertView.findViewById(R.id.tvSearchCompaignFragmentFollowingCount);
        holder.tvSearchFragmentTitle1 = (TextView) convertView.findViewById(R.id.tvSearchFragmentTitle1);
        holder.tvSearchFragmentTitle2 = (TextView) convertView.findViewById(R.id.tvSearchFragmentTitle2);
        holder.tvSearchFragmentTitle3 = (TextView) convertView.findViewById(R.id.tvSearchFragmentTitle3);
            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();

//        Holder holder = new Holder();
//        View v;
//        v = inflater.inflate(R.layout.list_leaderboard_recyclerview, null);

        Drawable background = holder.btnSearchCampaignFragment.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(Constant.colorPrimary));
        }

        // set fonts
        Constant.setTextFontsSemibold(context, holder.tvSearchCompaignFragmentVideoCount);
        Constant.setTextFontsSemibold(context, holder.tvSearchCompaignFragmentProducersCount);
        Constant.setTextFontsSemibold(context, holder.tvSearchCompaignFragmentFollowingCount);
        Constant.setTextFontsRegular(context, holder.tvSearchFragmentTitle1);
        Constant.setTextFontsRegular(context, holder.tvSearchFragmentTitle2);
        Constant.setTextFontsRegular(context, holder.tvSearchFragmentTitle3);
        Constant.setTextFontsRegular(context, holder.btnSearchCampaignFragment);

        // set data

        holder.tvSearchCompaignFragmentVideoCount.setText(datalist.get(position).getVideos());
        holder.tvSearchCompaignFragmentProducersCount.setText(datalist.get(position).getUsers());
        holder.tvSearchCompaignFragmentFollowingCount.setText(datalist.get(position).getFollowers());
        holder.btnSearchCampaignFragment.setText(datalist.get(position).getTitle());

        Glide.with(context).load(datalist.get(position).getPhotoPath())
                .placeholder(context.getResources().getColor(R.color.tabColor))
                .error(context.getResources().getColor(R.color.tabColor))
                .into(holder.ivSearchCampaignMainImage);
        holder.ivSearchCampaignMainImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        holder.btnSearchCampaignFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CampaignFragment fragment = new CampaignFragment();
                Bundle b1 = new Bundle();
                b1.putString("campId", datalist.get(position).getId());
                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();
            }
        });

        return convertView;
    }
}

