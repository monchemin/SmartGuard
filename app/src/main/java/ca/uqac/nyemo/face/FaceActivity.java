package ca.uqac.nyemo.face;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import ca.uqac.nyemo.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ca.uqac.nyemo.receivers.AppReceiver;
import ca.uqac.nyemo.utils.EigenFaces;
import ca.uqac.nyemo.utils.PermissionRequest;
import ca.uqac.nyemo.utils.SysFonctions;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;


public class FaceActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "FaceActivity";
    JavaCameraView javaCameraView;
    Mat mRgba;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage, mface;
    private int absoluteFaceSize;
    private AppReceiver appReceiver;
    private boolean recognize = false;
    //protected ArrayList<Mat> detectfaces = new ArrayList<Mat>();
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS: {
                    initializeOpenCVDependencies();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;

                }
            }

        }
    };

    static {

    }

    private int faceNumber;

    private void initializeOpenCVDependencies() {

        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            InputStream ims = getResources().openRawResource(R.raw.ny);
            //InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);

            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            //File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
           File imf =  new File(cascadeDir, "bess1.jpg");
            FileOutputStream imo = new FileOutputStream(imf);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            while ((bytesRead = ims.read(buffer)) != -1) {
                imo.write(buffer, 0, bytesRead);
            }
            ims.close();
            imo.close();

            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
           // String stream = getResources().getResourceName(R.raw);
            Mat image = imread(imf.getAbsolutePath());



        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }

        // And we are ready to go
        javaCameraView.enableView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_face);
       // getPermission();

        javaCameraView = findViewById(R.id.java_camera_view);
        javaCameraView.setCameraIndex(1);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recognize = extras.getBoolean("RECOGNIZE");
            Log.i(TAG, "onResume: " + recognize);
        }
        else {
            //getApplicationContext().getExternalFilesDir("imgfaces").delete();
            Log.i(TAG, "onResume: null bundle");
        }

       // Log.i(TAG, "onCreate: " + getApplicationContext().getExternalFilesDir(null).getAbsolutePath());
        PermissionRequest.cameraPermission(this, this);

    }

    @Override
    protected void onPause(){
        super.onPause();

        if(javaCameraView != null) javaCameraView.disableView();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(javaCameraView != null) javaCameraView.disableView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //getPermission();


        if (OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCv successfully loaded");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else {
            Log.d(TAG, "OpenCv not loaded");
            try {
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallBack);
            } catch (Exception e){
                Log.d(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permission = false;
        switch (requestCode){
            case PermissionRequest.REQUEST_CAMERA_PERMISSION:
                permission  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permission ) finish();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);

        // The faces will be a 20% of the height of the screen
        //absoluteFaceSize = (int) (height * 0.10);
        //absoluteFaceSize = 200;
        if (absoluteFaceSize == 0) {
            int h = grayscaleImage.rows();
            if (Math.round(width * 0.2) > 0) {
                absoluteFaceSize = (int) Math.round(width * 0.2);
            }
        }

        Log.i(TAG, "face size : " + absoluteFaceSize);


    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        grayscaleImage.release();
//        mRotated.release();

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

           // Log.i(TAG, "onCameraFrame: yeahh");


            mRgba = inputFrame.rgba();




            //grayscaleImage = inputFrame.gray();
            mRgba.convertTo(mRgba, -1, 1, 1);

            //return mRotated;
            //Core.flip(mRgba, mRgba, -1);

            //Core.flip(grayscaleImage, grayscaleImage, 1);

            Imgproc.cvtColor(mRgba, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
            //Imgproc.cvtColor(mRotated, grayscaleImage, Imgproc.COLOR_RGBA2RGB);


            MatOfRect faces = new MatOfRect();

            if (cascadeClassifier != null) {
                cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 0, 2,
                        new Size(600, 600), new Size(600, 600));
            }


            // If there are any faces found, draw a rectangle around it
            Rect[] facesArray = faces.toArray();
            if (facesArray.length != 0) {
                // finish();
                    faceNumber++;
                // Toast.makeText(this, "vue :" + facesArray.length , Toast.LENGTH_SHORT).show();

                for (int i = 0; i < facesArray.length; i++) {

                    mface = mRgba.submat(facesArray[i].y, facesArray[i].y + facesArray[i].height, facesArray[i].x, facesArray[i].x + facesArray[i].width);
                    Log.i(TAG, "mface : ");

                    //Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            if(recognize) {

                                String fileName = getApplicationContext().getExternalFilesDir(null).getAbsolutePath()+"/you.jpg";
                                imwrite(fileName, mface);
                                recognizeProcess();

                            } else {
                                //Toast.makeText(getApplicationContext(), "Your message to main thread", Toast.LENGTH_SHORT).show();
                                //Core.flip(mRgba, mRgba, 0);
                                //faceDetection(mRgba);
                                //faceNumber++;
                                Toast.makeText(getApplicationContext(), "onCameraFrame: face :" + faceNumber + " ", Toast.LENGTH_SHORT).show();
                                String fileName = getApplicationContext().getExternalFilesDir("imgfaces").getAbsolutePath();//Environment.getExternalStorageDirectory().getPath() +

                                SysFonctions.saveMatToImage(mface, fileName);
                                if (faceNumber == 3) {

                                    //trainProcess();
                                    startService(new Intent(getApplicationContext(), FaceTrainerService.class));
                                    finish();
                                }
                            }


                            //saveImage(mRgba);
                        }
                    });
                }


            }


        return mRgba;
    }
/*
    private void trainProcess() {
        Intent train = new Intent(this, MainActivity.class);
        //train.put
        Toast.makeText(this, "Reconnaissance en cours" , Toast.LENGTH_LONG).show();
        Log.i(TAG, "trainProcess: begin");
        onCameraViewStopped();


        EigenFaces eigenFaces = new EigenFaces();
        try {
            eigenFaces.execute(detectfaces);
            eigenFaces.saveModel(getApplicationContext().getExternalFilesDir(null).getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "trainProcess: end");

        //Toast.makeText(this, "Reconnaissance terminée" , Toast.LENGTH_LONG).show();

    }

*/

public void recognizeProcess() {
    String modelFile = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/model";
    try {
        EigenFaces testE = EigenFaces.loadModel(modelFile);
        double distance = testE.find(getApplicationContext().getExternalFilesDir(null).getAbsolutePath()+"/you.jpg");
        Log.i(TAG, "recognizeProcess: distance " + distance);
        if(distance <= 500 ) { finish();}
        //Toast.makeText(this, "distance : " + distance, Toast.LENGTH_LONG);

    } catch (IOException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
}

public void getCameraPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Log.d(SysFonctions.TAG, "buld");
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d(SysFonctions.TAG, "permission");
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 1);
        }
    }


}

}
