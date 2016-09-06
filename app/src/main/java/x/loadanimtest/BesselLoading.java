package x.loadanimtest;

/**
 * Created by xushengfu on 16/9/5.
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cookie on 2016/3/8.
 */
public class BesselLoading extends View {

    private final static int[] CIRCLE_X = new int[]{120, 240, 360, 480};
    public static final int ALPHA = 255;

    private Paint[] mPaint = new Paint[4];

    private int floatY;
    //
    private float mRadius;
    private int mColor;
    private long mDuration;
    //
    int[] alphas = new int[]{ALPHA,
            ALPHA,
            ALPHA,
            ALPHA};


    public BesselLoading(Context context) {
        this(context, null);
    }

    public BesselLoading(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BesselLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //从xml文件中获取数据
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.BesselLoading);

        int count = mTypedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = mTypedArray.getIndex(i);
            if (R.styleable.BesselLoading_radius == attr) {
                mRadius = mTypedArray.getDimensionPixelSize(attr, 20);
            } else if (R.styleable.BesselLoading_circlecolor == attr) {
                mColor = mTypedArray.getColor(attr, 0xff00dddd);
            } else if (R.styleable.BesselLoading_duration == attr) {
                mDuration = mTypedArray.getInt(attr, 1000);
            }
        }

        mTypedArray.recycle();

        initialize();
    }

    //初始化
    private void initialize() {
        floatY = 50;

        for (int i = 0; i < 4; i++) {
            mPaint[i] = new Paint();
            mPaint[i].setColor(mColor);
            mPaint[i].setAntiAlias(true);
            setAnimation(i);
        }
    }

    private void setAnimation(final int i) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(255, 100, 255);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setStartDelay(CIRCLE_X[i]);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alphas[i] = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth;
        int mHeight;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = getPaddingLeft() + 600 + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = getPaddingTop() + 100 + getPaddingBottom();
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(final Canvas canvas) {

        drawCircle(canvas);
        super.onDraw(canvas);
    }

    //画3个圆
    private void drawCircle(Canvas canvas) {

        for (int i = 0; i < 4; i++) {
            mPaint[i].setAlpha(alphas[i]);
            canvas.drawCircle(CIRCLE_X[i], floatY, mRadius, mPaint[i]);
        }
    }
}