package edu.openapp.view;

import java.util.List;

import edu.openapp.model.MemberModel;

/**
 * Created by Ankit on 21/01/18.
 */

public interface DashboardView {

    void getAllMembers();
    void setList(List<MemberModel> list);
    void setAdapter();
    void populate();


}
