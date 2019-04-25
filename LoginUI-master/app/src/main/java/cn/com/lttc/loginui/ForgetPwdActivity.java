package cn.com.lttc.loginui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import myOkHttp.MyOkHttp;
import okhttp3.FormBody;
import okhttp3.Request;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_retrieve_tel,et_retrieve_code_input,et_reset_pwd;
    //private  Button bt_reset_submit;
    private Button retrieve_sms_call,bt_retrieve_submit,bt_reset_submit;
    private String code,user_phoneNumber;
//计时器
    private  TimeCount timeCount;
    //et_retrieve_tel手机号
    //et_retrieve_code_input短信验证码
    //retrieve_sms_call获取短信验证码
    //bt_retrieve_submit下一步

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_retrieve_pwd);

        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        init();
    }


    private void init() {
        et_retrieve_tel = (EditText) findViewById(R.id.et_retrieve_tel);
        et_retrieve_tel.setOnClickListener(this);
        et_retrieve_code_input = (EditText) findViewById(R.id.et_retrieve_code_input);
        et_retrieve_code_input.setOnClickListener(this);
        retrieve_sms_call = (Button)findViewById(R.id.retrieve_sms_call);
        retrieve_sms_call.setEnabled(true);
        retrieve_sms_call.setOnClickListener(this);
        bt_retrieve_submit = (Button)findViewById(R.id.bt_retrieve_submit);
        bt_retrieve_submit.setOnClickListener(this);




    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
                //获取验证码
            case R.id.retrieve_sms_call:
                getAuth();
                break;
                //输入验证码。进行下一步按钮
            case R.id.bt_retrieve_submit:
                toPage();
                break;
                //确认按钮
            case R.id.bt_reset_submit:
                changePassWord();
                toPageRegister();
                break;
        }
    }

    private void toPageRegister() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ForgetPwdActivity.this, MainActivity.class));
            }
        });
    }

    private void changePassWord() {
        String newPassWord = et_reset_pwd.getText().toString();
        Log.i("1","重新输入的密码"+newPassWord);
        Log.i("2","手机号码"+user_phoneNumber);
        FormBody formBody = new FormBody.Builder().add("password", newPassWord).add("phonenumber",user_phoneNumber).build();
      // MyOkHttp.getInstance().asyncPost("http://192.168.199.154:8080/appReq/forgetPassController/changePassWord", formBody, new MyOkHttp.HttpCallBack() {
       // MyOkHttp.getInstance().asyncPost("http://192.168.1.6:8080/appReq/forgetPassController/changePassWord", formBody, new MyOkHttp.HttpCallBack() {
       MyOkHttp.getInstance().asyncPost("http://192.168.199.178:8080/appReq/forgetPassController/changePassWord", formBody, new MyOkHttp.HttpCallBack() {

       // MyOkHttp.getInstance().asyncPost("http://192.168.43.228:8080/appReq/forgetPassController/changePassWord", formBody, new MyOkHttp.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i("NO", "修改密码失败");
            }

            @Override
            public void onSuccess(Request request, String result) {
//                textView.setText(result);
                Log.i("YES", result);
                try{
                    JSONObject json=new JSONObject(result);
                    Toast.makeText(ForgetPwdActivity.this,json.getString("String"),Toast.LENGTH_SHORT).show();

                }catch(JSONException e){
                    Log.i("Exception","JSONException");
                }
            }
        });
    }
    private void toPage() {
        //inputNuber:手机输入的验证码
       String inputNumber = et_retrieve_code_input.getText().toString();
        Log.i(
                "2","手机输入的验证码"+inputNumber
        );

        if(inputNumber.equals(code)&&!TextUtils.isEmpty(et_retrieve_code_input.getText())){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.activity_main_reset_pwd);

                    et_reset_pwd = (EditText)findViewById(R.id.et_reset_pwd);
                    et_reset_pwd.setOnClickListener(ForgetPwdActivity.this::onClick);
                    bt_reset_submit = (Button)findViewById(R.id.bt_reset_submit);
                    bt_reset_submit.setOnClickListener(ForgetPwdActivity.this::onClick);




                }
            });
        }else{
            Toast.makeText(ForgetPwdActivity.this,"输入验证码错误",Toast.LENGTH_SHORT);

        }
    }

    //authNumber:从手机输入的验证码
    //code：手机获得的验证码
    private void getAuth() {
        final String phoneNumber = et_retrieve_tel.getText().toString();
        Log.i("1","phoneNumber手机输入的手机号"+phoneNumber);
        FormBody formBody = new FormBody.Builder().add("phoneNumber", phoneNumber).build();
       //MyOkHttp.getInstance().asyncPost("http://192.168.199.154:8080/appReq/forgetPassController/getAuthNumber", formBody, new MyOkHttp.HttpCallBack() {
      MyOkHttp.getInstance().asyncPost("http://192.168.199.178:8080/appReq/forgetPassController/getAuthNumber", formBody, new MyOkHttp.HttpCallBack() {
       // MyOkHttp.getInstance().asyncPost("http://192.168.43.228:8080/appReq/forgetPassController/changePassWord", formBody, new MyOkHttp.HttpCallBack() {
      //  MyOkHttp.getInstance().asyncPost("http://192.168.1.6:8080/appReq/forgetPassController/getAuthNumber", formBody, new MyOkHttp.HttpCallBack() {

            @Override
            public void onError(Request request, IOException e) {
                Log.i("NO", "获取验证码失败");
            }

            @Override
            public void onSuccess(Request request, String result){
//                textView.setText(result);
                Log.i("YES", result);
                try{
                    JSONObject json=new JSONObject(result);
                    Toast t=Toast.makeText(ForgetPwdActivity.this,json.getString("String"),Toast.LENGTH_SHORT);
                    t.show();
                    code = json.getString("number");
                    Log.i("1","code"+code);
                    if(json.getInt("state")<0){
                        Toast toast=Toast.makeText(ForgetPwdActivity.this,json.getString("String"),Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        a();
                        user_phoneNumber=phoneNumber;
                        code = json.getString("number");
                        Toast toast=Toast.makeText(ForgetPwdActivity.this,json.getString("String"),Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch(JSONException e){
                    Log.i("Exception","JSONException");
                }
            }
        });
    }
    private void a(){
        new Thread(new Runnable() {
            private long start=10000;
            @Override
            public void run() {
                while (true) {
                    b(start);
                    start=start-1000;
                    if(start<=0) {
                        c();
                        return ;
                    }
                    // ------- ends here
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.i("Exception","exp");
                    }
                }
            }
        }).start();
    }
    private void b(final long t){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                retrieve_sms_call.setEnabled(false);
                retrieve_sms_call.setText(( t / 1000) + "后可重新发送验证码");
            }
        });

    }
    private void c(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                retrieve_sms_call.setEnabled(true);
                retrieve_sms_call.setText("发送验证码");
            }
        });

    }
    /**
     * 内部类计时器
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            retrieve_sms_call.setText("重新发送验证码");
            retrieve_sms_call.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            retrieve_sms_call.setText((millisUntilFinished / 1000) + "后可重新发送验证码");
            retrieve_sms_call.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount!=null) {
            timeCount.cancel();
        }
    }

}


