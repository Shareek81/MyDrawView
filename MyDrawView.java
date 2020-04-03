package com.example.shareek.lrltest2;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class MyDrawView extends View {
    public Bitmap  mBitmap;
    public Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private Paint   mPaint;
    private int color=1;
    private ArrayList<Path> undonePaths = new ArrayList<Path>();
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Integer> pathclr = new ArrayList<Integer>();
    private ArrayList<Integer> upathclr = new ArrayList<Integer>();
    private boolean isErasemode = false;

    public MyDrawView(Context c, AttributeSet attrs) {
        super(c, attrs);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
        mPath = new Path();
    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i=0;i<paths.size();i++) {
            switch(pathclr.get(i))
            {
                case 0:mPaint.setColor(Color.BLACK);break;
                case 1:mPaint.setColor(Color.BLUE);break;
                case 2:mPaint.setColor(Color.RED);break;
                case 3:mPaint.setColor(Color.CYAN);break;
                case 4:mPaint.setColor(Color.GREEN);break;
                case 5:mPaint.setColor(Color.MAGENTA);break;
                case 6:mPaint.setColor(Color.YELLOW);break;
            }
            canvas.drawPath(paths.get(i), mPaint);
        }
        switch(color)
        {
            case 0:mPaint.setColor(Color.BLACK);break;
            case 1:mPaint.setColor(Color.BLUE);break;
            case 2:mPaint.setColor(Color.RED);break;
            case 3:mPaint.setColor(Color.CYAN);break;
            case 4:mPaint.setColor(Color.GREEN);break;
            case 5:mPaint.setColor(Color.MAGENTA);break;
            case 6:mPaint.setColor(Color.YELLOW);break;
        }
        canvas.drawPath(mPath, mPaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        undonePaths.clear();
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
        }
    }
    public void toeraser(){
        if(!isErasemode){
            isErasemode = true;
        }else{
            isErasemode = false;
        }
    }

    private void remove(int index){
        paths.remove(index);
        pathclr.remove(index);
        invalidate();
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        paths.add(mPath);
        pathclr.add(color);
        mPath = new Path();
    }
    public void onClickUndo () {
        if (paths.size()>=0 && pathclr.size()>=0)
        {
            try{
                undonePaths.add(paths.remove(paths.size()-1));
                upathclr.add(pathclr.remove(pathclr.size()-1));
                invalidate();}
            catch(Exception e){}
        }
        else
        {

        }
    }

    public void onClickRedo (){

        if (undonePaths.size()>=0 && upathclr.size()>=0)
        {
            try{
                paths.add(undonePaths.remove(undonePaths.size()-1));
                pathclr.add(upathclr.remove(upathclr.size()-1));
                invalidate();}
            catch(Exception e){}
        }
        else
        {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch(color)
        {
            case 0:mPaint.setColor(Color.BLACK);break;
            case 1:mPaint.setColor(Color.BLUE);break;
            case 2:mPaint.setColor(Color.RED);break;
            case 3:mPaint.setColor(Color.CYAN);break;
            case 4:mPaint.setColor(Color.GREEN);break;
            case 5:mPaint.setColor(Color.MAGENTA);break;
            case 6:mPaint.setColor(Color.YELLOW);break;
        }
        if(isErasemode){

            for(int i = 0;i<paths.size();i++){
                RectF r = new RectF();
                Point pComp = new Point((int) (event.getX()), (int) (event.getY() ));

                Path mPath = paths.get(i);
                mPath.computeBounds(r, true);
                if (r.contains(pComp.x, pComp.y)) {
                    remove(i);
                    break;
                }
            }
            return false;
        }else {

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
    }

    public Bitmap getBitmap()
    {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return bmp;
    }
    public void setclr(int clr)
    {
        color=clr;
    }
    public void clearit(){
        while (paths.size()>0)
        {
            try{
                paths.remove(paths.size()-1);
                invalidate();}
            catch(Exception e){}
        }
        while (pathclr.size()>0)
        {
            try{
                pathclr.remove(pathclr.size()-1);
                invalidate();}
            catch(Exception e){}
        }
    }

    public void setBackImg(String loc)
    {
        Resources r = getResources();
        BitmapDrawable bd=new BitmapDrawable(r,BitmapFactory.decodeFile(loc));
        setBackground(bd);
    }
}

