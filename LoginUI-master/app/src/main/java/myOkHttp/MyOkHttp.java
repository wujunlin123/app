package myOkHttp;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyOkHttp {
        private static MyOkHttp myOkHttpClient;
        private OkHttpClient okHttpClient;
        private Handler handler;

        private MyOkHttp() {
            okHttpClient = new OkHttpClient();
            handler = new Handler(Looper.getMainLooper());
        }

        public static MyOkHttp getInstance() {
            if (myOkHttpClient == null) {
                synchronized (MyOkHttp.class) {
                    if (myOkHttpClient == null) {
                        myOkHttpClient = new MyOkHttp();
                    }
                }
            }

            return myOkHttpClient;
        }

        class StringCallBack implements Callback {
            private HttpCallBack httpCallBack;
            private Request request;

            public StringCallBack(Request request, HttpCallBack httpCallBack) {
                this.request = request;
                this.httpCallBack = httpCallBack;
            }

            @Override
            public void onFailure(Call call, IOException e) {
                final IOException fe = e;
                if (httpCallBack != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpCallBack.onError(request, fe);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (httpCallBack != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpCallBack.onSuccess(request, result);
                        }
                    });
                }
            }
        }

        public void asyncGet(String url, HttpCallBack httpCallBack) {
            Request request = new Request.Builder().url(url).build();
            okHttpClient.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
        }


        public void asyncPost(String url, FormBody formBody, HttpCallBack httpCallBack) {
            Request request = new Request.Builder().url(url).post(formBody).build();
            okHttpClient.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
        }

        public interface HttpCallBack {
            void onError(Request request, IOException e);

            void onSuccess(Request request, String result);
        }
    }