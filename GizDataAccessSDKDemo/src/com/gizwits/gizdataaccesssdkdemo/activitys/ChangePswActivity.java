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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePswActivity extends Activity implements OnClickListener {
	private EditText etOldPsw;
	private EditText etNewPsw;
	private Button btnChange;
	private Button btnCancle;
	private GizDataAccessLoginListener gizDataAccessLoginListener = new GizDataAccessLoginListener() {
		public void didChangeUserPassword(GizDataAccessErrorCode result,
				String message) {
			setProgressBarIndeterminateVisibility(false);
			if (result == GizDataAccessErrorCode.kGizDataAccessErrorNone) {
				Toast.makeText(ChangePswActivity.this, "修改成功!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ChangePswActivity.this, "修改失败:" + message,
						Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_change_psw);
		setProgressBarIndeterminateVisibility(false);
		etNewPsw = (EditText) findViewById(R.id.etNewPsw);
		etOldPsw = (EditText) findViewById(R.id.etOldPsw);
		btnChange = (Button) findViewById(R.id.btnChange);
		btnCancle = (Button) findViewById(R.id.btnCancle);
		btnCancle.setOnClickListener(this);
		btnChange.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnChange:
			if(NetworkUtils.isNetworkConnected(ChangePswActivity.this)){
				String oldPsw = etOldPsw.getText().toString();
				String newPsw = etNewPsw.getText().toString();
				if (RegexUtils.isEmpty(oldPsw)) {
					Toast.makeText(ChangePswActivity.this, "请输入旧的密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (RegexUtils.isEmpty(newPsw)) {
					Toast.makeText(ChangePswActivity.this, "请输入新的密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				doChangePsw(oldPsw, newPsw);
			}else{
				Toast.makeText(ChangePswActivity.this, "网络未连接", Toast.LENGTH_SHORT)
				.show();
			}
			
			break;

		default:
			onBackPressed();
			break;
		}

	}

	private void doChangePsw(String oldPassword, String newPassword) {
		setProgressBarIndeterminateVisibility(true);
		System.out.println("token:"+Constant.TOKEN);
		new GizDataAccessLogin(gizDataAccessLoginListener).changeUserPassword(
				Constant.TOKEN, oldPassword, newPassword);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
