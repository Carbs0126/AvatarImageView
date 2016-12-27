package cn.carbs.android.avatarimageview.library;

import android.carbs.cn.library.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Carbs.Wang on 2016/12/7.
 *
 *  1.make a bitmap with 4 round corners
 *  2.can not make string to "bitmap"
 *
 */
public class SquareAvatarImageView extends ImageView {

    private static final int COLOR_DRAWABLE_DIMENSION = 1;
    private static final Bitmap.Config BITMAP_CONFIG_8888 = Bitmap.Config.ARGB_8888;
    private static final int DEFAULT_CORNER_RADIUS_DP = 6;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private Matrix mMatrix = new Matrix();
    private RectF mRectF = new RectF();
    private Paint mPaintDraw= new Paint();
    private float mImageViewH;
    private float mImageViewW;
    private float mDrawableW;
    private float mDrawableH;
    private int mCornerRadius = -1;

    public SquareAvatarImageView(Context context) {
        super(context);
        init();
    }
    public SquareAvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }
    public SquareAvatarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void init(){
        mPaintDraw.setAntiAlias(true);
        mPaintDraw.setStyle(Paint.Style.FILL);
        if (mCornerRadius < 0){
            mCornerRadius = dp2px(getContext(), DEFAULT_CORNER_RADIUS_DP);
        }
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView);
        if (a == null) {
            return;
        }
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.AvatarImageView_aiv_CornerRadius) {
                mCornerRadius = a.getDimensionPixelSize(attr, dp2px(getContext(), DEFAULT_CORNER_RADIUS_DP));
            }
        }
        a.recycle();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        setDrawable(drawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        Drawable drawable =new BitmapDrawable(bm);
        setDrawable(drawable);
    }

    public void setDrawable(Drawable drawable) {
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        setBitmap(bitmap);
    }

    private void setBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (bitmap != this.mBitmap) {
            this.mBitmap = bitmap;
            invalidate();
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            mDrawableW = drawable.getIntrinsicWidth();
            mDrawableH = drawable.getIntrinsicHeight();
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            mDrawableW = canvas.getWidth();
            mDrawableH = canvas.getHeight();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setImageResource(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setDrawable(getContext().getDrawable(resId));
        }else{
            setDrawable(getContext().getResources().getDrawable(resId));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            toDrawBitmap(canvas);
        }
    }

    private void toDrawBitmap(Canvas canvas) {
        if(mBitmap == null) return;
        drawBitmap(canvas, mBitmap, true);
    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap, boolean adjustScale){
        refreshBitmapShaderConfig(bitmap, adjustScale);
        mPaintDraw.setShader(mBitmapShader);
        canvas.drawRoundRect(mRectF, mCornerRadius,mCornerRadius, mPaintDraw);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mImageViewH = h - getPaddingTop() - getPaddingBottom();
        mImageViewW = w - getPaddingLeft() - getPaddingRight();
        mRectF.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
    }

    private void refreshBitmapShaderConfig(Bitmap bitmap, boolean adjustScale){
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mMatrix.reset();
        if(adjustScale) {
            if ((mImageViewH * mDrawableW > mDrawableH * mImageViewW)) {
                float scale1 = (mImageViewH)/(mDrawableH);
                float offset1 = (mDrawableW * scale1 - mImageViewW)/2;
                mMatrix.setScale(scale1, scale1);
                mMatrix.postTranslate(-offset1 + getPaddingLeft(), 0 + getPaddingTop());
            } else {
                float scale2 = (mImageViewW)/(mDrawableW);
                float offset2 = (mDrawableH * scale2 - mImageViewH)/2;
                mMatrix.setScale(scale2, scale2);
                mMatrix.postTranslate(0 + getPaddingLeft(), -offset2 + getPaddingTop());
            }
        }
        mBitmapShader.setLocalMatrix(mMatrix);
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
