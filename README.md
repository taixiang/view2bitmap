>文章链接：[https://mp.weixin.qq.com/s/FQmYfT-KYiDbp-0HzK_Hpw](https://mp.weixin.qq.com/s/FQmYfT-KYiDbp-0HzK_Hpw)

项目中经常会用到分享的功能，有分享链接也有分享图片，其中分享图片有的需要移动端对屏幕内容进行截取分享，说白了就是将view 转成bitmap 再到图片分享，还有一种情况是将不可见的view 转成bitmap ，这种view是没有直接显示在界面上的，需要我们使用inflate 进行创建的view。   
#### 第一种
先看通过 DrawingCache 方法来截取普通的view，获取它的视图（Bitmap）。  
```
private Bitmap createBitmap(View view) {
    view.buildDrawingCache();
    Bitmap bitmap = view.getDrawingCache();
    return bitmap;
}
```
这个方法适用于view 已经显示在界面上了，可以获得view 的宽高实际大小，进而通过DrawingCache 保存为bitmap。  
#### 第二种
**但是** 如果要截取的view 没有在屏幕上显示完全的，例如要截取的是超过一屏的 scrollview ，通过上面这个方法是获取不到bitmap的，需要使用下面方法，传的view 是scrollview 的子view（LinearLayout）等， 当然完全显示的view（第一种情况的view） 也可以使用这个方法截取。
```
public Bitmap createBitmap2(View v) {
    Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bmp);
    c.drawColor(Color.WHITE);
    v.draw(c);
    return bmp;
}
```
#### 第三种
还有一种 是view完全没有显示在界面上，通过inflate 转化的view，这时候通过 DrawingCache 是获取不到bitmap 的，也拿不到view 的宽高，以上两种方法都是不可行的。第三种方法通过measure、layout 去获得view 的实际尺寸。
```
public Bitmap createBitmap3(View v, int width, int height) {
    //测量使得view指定大小
    int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
    int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
    v.measure(measuredWidth, measuredHeight);
    //调用layout方法布局后，可以得到view的尺寸大小
    v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bmp);
    c.drawColor(Color.WHITE);
    v.draw(c);
    return bmp;
}


View view = LayoutInflater.from(this).inflate(R.layout.view_inflate, null, false);
//这里传值屏幕宽高，得到的视图即全屏大小
createBitmap3(view, getScreenWidth(), getScreenHeight());

```

另外写了个简易的保存图片的方法，方便查看效果的。
```
private void saveBitmap(Bitmap bitmap) {
    FileOutputStream fos;
    try {
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root, "test.png");
        fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
        fos.flush();
        fos.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

github地址：[https://github.com/taixiang/view2bitmap](https://github.com/taixiang/view2bitmap)

欢迎关注我的博客：[https://www.manjiexiang.cn/](https://www.manjiexiang.cn/)  

更多精彩欢迎关注微信号：春风十里不如认识你  
一起学习 一起进步

![](https://user-gold-cdn.xitu.io/2018/8/12/1652cd77eaebeb98?w=900&h=540&f=jpeg&s=64949)    
