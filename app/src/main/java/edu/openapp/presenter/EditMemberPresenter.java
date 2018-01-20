package edu.openapp.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;

/**
 * Created by Ankit on 21/01/18.
 */

public class EditMemberPresenter {

    Context context;

    public EditMemberPresenter(Context context) {
        this.context = context;
    }


    public void updateMember(final MemberModel memberModel, AppDatabase appDatabase) {
        new EditMemberPresenter.updateAsyncTask(appDatabase).execute(memberModel);
    }

    private class updateAsyncTask extends AsyncTask<MemberModel, Void, Void> {

        private AppDatabase db;

        updateAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "Updating", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(final MemberModel... params) {
            db.itemAndPersonModel().updateMember(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
