package class_adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apradanas.simplelinkabletext.Link;
import com.apradanas.simplelinkabletext.LinkableTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import db.Home;
import db.Reports;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fragments.CampaignFragment;
import fragments.CommentsFragment;
import fragments.ProfileFragment;

import com.votocast.votocast.R;
import com.votocast.votocast.VC_ReportsActivity;

/**
 * Created by Anil on 5/26/2016.
 */
@SuppressWarnings("WrongConstant")
public class HomeTestAdapter extends BaseAdapter {

    private static final String TAG = "HomeTestAdapter";
    ArrayList<Home> datalist;
    Activity context;
    Holder holder;
    int isLike, isFollow;
    int valLike, valFollow;
    int myPosition;
    int currentPos = 0;
    public static Boolean isScrolling = true;

    public HomeTestAdapter(Activity context, ArrayList<Home> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder {

        TextView tvListItemProfileName, tvListItemProfileFollower;
        CircleImageView civListItemVotocastProfilePic;

        public TextView tvListItemVideoLike, tvListItemVideoPlay, tvListItemVideoShare;
        TextView tvListItemProfileVote, tvListItemProfileShare, tvListItemProfileComment, tvListItemProfileDescSimpleTest;
        public ImageView ivListItemProfileSettingIcon, ivListItemDots;
        RelativeLayout rlListItemTwo;
        LinkableTextView tvListItemProfileDesc;
        TextView tvListItemProfileTagTest;
        ImageView ivHomeVidPlayTest, ivHomeBannerTest, ivHomeVideoFullScreenTest, ivHomeVideoMuteTest;
        JCVideoPlayer videoplayer;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new Holder();

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_home_list_item, parent, false);

            holder.videoplayer = (JCVideoPlayer) convertView.findViewById(R.id.videoplayer);
            holder.ivHomeVidPlayTest = (ImageView) convertView.findViewById(R.id.ivHomeVidPlayTest);
            holder.ivHomeVideoFullScreenTest = (ImageView) convertView.findViewById(R.id.ivHomeVideoFullScreenTest);
            holder.ivHomeBannerTest = (ImageView) convertView.findViewById(R.id.ivHomeBannerTest);
            holder.ivHomeVideoMuteTest = (ImageView) convertView.findViewById(R.id.ivHomeVideoMuteTest);

            holder.tvListItemProfileName = (TextView) convertView.findViewById(R.id.tvListItemProfileNameTest);
            holder.tvListItemProfileFollower = (TextView) convertView.findViewById(R.id.tvListItemProfileFollowerTest);
            holder.civListItemVotocastProfilePic = (CircleImageView) convertView.findViewById(R.id.civListItemVotocastProfilePicTest);
            holder.ivListItemDots = (ImageView) convertView.findViewById(R.id.ivListItemDotsTest);
            holder.ivListItemProfileSettingIcon = (ImageView) convertView.findViewById(R.id.ivListItemProfileSettingIconTest);
            holder.rlListItemTwo = (RelativeLayout) convertView.findViewById(R.id.rlListItemTwoTest);
            holder.tvListItemVideoLike = (TextView) convertView.findViewById(R.id.tvListItemVideoLikeTest);
            holder.tvListItemVideoPlay = (TextView) convertView.findViewById(R.id.tvListItemVideoPlayTest);
            holder.tvListItemVideoShare = (TextView) convertView.findViewById(R.id.tvListItemVideoShareTest);

            holder.tvListItemProfileVote = (TextView) convertView.findViewById(R.id.tvListItemProfileVoteTest);
            holder.tvListItemProfileShare = (TextView) convertView.findViewById(R.id.tvListItemProfileShareTest);
            holder.tvListItemProfileComment = (TextView) convertView.findViewById(R.id.tvListItemProfileCommentTest);
            holder.tvListItemProfileDesc = (LinkableTextView) convertView.findViewById(R.id.tvListItemProfileDescTest);
            holder.tvListItemProfileTagTest = (TextView) convertView.findViewById(R.id.tvListItemProfileTagTest);
            holder.tvListItemProfileDescSimpleTest = (TextView) convertView.findViewById(R.id.tvListItemProfileDescSimpleTest);

            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();

        // ------ set font -----------
        if(context != null) {
            Constant.setTextFontsMedium(context, holder.tvListItemProfileName);
            Constant.setTextFontsRegular(context, holder.tvListItemProfileFollower);

            Constant.setTextFontsMedium(context, holder.tvListItemVideoLike);
            Constant.setTextFontsMedium(context, holder.tvListItemVideoPlay);
            Constant.setTextFontsMedium(context, holder.tvListItemVideoShare);

            Constant.setTextFontsSemibold(context, holder.tvListItemProfileVote);
            Constant.setTextFontsSemibold(context, holder.tvListItemProfileShare);
            Constant.setTextFontsSemibold(context, holder.tvListItemProfileComment);

            Constant.setTextFontsRegular(context, holder.tvListItemProfileDescSimpleTest);
            Constant.setTextFontsRegular(context, holder.tvListItemProfileDesc);
            Constant.setTextFontsRegular(context, holder.tvListItemProfileTagTest);
        }
        //-------set data -------

//        if (datalist.get(position).getRank() <= 10) {
//            holder.ivHomeBannerTest.setVisibility(View.VISIBLE);
        holder.ivHomeBannerTest.setImageResource(datalist.get(position).getRank());
//        } else
        holder.ivHomeBannerTest.setVisibility(datalist.get(position).getIsShowBanner());

        holder.tvListItemProfileTagTest.setTextColor(Color.parseColor(Constant.colorPrimary));

        String token = Constant.getShareData(context, "pref_login");
        holder.videoplayer.setVideoId(datalist.get(position).getVidId(), token, Constant.video_show_url, Constant.Authorization);
        holder.videoplayer.setVideoViews(datalist.get(position).getView());
        holder.videoplayer.setVideoLike(datalist.get(position).getLike());
        holder.videoplayer.setUp(datalist.get(position).getVideoUrl(), "");
        if (datalist.get(position).getVideoThumbImage() != null && !datalist.get(position).getVideoThumbImage().equals("")) {
            Glide.with(context).load(datalist.get(position).getVideoThumbImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.videoplayer.ivThumb);
        }
//        else {
//            Glide.with(context).load(R.drawable.backimage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.videoplayer.ivThumb);
//        }

        String myUserId = Constant.getShareData(context, "myId");
//        if (myUserId.equals(datalist.get(position).getUserId()))
        holder.ivListItemProfileSettingIcon.setVisibility(myUserId.equals(datalist.get(position).getUserId()) ? View.GONE : View.VISIBLE);
//        else
//            holder.ivListItemProfileSettingIcon.setVisibility(View.VISIBLE);

        if (datalist.get(position).getHeaderImage() != null && !datalist.get(position).getHeaderImage().equals(""))
            Picasso.with(context).load(datalist.get(position).getHeaderImage()).placeholder(R.drawable.campaign_icon).resize(100, 100).error(R.drawable.campaign_icon).into(holder.civListItemVotocastProfilePic);

        holder.tvListItemProfileName.setText(datalist.get(position).getHeaderMainText());
        holder.tvListItemProfileFollower.setText(datalist.get(position).getHeaderSubText() + " followers");

        Link linkHashtag = new Link(Pattern.compile("(#\\w+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
//                        MyUtils.showToast(context, "click");
                        CampaignFragment fragment = new CampaignFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("campId", datalist.get(position).getCampId());
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.addToBackStack(fragment.getClass().getName());
//                        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                        ft.commit();

                    }
                });
        Link linkMoreTag = new Link(Pattern.compile("(\\...\\w+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        Log.i("CommentVidId", "Id - " + String.valueOf(datalist.get(position).getVidId()));

                        CommentsFragment fragment = new CommentsFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("vidId", String.valueOf(datalist.get(position).getVidId()));
                        b1.putString("pic", String.valueOf(datalist.get(position).getHeaderImage()));
                        b1.putString("name", String.valueOf(datalist.get(position).getHeaderMainText()));
                        b1.putString("desc", String.valueOf(datalist.get(position).getDesc()));
                        b1.putString("date", String.valueOf(datalist.get(position).getCreated_on()));
                        b1.putString("userId", String.valueOf(datalist.get(position).getUserId()));
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.addToBackStack(fragment.getClass().getName());
                        ft.commit();
                    }
                });

        Link linkWithoutHash = new Link(Pattern.compile("(\\w+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {

                    }
                });
        List<Link> links = new ArrayList<>();
        links.add(linkMoreTag);
        links.add(linkHashtag);
