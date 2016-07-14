SnackbarCustomUsage is a project which have highly custom snackbar usage:show snackbar on the top of the mobile, custom the color of message text and the background, and add a view to snackbar.

Snackbar 使用详解及源码解析

本文全部内容为orzangleli原创，未经允许禁止转载。

简介：本文将详细介绍Snackbar的使用方法，源码解析以及如何不修改源码的情况下完成Snackbar的顶部显示。

1. 什么是Snackbar?

官网上是这么解释Snackbar的功能和特点：

Snackbars provide lightweight feedback about an operation. They show a brief message at the bottom of the screen on mobile and lower left on larger devices. Snackbars appear above all other elements on screen and only one can be displayed at a time.

They automatically disappear after a timeout or after user interaction elsewhere on the screen, particularly after interactions that summon a new surface or activity. Snackbars can be swiped off screen.

Snackbars can contain an action which is set via setAction(CharSequence, android.view.View.OnClickListener).

To be notified when a snackbar has been shown or dismissed, you can provide a Snackbar.Callback via setCallback(Callback).

Snackbar是一个轻量级的交互反馈控件，可以在手机的屏幕下方或者更大设备（例如平板）的左下方显示一个简短的信息，Snackbar会显示在所有元素之上，而且只持续一段时间。

它们会在超时或者用户与交互屏幕其他地方交互后自动消失，特别是在和一个新的surface和activity交互之后。Sanckbar可以被滑动移除屏幕。

为了知道何时snackbar已经被显示或者关闭，你可以通过setCallback(Callback)提供一个Snackbar.Callback（反馈）。

一般意义上，Snackbar的效果如图所示（图片摘自博客）。

http://img.blog.csdn.net/20150114103321515

2. 怎么使用Snackbar

1). 常量

int LENGTH_INDEFINITE 无限期的显示snackbar (这里只是说不会自动消失，用户仍可通过右滑移除)

int LENGTH_LONG 长时间的显示snackbar (类似于Toast)

int LENGTH_SHORT 短时间的显示snackbar (类似于Toast)

2). 内部类

Snackbar.Callback是Snackbar的内部类，这个类有两个方法需要重写，onDismissed 和 onShown ，下面的例子一看就明白：

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
3). 公开方法



重点说下几个方法： 1.make(View view, CharSequence text, int duration) 和 make(View view, int resId, int duration) 实际上是两种初始化Snackbar的方法，只不过第二种使用的是text的id而已。

2.setAction(CharSequence text, View.OnClickListener listener) 是给Snackbar增加一个动作按钮，按钮的响应用过OnClickListenr重写。最典型的用途是，在用户完成一个操作时，Snackbar提示用户操作成功，按钮显示撤销操作。

3.setActionTextColor(int color) 动作按钮的文本颜色可以设置，默认的颜色是主题的colorAccent颜色。

4）.使用Snackbar

使用SnackBar非常简单，代码如下：

                Snackbar snackbar = Snackbar.make(view, "张三：今天晚上干嘛？", Snackbar.LENGTH_LONG)
                    .setAction("关闭", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {

                        }
                    });
4.阅读源码并自定义Snackbar

1）. 右滑的说明

补充说明下：Snackbar 只能在CoordinatorLayout布局下才能执行右滑删除，在其他布局下右滑手势无效。这一点可以从下面代码中得到解释。

在使用make方法构造Snackbar时，会调用下面的方法：

@NonNull
public static Snackbar make(@NonNull View view, @NonNull CharSequence text,
        @Duration int duration) {
    Snackbar snackbar = new Snackbar(findSuitableParent(view));
    snackbar.setText(text);
    snackbar.setDuration(duration);
    return snackbar;
}
其中findSuitableParent(view)是一个寻找父布局的方法，它会在寻找到CoordinatorLayout或者id为android.R.id.content的FrameLayout时停止寻找，否则就返回默认的布局。

private static ViewGroup findSuitableParent(View view) {
    ViewGroup fallback = null;
    do {
        if (view instanceof CoordinatorLayout) {
            // We've found a CoordinatorLayout, use it
            return (ViewGroup) view;
        } else if (view instanceof FrameLayout) {
            if (view.getId() == android.R.id.content) {
                // If we've hit the decor content view, then we didn't find a CoL in the
                // hierarchy, so use it.
                return (ViewGroup) view;
            } else {
                // It's not the content view but we'll use it as our fallback
                fallback = (ViewGroup) view;
            }
        }

        if (view != null) {
            // Else, we will loop and crawl up the view hierarchy and try to find a parent
            final ViewParent parent = view.getParent();
            view = parent instanceof View ? (View) parent : null;
        }
    } while (view != null);

    // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
    return fallback;
}
而且监听右滑操作的Behavior是继承自SwipeDismissBehavior，而SwipeDismissBehavior继承自CoordinatorLayout.Behavior，所以只有在CoordinatorLayout布局下的Snackbar才能被监听到右滑事件。

2）.自定义Snackbar

看源码后会发现Snackbar预留出的接口方法太少了，不足以满足日常需求，现在我就几个常见的需求做单独说明。

a. Snackbar顶部显示

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
b.修改Snackbar背景颜色和文字颜色

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
            //((Button)snackbarLayout.findViewById(R.id.snackbar_action)).setBackgroundResource(R.mipmap.ic_launcher);
            snackbar.show();
        }
    });
c.增加内容到Snackbar

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
效果图如图所示。

https://github.com/hust201010701/SnackbarCustomUsage/blob/master/demo.gif

写在最后

本文使用的代码在github上可以获取到，地址：

https://github.com/hust201010701/SnackbarCustomUsage/
