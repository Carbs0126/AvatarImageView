# AvatarImageView
![Android Arsenal][1]
* [English](#english)

#前言：
在做电话本或者其他应用时，显示联系人头像的策略一般是这样的：先判断是否有头像图片，如果有，则直接显示图片；如果没有，则显示联系人的名字的第一个字，将这个文字作为头像，并添加背景颜色。如果设置的文字的长度大于1，则具有clip效果，即超出圆形的部分将被clip掉。

##截图：
<center>
![1.0.2添加clip文字效果][2]
</center>
<center>
![本地图片加载与显示][3]
</center> 

##添加至工程
```groovy
compile 'cn.carbs.android:AvatarImageView:1.0.2'
```

##主要功能：
1. 显示圆形图像；
2. 显示圆形文字；
3. 文字大小与圆形半径的比例可以调整；
4. 可以添加圆形边框；
5. 图片、文字的显示始终是居中的；其中图片的居中规则是：如果图片宽大于高，那么截取以高为正方形的中间部分。如果图片高大于宽，那么截取以宽为正方形的中间部分；
6. 由于是继承于ImageView，且在onDraw()时去掉了super.onDraw()，并覆写了设置drawable的函数，因此可以直接将此view赋值给类似Glide等第三方的库，使其直接加载图片，且图片为圆形（不需要做调整）;
7. 支持padding；
8. 多个文字显示具有clip效果
9. 可以自定义clip放大比例，使用maskratio来调节文字放大的比例

##注意事项：
暂时不支持wrap_content模式

##圆形图像显示原理：
使用shader + matrix的方式显示圆形图片，其中圆形是有shader产生的，而居中的调整方式是由matrix设置的。

##使用方法：
(1)设置图片或者文字的方法：
```java
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
aiv.setTextAndColor("安", AvatarImageView.COLORS[0]);//直接设置颜色，如果设置的文字为多个字符，则会具有clip效果，单个字符没有clip效果
//或者：
aiv.setTextAndColorSeed("安","安卓");//“安卓”字样作为产生backgroundcolor的seed
```
(2)与Glide的结合：
```java
Glide
    .with(activity)
    .load(picurl)
    .centerCrop()
    .crossFade()
    .into(aiv);
```
##添加至工程
1.添加依赖
```groovy
compile 'cn.carbs.android:AvatarImageView:1.0.2'
```
2.layout文件中添加此view
```xml
<cn.carbs.android.avatarimageview.library.AvatarImageView
            android:id="@+id/item_avatar"
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:padding="5dp"
            app:aiv_BoarderWidth="2dp"//边框宽度
            app:aiv_ShowBoarder="false"//是否显示边框
            app:aiv_TextSizeRatio="0.4"/>//文字大小与原型直径大小的比例
```

##感谢
####[CircleImageView][4]
---------------------
#English

##Abstract
AvatarImageView on Android platform can display circle text or circle image

##Screenshot
<center>
![load local image resources][2]
</center>
<center>
![load local image resources][3]
</center>

##Main features:
1. show circle image(including bitmap and drawable);
2. show circle text(can generate a colorful background for text);
3. the textsize can be modified according to the ratio of AvatarImageView's width;
4. can add a circle boarder for text or image, and the boarder can be adjusted in stroke width or color;
5. the image or text is aways in the center; the rule is : if the image's width is larger than height, then get the center square of the image, with the same length side of image's width. vice versa.
6. since AvatarImageView extends from ImageView, and remove super.onDraw(), and override setImageResoure() setImageDrawable(), then we can use Glide or other famouse Loading Image Libary to load image into AvatarImageView conveniently.
7. surpport padding;
8. have clip effect if the text's length > 1
9. can set the clip effect's amplify ratio by setting mask ratio

##Attention
not support wrap_content pattern temporarily

##How to use it:
(1)How to set image or text
```java
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
aiv.setTextAndColor("Android", AvatarImageView.COLORS[0]);//set backgroundcolor directly, if the text's length > 1, will have clip effect
//or
aiv.setTextAndColorSeed("Android","Android platform");//"Android platform" as the backgroundcolor seed
```
(2)use with Glide:
```java
Glide
    .with(activity)
    .load(picurl)
    .centerCrop()
    .crossFade()
    .into(aiv);
```

##add to your project
1.add dependence
```groovy
compile 'cn.carbs.android:AvatarImageView:1.0.2'
```
2.add into xml layout file
```xml
<cn.carbs.android.avatarimageview.library.AvatarImageView
            android:id="@+id/item_avatar"
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:padding="5dp"
            app:aiv_BoarderWidth="2dp"//
            app:aiv_ShowBoarder="false"//
            app:aiv_TextSizeRatio="0.4"/>//equals textsize divides circle diameter
```

##Thanks
####[CircleImageView][4]

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



[1]: https://img.shields.io/badge/Android%20Arsenal-AvatarImageView-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3820
[2]: https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view_3_in_1.jpg
[3]: https://github.com/Carbs0126/Screenshot/blob/master/avatar_image_view_2_in_1.jpg
[4]: https://github.com/hdodenhof/CircleImageView

