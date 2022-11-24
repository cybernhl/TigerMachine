package com.example.tigermachine;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.widget.OnWheelChangedListener;
import com.example.widget.OnWheelScrollListener;
import com.example.widget.WheelView;
import com.example.widget.adapters.AbstractWheelAdapter;

public class MainActivity extends Activity {
	private TextView resultTipstv;

	private String TAG = "NUM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initWheel(R.id.slot_1);
		initWheel(R.id.slot_2);
		initWheel(R.id.slot_3);
		resultTipstv = (TextView) findViewById(R.id.pwd_status);
		Button mix = (Button) findViewById(R.id.btn_mix);
		mix.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mixWheel(R.id.slot_1);
				mixWheel2(R.id.slot_2);
				mixWheel3(R.id.slot_3);
			}
		});
	}

	// 车轮滚动标志
	private boolean wheelScrolled = false;

	// 车轮滚动的监听器
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			System.out.println("轮子---->" + wheel.getCurrentItem());
			updateStatus();
		}
	};

	// 车轮item改变的监听器
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
				System.out.println("轮子item---->" + wheel.getCurrentItem());
				// updateStatus();

			}
		}
	};

	/**
	 * 更新状态
	 */
	private void updateStatus() {
		resultTipstv = (TextView) findViewById(R.id.pwd_status);

		int value = getWheel(R.id.slot_1).getCurrentItem();
		int value1 = getWheel(R.id.slot_2).getCurrentItem();
		int value2 = getWheel(R.id.slot_3).getCurrentItem();
		resultTipstv.setText("运气不错哦！" + (value % 10) + ",,," + (value1 % 10) + ",,," + (value2 % 10));

	}

	/**
	 * 初始化轮子
	 * 
	 * @param id
	 *            the wheel widget Id
	 */
	private void initWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.setViewAdapter(new SlotMachineAdapter(this));
		// wheel.setCurrentItem((int)(Math.random() * 10));
		wheel.setCurrentItem(0);
		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
	}

	/**
	 * 根据id获取轮子
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * 测试轮子转动位置
	 * 
	 * @return true
	 */
	private boolean test() {
		int value = getWheel(R.id.slot_1).getCurrentItem();
		return testWheelValue(R.id.slot_2, value) && testWheelValue(R.id.slot_3, value);
	}

	/**
	 * 根据轮子id获取当前item值
	 * 
	 * @param id
	 *            the wheel Id
	 * @param value
	 *            the value to test
	 * @return true if wheel value is equal to passed value
	 */
	private boolean testWheelValue(int id, int value) {
		return getWheel(id).getCurrentItem() == value;
	}

	/**
	 * 转动轮子
	 * 
	 * @param id
	 *            the wheel id
	 */
	private void mixWheel(int id) {
		WheelView wheel = getWheel(id);
		// wheel.scroll(round, time);
		// wheel.scroll((int)(Math.random() * 50)+round, time);
		int num1 = -350 + (int) (Math.random() * 50);
		Log.i(TAG, "num1===>>" + num1);
		wheel.scroll(num1, 2000);
	}

	private void mixWheel2(int id) {
		WheelView wheel = getWheel(id);
		// wheel.scroll(round, time);
		// wheel.scroll((int)(Math.random() * 50)+round, time);
		int num2 = -350 + (int) (Math.random() * 50);
		Log.i(TAG, "num2===>>" + num2);
		wheel.scroll(num2, 2000);
	}

	private void mixWheel3(int id) {
		WheelView wheel = getWheel(id);
		// wheel.scroll(round, time);
		// wheel.scroll((int)(Math.random() * 50)+round, time);
		int num3 = -350 + (int) (Math.random() * 50);
		Log.i(TAG, "num3===>>" + num3);
		wheel.scroll(num3, 2000);
	}

	/**
	 * 老虎机适配器
	 */
	private class SlotMachineAdapter extends AbstractWheelAdapter {
		// 图片的大小
		final int IMAGE_WIDTH = 60;
		final int IMAGE_HEIGHT = 60;

		// 图片的数组
		private final int items[] = new int[] { R.drawable.num0, R.drawable.num1, R.drawable.num2, R.drawable.num3,
				R.drawable.num4, R.drawable.num5, R.drawable.num6, R.drawable.num7, R.drawable.num8, R.drawable.num9,
				R.drawable.num0, R.drawable.num1, R.drawable.num2, R.drawable.num3, R.drawable.num4, R.drawable.num5,
				R.drawable.num6, R.drawable.num7, R.drawable.num8, R.drawable.num9, };

		// 对图片的缓存
		private List<SoftReference<Bitmap>> images;

		// 布局膨胀器
		private Context context;

		/**
		 * 构造函数
		 */
		public SlotMachineAdapter(Context context) {
			this.context = context;
			images = new ArrayList<SoftReference<Bitmap>>(items.length);
			for (int id : items) {
				images.add(new SoftReference<Bitmap>(loadImage(id)));
			}
		}

		/**
		 * 从资源加载图片
		 */
		private Bitmap loadImage(int id) {
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
			Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
			bitmap.recycle();
			return scaled;
		}

		@Override
		public int getItemsCount() {
			return items.length;
		}

		// 设置图片布局的参数
		final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			ImageView img;
			if (cachedView != null) {
				img = (ImageView) cachedView;
			} else {
				img = new ImageView(context);
			}
			img.setLayoutParams(params);
			// img.setScaleType(ScaleType.FIT_XY);
			SoftReference<Bitmap> bitmapRef = images.get(index);
			Bitmap bitmap = bitmapRef.get();
			if (bitmap == null) {
				bitmap = loadImage(items[index]);
				images.set(index, new SoftReference<Bitmap>(bitmap));
			}
			img.setImageBitmap(bitmap);

			return img;
		}
	}

}
