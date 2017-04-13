package cn.carbs.android.avatarimageview.library;

import android.carbs.cn.library.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AvatarImageView extends ImageView {

    public static final int[] COLORS = {0xff44bb66,
                                        0xff55ccdd,
                                        0xffbb7733,
                                        0xffff6655,
                                        0xffffbb44,
                                        0xff44aaff};

    private static final int COLORS_NUMBER = COLORS.length;
    private static final int DEFAULT_TEXT_COLOR = 0xffffffff;
    private static final int DEFAULT_BOARDER_COLOR = 0xffffffff;
    private static final int DEFAULT_BOARDER_WIDTH = 4;
    private static final int DEFAULT_TYPE_BITMAP = 0;
    private static final int DEFAULT_TYPE_TEXT = 1;
    private static final String DEFAULT_TEXT = "";
    private static final int COLORDRAWABLE_DIMENSION = 1;
    private static final float DEFAULT_TEXT_SIZE_RATIO = 0.4f;
    private static final float DEFAULT_TEXT_MASK_RATIO = 0.8f;
    private static final boolean DEFAULT_BOARDER_SHOW = false;
    private static final Bitmap.Config BITMAP_CONFIG_8888 = Bitmap.Config.ARGB_8888;
    private static final Bitmap.Config BITMAP_CONFIG_4444 = Bitmap.Config.ARGB_4444;

    private int mRadius;//the circle's radius
    private int mCenterX;
    private int mCenterY;
    private int mType = DEFAULT_TYPE_BITMAP;
    private int mBgColor = COLORS[0];//background color when show text
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mBoarderColor = DEFAULT_BOARDER_COLOR;
    private int mBoarderWidth = DEFAULT_BOARDER_WIDTH;
    private float mTextSizeRatio = DEFAULT_TEXT_SIZE_RATIO;//the text size divides (2 * mRadius)
    private float mTextMaskRatio = DEFAULT_TEXT_MASK_RATIO;//the inner-radius text divides outer-radius text
    private boolean mShowBoarder = DEFAULT_BOARDER_SHOW;
    private String mText = DEFAULT_TEXT;

    private Paint mPaintTextForeground;//draw text, in text mode
    private Paint mPaintTextBackground;//draw circle, in text mode
    private Paint mPaintDraw;//draw bitmap, int bitmap mode
    private Paint mPaintCircle;//draw boarder
    private Paint.FontMetrics mFontMetrics;

    private Bitmap mBitmap;//the pic
    private BitmapShader mBitmapShader;//used to adjust position of bitmap
    private Matrix mMatrix;//used to adjust position of bitmap

    public AvatarImageView(Context context) {
        super(context);
        init();
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public AvatarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
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
            if (attr == R.styleable.AvatarImageView_aiv_TextSizeRatio) {
                mTextSizeRatio = a.getFloat(attr, DEFAULT_TEXT_SIZE_RATIO);
            }else if (attr == R.styleable.AvatarImageView_aiv_TextMaskRatio){
                mTextMaskRatio = a.getFloat(attr, DEFAULT_TEXT_MASK_RATIO);
            }else if (attr == R.styleable.AvatarImageView_aiv_BoarderWidth) {
                mBoarderWidth = a.getDimensionPixelSize(attr, DEFAULT_BOARDER_WIDTH);
            }else if (attr == R.styleable.AvatarImageView_aiv_BoarderColor) {
                mBoarderColor = a.getColor(attr, DEFAULT_BOARDER_COLOR);
            }else if (attr == R.styleable.AvatarImageView_aiv_TextColor) {
                mTextColor = a.getColor(attr, DEFAULT_TEXT_COLOR);
            }else if (attr == R.styleable.AvatarImageView_aiv_ShowBoarder){
                mShowBoarder = a.getBoolean(attr, DEFAULT_BOARDER_SHOW);
            }
        }
        a.recycle();
    }

    private void init() {
        mMatrix = new Matrix();

        mPaintTextForeground = new Paint();
        mPaintTextForeground.setColor(mTextColor);
        mPaintTextForeground.setAntiAlias(true);
        mPaintTextForeground.setTextAlign(Paint.Align.CENTER);

        mPaintTextBackground = new Paint();
        mPaintTextBackground.setAntiAlias(true);
        mPaintTextBackground.setStyle(Paint.Style.FILL);

        mPaintDraw = new Paint();
        mPaintDraw.setAntiAlias(true);
        mPaintDraw.setStyle(Paint.Style.FILL);

        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setColor(mBoarderColor);
        mPaintCircle.setStrokeWidth(mBoarderWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int contentWidth = w - paddingLeft - getPaddingRight();
        int contentHeight = h - paddingTop - getPaddingBottom();

        mRadius = contentWidth < contentHeight ? contentWidth / 2 : contentHeight / 2;
        mCenterX = paddingLeft + mRadius;
        mCenterY = paddingTop + mRadius;
        refreshTextSizeConfig();
    }

    private void refreshTextSizeConfig() {
        mPaintTextForeground.setTextSize(mTextSizeRatio * 2 * mRadius);
        mFontMetrics = mPaintTextForeground.getFontMetrics();
    }

    private void refreshTextConfig(){
        if(mBgColor != mPaintTextBackground.getColor()) {
            mPaintTextBackground.setColor(mBgColor);
        }
        if(mTextColor != mPaintTextForeground.getColor()) {
            mPaintTextForeground.setColor(mTextColor);
        }
    }

    public void setTextAndColor(String text, int bgColor) {
        if (this.mType != DEFAULT_TYPE_TEXT || !stringEqual(text, this.mText) || bgColor != this.mBgColor) {
            this.mText = text;
            this.mBgColor = bgColor;
            this.mType = DEFAULT_TYPE_TEXT;
            invalidate();
        }
    }

    public void setTextColor(int textColor){
        if(this.mTextColor != textColor) {
            mTextColor = textColor;
            mPaintTextForeground.setColor(mTextColor);
            invalidate();
        }
    }

    public void setTextAndColorSeed(String text, String colorSeed) {
        setTextAndColor(text, getColorBySeed(colorSeed));
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (this.mType != DEFAULT_TYPE_BITMAP || bitmap != this.mBitmap) {
            this.mBitmap = bitmap;
            this.mType = DEFAULT_TYPE_BITMAP;
            invalidate();
        }
    }

    public void setDrawable(Drawable drawable) {
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        setBitmap(bitmap);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null && mType == DEFAULT_TYPE_BITMAP) {
            toDrawBitmap(canvas);
        } else if (mText != null && mType == DEFAULT_TYPE_TEXT) {
            toDrawText(canvas);
        }
        if(mShowBoarder){
            drawBoarder(canvas);
        }
    }

    private void toDrawText(Canvas canvas){
        if(mText.length() == 1) {
            //draw text to the view's canvas directly
            drawText(canvas);
        }else{
            //draw text with clip effect, need to create a bitmap
            drawBitmap(canvas, createClipTextBitmap((int) (mRadius / mTextMaskRatio)), false);
        }
    }

    private void toDrawBitmap(Canvas canvas) {
        if(mBitmap == null) return;
        drawBitmap(canvas, mBitmap, true);
    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap, boolean adjustScale){
        refreshBitmapShaderConfig(bitmap, adjustScale);
        mPaintDraw.setShader(mBitmapShader);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaintDraw);
    }

    private void refreshBitmapShaderConfig(Bitmap bitmap, boolean adjustScale){
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mMatrix.reset();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if(adjustScale) {
            int bSize = Math.min(bitmapWidth, bitmapHeight);
            float scale = mRadius * 2.0f / bSize;//TODO
            mMatrix.setScale(scale, scale);
            if (bitmapWidth > bitmapHeight) {
                mMatrix.postTranslate(-(bitmapWidth * scale / 2 - mRadius - getPaddingLeft()), getPaddingTop());
            } else {
                mMatrix.postTranslate(getPaddingLeft(), -(bitmapHeight * scale / 2 - mRadius - getPaddingTop()));
            }
        }else{
            mMatrix.postTranslate(-(bitmapWidth * 1 / 2 - mRadius - getPaddingLeft()), -(bitmapHeight * 1 / 2 - mRadius - getPaddingTop()));
        }

        mBitmapShader.setLocalMatrix(mMatrix);
    }

    private Bitmap createClipTextBitmap(int bitmapRadius){
        Bitmap bitmapClipText = Bitmap.createBitmap(bitmapRadius * 2, bitmapRadius *2, BITMAP_CONFIG_4444);
        Canvas canvasClipText = new Canvas(bitmapClipText);
        Paint paintClipText = new Paint();
        paintClipText.setStyle(Paint.Style.FILL);
        paintClipText.setAntiAlias(true);
        paintClipText.setColor(mBgColor);
        canvasClipText.drawCircle(bitmapRadius, bitmapRadius, bitmapRadius, paintClipText);

        paintClipText.setTextSize(mTextSizeRatio * mRadius * 2);
        paintClipText.setColor(mTextColor);
        paintClipText.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paintClipText.getFontMetrics();
        canvasClipText.drawText(mText, 0, mText.length(), bitmapRadius,
                bitmapRadius + Math.abs(fontMetrics.top + fontMetrics.bottom) / 2, paintClipText);
        return bitmapClipText;
    }

    private void drawText(Canvas canvas) {
        refreshTextConfig();
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaintTextBackground);
        canvas.drawText(mText, 0, mText.length(), mCenterX, mCenterY + Math.abs(mFontMetrics.top + mFontMetrics.bottom) / 2, mPaintTextForeground);
    }

    private void drawBoarder(Canvas canvas){
        canvas.drawCircle(mCenterX, mCenterY, mRadius - mBoarderWidth /2, mPaintCircle);
    }

    public int getColorBySeed(String seed) {
        if (TextUtils.isEmpty(seed)) {
            return COLORS[0];
        }
        return COLORS[Math.abs(seed.hashCode() % COLORS_NUMBER)];
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        setDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setDrawable(getContext().getDrawable(resId));
        }else{
            setDrawable(getContext().getResources().getDrawable(resId));
        }
    }

    private boolean stringEqual(String a, String b){
        if(a == null){
            return (b == null);
        }else{
            if(b == null){
                return false;
            }
            return a.equals(b);
        }
    }
}
