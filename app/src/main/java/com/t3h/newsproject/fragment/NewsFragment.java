package com.t3h.newsproject.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.newsproject.Const;
import com.t3h.newsproject.MainActivity;
import com.t3h.newsproject.R;
import com.t3h.newsproject.WebViewActivity;
import com.t3h.newsproject.api.ApiBuilder;
import com.t3h.newsproject.dao.AppDatabase;
import com.t3h.newsproject.model.DownloadAsync;
import com.t3h.newsproject.model.News;
import com.t3h.newsproject.model.NewsAdapter;
import com.t3h.newsproject.model.NewsResponsive;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends BaseFragment<MainActivity> implements Callback<NewsResponsive>, SearchView.OnQueryTextListener, NewsAdapter.ItemClickListener, View.OnClickListener, DownloadAsync.DownloadCallback {
    private RecyclerView recyclerNews;
    private NewsAdapter adapter;
    private String language = "vi";
    private String TAG = "NewsFragment";
    private SearchView searchView;
    private List<News> currentData;
    private Dialog dialog;
    private MenuItem itemCountry;
    private String currentLanguage = "vi";
    public String path;
    private News item;
    private Dialog dialogLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public String getTitle() {
        return "News";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        recyclerNews = findViewById(R.id.recycler_news);
        adapter = new NewsAdapter(getContext());
        recyclerNews.setAdapter(adapter);
        recyclerNews.addItemDecoration(new DividerItemDecoration(recyclerNews.getContext(), DividerItemDecoration.VERTICAL));
        adapter.setItemClickListener(this);
        setHasOptionsMenu(true);
        if (currentData != null) {
            adapter.setData(currentData);
        }
        language = currentLanguage;

        dialogLoading = new Dialog(getContext());
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoading.setContentView(R.layout.dialog_loading);

        ImageButton btn_vi, btn_en;
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog);
        btn_vi = dialog.findViewById(R.id.btn_vi);
        btn_en = dialog.findViewById(R.id.btn_en);
        btn_vi.setOnClickListener(this);
        btn_en.setOnClickListener(this);
        Log.d("NewsFag", "created");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem itemSearch = menu.findItem(R.id.search_bar);
        searchView = (SearchView) itemSearch.getActionView();
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        itemCountry = menu.findItem(R.id.choose_country);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_country:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        dialog.show();
    }

    //    public void initData() {
//        if (keySearch != null) {
//            String apiKey = "8921d0b0544848a9b059d19e8a93b71b";
//            String language = "vi";
//            ApiBuilder.getInstance().getNews(keySearch, apiKey, language).enqueue(this);
//            Log.d(TAG, "not null");
//        }
//    }

    @Override
    public void onResponse(Call<NewsResponsive> call, Response<NewsResponsive> response) {
        List<News> news = response.body().getNews();
        News[] arr = new News[news.size()];
        news.toArray(arr);
        Log.d(TAG, "response " + arr.length);
        adapter.setData(Arrays.asList(arr));
        currentData = news;
        dialogLoading.dismiss();
    }

    @Override
    public void onFailure(Call<NewsResponsive> call, Throwable t) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String keySearch = searchView.getQuery().toString();
        String apiKey = "8921d0b0544848a9b059d19e8a93b71b";
        ApiBuilder.getInstance().getNews(keySearch, apiKey, language).enqueue(this);
        dialogLoading.show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onItemClickListener(int position) {
        String url = adapter.getData().get(position).getUrl();
        Intent intent = new Intent(getParentActivity(), WebViewActivity.class);
        intent.putExtra(Const.EXTRA_URL, url);
        getParentActivity().startActivity(intent);
        Log.d("NewsFrag", "show web view:" + url);
    }

    @Override
    public void onItemLongClickListener(int position) {
        dialogLoading.show();
        item = adapter.getData().get(position);

        String link = item.getUrl();
        if (link.isEmpty()) {
            Toast.makeText(getContext(), "Cant save this news", Toast.LENGTH_SHORT).show();
            return;
        }
        DownloadAsync async = new DownloadAsync(this);
        async.execute(link);

//        if (path != null) {
//            item.setUrl(path);
//        }
//        AppDatabase.getInstance(getContext()).getNewsDao().insert(item);
//        getParentActivity().getFmSave().initData();


//        Toast.makeText(getContext(), "Tin đã lưu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_vi:
                language = "vi";
                itemCountry.setIcon(R.drawable.ic_vietnam);
                currentLanguage = "vi";
                dialog.dismiss();
                break;
            case R.id.btn_en:
                language = "en";
                itemCountry.setIcon(R.drawable.ic_england);
                currentLanguage = "en";
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onDownloadUpdate(int percent) {

    }

    @Override
    public void onDownloadSuccess(String path) {
        dialogLoading.dismiss();
        this.path = path;

        if (path != null) {
            long id = item.getId();
            String title = item.getTitle();
            String desc = item.getDesc();
            String img = item.getImg();
            String date = item.getDate();
            News news = new News(id, title, desc, path, img, date);
            AppDatabase.getInstance(getContext()).getNewsDao().insert(news);
        }
        getParentActivity().getFmSave().initData();
        Toast.makeText(getContext(), "Download complete", Toast.LENGTH_SHORT).show();
    }
}
