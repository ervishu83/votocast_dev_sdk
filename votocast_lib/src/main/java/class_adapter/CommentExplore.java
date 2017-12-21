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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apradanas.simplelinkabletext.Link;
import com.apradanas.simplelinkabletext.LinkableTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import db.Comment;
import fragments.ProfileFragment;
import com.votocast.votocast.R;

/**
 * Created by Anil on 5/23/2016.
 */
public class CommentExplore extends BaseAdapter {

    ArrayList<Comment> datalist;
    Context context;

    public CommentExplore(Context context, ArrayList<Comment> datalist) {
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
        public LinkableTextView tvListItemComment;
        public TextView tvListItemCommentDate;
        public LinearLayout llCommentListItem;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_explore, parent, false);

        holder.tvListItemComment = (LinkableTextView) v.findViewById(R.id.tvListItemComment);
        holder.tvListItemCommentDate = (TextView) v.findViewById(R.id.tvListItemCommentDate);
        holder.llCommentListItem = (LinearLayout) v.findViewById(R.id.llCommentListItem);

//        Constant.setTextFontsRegular(context, holder.tvListItemComment);
//        Constant.setTextFontsRegular(context, holder.tvListItemCommentDate);

        Link linkUsername = new Link(Pattern.compile("(@\\w+)"))
                .setUnderlined(false)
                .setTextColor(Color.parseColor(Constant.colorPrimary))
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        // do something
//                        datalist.get(position).getmVideoPlayerManager().resetMediaPlayer();
                        ProfileFragment fragment = new ProfileFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("user_id", datalist.get(position).getUserId());
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.addToBackStack(fragment.getClass().getName());
//                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                    }
                });

        List<Link> links = new ArrayList<>();
        links.add(linkUsername);

        // set fonts
        Constant.setDisplayFontsRegular(context, holder.tvListItemComment);
        Constant.setDisplayFontsRegular(context, holder.tvListItemCommentDate);

        // set data
        holder.tvListItemComment.setText("@"+datalist.get(position).getUsername() + " " + datalist.get(position).getComment()).addLinks(links).build();
        holder.tvListItemCommentDate.setText(Constant.getDateCurrentTimeZone1(datalist.get(position).getDate()));

//        holder.llCommentListItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ProfileFragment fragment = new ProfileFragment();
//                Bundle b1 = new Bundle();
//                b1.putString("user_id", datalist.get(position).getUserId());
//                fragment.setArguments(b1);
//                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.frame_container, fragment);
//                ft.addToBackStack(fragment.getClass().getName());
//                ft.commit();
//            }
//        });
        return v;
    }
}
