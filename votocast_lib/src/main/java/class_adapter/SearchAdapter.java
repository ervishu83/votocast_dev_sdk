package class_adapter;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import db.Search;
import de.hdodenhof.circleimageview.CircleImageView;
import fragments.CampaignFragment;
import fragments.ProfileFragment;

import com.votocast.votocast.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Anil on 5/14/2016.
 */
public class SearchAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    ArrayList<Search> datalist;
    //    Context context;
    Activity mActivity;
    Holder holder;
    private int isFollow, isUserFollow;
    private int valFollow, valUserFollow;
    StickyListHeadersListView listSearchResultFragment;

    public SearchAdapter(Activity mActivity, ArrayList<Search> datalist, StickyListHeadersListView listSearchResultFragment) {
        this.mActivity = mActivity;
        this.datalist = datalist;
        this.listSearchResultFragment = listSearchResultFragment;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vc_search_header_item, parent, false);
            holder.tvSearchHeader = (TextView) convertView.findViewById(R.id.tvSearchHeader);
            holder.llSearchHeader = (LinearLayout) convertView.findViewById(R.id.llSearchHeader);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

//        Log.i("search db", datalist.get(position).toString());

        Constant.setDisplayFontsBold(mActivity, holder.tvSearchHeader);

        if (datalist.get(position).getNodeId() == 1 && datalist.get(position).getNodePos() == 0) {
            holder.tvSearchHeader.setText("CAMPAIGNS");
//            Log.i("if", datalist.get(position).toString());
        } else {
            if (datalist.get(position).getNodeId() == 2 && datalist.get(position).getNodePos() == 0) {
//                Log.i("else if", datalist.get(position).toString());
                holder.tvSearchHeader.setText("PRODUCERS");
            } else {
//                Log.i("else", datalist.get(position).toString());
                holder.llSearchHeader.setVisibility(View.GONE);
            }
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
        CircleImageView civListItemSearchMainImage;
        RelativeLayout rlSearchItem, rlSearchResultData, rlSearchResultFollow;
        public TextView tvListItemSearchMainText, tvListItemSearchSubText;
        public ImageView ivListItemSearchSubImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        holder = new Holder();
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vc_search_list_item, parent, false);

        holder.rlSearchItem = (RelativeLayout) v.findViewById(R.id.rlSearchItem);
        holder.ivListItemSearchSubImage = (ImageView) v.findViewById(R.id.ivListItemSearchSubImage);

        holder.tvListItemSearchMainText = (TextView) v.findViewById(R.id.tvListItemSearchMainText);
        holder.tvListItemSearchSubText = (TextView) v.findViewById(R.id.tvListItemSearchSubText);
        holder.civListItemSearchMainImage = (CircleImageView) v.findViewById(R.id.civListItemSearchMainImage);

        holder.rlSearchResultData = (RelativeLayout) v.findViewById(R.id.rlSearchResultData);
        holder.rlSearchResultFollow = (RelativeLayout) v.findViewById(R.id.rlSearchResultFollow);
        // Log.i("search set db", position + "-" + datalist.get(position).toString());

        Constant.setTextFontsMedium(mActivity, holder.tvListItemSearchMainText);
        Constant.setTextFontsRegular(mActivity, holder.tvListItemSearchSubText);

        //set data

        String myUserId = Constant.getShareData(mActivity, "myId");
