package edu.openapp.presenter;

import android.content.Context;
import android.os.AsyncTask;

import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;

/**
 * Created by Ankit on 19/01/18.
 */

public class AddMemberPresenter {

    private Context context;

    public AddMemberPresenter(Context context) {
        this.context = context;
    }


    public void addMember(final MemberModel memberModel, AppDatabase appDatabase) {
        new addAsyncTask(appDatabase).execute(memberModel);
    }

    private class addAsyncTask extends AsyncTask<MemberModel, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(final MemberModel... params) {
            db.itemAndPersonModel().addMember(params[0]);
            return null;
        }

    }

}
