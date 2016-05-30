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
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AvatarImageView extends ImageView {

    public static final int[] COLORS = {0xff44bb66,
                                        0xff55ccdd,
                                        0xffbb7733,
                                        0xffff6655,
                                        0xffffbb44,
                                        0xff44aaff};

    private static final int COLORS_NUMBER = COLORS.length;

    private static final float DEFAULT_TEXT_SIZE_RATIO = 0.4f;
    private static final int DEFAULT_BOARDER_COLOR = 0xffffffff;
    private static final int DEFAULT_BOARDER_WIDTH = 4;

    private static final int DEFAULT_TYPE_BITMAP = 0;
    private static final int DEFAULT_TYPE_TEXT = 1;

    private String text;
    private int colorBg = COLORS[0];
    private float textSizeRatio = DEFAULT_TEXT_SIZE_RATIO;
    private int boarderColor = DEFAULT_BOARDER_COLOR;
    private float boarderWidth = DEFAULT_BOARDER_WIDTH;
    private boolean showBoarder = false;

    private Paint paintTextForeground;
    private Paint paintTextBackground;
    private Paint paintDraw;
    private Paint paintCircle;
    private Paint.FontMetrics fontMetrics;

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
                textSizeRatio = a.getFloat(attr, DEFAULT_TEXT_SIZE_RATIO);
            }else if (attr == R.styleable.AvatarImageView_aiv_BoarderWidth) {
                boarderWidth = a.getDimensionPixelSize(attr, DEFAULT_BOARDER_WIDTH);
            }else if (attr == R.styleable.AvatarImageView_aiv_BoarderColor) {
                boarderColor = a.getColor(attr, DEFAULT_BOARDER_COLOR);
            }else if (attr == R.styleable.AvatarImageView_aiv_ShowBoarder){
                showBoarder = a.getBoolean(attr, false);
            }
        }
        a.recycle();
    }

    private void init() {
        mMatrix = new Matrix();

        paintTextForeground = new Paint();
        paintTextForeground.setColor(0xffffffff);
        paintTextForeground.setAntiAlias(true);
        paintTextForeground.setTextAlign(Paint.Align.CENTER);

        paintTextBackground = new Paint();
        paintTextBackground.setColor(colorBg);
        paintTextBackground.setAntiAlias(true);
        paintTextBackground.setStyle(Paint.Style.FILL);

        paintDraw = new Paint();
        paintDraw.setAntiAlias(true);
        paintDraw.setStyle(Paint.Style.FILL);

        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setColor(boarderColor);
        paintCircle.setStrokeWidth(boarderWidth);
    }

    private int radius;
    private int centerX;
    private int centerY;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int contentWidth = w - paddingLeft - getPaddingRight();
        int contentHeight = h - paddingTop - getPaddingBottom();

        radius = contentWidth < contentHeight ? contentWidth / 2 : contentHeight / 2;
        centerX = paddingLeft + radius;
        centerY = paddingTop + radius;
        refreshTextSizeConfig();
    }

    private void refreshTextSizeConfig() {
        paintTextForeground.setTextSize(textSizeRatio * 2 * radius);
        fontMetrics = paintTextForeground.getFontMetrics();
    }

    public void setTextAndColor(String text, int colorBg) {
        if (this.type != DEFAULT_TYPE_TEXT || !stringEqual(text, this.text) || colorBg != this.colorBg) {
            this.text = text;
            this.colorBg = colorBg;
            this.type = DEFAULT_TYPE_TEXT;
            invalidate();
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

    public void setTextAndColorSeed(String text, String colorSeed) {
        setTextAndColor(text, getColorBySeed(colorSeed));
    }

    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (this.type != DEFAULT_TYPE_BITMAP || bitmap != this.bitmap) {
            this.bitmap = bitmap;
            this.type = DEFAULT_TYPE_BITMAP;
            invalidate();
        }
    }

    public void setDrawable(Drawable drawable) {
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        setBitmap(bitmap);
    }

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

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
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
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

    private int type = DEFAULT_TYPE_BITMAP;

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (bitmap != null && type == DEFAULT_TYPE_BITMAP) {
            drawBitmap(canvas);
        } else if (text != null && type == DEFAULT_TYPE_TEXT) {
            drawText(canvas);
        }
        if(showBoarder){
            drawBoarder(canvas);
        }
    }

    private BitmapShader mBitmapShader;
    private Matrix mMatrix;
    private int bitmapWidth;
    private int bitmapHeight;

    private void drawBitmap(Canvas canvas) {

        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();

        int bSize = Math.min(bitmapWidth, bitmapHeight);
        float scale = radius * 2.0f / bSize;
        mMatrix.reset();
        mMatrix.setScale(scale, scale);
        if (bitmapWidth > bitmapHeight) {
            mMatrix.postTranslate(-(bitmapWidth * scale / 2 - radius - getPaddingLeft()), getPaddingTop());
        } else {
            mMatrix.postTranslate(getPaddingLeft(), -(bitmapHeight * scale / 2 - radius - getPaddingTop()));
        }

        mBitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mMatrix);

        paintDraw.setShader(mBitmapShader);
        canvas.drawCircle(centerX, centerY, radius, paintDraw);

    }

    private void drawText(Canvas canvas) {
        paintTextBackground.setColor(colorBg);
        canvas.drawCircle(centerX, centerY, radius, paintTextBackground);
        canvas.drawText(text, 0, text.length(), centerX, centerY + Math.abs(fontMetrics.top + fontMetrics.bottom) / 2, paintTextForeground);
    }

    private void drawBoarder(Canvas canvas){
        canvas.drawCircle(centerX, centerY, radius - boarderWidth/2, paintCircle);
    }

    public int getColorBySeed(String seed) {
        if (TextUtils.isEmpty(seed)) {
            return COLORS[0];
        }
        return COLORS[Math.abs(seed.hashCode()) % COLORS_NUMBER];
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

    //static function to add image to avatarview, using rxandroid
    public static void updateAvatarView(final AvatarImageView avatarImageView,
                                        final Object uniqueIdentifierTag,
                                        final LruCache<String, Bitmap> cache,
                                        final String cacheKey,
                                        final String localImagePath,
                                        final String textSeed) {

        avatarImageView.setTag(uniqueIdentifierTag);
        Bitmap retBitmap = null;
        if (cache != null) {
            retBitmap = cache.get(cacheKey);
        }
        if (retBitmap != null) {
            if ((uniqueIdentifierTag == null && avatarImageView.getTag() == null)
                    || (uniqueIdentifierTag != null && uniqueIdentifierTag.equals(avatarImageView.getTag()))) {
                avatarImageView.setBitmap(retBitmap);
            }
        } else {
            Observable
                    .create(new Observable.OnSubscribe<Bitmap>() {
                        @Override
                        public void call(Subscriber<? super Bitmap> subscriber) {
                            Bitmap bitmap = null;
                            if (!TextUtils.isEmpty(localImagePath)) {
                                bitmap = BitmapFactory.decodeFile(localImagePath);
                                if (bitmap != null && cache != null) {
                                    cache.put(cacheKey, bitmap);
                                }
                            }
                            subscriber.onNext(bitmap);
                            subscriber.onCompleted();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Bitmap>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Bitmap b) {
                            if ((uniqueIdentifierTag == null && avatarImageView.getTag() == null)
                                    || (uniqueIdentifierTag != null && uniqueIdentifierTag.equals(avatarImageView.getTag()))) {
                                if (b == null) {
                                    if (textSeed == null || TextUtils.isEmpty(textSeed.trim())) {
                                        avatarImageView.setTextAndColorSeed(" ", " ");
                                    } else {
                                        avatarImageView.setTextAndColorSeed(textSeed.trim().substring(0, 1), textSeed);
                                    }
                                } else {
                                    avatarImageView.setBitmap(b);
                                }
                            }
                        }
                    });
        }

    }

}
