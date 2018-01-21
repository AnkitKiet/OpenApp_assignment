package edu.openapp.view;

import java.io.File;
import java.util.List;

import edu.openapp.model.MemberModel;

/**
 * Created by Ankit on 21/01/18.
 */

public interface GalleryView {

    void setList(List<MemberModel> list);
    void setAdapter(List<File> listFiles);
    void makelistofimages();

}
