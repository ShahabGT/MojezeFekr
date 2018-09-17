package projects.shahabgt.com.onlinelibrary;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.toolbox.StringRequest;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    MaterialDialog dialog;
    public static String money="";
    public static String number="";
    public static String sid="";
    Button back;
    int c=0;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        back=findViewById(R.id.web_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WebActivity.this, SubjectsActivity.class));
                WebActivity.this.finish();

            }
        });

        webView = findViewById(R.id.web);

        loadWeb("http://mojezeyefekr.ir/onlinelibrary/default.php?money="+money+"&number="+number+"&sid="+sid);

    }
    private void loadWeb (final String address){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("در حال اتصال به درگاه بانکی!")
                .theme(Theme.LIGHT)
                .progress(true,0)
                .progressIndeterminateStyle(true)
                .cancelable(false);
        dialog = builder.build();

        webView.getSettings().setAppCacheEnabled(false);
        webView.clearHistory();
        webView.clearCache(true);
        webView.clearSslPreferences();
        webView.clearFormData();
        webView.clearMatches();
        webView.loadUrl("about:blank");
        webView.reload();
        dialog.show();
        webView.loadUrl(address);
        c=0;

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDatabaseEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {

                view.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                dialog.dismiss();
                if(view.getUrl().endsWith("mojezeyefekr.ir/onlinelibrary/verify.php")){
                    back.setVisibility(View.VISIBLE);

                }
          //      Toast.makeText(WebActivity.this,view.getUrl(),Toast.LENGTH_LONG).show();

            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                swipeRefreshLayout.setRefreshing(false);
                if(c<4) {
                    view.reload();
                    c++;
                }
               // view.loadUrl("file:///android_asset/error.html");
            }
        });
        swipeRefreshLayout = findViewById(R.id.mainSwipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.getSettings().setAppCacheEnabled(false);
                webView.clearCache(true);
                webView.loadUrl("about:blank");
                webView.reload();
                webView.loadUrl(address);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(WebActivity.this,"بازگشت در این مرحله مجاز نیست",Toast.LENGTH_LONG).show();

      //  super.onBackPressed();
    }

    @Override
    protected void onStop() {
        webView.clearHistory();
        webView.clearCache(true);
        webView.clearSslPreferences();
        webView.clearFormData();
        webView.clearMatches();
        webView.destroy();
        super.onStop();
    }
}
