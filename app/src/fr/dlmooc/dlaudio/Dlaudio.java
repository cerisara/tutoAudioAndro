package fr.dlmooc.dlaudio;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Context;
import java.lang.Exception;
import android.widget.Toast;
import java.net.URI;
import java.io.File;
import java.util.Random;
import java.io.DataInputStream;
import java.util.ArrayList;

import com.softmoore.android.graphlib.Function;
import com.softmoore.android.graphlib.Graph;
import com.softmoore.android.graphlib.GraphView;
import com.softmoore.android.graphlib.Label;
import com.softmoore.android.graphlib.Point;

public class Dlaudio extends Activity {

    public static final int dimX = 160;
    public static final int dimZ = 10;
    public static final float LR = 0.1f;
    public static final int RECSEC = 4;
    public static final int NEPOCHS = 1000;

    public static Dlaudio main = null;
    public File fdir = null;
    private Mike mike = null;

    private float[][] W = new float[dimZ][dimX];
    private float[][] V = new float[dimX][dimZ];
    private float[][] gradW = new float[dimZ][dimX];
    private float[][] gradV = new float[dimX][dimZ];
    private float[] Z = new float[dimZ];
    private float[] Y = new float[dimX];
    private Random rand = new Random();

    private Graph graph;
    private ArrayList<Point> pts = new ArrayList<Point>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main=this;
        fdir = getExternalFilesDir(null);
        mike = new Mike();
        initModel();

        setContentView(R.layout.main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void msg(final String s) {
        main.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(main, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void start(View v) {
        pts.clear();
        final Button but = (Button) findViewById(R.id.but1);
        but.setEnabled(false);
        Thread waiter = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECSEC*1000);
                    mike.stopRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        waiter.start();
        mike.resetAudioSource();
        mike.startRecord();
    }

    public void mikeEnded() {
        msg("training...");
        trainModel();
        main.runOnUiThread(new Runnable() {
            public void run() {
                final Button but = (Button)main.findViewById(R.id.but1);
                but.setEnabled(true);
            }
        });
    }

    private void initModel() {
        for (int i=0;i<dimZ;i++) {
            for (int j=0;j<dimX;j++) {
                W[i][j]=rand.nextFloat()*0.2f-0.1f;
                V[j][i]=rand.nextFloat()*0.2f-0.1f;
            }
        }
    }

    private float MSE(float[] X, float[] Y) {
        float err = 0f;
        for (int i=0;i<dimX;i++) err += (X[i]-Y[i])*(X[i]-Y[i]);
        return err;
    }

    private float[] encodeur(float[] X) {
        for (int i=0;i<dimZ;i++) {
            Z[i]=0f;
            for (int j=0;j<dimX;j++) {
                Z[i] += W[i][j]*X[j];
            }
            if (Z[i]<0) Z[i]=0f;
        }
        return Z;
    }
    private float[] decodeur(float[] Z) {
        for (int i=0;i<dimX;i++) {
            Y[i]=0f;
            for (int j=0;j<dimZ;j++) {
                Y[i] += V[i][j]*Z[j];
            }
        }
        return Y;
    }
    private float[][] calcGradV(float[] X, float[] Z, float[] Y) {
        // TODO
        return gradV;
    }
    private float[][] calcGradW(float[] X, float[] Z, float[] Y) {
        // TODO
        return gradW;
    }

    private void trainModel() {
        try {
            DataInputStream son = new DataInputStream(mike);
            final int nsamps = (int)(16000f*(float)(RECSEC-1) / (float)dimX);
            float[][] X = new float[nsamps][dimX];
            for (int samp=0; samp<X.length;samp++) {
                for (int i=0;i<dimX;i++) {
                    X[samp][i] = (float)son.readShort()/32000f;
                }
            }
            for (int epoch=0; epoch<NEPOCHS; epoch++) {
                float toterr = 0f;
                // for (int samp=0; samp<X.length;samp++) 
                for (int samp=0; samp<1;samp++) {
                    float[] Z = encodeur(X[samp]);
                    float[] Y = decodeur(Z);
                    float err = MSE(X[samp],Y);
                    toterr += err;
                    float[][] gradW = calcGradW(X[samp],Z,Y);
                    float[][] gradV = calcGradV(X[samp],Z,Y);
                    for (int i=0;i<dimX;i++) {
                        for (int j=0;j<dimZ;j++) {
                            W[j][i] -= LR * gradW[j][i];
                            V[i][j] -= LR * gradV[i][j];
                        }
                    }
                }
                toterr /= (float)X.length;
                pts.add(new Point(epoch,toterr));

                final float vloss = toterr;
                final int ep = epoch;
                main.runOnUiThread(new Runnable() {
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.textview);
                        textView.setText("loss: "+ep+" "+vloss);
                    }
                });
            }

            final Point[] pts2 = new Point[pts.size()];
            pts.toArray(pts2);
            main.runOnUiThread(new Runnable() {
                public void run() {
                    float vmin = (float)pts2[0].getY(), vmax = vmin;
                    for (int i=1;i<pts2.length;i++) {
                        if (vmin>pts2[i].getY()) vmin=(float)pts2[i].getY();
                        if (vmax<pts2[i].getY()) vmax=(float)pts2[i].getY();
                    }
                    graph = new Graph.Builder()
                        .setWorldCoordinates(0, NEPOCHS, vmin, vmax)
                        .addLineGraph(pts2)
                        .build();
                    GraphView graphView = (GraphView)findViewById(R.id.graph_view);
                    graphView.setGraph(graph);
                }
            });

        } catch (Exception e) {
            msg("ERROR reading WAV "+e);
            e.printStackTrace();
        }
    }
}

