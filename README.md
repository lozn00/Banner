# AutoScrollImgLayout
## 这是一个轮播图,使用viewpager实现页面的滑动，使用postDelay实现自调循环
可以显示标题,也可以显示总数点,效果图可以下载下来试试  就知道啦!
# 使用方法
Jcenter gradle 目标sdk25.0.3 
```
 compile 'cn.qssq666:banner:0.1'
 
 
    <cn.qssq666.banner.Banner
        android:id="@+id/asl"
        android:layout_width="match_parent"
        android:layout_height="150dp"></cn.qssq666.banner.Banner>
```
 图片演示

![演示图片地址](https://github.com/qssq/banner/blob/master/Pictures/1.gif)

# 常用方法

```
    banner.setPointSize(8);//dp
        banner.setPointMargin(20);//dp
        banner.setItem(listImgInfo);
        banner.setAutoScroll(true);
        banner.setScrollTime(1000);
    banner.startAutoScroll();
   banner.getViewPager().setPageTransformer(false, transformer);
   
```
   
# 本库的优点

优点就是布局diy基于布局文件的，这是别人的banner库最大的痛处，这个痛处导致稍微变通一下就没法解决了，这不是需求的变态，而是库写的不好,文档写的那么花哨，很多点赞，却坑了很多人,这是何必呢？
所以各位多多给我点赞吧!
只需要创建一个一模一样的view_auto_scroll_layout 稍微调整一下就可以了，或者继承该类,因为可以这样，什么需求还做不到？能滚动的各种view都通通可以。
# 后续更新
 2017年7月19日 18:03:30  不需要实现imageUrl，因为banner基于view, 所以 图片加载部分直接又用户构思， 不会用的朋友可以跑demo.MainActivity.this有介绍
 # 吐槽
 受够了别人的banner库，banner多扩展差劲，bugN多 ，也是醉了，
 ok ，本banner非常精简没有那么多垃圾代码，而且无bug,接口形式 非常容易扩展，你要的动画翻页都有。
 完全可以自定义任意指示器 标题栏布局
 支持自己拿Viewpager,方便用户进行更多操作。甚至包括修改轮播逻辑。

# 常见调整
   隐藏指示点 隐藏标题 ，
```
  banner.setNeedPoint(true);
 banner.setNeedTitle(true);
           
```



 # 变态需求与解决方案
### 场景一
让指示器或 标题在外部非viewpager区域显示需求
so easy ，只需要保持id什么一样就行了,所以复制一份R.layout.view_auto_scroll_layout然后把指示器容器布局弄到外面就行，然后继承Banner类复写getLayout 方法，因为指示器是根据findById来的，所以这个需求不在话下。
或者直接直接复制一个到自己的项目中，也行，android studio默认识别宿主项目 的布局文件，切记别把id给删除了。

### 场景二
自由通过Xml调整布局，比如让标题发生一下旋转，倒影，或者描边， 总所周知,通过xml调节的方法更亲民,扩展性更好,因此解决方案就是场景1
 采用接口形式 非常容易扩展
 抽出轮播view里面的布局，也就是说你可以不是图片，那么这个代码自己写就好 
 
	声明周期控制 
		
	
 ```
          setContentView(R.layout.activity_main);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        banner = (Banner) findViewById(R.id.asl);
        final List<MyImageInfo> listImgInfo = new ArrayList<MyImageInfo>();
        listImgInfo.add(new MyImageInfo("图片1", "http://pic.ffpic.com/files/2012/1109/1106fengyeer07.jpg"));
        listImgInfo.add(new MyImageInfo("美女2", "http://f.hiphotos.baidu.com/image/pic/item/a044ad345982b2b725c274ca33adcbef76099b5b.jpg"));
        listImgInfo.add(new MyImageInfo("美女3", "http://a.hiphotos.baidu.com/image/pic/item/7a899e510fb30f24a23edc1cca95d143ad4b030c.jpg"));
        listImgInfo.add(new MyImageInfo("美女4", "http://g.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828b38f1a605f91f4134960a5a01.jpg"));

        banner.setItem(listImgInfo);
        banner.setAutoScroll(true);
        banner.setScrollTime(1000);
        banner.setBindHolderProvider(new Banner.OnViewBindHolderProvider<Banner.IImgInfo>() {
            @Override
            public View onCreateView(ViewGroup group, Banner.IImgInfo model, int position) {
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

```




生命周期控制
   
 ```  
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
```
`



另外当手触摸的时候就不会继续滚动了，松开手就会。

轮播图的方式从0->总数 然后逆向回来，那种无限循环实际上网上的项目都有bug,为了提供一个完全无bug兼容对话框里面的布局等 设计，我就自己写了这个。