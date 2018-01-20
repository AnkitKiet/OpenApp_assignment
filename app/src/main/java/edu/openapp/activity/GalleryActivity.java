package edu.openapp.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.openapp.R;
import edu.openapp.adapters.GalleryAdapter;
import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;
import edu.openapp.presenter.GalleryPresenter;

/**
 * Created by Ankit on 21/01/18.
 */

public class GalleryActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    private AppDatabase appDatabase;
    List<MemberModel> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        appDatabase = AppDatabase.getDatabase(getApplication());
        GalleryPresenter presenter = new GalleryPresenter(GalleryActivity.this, this);
        presenter.getImages(appDatabase);
    }

    public void setList(List<MemberModel> list) {
        this.mList = list;
        makelistofimages();
    }

    private void makelistofimages() {
        List<File> listFiles=new ArrayList<>();
        for (MemberModel model : mList) {
            File f = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "file" + model.getName() + ".jpg");
            listFiles.add(model.getId()-1,f);
        }
        setAdapter(listFiles);


    }

    private void setAdapter(List<File> listFiles) {

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        GalleryAdapter adapter = new GalleryAdapter(GalleryActivity.this, listFiles);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
