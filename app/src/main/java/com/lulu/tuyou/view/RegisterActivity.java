package com.lulu.tuyou.view;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.ActivityRegisterBinding;
import com.lulu.tuyou.presenter.IRegisterPresenter;
import com.lulu.tuyou.presenter.RegisterPresenterImpl;
import com.lulu.tuyou.utils.Utils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, IRegisterView {
    private IRegisterPresenter mPresenter;
    private Button mRegisterBtn;
    private EditText mEtUseName;
    private EditText mEtPwd;
    private EditText mEtPwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mPresenter = new RegisterPresenterImpl(this);
        initView(binding);
    }

    private void initView(ActivityRegisterBinding binding) {
        mRegisterBtn = binding.regBtnRegister;
        mEtUseName = binding.regEtUsername;
        mEtPwd = binding.regEtPassword;
        mEtPwd2 = binding.regEtPassword2;
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reg_btn_register:
                Dialog dialog = Utils.createLoadingDialog(
                        this, getString(R.string.reg_text), getString(R.string.reg_reging)
                );
                dialog.show();
                String username = mEtUseName.getText().toString();
                String pwd = mEtPwd.getText().toString();
                String pwd2 = mEtPwd2.getText().toString();
                mPresenter.doRegister(
                        username,
                        pwd,
                        pwd2,
                        dialog
                );
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 处理注册完毕之后的逻辑
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onResult(boolean isSuccess, int resultCode) {
        if (isSuccess) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, TuYouActivity.class));
            finish();
        } else {
            Toast.makeText(this, "注册异常" + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
