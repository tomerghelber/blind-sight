package org.opencv.samples.blindsight.detection;

import android.os.AsyncTask;
import android.util.Log;

import org.opencv.core.Mat;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Shira-PC on 14/11/2014.
 */
public class NetworkThread extends AsyncTask<Mat, Integer, Void> {
    public Mat m;

    public NetworkThread(Mat m) {
        this.m = m;
    }
    @Override
    protected Void doInBackground(Mat... p) {
        try  {
            Socket socket = new Socket("172.20.20.193", 1337);
            socket.getOutputStream().write(this.m.dump().getBytes());
            socket.getOutputStream().flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected  void onProgressUpdate(Integer... values) {

    }

}
