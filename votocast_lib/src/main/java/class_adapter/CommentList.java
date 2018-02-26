package class_adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import db.Comment;
import de.hdodenhof.circleimageview.CircleImageView;
import fragments.ProfileFragment;

import com.votocast.votocast.R;

/**
 * Created by Anil on 5/24/2016.
 */
public class CommentList extends BaseAdapter {

    List<Comment> datalist;
    List<Comment> newDatalist;
    Activity mActivity;
    Holder holder;
    private SparseBooleanArray mSelectedItemsIds;

    public CommentList(ArrayList<Comment> datalist, Activity mActivity) {
        this.datalist = datalist;
        this.mActivity = mActivity;
        mSelectedItemsIds = new SparseBooleanArray();
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
        public CircleImageView civListCommentVideoPic;
        public TextView tvListCommentUsername, tvListCommentDate, tvListCommentDesc;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            holder = new Holder();

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vc_comment_list_item, parent, false);

            holder.civListCommentVideoPic = (CircleImageView) convertView.findViewById(R.id.civListCommentVideoPic);
            holder.tvListCommentUsername = (TextView) convertView.findViewById(R.id.tvListCommentUsername);
            holder.tvListCommentDate = (TextView) convertView.findViewById(R.id.tvListCommentDate);
            holder.tvListCommentDesc = (TextView) convertView.findViewById(R.id.tvListCommentDesc);

            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();
//        Holder holder = new Holder();
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vc_comment_list_item, parent, false);


        Constant.setTextFontsMedium(mActivity, holder.tvListCommentUsername);
        Constant.setTextFontsRegular(mActivity, holder.tvListCommentDate);
        Constant.setTextFontsRegular(mActivity, holder.tvListCommentDesc);

        if (datalist.get(position).getUserImage() != null && !datalist.get(position).getUserImage().equals(""))
            Glide.with(mActivity).load(datalist.get(position).getUserImage()).skipMemoryCache(true).placeholder(R.drawable.user_default).into(holder.civListCommentVideoPic);
        else
            Picasso.with(mActivity).load(R.drawable.user_default).into(holder.civListCommentVideoPic);

        holder.tvListCommentUsername.setText(datalist.get(position).getUsername());
        holder.tvListCommentDate.setText(Constant.getDateCurrentTimeZone1(datalist.get(position).getDate()));
        holder.tvListCommentDesc.setText(datalist.get(position).getComment());

        holder.tvListCommentUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment fragment = new ProfileFragment();
                Bundle b1 = new Bundle();
                b1.putString("user_id", datalist.get(position).getUserId());
                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();
            }
        });

        convertView.setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4 : Color.TRANSPARENT);

        return convertView;
    }

    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
        Log.i("CommentVidId ", "toggleSelection ** " + position + " - " + !mSelectedItemsIds.get(position));
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
        Log.i("CommentVidId ", "getSelectedIds ** " + mSelectedItemsIds.size());
        return mSelectedItemsIds;
    }

    public void deleteRow(ActionMode mode) {

        SparseBooleanArray selected = getSelectedIds();//Get selected ids
//        //Loop all selected ids
        for (int i = 0; i < selected.size(); i++) {
            if (selected.valueAt(i)) {
                //If current id is selected remove the item via key
                datalist.remove(selected.keyAt(i));
                this.notifyDataSetChanged();//notify adapter
//                notifyDataSetInvalidated();
//                new CommentList(datalist, mActivity);
            }
        }
        updateReceiptsList(datalist);

        mode.finish();
    }
    public void updateReceiptsList(List<Comment> newlist) {
        newDatalist = new ArrayList<>();
        newDatalist.addAll(newlist);
        Log.e("CommentVidId ", "updateReceiptsList ** " + newlist.size());
        datalist.clear();
        Log.e("CommentVidId ", "before clear ** " + datalist.size() + " - " + newlist.size()+ " - " + newDatalist.size());
        datalist.addAll(newDatalist);
        Log.e("CommentVidId ", "after clear ** " + datalist.size() + " - " + + newlist.size() + " - " + newDatalist.size());
        this.notifyDataSetChanged();
    }
}
