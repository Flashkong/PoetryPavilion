package com.poetrypavilion.poetrypavilion.Adapters;

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
public class PaintingsAdapter extends ItemsAdapter<PoemDetail, PaintingsAdapter.ViewHolder>
        implements View.OnClickListener {

    private XXListener mXXListener;

    public PaintingsAdapter(List<PoemDetail> poemDetails) {
//        setItemsList(Arrays.asList(Painting.getAllPaintings(context.getResources())));
        setItemsList(poemDetails);
    }

    @Override
    protected ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        final ViewHolder holder = new ViewHolder(parent);

        //设置监听
        holder.layout.setOnClickListener(this);
        holder.poem_text.setOnClickListener(this);

        return holder;
    }

    @Override
    protected void onBindHolder(ViewHolder holder, int position) {
        final PoemDetail item = getItem(position);
        //设置tag
        holder.layout.setTag(R.id.poem_board_concision_linear_layout, item);

        //设置listView的具体内容
        setListViewContent(holder,item);
    }

    private void setListViewContent(ViewHolder holder,PoemDetail item){
        //设置诗歌的内容
        RichText.from(item.getPoetry()).into(holder.poem_text);
        //设置头像

        //设置用户名
        holder.poem_board_concision_username.setText(item.getEditor());
        //设置诗歌的发表时间
        holder.poem_board_concision_user_uplode_time.setText(item.getDynasty());
        //设置诗歌的标题
        holder.poem_board_poem_title.setText(item.getTitle());
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
    static class ViewHolder extends ItemsAdapter.ViewHolder {
        final TextView poem_text;
        final LinearLayout layout;
        final TextView poem_board_concision_username;
        final TextView poem_board_concision_user_uplode_time;
        final TextView poem_board_poem_title;

        ViewHolder(ViewGroup parent) {
            super(Views.inflate(parent, R.layout.poem_board_concision));
            poem_text = Views.find(itemView, R.id.poem_board_concision_poem);
            layout = Views.find(itemView, R.id.poem_board_concision_linear_layout);
            poem_board_concision_username = Views.find(itemView, R.id.poem_board_concision_username);
            poem_board_concision_user_uplode_time = Views.find(itemView, R.id.poem_board_concision_user_uplode_time);
            poem_board_poem_title = Views.find(itemView,R.id.poem_board_concision_poem_title);
        }
    }
}
