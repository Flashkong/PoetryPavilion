package com.poetrypavilion.poetrypavilion.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.alexvasilkov.android.commons.ui.Views;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.PoemFragment;
import com.poetrypavilion.poetrypavilion.Items.Painting;
import com.poetrypavilion.poetrypavilion.R;
import com.zzhoujay.richtext.RichText;


import java.util.Arrays;


//PaintingAdapter其实不是Painting的adapter，而是大概的那个滑动的listview的adapter
public class PaintingsAdapter extends ItemsAdapter<Painting, PaintingsAdapter.ViewHolder>
        implements View.OnClickListener {

    private XXListener mXXListener;

    public PaintingsAdapter(PoemFragment context) {
        setItemsList(Arrays.asList(Painting.getAllPaintings(context.getResources())));
    }

    @Override
    protected ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        final ViewHolder holder = new ViewHolder(parent);

        //设置监听
        holder.layout.setOnClickListener(this);
        holder.poem.setOnClickListener(this);

        return holder;
    }

    @Override
    protected void onBindHolder(ViewHolder holder, int position) {
        final Painting item = getItem(position);
        //设置tag
        holder.layout.setTag(R.id.poem_board_concision_linear_layout, item);

        //设置listView的具体内容
        setListViewContent(holder,item);
    }

    private void setListViewContent(ViewHolder holder,Painting item){
        //设置诗歌的内容
        RichText.from(item.getTitle()).into(holder.poem);
        //设置头像

        //设置用户名

        //设置诗歌的发表时间

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.poem_board_concision_linear_layout:
                final Painting item = (Painting) view.getTag(R.id.poem_board_concision_linear_layout);
                mXXListener.onXXClick(view, item);
                break;
            case R.id.poem_board_poem:
                View parent = (View) view.getParent();
                mXXListener.onXXClick(parent, (Painting) parent.getTag(R.id.poem_board_concision_linear_layout));
                break;
        }
    }

    public interface XXListener {
        void onXXClick(View view, Painting item);
    }

    public void setOnXXClickListener(XXListener XXListener) {
        this.mXXListener = XXListener;
    }

    //ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
    static class ViewHolder extends ItemsAdapter.ViewHolder {
        final TextView poem;
        final LinearLayout layout;
        final TextView poem_board_concision_username;
        final TextView poem_board_concision_user_uplode_time;

        ViewHolder(ViewGroup parent) {
            super(Views.inflate(parent, R.layout.poem_board_concision));
            poem = Views.find(itemView, R.id.poem_board_poem);
            layout = Views.find(itemView, R.id.poem_board_concision_linear_layout);
            poem_board_concision_username = Views.find(itemView, R.id.poem_board_concision_username);
            poem_board_concision_user_uplode_time = Views.find(itemView, R.id.poem_board_concision_user_uplode_time);
        }
    }
}
