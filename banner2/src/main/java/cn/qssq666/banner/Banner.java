package cn.qssq666.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class Banner extends FrameLayout {
    private final static String TAG = "Banner";
    private ViewGroup mTitleGroup;

    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * 在初始化init里面调用的。
     */
    private ViewPager viewPager;

    public static void setScrollTime(long scrollTime) {
        SCROLL_TIME = scrollTime;
    }

    private static long SCROLL_TIME = 1000;

    public static void setPointSize(int pointSize) {
        POINT_SIZE = pointSize;
    }

    private static int POINT_SIZE = 7;

    public TextView getTitleView() {
        return mTvTitle;
    }

    public ViewGroup getTitleGroup() {
        return mTitleGroup;
    }

    public LinearLayout getPointViewGroup() {
        return mPointContainer;
    }

    //	Enum<Enum<E>>;
    private TextView mTvTitle;


    private LinearLayout mPointContainer;
    private List<? extends IImgInfo> mItems;
    private boolean mAutoScroll;
    private boolean mAutoScrolling;
    // public AutoScrollLayout(Context context, AttributeSet attrs,
    // int defStyleAttr, int defStyleRes) {
    // super(context, attrs, defStyleAttr, defStyleRes);
    // init();
    //
    // }

    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
//		Toast.makeText(context, text, duration)
    }

    public void setNeedPoint(boolean needPoint) {
        if (mPointContainer != null) {
            mPointContainer.setVisibility(needPoint ? View.VISIBLE : GONE);
        }
    }

    public void setNeedTitle(boolean needTitle) {
        if (mTitleGroup != null) {
            mTitleGroup.setVisibility(needTitle ? View.VISIBLE : GONE);

        }
    }


    /**
     * 设置时间
     *
     * @param time
     */
    public void setDuration(long time) {
        SCROLL_TIME = time;
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public Banner(Context context) {
        this(context, null);
    }

    public void init() {
        inflate(getContext(), getLayout(), this);
        mTvTitle = (TextView) findViewById(R.id.banner_title);
        mTitleGroup = (ViewGroup) findViewById(R.id.banner_title_bg);
        mPointContainer = (LinearLayout) findViewById(R.id.banner_point_container);
        viewPager = (ViewPager) findViewById(R.id.banner_viewpager);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(mPagelistener);
        viewPager.setOnTouchListener(mTouchlistener);
        detector = new GestureDetector(getContext(), geSturelistener);
    }

    protected int getLayout() {
        return R.layout.view_auto_scroll_layout;
    }

    public boolean isAutoScroll() {
        return mAutoScroll;
    }

    public boolean isAutoScrolling() {
        return mAutoScrolling;
    }

    public void setAutoScroll(boolean autoScroll) {
        this.mAutoScroll = autoScroll;
     /*   *//**
         * 先清除所有的 然后根据是否需要继续
         *//*
        stopAutoScroll();
//		mHandler.removeCallbacksAndMessages(null);
        if (this.mAutoScroll) {
            startAutoScroll();
        }*/
    }

    /**
     * 开始滚动
     */
    public void startAutoScroll() {
        if (mItems == null) {
            Log.d(TAG, "不能开始startAutoScroll因为item数据为空");
            return;
        }
        mAutoScrolling = true;
        removeCallbacks(mScrollRunnable);
        postDelayed(mScrollRunnable, SCROLL_TIME);
    }


    /**
     * 临时停止滚动 并不会改变状态
     */
    public void stopAutoScroll() {
        Log.d(TAG, "stopAutoScroll");
        removeCallbacks(mScrollRunnable);
        mAutoScrolling = false;
    }

    public Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            boolean isStart = false;
            if (isForward) {
                if (currentItem >= mItems.size() - 1) {
                    isForward = false;
                    viewPager.setCurrentItem(currentItem - 1, true);

                } else {
                    viewPager.setCurrentItem(currentItem + 1, true);
                }

            } else {
                if (currentItem == 0) {
                    viewPager.setCurrentItem(currentItem + 1, true);
                    isForward = true;
                } else {
                    viewPager.setCurrentItem(currentItem - 1, true);
                }
            }
//            currentItem = currentItem == mItems.size() && (isStart = true) ? 0 : currentItem;//如果是是最后一个那么就是开始了，那么就会真


            postDelayed(this, SCROLL_TIME);//递归执行
        }
    };

    private boolean isForward = true;

    public void setItem(List<? extends IImgInfo> listImgInfo) {
        if (listImgInfo == null) {
            Log.d(TAG, "imgInfo为空");
            return;
        }
        this.mItems = listImgInfo;
        mPointContainer.removeAllViews();//防止多次设置产生更多的圆点
        int point_px_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, POINT_SIZE, getResources().getDisplayMetrics());
        for (IImgInfo iImgInfo : listImgInfo) {
            View viewPoint = new View(getContext());
            viewPoint.setBackgroundResource(R.drawable.dot);
            TypedValue value = new TypedValue();
            // value.complexToDimensionPixelOffset(data, metrics)
            // 把像素转换为点
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(point_px_size, point_px_size);
            params.leftMargin = point_px_size;
            viewPoint.setLayoutParams(params);
            mPointContainer.addView(viewPoint);// 添加点
        }
        mAdapter.notifyDataSetChanged();
        mPagelistener.onPageSelected(0);
    }

    public interface IImgInfo {
        public String getBannerTitle();

        public String getImageUrl();
    }


    public int currentP = 0;
    private OnPageChangeListener mPagelistener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // 被选中的是

            mPointContainer.getChildAt(currentP).setEnabled(true);
            mTvTitle.setText(mItems.get(position).getBannerTitle());
            mPointContainer.getChildAt(position).setEnabled(false);
            currentP = position;
            Log.w(TAG, "当前位置:" + position + "," + mItems.get(position).getBannerTitle());
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
    private PagerAdapter mAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {

            return mItems == null ? 0 : mItems.size();
            // return items.size();//刚开始没设置所以是空指针的
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            IImgInfo model = mItems.get(position);
            View view = bindHolderProvider.onCreateView(container, model, position);
            container.addView(view);
            return view;
        }

    };


    OnTouchListener mTouchlistener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            detector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mAutoScroll)//如果在滚动就停止滚动
                    {
                        stopAutoScroll();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mAutoScroll)//如果本来是滚动状态那么你该恢复滚动了
                    {
                        startAutoScroll();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    OnGestureListener geSturelistener = new OnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (onItemListener != null) {
                onItemListener.onClick(currentP);
            }
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {

            return false;
        }
    };
    private GestureDetector detector;
    private OnItemClickListener onItemListener;

    /**
     * 监听点击事件
     *
     * @param onItemListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemClickListener {
        public void onClick(int index);
    }

    public interface OnViewBindHolderProvider<T extends IImgInfo> {
        View onCreateView(ViewGroup group, IImgInfo model, int position);
    }


    public void setBindHolderProvider(OnViewBindHolderProvider<? extends IImgInfo> bindHolderProvider) {
        this.bindHolderProvider = bindHolderProvider;
    }

    OnViewBindHolderProvider<? extends IImgInfo> bindHolderProvider;

}
