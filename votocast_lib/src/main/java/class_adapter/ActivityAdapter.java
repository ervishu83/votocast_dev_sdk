package class_adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apradanas.simplelinkabletext.Link;
import com.apradanas.simplelinkabletext.LinkableTextView;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import db.ActivityClass;
import de.hdodenhof.circleimageview.CircleImageView;
import fragments.CampaignFragment;
import fragments.ExploreFragment;
import fragments.ProfileFragment;
import com.votocast.votocast.R;

import org.json.JSONException;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Anil on 5/30/2016.
 */
public class ActivityAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    ArrayList<ActivityClass> datalist;
    Activity mActivity;
    Holder holder;
    String myId;
    private SparseBooleanArray mSelectedItemsIds;

    public ActivityAdapter(String myId, Activity mActivity, ArrayList<ActivityClass> datalist) {
        this.myId = myId;
        this.mActivity = mActivity;
        this.datalist = datalist;
        mSelectedItemsIds = new SparseBooleanArray();
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

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_header_item, parent, false);
            holder.tvSearchHeader = (TextView) convertView.findViewById(R.id.tvSearchHeader);
            holder.llSearchHeader = (LinearLayout) convertView.findViewById(R.id.llSearchHeader);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        Constant.setDisplayFontsBold(mActivity, holder.tvSearchHeader);

        if (datalist.get(position).getNodeId() == 1) {
            holder.tvSearchHeader.setText("CAMPAIGN");
//            Log.i("if", datalist.get(position).toString());
        } else if (datalist.get(position).getNodeId() == 2) {
//            Log.i("else if", datalist.get(position).toString());
            holder.tvSearchHeader.setText("PRODUCERS");
        } else if (datalist.get(position).getNodeId() == 3) {
//            Log.i("else", datalist.get(position).toString());
            holder.tvSearchHeader.setText("VOTES + SHARES");
        }else if (datalist.get(position).getNodeId() == 4) {
//            Log.i("else", datalist.get(position).toString());
            holder.tvSearchHeader.setText("NOTIFICATION");
        }


        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return datalist.get(position).getNodeId();
    }


    class HeaderViewHolder {
        TextView tvSearchHeader;
        LinearLayout llSearchHeader;
    }

    public class Holder {
        CircleImageView civListItemActivityProfilePic;
        LinkableTextView tvListActivityText;
        ImageView ivActivityVideoPic, ivActivityActionIcon;
        TextView tvListActivitySimpleText;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        holder = new Holder();
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_activity, parent, false);

