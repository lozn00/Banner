package bwn.bwn.autoscrollimglayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bwn.bwn.autoscrollimglayout.transformer.AccordionTransformer;
import bwn.bwn.autoscrollimglayout.transformer.BackgroundToForegroundTransformer;
import bwn.bwn.autoscrollimglayout.transformer.CubeOutTransformer;
import bwn.bwn.autoscrollimglayout.transformer.DepthPageTransformer;
import bwn.bwn.autoscrollimglayout.transformer.ForegroundToBackgroundTransformer;
import bwn.bwn.autoscrollimglayout.transformer.RotateDownTransformer;
import bwn.bwn.autoscrollimglayout.transformer.RotateUpTransformer;
import bwn.bwn.autoscrollimglayout.transformer.ScaleInOutTransformer;
import bwn.bwn.autoscrollimglayout.transformer.StackTransformer;
import bwn.bwn.autoscrollimglayout.transformer.ZoomInTransformer;
import bwn.bwn.autoscrollimglayout.transformer.ZoomOutSlideTransformer;
import bwn.bwn.autoscrollimglayout.transformer.ZoomOutTranformer;
import cn.qssq666.banner.Banner;

public class MainActivity extends Activity {

    private Banner banner;
    private ImageLoader imageLoader;
    private final static String TAG = "BannaerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        banner = (Banner) findViewById(R.id.asl);
        final List<MyImageInfo> listImgInfo = new ArrayList<MyImageInfo>();
        listImgInfo.add(new MyImageInfo("图片1", "http://pic.ffpic.com/files/2012/1109/1106fengyeer07.jpg"));
        listImgInfo.add(new MyImageInfo("美女2", "http://f.hiphotos.baidu.com/image/pic/item/a044ad345982b2b725c274ca33adcbef76099b5b.jpg"));
        listImgInfo.add(new MyImageInfo("美女3", "http://a.hiphotos.baidu.com/image/pic/item/7a899e510fb30f24a23edc1cca95d143ad4b030c.jpg"));
        listImgInfo.add(new MyImageInfo("美女4", "http://g.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828b38f1a605f91f4134960a5a01.jpg"));

        banner.setPointSize(8);//dp
        banner.setPointMargin(20);//dp
        banner.setItem(listImgInfo);
        banner.setAutoScroll(true);
        banner.setScrollTime(1000);
        banner.setBindHolderProvider(new Banner.OnViewBindHolderProvider<MyImageInfo>() {
            @Override
            public View onCreateView(ViewGroup group, MyImageInfo model, int position) {
                ImageView imageView = new ImageView(group.getContext());
                ViewPager.LayoutParams params = new ViewPager.LayoutParams();
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(model.getImageUrl(), imageView);
                return imageView;
            }
        });

        banner.setNeedPoint(true);
        banner.setNeedTitle(true);

        banner.setOnItemClickListener(new Banner.OnItemClickListener() {
            @Override
            public void onClick(int index) {
                MyImageInfo iImgInfo = listImgInfo.get(index);
                Toast.makeText(MainActivity.this, "你点击了" + iImgInfo.getBannerTitle(), Toast.LENGTH_SHORT).show();


            }
        });
        ArrayList<Class<? extends ViewPager.PageTransformer>> anims = new ArrayList<>();
//        anims.add(DefaultTransformer.class);
        anims.add(AccordionTransformer.class);
        anims.add(BackgroundToForegroundTransformer.class);

        anims.add(ForegroundToBackgroundTransformer.class);
//        anims.add(CubeInTransformer.class);
        anims.add(CubeOutTransformer.class);
        anims.add(DepthPageTransformer.class);
//        anims.add(FlipHorizontalTransformer.class);
//        anims.add(FlipVerticalTransformer.class);
        anims.add(RotateDownTransformer.class);
        anims.add(RotateUpTransformer.class);
        anims.add(ScaleInOutTransformer.class);
        anims.add(StackTransformer.class);
//        anims.add(TabletTransformer.class);
        anims.add(ZoomInTransformer.class);
        anims.add(ZoomOutTranformer.class);
        anims.add(ZoomOutSlideTransformer.class);
        Class<? extends ViewPager.PageTransformer> aClass = anims.get(new Random().nextInt(anims.size()));
        ViewPager.PageTransformer transformer = null;
        try {
            transformer = aClass.newInstance();
            banner.getViewPager().setPageTransformer(false, transformer);//荣国动画白屏请删除此代码
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        banner.startAutoScroll();
        Log.w(TAG, aClass.getSimpleName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        banner.startAutoScroll();
    }


    @Override
    protected void onPause() {

        super.onPause();

        banner.stopAutoScroll();
    }

    public class MyImageInfo implements Banner.IImgInfo {
        String title;
        String imgUrl;

        @Override
        public String toString() {
            return "MyImageInfo [title=" + title + ", imgUrl=" + imgUrl + "]";
        }

        public MyImageInfo(String title, String imgUrl) {
            this.title = title;
            this.imgUrl = imgUrl;
        }

        public MyImageInfo() {
        }

        @Override
        public String getBannerTitle() {

            return this.title;
        }

        public String getImageUrl() {

            return this.imgUrl;
        }

    }
}
