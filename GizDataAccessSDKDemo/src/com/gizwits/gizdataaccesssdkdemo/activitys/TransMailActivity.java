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

public class TransMailActivity extends Activity implements OnClickListener {
	private Button btnReset;
	private Button btnCancle;
	private EditText etUsername;
	private EditText etPassword;

	/**
	 * The timer.
	 */
	Timer timer;

	private GizDataAccessLoginListener gizDataAccessLoginListener = new GizDataAccessLoginListener() {

		public void didTransAnonymousUser(GizDataAccessErrorCode result,
				String message) {
			setProgressBarIndeterminateVisibility(false);
			if (result == GizDataAccessErrorCode.kGizDataAccessErrorNone) {
				Toast.makeText(TransMailActivity.this, "转换成功!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TransMailActivity.this, "转换失败:" + message,
						Toast.LENGTH_SHORT).show();
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_trans_mail);
		btnReset = (Button) findViewById(R.id.btnRegist);
		btnCancle = (Button) findViewById(R.id.btnCancle);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnCancle.setOnClickListener(this);
		btnReset.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRegist:
			if (NetworkUtils.isNetworkConnected(TransMailActivity.this)) {
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				if (RegexUtils.isEmpty(username)
						|| RegexUtils.checkCellphone(username)) {
					Toast.makeText(TransMailActivity.this, "请输入正确的账号",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (RegexUtils.isEmpty(password)) {
					Toast.makeText(TransMailActivity.this, "请输入密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				doTrans(username, password);

			} else {
				Toast.makeText(TransMailActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			onBackPressed();
			break;
		}

	}

	private void doTrans(String username, String password) {
		setProgressBarIndeterminateVisibility(true);
		System.out.println("doTrans");
		new GizDataAccessLogin(gizDataAccessLoginListener)
				.transAnonymousUserToNormalUser(Constant.TOKEN, username,
						password);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
