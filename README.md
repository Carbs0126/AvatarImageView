# AvatarImageView
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AvatarImageView-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3820)
* [English](#english)

#前言：
在做电话本或者其他应用时，显示联系人头像的策略一般是这样的：先判断是否有头像图片，如果有，则直接显示图片；如果没有，则显示联系人的名字的第一个字，将这个文字作为头像，并添加背景颜色。

##截图：
<center>
![本地图片加载与显示](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view3.jpg)
</center> 
<center>
![网络图片加载与显示](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view4.jpg)
</center> 
<center>
![可添加边框](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view1.jpg)
</center> 
<center>
![可添加边框](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view2.jpg)
</center> 

##添加至工程
```
compile 'cn.carbs.android:AvatarImageView:1.0.1'
```

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
##添加至工程
1.添加依赖
```
compile 'cn.carbs.android:AvatarImageView:1.0.1'
```
2.layout文件中添加此view
```
    <cn.carbs.android.avatarimageview.library.AvatarImageView
        android:id="@+id/item_avatar"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:padding="5dp" />
```

##感谢
####[CircleImageView](https://github.com/hdodenhof/CircleImageView) 
---------------------
#English

##Abstract
AvatarImageView on Android platform can display circle text or circle image

##Screenshot
<center>
![load local image resources](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view3.jpg)
</center>
<center>
![load net image resources](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view4.jpg)
</center>
<center>
![add boarder](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view1.jpg)
</center>
<center>
![add boarder](https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view2.jpg)
</center>

##Main features:
1. show circle image(including bitmap and drawable);
2. show circle text(can generate a colorful background for text);
3. the textsize can be modified according to the ratio of AvatarImageView's width;
4. can add a circle boarder for text or image, and the boarder can be adjusted in stroke width or color;
5. the image or text is aways in the center; the rule is : if the image's width is larger than height, then get the center square of the image, with the same length side of image's width. vice versa.
6. since AvatarImageView extends from ImageView, and remove super.onDraw(), and override setImageResoure() setImageDrawable(), then we can use Glide or other famouse Loading Image Libary to load image into AvatarImageView conveniently.
7. surpport padding;

##Attention
not support wrap_content pattern temporarily

##How to use it:
(1)How to set image or text
```
AvatarImageView aiv = (AvatarImageView) this.findViewById(R.id.aiv);
//set image：
aiv.setImageResource(R.drawable.id_014);
//or：
aiv.setDrawable(drawable);
//or：
aiv.setBitmap(bitmap);
//or：
aiv.setImageDrawable(drawable);
//set text:
aiv.setTextAndColor("Android", AvatarImageView.COLORS[0]);//set backgroundcolor directly
//or
aiv.setTextAndColorSeed("Android","Android platform");//"Android platform" as the backgroundcolor seed
```
(2)use with Glide:
```
            Glide
                .with(activity)
                .load(picurl)
                .centerCrop()
                .crossFade()
                .into(aiv);
```

##add to your project
1.add dependence
```
compile 'cn.carbs.android:AvatarImageView:1.0.1'
```
2.add into xml layout file
```
    <cn.carbs.android.avatarimageview.library.AvatarImageView
        android:id="@+id/item_avatar"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:padding="5dp" />
```

##Thanks
####[CircleImageView](https://github.com/hdodenhof/CircleImageView)

## License

    Copyright 2016 Carbs.Wang (AvatarImageView)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

