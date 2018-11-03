package com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.poetrypavilion.poetrypavilion.Adapters.PaintingsAdapter;
import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.Items.Painting;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.databinding.PoemFragmentBinding;

public class PoemFragment extends BaseFragment<PoemFragmentBinding>{

    @Override
    public int setViewXml() {
        return R.layout.poem_fragment;
    }

    @Override
    public void OtherProcess() {
        final ListView listView = bindingView.poemInclude.poemBoardsListView;
        PaintingsAdapter adapter = new PaintingsAdapter(this);
        listView.setAdapter(adapter);

        bindingView.poemInclude.poemBoardDetailLayout.setVisibility(View.INVISIBLE);

        bindingView.poemInclude.poemDetailUnfoldableview.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                //这一句在点击listview里面的具体部件的时候首先执行
                bindingView.poemInclude.poemBoardDetailLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                //这个在打开完成具体的页面之后调用
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                //这个在开始关闭的时候调用
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                //这个在关闭即将结束的时候调用
                bindingView.poemInclude.poemBoardDetailLayout.setVisibility(View.INVISIBLE);
            }
        });

        adapter.setOnXXClickListener(new PaintingsAdapter.XXListener(){
            @Override
            public void onXXClick(View view, Painting item) {
                openDetails(view,item);
            }
        });
    }


    public void openDetails(View coverView, Painting painting) {
        final TextView title = Views.find(bindingView.poemInclude.poemBoardDetailLayout, R.id.details_title);
        title.setText(painting.getTitle());
        bindingView.poemInclude.poemDetailUnfoldableview.unfold(coverView, bindingView.poemInclude.poemBoardDetailLayout);
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
        if (bindingView.poemInclude.poemDetailUnfoldableview != null
                && (bindingView.poemInclude.poemDetailUnfoldableview.isUnfolded()
                || bindingView.poemInclude.poemDetailUnfoldableview.isUnfolding())) {
            bindingView.poemInclude.poemDetailUnfoldableview.foldBack();
            return true;
        }else {
            return false;
        }
    }
}
