package co.martinbrown.example.httpget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class HttpGetExample extends Activity {

    WebView browser;
    String toast;
    TextView responseText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        browser = (WebView) findViewById(R.id.browser);
        responseText = (TextView) findViewById(R.id.textView1);

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    executeHttpGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void executeHttpGet() throws Exception {
        BufferedReader in = null;

        try {

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("http://mobile.cs.fsu.edu/androids"));
            HttpResponse response = client.execute(request);

            final int statusCode = response.getStatusLine().getStatusCode();

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    responseText.setText("" + statusCode);
                }

            });

            if(statusCode == 200) {

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");

                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }

                in.close();

                browser.loadData(sb.toString(), "text/html", "UTF-8");
            }

        }
        finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}