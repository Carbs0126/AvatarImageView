package cn.carbs.android.avatarimageview.example;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * test AvatarImageView in listview
 * @author Carbs.Wang
 */
public class ActivityViewListLocal extends AppCompatActivity {

	private ListView list;

	private HeroBaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_view_list);
		list = (ListView)this.findViewById(R.id.list);
		init();
	}

	class HeroBaseAdapter extends BaseAdapter{

		private List<Hero> heroes;
		private LayoutInflater inflater;

		public HeroBaseAdapter(Context context, List<Hero> heroes){
			this.heroes = heroes;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return heroes == null ? 0 : heroes.size();
		}

		@Override
		public Object getItem(int position) {
			return heroes.get(position);
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

			Hero hero = (Hero)getItem(position);
			viewHolder.name.setText(hero.toString());
			refreshAvatarImageView(viewHolder.avatar, hero);

			return convertView;
		}

		private void refreshAvatarImageView(AvatarImageView aiv, Hero hero){
			if(aiv == null){
				return;
			}
			AvatarImageView.updateAvatarView(aiv, hero, null, null, hero.file, hero.realName);
		}
	}

	static class ViewHolder{
		TextView name;
		AvatarImageView avatar;
	}

	private void init(){
		final String fileDir = getFilesDir().toString() + File.separator;
		Observable
				.create(new Observable.OnSubscribe<ArrayList<Hero>>() {
					@Override
					public void call(Subscriber<? super ArrayList<Hero>> subscriber) {
						copyAssets(fileDir);
						subscriber.onNext(inflateData(fileDir));
						subscriber.onCompleted();
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<ArrayList<Hero>>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
					}

					@Override
					public void onNext(ArrayList<Hero> heroes) {
						adapter = new HeroBaseAdapter(ActivityViewListLocal.this, heroes);
						list.setAdapter(adapter);
					}
				});
	}

	private void copyAssets(String fileDir){
		try {
			String[] picAssets = getAssets().list("");
			copyDataToSD(fileDir, picAssets);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void copyDataToSD(String fileDir, String[] assetfileNames) throws IOException {

		OutputStream output = null;
		InputStream input = null;
		File file = null;
		byte[] buffer = new byte[1024];
		AssetManager assetManager = getAssets();

		for(String assetfileName : assetfileNames) {

			if(!assetfileName.startsWith("id_")) continue;

			file = new File(fileDir + assetfileName);
			if(file.exists()) continue;

			output = new FileOutputStream(file);
			input = assetManager.open(assetfileName);

			int length = input.read(buffer);
			while (length > 0) {
				output.write(buffer, 0, length);
				length = input.read(buffer);
			}

			output.flush();
			input.close();
			output.close();
		}
	}

	private ArrayList<Hero> inflateData(String fileDir){

		ArrayList<Hero> heroes = new ArrayList<Hero>();
		String id_prefix = "id_";
		int id_int = 1;
		heroes.add(new Hero("呼保义",	"宋江", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("玉麒麟",	"卢俊义", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("智多星",	"吴用", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("入云龙",	"公孙胜", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("大刀",		"关胜", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("豹子头",	"林冲", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("霹雳火",	"秦明", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("双鞭将",	"呼延灼", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("小李广",	"花荣",  	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("小旋风",	"柴进", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("扑天雕",	"李应", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("美髯公",	"朱仝", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("花和尚",	"鲁智深", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("行者",		"武松", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("双抢将",	"董平", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("没羽箭",	"张清", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("青面兽",	"杨志", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("金抢手",	"徐宁", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("急先锋",	"索超", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("神行太保",	"戴宗", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("赤发鬼",	"刘唐", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("黑旋风",	"李逵", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("九纹龙",	"史进", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("没遮拦",	"穆弘", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("插翅虎",	"雷横", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("混江龙",	"李俊", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("立地太岁",	"阮小二",	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("船火儿",	"张横", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("短命二郎",	"阮小五",	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("浪里白条",	"张顺", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("活阎罗",	"阮小七", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("病关索",	"扬雄", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("拼命三郎",	"石秀", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("两头蛇",	"解珍", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("双尾蝎",	"解宝", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("浪子",		"燕青", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("神机军师",	"朱武", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("镇三山",	"黄信", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("病尉迟",	"孙立", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("丑郡马",	"宣赞", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("井木犴",	"郝思文", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("百胜将",	"韩滔", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("天目将",	"彭玘", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("圣水将",	"单廷圭", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("神火将",	"魏定国", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("圣手书生",	"萧让", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("铁面孔目",	"裴宣", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("摩云金翅",	"欧鹏", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("火眼狻猊",	"邓飞", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("锦毛虎",	"燕顺", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("锦豹子",	"杨林", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("轰天雷",	"凌振", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("神算子",	"蒋敬", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("小温侯",	"吕方", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("赛仁贵",	"郭盛", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("神医",		"安道全", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("紫髯伯",	"皇甫端", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("矮脚虎",	"王英", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("一丈青",	"扈三娘", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero(" 丧门神",	"鲍旭", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("混世魔王",	"樊瑞", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("毛头星",	"孔明", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("独火星",	"孔亮", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("八臂哪吒",	"项充", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("飞天大圣",	"李衮", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("玉臂匠",	"金大坚", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("铁笛仙",	"马麟", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("出洞蛟",	"童威", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("翻江蜃",	"童猛", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("玉幡竿",	"孟康", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("通臂猿",	"侯健", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("跳涧虎",	"陈达", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("白花蛇",	"杨春", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("白面郎君",	"郑天寿",	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("九尾龟",	"陶宗旺", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("铁扇子",	"宋清", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("铁叫子",	"乐和", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("花项虎",	"龚旺", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("中箭虎",	"丁得孙", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("小遮拦",	"穆春", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("操刀鬼",	"曹正", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("云里金刚",	"宋万", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("摸着天",	"杜迁", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("病大虫",	"薛永", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("金眼彪",	"施恩", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("打虎将",	"李忠", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("小霸王",	"周通", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("金钱豹子",	"汤隆", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("鬼脸儿",	"杜兴", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("出林龙",	"邹渊", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("独角龙",	"邹润", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("旱地忽律",	"朱贵 ", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("笑面虎",	"朱富", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("铁臂膊",	"蔡福", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("一枝花",	"蔡庆", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("催命判官",	"李立", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("青眼虎",	"李云", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("没面目",	"焦挺", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("石将军",	"石勇", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("小尉迟",	"孙新", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("母大虫",	"顾大嫂", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("菜园子",	"张青", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("母夜叉",	"孙二娘", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("活闪婆",	"王定六", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("险道神",	"郁保四", 	getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("白日鼠",	"白胜", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("鼓上蚤",	"时迁", 		getFile(fileDir, id_prefix, id_int++)));
		heroes.add(new Hero("金毛犬",	"段景住", 	getFile(fileDir, id_prefix, id_int++)));
		return heroes;
	}

	private String getFile(String fileDir, String id_prefix, int id){
		return fileDir + id_prefix + String.format("%03d", id) + ".jpg";
	}

	static class Hero{
		public String nickName;
		public String realName;
		public String file;
		public Hero(String nickName, String realName, String file){
			this.nickName = nickName;
			this.realName = realName;
			this.file = file;
		}
		@Override
		public String toString() {
			return nickName + "\t" + realName;
		}
	}

}