//        links.add(linkWithoutHash);

        holder.tvListItemProfileDesc.setVisibility(datalist.get(position).getIsMoreDesc());
        holder.tvListItemProfileDescSimpleTest.setVisibility(datalist.get(position).getIsSimpleDesc());
        holder.tvListItemProfileDesc.setText(datalist.get(position).getDesc()).addLinks(links).build();
        holder.tvListItemProfileDescSimpleTest.setText(datalist.get(position).getDesc());

//        String descText = datalist.get(position).getDesc().replaceAll("\n", " ");
////        Log.i("HomeText",descText.length()+"-"+datalist.get(position).getShortCode());
//        if (descText.length() > 0) {
//            if (descText.length() > 82) {
//                descText = descText.substring(0, 82) + " ...More";
//
//                holder.tvListItemProfileDescSimpleTest.setVisibility(View.GONE);
//                holder.tvListItemProfileDesc.setVisibility(View.VISIBLE);
//                holder.tvListItemProfileDesc.setText(descText).addLinks(links).build();
//            } else {
//                holder.tvListItemProfileDescSimpleTest.setVisibility(View.VISIBLE);
//                holder.tvListItemProfileDesc.setVisibility(View.GONE);
//                holder.tvListItemProfileDescSimpleTest.setText(descText);
//            }
//        } else {
//            holder.tvListItemProfileDesc.setVisibility(View.GONE);
//            holder.tvListItemProfileDescSimpleTest.setVisibility(View.GONE);
//        }

