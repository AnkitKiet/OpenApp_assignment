package edu.openapp.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.openapp.fragments.DashboardFragment;
import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;

/**
 * Created by Ankit on 21/01/18.
 */

public class DashboardPresenter {

    private List<MemberModel>list=new ArrayList<>();
    private Context context;
    private DashboardFragment fragment;
    public  DashboardPresenter(Context context, DashboardFragment fragment){
        this.context=context;
        this.fragment=fragment;
    }

    public void getMember(final AppDatabase appDatabase) {
        new getAllMembersAsyncTask(appDatabase).execute();
    }

    public class getAllMembersAsyncTask extends AsyncTask<Object, Object, List<MemberModel>> {

        private AppDatabase db;

        getAllMembersAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MemberModel> doInBackground(Object... params) {
            list = db.itemAndPersonModel().getAllMember();
            return list;
        }

        @Override
        protected void onPostExecute(List<MemberModel> list) {
            super.onPostExecute(list);
            fragment.setList(list);

        }
    }


}
