package edu.openapp.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.openapp.activity.GalleryActivity;
import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;

/**
 * Created by Ankit on 21/01/18.
 */

public class GalleryPresenter {

    private Context context;
    private List<MemberModel> mList = new ArrayList<>();
    private GalleryActivity galleryActivity;

    public GalleryPresenter(Context context, GalleryActivity galleryActivity) {
        this.context = context;
        this.galleryActivity = galleryActivity;
    }

    public void getImages(AppDatabase appDatabase) {
        new GalleryPresenter.getAsyncTask(appDatabase).execute();
    }

    private class getAsyncTask extends AsyncTask<Void, Void, Void> {

        private AppDatabase db;

        getAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "Fetching Data", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mList = db.itemAndPersonModel().getAllImage();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            galleryActivity.setList(mList);
        }
    }
}
