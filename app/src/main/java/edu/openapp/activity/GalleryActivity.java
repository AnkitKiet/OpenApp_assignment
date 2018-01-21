package edu.openapp.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.openapp.R;
import edu.openapp.adapters.GalleryAdapter;
import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;
import edu.openapp.presenter.GalleryPresenter;
import edu.openapp.view.GalleryView;

/**
 * Created by Ankit on 21/01/18.
 */

public class GalleryActivity extends BaseActivity implements GalleryView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.progressbar)
    public ProgressBar progressBar;
    private AppDatabase appDatabase;
    List<MemberModel> mList;
    @Bind(R.id.back)
    ImageButton back;
    @Bind(R.id.txtTitle)
    TextView txtTitle;

    @OnClick(R.id.back)
    void click() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        txtTitle.setText("Gallery");
        appDatabase = AppDatabase.getDatabase(getApplication());
        GalleryPresenter presenter = new GalleryPresenter(GalleryActivity.this, this);
        presenter.getImages(appDatabase);
    }


    @Override
    public void makelistofimages() {
        List<File> listFiles = new ArrayList<>();
        for (MemberModel model : mList) {
            File f = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "file" + model.getImage() + ".jpg");
            listFiles.add(model.getId() - 1, f);
        }
        setAdapter(listFiles);
    }

    @Override
    public void setList(List<MemberModel> list) {
        this.mList = list;
        makelistofimages();
    }

    @Override
    public void setAdapter(List<File> listFiles) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        GalleryAdapter adapter = new GalleryAdapter(GalleryActivity.this, listFiles);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
