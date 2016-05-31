# AvatarImageView
an AvatarImageView on Android platform which can display circle text or circle image


#前言：
在做电话本或者其他应用时，显示联系人头像的策略一般是这样的：先判断是否有头像图片，如果有，则直接显示图片；如果没有，则显示联系人的名字的第一个字，将这个文字作为头像，并添加背景颜色。

##截图：
<center>
![本地图片加载与显示](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view3.jpg)
</center>
<center>
![网络图片加载与显示](http://https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view4.jpg)
</center>
<center>
![可添加边框](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view1.jpg)
</center>

##主要功能：
1. 显示圆形图像；
2. 显示圆形文字；
3. 文字大小与圆形半径的比例可以调整；
4. 可以添加圆形边框；
5. 图片、文字的显示始终是居中的；其中图片的居中规则是：如果图片宽大于高，那么截取以高为正方形的中间部分。如果图片高大于宽，那么截取以宽为正方形的中间部分；
6. 由于是继承于ImageView，且在onDraw()时去掉了super.onDraw()，并覆写了设置drawable的函数，因此可以直接将此view赋值给类似Glide等第三方的库，使其直接加载图片，且图片为圆形（不需要做调整）;
7. 支持padding；

##注意事项：
暂时不支持wrap_content模式

##圆形图像显示原理：
使用shader + matrix的方式显示圆形图片，其中圆形是有shader产生的，而居中的调整方式是由matrix设置的。

##使用方法：
(1)设置图片或者文字的方法：
```
AvatarImageView aiv = (AvatarImageView) this.findViewById(R.id.aiv);
//设置图像：
aiv.setImageResource(R.drawable.id_014);
//或者：
aiv.setDrawable(drawable);
//或者：
aiv.setBitmap(bitmap);
//或者：
aiv.setImageDrawable(drawable);
//设置文字：
aiv.setTextAndColor("安", AvatarImageView.COLORS[0]);//直接设置颜色
//或者：
aiv.setTextAndColorSeed("安","安卓");//“安卓”字样作为产生backgroundcolor的seed
```
(2)与Glide的结合：
```
            Glide
                .with(activity)
                .load(picurl)
                .centerCrop()
                .crossFade()
                .into(aiv);
```

