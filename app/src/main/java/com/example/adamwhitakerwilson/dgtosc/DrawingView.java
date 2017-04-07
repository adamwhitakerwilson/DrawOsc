package com.example.adamwhitakerwilson.dgtosc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamwhitakerwilson on 2017-04-04.
 */



//Custom View

public class DrawingView extends View {
    private OSCPortOut sender1 = null;
    private OSCPortOut sender2 = null;
    private OSCPortOut sender3 = null;
    private OSCPortOut sender4 = null;
    private OSCPortOut sender5 = null;
    private OSCPortOut sender6 = null;
    private OSCPortOut sender7 = null;
    private OSCPortOut sender8 = null;
    //private OSCPortIn  receiver;
    private InetAddress targetIP;
    private String targetIPStr;
    private int portNumber;

    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float x3;
    private float y3;
    private float x4;
    private float y4;
    private float x5;
    private float y5;
    private float x6;
    private float y6;
    private float x7;
    private float y7;
    private float x8;
    private float y8;

    float CNeg315 = (float)Math.cos(-315);
    float SNeg315 = (float)Math.sin(-315);
    float C180 = (float)Math.cos(180);
    float S180 = (float)Math.sin(180);
    float CNeg135 = (float)Math.cos(-135);
    float SNeg135 = (float)Math.sin(-135);
    float C315 = (float)Math.cos(315);
    float S315 = (float)Math.sin(315);

    private boolean to = false;

    private final List<Float> xHold = new ArrayList<>();
    private final List<Float> yHold = new ArrayList<>();
    private final List<Long> timeHold = new ArrayList<>();

    private long timeStampStart;

    private Paint mPaint;
    private boolean clearOn = false;

    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private final Path mPath;
    private final Paint mBitmapPaint;
    private final Paint circlePaint;
    private final Path circlePath;

    int maxX = 500;
    int maxY = 250;


    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(2f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2);

        portNumber = 8800;
        setConnection();

        isInEditMode();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        maxY= MeasureSpec.getSize(heightMeasureSpec);
        maxX= MeasureSpec.getSize(widthMeasureSpec);

    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        //w = (int) (((double)wM)-((0.1) * ((double)wM)));
        // h = (int)(((double)hM) - ((0.29) * ((double)hM)));

