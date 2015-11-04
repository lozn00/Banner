package bwn.bwn.autoscrollimglayout;

import android.widget.FrameLayout;



        import java.util.List;

        import com.nostra13.universalimageloader.core.ImageLoader;
        import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

        import android.content.Context;
        import android.os.Handler;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v4.view.ViewPager.OnPageChangeListener;
        import android.util.AttributeSet;
        import android.util.TypedValue;
        import android.view.GestureDetector;
        import android.view.GestureDetector.OnGestureListener;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.webkit.WebSettings.TextSize;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.ImageView.ScaleType;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

public class ShufflingFigure extends FrameLayout {
    /**
     * 在初始化init里面调用的。
     */
    private ViewPager viewPager;
    private static long SCROLL_TIME = 1000;
    private static int POINT_SIZE = 7;
    //	Enum<Enum<E>>;
    private TextView mTvTitle;
    private LinearLayout mView_pointer;
    private List<? extends IImgInfo> mItems;
    private ImageLoader imageLoader;
    private boolean mAutoScroll;
    // public AutoScrollLayout(Context context, AttributeSet attrs,
    // int defStyleAttr, int defStyleRes) {
    // super(context, attrs, defStyleAttr, defStyleRes);
    // init();
    //
    // }

    public ShufflingFigure(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
//		Toast.makeText(context, text, duration)
    }
    /**
     * 设置时间
     * @param time
     */
    public void setDuration(long time){
        SCROLL_TIME=time;
    }
    public ShufflingFigure(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }
    public ShufflingFigure(Context context) {
        this(context, null);
    }
    public void init() {
        Logger.jLog().d("初始化");//
        inflate(getContext(), R.layout.view_auto_scroll_layout, this);
        mTvTitle = (TextView) findViewById(R.id.asl_title);
        mView_pointer = (LinearLayout) findViewById(R.id.asl_ll_pointer);
        viewPager = (ViewPager) findViewById(R.id.asl_viewpager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(mPagelistener);
        viewPager.setOnTouchListener(mTouchlistener);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));

        detector = new GestureDetector(getContext(), geSturelistener);
    }

    public boolean isAutoScroll() {
        return mAutoScroll;
    }
    public void setAutoScroll(boolean autoScroll){
        this.mAutoScroll=autoScroll;
        /**
         * 先清除所有的 然后根据是否需要继续
         */
        stopAutoScroll();
//		mHandler.removeCallbacksAndMessages(null);
        if(this.mAutoScroll)
        {
            startAutoScroll();
        }
    }
    /**
     * 开始滚动
     */
    public void startAutoScroll()
    {
        if(mItems==null)
        {
            Logger.jLog().d("不能开始startAutoScroll因为item数据为空");
            return;
        }
        mHandler.postDelayed(ScrollTask, SCROLL_TIME);
    }
    /**
     * 如果设置的是自动滚动为真将会激活滚动
     */
    public void activeAutoScroll()
    {
        Logger.jLog().d("activeAutoScroll");
        setAutoScroll(this.mAutoScroll);
    }
    /**
     * 临时停止滚动 并不会改变状态
     */
    public void stopAutoScroll()
    {
        Logger.jLog().d("stopAutoScroll");
        mHandler.removeCallbacksAndMessages(null);
    }
    public Runnable ScrollTask =new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem()+1;
            boolean isStart=false;
            currentItem=currentItem==mItems.size()&& (isStart=true)?0 :currentItem;//如果是是最后一个那么就是开始了，那么就会真
            viewPager.setCurrentItem(currentItem,!isStart);//是否平缓的滚动 取反 即可 如果不为最后一个那么还是原来的方式也就是true 平缓的过去
            mHandler.postDelayed(this, SCROLL_TIME);//递归执行
        }
    };
    Handler mHandler=new Handler();
    public void setItem(List<? extends IImgInfo> listImgInfo) {
        if(listImgInfo==null)
        {
            Logger.jLog().d("imgInfo为空");
            return;
        }
        this.mItems = listImgInfo;
        mView_pointer.removeAllViews();//防止多次设置产生更多的圆点
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
            mView_pointer.addView(viewPoint);// 添加点
        }
        mAdapter.notifyDataSetChanged();
        mPagelistener.onPageSelected(0);
    }

    public interface IImgInfo {
        public String getTitle();

        public String getImageUrl();
    }


    public int currentP = 0;
    private OnPageChangeListener mPagelistener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // 被选中的是

            mView_pointer.getChildAt(currentP).setEnabled(true);
            mTvTitle.setText(mItems.get(position).getTitle());
            mView_pointer.getChildAt(position).setEnabled(false);
            currentP = position;
            Logger.jLog().i("当前位置:" + position+","+mItems.get(position).getTitle());
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
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageLoader.displayImage(mItems.get(position).getImageUrl(), imageView);
            /**
             * 犯了低级错误其实就是添加控件到里面去。
             */
            container.addView(imageView);
            return imageView;
            // items.get(position).get;
            // return
            // return super.instantiateItem(container, position);
        }

    };

    OnTouchListener mTouchlistener=new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            detector.onTouchEvent(event);//窃听器
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(mAutoScroll)//如果在滚动就停止滚动
                    {
                        stopAutoScroll();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(mAutoScroll)//如果本来是滚动状态那么你该恢复滚动了
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
    OnGestureListener geSturelistener=new OnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(onItemListener!=null)
            {
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
     * @param onItemListener
     */
    public void setOnItemClickListener( OnItemClickListener onItemListener){
        this.onItemListener=onItemListener;
    }
    public interface OnItemClickListener
    {
        public void onClick(int index);
    }


}
