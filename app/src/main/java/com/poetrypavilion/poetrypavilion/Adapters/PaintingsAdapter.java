package com.poetrypavilion.poetrypavilion.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.alexvasilkov.android.commons.ui.ContextHelper;
import com.alexvasilkov.android.commons.ui.Views;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.PoemFragment;
import com.poetrypavilion.poetrypavilion.Items.Painting;
import com.poetrypavilion.poetrypavilion.R;


import java.util.Arrays;


//PaintingAdapter其实不是Painting的adapter，而是大概的那个滑动的listview的adapter
public class PaintingsAdapter extends ItemsAdapter<Painting, PaintingsAdapter.ViewHolder>
        implements View.OnClickListener {

    public PaintingsAdapter(PoemFragment context) {
        setItemsList(Arrays.asList(Painting.getAllPaintings(context.getResources())));
    }

    @Override
    protected ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        final ViewHolder holder = new ViewHolder(parent);
        holder.layout.setOnClickListener(this);
        return holder;
    }

    @Override
    protected void onBindHolder(ViewHolder holder, int position) {
        final Painting item = getItem(position);

        holder.layout.setTag(R.id.poem_board_concision_linear_layout, item);
        holder.title.setText(item.getTitle());
    }

    @Override
    public void onClick(View view) {
        final Painting item = (Painting) view.getTag(R.id.poem_board_concision_linear_layout);
        mXXListener.onXXClick(view,item);
//        final Activity activity = ContextHelper.asActivity(view.getContext());
//        ((PoemFragment) activity).openDetails(view, item);
    }

    private XXListener mXXListener;
    public interface XXListener {
        void onXXClick(View view, Painting item);
    }

    public void setOnXXClickListener (XXListener  XXListener) {
        this.mXXListener = XXListener;
    }

    //ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
    static class ViewHolder extends ItemsAdapter.ViewHolder {
        final TextView title;
        final LinearLayout layout;

        ViewHolder(ViewGroup parent) {
            super(Views.inflate(parent, R.layout.poem_board_concision));
            title = Views.find(itemView, R.id.poem_board_user_name);
            layout =Views.find(itemView,R.id.poem_board_concision_linear_layout);
        }
    }
}
