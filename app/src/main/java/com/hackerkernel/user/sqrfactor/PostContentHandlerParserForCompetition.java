package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class PostContentHandlerParserForCompetition {

    Context context;
    String content;
    LinearLayout linearLayout;
    TextView fullPostDescription;
    public PostContentHandlerParserForCompetition(Context context, String content , LinearLayout linearLayout, TextView fullPostDescription){
        this.context=context;
        this.content=content;
        this.linearLayout=linearLayout;
        this.fullPostDescription=fullPostDescription;

    }

    @SuppressLint("ResourceAsColor")
    public void setContentToView(){
        Document doc = Jsoup.parse(content);

        Elements elements = doc.getAllElements();

        for(Element element :elements){
            Tag tag = element.tag();

            if(tag.getName().equalsIgnoreCase("a")){
                String name  = element.html();
                //String heading = element.select(tag.getName().toString()).text();
                Log.v("des1",name);
                if(name.contains("span")||name.contains("<i>")||name.contains("<b>"))
                {
                    continue;
                }
                else {
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setTextSize(20);
                    textView.setTextColor(Color.RED);
                    textView.setText(name);
                    textView.setPadding(5, 0, 5, 0);
                    linearLayout.addView(textView);
                }

            }

            else if(tag.getName().equalsIgnoreCase("h3")){
                String title  = element.html();
                //String heading = element.select(tag.getName().toString()).text();
                String[] parsedTitle=title.split("<|>");
                Log.v("des2",title);
                StringBuilder builder = new StringBuilder();

                for(String s:parsedTitle)
                {
                    if(s.equals("b")||s.equals("/b")||s.equals("/span")||s.equals(":")||s.equals("i")||s.equals("br")||s.equals(".")||s.equals("/i"))
                    {
                        continue;
                    }
                    else{
                        // Log.v("des3",s);
                        builder.append(s+".");
                    }
                }


                if(title.contains("&nbsp")||title.contains("href")||title.equals("<br>"))
                {
                    continue;
                }

                else {
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setTextSize(20);
                    textView.setTextColor(Color.BLACK);
                    textView.setText(builder.toString());
                    textView.setPadding(8, 5, 8, 5);
                    linearLayout.addView(textView);
                    continue;
                }

            }

            else if(tag.getName().equalsIgnoreCase("p")){

                element.select("img").remove();
                String body= element.html();

                String[] parsedBody=body.split("&nbsp;|>|<|<i|\\.");
                StringBuilder builder = new StringBuilder();
                for(String s : parsedBody) {
                   // Log.v("des3",s);
                    if(s.equals("\\.")||s.equals("b")||s.equals("/b")||s.equals("/span")||s.equals(":")||s.equals("i")||s.equals("br")||s.equals(".")||s.equals("/i")||s.contains("style=")||s.contains("&nbsp")||s.contains("<span")||s.contains("</span>")||s.contains("<br>"))
                    {
                        continue;
                    }
                    else{
                        Log.v("des3",s);
                        builder.append(s+".");
                    }

                }
                String str = builder.toString();

                str = str.replaceAll("\\.+"," ");



                Log.v("strringPrint123",str);

                if(body.contains("href")||body.equals("<br>"))
                {
                    continue;
                }
                else {

                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    textView.setTextColor(Color.BLACK);
                    if(str.length()<25)
                    {
                        textView.setTextSize(20);
                        textView.setText(str);
                    }
                    else {
                        textView.setTextSize(16);
                        textView.setText(str+".");
                    }


                    textView.setPadding(8, 10, 8, 10);
                    linearLayout.addView(textView);
                    continue;
                }


            }
            else if (tag.getName().equalsIgnoreCase("img")){
                String url  = element.select("img").attr("src");
                Log.v("des4",url);

                final ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setAdjustViewBounds(true);
                imageView.requestLayout();
                Glide.with(context).load(url)
                        .into(imageView);
                linearLayout.addView(imageView);
                continue;
            }
            else if (tag.getName().equalsIgnoreCase("iframe")){
                String url  = element.select("iframe").attr("src");
                Log.v("des5",url);

                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = (FrameLayout) inflater.inflate(R.layout.fullpostwebview, null);
                WebView wv = (WebView)v.findViewById(R.id.webView1);
                WebSettings settings = wv.getSettings();
                wv.getSettings().setJavaScriptEnabled(true);
                wv.setWebViewClient(new WebViewClient());
                wv.setWebChromeClient(new WebChromeClient());
                wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                wv.getSettings().setDefaultFontSize(18);

                wv.getSettings().setPluginState(WebSettings.PluginState.ON);
                wv.getSettings().setLoadWithOverviewMode(true);

                String[] stringId=url.split("/");
                String id=stringId[stringId.length-1];
                String src1="src="+'"'+"https://www.youtube.com/embed/"+id+'"';
                String html="<iframe width=\"100%\" height=\"300\" "+src1+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
                //String src1="src="+'"'+url+'"';
                // String html="<iframe width=\"100%\" height=\"200\" "+src1+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
                //wv.loadDataWithBaseURL(url,html, "text/html", "UTF-8", null);
                wv.loadData(html, "text/html", "UTF-8");

                v.getRootView();
                linearLayout.addView(v);
                v.setVisibility(View.VISIBLE);
                continue;
            }



        }
    }

    public static String removeConsecutive(String str, char remove) {
        StringBuilder sb = new StringBuilder();
        for(char c : str.toCharArray()) {
            int length = sb.length();
            if(c != remove || length == 0 || sb.charAt(length - 1) != c) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
