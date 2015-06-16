package com.optimus.bclass;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SendDanmuActivity extends Activity {
    List<HashMap<String, Object>> danmus;
    protected ListView chatListView = null;
    protected Button chatSendButton = null;
    protected Button chatBackButton = null;
    protected EditText editText = null;
    protected ImageButton addButton = null;
    protected LinearLayout setSizelinearLayout = null;
    protected InputMethodManager inputMethodManager = null;
    protected RadioGroup radioGroup = null;
    protected TextView sampleSizeColor = null;
    // protected TextView chatListText = null;
    // protected LayoutInflater layoutInflater = null;
//    protected PopupWindow popupWindow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_danmu);
        Intent intent = getIntent();
        danmus = new ArrayList<>();
        // layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        chatListView = (ListView) findViewById(R.id.chat_list);
        chatSendButton = (Button) findViewById(R.id.chat_bottom_sendbutton);
        chatBackButton = (Button) findViewById(R.id.chat_back_button);
        addButton = (ImageButton) findViewById(R.id.chat_bottom_add);
        setSizelinearLayout = (LinearLayout) findViewById(R.id.set_size_color);
        editText = (EditText) findViewById(R.id.chat_bottom_edittext);
        //chatListText = (TextView) (layoutInflater.inflate(R.layout.chat_listitem,null).findViewById(R.id.chatlist_text));
        sampleSizeColor = (TextView) findViewById(R.id.sample_size_color);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

//        popupWindow = new PopupWindow((((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.set_size_color, null, false)), ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        final String key = intent.getStringExtra("key");
        final String url = "http://172.18.33.10:3000/shoot";
        final ListViewAdapter listAdapter = new ListViewAdapter(this, danmus);
        chatListView.setAdapter(listAdapter);
        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Editable danmu = editText.getText();
                if (danmu != null && !danmu.toString().equals("")) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("text", danmu.toString());
                    map.put("color", sampleSizeColor.getTextColors());
                    danmus.add(map);
                    listAdapter.notifyDataSetChanged();
                    editText.setText("");
                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            HttpPost post = new HttpPost(url);
                            post.addHeader("Content-Type", "application/json");
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("key", key);
                                jsonObject.put("danmu", danmu.toString());
                                StringEntity entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
                                post.setEntity(entity);
                                HttpClient client = new DefaultHttpClient();
                                HttpParams httpParams = client.getParams();
//                                HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
//                                HttpConnectionParams.setSoTimeout(httpParams, 5000);

                                HttpResponse httpResponse = client.execute(post);
                                if (httpResponse.getStatusLine().getStatusCode() == 200)
                                    Toast.makeText(SendDanmuActivity.this, "send message succeed", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(SendDanmuActivity.this, "send message fail", Toast.LENGTH_SHORT).show();
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (ClientProtocolException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Looper.loop();
                        }
                    }.start();
                }
            }
        });
        chatBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDanmuActivity.this.finish();
            }
        });
        addButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                if (event.getAction() == MotionEvent.ACTION_UP)
                    if (setSizelinearLayout.getVisibility() == View.GONE)
                        setSizelinearLayout.setVisibility(View.VISIBLE);
                    else
                        setSizelinearLayout.setVisibility(View.GONE);
                return false;
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    if (setSizelinearLayout.getVisibility() != View.GONE)
                        setSizelinearLayout.setVisibility(View.GONE);
                return false;
            }
        });
        chatListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    setSizelinearLayout.setVisibility(View.GONE);
                }
                return onTouchEvent(event);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.redRadioButton) {
                    sampleSizeColor.setTextColor(Color.RED);
                } else if (checkedId == R.id.blueRadioButton) {
                    sampleSizeColor.setTextColor(Color.BLUE);
                } else if (checkedId == R.id.greenRadioButton) {
                    sampleSizeColor.setTextColor(Color.GREEN);
                } else if (checkedId == R.id.blackRadioButton) {
                    sampleSizeColor.setTextColor(Color.BLACK);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}