package com.example.administrator.babygrowth01.babyrecords.Main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.administrator.babygrowth01.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/31.
 */
public class AddRecordHelper {

    public AddRecordHelper(Child_Time_Record_Activity activity) {
        this.activity = activity;
    }

    public void openGallery(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, ADD_EDIT_RECORD_GALLERY);
    }

    public void openCamera(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri=getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);//set the video image quality to high
        activity.startActivityForResult(intent,ADD_EDIT_RECORD_CAMERA);// start the video capture intent
    }

    public void editRecord(String pic_uri,int child_id,Context applicationContext){

        activity.findViewById(R.id.edit).setVisibility(View.VISIBLE);
        new RecordEdit(activity,pic_uri,child_id,applicationContext);

    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        String state=Environment.getExternalStorageState();
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if(Environment.MEDIA_MOUNTED.equals(state)){
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.
            // Create the storage directory if it does not exist

            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE){
                pic_path=mediaStorageDir.getPath() + File.separator +
                        "IMG_"+ timeStamp + ".jpg";
                mediaFile = new File(pic_path);
            } else if(type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_"+ timeStamp + ".mp4");
            } else {
                return null;
            }
            return mediaFile;
        }else{
            // else save to the inner storage
            File file=new File(activity.getFilesDir().toString()+"/record/"+"IMG_"+timeStamp+".jpg");
            System.out.println("back internal");
            return file;
        }
    }

    public String getPic_path() {
        return pic_path;
    }

    private Child_Time_Record_Activity activity;
    private final int ADD_EDIT_RECORD_GALLERY=5;
    private final int ADD_EDIT_RECORD_CAMERA=6;
    private final int MEDIA_TYPE_IMAGE = 1;
    private final int MEDIA_TYPE_VIDEO = 2;
    private String pic_path;

}
