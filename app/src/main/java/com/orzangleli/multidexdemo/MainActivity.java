package com.orzangleli.multidexdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button1,button2,button3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button1 = (Button)this.findViewById(R.id.button1);
        button2 = (Button)this.findViewById(R.id.button2);
        button3 = (Button)this.findViewById(R.id.button3);

        //顶部显示Snackbar
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "张三：今天晚上干嘛？", Snackbar.LENGTH_LONG)
                        .setAction("关闭", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                CoordinatorLayout.LayoutParams ly = new CoordinatorLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,200);
                ly .gravity = Gravity.TOP;
                snackbar.getView().setLayoutParams(ly);

                snackbar.show();
            }
        });

        //修改Snackbar背景颜色和文字颜色
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "张三：今天晚上干嘛？", Snackbar.LENGTH_LONG)
                        .setAction("关闭", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                //设置动作按钮文本颜色
                snackbar.setActionTextColor(Color.BLACK);
                //设置背景颜色
                snackbar.getView().setBackgroundColor(Color.WHITE);

                Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout)snackbar.getView();
                //设置消息文本字体颜色
                ((TextView)snackbarLayout.findViewById(R.id.snackbar_text)).setTextColor(Color.BLACK);
                //同理可以对动作按钮进行相关设置
//                ((Button)snackbarLayout.findViewById(R.id.snackbar_action)).setBackgroundResource(R.mipmap.ic_launcher);
                snackbar.show();
            }
        });
        //增加内容到Snackbar
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "张三：今天晚上干嘛？", Snackbar.LENGTH_LONG)
                        .setAction("关闭", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                View add_view = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_layout
                        , null);

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.gravity = Gravity.CENTER_VERTICAL; //数字表示新加的布局在SnackBar中的位置，从0开始,取决于你SnackBar里面有多少个子View

                Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout)snackbar.getView();
                snackbarLayout.addView(add_view, 0, p);


                snackbar.show();
            }
        });



       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "张三：今天晚上干嘛？", Snackbar.LENGTH_LONG)
                        .setAction("关闭", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                CoordinatorLayout.LayoutParams lytp = new CoordinatorLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,200);
                lytp .gravity = Gravity.TOP;
                snackbar.getView().setLayoutParams(lytp);

                snackbar.setActionTextColor(Color.BLACK);
                snackbar.getView().setBackgroundColor(Color.WHITE);
                Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout)snackbar.getView();
                ((TextView)snackbarLayout.findViewById(R.id.snackbar_text)).setTextColor(Color.BLACK);
//                ((Button)snackbarLayout.findViewById(R.id.snackbar_action)).setBackgroundResource(R.mipmap.ic_launcher);
                snackbar.setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        Toast.makeText(MainActivity.this,"通过Callback监听到snackbar消失",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                        Toast.makeText(MainActivity.this,"通过Callback监听到snackbar显示",Toast.LENGTH_SHORT).show();
                    }
                });
                snackbar.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
