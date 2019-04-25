package cn.com.lttc.loginui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

import myOkHttp.MyOkHttp;
import okhttp3.FormBody;
import okhttp3.Request;
import android.widget.CompoundButton.OnCheckedChangeListener;



public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    //获取验证码
    private TextView tv_register_man,tv_register_female;
    // 确认按钮
    private Button btn_enter,bt_register_submit,bt_register_submit2;
    private Button tv_register_sms_call;
    // 发送验证码
    private Button btn_send_code;
    // 验证码输入框
    private EditText edit_code,et_register_username,et_register_auth_code;
    //注册用户名、密码输入框
    private EditText et_register_username2,et_register_pwd_input2;
    // 手机号码输入框
    private EditText edit_phone;
    // 验证码
    private String code,user_phoneNumber;
    String sex;
    CheckBox cb_protocol = null;
    /**
     * 短信平台 Account sid
     */
    public final static String SMS_SID = "";
    /**
     * 短信平台 Auth Token
     */
    public final static String SMS_TOKEN = "";
    /**
     * 短信平台 SMS_APPID
     */
    public final static String SMS_APPID = "";
    /**
     * 短信模板ID SMS_TEMPLATEID
     */
    public final static String SMS_TEMPLATEID = "";

    //计时器
    private  TimeCount timeCount;
    //chekBoxListener
    private OnCheckedChangeListener CheckedChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_step_one);
        init();

    }



    private void init() {
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        et_register_username  = (EditText) findViewById(R.id.et_register_username);
        et_register_username.setOnClickListener(this);
        et_register_auth_code  = (EditText) findViewById(R.id.et_register_auth_code);
        et_register_auth_code.setOnClickListener(this);
        tv_register_sms_call=(Button) findViewById(R.id.tv_register_sms_call);
        tv_register_sms_call.setEnabled(true);
        tv_register_sms_call.setOnClickListener(this);
        bt_register_submit = (Button)findViewById(R.id.bt_register_submit);
        bt_register_submit.setOnClickListener(this);
        cb_protocol = (CheckBox) findViewById(R.id.cb_protocol);
        CheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                 boolean isChecked) {
                // TODO Auto-generated method stub
                if (cb_protocol.isChecked()) {

                 Log.i("YES","已同意服务协议");
                } else {
                    Log.i("NO","请阅读并同意服务协议");
                }
            }
        };
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.ly_register_bar2:
                finish();
                break;
            case R.id.tv_register_sms_call:
                //获取验证码
                getAuth();
                break;
            case R.id.bt_register_submit:
                Registe();
                break;
            case R.id.bt_register_submit2:
                RegisteSuccess();
                break;
            case R.id.tv_register_man:
                tv_register_man.setTextColor(R.color.login_input_active);
                tv_register_man.setTextSize(20);
                tv_register_female.setTextColor(R.color.login_line_color);
                tv_register_female.setTextSize(12);
                checkSex();
                break;
            case R.id.tv_register_female:
                tv_register_female.setTextColor(R.color.login_input_active);
                tv_register_female.setTextSize(20);
                tv_register_man.setTextColor(R.color.login_line_color);
                tv_register_man.setTextSize(12);
                checkSex();
                break;
        }
    }

    private void checkSex() {
        if(tv_register_man.getTextSize()>tv_register_female.getTextSize()){
            Log.i("Sex","male");
            sex="男";
        }else  if(tv_register_man.getTextSize()<tv_register_female.getTextSize()){
           Log.i("Sex","female");
           sex = "女";
        }else {
            Toast.makeText(RegisterActivity.this,"请选择性别",Toast.LENGTH_SHORT).show();

      }
    }

    private void RegisteSuccess() {
        String userName = et_register_username2.getText().toString();
        String passWord = et_register_pwd_input2.getText().toString();
        FormBody formBody = new FormBody.Builder().add("username", userName).add("password", passWord).add("sex",sex).add("phonenumber",user_phoneNumber).build();
        Log.i("message","用户名"+userName +",密码"+passWord +",性别"+sex);
        //MyOkHttp.getInstance().asyncPost("http://192.168.199.154:8080/appReq/registerController/registerSuccess", formBody, new MyOkHttp.HttpCallBack() {
        //MyOkHttp.getInstance().asyncPost("http://192.168.43.228:8080/appReq/forgetPassController/changePassWord", formBody, new MyOkHttp.HttpCallBack() {
      //  MyOkHttp.getInstance().asyncPost("http://192.168.1.6:8080/appReq/registerController/registerSuccess", formBody, new MyOkHttp.HttpCallBack() {
        MyOkHttp.getInstance().asyncPost("http://192.168.199.178:8080/appReq/registerController/registerSuccess", formBody, new MyOkHttp.HttpCallBack() {

            @Override
            public void onError(Request request, IOException e) {
                Log.i("NO", e.toString());
            }

            @Override
            public void onSuccess(Request request, String result) {
//                textView.setText(result);
                Log.i("YES", result);
                try{
                    JSONObject json=new JSONObject(result);
                    Toast.makeText(RegisterActivity.this,json.getString("String"),Toast.LENGTH_SHORT).show();

//                    if(json.getInt("state")<0){
//                        Toast t=Toast.makeText(RegisterActivity.this,json.getString("String"),Toast.LENGTH_SHORT);
//                        t.show();
//                    }else{
//                        a();
//                        user_phoneNumber=phoneNumber;
//                        code = json.getString("number");
//                        Toast t=Toast.makeText(RegisterActivity.this,json.getString("String"),Toast.LENGTH_SHORT);
//                        t.show();
//                    }
                }catch(JSONException e){
                    Log.i("Exception","JSONException");
                }
            }
        });
    }

    //判断验证码和手机输入的是否
    //authNumber:从手机输入的验证码
    //code：手机获得的验证码
    private void Registe() {
        String authNumber = et_register_auth_code.getText().toString();
        Log.i("1", authNumber);
        Log.i("2",code);

            if (authNumber.equals(code)&&cb_protocol.isChecked()&&!TextUtils.isEmpty(et_register_username.getText())&&!TextUtils.isEmpty(et_register_auth_code.getText())){
                toRegPage();
            }else {
                Toast.makeText(RegisterActivity.this,"请阅读并同意服务协议，填写正确的验证码",Toast.LENGTH_SHORT).show();
            }
    }

    private void toRegPage(){
        runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                setContentView(R.layout.activity_main_register_step_two);

                tv_register_man=(TextView)findViewById(R.id.tv_register_man);
                tv_register_female=(TextView)findViewById(R.id.tv_register_female);
                bt_register_submit2=(Button)findViewById(R.id.bt_register_submit2);
                bt_register_submit2.setOnClickListener(RegisterActivity.this);
                et_register_username2=(EditText) findViewById(R.id.et_register_username2);
                et_register_pwd_input2=(EditText) findViewById(R.id.et_register_pwd_input2);

                tv_register_man.setOnClickListener(RegisterActivity.this);
                tv_register_female.setOnClickListener(RegisterActivity.this);
                tv_register_man.setTextColor(R.color.login_line_color);
                tv_register_man.setTextSize(12);
                tv_register_female.setTextColor(R.color.login_line_color);
                tv_register_female.setTextSize(12);

            }
        });
    }

    private void getAuth() {
        final String phoneNumber = et_register_username.getText().toString();
        FormBody formBody = new FormBody.Builder().add("phoneNumber", phoneNumber).build();
        //MyOkHttp.getInstance().asyncPost("http://192.168.199.154:8080/appReq/registerController/checkCellphone", formBody, new MyOkHttp.HttpCallBack() {
       // MyOkHttp.getInstance().asyncPost("http://192.168.43.228:8080/appReq/forgetPassController/changePassWord", formBody, new MyOkHttp.HttpCallBack() {
       // MyOkHttp.getInstance().asyncPost("http://192.168.1.6:8080/appReq/registerController/checkCellphone", formBody, new MyOkHttp.HttpCallBack() {
        //MyOkHttp.getInstance().asyncPost(formBody, "http://192.168.1.6:8080/appReq/registerController/checkCellphone", new MyOkHttp.HttpCallBack() {
        MyOkHttp.getInstance().asyncPost("http://192.168.199.178:8080/appReq/registerController/checkCellphone", formBody, new MyOkHttp.HttpCallBack() {

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
                            if(json.getInt("state")<0){
                                Toast t=Toast.makeText(RegisterActivity.this,json.getString("String"),Toast.LENGTH_SHORT);
                                t.show();
                            }else{
                                a();
                                user_phoneNumber=phoneNumber;
                                code = json.getString("number");
                                Toast t=Toast.makeText(RegisterActivity.this,json.getString("String"),Toast.LENGTH_SHORT);
                                t.show();
                            }
                        }catch(JSONException e){
                            Log.i("Exception","JSONException");
                        }
                    }
                });
    }

    private void a(){
        new Thread(new Runnable() {
            private long start=60000;
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
                tv_register_sms_call.setEnabled(false);
                tv_register_sms_call.setText(( t / 1000) + "后可重新发送验证码");
            }
        });

    }
    private void c(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_register_sms_call.setEnabled(true);
                tv_register_sms_call.setText("发送验证码");
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
                tv_register_sms_call.setText("重新发送验证码");
                tv_register_sms_call.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
              tv_register_sms_call.setText((millisUntilFinished / 1000) + "后可重新发送验证码");
              tv_register_sms_call.setEnabled(false);
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