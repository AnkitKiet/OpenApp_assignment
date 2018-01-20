package edu.openapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.openapp.R;
import edu.openapp.adapters.MemberListAdapter;
import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;

/**
 * Created by Ankit on 19/01/18.
 */

public class DashboardFragment extends Fragment {

    private View parentView;
    private AppDatabase appDatabase;
    private RecyclerView recycler;
    public MemberListAdapter listAdapter;
    public List<MemberModel> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_list, container, false);
        recycler = parentView.findViewById(R.id.recycler);
        populate();
        return parentView;
    }

    private void populate() {
        list = new ArrayList<>();
        appDatabase = AppDatabase.getDatabase(getActivity().getApplication());
        getAllMembers();


    }

    public void getAllMembers() {
        new getAllMembersAsyncTask(appDatabase).execute();
    }

    private class getAllMembersAsyncTask extends AsyncTask<Void, Void, Void> {

        private AppDatabase db;

        getAllMembersAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(Void... params) {
            list = db.itemAndPersonModel().getAllMember();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setAdapter();
                }
            });
        }
    }



    private void setAdapter() {
        listAdapter = new MemberListAdapter(getActivity(), list);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
        recycler.setItemViewCacheSize(20);
        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recycler.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

    }


}
