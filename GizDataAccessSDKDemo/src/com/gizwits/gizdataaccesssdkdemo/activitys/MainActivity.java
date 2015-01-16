/**
 * Project Name:GizDataAccessSDKDemo
 * File Name:MainActivity.java
 * Package Name:com.gizwits.gizdataaccesssdkdemo
 * Date:2015-1-12 15:30:22
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gizwits.gizdataaccesssdkdemo.activitys;

import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gizwits.gizdataaccess.GizDataAccess;
import com.gizwits.gizdataaccess.GizDataAccessLogin;
import com.gizwits.gizdataaccess.GizDataAccessSource;
import com.gizwits.gizdataaccess.entity.GizDataAccessErrorCode;
import com.gizwits.gizdataaccess.listener.GizDataAccessLoginListener;
import com.gizwits.gizdataaccess.listener.GizDataAccessSourceListener;
import com.gizwits.gizdataaccesssdkdemo.Constant;
import com.gizwits.gizdataaccesssdkdemo.R;
import com.gizwits.gizdataaccesssdkdemo.R.id;
import com.gizwits.gizdataaccesssdkdemo.R.layout;
import com.gizwits.gizdataaccesssdkdemo.utils.DateUtils;
import com.gizwits.gizdataaccesssdkdemo.utils.NetworkUtils;

// TODO: Auto-generated Javadoc
/**
 * 
 * ClassName: Class MainActivity. <br/>
 * <br/>
 * date: 2015-1-12 15:30:22 <br/>
 * 
 * @author Lien
 */
