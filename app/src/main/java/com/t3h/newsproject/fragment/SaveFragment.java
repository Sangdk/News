package com.t3h.newsproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.newsproject.Const;
import com.t3h.newsproject.MainActivity;
import com.t3h.newsproject.R;
import com.t3h.newsproject.WebViewActivity;
import com.t3h.newsproject.dao.AppDatabase;
import com.t3h.newsproject.model.News;
import com.t3h.newsproject.model.NewsAdapter;

import java.util.List;

public class SaveFragment extends BaseFragment<MainActivity> implements NewsAdapter.ItemClickListener, PopupMenu.OnMenuItemClickListener {
    private TextView txtSave;
    private RecyclerView recyclerSave;
    private NewsAdapter adapter;
    private int position;
    private List<News> data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_saved;
    }

    @Override
    public String getTitle() {
        return "Saved";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        Log.d("SaveFrag", "created");
    }

    public void initData() {
        data = AppDatabase.getInstance(getContext()).getNewsDao().getAll();
        if (data != null) {
            adapter.setData(data);
        }
        setText();
    }

    private void setText() {
        StringBuffer txt = new StringBuffer();
        txt.append("Saved: ");
        txt.append(data.size());
        txt.append(" news");
        txtSave.setText(txt.toString());
    }

    private void initView() {
        txtSave = findViewById(R.id.txt_save);
        recyclerSave = findViewById(R.id.recycler_saved);

        adapter = new NewsAdapter(getContext());
        recyclerSave.setAdapter(adapter);
        adapter.setItemClickListener(this);
    }

    @Override
    public void onItemClickListener(int position) {
        String url = adapter.getData().get(position).getUrl();
        Intent intent = new Intent(getParentActivity(), WebViewActivity.class);
        intent.putExtra(Const.EXTRA_PATH, url);
        getParentActivity().startActivity(intent);
        Log.d("SaveFrag", "show web view:" + url);

    }

    @Override
    public void onItemLongClickListener(int position) {
        this.position = position;
        PopupMenu popup = new PopupMenu(getContext(), recyclerSave
                .findViewHolderForAdapterPosition(position)
                .itemView);
        popup.inflate(R.menu.context_menu_save);
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        long id = data.get(position).getId();
        switch (item.getItemId()) {
            case R.id.it_like:
                AppDatabase.getInstance(getContext()).getNewsDao().setFavorite(id);
                getParentActivity().getFmFavorite().initData();
                break;
            case R.id.it_delete:
                data.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, data.size());
                setText();

                AppDatabase.getInstance(getContext()).getNewsDao().deleteByID(id);
                break;
        }
        return false;
    }
}
