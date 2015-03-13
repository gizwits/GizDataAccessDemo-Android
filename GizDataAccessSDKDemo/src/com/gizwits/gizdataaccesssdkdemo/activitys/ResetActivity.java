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

public class ResetActivity extends Activity implements OnClickListener {
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

	private GizDataAccessAccountType mAccessAccountType = GizDataAccessAccountType.kGizDataAccessAccountTypePhone;
	private GizDataAccessLoginListener gizDataAccessLoginListener = new GizDataAccessLoginListener() {
		public void didRequestSendVerifyCode(GizDataAccessErrorCode result,
				String message) {
			setProgressBarIndeterminateVisibility(false);
			if (result == GizDataAccessErrorCode.kGizDataAccessErrorNone) {
				Toast.makeText(ResetActivity.this, "发送成功!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(ResetActivity.this, "发送失败:" + message,
						Toast.LENGTH_SHORT);
			}
		};

		public void didChangeUserPassword(GizDataAccessErrorCode result,
				String message) {
			setProgressBarIndeterminateVisibility(false);
			if (result == GizDataAccessErrorCode.kGizDataAccessErrorNone) {

				if (mAccessAccountType == GizDataAccessAccountType.kGizDataAccessAccountTypePhone) {
					Toast.makeText(ResetActivity.this, "修改成功!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ResetActivity.this, "已发送邮件",
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(ResetActivity.this, "修改失败:" + message,
						Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_reset);
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
			if (NetworkUtils.isNetworkConnected(ResetActivity.this)) {
				String userName = etUsername.getText().toString();
				String code = etCode.getText().toString();
				String password = etPassword.getText().toString();
				if (RegexUtils.isEmpty(userName)) {
					Toast.makeText(ResetActivity.this, "请输入账号",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (RegexUtils.checkCellphone(userName)) {
					if (RegexUtils.isEmpty(code)) {
						Toast.makeText(ResetActivity.this, "请输入验证码",
								Toast.LENGTH_SHORT).show();
						return;
					} else if (RegexUtils.isEmpty(password)) {
						Toast.makeText(ResetActivity.this, "请输入密码",
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						mAccessAccountType = GizDataAccessAccountType.kGizDataAccessAccountTypePhone;
						doReset(userName,
								code,
								password,
								GizDataAccessAccountType.kGizDataAccessAccountTypePhone);
					}
				} else if (RegexUtils.checkEmail(userName)) {
					mAccessAccountType = GizDataAccessAccountType.kGizDataAccessAccountTypeEmail;
					doReset(userName,
							null,
							null,
							GizDataAccessAccountType.kGizDataAccessAccountTypeEmail);
				} else {
					Toast.makeText(ResetActivity.this, "只支持使用手机号或邮箱重置",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(ResetActivity.this, "网络未连接", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.btnRequestCode:
			String phone = etUsername.getText().toString();
			if (RegexUtils.isEmpty(phone) || !RegexUtils.checkCellphone(phone)) {
				Toast.makeText(ResetActivity.this, "请输入正确的手机号",
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

	private void doReset(String userName, String code, String password,
			GizDataAccessAccountType accountType) {
		setProgressBarIndeterminateVisibility(true);
		System.out.println("doReset");
		new GizDataAccessLogin(gizDataAccessLoginListener).resetPassword(
				userName, code, password, accountType);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