//        Log.i("MyId",myId);
        holder.tvListActivitySimpleText = (TextView) v.findViewById(R.id.tvListActivitySimpleText);
        holder.ivActivityVideoPic = (ImageView) v.findViewById(R.id.ivActivityVideoPic);
        holder.ivActivityActionIcon = (ImageView) v.findViewById(R.id.ivActivityActionIcon);
        holder.tvListActivityText = (LinkableTextView) v.findViewById(R.id.tvListActivityText);
        holder.civListItemActivityProfilePic = (CircleImageView) v.findViewById(R.id.civListItemActivityProfilePic);

        Constant.setDisplayFontsRegular(mActivity, holder.tvListActivityText);
        v.setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4 : Color.TRANSPARENT);

        //set data

        if (datalist.get(position).getNodeId() == 1) {
            if (!datalist.get(position).getMainUserPic().equals(""))
                Glide.with(mActivity).load(datalist.get(position).getMainUserPic()).placeholder(R.drawable.campaign_icon).into(holder.civListItemActivityProfilePic);
            else
                Glide.with(mActivity).load(R.drawable.campaign_icon).into(holder.civListItemActivityProfilePic);
        } else {
            if (!datalist.get(position).getMainUserPic().equals(""))
                Glide.with(mActivity).load(datalist.get(position).getMainUserPic()).placeholder(R.drawable.user_default).into(holder.civListItemActivityProfilePic);
            else
                Glide.with(mActivity).load(R.drawable.user_default).into(holder.civListItemActivityProfilePic);
        }

        Link linkHashtag = new Link(Pattern.compile("(#\\w+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        CampaignFragment fragment = new CampaignFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("campId", datalist.get(position).getOtherUserId());
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.addToBackStack(fragment.getClass().getName());
                        ft.commit();
                    }
                });

        Link linkVotocasttag = new Link(Pattern.compile("(VC_\\w+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        CampaignFragment fragment = new CampaignFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("campId", datalist.get(position).getOtherUserId());
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
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
                        ProfileFragment fragment = new ProfileFragment();
                        Bundle b1 = new Bundle();
                        if (myId.equals(datalist.get(position).getOtherUserId()))
                            b1.putString("user_id", datalist.get(position).getMainUserId());
                        else
                            b1.putString("user_id", datalist.get(position).getOtherUserId());
//                        b1.putString("user_id", datalist.get(position).getOtherUserId());
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.addToBackStack(fragment.getClass().getName());
                        ft.commit();
                    }
                });

        Link linkDatetag = new Link(Pattern.compile("(  \\w+)"))
                .setUnderlined(false)
                .setTextColor(mActivity.getResources().getColor(R.color.homeSubTitle));

        Link linkDashDatetag = new Link(Pattern.compile("(-\\w+)"))
                .setUnderlined(false)
                .setTextColor(mActivity.getResources().getColor(R.color.homeSubTitle));

        //http://www.vogella.com/tutorials/JavaRegularExpressions/article.html
        Link linkUrltag = new Link(Pattern.compile("(www.\\S+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                            Log.e("LINK Click",text);
                        String url = text;
                        Uri uri;
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            uri = Uri.parse("http://" + url);
                        }else
                            uri = Uri.parse(text);

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                        mActivity.startActivity(browserIntent);
                    }
                });

        List<Link> links = new ArrayList<>();
        links.add(linkHashtag);
        links.add(linkUsername);
        links.add(linkDatetag);
        links.add(linkDashDatetag);
        links.add(linkVotocasttag);
        links.add(linkUrltag);

        if (datalist.get(position).getNodeId() == 1) {
            holder.ivActivityVideoPic.setVisibility(View.GONE);
            holder.ivActivityActionIcon.setVisibility(View.VISIBLE);

            if (myId.equals(datalist.get(position).getMainUserId())) {
                if (datalist.get(position).getAction().equals("follow")) {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.award_orange);
                    holder.tvListActivityText.setText("You are following " + datalist.get(position).getOtherUsername() + "  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                } else {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.award_grey);
                    holder.tvListActivityText.setText("You are un following " + datalist.get(position).getOtherUsername() + "  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                }
            } else {
                if (datalist.get(position).getAction().equals("follow")) {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.award_orange);
                    holder.tvListActivityText.setText(datalist.get(position).getMainUserName() + " is following " + datalist.get(position).getOtherUsername() + "  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                } else {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.award_grey);
                    holder.tvListActivityText.setText(datalist.get(position).getMainUserName() + " is un following " + datalist.get(position).getOtherUsername() + "  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                }
            }
        } else if (datalist.get(position).getNodeId() == 2) {
            holder.ivActivityVideoPic.setVisibility(View.GONE);
            holder.ivActivityActionIcon.setVisibility(View.VISIBLE);

            if (myId.equals(datalist.get(position).getMainUserId())) {
                if (datalist.get(position).getAction().equals("follow")) {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.person_green);
                    holder.tvListActivityText.setText("You are following " + "@" + datalist.get(position).getOtherUsername() + "  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                } else {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.person_orange);
                    holder.tvListActivityText.setText("You are un following " + "@" + datalist.get(position).getOtherUsername() + "  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                }
            } else if (myId.equals(datalist.get(position).getOtherUserId())) {

                if (datalist.get(position).getAction().equals("follow")) {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.person_green);
                    holder.tvListActivityText.setText("@" + datalist.get(position).getMainUserName() + " is following you.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                } else {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.person_orange);
                    holder.tvListActivityText.setText("@" + datalist.get(position).getMainUserName() + " is un following you.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                }
            } else {
                if (datalist.get(position).getAction().equals("follow")) {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.person_green);
                    holder.tvListActivityText.setText("@" + datalist.get(position).getMainUserName() + " is following " + "@" + datalist.get(position).getOtherUsername() + "  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                } else {
                    holder.ivActivityActionIcon.setImageResource(R.drawable.person_orange);
                    holder.tvListActivityText.setText("@" + datalist.get(position).getMainUserName() + " is un following " + "@" + datalist.get(position).getOtherUsername() + "  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                }
            }
        } else if (datalist.get(position).getNodeId() == 3){
            holder.ivActivityVideoPic.setVisibility(View.VISIBLE);
            holder.ivActivityActionIcon.setVisibility(View.GONE);

            if (datalist.get(position).getOtherUserPic() != null && !datalist.get(position).getOtherUserPic().equals(""))
                Picasso.with(mActivity).load(datalist.get(position).getOtherUserPic()).placeholder(R.drawable.user_default).into(holder.ivActivityVideoPic);
            else
                Picasso.with(mActivity).load(R.drawable.user_default).into(holder.ivActivityVideoPic);
            holder.ivActivityVideoPic.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if (myId.equals(datalist.get(position).getMainUserId())) {
                if (datalist.get(position).getAction().equals("vote"))
                    holder.tvListActivityText.setText("You voted for this video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                else if (datalist.get(position).getAction().equals("unvote"))
                    holder.tvListActivityText.setText("You unvoted this video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                else if (datalist.get(position).getAction().equals("share"))
                    holder.tvListActivityText.setText("You shared this video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                else if (datalist.get(position).getAction().equals("comment"))
                    holder.tvListActivityText.setText("You commented on this video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                else if (datalist.get(position).getAction().equals("view"))
                    holder.tvListActivityText.setText("You viewed this video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
            } else {
                if (datalist.get(position).getAction().equals("vote"))
                    holder.tvListActivityText.setText("@" + datalist.get(position).getOtherUsername() + " voted for your video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                else if (datalist.get(position).getAction().equals("unvote"))
                    holder.tvListActivityText.setText("@" + datalist.get(position).getOtherUsername() + " unvoted your video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                else if (datalist.get(position).getAction().equals("share"))
                    holder.tvListActivityText.setText("@" + datalist.get(position).getOtherUsername() + " shared your video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                else if (datalist.get(position).getAction().equals("comment"))
                    holder.tvListActivityText.setText("@" + datalist.get(position).getOtherUsername() + " commented on your video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
                else if (datalist.get(position).getAction().equals("view"))
                    holder.tvListActivityText.setText("@" + datalist.get(position).getOtherUsername() + " viewed your video.  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
            }
        }else if (datalist.get(position).getNodeId() == 4){
            Log.e("Notification in",datalist.get(position).getOtherUsername());
            holder.tvListActivityText.setText(datalist.get(position).getActionContent() + "  " + Constant.getDateCurrentTimeZone1(datalist.get(position).getDate())).addLinks(links).build();
            holder.ivActivityVideoPic.setVisibility(View.GONE);
            holder.ivActivityActionIcon.setVisibility(View.GONE);
        }

        holder.civListItemActivityProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datalist.get(position).getNodeId() != 1) {
                    ProfileFragment fragment = new ProfileFragment();
                    Bundle b1 = new Bundle();
                    b1.putString("user_id", datalist.get(position).getOtherUserId());
                    fragment.setArguments(b1);
                    FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame_container, fragment);
                    ft.addToBackStack(fragment.getClass().getName());
                    ft.commit();
                }
            }
        });

        holder.ivActivityVideoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("click", view.getId() + "");

                ExploreFragment fragment = new ExploreFragment();
                Bundle b1 = new Bundle();
                b1.putString("vidId", datalist.get(position).getOtherUserId());
                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();
            }
        });

        return v;
    }
    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
        Log.i("deleteNotification ", "toggleSelection ** " + position + " - " + !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        Log.i("deleteNotification ", "getSelectedIds ** " + mSelectedItemsIds.size());
        return mSelectedItemsIds;
    }
}
