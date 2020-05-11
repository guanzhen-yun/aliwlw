package com.ali.alisimulate.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ali.alisimulate.R;

import java.util.ArrayList;
import java.util.List;

public class TopViewCycle extends FrameLayout {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 轮播视图
     */
    private CycleViewPager mViewPager;
    /**
     * 数据集合
     * Map<String,String> map=new HashMap<String, String>();
     * map.put("","");
     */
    private List<String> data = new ArrayList<>();

    /**
     * 图片轮播指示器容器
     */
    private LinearLayout mIndicationGroup;
    /**
     * 轮播的总数
     */
    private int mCount = 0;
    /**
     * 未获得焦点指示器资源
     */
    private Bitmap unFocusIndicationStyle;
    /**
     * 获得焦点指示器资源
     */
    private Bitmap focusIndicationStyle;
    /**
     * 单击事件监听器
     */
    private OnPageClickListener mOnPageClickListener;
    /**
     * 图片轮播是自动滚动状态  true 自动滚动，false 图片不能自动滚动只能手动左右滑动
     */
    private boolean isAutoCycle = true;
    /**
     * 自动轮播时间间隔默认3秒
     */
    private long mCycleDelayed = 3000;
    /**
     * 图片是否无限切换。true 是
     */
    private boolean isCountMin = true;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                handler.sendEmptyMessageDelayed(0, mCycleDelayed);
            }
            return false;
        }
    });
    private CyclePageChangeListener mListener;


    public TopViewCycle(Context context) {
        super(context);
        init(context);
    }


    public TopViewCycle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    /**
     * 初始化基础信息
     */
    private void init(Context context) {
        mContext = context;
        unFocusIndicationStyle = drawCircle(8, Color.GRAY);
        focusIndicationStyle = drawOval(8, Color.GREEN);
        initView();
    }


    /**
     * 初始化view控件
     *
     * @author 代凯男
     */
    private void initView() {
        View.inflate(mContext, R.layout.view_cycle, this);
        FrameLayout fl_cycle = findViewById(R.id.fl_cycle);
        mViewPager = new CycleViewPager(mContext);
        mViewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        fl_cycle.addView(mViewPager);
        mListener = new CyclePageChangeListener();
        mViewPager.setOnPageChangeListener(mListener);
        mIndicationGroup = findViewById(R.id.ll_indication_group);
    }


    /**
     * 设置轮播指示器样式，如果你对默认的样式不满意可以自己设置
     *
     * @param unFocus               未获得焦点指示器资源id  图片或shape或color值
     * @param focus                 获得焦点指示器资源id 图片或shape或color值
     */
    public void setIndicationStyle(int unFocus, int focus) {
        unFocusIndicationStyle = drawCircle(8, unFocus);
        focusIndicationStyle = drawOval(8, focus);
        initIndication();
    }

    public CyclePageChangeListener getListener() {
        return mListener;
    }


    public void setListener(CyclePageChangeListener listener) {
        mListener = listener;
    }


    /**
     * 设置是否自动无限轮播
     *
     * @param delayed 自动轮播时间间隔
     */
    public void setCycleDelayed(long delayed) {
        mCycleDelayed = delayed;
    }


    /**
     * 设置是否自动无限轮播
     */
    public void setAutoCycle(Boolean state) {
        isAutoCycle = state;
        if (!state) {
            stopImageCycle();
            mIndicationGroup.setVisibility(GONE);
        } else {
            mIndicationGroup.setVisibility(VISIBLE);
            stopImageCycle();
            startImageCycle();
        }
    }


    /**
     * 禁止手动滑动
     */
    public void setScrollEnable(boolean enable) {
        mViewPager.setScanScroll(enable);
    }


    /**
     * 加载显示的数据  网络图片资源及标题
     */
    public void loadData(List<String> list) {
        data = list;
        mCount = list.size();
        initIndication();
        mViewPager.setAdapter(new CycleAdapter());
        mViewPager.setCurrentItem(0);
    }


    /**
     * 设置点击事件监听回调函数
     */
    public void setOnPageClickListener(OnPageClickListener listener) {
        mOnPageClickListener = listener;
    }


    /**
     * 初始化指标器
     */
    private void initIndication() {
        mIndicationGroup.removeAllViews();
        for (int i = 0; i < mCount; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicationGroup
                    .getLayoutParams().height, LinearLayout.LayoutParams.MATCH_PARENT);
            params.leftMargin = 12;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setImageBitmap(focusIndicationStyle);
            } else {
                imageView.setImageBitmap(unFocusIndicationStyle);
            }
            mIndicationGroup.addView(imageView);
        }
    }


    private Bitmap drawOval(int radius, int white) {
        Paint p = new Paint();
        p.setColor(white);
        RectF rectF = new RectF(0, 0, 25, radius);
        Bitmap bitmap = Bitmap.createBitmap(25, radius, Bitmap.Config.ARGB_8888);
        Canvas mc = new Canvas(bitmap);
        mc.drawRoundRect(rectF, 5, 5, p);
        return bitmap;
    }


    private Bitmap drawCircle(int radius, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);// 设置颜色
        Bitmap bitmap = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(radius / 2, radius / 2, radius / 2, paint);
        return bitmap;
    }


    /**
     * 开始图片轮播
     */
    private void startImageCycle() {
        handler.sendEmptyMessageDelayed(0, mCycleDelayed);
    }


    /**
     * 暂停图片轮播
     */
    private void stopImageCycle() {
        handler.removeCallbacksAndMessages(null);
    }


    /**
     * 触摸停止计时器，抬起启动计时器
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isAutoCycle) {
                // 开始图片滚动
                startImageCycle();
            }
        } else {
            if (isAutoCycle) {
                // 停止图片滚动
                stopImageCycle();
            }
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 停止图片滚动
        stopImageCycle();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAutoCycle) {
            startImageCycle();
        }
    }


    //设置是否无限图片
    public void setCountMin(boolean countMin) {
        isCountMin = countMin;
    }


    public enum IndicationStyle {
        COLOR, IMAGE
    }

    /**
     * 轮播控件的监听事件
     */
    public interface OnPageClickListener {
        /**
         * 单击事件
         */
        void onClick(int position);
    }

    public static class ImageInfo {
        public Object image;
        public String share_url = "";
        public String share_content = "";
        public String text = "";
        public Object value;
        public int type;


        public ImageInfo(Object image, String text, Object value, String share_url, String share_content, int type) {
            this.image = image;
            this.text = text;
            this.value = value;
            this.share_url = share_url;
            this.share_content = share_content;
            this.type = type;
        }


        public ImageInfo(Object image, String text, Object value, String share_url, String share_content) {
            this.image = image;
            this.text = text;
            this.value = value;
            this.share_url = share_url;
            this.share_content = share_content;
            this.type = 0;
        }
    }

    /**
     * 轮播监听
     */
    public final class CyclePageChangeListener implements ViewPager.OnPageChangeListener {

        //上次指示器指示的位置,开始为默认位置0
        public int preIndex = 0;


        @Override
        public void onPageSelected(int index) {
            index = index % mCount;

            if (mIndicationGroup.getChildAt(preIndex) != null && unFocusIndicationStyle != null) {
                //恢复默认没有获得焦点指示器样式
                ((ImageView) (mIndicationGroup.getChildAt(preIndex))).setImageBitmap(unFocusIndicationStyle);
            }

            // 设置当前显示图片的指示器样式
            if (mIndicationGroup.getChildAt(index) != null && focusIndicationStyle != null) {
                ((ImageView) (mIndicationGroup.getChildAt(index))).setImageBitmap(focusIndicationStyle);
            }
            preIndex = index;
        }


        @Override
        public void onPageScrollStateChanged(int state) {
        }


        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }
    }

    /**
     * 图片轮播适配器
     */
    private class CycleAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(mContext, R.layout.item_banner, null);
            position = position % mCount;
            TextView tv_test = view.findViewById(R.id.tv_name);
            tv_test.setText(data.get(position));
            TextView tv_look = view.findViewById(R.id.tv_look);
            // 设置图片点击监听
            int finalPosition = position;
            tv_look.setOnClickListener(v -> {
                if (mOnPageClickListener != null) {
                    mOnPageClickListener.onClick(finalPosition);
                }
            });
            container.addView(view);
            return view;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public int getCount() {
            return isCountMin ? Integer.MAX_VALUE : mCount;
        }


        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
    }

    /**
     * 自定义ViewPager主要用于事件处理
     */
    public class CycleViewPager extends ViewPager {
        private boolean isCanScroll = true;
        private float mDownY;
        private float mDownX;


        public CycleViewPager(Context context) {
            super(context);
        }


        public CycleViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }


        /**
         * 事件拦截
         */
        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            if (isCanScroll) {
                return super.onInterceptTouchEvent(event);
            }
            return false;
        }


        /**
         * 事件分发
         */
        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            if (this.isCanScroll) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mDownX = event.getX();
                        mDownY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (Math.abs(event.getX() - mDownX) > Math.abs(event.getY() - mDownY)) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    default:
                        break;
                }
            }
            return super.dispatchTouchEvent(event);
        }


        /**
         * 事件处理
         */
        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            /* return false;//super.onTouchEvent(arg0); */
            if (isCanScroll) {
                return super.onTouchEvent(ev);
            }

            return false;
        }


        public void setScanScroll(boolean isCanScroll) {
            this.isCanScroll = isCanScroll;
        }
    }
}

