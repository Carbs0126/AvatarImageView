package cn.carbs.android.avatarimageview.example;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * test AvatarImageView in listview
 * @author Carbs.Wang
 */
public class ActivityViewListNet extends AppCompatActivity {

	private ListView list;
	
	private ModelBaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_view_list);
		list = (ListView)this.findViewById(R.id.list);
		
		inflateListView(list);
	}
	
	
	class ModelBaseAdapter extends BaseAdapter{

		private List<Model> Modeles;
		private LayoutInflater inflater;
		
		public ModelBaseAdapter(Context context, List<Model> Modeles){
			this.Modeles = Modeles;
			this.inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return Modeles == null ? 0 : Modeles.size();
		}

		@Override
		public Object getItem(int position) {
			return Modeles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
	        if (null == convertView){
	            viewHolder = new ViewHolder();
	            convertView = inflater.inflate(R.layout.item_hero, parent, false);
	            viewHolder.name = (TextView) convertView.findViewById(R.id.item_name);
	            viewHolder.avatar = (AvatarImageView) convertView.findViewById(R.id.item_avatar);
	            convertView.setTag(viewHolder);
	        }
	        else{
	            viewHolder = (ViewHolder) convertView.getTag();
	        }

	        Model model = (Model)getItem(position);
	        viewHolder.name.setText(model.text);
	        refreshAvatarImageView(viewHolder.avatar, model);
	        
	        return convertView;
		}
		
		private void refreshAvatarImageView(AvatarImageView aiv, Model model){
			if(aiv == null){
				return;
			}
			
			Glide
				.with(ActivityViewListNet.this)
				.load(model.url)
				.centerCrop()
				.crossFade()
				.into(aiv);
		}
	}
	

	static class ViewHolder{
        TextView name;
		AvatarImageView avatar;
    }

	private void inflateListView(ListView listView){
		List<Model> Modeles = new ArrayList<Model>();
		Modeles.add(new Model("百度新闻",	"http://news.baidu.com/resource/img/logo_news_276_88.png"));
		Modeles.add(new Model("腾讯新闻",	"http://mat1.gtimg.com/news/news2013/LOGO.jpg"));
		Modeles.add(new Model("腾讯首页",	"http://mat1.gtimg.com/www/images/qq2012/qqlogo_aoyun.png"));
		Modeles.add(new Model("京东",		"http://misc.360buyimg.com/lib/img/e/logo-201305.png"));
		Modeles.add(new Model("新浪",		"http://i1.sinaimg.cn/dy/deco/2013/0329/logo/LOGO_1x.png"));
		Modeles.add(new Model("图片1",	"http://www.23book.com/upload/2015/07/24/ebfc5dd9-ef65-42c0-888b-5dcfac4b939d.jpg"));
		Modeles.add(new Model("图片2",	"http://img623.ph.126.net/ZoKq9slVrnxHunwX01P6IQ==/1684064785661661487.jpg"));
		Modeles.add(new Model("图片3",	"http://img39.ph.126.net/13iJL2LgqL-Tq3SVUCvh4Q==/3138164515349374239.jpg"));
		Modeles.add(new Model("图片4",	"http://online.sccnn.com/img2/845/dclg_26.jpg"));
		Modeles.add(new Model("图片5",	"http://img165.ph.126.net/_nIc8z87Ed4wfca2rIRYKg==/2257710788197756460.jpg"));
		Modeles.add(new Model("图片6",	"http://www.creativetube.com.cn/upload_files/32959652660_231028068471.jpg"));
		Modeles.add(new Model("图片7",	"http://img3.imgtn.bdimg.com/it/u=2907192921,377209920&fm=21&gp=0.jpg"));
		Modeles.add(new Model("图片8",	"http://f.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=d012799522a446237e9fad66ad125e38/4afbfbedab64034fc5c0c1e9adc379310b551dcb.jpg"));
		Modeles.add(new Model("图片9",	"http://www.jshaxx.com/UploadFiles/bjzy/2013/5/201305140951540809.jpg"));
		Modeles.add(new Model("图片10",	"http://img.sucai.redocn.com/attachments/images/201203/20120329/Redocn_2012032905302438.jpg"));
		Modeles.add(new Model("图片11",	"http://www.tom61.com/d/file/xiaoxuekejian/renjiaobanxiaoxueshuxueliunianjishangcekejian/2012-03-01/f401ded1a7c55a91decfaa7819c1f6b1.jpg"));
		Modeles.add(new Model("图片12",	"http://picm.photophoto.cn/049/029/008/0290080024.jpg"));
		Modeles.add(new Model("图片13",	"http://img3.redocn.com/20120706/Redocn_2012070600470552.jpg"));
		Modeles.add(new Model("图片14",	"http://www.zjsyxx.com/pic/201211/19/2012111985123503.jpg"));
		Modeles.add(new Model("图片15",	"http://pic.baike.soso.com/p/20140221/20140221012100-1587730782.jpg"));
		Modeles.add(new Model("图片16",	"http://www.zjsyxx.com/pic/201211/19/2012111985151600.jpg"));
		Modeles.add(new Model("图片17",	"http://img3.redocn.com/tupian/20151027/duichengtuxingsheji_5132788.jpg"));
		Modeles.add(new Model("图片18",	"http://pic.baike.soso.com/p/20140220/20140220163420-374569575.jpg"));
		Modeles.add(new Model("图片19",	"http://imgsrc.baidu.com/baike/pic/item/faedab64034f78f0e8d87ada7b310a55b2191cc3.jpg"));
		Modeles.add(new Model("图片20",	"http://hiphotos.baidu.com/%D6%F1%C6%B7%C8%CB%C9%FAhhh/pic/item/fa8e559acfb83babc8eaf43a.jpg"));
		Modeles.add(new Model("图片21",	"http://g.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=a30e7b96d1c8a786be7f420a5239e50b/0df431adcbef7609cd2742f32edda3cc7cd99eaa.jpg"));

		adapter = new ModelBaseAdapter(this, Modeles);
		list.setAdapter(adapter);
	}

	static class Model{
		public String text;
		public String url;
		public Model(String text, String url){
			this.text = text;
			this.url = url;
		}
		@Override
		public String toString() {
			return text + "\t" + url;
		}
	}
}
