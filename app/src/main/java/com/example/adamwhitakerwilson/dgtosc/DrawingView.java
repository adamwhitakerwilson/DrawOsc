package com.example.adamwhitakerwilson.dgtosc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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


    private static final float TOUCH_TOLERANCE = 4;
    private final List<Float> xHold = new ArrayList<>();
    private final List<Float> yHold = new ArrayList<>();
    //time
    private final List<Long> timeHold = new ArrayList<>();
    private final Path mPath;
    private final Paint mBitmapPaint;
    private final Paint circlePaint;
    private final Path circlePath;
    //maths
    private final float CNeg315 = (float) Math.cos(-315);
    private final float SNeg315 = (float) Math.sin(-315);
    private final float C180 = (float) Math.cos(180);
    private final float S180 = (float) Math.sin(180);
    private final float CNeg135 = (float) Math.cos(-135);
    private final float SNeg135 = (float) Math.sin(-135);
    private final float C315 = (float) Math.cos(315);
    private final float S315 = (float) Math.sin(315);
    //paints
    private final Paint mPaint;
    //measurements
    private int width;
    private int height;
    private long timeDifferenceTotal;
    private int radio;
    private int radioTrack = 1;
    private int maxX = 500;
    private int maxY = 250;
    //network variables
    private OSCPortOut sender1 = null;
    private OSCPortOut sender2 = null;
    private OSCPortOut sender3 = null;
    private OSCPortOut sender4 = null;
    private OSCPortOut sender5 = null;
    private OSCPortOut sender6 = null;
    private OSCPortOut sender7 = null;
    private OSCPortOut sender8 = null;
    private InetAddress targetIP;
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
    private long timeStampStart;
    //triggers
    private boolean to = false;
    private boolean clearOn = false;
    private boolean up = true;
    private long timeDifferenceMove;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private float mX, mY;

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

        setConnection();

        isInEditMode();
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

        width = w;
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(circlePath, circlePaint);
    }

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
        // commit the path to offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so i don't double draw
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

                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

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

    //clear canvas

    private void clearDrawing() {

        setDrawingCacheEnabled(false);
        onSizeChanged(width, height, width, height);
        setDrawingCacheEnabled(true);
    }

    //Looper

    private void printDataFile() {
        System.gc();

        radio = ((DrawActivity2) getContext()).getSender();

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

        int portNumber = ((DrawActivity2) getContext()).getPort();
        String targetIPStr = ((DrawActivity2) getContext()).getIp();
        try {
            targetIP = InetAddress.getByName(targetIPStr);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            sender1 = new OSCPortOut(targetIP, portNumber);
            sender2 = new OSCPortOut(targetIP, portNumber + 1);
            sender3 = new OSCPortOut(targetIP, portNumber + 2);
            sender4 = new OSCPortOut(targetIP, portNumber + 3);
            sender5 = new OSCPortOut(targetIP, portNumber + 4);
            sender6 = new OSCPortOut(targetIP, portNumber + 5);
            sender7 = new OSCPortOut(targetIP, portNumber + 6);
            sender8 = new OSCPortOut(targetIP, portNumber + 7);

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    //send OSC messages

    private void sendMyOscMessage() {

        try {

            if(!((DrawActivity2) getContext()).getPauser()){

                radioTrack = ((DrawActivity2) getContext()).getTrack();
            OSCMessage msgX1 = new OSCMessage();
            msgX1.setAddress("/X_1");
            msgX1.addArgument(x1);

            OSCMessage msgY1 = new OSCMessage();
            msgY1.setAddress("/Y_1");
            msgY1.addArgument(y1);

            sender1.send(msgX1);
            sender1.send(msgY1);

                switch (radioTrack) {
                    case 2: {

                        OSCMessage msgX4 = new OSCMessage();
                        msgX4.setAddress("/X_4");
                        msgX4.addArgument(x4);

                        // Log.d("x1/maxX: ", Float.toString(x1));

                        OSCMessage msgY4 = new OSCMessage();
                        msgY4.setAddress("/Y_4");
                        msgY4.addArgument(y4);

                        sender4.send(msgX4);
                        sender4.send(msgY4);
                        break;
                    }
                    case 3: {

                        OSCMessage msgX3 = new OSCMessage();
                        msgX3.setAddress("/X_3");
                        msgX3.addArgument(x3);


                        OSCMessage msgY3 = new OSCMessage();
                        msgY3.setAddress("/Y_3");
                        msgY3.addArgument(y3);

                        sender3.send(msgX3);
                        sender3.send(msgY3);

                        OSCMessage msgX7 = new OSCMessage();
                        msgX7.setAddress("/X_7");
                        msgX7.addArgument(x7);


                        OSCMessage msgY7 = new OSCMessage();
                        msgY7.setAddress("/Y_7");
                        msgY7.addArgument(y7);


                        sender7.send(msgX7);
                        sender7.send(msgY7);
                        break;
                    }
                    case 4: {

                        OSCMessage msgX6 = new OSCMessage();
                        msgX6.setAddress("/X_6");
                        msgX6.addArgument(x6);


                        OSCMessage msgY6 = new OSCMessage();
                        msgY6.setAddress("/Y_6");
                        msgY6.addArgument(y6);

                        sender6.send(msgX6);
                        sender6.send(msgY6);
                        break;
                    }
                    case 5: {

                        OSCMessage msgX2 = new OSCMessage();
                        msgX2.setAddress("/X_2");
                        msgX2.addArgument(x2);


                        OSCMessage msgY2 = new OSCMessage();
                        msgY2.setAddress("/Y_2");
                        msgY2.addArgument(y2);

                        sender2.send(msgX2);
                        sender2.send(msgY2);
                        break;
                    }
                    case 6:{

                        OSCMessage msgX3 = new OSCMessage();
                        msgX3.setAddress("/X_3");
                        msgX3.addArgument(x3);


                        OSCMessage msgY3 = new OSCMessage();
                        msgY3.setAddress("/Y_3");
                        msgY3.addArgument(y3);

                        sender3.send(msgX3);
                        sender3.send(msgY3);

                        OSCMessage msgX5 = new OSCMessage();
                        msgX5.setAddress("/X_5");
                        msgX5.addArgument(x5);


                        OSCMessage msgY5 = new OSCMessage();
                        msgY5.setAddress("/Y_5");
                        msgY5.addArgument(y5);

                        sender5.send(msgX5);
                        sender5.send(msgY5);


                        OSCMessage msgX7 = new OSCMessage();
                        msgX7.setAddress("/X_7");
                        msgX7.addArgument(x7);


                        OSCMessage msgY7 = new OSCMessage();
                        msgY7.setAddress("/Y_7");
                        msgY7.addArgument(y7);


                        sender7.send(msgX7);
                        sender7.send(msgY7);
                        break;
                    }
                    case 7:{

                        OSCMessage msgX2 = new OSCMessage();
                        msgX2.setAddress("/X_2");
                        msgX2.addArgument(x2);


                        OSCMessage msgY2 = new OSCMessage();
                        msgY2.setAddress("/Y_2");
                        msgY2.addArgument(y2);

                        sender2.send(msgX2);
                        sender2.send(msgY2);

                        OSCMessage msgX3 = new OSCMessage();
                        msgX3.setAddress("/X_3");
                        msgX3.addArgument(x3);


                        OSCMessage msgY3 = new OSCMessage();
                        msgY3.setAddress("/Y_3");
                        msgY3.addArgument(y3);

                        sender3.send(msgX3);
                        sender3.send(msgY3);

                        OSCMessage msgX4 = new OSCMessage();
                        msgX4.setAddress("/X_4");
                        msgX4.addArgument(x4);


                        OSCMessage msgY4 = new OSCMessage();
                        msgY4.setAddress("/Y_4");
                        msgY4.addArgument(y4);

                        sender4.send(msgX4);
                        sender4.send(msgY4);


                        OSCMessage msgX5 = new OSCMessage();
                        msgX5.setAddress("/X_5");
                        msgX5.addArgument(x5);


                        OSCMessage msgY5 = new OSCMessage();
                        msgY5.setAddress("/Y_5");
                        msgY5.addArgument(y5);

                        sender5.send(msgX5);
                        sender5.send(msgY5);


                        OSCMessage msgX6 = new OSCMessage();
                        msgX6.setAddress("/X_6");
                        msgX6.addArgument(x6);


                        OSCMessage msgY6 = new OSCMessage();
                        msgY6.setAddress("/Y_6");
                        msgY6.addArgument(y6);


                        sender6.send(msgX6);
                        sender6.send(msgY6);

                        OSCMessage msgX7 = new OSCMessage();
                        msgX7.setAddress("/X_7");
                        msgX7.addArgument(x7);

                        OSCMessage msgY7 = new OSCMessage();
                        msgY7.setAddress("/Y_7");
                        msgY7.addArgument(y7);


                        sender7.send(msgX7);
                        sender7.send(msgY7);

                        OSCMessage msgX8 = new OSCMessage();
                        msgX8.setAddress("/X_8");
                        msgX8.addArgument(x8);

                        OSCMessage msgY8 = new OSCMessage();
                        msgY8.setAddress("/Y_8");
                        msgY8.addArgument(y8);

                        sender8.send(msgX8);
                        sender8.send(msgY8);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

        } catch (Exception ignored) {

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