//        Log.i("Home TAG",datalist.get(position).getShortCode());
        holder.tvListItemProfileTagTest.setText(datalist.get(position).getShortCode());
//        holder.tvListItemProfileDesc.setText(datalist.get(position).getDesc() + " " + datalist.get(position).getShortCode()).addLinks(links).build();

        holder.tvListItemVideoLike.setText(datalist.get(position).getLike());
        holder.tvListItemVideoPlay.setText(datalist.get(position).getView());
        holder.tvListItemVideoShare.setText(datalist.get(position).getShare());

        if (datalist.get(position).getDidfollowed().equals("true")) {
            isFollow = 1;
            holder.ivListItemProfileSettingIcon.setImageResource(R.drawable.person_green);
        } else {
            isFollow = 0;
            holder.ivListItemProfileSettingIcon.setImageResource(R.drawable.person_orange);
        }

        if (datalist.get(position).getDidliked().equals("true")) {
            isLike = 1;
            holder.tvListItemProfileVote.setTextColor(Color.parseColor(Constant.colorPrimary));
        } else {
            isLike = 0;
            holder.tvListItemProfileVote.setTextColor(context.getResources().getColor(R.color.homeVoteShare));
        }

//        if (!datalist.get(position).getCnt_comments().equals("0"))
//            holder.tvListItemProfileComment.setText("COMMENTS(" + datalist.get(position).getCnt_comments() + ")");
//        else
//            holder.tvListItemProfileComment.setText("COMMENTS");
        holder.tvListItemProfileComment.setText(datalist.get(position).getCnt_comments());

        // ----------- click events ----------------

        final View finalConvertView = convertView;

        holder.tvListItemProfileTagTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        holder.videoplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("HomViewClick","click");
                TextView tvListItemVideoPlayTest = (TextView) finalConvertView.findViewById(R.id.tvListItemVideoPlayTest);
                tvListItemVideoPlayTest.setText(String.valueOf(Integer.parseInt(datalist.get(myPosition).getView() + 1)));
                datalist.get(myPosition).setView(String.valueOf(Integer.parseInt(datalist.get(myPosition).getView() + 1)));
            }
        });

        holder.tvListItemProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment fragment = new ProfileFragment();
                Bundle b1 = new Bundle();
                b1.putString("user_id", datalist.get(position).getUserId());
                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(fragment.getClass().getName());
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
        holder.ivListItemProfileSettingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valFollow = Integer.parseInt(datalist.get(position).getHeaderSubText());
                if (datalist.get(position).getDidfollowed().equals("false"))
                    isFollow = 0;
                else
                    isFollow = 1;
                ImageView ivListItemProfileSettingIcon = (ImageView) finalConvertView.findViewById(R.id.ivListItemProfileSettingIconTest);
                TextView tvListItemProfileFollower = (TextView) finalConvertView.findViewById(R.id.tvListItemProfileFollowerTest);

                if (isFollow == 0) {
                    isFollow = isFollow + 1;
                    valFollow = valFollow + 1;
                    datalist.get(position).setHeaderSubText(String.valueOf(valFollow));
                    datalist.get(position).setDidfollowed("true");
                    ivListItemProfileSettingIcon.setImageResource(R.drawable.person_green);
                    tvListItemProfileFollower.setText(String.valueOf(valFollow) + " followers");
                    new setUserFollower(datalist.get(position).getUserId()).execute();
                } else {
                    dialogUnFollowUser(position, datalist.get(position).getUserId(), datalist.get(position).getHeaderImage(), datalist.get(position).getHeaderMainText(), tvListItemProfileFollower, ivListItemProfileSettingIcon);
                }

            }
        });
        holder.tvListItemProfileComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent m1 = new Intent(context, CommentActivity.class);
