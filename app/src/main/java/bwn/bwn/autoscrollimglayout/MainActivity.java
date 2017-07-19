package bwn.bwn.autoscrollimglayout;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Banner scrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollLayout = (Banner) findViewById(R.id.asl);
        List<Banner.IImgInfo> listImgInfo=new ArrayList<Banner.IImgInfo>();
//		List<? extends IImgInfo> listImgInfo=new ArrayList<IImgInfo>();//he method add(capture#1-of ? extends AutoScrollLayout.IImgInfo) in the type List<capture#1-of ? extends AutoScrollLayout.IImgInfo> is not applicable for the arguments (MainActivity.MyImageInfo)
//		IImgInfo imgInfo=new //http://192.168.1.2/imgs/
//        for (int i = 1; i <9; i++) {
//			listImgInfo.add(new MyImageInfo("图片"+i, "http://192.168.1.2/imgs/"+i+".jpg"));
            listImgInfo.add(new MyImageInfo("图片1", "http://pic.ffpic.com/files/2012/1109/1106fengyeer07.jpg"));
            listImgInfo.add(new MyImageInfo("美女2", "http://f.hiphotos.baidu.com/image/pic/item/a044ad345982b2b725c274ca33adcbef76099b5b.jpg"));
            listImgInfo.add(new MyImageInfo("美女3", "http://a.hiphotos.baidu.com/image/pic/item/7a899e510fb30f24a23edc1cca95d143ad4b030c.jpg"));
            listImgInfo.add(new MyImageInfo("美女4", "http://g.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828b38f1a605f91f4134960a5a01.jpg"));
//			listImgInfo.add(new MyImageInfo("图片2", "http://192.168.1.2/imgs/2.jpg"));
//			listImgInfo.add(new MyImageInfo("图片3", "http://192.168.1.2/imgs/3.jpg"));
//			int [] a={};
//			Arrays.asList(array)

//        }

        scrollLayout.setItem(listImgInfo);
        scrollLayout.setAutoScroll(true);
    }
    public class MyImageInfo implements Banner.IImgInfo{
        String title;
        String imgUrl;
        @Override
        public String toString() {
            return "MyImageInfo [title=" + title + ", imgUrl=" + imgUrl + "]";
        }
        public MyImageInfo(String title,String imgUrl) {
            this.title=title;
            this.imgUrl=imgUrl;
        }

        public MyImageInfo() {
        }
        @Override
        public String getTitle() {

            return this.title;
        }

        @Override
        public String getImageUrl() {

            return this.imgUrl;
        }

    }
}
