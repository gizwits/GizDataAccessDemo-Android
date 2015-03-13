package com.gizwits.gizdataaccesssdkdemo.activitys;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gizwits.gizdataaccess.GizDataAccessLogin;
import com.gizwits.gizdataaccess.entity.GizDataAccessAccountType;
import com.gizwits.gizdataaccess.entity.GizDataAccessErrorCode;
import com.gizwits.gizdataaccess.listener.GizDataAccessLoginListener;
import com.gizwits.gizdataaccesssdkdemo.R;
import com.gizwits.gizdataaccesssdkdemo.utils.NetworkUtils;
import com.gizwits.gizdataaccesssdkdemo.utils.RegexUtils;

public class RegisterActivity extends Activity implements OnClickListener {
	private Button btnRequestCode;
	private Button btnRegist;
	private Button btnCancle;
	private EditText etUsername;
	private EditText etCode;
	private EditText etPassword;

	/**
	 * 验证码重发倒计时
	 */
	int secondleft = 60;

	/**
	 * The timer.
	 */
	Timer timer;

	/**
	 * ClassName: Enum handler_key. <br/>
	 * <br/>
	 * date: 2014-11-26 17:51:10 <br/>
	 * 
	 * @author Lien
	 */
	private enum handler_key {

		/**
		 * 倒计时通知
		 */
		TICK_TIME,

	}

	/**
	 * The handler.
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			secondleft--;
			if (secondleft <= 0) {
				timer.cancel();
				btnRequestCode.setEnabled(true);
				btnRequestCode.setText("重新获取验证码");

			} else {
				btnRequestCode.setText(secondleft + "秒后\n重新获取");

			}

		}
	};
	private GizDataAccessLoginListener gizDataAccessLoginListener = new GizDataAccessLoginListener() {
		public void didRequestSendVerifyCode(GizDataAccessErrorCode result,
				String message) {
			setProgressBarIndeterminateVisibility(false);
			if (result == GizDataAccessErrorCode.kGizDataAccessErrorNone) {
				Toast.makeText(RegisterActivity.this, "发送成功!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(RegisterActivity.this, "发送失败:" + message,
						Toast.LENGTH_SHORT).show();
			}
		};

		public void didRegisterUser(String uid, String token,
				GizDataAccessErrorCode result, String message) {
			setProgressBarIndeterminateVisibility(false);
			if (result == GizDataAccessErrorCode.kGizDataAccessErrorNone) {
				Toast.makeText(RegisterActivity.this, "注册成功!",
						Toast.LENGTH_SHORT).show();
				onBackPressed();
			} else {
				Toast.makeText(RegisterActivity.this, "注册失败:" + message,
						Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_register);
		setProgressBarIndeterminateVisibility(false);
		btnRegist = (Button) findViewById(R.id.btnRegist);
		btnRequestCode = (Button) findViewById(R.id.btnRequestCode);
		btnCancle = (Button) findViewById(R.id.btnCancle);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etCode = (EditText) findViewById(R.id.etCode);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnCancle.setOnClickListener(this);
		btnRegist.setOnClickListener(this);
		btnRequestCode.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRegist:
			if (NetworkUtils.isNetworkConnected(RegisterActivity.this)) {
				String userName = etUsername.getText().toString();
				String code = etCode.getText().toString();
				String password = etPassword.getText().toString();
				if (RegexUtils.isEmpty(userName)) {
					Toast.makeText(RegisterActivity.this, "请输入账号",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (RegexUtils.isEmpty(password)) {
					Toast.makeText(RegisterActivity.this, "请输入密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (RegexUtils.checkCellphone(userName)) {
					if (RegexUtils.isEmpty(code)) {
						btnRequestCode.setEnabled(true);
						Toast.makeText(RegisterActivity.this, "请输入验证码",
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						doRegister(
								userName,
								code,
								password,
								GizDataAccessAccountType.kGizDataAccessAccountTypePhone);
					}
				} else if (RegexUtils.checkEmail(userName)) {
					doRegister(
							userName,
							code,
							password,
							GizDataAccessAccountType.kGizDataAccessAccountTypeEmail);
				} else {
					doRegister(
							userName,
							code,
							password,
							GizDataAccessAccountType.kGizDataAccessAccountTypeNormal);
				}
			} else {
				Toast.makeText(RegisterActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.btnRequestCode:
			String phone = etUsername.getText().toString();
			if (RegexUtils.isEmpty(phone) || !RegexUtils.checkCellphone(phone)) {
				Toast.makeText(RegisterActivity.this, "请输入正确的手机号",
						Toast.LENGTH_SHORT).show();
				return;
			}
			requestSendVerifyCode(phone);
			break;

		default:
			onBackPressed();
			break;
		}

	}

	private void requestSendVerifyCode(String phone) {
		setProgressBarIndeterminateVisibility(true);
		new GizDataAccessLogin(gizDataAccessLoginListener)
				.requestSendVerifyCode(phone);
		btnRequestCode.setEnabled(false);
		secondleft = 60;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(handler_key.TICK_TIME.ordinal());
			}
		}, 1000, 1000);
	}

	private void doRegister(String userName, String code, String password,
			GizDataAccessAccountType accountType) {
		setProgressBarIndeterminateVisibility(true);
		new GizDataAccessLogin(gizDataAccessLoginListener).registerUser(
				userName, password, code, accountType);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
