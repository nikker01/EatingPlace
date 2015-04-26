package application.utility;

import android.app.Activity;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Henry on 2015/4/26.
 */
public class HttpMakecall {

    private static String serverResponse = "No Response";
    private final static String NULL_EXCEPTION = "Null Exception";
    public static  boolean isDownloadSucceed = false;

    private static final int CONNECTION_TIMEOUT = 30000;

    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(200);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(replyString);

        // trim the whitespaces
        return replyString.trim();
    }

    public static String get(String url, String[] parameter, String[] data, Activity activity)
    {
        Log.e("=======", "Http Get");

        String URL = null;

        if(parameter == null)
        {
            URL = url;
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            int length = parameter.length;

            try {
                for(int i=0; i<length; i++)
                {
                    if(i == 0)
                        sb.append("?"+parameter[0]+"="+ URLEncoder.encode(data[0], "UTF-8"));
                    else
                        sb.append("&"+parameter[i]+"="+URLEncoder.encode(data[i], "UTF-8"));
                }
                URL = url + sb.toString();
                //Log.e("URL", URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try
        {
            HttpParams httpParam = setConnectionTimeOut();
            HttpClient httpClient = new DefaultHttpClient(httpParam);
            HttpResponse httpResponse = null;

            HttpGet httpGet = new HttpGet(URL);
            // add cookies
            //Cookie.AddCookies(activity, httpGet);

            httpResponse = httpClient.execute(httpGet);

            return EntityUtils.toString(httpResponse.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String post(String url, String[] parameter, String[] data, Activity activity)
    {
        Log.e("=======","Http Post");

        int length = parameter.length;

        ArrayList<NameValuePair> pair = new ArrayList<NameValuePair>();
        for(int i=0; i<length; i++)
        {
            pair.add(new BasicNameValuePair(parameter[i], data[i]));
        }

        try
        {
            HttpParams httpParam = setConnectionTimeOut();
            HttpClient httpClient = new DefaultHttpClient(httpParam);
            HttpResponse httpResponse = null;

            HttpPost httpPost = new HttpPost(url);
            // add cookies
            //Cookie.AddCookies(activity, httpPost);

            if (pair != null) {
                for (int i=0; i<length; i++) {
                    String headerNameTmp = parameter[i];
                    String headerValueTmp = data[i];

                    httpPost.setHeader(headerNameTmp, headerValueTmp);
                }
            }

            httpPost.setEntity(new UrlEncodedFormEntity(pair, "UTF-8"));
            httpResponse = httpClient.execute(httpPost);

            return EntityUtils.toString(httpResponse.getEntity());

        } catch (Exception e) {
            Log.d("http_error","http error :"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /** Time out parameter of Connection methods **/
    private static HttpParams setConnectionTimeOut() {
		/* Setup the timeout */
        HttpParams httpParameters = new BasicHttpParams();

		/*
		 * Set the timeout in milliseconds until a connection is established.
		 * The default value is zero, that means the timeout is not used.
		 */
        int timeoutConnection = CONNECTION_TIMEOUT;

        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        return httpParameters;
    }

    /** Use to Print Exception **/
    private static String printException(Exception exception) {
        String exceptionMessage = NULL_EXCEPTION;

        // if (exception != null) {
        // exceptionMessage = exception.getMessage().toString();
        // }

        return exceptionMessage;
    }

}

