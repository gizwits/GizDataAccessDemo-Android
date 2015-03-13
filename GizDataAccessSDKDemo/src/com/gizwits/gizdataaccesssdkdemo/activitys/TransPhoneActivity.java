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
import com.gizwits.gizdataaccesssdkdemo.Constant;
import com.gizwits.gizdataaccesssdkdemo.R;
import com.gizwits.gizdataaccesssdkdemo.utils.NetworkUtils;
import com.gizwits.gizdataaccesssdkdemo.utils.RegexUtils;

public class TransPhoneActivity extends Activity implements OnClickListener {
	private Button btnRequestCode;
	private Button btnReset;
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
				Toast.makeText(TransPhoneActivity.this, "发送成功!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TransPhoneActivity.this, "发送失败:" + message,
						Toast.LENGTH_SHORT);
			}
		};

		public void didTransAnonymousUser(GizDataAccessErrorCode result,
				String message) {
			setProgressBarIndeterminateVisibility(false);
			if (result == GizDataAccessErrorCode.kGizDataAccessErrorNone) {
				Toast.makeText(TransPhoneActivity.this, "转换成功!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TransPhoneActivity.this, "转换失败:" + message,
						Toast.LENGTH_SHORT).show();
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_trans_phone);
		btnReset = (Button) findViewById(R.id.btnRegist);
		btnRequestCode = (Button) findViewById(R.id.btnRequestCode);
		btnCancle = (Button) findViewById(R.id.btnCancle);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etCode = (EditText) findViewById(R.id.etCode);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnCancle.setOnClickListener(this);
		btnReset.setOnClickListener(this);
		btnRequestCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRegist:
			if (NetworkUtils.isNetworkConnected(TransPhoneActivity.this)) {
				String phone = etUsername.getText().toString();
				String code = etCode.getText().toString();
				String password = etPassword.getText().toString();
				if (RegexUtils.isEmpty(phone)
						|| !RegexUtils.checkCellphone(phone)) {
					Toast.makeText(TransPhoneActivity.this, "请输入正确的手机号",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (RegexUtils.isEmpty(code)) {
					Toast.makeText(TransPhoneActivity.this, "请输入验证码",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (RegexUtils.isEmpty(password)) {
					Toast.makeText(TransPhoneActivity.this, "请输入密码",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					doTrans(phone, code, password);
				}

			} else {
				Toast.makeText(TransPhoneActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnRequestCode:
			String phone = etUsername.getText().toString();
			if (RegexUtils.isEmpty(phone) || !RegexUtils.checkCellphone(phone)) {
				Toast.makeText(TransPhoneActivity.this, "请输入正确的手机号",
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

	private void doTrans(String phone, String code, String password) {
		setProgressBarIndeterminateVisibility(true);
		System.out.println("doTrans");
		new GizDataAccessLogin(gizDataAccessLoginListener)
				.transAnonymousUserToPhoneUser(Constant.TOKEN, phone, code,
						password);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