//                m1.putExtra("vidId", String.valueOf(datalist.get(position).getVidId()));
//                m1.putExtra("pic", String.valueOf(datalist.get(position).getHeaderImage()));
//                m1.putExtra("name", String.valueOf(datalist.get(position).getHeaderMainText()));
//                m1.putExtra("desc", String.valueOf(datalist.get(position).getDesc()));
//                m1.putExtra("date", String.valueOf(datalist.get(position).getCreated_on()));
//                m1.putExtra("userId", String.valueOf(datalist.get(position).getUserId()));
//                context.startActivity(m1);

                Log.i("CommentVidId", "Id - " + String.valueOf(datalist.get(position).getVidId()));

                CommentsFragment fragment = new CommentsFragment();
                Bundle b1 = new Bundle();
                b1.putString("vidId", String.valueOf(datalist.get(position).getVidId()));
                b1.putString("pic", String.valueOf(datalist.get(position).getHeaderImage()));
                b1.putString("name", String.valueOf(datalist.get(position).getHeaderMainText()));
                b1.putString("desc", String.valueOf(datalist.get(position).getDesc()));
                b1.putString("date", String.valueOf(datalist.get(position).getCreated_on()));
                b1.putString("userId", String.valueOf(datalist.get(position).getUserId()));
                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
