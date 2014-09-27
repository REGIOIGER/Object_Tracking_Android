package org.opencv.samples.tutorial2;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Tutorial2Activity extends Activity implements CvCameraViewListener2 {
    private static final String    TAG = "OCVSample::Activity";

    int H_MIN = 10;
    int H_MAX = 94;
    int S_MIN = 125;
    int S_MAX = 239;
    int V_MIN = 113;
    int V_MAX = 245;
    
    private static final int       VIEW_MODE_RGBA     = 0;
    private static final int       VIEW_MODE_GRAY     = 1;
    private static final int       VIEW_MODE_CANNY    = 2;
    
    private static final int       VIEW_MODE_THRESH = 3;
    
    private static final int       VIEW_MODE_FEATURES = 5;
    
    private static final int       VIEW_MODE_HPLUS = 6;
    private static final int       VIEW_MODE_HMINUS = 7;
    private static final int       VIEW_MODE_SPLUS = 8;
    private static final int       VIEW_MODE_SMINUS = 9;
    private static final int       VIEW_MODE_VPLUS = 10;
    private static final int       VIEW_MODE_VMINUS = 11;
    
    
    
    

    private int                    mViewMode;
    private Mat                    mRgba;
    private Mat                    mIntermediateMat;
    private Mat                    mGray;

    private MenuItem               mItemPreviewThresh;
    
    private MenuItem               mItemPreviewRGBA;
    private MenuItem               mItemPreviewGray;
    private MenuItem               mItemPreviewCanny;
    private MenuItem               mItemPreviewFeatures;
    
    
    private MenuItem               mItemPreviewHplus;
    private MenuItem               mItemPreviewHminus;
    private MenuItem               mItemPreviewSplus;
    private MenuItem               mItemPreviewSminus;
    private MenuItem               mItemPreviewVplus;
    private MenuItem               mItemPreviewVminus;
    

    private CameraBridgeViewBase   mOpenCvCameraView;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization
                    //System.loadLibrary("mixed_sample");
                    System.loadLibrary("object_tracking");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public Tutorial2Activity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.tutorial2_surface_view);
        
        

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial2_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemPreviewRGBA = menu.add("Preview RGBA");
        mItemPreviewGray = menu.add("Preview GRAY");
        mItemPreviewCanny = menu.add("Canny");
        mItemPreviewFeatures = menu.add("Find features");
        mItemPreviewThresh = menu.add("Thresh");
        
        mItemPreviewHplus = menu.add("H+");
        mItemPreviewHminus = menu.add("H-");
        mItemPreviewSplus = menu.add("S+");
        mItemPreviewSminus = menu.add("S-");
        mItemPreviewVplus = menu.add("V+");
        mItemPreviewVminus = menu.add("V-");

        return true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mIntermediateMat = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
    }

    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
        mIntermediateMat.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        final int viewMode = mViewMode;
        switch (viewMode) {
        case VIEW_MODE_GRAY:
            // input frame has gray scale format
            Imgproc.cvtColor(inputFrame.gray(), mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
            break;
        case VIEW_MODE_RGBA:
            // input frame has RBGA format
            mRgba = inputFrame.rgba();
            break;
        case VIEW_MODE_CANNY:
            // input frame has gray scale format
            mRgba = inputFrame.rgba();
            Imgproc.Canny(inputFrame.gray(), mIntermediateMat, 80, 100);
            Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
            break;
        case VIEW_MODE_FEATURES:
            // input frame has RGBA format
            mRgba = inputFrame.rgba();
            mGray = inputFrame.gray();
            ObjectTracking(mGray.getNativeObjAddr(), mRgba.getNativeObjAddr(), H_MAX, H_MIN);
            break;
            
        case VIEW_MODE_THRESH:
        	mRgba = inputFrame.rgba();
        	int maxValue = 255;
        	int blockSize = 61;
        	int meanOffset = 15;
        	Imgproc.adaptiveThreshold(inputFrame.gray(), mIntermediateMat, maxValue, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, blockSize, meanOffset);
        	Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
        	break;
        	
        case VIEW_MODE_HPLUS:
        	H_MAX+=10;
        	mViewMode = VIEW_MODE_FEATURES;
        	 mRgba = inputFrame.rgba();
             mGray = inputFrame.gray();
             //ObjectTracking(mGray.getNativeObjAddr(), mRgba.getNativeObjAddr(), H_MAX, H_MIN);
        	break;        	
        case VIEW_MODE_HMINUS:
        	H_MAX-=10;
        	mViewMode = VIEW_MODE_FEATURES;
        	 mRgba = inputFrame.rgba();
             mGray = inputFrame.gray();
             //ObjectTracking(mGray.getNativeObjAddr(), mRgba.getNativeObjAddr(), H_MAX, H_MIN);
        	break;
        case VIEW_MODE_SPLUS:
        	H_MIN+=10;
        	mViewMode = VIEW_MODE_FEATURES;
        	 mRgba = inputFrame.rgba();
             mGray = inputFrame.gray();
             //ObjectTracking(mGray.getNativeObjAddr(), mRgba.getNativeObjAddr(), H_MAX, H_MIN);
        	break;
        case VIEW_MODE_SMINUS:
        	H_MIN-=10;
        	mViewMode = VIEW_MODE_FEATURES;
        	 mRgba = inputFrame.rgba();
             mGray = inputFrame.gray();
             //ObjectTracking(mGray.getNativeObjAddr(), mRgba.getNativeObjAddr(), H_MAX, H_MIN);
        	break;
        case VIEW_MODE_VPLUS:
        	V_MAX++;
        	mViewMode = VIEW_MODE_FEATURES;
        	 mRgba = inputFrame.rgba();
             mGray = inputFrame.gray();
             //ObjectTracking(mGray.getNativeObjAddr(), mRgba.getNativeObjAddr(), H_MAX, H_MIN);
        	break;
        case VIEW_MODE_VMINUS:
        	V_MIN--;
        	mViewMode = VIEW_MODE_FEATURES;
        	 mRgba = inputFrame.rgba();
             mGray = inputFrame.gray();
             //ObjectTracking(mGray.getNativeObjAddr(), mRgba.getNativeObjAddr(), H_MAX, H_MIN);
        	break;
        	
        	
        }
        return mRgba;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        if (item == mItemPreviewRGBA) {
            mViewMode = VIEW_MODE_RGBA;
        } else if (item == mItemPreviewGray) {
            mViewMode = VIEW_MODE_GRAY;
        } else if (item == mItemPreviewCanny) {
            mViewMode = VIEW_MODE_CANNY;
        } else if (item == mItemPreviewFeatures) {
            mViewMode = VIEW_MODE_FEATURES;
        } else if (item == mItemPreviewThresh) {
        	mViewMode = VIEW_MODE_THRESH;
        } else if (item == mItemPreviewHplus) {
        	mViewMode = VIEW_MODE_HPLUS;
        } else if (item == mItemPreviewHminus) {
        	mViewMode = VIEW_MODE_HMINUS;
        } else if (item == mItemPreviewSplus) {
        	mViewMode = VIEW_MODE_SPLUS;
        } else if (item == mItemPreviewSminus) {
        	mViewMode = VIEW_MODE_SMINUS;
        } else if (item == mItemPreviewVplus) {
        	mViewMode = VIEW_MODE_VPLUS;
        } else if (item == mItemPreviewVminus) {
        	mViewMode = VIEW_MODE_VMINUS;
        } 

        return true;
    }

    //public native void FindFeatures(long matAddrGr, long matAddrRgba);
    public native void ObjectTracking(long matAddrGr, long matAddrRgba, int H_max, int H_min);
}