public class MainActivity extends Activity implements OnClickListener,
		GizDataAccessSourceListener {

	/** The tv version. */
	TextView tvVersion;

	/** The tv terminal. */
	TextView tvTerminal;

	/** The btn save. */
	Button btnSave;

	/** The btn load. */
	Button btnLoad;

	/** The btn clean. */
	Button btnClean;

	/** The btn start load. */
	Button btnStartLoad;

	/** The buffer. */
	StringBuffer buffer;

	/** The dialog. */
	Dialog dialog;

	/** The et sec start. */
	EditText etYearStart, etMonStart, etDayStart, etHourStart, etMinStart,
			etSecStart;

	/** The et sec end. */
	EditText etYearEnd, etMonEnd, etDayEnd, etHourEnd, etMinEnd, etSecEnd;

	/** The et skip. */
	EditText etLimit, etSkip;

	/** The load start time. */
	long loadStartTime;

	/** The load end time. */
	long loadEndTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		tvTerminal = (TextView) findViewById(R.id.tvTerminal);
		btnLoad = (Button) findViewById(R.id.btnLoad);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnClean = (Button) findViewById(R.id.btnClean);
		tvVersion = (TextView) findViewById(R.id.tvVersion);
		buffer = new StringBuffer();
		tvTerminal.setText(buffer.toString());
		btnLoad.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnClean.setOnClickListener(this);
		dialog = dateTimePicKDialog();
		tvVersion.setText("SDK版本号:" + GizDataAccess.getVersion());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLoad:

			if (NetworkUtils.isNetworkConnected(MainActivity.this)) {
				dialog.show();
			} else {
				Toast.makeText(MainActivity.this, "网络已断开", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.btnSave:
			Intent intent = new Intent(MainActivity.this, SaveActivity.class);
			startActivity(intent);
			break;
		case R.id.btnClean:
			buffer.setLength(0);
			tvTerminal.setText(buffer.toString());
			break;
		}

	}

	/**
	 * Load data.
	 */
	private void loadData() {
		String strYearStart = etYearStart.getText().toString();
		String strMonStart = etMonStart.getText().toString();
		String strDayStart = etDayStart.getText().toString();
		String strHourStart = etHourStart.getText().toString();
		String strMinStart = etMinStart.getText().toString();
		String strSecStart = etSecStart.getText().toString();
		String strYearEnd = etYearEnd.getText().toString();
		String strMonEnd = etMonEnd.getText().toString();
		String strDayEnd = etDayEnd.getText().toString();
		String strHourEnd = etHourEnd.getText().toString();
		String strMinEnd = etMinEnd.getText().toString();
		String strSecEnd = etSecEnd.getText().toString();
		int limit = Integer
				.valueOf(isEmpty(etLimit.getText().toString()) ? "20" : etLimit
						.getText().toString());
		int skip = Integer.valueOf(isEmpty(etSkip.getText().toString()) ? "0"
				: etSkip.getText().toString());

		if (isEmpty(strYearStart) || isEmpty(strMonStart)
				|| isEmpty(strDayStart) || isEmpty(strHourStart)
				|| isEmpty(strMinStart) || isEmpty(strSecStart)) {
			Toast.makeText(MainActivity.this, "请输入正确的开始时间", Toast.LENGTH_SHORT)
					.show();
		} else if (isEmpty(strYearEnd) || isEmpty(strMonEnd)
				|| isEmpty(strDayEnd) || isEmpty(strHourEnd)
				|| isEmpty(strMinEnd) || isEmpty(strSecEnd)) {
			Toast.makeText(MainActivity.this, "请输入正确的结束时间", Toast.LENGTH_SHORT)
					.show();
		} else {
			loadStartTime = DateUtils.getStringToDate(strYearStart
					+ strMonStart + strDayStart + strHourStart + strMinStart
					+ strSecStart + "0000");
			loadEndTime = DateUtils.getStringToDate(strYearEnd + strMonEnd
					+ strDayEnd + strHourEnd + strMinEnd + strSecEnd + "0000");
			Log.i("loadData", "timeStartlong=" + loadStartTime);
			Log.i("loadData",
					"timeStartstr=" + DateUtils.getDateToString(loadStartTime));
			Log.i("loadData", "timeEndlong=" + loadEndTime);
			Log.i("loadData",
					"timeEndstr=" + DateUtils.getDateToString(loadEndTime));
			new GizDataAccessSource(this).loadData(Constant.TOKEN,
					Constant.PRODUCTKEY, Constant.DEVICE_SN, loadStartTime,
					loadEndTime, limit, skip);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gizwits.gizdataaccess.listener.GizDataAccessSourceListener#didLoadData
	 * (com.gizwits.gizdataaccess.GizDataAccessSource, org.json.JSONArray,
	 * com.gizwits.gizdataaccess.entity.GizDataAccessErrorCode,
	 * java.lang.String)
	 */
	@Override
	public void didLoadData(GizDataAccessSource arg0, JSONArray jsonArray,
			GizDataAccessErrorCode result, String message) {
		if (result.getResult() == 0) {
			if (jsonArray != null) {
				buffer.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
				for (int i = 0; i < jsonArray.length(); i++) {
					try {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						buffer.append("attrs:"
								+ jsonObject.get("attrs").toString() + "\n");
						buffer.append("ts:"
								+ DateUtils.getDateToString(jsonObject
										.getLong("ts")) + "\n");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else {
				buffer.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
				buffer.append("暂无数据");

			}
		} else {
			buffer.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
			buffer.append("读取失败：" + message);
		}
		tvTerminal.setText(buffer.toString());
	}

	/**
	 * 弹出日期时间选择框方法.
	 * 
	 * @return the dialog
	 */
	public Dialog dateTimePicKDialog() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = this.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		LinearLayout dateTimeLayout = (LinearLayout) this.getLayoutInflater()
				.inflate(R.layout.dialog_time, null);

		etLimit = (EditText) dateTimeLayout.findViewById(R.id.etLimit);
		etSkip = (EditText) dateTimeLayout.findViewById(R.id.etSkip);
		etYearStart = (EditText) dateTimeLayout.findViewById(R.id.etYearStart);
		etYearEnd = (EditText) dateTimeLayout.findViewById(R.id.etYearEnd);
		etMonStart = (EditText) dateTimeLayout.findViewById(R.id.etMonStart);
		etMonEnd = (EditText) dateTimeLayout.findViewById(R.id.etMonEnd);
		etDayStart = (EditText) dateTimeLayout.findViewById(R.id.etDayStart);
		etDayEnd = (EditText) dateTimeLayout.findViewById(R.id.etDayEnd);
		etHourStart = (EditText) dateTimeLayout.findViewById(R.id.etHourStart);
		etHourEnd = (EditText) dateTimeLayout.findViewById(R.id.etHourEnd);
		etMinStart = (EditText) dateTimeLayout.findViewById(R.id.etMinStart);
		etMinEnd = (EditText) dateTimeLayout.findViewById(R.id.etMinEnd);
		etSecStart = (EditText) dateTimeLayout.findViewById(R.id.etSecStart);
		etSecEnd = (EditText) dateTimeLayout.findViewById(R.id.etSecEnd);
		btnStartLoad = (Button) dateTimeLayout.findViewById(R.id.btnStartLoad);
		btnStartLoad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadData();

			}
		});

		dialog = new Dialog(this);
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = (int) (screenWidth * 0.8);
		params.height = screenHeight / 5;
		params.width = (int) (screenWidth * 0.8);
		params.height = (int) (screenHeight * 0.8);
		dialog.getWindow().setAttributes(params);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(dateTimeLayout);

		return dialog;
	}

	/**
	 * 判断字符串是否为null或者为空.
	 * 
	 * @param str
	 *            传入的字符串
	 * @return boolean true or false
	 */
	private static boolean isEmpty(String str) {
		if (str == null || str == "" || str.trim().equals(""))
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gizwits.gizdataaccess.listener.GizDataAccessSourceListener#didSaveData
	 * (com.gizwits.gizdataaccess.GizDataAccessSource,
	 * com.gizwits.gizdataaccess.entity.GizDataAccessErrorCode,
	 * java.lang.String)
	 */
	@Override
	public void didSaveData(GizDataAccessSource arg0,
			GizDataAccessErrorCode arg1, String arg2) {
		// TODO Auto-generated method stub

	}

}
