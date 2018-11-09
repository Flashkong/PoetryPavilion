package com.poetrypavilion.poetrypavilion.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.alexvasilkov.android.commons.ui.Views;
import com.poetrypavilion.poetrypavilion.Beans.Poetry.PoemDetail;
import com.poetrypavilion.poetrypavilion.R;
import com.zzhoujay.richtext.RichText;


import java.util.List;


//PaintingAdapter其实不是Painting的adapter，而是大概的那个滑动的listview的adapter
public class PaintingsAdapter extends RecyclerView.Adapter<PaintingsAdapter.ViewHolder>
        implements View.OnClickListener {

    private XXListener mXXListener;
    private List<PoemDetail> poemDetailList;

    public PaintingsAdapter(List<PoemDetail> poemDetails) {
        this.poemDetailList=poemDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poem_board_concision,parent,false);
        ViewHolder holder = new ViewHolder(view);

        //设置监听
        holder.layout.setOnClickListener(this);
        holder.poem_text.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PoemDetail item = poemDetailList.get(position);
        //设置tag
        holder.layout.setTag(R.id.poem_board_concision_linear_layout, item);

        //设置listView的具体内容
        setListViewContent(holder,item);
    }

    private void setListViewContent(ViewHolder holder,PoemDetail item){
        //设置诗歌的内容
        holder.poem_text.setText(item.getPoetry());
        //设置头像

        //设置用户名
        holder.poem_board_concision_username.setText(item.getEditor());
        //设置诗歌的发表时间
        holder.poem_board_concision_user_uplode_time.setText(item.getDynasty());
        //设置诗歌的标题
        holder.poem_board_poem_title.setText(item.getTitle());
    }

    @Override
    public int getItemCount(){
        return poemDetailList.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.poem_board_concision_linear_layout:
                final PoemDetail item = (PoemDetail) view.getTag(R.id.poem_board_concision_linear_layout);
                mXXListener.onXXClick(view, item);
                break;
            case R.id.poem_board_concision_poem:
                View parent = (View) view.getParent();
                mXXListener.onXXClick(parent, (PoemDetail) parent.getTag(R.id.poem_board_concision_linear_layout));
                break;
        }
    }

    public interface XXListener {
        void onXXClick(View view, PoemDetail item);
    }

    public void setOnXXClickListener(XXListener XXListener) {
        this.mXXListener = XXListener;
    }

    //ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView poem_text;
        final LinearLayout layout;
        final TextView poem_board_concision_username;
        final TextView poem_board_concision_user_uplode_time;
        final TextView poem_board_poem_title;

        ViewHolder(View view) {
            super(view);
            poem_text = view.findViewById( R.id.poem_board_concision_poem);
            layout =view.findViewById( R.id.poem_board_concision_linear_layout);
            poem_board_concision_username = view.findViewById(R.id.poem_board_concision_username);
            poem_board_concision_user_uplode_time = view.findViewById(R.id.poem_board_concision_user_uplode_time);
            poem_board_poem_title =view.findViewById(R.id.poem_board_concision_poem_title);
        }
    }
}
