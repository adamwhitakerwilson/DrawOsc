package com.example.adamwhitakerwilson.dgtosc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by adamwhitakerwilson on 2017-04-04.
 */

//Custom View

public class DrawingView extends View {


    //network variables
    private OSCPortOut sender1 = null;
    private OSCPortOut sender2 = null;
    private OSCPortOut sender3 = null;
    private OSCPortOut sender4 = null;
    private OSCPortOut sender5 = null;
    private OSCPortOut sender6 = null;
    private OSCPortOut sender7 = null;
    private OSCPortOut sender8 = null;
    private OSCPortIn  receiver;
    private InetAddress targetIP;
    private String targetIPStr;
    private int portNumber;

    //points
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

    private final List<Float> xHold = new ArrayList<>();
    private final List<Float> yHold = new ArrayList<>();

    //time
    private final List<Long> timeHold = new ArrayList<>();
    private long timeStampStart;
    long timeDifferenceTotal;

    //maths
    float CNeg315 = (float) Math.cos(-315);
    float SNeg315 = (float) Math.sin(-315);
    float C180 = (float) Math.cos(180);
    float S180 = (float) Math.sin(180);
    float CNeg135 = (float) Math.cos(-135);
    float SNeg135 = (float) Math.sin(-135);
    float C315 = (float) Math.cos(315);
    float S315 = (float) Math.sin(315);

    //triggers
    private boolean to = false;
    private boolean clearOn = false;
    private boolean up = true;
    private int radioId = 0;

    int radio;
    float speed;

    //paints
    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private final Path mPath;
    private final Paint mBitmapPaint;
    private final Paint circlePaint;
    private final Path circlePath;

    //measurements
    public int width;
    public int height;

    int maxX = 500;
    int maxY = 250;

    //GUIs
    RadioButton forward;
    RadioButton backward;
    RadioButton backwardForward;
    RadioGroup radioGroup;



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

      //  portNumber = ((DrawActivity2) getContext()).getPort();
        targetIPStr = ((DrawActivity2) getContext()).getIp();
      //  Log.d("port: ", Integer.toString(portNumber));
//        Log.d("IP: ", targetIPStr);
        portNumber = 8800;
        setConnection();

