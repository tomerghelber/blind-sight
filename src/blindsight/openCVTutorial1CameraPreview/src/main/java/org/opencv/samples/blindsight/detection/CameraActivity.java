package org.opencv.samples.blindsight.detection;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Scalar;
import org.opencv.samples.blindsight.R;
import org.opencv.samples.blindsight.handler.StateHandler;
import org.opencv.samples.blindsight.handler.VocalHandler;
import org.opencv.samples.blindsight.navigation.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    private static final Scalar RED = new Scalar(255.0, 0.0, 0.0);
    private static final Scalar GREEN = new Scalar(0.0, 255.0, 0.0);
    private static final Scalar GRAY = new Scalar(100.0, 100.0, 100.0);

    public TextView data;

    private StateHandler handler;

    public Socket socket;
    public OutputStreamWriter out;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }

    };

    public CameraActivity() {
        Log.i(TAG, "Instantiated new CameraActivity");
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.tutorial1_surface_view);

        if (mIsJavaCamera)
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        else
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOpenCvCameraView != null)
                    mOpenCvCameraView.disableView();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        latest = new ArrayList<TrafficLight>();

        data = (TextView) findViewById(R.id.data);

        handler = new VocalHandler(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemSwitchCamera = menu.add("Toggle Native/Java camera");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String toastMesage = new String();
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        if (item == mItemSwitchCamera) {
            mOpenCvCameraView.setVisibility(SurfaceView.GONE);
            mIsJavaCamera = !mIsJavaCamera;

            if (mIsJavaCamera) {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
                toastMesage = "Java Camera";
            } else {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
                toastMesage = "Native Camera";
            }

            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
            mOpenCvCameraView.enableView();
            Toast toast = Toast.makeText(this, toastMesage, Toast.LENGTH_LONG);
            toast.show();
        }

        return true;
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        final Mat img = inputFrame.rgba();
        new NetworkThread(img).execute();
        TrafficLightsDetector det = new TrafficLightsDetector();
        return img;
//            List<TrafficLight> trafs = det.getAllTrafficLights(img, 30, 90);
//            for (TrafficLight traf : trafs) {
//                Rect rect = traf.getRect();
//                Core.rectangle(img ,rect.br(), rect.tl(), getColor(traf.detectLightState()), 2);
//            }

//            TrafficLight light = det.getState(img);
//            latest.add(light);
//            if (latest.size() > 3) {
//                latest.remove(0);
//            }
//            TrafficLight.State state  = getDominantState();
//            if (state == TrafficLight.State.GREEN) {
//                handler.handlegreen();
//            } else if (state == TrafficLight.State.RED) {
//                handler.handleRed();
//            }
//            return img;
//
//        } catch (Exception e) {
//            Log.e(TAG, "exception", e);
//        }
//        return img;
    }

    private Scalar getColor(TrafficLight.State state) {
        if (state == TrafficLight.State.GREEN) {
            return GREEN;
        } else if (state == TrafficLight.State.RED) {
            return RED;
        }
        return GRAY;
    }

    private TrafficLight.State getDominantState() {
        int red = 0, green = 0;
        for (TrafficLight light : latest) {
            if (light.detectLightState() == TrafficLight.State.GREEN) {
                green += 1;
            } else if (light.detectLightState() == TrafficLight.State.RED) {
                red += 1;
            }
        }

        if (red < latest.size() / 2 && green < latest.size() / 2) {
            return TrafficLight.State.NA;
        } else {
            return green < red ? TrafficLight.State.RED : TrafficLight.State.GREEN;
        }
    }

    private void sendToServer(Mat img) {

    }

    List<TrafficLight> latest;

}