        width = w;      // don't forget these
        height = h;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mBitmap.eraseColor(Color.BLACK);
        mCanvas = new Canvas(mBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3F);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);

        mCanvas.drawLine(250F, 0F, 250F, 500F, paint);
        mCanvas.drawLine(0F, 250F, 500F, 250F, paint);
        mCanvas.drawCircle(250F, 250F, 200F, paint);
        // mCanvas.drawRect(150F, 150F, 250F, 500F, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath,  mPaint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    private void touch_start(float x, float y) {

        if(clearOn){
            clearDrawing();
        }
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();
        clearOn = true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        x1 = event.getX();
        y1 = event.getY();
        float xRaw = event.getX();
        float yRaw = event.getY();

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                to = true;
                timeStampStart = System.nanoTime()/1000000;

                if(xHold.size() != 0){
                    xHold.clear();
                    yHold.clear();
                    timeHold.clear();
                }

                clearDrawing();
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                long timeStampMove = System.nanoTime() / 1000000;
                long timeDifferenceMove = timeStampMove - timeStampStart;
                xHold.add(xRaw);
                yHold.add(yRaw);
                timeHold.add(timeDifferenceMove);

                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:


                long timeStampEnd = System.nanoTime() / 1000000;
                @SuppressWarnings("UnusedAssignment") long timeDifferenceTotal = timeStampEnd - timeStampStart;
                to = false;

                touch_up();
                invalidate();

                try {
                    Thread.sleep(200);
                    printDataFile();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
        }

        // Start the thread that sends messages

        new Thread(new Runnable() {
            public void run() {

                x1 = normalizeX(x1);
                y1 = normalizeY(y1);

                adamsMath();
                sendMyOscMessage();
            }

        }).start();


        return true;
    }

    public void clearDrawing() {
        //clear canvas
        setDrawingCacheEnabled(false);
        // don't forget that one and the match below,
        // or you just keep getting a duplicate when you save.

        onSizeChanged(width, height, width, height);

        setDrawingCacheEnabled(true);
    }

    //Looper

    public void printDataFile() {

        new Thread(new Runnable() {

            int i = 0;
            // int i = xHold.size();
            // i--;
            final long now1 = System.nanoTime()/1000000;
            long now2 = 0;

            @Override
            public void run() {
                int pathSize = xHold.size();

                while(i < pathSize) {

                    //                       touch_start(xHold.get(i), yHold.get(i));
//                        invalidate();

                    //  while(i >= 0){
                    now2 = System.nanoTime()/1000000;

                    if(pathSize == 0){
                        // to = true;
                        //break;
                    }

                    else if (now2 - now1 == timeHold.get(i)) {
                        x1 = xHold.get(i);
                        y1 = yHold.get(i);
//                            touch_move(xHold.get(i), yHold.get(i));
//                            invalidate();

                        x1 = normalizeX(x1);
                        y1 = normalizeY(y1);

                        adamsMath();
                        sendMyOscMessage();

                        //   Log.d("size: ", Integer.toString(xHold.size()));
                        //   Log.d("xHold: ", Float.toString(xHold.get(i)));
                        //   Log.d("TimeHold: ", Float.toString(timeHold.get(i)));
                        //   Log.d("now: ", Long.toString(now2-now1));
                        i++;

                        // i--;
                        // float sum = getDistance(xRaw, yRaw, ev);
                        //  Log.d("distance", Float.toString(sum));

                    }

                }
                if(!to){
                    try {
                        printDataFile();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //set OSC UDP Port Connection

    private void setConnection(){
        try {
            targetIP = InetAddress.getByName("192.168.0.100");
            //targetIP = InetAddress.getLocalHost();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            //toast(getString(R.string.notConnecting));
        }

        try {
            sender1 = new OSCPortOut(targetIP, portNumber);
            sender2 = new OSCPortOut(targetIP, portNumber+1);
            sender3 = new OSCPortOut(targetIP, portNumber+2);
            sender4 = new OSCPortOut(targetIP, portNumber+3);
            sender5 = new OSCPortOut(targetIP, portNumber+4);
            sender6 = new OSCPortOut(targetIP, portNumber+5);
            sender7 = new OSCPortOut(targetIP, portNumber+6);
            sender8 = new OSCPortOut(targetIP, portNumber+7);//------set up outgoing ------
        } catch (SocketException e) {
            e.printStackTrace();
            // toast(getString(R.string.notConnecting));
        }

    }


    //send OSC messages

    private void sendMyOscMessage(){



        OSCMessage msgX1 = new OSCMessage();
        msgX1.setAddress("/X_1");
        msgX1.addArgument(x1);

        // Log.d("x1/maxX: ", Float.toString(x1));

        OSCMessage msgY1 = new OSCMessage();
        msgY1.setAddress("/Y_1");
        msgY1.addArgument(y1);

        OSCMessage msgX2 = new OSCMessage();
        msgX2.setAddress("/X_2");
        msgX2.addArgument(x2);

        // Log.d("x1/maxX: ", Float.toString(x1));

        OSCMessage msgY2 = new OSCMessage();
        msgY2.setAddress("/Y_2");
        msgY2.addArgument(y2);

        OSCMessage msgX3 = new OSCMessage();
        msgX3.setAddress("/X_3");
        msgX3.addArgument(x3);

        // Log.d("x1/maxX: ", Float.toString(x1));

        OSCMessage msgY3 = new OSCMessage();
        msgY3.setAddress("/Y_3");
        msgY3.addArgument(y3);

        OSCMessage msgX4 = new OSCMessage();
        msgX4.setAddress("/X_4");
        msgX4.addArgument(x4);

        // Log.d("x1/maxX: ", Float.toString(x1));

        OSCMessage msgY4 = new OSCMessage();
        msgY4.setAddress("/Y_4");
        msgY4.addArgument(y4);


        OSCMessage msgX5 = new OSCMessage();
        msgX5.setAddress("/X_5");
        msgX5.addArgument(x5);

        // Log.d("x1/maxX: ", Float.toString(x1));

        OSCMessage msgY5 = new OSCMessage();
        msgY5.setAddress("/Y_5");
        msgY5.addArgument(y5);

        OSCMessage msgX6 = new OSCMessage();
        msgX6.setAddress("/X_6");
        msgX6.addArgument(x6);

        // Log.d("x1/maxX: ", Float.toString(x1));

        OSCMessage msgY6 = new OSCMessage();
        msgY6.setAddress("/Y_6");
        msgY6.addArgument(y6);

        OSCMessage msgX7 = new OSCMessage();
        msgX7.setAddress("/X_7");
        msgX7.addArgument(x7);

        // Log.d("x1/maxX: ", Float.toString(x1));

        OSCMessage msgY7 = new OSCMessage();
        msgY7.setAddress("/Y_7");
        msgY7.addArgument(y7);

        OSCMessage msgX8 = new OSCMessage();
        msgX8.setAddress("/X_8");
        msgX8.addArgument(x8);

        // Log.d("x1/maxX: ", Float.toString(x1));

        OSCMessage msgY8 = new OSCMessage();
        msgY8.setAddress("/Y_8");
        msgY8.addArgument(y8);


        try {

            sender1.send(msgX1);
            sender1.send(msgY1);

            sender2.send(msgX2);
            sender2.send(msgY2);

            sender3.send(msgX3);
            sender3.send(msgY3);

            sender4.send(msgX4);
            sender4.send(msgY4);

            sender5.send(msgX5);
            sender5.send(msgY5);

            sender6.send(msgX6);
            sender6.send(msgY6);

            sender7.send(msgX7);
            sender7.send(msgY7);

            sender8.send(msgX8);
            sender8.send(msgY8);

        } catch (Exception ignored) {

        }

    }

    //touch scale normalizers from {[min(x,y)] to [max(x,y)]} to {[-1.0 to 1.0]}

    private float normalizeX(float n) {

        n = n/(maxX>>1)-1;

        return n;
    }
    private float normalizeY(float n){

        n = (n/(maxY>>1)-1)*(-1);

        return n;
    }

    //Rotates {(x2,y2), (x3,y3)...(x8,y8)} by 45 degrees

    private void adamsMath(){

        x2= (x1*(CNeg315))-(y1*(SNeg315));
        y2= (x1*(SNeg315)+(y1*(CNeg315)));

        x3= y1;
        y3= x1*(-1);

        x4= (x1*(C180))-(y1*(S180));
        y4= (x1*(S180)+(y1*(C180)));

        x5 = (x1*(CNeg135))-(y1*(SNeg135));
        y5 = (x1*(SNeg135)+(y1*(CNeg135)));

        x6 = x2*(-1);
        y6 = y2*(-1);

        x7= y1*(-1);
        y7= x1;

        x8= (x1*(C315))-(y1*(S315));
        y8= (x1*(S315)+(y1*(C315)));

    }



}
