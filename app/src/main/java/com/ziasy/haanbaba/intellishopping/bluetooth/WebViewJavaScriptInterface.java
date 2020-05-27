package com.ziasy.haanbaba.intellishopping.bluetooth;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebViewJavaScriptInterface {
    private Context context;
    WebViewResponse response;

    public interface WebViewResponse {
        void onSuccess(String str);
    }

    public WebViewJavaScriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showBalance(String balance) {
        if (this.response != null) {
            this.response.onSuccess(balance);
        }
    }

    @JavascriptInterface
    public void showResponse(String message) {
        if (this.response != null) {
            this.response.onSuccess(message);
        }
    }

    public void callJavascriptfunc(WebViewResponse response) {
        this.response = response;
    }
}