        isInEditMode();




    }

    @Override
    public Object getTag() {
        return super.getTag();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        maxX = MeasureSpec.getSize(widthMeasureSpec);
        maxY = MeasureSpec.getSize(heightMeasureSpec);

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

        final float a, b;

        a = maxX / 2;

        b = maxY / 2;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mBitmap.eraseColor(Color.BLACK);
        mCanvas = new Canvas(mBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3F);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);

        mCanvas.drawLine(a, b - (b / 2), a, b + (b / 2), paint);
        mCanvas.drawLine((a / 2) + (a / 4), b, (maxX * 0.75f) - (a / 4), b, paint);
        mCanvas.drawCircle(a, b, 200F, paint);
        //  mCanvas.drawRect(maxX/6, maxY/6, maxX-(maxX/6), maxY-(maxY/6), paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(circlePath, circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    long timeDifferenceMove;

    private void touch_start(float x, float y) {

        x1 = x;
        y1 = y;
        up = false;

        //sleep so looper has time to know that up is false
        try {
            Thread.sleep(16);
            printDataFile();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        to = true;


        if (xHold.size() != 0) {
            xHold.clear();
            yHold.clear();
            timeHold.clear();
        }

        if (clearOn) {
            clearDrawing();
        }
        mPath.reset();
        timeStampStart = System.nanoTime() / 1000000;
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        try {
            Thread.sleep(16);
            printDataFile();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            x1 = x;
            y1 = y;
            circlePath.reset();
            circlePath.addCircle(mX, mY, 10, Path.Direction.CW);


            long timeStampMove = System.nanoTime() / 1000000;
            timeDifferenceMove = timeStampMove - timeStampStart;
            timeHold.add(timeDifferenceMove);
            xHold.add(x);
            yHold.add(y);

            // Log.d("x: ", Float.toString(timeDifferenceMove));

            // Start the thread that sends messages

            new Thread(new Runnable() {
                public void run() {

                    x1 = normalizeX(x1);
                    y1 = normalizeY(y1);

                    adamsMath();
                    sendMyOscMessage();
                }

            }).start();

        }

    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
        clearOn = true;
        up = true;

        long timeStampEnd = System.nanoTime() / 1000000;
        timeDifferenceTotal = timeStampEnd - timeStampStart;
        to = false;
        try {
            Thread.sleep(20);
            printDataFile();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                // clearDrawing();
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:


                //  Log.d("xRaw: ", Float.toString(xRaw));
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:


                touch_up();
                invalidate();


                break;
        }


        return true;

    }



    public void clearDrawing() {
        //clear canvas
        setDrawingCacheEnabled(false);

        onSizeChanged(width, height, width, height);
        setDrawingCacheEnabled(true);
    }

    //Looper

    public void printDataFile() {
        System.gc();

        radio = ((DrawActivity2) getContext()).getSender();
        speed = ((DrawActivity2) getContext()).getSpeed();
       // Log.d("speed", Float.toString(speed));
       // Log.d("sender", Integer.toString(radio));

        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (up) {

            new Thread(new Runnable() {

                int i = 0;
                int i2 = 0;
                long now2 = 0;


                @Override
                public void run() {
                    int pathSize = xHold.size();
                    i2 = pathSize - 1;
                    //    Log.d("size: ", Long.toString(pathSize));
/*
                    int delay = 16;   // delay for 5 sec.
                    final int interval = 16;  // iterate every sec.
                    Timer timer = new Timer();



                    timer.scheduleAtFixedRate(new TimerTask() {
                        int i = 0;
                        public void run() {
                            if(i == xHold.size()) {
                                i = 0;
                            }
                            if (now2 - now1 == timeHold.get(i)) {
                                x1 = xHold.get(i);
                                y1 = yHold.get(i);

                                x1 = normalizeX(x1);
                                y1 = normalizeY(y1);

                                adamsMath();
                                sendMyOscMessage();
                                Log.d("osc: ", "sent");
                                i++;
                            }

                            Log.d("xLoop: ", Float.toString(xHold.get(i)));
                            Log.d("yLoop: ", Float.toString(yHold.get(i)));
                        //    Log.d("timeLoop: ", Integer.toString(timeHold.get(i)));
                            i++;
                        }
                    }, delay, interval);

*/

//backward forward
                    if (radio == 0) {

                        long now1 = System.nanoTime() / 1000000;

                        while (i2 > -1) {

                            if (!up) {
                                i2 = pathSize - 1;
                                break;
                            }
                            float tmh;

                            if (i2 == 0) {
                                tmh = timeDifferenceTotal;
                            } else {
                                tmh = timeDifferenceTotal - timeHold.get(i2 - 1);
                            }

                            now2 = System.nanoTime() / 1000000;
                            long nd = now2 - now1;

                            if (nd == tmh) {
                                x1 = xHold.get(i2);
                                y1 = yHold.get(i2);

                                x1 = normalizeX(x1);
                                y1 = normalizeY(y1);

                                adamsMath();
                                sendMyOscMessage();

                                //  Log.d("osc: ", "sent");
                                i2--;
                            }
                        }
                        i = 0;
                        now1 = System.nanoTime() / 1000000;

                        while (i < pathSize) {
                            if (!up) {
                                i = 0;
                                break;
                            }
                            now2 = System.nanoTime() / 1000000;
                            long nd = now2 - now1;
                            float tmh = timeHold.get(i);

                            if (nd == tmh) {
                                x1 = xHold.get(i);
                                y1 = yHold.get(i);

                                x1 = normalizeX(x1);
                                y1 = normalizeY(y1);

                                adamsMath();
                                sendMyOscMessage();
                                //  Log.d("osc: ", "sent");
                                i++;
                            }
                        }
                        i2 = pathSize - 1;
                        if (up) {
                            try {

                                printDataFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

//backward loop

                    if (radio == 2) {

                        long now1 = System.nanoTime() / 1000000;
                        while (i2 > -1) {

                            if (!up) {
                                i2 = pathSize - 1;
                                break;
                            }

                            float tmh;

                            if (i2 == 0) {
                                tmh = timeDifferenceTotal;
                            } else {
                                tmh = timeDifferenceTotal - timeHold.get(i2 - 1);
                            }

                            now2 = System.nanoTime() / 1000000;
                            long nd = now2 - now1;

                            if (nd == tmh) {

                                x1 = xHold.get(i2);
                                y1 = yHold.get(i2);

                                x1 = normalizeX(x1);
                                y1 = normalizeY(y1);

                                adamsMath();
                                sendMyOscMessage();
                                //  Log.d("osc: ", "sent");
                                i2--;
                            }
                        }
                        i2 = pathSize - 1;
                        if (up) {
                            try {
                                Thread.sleep(16);
                                printDataFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

//forward loop

                    if (radio == 1) {


                        long now1 = System.nanoTime() / 1000000;
                        while (i < pathSize) {
                            if (!up) {
                                i = 0;

                                break;
                            }

                            now2 = System.nanoTime() / 1000000;
                            long nd = now2 - now1;
                            float tmh = timeHold.get(i);

                            if (nd == tmh) {
                                x1 = xHold.get(i);
                                y1 = yHold.get(i);

                                x1 = normalizeX(x1);
                                y1 = normalizeY(y1);

                                adamsMath();
                                sendMyOscMessage();
                                //  Log.d("osc: ", "sent");
                                i++;
                            }
                        }
                        i = 0;
                        if (up) {
                            try {
                                Thread.sleep(16);
                                printDataFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }).start();
        }
    }

    //set OSC UDP Port Connection

    private void setConnection() {
        try {
            targetIP = InetAddress.getByName("192.168.0.104");
            //targetIP = InetAddress.getLocalHost();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            //toast(getString(R.string.notConnecting));
        }

        try {
            sender1 = new OSCPortOut(targetIP, portNumber);
            sender2 = new OSCPortOut(targetIP, portNumber + 1);
            sender3 = new OSCPortOut(targetIP, portNumber + 2);
            sender4 = new OSCPortOut(targetIP, portNumber + 3);
            sender5 = new OSCPortOut(targetIP, portNumber + 4);
            sender6 = new OSCPortOut(targetIP, portNumber + 5);
            sender7 = new OSCPortOut(targetIP, portNumber + 6);
            sender8 = new OSCPortOut(targetIP, portNumber + 7);//------set up outgoing ------

        } catch (SocketException e) {
            e.printStackTrace();
            // toast(getString(R.string.notConnecting));
        }


    }


    //send OSC messages

    private void sendMyOscMessage() {

        try {


            OSCMessage msgX1 = new OSCMessage();
            msgX1.setAddress("/X_1");
            msgX1.addArgument(x1);

            // Log.d("x1/maxX: ", Float.toString(x1));

            OSCMessage msgY1 = new OSCMessage();
            msgY1.setAddress("/Y_1");
            msgY1.addArgument(y1);



  /*      OSCMessage msgX2 = new OSCMessage();
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
*/


            sender1.send(msgX1);
            sender1.send(msgY1);
            ///  sender1 = null;

/*
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
*/

        } catch (Exception e) {

        }

    }

    //touch scale normalizers from {[min(x,y)] to [max(x,y)]} to {[-1.0 to 1.0]}

    private float normalizeX(float n) {

        n = n / (maxX >> 1) - 1;

        return n;
    }

    private float normalizeY(float n) {

        n = (n / (maxY >> 1) - 1) * (-1);

        return n;
    }

    //Rotates {(x2,y2), (x3,y3)...(x8,y8)} by 45 degrees

    private void adamsMath() {

        x2 = (x1 * (CNeg315)) - (y1 * (SNeg315));
        y2 = (x1 * (SNeg315) + (y1 * (CNeg315)));

        x3 = y1;
        y3 = x1 * (-1.0F);

        x4 = (x1 * (C180)) - (y1 * (S180));
        y4 = (x1 * (S180) + (y1 * (C180)));

        x5 = (x1 * (CNeg135)) - (y1 * (SNeg135));
        y5 = (x1 * (SNeg135) + (y1 * (CNeg135)));

        x6 = x2 * (-1.0F);
        y6 = y2 * (-1.0F);

        x7 = y1 * (-1.0F);
        y7 = x1;

        x8 = (x1 * (C315)) - (y1 * (S315));
        y8 = (x1 * (S315) + (y1 * (C315)));

    }


}