//                ft.addToBackStack(fragment.getClass().getName());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        holder.tvListItemProfileVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valLike = Integer.parseInt(datalist.get(position).getLike());

                if (datalist.get(position).getDidliked().equals("false"))
                    isLike = 0;
                else
                    isLike = 1;

                TextView tvListItemVideoLikeTest = (TextView) finalConvertView.findViewById(R.id.tvListItemVideoLikeTest);
                TextView tvListItemProfileVoteTest = (TextView) finalConvertView.findViewById(R.id.tvListItemProfileVoteTest);

                if (isLike == 0) {
                    isLike = isLike + 1;
                    valLike = valLike + 1;
                    datalist.get(position).setLike(String.valueOf(valLike));
                    datalist.get(position).setDidliked("true");
//                    holder.videoplayer.setVideoLike(String.valueOf(valLike));
                    tvListItemVideoLikeTest.setText("");
                    tvListItemVideoLikeTest.setText(String.valueOf(valLike));
                    tvListItemProfileVoteTest.setTextColor(Color.parseColor(Constant.colorPrimary));
                    new setLikeVideo(position, datalist.get(position).getVidId(), tvListItemVideoLikeTest, tvListItemProfileVoteTest).execute();
                } else {
                    isLike = isLike - 1;
                    valLike = valLike - 1;
                    datalist.get(position).setLike(String.valueOf(valLike));
                    datalist.get(position).setDidliked("false");
//                    holder.videoplayer.setVideoLike(String.valueOf(valLike));
                    tvListItemVideoLikeTest.setText("");
                    tvListItemVideoLikeTest.setText(String.valueOf(valLike));
                    tvListItemProfileVoteTest.setTextColor(context.getResources().getColor(R.color.homeVoteShare));
                    new setUnLikeVideo(datalist.get(position).getVidId()).execute();
                }
            }
        });
        holder.tvListItemProfileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvListItemVideoShareTest = (TextView) finalConvertView.findViewById(R.id.tvListItemVideoShareTest);
                dialogShare(position, datalist.get(position).getShare(), tvListItemVideoShareTest, String.valueOf(datalist.get(position).getVidId()), datalist.get(position).getReportList(), datalist.get(position).getVideoUrl(), datalist.get(position).getSelf_video());
                datalist.get(position).setShare(String.valueOf(Integer.parseInt(datalist.get(position).getShare()) + 1));
            }
        });

        holder.ivListItemDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvListItemVideoShareTest = (TextView) finalConvertView.findViewById(R.id.tvListItemVideoShareTest);
                dialogShare(position, datalist.get(position).getShare(), tvListItemVideoShareTest, String.valueOf(datalist.get(position).getVidId()), datalist.get(position).getReportList(), datalist.get(position).getVideoUrl(), datalist.get(position).getSelf_video());
                datalist.get(position).setShare(String.valueOf(Integer.parseInt(datalist.get(position).getShare()) + 1));
            }
        });

        return convertView;
    }

    private void dialogUnFollowUser(final int position, final String userId, String headerImage, String headerMainText, final TextView tvListItemProfileFollower, final ImageView ivListItemProfileSettingIcon) {
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.unfollow_user_dialog);

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.tvDialogUnfollowUserName);
        text.setText(headerMainText);
        ImageView image = (ImageView) dialog.findViewById(R.id.ivDialogUnfollowUserPic);
        if (!headerImage.equals(""))
            Picasso.with(context).load(headerImage)
                    .placeholder(R.drawable.user_default)
                    .error(R.drawable.user_default)
                    .into(image);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnDialogUnfollowCancel);
        Button btnUnfollow = (Button) dialog.findViewById(R.id.btnDialogUnfollow);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //            isFollow = isFollow - 1;
                isFollow = 0;
                valFollow = valFollow - 1;

