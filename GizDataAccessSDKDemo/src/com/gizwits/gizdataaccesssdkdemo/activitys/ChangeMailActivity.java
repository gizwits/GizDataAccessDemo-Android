package com.gizwits.gizdataaccesssdkdemo.activitys;

import com.gizwits.gizdataaccess.GizDataAccessLogin;
import com.gizwits.gizdataaccess.entity.GizDataAccessAccountType;
import com.gizwits.gizdataaccess.entity.GizDataAccessErrorCode;
import com.gizwits.gizdataaccess.listener.GizDataAccessLoginListener;
import com.gizwits.gizdataaccesssdkdemo.Constant;
import com.gizwits.gizdataaccesssdkdemo.R;
import com.gizwits.gizdataaccesssdkdemo.utils.NetworkUtils;
import com.gizwits.gizdataaccesssdkdemo.utils.RegexUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeMailActivity extends Activity implements OnClickListener {
	private Button btnChange;
	private Button btnCancle;
	private EditText etMail;
	private GizDataAccessLoginListener gizDataAccessLoginListener = new GizDataAccessLoginListener() {

		public void didChangeUserInfo(GizDataAccessErrorCode result,
				String message) {
			setProgressBarIndeterminateVisibility(false);
			if (result == GizDataAccessErrorCode.kGizDataAccessErrorNone) {
				Toast.makeText(ChangeMailActivity.this, "修改成功!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ChangeMailActivity.this, "修改失败:" + message,
						Toast.LENGTH_SHORT).show();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_change_mail);
		setProgressBarIndeterminateVisibility(false);
		btnChange = (Button) findViewById(R.id.btnChange);
		btnCancle = (Button) findViewById(R.id.btnCancle);
		etMail = (EditText) findViewById(R.id.etMail);
		btnChange.setOnClickListener(this);
		btnCancle.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnChange:
			if (NetworkUtils.isNetworkConnected(ChangeMailActivity.this)) {
				String mail = etMail.getText().toString();
				if (RegexUtils.isEmpty(mail) || !RegexUtils.checkEmail(mail)) {
					Toast.makeText(ChangeMailActivity.this, "请输入邮箱地址",
							Toast.LENGTH_SHORT).show();
					return;
				}
				doChangeInfo(mail, null,
						GizDataAccessAccountType.kGizDataAccessAccountTypeEmail);
			} else {
				Toast.makeText(ChangeMailActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnCancle:
			onBackPressed();
			break;
		}

	}

	private void doChangeInfo(String mail, String code,
			GizDataAccessAccountType accountType) {
		setProgressBarIndeterminateVisibility(true);
		new GizDataAccessLogin(gizDataAccessLoginListener).changeUserInfo(
				Constant.TOKEN, mail, code, accountType);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
