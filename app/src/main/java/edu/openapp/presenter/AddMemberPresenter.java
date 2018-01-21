package edu.openapp.presenter;

import android.content.Context;
import android.os.AsyncTask;

import edu.openapp.activity.AddMemberActivity;
import edu.openapp.activity.EditMemberActivity;
import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;
import edu.openapp.view.AddMemberView;

/**
 * Created by Ankit on 19/01/18.
 */

public class AddMemberPresenter {

    private Context context;
    private AddMemberActivity addMemberActivity;
    public AddMemberPresenter(Context context,AddMemberActivity addMemberActivity) {
        this.context = context;
        this.addMemberActivity=addMemberActivity;
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            addMemberActivity.finishUpdate();

        }
    }

}