//        Log.i("searchData" , datalist.get(position).getNodeId() + "-" + datalist.get(position).getMainId() + "-" + myUserId);
        if (datalist.get(position).getNodeId() == 2 && myUserId.equals(datalist.get(position).getMainId()))
            holder.ivListItemSearchSubImage.setVisibility(View.GONE);
        else
            holder.ivListItemSearchSubImage.setVisibility(View.VISIBLE);

        if (!datalist.get(position).getMainImage().equals("")) {
            if (datalist.get(position).getNodeId() == 1)
                Glide.with(mActivity).load(datalist.get(position).getMainImage()).placeholder(R.drawable.campaign_icon).into(holder.civListItemSearchMainImage);
            else
                Glide.with(mActivity).load(datalist.get(position).getMainImage()).placeholder(R.drawable.user_default).into(holder.civListItemSearchMainImage);
        } else {
            if (datalist.get(position).getNodeId() == 1)
                Picasso.with(mActivity).load(R.drawable.campaign_icon).into(holder.civListItemSearchMainImage);
            else
                Picasso.with(mActivity).load(R.drawable.user_default).into(holder.civListItemSearchMainImage);
        }

        if (datalist.get(position).getMainText().equals("")) {
            holder.tvListItemSearchMainText.setText(datalist.get(position).getSubText());
            holder.tvListItemSearchSubText.setVisibility(View.GONE);
        } else {
            holder.tvListItemSearchSubText.setVisibility(View.VISIBLE);
            holder.tvListItemSearchMainText.setText(datalist.get(position).getMainText());
            holder.tvListItemSearchSubText.setText(datalist.get(position).getSubText());
        }

        if (datalist.get(position).getNodeId() == 1) {
            holder.tvListItemSearchSubText.setText(datalist.get(position).getTotalFollow() + " videos");
            if (datalist.get(position).getDidFollow().equals("true")) {
                isFollow = 1;
                holder.ivListItemSearchSubImage.setImageResource(R.drawable.award_orange);
            } else {
                isFollow = 0;
                holder.ivListItemSearchSubImage.setImageResource(R.drawable.award_grey);
            }
//            Log.i("if image", datalist.get(position).toString());
        } else {
            if (datalist.get(position).getNodeId() == 2) {
//                Log.i("else if image", datalist.get(position).toString());
                if (datalist.get(position).getDidFollow().equals("true")) {
                    isUserFollow = 1;
                    holder.ivListItemSearchSubImage.setImageResource(R.drawable.person_green);
                } else {
                    isUserFollow = 0;
                    holder.ivListItemSearchSubImage.setImageResource(R.drawable.person_orange);
                }
            } else {
//                Log.i("else image", datalist.get(position).toString());
                holder.ivListItemSearchSubImage.setVisibility(View.GONE);
            }
        }

        holder.rlSearchResultData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                listSearchResultFragment.setVisibility(View.GONE);
                if (datalist.get(position).getNodeId() == 1) {
                    Log.i("search getNodeId", datalist.get(position).getMainId());

                    CampaignFragment fragment = new CampaignFragment();
                    Bundle b1 = new Bundle();
                    b1.putString("campId", datalist.get(position).getMainId());
//                    b1.putString("from", "search");
                    fragment.setArguments(b1);
                    FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame_container, fragment);
                    ft.addToBackStack(fragment.getClass().getName());
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                } else if (datalist.get(position).getNodeId() == 2) {
                    ProfileFragment fragment = new ProfileFragment();
                    Bundle b1 = new Bundle();
                    b1.putString("user_id", datalist.get(position).getMainId());
//                    b1.putString("from", "search");
                    fragment.setArguments(b1);
                    FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame_container, fragment);
                    ft.addToBackStack(fragment.getClass().getName());
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            }
        });

        holder.rlSearchResultFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                listSearchResultFragment.setVisibility(View.GONE);
