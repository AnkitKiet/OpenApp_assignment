package edu.openapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.openapp.R;
import edu.openapp.global.AppDatabase;
import edu.openapp.model.MemberModel;
import edu.openapp.presenter.AddMemberPresenter;
import edu.openapp.view.AddMemberView;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

/**
 * Created by Ankit on 19/01/18.
 */

public class AddMemberActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, AddMemberView {

    private AppDatabase appDatabase;
    @Bind(R.id.edtName)
    EditText edtName;
    @Bind(R.id.edtAddress)
    EditText edtAddress;
    @Bind(R.id.edtdob)
    TextView edtDob;
    @Bind(R.id.btnSelectImage)
    Button btnImage;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;
    @Bind(R.id.back)
    ImageButton back;
    @Bind(R.id.txtTitle)
    TextView txtTitle;

    @OnClick(R.id.back)
    void click() {
        finish();
    }

    boolean isUploadedPic = false;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Date dob;
    private String[] Permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int PERMS_REQUEST_CODE = 140;
    private Uri uri;
    private static final int CAMERA = 3;
    private String image;
    private String imageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        populate();
    }

    @Override
    public void populate() {
        txtTitle.setText("Add New Member");
        appDatabase = AppDatabase.getDatabase(getApplication());
        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_field_filled()) {
                    String name = edtName.getText().toString().trim();
                    String address = edtAddress.getText().toString().trim();
                    Date lud = Calendar.getInstance().getTime();
                    AddMemberPresenter presenter = new AddMemberPresenter(AddMemberActivity.this, AddMemberActivity.this);
                    image = getSharedPreferences("Temp", MODE_PRIVATE).getString("filepath", "");
                    imageName = edtName.getText().toString() + edtAddress.getText().toString();
                    presenter.addMember(new MemberModel(name, address, lud, imageName, dob), appDatabase);
                    Toast.makeText(AddMemberActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddMemberActivity.this, "Fill all entries", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();

            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().length() != 0) {
                    if ((ContextCompat.checkSelfPermission(AddMemberActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(AddMemberActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(AddMemberActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                        showFileChooser();
                    } else {
                        ActivityCompat.requestPermissions(AddMemberActivity.this, Permissions, PERMS_REQUEST_CODE);

                    }
                } else {
                    Toast.makeText(AddMemberActivity.this, "Please Enter Name First", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*Trying out only with camera
    * Will implement Gallery option later
    */
    @Override
    public void showFileChooser() {
        Intent CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "file" + edtName.getText() +edtAddress.getText()+ ".jpg");
        uri = FileProvider.getUriForFile(AddMemberActivity.this, "edu.openapp.provider", file);
        getSharedPreferences("Temp", MODE_PRIVATE).edit().putString("Uri", file.toString()).apply();
        getSharedPreferences("Temp", MODE_PRIVATE).edit().putString("filepath", file.getAbsolutePath()).apply();

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        CamIntent.putExtra("return-data", false);
        CamIntent.putExtra("outputX", 60);
        CamIntent.putExtra("outputY", 40);
        CamIntent.putExtra("aspectX", 16);
        CamIntent.putExtra("aspectY", 9);
        CamIntent.putExtra("scale", true);
        CamIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(CamIntent, CAMERA);
    }

    @Override
    public void finishUpdate() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            uri = Uri.parse(getSharedPreferences("Temp", MODE_PRIVATE).getString("Uri", ""));
            compressImage();
        }
    }


    /*
    *Library used to reduce size of large image captured from camera
    *App getting too slow while loading local images if image is not compressed
    * I have tried to handle this situation by capturing the image then saving temporary in SDcard at location, then reducing size and again writing this reduced image at same location
    */
    @Override
    public void compressImage() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "file" + edtName.getText()+edtAddress.getText() + ".jpg");

        Luban.compress(AddMemberActivity.this, file)
                .putGear(Luban.FIRST_GEAR)      // set the compress mode, default is : THIRD_GEAR
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        isUploadedPic = true;
                        Toast.makeText(AddMemberActivity.this, "Compressed", Toast.LENGTH_SHORT).show();
                        File existfile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                "file" + edtName.getText() +edtAddress.getText()+ ".jpg");
                        if (existfile.exists())
                            existfile.delete();
                        File Finalfile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                "file" + edtName.getText()+edtAddress.getText() + ".jpg");
                        byte[] buffer = new byte[1024];
                        int length;
                        FileInputStream instream = null;
                        try {
                            instream = new FileInputStream(file);
                            FileOutputStream outstream = new FileOutputStream(Finalfile);
                            while ((length = instream.read(buffer)) > 0) {
                                try {
                                    outstream.write(buffer, 0, length);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            instream.close();
                            outstream.close();
                            Toast.makeText(AddMemberActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddMemberActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    //Checking Validations Of Fields Filled
    @Override
    public boolean check_field_filled() {
        boolean valid = false;

        if (edtAddress.getText().toString().length() != 0 && edtDob.getText().toString().length() != 0 && edtName.getText().toString().length() != 0 && isUploadedPic)
            valid = true;

        return valid;
    }

    //Handelling Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_REQUEST_CODE) {
            btnImage.performClick();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dob = calendar.getTime();
        int yr = dob.getYear();
        if (yr > 100) {
            yr = dob.getYear() - 100;
        }
        edtDob.setText(String.valueOf(dob.getDate()) + " / " + String.valueOf(dob.getMonth() + 1) + " / " + String.valueOf(yr));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
