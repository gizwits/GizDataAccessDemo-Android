/**
 * Project Name:GizDataAccessSDKDemo
 * File Name:LoginActivity.java
 * Package Name:com.gizwits.gizdataaccesssdkdemo
 * Date:2015-1-12 15:30:14
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizdataaccess.GizDataAccessLogin;
import com.gizwits.gizdataaccess.entity.GizDataAccessErrorCode;
import com.gizwits.gizdataaccess.listener.GizDataAccessLoginListener;
import com.gizwits.gizdataaccesssdkdemo.Constant;
import com.gizwits.gizdataaccesssdkdemo.R;
import com.gizwits.gizdataaccesssdkdemo.R.id;
import com.gizwits.gizdataaccesssdkdemo.R.layout;
import com.gizwits.gizdataaccesssdkdemo.utils.NetworkUtils;

// TODO: Auto-generated Javadoc
/**
 * 
 * ClassName: Class LoginActivity. <br/>
 * <br/>
 * date: 2015-1-9 14:08:00 <br/>
 * 
 * @author Lien
 */
public class LoginActivity extends Activity implements
		GizDataAccessLoginListener {

	/** The btn login. */
	Button btnLogin;

	/** The tv login. */
	TextView tvLogin;

	/** The pb login. */
	ProgressBar pbLogin;

	/**
	 * On create.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		tvLogin = (TextView) findViewById(R.id.tvLogin);
		pbLogin = (ProgressBar) findViewById(R.id.pbLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login();

			}
		});
		login();
	}

	/**
	 * Login.
	 */
	private void login() {
		pbLogin.setVisibility(View.VISIBLE);
		tvLogin.setText("登录中请稍候");
		btnLogin.setVisibility(View.GONE);
		new GizDataAccessLogin(this).loginAnonymous();
	}

	/**
	 * Did login.
	 * 
	 * @param uid
	 *            the uid
	 * @param token
	 *            the token
	 * @param result
	 *            the result
	 * @param message
	 *            the message
	 */
	@Override
	public void didLogin(String uid, String token,
			GizDataAccessErrorCode result, String message) {
		if (result.getResult() == 0 && uid != null && token != null) {
			Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			Constant.TOKEN = token;
			Constant.UID = uid;
			startActivity(intent);
			finish();
		} else {
			pbLogin.setVisibility(View.GONE);
			if (NetworkUtils.isNetworkConnected(this)) {
				tvLogin.setText("登录失败:" + message == null ? "" : message);
			} else {
				tvLogin.setText("登录失败:网络已断开");
			}

			btnLogin.setVisibility(View.VISIBLE);
		}

	}
}