//                Log.i("click", view.getId() + "");
//                ImageView imageView = (ImageView) view.findViewById(view.getId());

                if (datalist.get(position).getNodeId() == 1) {
                    if (datalist.get(position).getDidFollow().equals("false"))
                        isFollow = 0;
                    else
                        isFollow = 1;

//                    valFollow = Integer.parseInt(datalist.get(position).getTotalFollow());

//                    TextView tvListItemProfileFollower = (TextView) v.findViewById(R.id.tvListItemSearchSubText);
                    ImageView ivListItemProfileSettingIcon = (ImageView) v.findViewById(R.id.ivListItemSearchSubImage);

                    if (isFollow == 0) {
                        isFollow = isFollow + 1;
//                        valFollow = valFollow + 1;

                        datalist.get(position).setSubText(String.valueOf(valFollow));
                        datalist.get(position).setDidFollow("true");
//                        TextView tvListItemProfileFollower = (TextView) v.findViewById(R.id.tvListItemSearchSubText);
//                        tvListItemProfileFollower.setText(String.valueOf(valFollow) + " followers");
//                        ImageView ivListItemProfileSettingIcon = (ImageView) v.findViewById(R.id.ivListItemSearchSubImage);
                        ivListItemProfileSettingIcon.setImageResource(R.drawable.award_orange);

                        new setCampFollower(datalist.get(position).getMainId()).execute();
                    } else {
//                            dialogUnFollowUser(viewHolder.tvListItemProfileFollower, viewHolder.ivListItemProfileSettingIcon);
                        if (!datalist.get(position).getMainText().equals(""))
                            dialogUnFollowUser("camp", position, datalist.get(position).getId(), datalist.get(position).getMainImage(), datalist.get(position).getMainText(), ivListItemProfileSettingIcon);
                        else
                            dialogUnFollowUser("camp", position, datalist.get(position).getId(), datalist.get(position).getMainImage(), datalist.get(position).getSubText(), ivListItemProfileSettingIcon);
                    }
//                    }
                } else if (datalist.get(position).getNodeId() == 2) {
                    if (datalist.get(position).getDidFollow().equals("false"))
                        isUserFollow = 0;
                    else
                        isUserFollow = 1;
                    ImageView ivListItemProfileSettingIcon = (ImageView) v.findViewById(R.id.ivListItemSearchSubImage);

                    if (isUserFollow == 0) {
                        isUserFollow = isUserFollow + 1;
                        datalist.get(position).setDidFollow("true");
//                        ImageView ivListItemProfileSettingIcon = (ImageView) v.findViewById(R.id.ivListItemSearchSubImage);
                        ivListItemProfileSettingIcon.setImageResource(R.drawable.person_green);
                        new setUserFollower(datalist.get(position).getMainId()).execute();
                    } else {
//                            dialogUnFollowUser(viewHolder.tvListItemProfileFollower, viewHolder.ivListItemProfileSettingIcon);
                        if (!datalist.get(position).getMainText().equals(""))
                            dialogUnFollowUser("user", position, datalist.get(position).getId(), datalist.get(position).getMainImage(), datalist.get(position).getMainText(), ivListItemProfileSettingIcon);
                        else
                            dialogUnFollowUser("user", position, datalist.get(position).getId(), datalist.get(position).getMainImage(), datalist.get(position).getSubText(), ivListItemProfileSettingIcon);
                    }
                }
            }
        });

        holder.tvListItemSearchMainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                listSearchResultFragment.setVisibility(View.GONE);
                Log.i("search click", view.getId() + "");

                if (datalist.get(position).getNodeId() == 1) {
//                    Log.i("search getNodeId", datalist.get(position).getMainId());

                    CampaignFragment fragment = new CampaignFragment();
                    Bundle b1 = new Bundle();
                    b1.putString("campId", datalist.get(position).getMainId());
//                    b1.putString("from", "search");
                    fragment.setArguments(b1);
                    FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame_container, fragment);
                    ft.addToBackStack(fragment.getClass().getName());
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                } else if (datalist.get(position).getNodeId() == 2) {
                    ProfileFragment fragment = new ProfileFragment();
                    Bundle b1 = new Bundle();
                    b1.putString("user_id", datalist.get(position).getMainId());
//                    b1.putString("from", "search");
                    fragment.setArguments(b1);
                    FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame_container, fragment);
                    ft.addToBackStack(fragment.getClass().getName());
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            }
        });

        return v;
    }

    private void dialogUnFollowUser(final String from, final int position, final String userId, String headerImage, String headerMainText, final ImageView ivListItemProfileSettingIcon) {
        final Dialog dialog = new Dialog(mActivity);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vc_unfollow_user_dialog);

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.tvDialogUnfollowUserName);
        text.setText(headerMainText);
        ImageView image = (ImageView) dialog.findViewById(R.id.ivDialogUnfollowUserPic);
//        Log.i("headerImage",headerImage);
        if (!headerImage.equals(""))
            Glide.with(mActivity).load(headerImage)
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

                if (from.equals("camp")) {
                    isFollow = isFollow - 1;
//                    valFollow = valFollow - 1;
                    datalist.get(position).setDidFollow("false");
//                    datalist.get(position).setSubText(String.valueOf(valFollow));
//                    tvListItemProfileFollower.setText(String.valueOf(valFollow) + " followers");
                    ivListItemProfileSettingIcon.setImageResource(R.drawable.award_grey);
                    new setCampUnFollower(datalist.get(position).getMainId()).execute();
                    dialog.dismiss();
                } else {
                    isUserFollow = isUserFollow - 1;
//                    valUserFollow = valUserFollow - 1;
                    datalist.get(position).setDidFollow("false");
                    ivListItemProfileSettingIcon.setImageResource(R.drawable.person_orange);
                    new setUserUnFollower(datalist.get(position).getMainId()).execute();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }

    class setCampFollower extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String campId;

        public setCampFollower(String campId) {
            this.campId = campId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", campId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.follow_campaign_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class setCampUnFollower extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String campId;

        public setCampUnFollower(String campId) {
            this.campId = campId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", campId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.unfollow_campaign_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
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

            String token = Constant.getShareData(mActivity, "pref_login");
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

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                }
//                                if (mProgressHUD.isShowing() && mProgressHUD != null)
//                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
//                        if (mProgressHUD.isShowing() && mProgressHUD != null)
//                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
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
//            mProgressHUD = ProgressHUD.show(mActivity, "", false, false, new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                }
//            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(mActivity, "pref_login");
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

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                }
//                                if (mProgressHUD.isShowing() && mProgressHUD != null)
//                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
//                        if (mProgressHUD.isShowing() && mProgressHUD != null)
//                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }
}