//                TextView tvListItemProfileFollower = (TextView) finalConvertView.findViewById(R.id.tvListItemProfileFollower);
//                tvListItemProfileFollower.setText(String.valueOf(valFollow) + " followers");
//                ImageView ivListItemProfileSettingIcon = (ImageView) finalConvertView.findViewById(R.id.ivListItemProfileSettingIcon);
//                ivListItemProfileSettingIcon.setImageResource(R.drawable.person_green);
                datalist.get(position).setDidfollowed("false");
                datalist.get(position).setHeaderSubText(String.valueOf(valFollow));
                tvListItemProfileFollower.setText(String.valueOf(valFollow) + " followers");
                ivListItemProfileSettingIcon.setImageResource(R.drawable.person_orange);
                new setUserUnFollower(userId).execute();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    class setLikeVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        int vidId, position;
        TextView tvListItemVideoLikeTest, tvListItemProfileVoteTest;

        public setLikeVideo(int position, int id, TextView tvListItemVideoLikeTest, TextView tvListItemProfileVoteTest) {
            vidId = id;
            this.position = position;
            this.tvListItemVideoLikeTest = tvListItemVideoLikeTest;
            this.tvListItemProfileVoteTest = tvListItemProfileVoteTest;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(context, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("video_id", String.valueOf(vidId)));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.like_video_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
//                                        MyUtils.showToast(context, posts);
                                    } else if (error == 2) {
                                        final AlertDialog.Builder myVoteHomeDialog = new AlertDialog.Builder(context);
                                        myVoteHomeDialog.setTitle("O2Life")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        isLike = isLike - 1;
                                                        valLike = valLike - 1;
                                                        datalist.get(position).setLike(String.valueOf(valLike));
                                                        datalist.get(position).setDidliked("false");
//                                                        holder.videoplayer.setVideoLike(String.valueOf(valLike));
                                                        tvListItemVideoLikeTest.setText("");
                                                        tvListItemVideoLikeTest.setText(String.valueOf(valLike));
                                                        tvListItemProfileVoteTest.setTextColor(context.getResources().getColor(R.color.homeVoteShare));
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        isLike = isLike - 1;
                                                        valLike = valLike - 1;
                                                        datalist.get(position).setLike(String.valueOf(valLike));
                                                        datalist.get(position).setDidliked("false");
//                                                        holder.videoplayer.setVideoLike(String.valueOf(valLike));
                                                        tvListItemVideoLikeTest.setText("");
                                                        tvListItemVideoLikeTest.setText(String.valueOf(valLike));
                                                        tvListItemProfileVoteTest.setTextColor(context.getResources().getColor(R.color.homeVoteShare));
                                                    }
                                                })
                                                .create()
                                                .show();
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), context);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class setUnLikeVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        int vidId;

        public setUnLikeVideo(int id) {
            vidId = id;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(context, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("video_id", String.valueOf(vidId)));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.unlike_video_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
//                                        MyUtils.showToast(context, posts);
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), context);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class setUserFollower extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String userId;

        public setUserFollower(String userId) {
            this.userId = userId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(context, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("user_id", userId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.follow_user_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), context);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class setUserUnFollower extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String userId;

        public setUserUnFollower(String userId) {
            this.userId = userId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(context, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("user_id", userId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.unfollow_user_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), context);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    public void dialogShare(final int position, final String shareCount, final TextView tvListItemVideoShareTest, final String vidId, final ArrayList<Reports> reportList, final String videoUrl, final String self_video) {
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.share_dialog);

        // set values for custom dialog components - text, image and button
        TextView tvShareReports = (TextView) dialog.findViewById(R.id.tvShareReports);
        TextView tvShareFb = (TextView) dialog.findViewById(R.id.tvShareFb);
        TextView tvShareTweet = (TextView) dialog.findViewById(R.id.tvShareTweet);
        TextView tvShareCopy = (TextView) dialog.findViewById(R.id.tvShareCopy);
        TextView tvDeleteVideo = (TextView) dialog.findViewById(R.id.tvDeleteVideo);

        if (self_video.equals("yes"))
            tvDeleteVideo.setVisibility(View.VISIBLE);
        else
            tvDeleteVideo.setVisibility(View.GONE);

        tvShareReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("report", vidId + "=" + reportList.toString());
                Intent m1 = new Intent(context, VC_ReportsActivity.class);
                m1.putExtra("vidId", vidId);
                m1.putExtra("report", reportList);
                context.startActivity(m1);
                dialog.dismiss();
            }
        });
        tvShareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newval = String.valueOf(Integer.parseInt(shareCount) + 1);
                new setShareVideo("facebook", vidId, tvListItemVideoShareTest, newval).execute();
                dialog.dismiss();
            }
        });
        tvShareTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newval = String.valueOf(Integer.parseInt(shareCount) + 1);
                new setShareVideo("twitter", vidId, tvListItemVideoShareTest, newval).execute();
                dialog.dismiss();
            }
        });
        tvShareCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newval = String.valueOf(Integer.parseInt(shareCount) + 1);
                new setShareVideo("url", vidId, tvListItemVideoShareTest, newval).execute();
                dialog.dismiss();
            }
        });
        tvDeleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("VOTOCAST")
                        .setMessage("Are you sure want to delete this video? ")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new deleteVideo(vidId, position).execute();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                // Create the AlertDialog object and return it
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });

        dialog.show();
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
//            Log.wtf(TAG, "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    class setShareVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String type, newval;
        String videoId;
        TextView tvListItemVideoShareTest;

        public setShareVideo(String type, String videoId, TextView tvListItemVideoShareTest, String newval) {
            this.type = type;
            this.videoId = videoId;
            this.tvListItemVideoShareTest = tvListItemVideoShareTest;
            this.newval = newval;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(context, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("video_id", videoId));
            pair.add(new BasicNameValuePair("type", type));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.video_share_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                        if (type.equals("facebook")) {
                                            ShareDialog shareDialog;
                                            FacebookSdk.sdkInitialize(context.getApplicationContext());
                                            shareDialog = new ShareDialog(context);
                                            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                                    .setContentUrl(Uri.parse(Constant.main_url + "/video/" + videoId))
                                                    .build();

                                            shareDialog.show(linkContent);
                                            tvListItemVideoShareTest.setText(newval);
                                        } else if (type.equals("twitter")) {
                                            String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                                                    urlEncode(""),
                                                    urlEncode(Constant.main_url + "/video/" + videoId));
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

                                            List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
                                            for (ResolveInfo info : matches) {
                                                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                                                    intent.setPackage(info.activityInfo.packageName);
                                                }
                                            }
                                            context.startActivity(intent);
                                            tvListItemVideoShareTest.setText(newval);
                                        } else if (type.equals("url")) {
                                            int sdk = android.os.Build.VERSION.SDK_INT;
                                            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                                clipboard.setText(Constant.main_url + "/video/" + videoId);
                                                MyUtils.showToast(context, "Url copied!");
                                            } else {
                                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                                android.content.ClipData clip = android.content.ClipData.newPlainText("Video", Constant.main_url + "/video/" + videoId);
                                                clipboard.setPrimaryClip(clip);
                                                MyUtils.showToast(context, "Url copied!");
                                            }
                                            tvListItemVideoShareTest.setText(newval);
                                        }
                                    } else if (error == 2) {
                                        final AlertDialog.Builder myShareDialog = new AlertDialog.Builder(context);
                                        myShareDialog.setTitle("O2Life")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .create()
                                                .show();
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), context);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class deleteVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String videoId;
        int position;

        public deleteVideo(String videoId, int position) {
            this.videoId = videoId;
            this.position = position;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(context, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("video_id", videoId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.delete_video_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                        datalist.remove(position);
                                        notifyDataSetChanged();
                                    } else if (error == 2) {
                                        final AlertDialog.Builder myShareDialog = new AlertDialog.Builder(context);
                                        myShareDialog.setTitle("O2Life")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .create()
                                                .show();
                                    } else if (error == 1) {
                                        Constant.ShowErrorMessage("Error", posts, context);
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), context);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class setResendEmail extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(context, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.resend_email_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                        MyUtils.showToast(context, posts);
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), context);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", context);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }
}