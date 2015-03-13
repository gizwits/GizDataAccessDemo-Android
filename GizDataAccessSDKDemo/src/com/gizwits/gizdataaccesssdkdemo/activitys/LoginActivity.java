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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizdataaccess.GizDataAccessLogin;
import com.gizwits.gizdataaccess.entity.GizDataAccessErrorCode;
import com.gizwits.gizdataaccess.entity.GizDataAccessThirdAccountType;
import com.gizwits.gizdataaccess.listener.GizDataAccessLoginListener;
import com.gizwits.gizdataaccesssdkdemo.Constant;
import com.gizwits.gizdataaccesssdkdemo.R;
import com.gizwits.gizdataaccesssdkdemo.utils.LoginType;
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
public class LoginActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	/** The btn login. */
	Button btnLoginAnonymous;
	Button btnLogin;
	Button btnThird;
	EditText etUserName;
	EditText etPassword;
	EditText etUid;
	EditText etToken;
	RadioGroup rgThirdAccount;
	RadioButton rbBaidu;
	RadioButton rbSina;
	RadioButton rbQQ;
	GizDataAccessThirdAccountType mAccessThirdAccountType = GizDataAccessThirdAccountType.kGizDataAccessThirdAccountTypeBAIDU;
	//
	// /** The tv login. */
	// TextView tvLogin;
	//
	// /** The pb login. */
	// ProgressBar pbLogin;

	GizDataAccessLoginListener accessLoginListener = new GizDataAccessLoginListener() {
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
				enableBtns(true);
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
				setProgressBarIndeterminateVisibility(false);
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				Constant.TOKEN = token;
				Constant.UID = uid;
				startActivity(intent);
			} else {
				enableBtns(true);

				setProgressBarIndeterminateVisibility(false);

				if (NetworkUtils.isNetworkConnected(LoginActivity.this)) {
					Toast.makeText(LoginActivity.this,
							"登录失败:" + message == null ? "" : message,
							Toast.LENGTH_SHORT).show();;
				} else {
					Toast.makeText(LoginActivity.this, "登录失败:网络已断开",
							Toast.LENGTH_SHORT).show();
				}

			}

		}
	};

	/**
	 * On create.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_login);
		setProgressBarIndeterminateVisibility(false);
		btnLoginAnonymous = (Button) findViewById(R.id.btnLoginAnonymous);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnThird = (Button) findViewById(R.id.btnThird);
		etUserName = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_password);
		etUid = (EditText) findViewById(R.id.et_uid);
		etToken = (EditText) findViewById(R.id.et_token);
		rgThirdAccount = (RadioGroup) findViewById(R.id.rg_thirdAccount);
		rbBaidu = (RadioButton) findViewById(R.id.rb_baidu);
		rbSina = (RadioButton) findViewById(R.id.rb_sina);
		rbQQ = (RadioButton) findViewById(R.id.rb_qq);
		rbBaidu.setChecked(true);
		btnLogin.setOnClickListener(this);
		btnLoginAnonymous.setOnClickListener(this);
		btnThird.setOnClickListener(this);
		rgThirdAccount.setOnCheckedChangeListener(this);
	}

	/**
	 * Login.
	 */
	private void loginAnonymous() {
		// pbLogin.setVisibility(View.VISIBLE);
		// tvLogin.setText("登录中请稍候");
		enableBtns(false);
		setProgressBarIndeterminateVisibility(true);
		new GizDataAccessLogin(accessLoginListener).loginAnonymous();
		Constant.loginType = LoginType.loginAnonymous;
	}

	/**
	 * Login.
	 */
	private void loginNormal(String username, String password) {
		// pbLogin.setVisibility(View.VISIBLE);
		// tvLogin.setText("登录中请稍候");
		enableBtns(false);
		setProgressBarIndeterminateVisibility(true);
		new GizDataAccessLogin(accessLoginListener).login(username, password);
		Constant.loginType = LoginType.LoginReal;
	}

	/**
	 * Login.
	 */
	private void loginThird(GizDataAccessThirdAccountType thirdAccountType,
			String uid, String token) {
		// pbLogin.setVisibility(View.VISIBLE);
		// tvLogin.setText("登录中请稍候");
		enableBtns(false);
		setProgressBarIndeterminateVisibility(true);
		new GizDataAccessLogin(accessLoginListener).loginWithThirdAccountType(
				thirdAccountType, uid, token);
		Constant.loginType = LoginType.loginThirdType;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_register:
			Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_reset:
			Intent intent2 = new Intent(LoginActivity.this,ResetActivity.class);
			startActivity(intent2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			String username = etUserName.getText().toString();
			String password = etPassword.getText().toString();
			if (username == null || password == null) {
				Toast.makeText(LoginActivity.this, "请输入账号和密码",
						Toast.LENGTH_SHORT);
			} else {
				loginNormal(username, password);
			}
			
			break;
		case R.id.btnThird:
			String uid = etUid.getText().toString();
			String token = etToken.getText().toString();
			if (etUid == null || etToken == null) {
				Toast.makeText(LoginActivity.this, "请输入uid和token",
						Toast.LENGTH_SHORT);
			} else {
				loginThird(mAccessThirdAccountType, uid, token);
			}
			break;

		default:
			loginAnonymous();
			break;
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int id = group.getCheckedRadioButtonId();
		switch (id) {
		case R.id.rb_baidu:
			mAccessThirdAccountType = GizDataAccessThirdAccountType.kGizDataAccessThirdAccountTypeBAIDU;
			break;
		case R.id.rb_sina:
			mAccessThirdAccountType = GizDataAccessThirdAccountType.kGizDataAccessThirdAccountTypeSINA;
			break;
		default:
			mAccessThirdAccountType = GizDataAccessThirdAccountType.kGizDataAccessThirdAccountTypeQQ;
			break;
		}

	}

	private void enableBtns(boolean flag) {
		btnLogin.setEnabled(flag);
		btnLoginAnonymous.setEnabled(flag);
		btnThird.setEnabled(flag);
	}
}
