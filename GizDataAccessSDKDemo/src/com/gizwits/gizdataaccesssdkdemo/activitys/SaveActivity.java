/**
 * Project Name:GizDataAccessSDKDemo
 * File Name:SaveActivity.java
 * Package Name:com.gizwits.gizdataaccesssdkdemo
 * Date:2015-1-12 15:30:30
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gizwits.gizdataaccess.GizDataAccessSource;
import com.gizwits.gizdataaccess.entity.GizDataAccessErrorCode;
import com.gizwits.gizdataaccess.listener.GizDataAccessSourceListener;
import com.gizwits.gizdataaccesssdkdemo.Constant;
import com.gizwits.gizdataaccesssdkdemo.R;
import com.gizwits.gizdataaccesssdkdemo.utils.DateUtils;
import com.gizwits.gizdataaccesssdkdemo.utils.NetworkUtils;

// TODO: Auto-generated Javadoc
/**
 * 
 * ClassName: Class SaveActivity. <br/>
 * 数据上传界面，该界面可指定上传的数据点json串和时间戳<br/>
 * date: 2015-1-9 18:26:04 <br/>
 * 
 * @author Lien
 */
public class SaveActivity extends Activity implements
		GizDataAccessSourceListener {

	/** 年份输入框. */
	EditText etYear;

	/** 月份输入框. */
	EditText etMon;

	/** 日期输入框. */
	EditText etDay;

	/** 小时输入框. */
	EditText etHour;

	/** 分钟输入框. */
	EditText etMin;

	/** 秒输入框. */
	EditText etSec;

	/** 数据点json输入框. */
	EditText etJson;

	/** 上传数据按钮. */
	Button btnSave;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_save);
		initViews();
	}

	/**
	 * 初始化空间.
	 */
	private void initViews() {
		etYear = (EditText) findViewById(R.id.etYear);
		etMon = (EditText) findViewById(R.id.etMon);
		etDay = (EditText) findViewById(R.id.etDay);
		etHour = (EditText) findViewById(R.id.etHour);
		etMin = (EditText) findViewById(R.id.etMin);
		etSec = (EditText) findViewById(R.id.etSec);
		etJson = (EditText) findViewById(R.id.etJson);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetworkUtils.isNetworkConnected(SaveActivity.this)) {
					saveData();
				} else {
					Toast.makeText(SaveActivity.this, "网络已断开",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	/**
	 * 上传数据.
	 */
	private void saveData() {
		// 获取空间中的字符串数据
		String strYear = etYear.getText().toString();
		String strMon = etMon.getText().toString();
		String strDay = etDay.getText().toString();
		String strHour = etHour.getText().toString();
		String strMin = etMin.getText().toString();
		String strSec = etSec.getText().toString();
		String strJson = replaceBlank(etJson.getText().toString().trim());
		// 非空判断
		if (isEmpty(strYear) || isEmpty(strMon) || isEmpty(strDay)
				|| isEmpty(strHour) || isEmpty(strMin) || isEmpty(strSec)) {
			Toast.makeText(SaveActivity.this, "请输入正确的时间", Toast.LENGTH_SHORT)
					.show();
		} else if (isEmpty(strJson)) {
			Toast.makeText(SaveActivity.this, "请输入正确的Json串", Toast.LENGTH_SHORT)
					.show();
		} else {

			// JSONObject jsonObject;
			// try {
			// 根据传入的json数据生成JsonObject
			// jsonObject = new JSONObject(strJson);
			// 根据传入的时间字符串转换为以1970为准的毫秒数
			long time = DateUtils.getStringToDate(strYear + strMon + strDay
					+ strHour + strMin + strSec + "000");
			Log.i("saveData", "timelong=" + time);
			Log.i("saveData", "timestr=" + DateUtils.getDateToString(time));
			Log.i("saveData", "json=" + strJson);
			// 上传数据
			new GizDataAccessSource(this)
					.saveData(Constant.TOKEN, Constant.PRODUCTKEY,
							Constant.DEVICE_SN, time, strJson + "");
			// } catch (JSONException e) {
			// e.printStackTrace();
			// Toast.makeText(SaveActivity.this, "json数据格式异常,请检查",
			// Toast.LENGTH_SHORT).show();
			// }

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
	public void didLoadData(GizDataAccessSource arg0, JSONArray arg1,
			GizDataAccessErrorCode arg2, String arg3) {

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
	public void didSaveData(GizDataAccessSource source,
			GizDataAccessErrorCode result, String message) {
		if (result.getResult() == 0) {
			Toast.makeText(SaveActivity.this, "上传成功！", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(SaveActivity.this, "上传失败:" + message,
					Toast.LENGTH_SHORT).show();
		}
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

	/**
	 * 处理字符串中的特殊字符
	 * 
	 * @param str
	 *            传入的字符串
	 * @return String 处理后的字符串
	 * */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

}
