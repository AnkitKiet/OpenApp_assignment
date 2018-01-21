package edu.openapp.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.openapp.R;
import edu.openapp.adapters.MemberListAdapter;
import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;
import edu.openapp.presenter.DashboardPresenter;
import edu.openapp.view.DashboardView;

/**
 * Created by Ankit on 19/01/18.
 */

public class DashboardFragment extends Fragment implements DashboardView {

    private View parentView;
    private AppDatabase appDatabase;
    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.progressbar)
    public ProgressBar progressBar;
    public MemberListAdapter listAdapter;
    public List<MemberModel> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_list, container, false);
        populate();
        return parentView;
    }
    @Override
    public void populate() {
        ButterKnife.bind(this, parentView);
        list = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        appDatabase = AppDatabase.getDatabase(getActivity().getApplication());
        getAllMembers();


    }

    @Override
    public void getAllMembers() {
       DashboardPresenter presenter= new DashboardPresenter(getActivity(),this);
        presenter.getMember(appDatabase);
    }

    @Override
    public void setList(List<MemberModel> list) {
        this.list = list;
        setAdapter();
    }

    @Override
    public void setAdapter() {
        Collections.sort(list, Collections.<MemberModel>reverseOrder());
        progressBar.setVisibility(View.GONE);
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
