package class_adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apradanas.simplelinkabletext.Link;
import com.apradanas.simplelinkabletext.LinkableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import db.LeaderBoard;
import fragments.CampaignFragment;
import fragments.ExploreFragment;
import fragments.ProfileFragment;
import com.votocast.votocast.R;

/**
 * Created by Anil on 5/25/2016.
 */
public class SearchListAdapter extends BaseAdapter {

    ArrayList<LeaderBoard> datalist;
    Context context;
    Holder holder;

    public SearchListAdapter(Context context, ArrayList<LeaderBoard> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        Log.i("size",datalist.size()+"");
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
        public TextView tvListLeaderboardName, tvListLeaderboardFollower, tvListLeaderboardLike, tvListLeaderboardPlay, tvListLeaderboardShare;
        public ImageView ivListLeaderboardImage,ivListBanner;
        public LinkableTextView tvListLeaderboardDesc;
        RelativeLayout rlVideoClickLink;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

//        if (convertView == null) {
            holder = new Holder();

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leaderboard_recyclerview, parent, false);

            holder.rlVideoClickLink = (RelativeLayout) convertView.findViewById(R.id.rlVideoClickLink);

            holder.ivListBanner = (ImageView) convertView.findViewById(R.id.ivListBanner);
            holder.ivListLeaderboardImage = (ImageView) convertView.findViewById(R.id.ivListLeaderboardImage);
            holder.tvListLeaderboardName = (TextView) convertView.findViewById(R.id.tvListLeaderboardName);
            holder.tvListLeaderboardFollower = (TextView) convertView.findViewById(R.id.tvListLeaderboardFollower);
            holder.tvListLeaderboardDesc = (LinkableTextView) convertView.findViewById(R.id.tvListLeaderboardDesc);
            holder.tvListLeaderboardLike = (TextView) convertView.findViewById(R.id.tvListLeaderboardLike);
            holder.tvListLeaderboardPlay = (TextView) convertView.findViewById(R.id.tvListLeaderboardPlay);
            holder.tvListLeaderboardShare = (TextView) convertView.findViewById(R.id.tvListLeaderboardShare);
//            convertView.setTag(holder);
//
//        } else
//            holder = (Holder) convertView.getTag();

//        Holder holder = new Holder();
//        View v;
//        v = inflater.inflate(R.layout.list_leaderboard_recyclerview, null);

        Link linkHashtag = new Link(Pattern.compile("(#\\w+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        CampaignFragment fragment = new CampaignFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("campId", datalist.get(position).getCampId());
//                        b1.putString("from","search");
//                        b1.putString("fromId",datalist.get(position).getCampId());
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.addToBackStack(fragment.getClass().getName());
//                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                    }
                });

        Link linkVotocastTag = new Link(Pattern.compile("(VC_\\w+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        CampaignFragment fragment = new CampaignFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("campId", datalist.get(position).getCampId());
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.addToBackStack(fragment.getClass().getName());
                        ft.commit();
                    }
                });

        Link linkUsername = new Link(Pattern.compile("(@\\w+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        // do something
                    }
                });

        List<Link> links = new ArrayList<>();
        links.add(linkHashtag);
        links.add(linkVotocastTag);
        links.add(linkUsername);

        // set fonts
        Constant.setTextFontsMedium(context, holder.tvListLeaderboardName);
        Constant.setTextFontsRegular(context, holder.tvListLeaderboardFollower);
        Constant.setTextFontsRegular(context, holder.tvListLeaderboardDesc);
        Constant.setTextFontsRegular(context, holder.tvListLeaderboardLike);
        Constant.setTextFontsRegular(context, holder.tvListLeaderboardPlay);
        Constant.setTextFontsRegular(context, holder.tvListLeaderboardShare);

        // set data

        if (datalist.get(position).getImageid() != null && !datalist.get(position).getImageid().equals(""))
            Picasso.with(context).load(datalist.get(position).getImageid()).placeholder(R.drawable.backimage).into(holder.ivListLeaderboardImage);
        else
            Picasso.with(context).load(R.drawable.backimage).into(holder.ivListLeaderboardImage);
        holder.ivListLeaderboardImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if(Integer.parseInt(datalist.get(position).getRank()) <= 10) {
            holder.ivListBanner.setVisibility(View.VISIBLE);
            if(datalist.get(position).getWinner().equals("yes"))
                holder.ivListBanner.setImageResource(R.drawable.winner_small);
            else {
                switch (Integer.parseInt(datalist.get(position).getRank())) {
                    case 1:
                        holder.ivListBanner.setImageResource(R.drawable.rank1list);
                        break;
                    case 2:
                        holder.ivListBanner.setImageResource(R.drawable.rank2list);
                        break;
                    case 3:
                        holder.ivListBanner.setImageResource(R.drawable.rank3list);
                        break;
                    case 4:
                        holder.ivListBanner.setImageResource(R.drawable.rank4list);
                        break;
                    case 5:
                        holder.ivListBanner.setImageResource(R.drawable.rank5list);
                        break;
                    case 6:
                        holder.ivListBanner.setImageResource(R.drawable.rank6list);
                        break;
                    case 7:
                        holder.ivListBanner.setImageResource(R.drawable.rank7list);
                        break;
                    case 8:
                        holder.ivListBanner.setImageResource(R.drawable.rank8list);
                        break;
                    case 9:
                        holder.ivListBanner.setImageResource(R.drawable.rank9list);
                        break;
                    case 10:
                        holder.ivListBanner.setImageResource(R.drawable.topsmall);
                        break;
                }
            }

        }
        else
            holder.ivListBanner.setVisibility(View.GONE);

        holder.tvListLeaderboardName.setText(datalist.get(position).getName());
        holder.tvListLeaderboardFollower.setText(datalist.get(position).getFolloers());

        holder.tvListLeaderboardDesc.setText(datalist.get(position).getDesc()).addLinks(links).build();

        holder.tvListLeaderboardLike.setText(datalist.get(position).getLike());
        holder.tvListLeaderboardPlay.setText(datalist.get(position).getPlay());
        holder.tvListLeaderboardShare.setText(datalist.get(position).getShare());

//        Log.i("camp id", datalist.get(position).getCampId());
        holder.tvListLeaderboardName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment fragment = new ProfileFragment();
                Bundle b1 = new Bundle();
                b1.putString("user_id", datalist.get(position).getUserId());
//                b1.putString("from","search");
//                b1.putString("fromId",datalist.get(position).getCampId());
                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(fragment.getClass().getName());
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        holder.rlVideoClickLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExploreFragment fragment = new ExploreFragment();
                Bundle b1 = new Bundle();
                b1.putString("vidId", datalist.get(position).getVidId());
                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();
            }
        });

        holder.ivListLeaderboardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExploreFragment fragment = new ExploreFragment();
                Bundle b1 = new Bundle();
                b1.putString("vidId", datalist.get(position).getVidId());
//                b1.putString("from", datalist.get(position).getFrom());
//                if (datalist.get(position).getFrom().equals("camp"))
//                    b1.putString("fromId", datalist.get(position).getCampId());
//                else if (datalist.get(position).getFrom().equals("search"))
//                    b1.putString("fromId", datalist.get(position).getCampId());
//                else if (datalist.get(position).getFrom().equals("profile"))
//                    b1.putString("fromId", datalist.get(position).getUserId());
                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(fragment.getClass().getName());
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        if (position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.list));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.whiteText));
        }
        return convertView;
    }
}
