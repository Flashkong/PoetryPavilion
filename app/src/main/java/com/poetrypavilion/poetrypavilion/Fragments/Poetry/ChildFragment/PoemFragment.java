package com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.poetrypavilion.poetrypavilion.Adapters.PaintingsAdapter;
import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.Items.Painting;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.BackHande.BackHandlerHelper;
import com.poetrypavilion.poetrypavilion.Utils.BackHande.FragmentBackHandler;

public class PoemFragment extends BaseFragment{

    private View poem_board_detail_layout;
    private UnfoldableView poem_detail_unfoldableview;
    private View PoemView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        PoemView = inflater.inflate(R.layout.poem_fragment,container,false);


        final ListView listView = Views.find(PoemView, R.id.poem_boards_list_view);
        PaintingsAdapter adapter = new PaintingsAdapter(this);
        listView.setAdapter(adapter);


        poem_board_detail_layout = Views.find(PoemView, R.id.poem_board_detail_layout);
        poem_board_detail_layout.setVisibility(View.INVISIBLE);

        poem_detail_unfoldableview = Views.find(PoemView, R.id.poem_detail_unfoldableview);

        poem_detail_unfoldableview.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                //这一句在点击listview里面的具体部件的时候首先执行
                poem_board_detail_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                //这个在打开完成具体的页面之后调用
                int i=0;
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                //这个在开始关闭的时候调用
                int i=0;
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                //这个在关闭即将结束的时候调用
                poem_board_detail_layout.setVisibility(View.INVISIBLE);
            }
        });

        adapter.setOnXXClickListener(new PaintingsAdapter.XXListener(){
            @Override
            public void onXXClick(View view, Painting item) {
                openDetails(view,item);
            }
        });
        return PoemView;
    }


    public void openDetails(View coverView, Painting painting) {
        final TextView title = Views.find(poem_board_detail_layout, R.id.details_title);

        title.setText(painting.getTitle());
        poem_detail_unfoldableview.unfold(coverView, poem_board_detail_layout);
        /*
        这里当时是设置具体的图片内容的地方，里面包含了描述的格式
        SpannableBuilder builder = new SpannableBuilder(PoemView.getContext());
        builder.createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.year).append(": ")
                .clearStyle()
                .append(painting.getYear()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.location).append(": ")
                .clearStyle()
                .append(painting.getLocation());
        description.setText(builder.build());*/
    }

    @Override
    public boolean onBackPressed() {
        if (poem_detail_unfoldableview != null
                && (poem_detail_unfoldableview.isUnfolded() || poem_detail_unfoldableview.isUnfolding())) {
            poem_detail_unfoldableview.foldBack();
            return true;
        }else {
            return false;
        }
    }
}
