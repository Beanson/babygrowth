package com.example.administrator.babygrowth01.babyrecords.Main;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/3/4.
 */
public class AddBabyActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        child_id= ((int) intent.getExtras().get("portrait_count"))+1;
        setContentView(R.layout.child_info);
        init();
    }

    public void init(){
        // initialize the widgets
        iv_portrait_new= (ImageView)findViewById(R.id.iv_portrait_new);
        et_nickname_new= (EditText) findViewById(R.id.et_nickname_new);
        tv_birth_detail_new= (TextView) findViewById(R.id.tv_birth_detail_new);
        rg_gender_new= (RadioGroup) findViewById(R.id.rg_gender_new);
        bt_submit_new= (Button) findViewById(R.id.bt_submit_new);

        /** set the default date to the birth*/
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        monthOfYear=(calendar.get(Calendar.MONTH)+1);
        dayOfMonth=(calendar.get(Calendar.DAY_OF_MONTH)+1);
        tv_birth_detail_new.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
        birth_date=year+"-"+monthOfYear+"-"+dayOfMonth;

        /** set onclick event*/
        iv_portrait_new.setOnClickListener(this);
        tv_birth_detail_new.setOnClickListener(this);
        bt_submit_new.setOnClickListener(this);
        getGender();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_portrait_new:{
                addPortrait();
                break;
            }
            case R.id.tv_birth_detail_new:{
                getBirth();
                break;
            }
            case R.id.bt_submit_new:{
                onSubmit();
                break;
            }
        }
    }

    public void addPortrait(){

        /* open gallery to pick photos */
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode==RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            /** select pictures by all columns */
            Cursor cursor =getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            /** action to the cursor */
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            portrait_path_original = cursor.getString(columnIndex);
            cursor.close();

            /** show the picture to image view*/
            Picasso.with(this).load(new File(portrait_path_original)).into(iv_portrait_new);

            /** save a copy to the local file image folder*/
            Bitmap b=BitmapFactory.decodeFile(portrait_path_original);
            portrait_path=getFilesDir().getPath()+BabyInfo.BABY_INFO_FOLDER+"/portrait_image_"+child_id+".jpg";
            File file=new File(portrait_path);
            try {
                FileOutputStream fos=new FileOutputStream(file);
                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /** get the gender when select */
    public void getGender(){
        rg_gender_new.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                gender = radioButton.getText().toString();
            }
        });
    }

    /** open a datePicker to select date*/
    public void getBirth(){
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birth_date=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                tv_birth_detail_new.setText(birth_date);
            }
        }, year, monthOfYear, dayOfMonth).show();
    }

    public void onSubmit(){

        /* add new child info to database */
        addPortraitToDB();
        /* add new baby records to the database */
        addDefaultRecordToDB();
        /* prepare for result back*/
        Intent intent=new Intent(AddBabyActivity.this,Child_Time_Record_Activity.class);
        intent.putExtra("new_portrait_uri",portrait_path_original);
        setResult(RESULT_OK, intent);
        finish(); //end the activity and return back
    }

    public void addPortraitToDB(){

        /* before on submit back ,insert the information to database first */
        SQLiteDatabase dbWriter= MyResource.getSqLite().getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("child_id",child_id);
        values.put("portrait_uri",portrait_path);
        values.put("nick_name",et_nickname_new.getText().toString());
        values.put("gender",gender);
        values.put("birth", birth_date);
        dbWriter.insert(MyResource.getChildren_info_table(),null,values);
        dbWriter.close();
    }
    public void addDefaultRecordToDB(){
        /* add the default record to the database*/
        SQLiteDatabase dbWrite=MyResource.getSqLite().getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("child_id",child_id);
        cv.put("pic_uri", "null_pic");
        cv.put("note", "null_note");
        cv.put("date_time",monthOfYear+"/"+dayOfMonth);
        dbWrite.insert(MyResource.getRecord_table(), null, cv);
        dbWrite.close();
    }

    private ImageView iv_portrait_new;
    private TextView tv_birth_detail_new;
    private EditText et_nickname_new;
    private RadioGroup rg_gender_new;
    private Button bt_submit_new;
    private int RESULT_LOAD_IMAGE = 2;
    private int child_id=0;

    /* about the information ready to add ,load and show*/
    String portrait_path="";
    String portrait_path_original="";
    int year,monthOfYear,dayOfMonth;
    String birth_date,gender;
}
