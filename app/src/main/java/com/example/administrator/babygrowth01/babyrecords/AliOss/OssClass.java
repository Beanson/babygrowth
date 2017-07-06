package com.example.administrator.babygrowth01.babyrecords.AliOss;

/**
 * Created by Administrator on 2016/5/1.
 */

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.example.administrator.babygrowth01.babyrecords.AliOss.Helper.GetObjectSamples;
import com.example.administrator.babygrowth01.babyrecords.AliOss.Helper.ManageBucketSamples;
import com.example.administrator.babygrowth01.babyrecords.AliOss.Helper.PutObjectSamples;
import com.example.administrator.babygrowth01.babyrecords.AliOss.Helper.ListObjectsSamples;
import com.example.administrator.babygrowth01.babyrecords.AliOss.Helper.ManageObjectSamples;
import com.example.administrator.babygrowth01.babyrecords.AliOss.Helper.ResuambleUploadSamples;
import com.example.administrator.babygrowth01.babyrecords.AliOss.Helper.SignURLSamples;
import com.example.administrator.babygrowth01.babyrecords.AliOss.Helper.MultipartUploadSamples;

public class OssClass {

    private OSS oss;
    // 运行sample前需要配置以下字段为有效的值
    private static final String endpoint = ";
    private static final String accessKeyId = "";
    private static final String accessKeySecret = "";
    private String uploadFilePath = "";
    private static final String testBucket = "";  /* bucket name*/
    private String uploadObject = "";
    private static final String downloadObject = "";
    private Context context; //the type in detail is getApplicationContext()

    public OssClass(Context context){
        this.context=context;
        init();
    }

    public void init(){

        // init the full path and name of the picture in oss
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(context, endpoint, credentialProvider, conf);
    }

    /**
     * upload image or video to oss
     */
    public void putObjectToOss(final String uploadFilePath){
        String []temp=uploadFilePath.split("\\/");
        final String uploadTarget=uploadObject+temp[temp.length-1];

        new Thread(new Runnable() {
            @Override
            public void run() {
                new PutObjectSamples(oss, testBucket, uploadTarget, uploadFilePath).asyncPutObjectFromLocalFile();
            }
        }).start();
    }

    public void delete(String objectKey){

        String []temp=objectKey.split("\\/");
        final String objectKey2=uploadObject+temp[temp.length-1];

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建删除请求
                DeleteObjectRequest delete = new DeleteObjectRequest(testBucket, objectKey2);
                // 异步删除
                OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
                    @Override
                    public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                        Log.d("asyncCopyAndDelObject", "success!");
                    }

                    @Override
                    public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        // 请求异常
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                        }
                        if (serviceException != null) {
                            // 服务异常
                            Log.e("ErrorCode", serviceException.getErrorCode());
                            Log.e("RequestId", serviceException.getRequestId());
                            Log.e("HostId", serviceException.getHostId());
                            Log.e("RawMessage", serviceException.getRawMessage());
                        }
                    }
                });
                deleteTask.waitUntilFinished();
            }
        }).start();
    }

    /**
     * download image or video from oss
     */
    public void downLoadImageFromOss(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetObjectSamples(oss, testBucket, downloadObject).asyncGetObjectSample();
            }
        }).start();
    }


    public void listObject(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                new ListObjectsSamples(oss, testBucket).asyncListObjectsWithPrefix();
            }
        }).start();

    }

    public void manageObject(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                new ManageObjectSamples(oss, testBucket, uploadObject).headObject();
            }
        }).start();

    }

    public void multipart(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new MultipartUploadSamples(oss, testBucket, uploadObject, uploadFilePath).multipartUpload();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void uploadFromBreakPoint(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                new ResuambleUploadSamples(oss, testBucket, uploadObject, uploadFilePath).resumableUpload();
            }
        }).start();
    }

    //签名URL
    public void signURL(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SignURLSamples(oss, testBucket, uploadObject, uploadFilePath).presignConstrainedURL();
            }
        }).start();
    }

    public void bucketManagement(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                new ManageBucketSamples(oss, testBucket, uploadFilePath).deleteNotEmptyBucket();
            }
        }).start();
    }



}
