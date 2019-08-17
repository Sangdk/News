package com.t3h.newsproject.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.newsproject.MainActivity;
import com.t3h.newsproject.R;
import com.t3h.newsproject.dao.AppDatabase;
import com.t3h.newsproject.model.News;
import com.t3h.newsproject.model.NewsAdapter;

import java.util.List;

public class FavoriteFragment extends BaseFragment<MainActivity> implements NewsAdapter.ItemClickListener, PopupMenu.OnMenuItemClickListener {
    private TextView txtFavorite;
    private RecyclerView recyclerFavorite;
    private NewsAdapter adapter;
    private List<News> data;
    private int position;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    public String getTitle() {
        return "Favorite";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
    }

    public void initData() {
        data = AppDatabase.getInstance(getContext()).getNewsDao().getNewsFavorite();
        if (data != null) {
            adapter.setData(data);
            Log.d("FavoriteFrag", "init data");
        }
    }

    private void initView() {
        txtFavorite = findViewById(R.id.txt_favorite);
        recyclerFavorite = findViewById(R.id.recycler_favorite);
        adapter = new NewsAdapter(getContext());
        recyclerFavorite.setAdapter(adapter);
        adapter.setItemClickListener(this);
        Log.d("FavoriteFrag", "init view");
    }

    @Override
    public void onItemClickListener(int position) {

    }

    @Override
    public void onItemLongClickListener(int position) {
        this.position = position;
        PopupMenu popup = new PopupMenu(getContext(), recyclerFavorite
                .findViewHolderForAdapterPosition(position)
                .itemView);
        popup.inflate(R.menu.context_menu_favorite);
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        long id = data.get(position).getId();
        AppDatabase.getInstance(getContext()).getNewsDao().delFavorite(id);
        data.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, data.size());
        return false;
    }
}
