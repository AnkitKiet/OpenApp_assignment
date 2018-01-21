package edu.openapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import edu.openapp.presenter.EditMemberPresenter;
import edu.openapp.view.EditMemberView;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

/**
 * Created by Ankit on 20/01/18.
 */

public class EditMemberActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, EditMemberView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
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
    @Bind(R.id.editSwitch)
    SwitchCompat editSwitch;
    @Bind(R.id.back)
    ImageButton back;
    @Bind(R.id.txtTitle)
    TextView txtTitle;

    @OnClick(R.id.back)
    void click() {
        finish();
    }

    boolean isUploadedPic = false;
    private AppDatabase appDatabase;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private Date dob;
    private Context context;
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
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        context = EditMemberActivity.this;
        txtTitle.setText(R.string.edit);
        appDatabase = AppDatabase.getDatabase(getApplication());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final MemberModel model = bundle.getParcelable("data");
        edtName.setText(model.getName());
        imageName=model.getImage();
        Date ludate = model.getTimestamp();
        int yr = ludate.getYear();
        if (yr > 100) {
            yr = ludate.getYear() - 100;
        }
        btnSubmit.setVisibility(View.GONE);
        String ldate = ludate.getDate() + "/" + ludate.getMonth() + "/" + yr;
        edtDob.setText(ldate);
        edtAddress.setText(model.getAddress());
        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(context, EditMemberActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date lud = Calendar.getInstance().getTime();
                if (isUploadedPic)
                    image = getSharedPreferences("Temp", MODE_PRIVATE).getString("filepath", "");

                else {
                    image = model.getImage();
                }
                if (check_field_filled()) {
                    MemberModel memberModel = new MemberModel(edtName.getText().toString(), edtAddress.getText().toString(), lud, imageName, dob);
                    memberModel.setId(model.getId());
                    EditMemberPresenter presenter = new EditMemberPresenter(EditMemberActivity.this, EditMemberActivity.this);
                    presenter.updateMember(memberModel, appDatabase);
                } else {
                    Toast.makeText(context, "Missing Values", Toast.LENGTH_SHORT).show();
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
                    if ((ContextCompat.checkSelfPermission(EditMemberActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(EditMemberActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(EditMemberActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                        showFileChooser();
                    } else {
                        ActivityCompat.requestPermissions(EditMemberActivity.this, Permissions, PERMS_REQUEST_CODE);

                    }
                } else {
                    Toast.makeText(context, "Please enter name first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edtName.setEnabled(false);
        edtAddress.setEnabled(false);
        edtDob.setEnabled(false);
        btnImage.setEnabled(false);

        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtName.setEnabled(true);
                    edtAddress.setEnabled(true);
                    edtDob.setEnabled(true);
                    btnImage.setEnabled(true);
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnImage.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                } else {
                    edtName.setEnabled(false);
                    edtAddress.setEnabled(false);
                    edtDob.setEnabled(false);
                    btnImage.setEnabled(false);
                    btnSubmit.setVisibility(View.GONE);
                    btnImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));


                }
            }
        });
    }

    @Override
    public void finishUpdate() {
        finish();
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
    public void showFileChooser() {
        Intent CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "file" + imageName + ".jpg");
        uri = FileProvider.getUriForFile(EditMemberActivity.this, "edu.openapp.provider", file);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            uri = Uri.parse(getSharedPreferences("Temp", MODE_PRIVATE).getString("Uri", ""));
            compressImage();
        }
    }

    @Override
    public void compressImage() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "file" + imageName+ ".jpg");

        Luban.compress(EditMemberActivity.this, file)
                .putGear(Luban.FIRST_GEAR)       // set the compress mode, default is : THIRD_GEAR
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        isUploadedPic = true;
                        Toast.makeText(EditMemberActivity.this, "Compressed", Toast.LENGTH_SHORT).show();
                        File existfile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                "file" + imageName+ ".jpg");
                        if (existfile.exists())
                            existfile.delete();
                        File Finalfile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                "file" + imageName + ".jpg");
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
                            Toast.makeText(EditMemberActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(EditMemberActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    //Checking Validations Of Fields Filled
    @Override
    public boolean check_field_filled() {
        boolean valid = false;

        if (edtAddress.getText().toString().length() != 0 && edtDob.getText().toString().length() != 0 && edtName.getText().toString().length() != 0)
            valid = true;

        return valid;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_REQUEST_CODE) {
            btnImage.performClick();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
