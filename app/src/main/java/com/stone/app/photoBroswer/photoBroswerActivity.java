package com.stone.app.photoBroswer;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.app.R;
import com.stone.app.Util.ToastUtil;
import com.stone.app.Util.getDataUtil;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.PictureData;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.library.CardAdapter;
import com.stone.app.library.CardSlidePanel;

import java.util.ArrayList;
import java.util.List;


public class photoBroswerActivity extends FragmentActivity implements View.OnClickListener {
    String month = "";
    String day="" ;
    private CardSlidePanel.CardSwitchListener cardSwitchListener;
    //    private String imagePaths[] = {};
    //    private String imagePaths[] = {"file:///android_asset/wall01.jpg",
    //            "file:///android_asset/wall02.jpg", "file:///android_asset/wall03.jpg",
    //            "file:///android_asset/wall04.jpg", "file:///android_asset/wall05.jpg",
    //            "file:///android_asset/wall06.jpg", "file:///android_asset/wall07.jpg",
    //            "file:///android_asset/wall08.jpg", "file:///android_asset/wall09.jpg",
    //            "file:///android_asset/wall10.jpg", "file:///android_asset/wall11.jpg",
    //            "file:///android_asset/wall12.jpg"}; // 12个图片资源
    //    private String names[] = {};
    //    private String imageplaces[] = {};
    //    private String imagetimes[] = {};
    //    private String names[] = {"郭富城", "刘德华", "张学友", "李连杰", "成龙", "谢霆锋", "李易峰",
    //            "霍建华", "胡歌", "曾志伟", "吴彦祖", "梁朝伟"}; // 12个人名
    private DataBaseManager dataBaseManager;
    //    private String imageplaces[] = {"上海", "南京", "北京", "杭州", "温州", "哈尔滨", "广州", "武汉", "云南", "香港", "四川", "新疆"};
    private int circulatetimes;
    private ImageView img_back;
    private int motionType;
    private List<CardDataItem> dataList = new ArrayList<>();
    private List<PictureData> pictlist = null;
    private int lenth=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示图片的时候不要标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photobrowser);

        initdData();
        initView();
    }

    //    数据库初始化
    private void initdData() {
        Intent intent = getIntent();
        dataBaseManager = RealmDB.getDataBaseManager();
        Log.i("TAG", " photoBrowser的dataBaseManager ID: " + dataBaseManager);
        //        dataBaseManager = new DataBaseManager();
        try {
            String memberID = getDataUtil.getmemberID(photoBroswerActivity.this);
            Log.i("TAG", " photoBrowser的memberID: " + memberID);
            pictlist = dataBaseManager.getPictureList("", "", memberID, "", "", 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //                pictlist = dataBaseManager.getPictureList("", "", intent.getStringExtra("memberID"), "", "",0,0);
        /**
         * 第一次修改
         */


//        if (pictlist.size() == 0) {
        /**
         * 第三次修改
         */
        if (pictlist.size() == 0) {
//        if (0== lenth) {
                        ToastUtil.showToast(photoBroswerActivity.this,"图片列表为空");
            ToastUtil.showToast(photoBroswerActivity.this,"图片列表为空");
            Log.i("TAG", "图片列表为空");
            finish();
            //        }else {
            //            int i = 0;
            //            for (PictureData data : pictlist) {
            //
            //                imagePaths[i] = data.getImagePath();
            //                names[i] = data.getName();
            //                imageplaces[i] = data.getLocation();
            //                imagetimes[i] = String.valueOf(data.getDate());
            //                Log.i("TAG","path= " +imagePaths[i]  );
            //                Log.i("TAG","places= " +  imageplaces[i]);
            //                Log.i("TAG","name= " +names[i] );
            //                Log.i("TAG"," imagetimes= " + imagetimes[i]  );
            //                i++;
            //        }

        }
    }

    private void initView() {
        final CardSlidePanel slidePanel = (CardSlidePanel) findViewById(R.id.image_slide_panel);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(photoBroswerActivity.this);
        circulatetimes = 1;
        // 1. 左右滑动监听
        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {

            @Override
            public void onShow(int index) {
                Log.d("Card", "正在显示-" + dataList.get(index).userName);
                /**
                 * 修改2
                 */
//                if (pictlist.size() > 1 && index == (pictlist.size() - 1) * circulatetimes) {
//                if (pictlist.size() > 1 && index == (lenth - 1) * circulatetimes) {
                Log.i("TAG","index=  " +index  +"length= "+lenth);

                if ( index == (lenth - 1)) {

                    appendDataList();//

                    slidePanel.getAdapter().notifyDataSetChanged();
                    //4. 数据更新<
                    circulatetimes++;
                }
            }

            @Override
            public void onCardVanish(int index, int type) {
               // Log.d("Card", "正在消失-" + dataList.get(index).userName + " 消失type=" + type);
                //motionType = type;
            }
        };
        //        slidePanel.vanishOnBtnClick(VANISH_TYPE_LEFT);      按钮的监听
        slidePanel.setCardSwitchListener(cardSwitchListener);
        slidePanel.setLongClickable(true);
        //        slidePanel.setOnTouchListener(photoBroswerActivity.this);

        // 2. 绑定Adapter
        prepareDataList();

        slidePanel.setAdapter(new CardAdapter() {
            @Override
            public int getLayoutId() {
                return R.layout.card_item;
            }

            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public void bindView(View view, int index) {
                Object tag = view.getTag();
                final ViewHolder viewHolder;
                if (null != tag) {
                    viewHolder = (ViewHolder) tag;
                } else {
                    viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);
                }

                //                final int finalIndex = index;
                //                new Handler().postDelayed(new Runnable(){
                //                    public void run() {
                //                        //execute the task
                //
                //                    }
                //                }, 500);
//                if(dataList.get(index).phototime.length()==8){

                    viewHolder.bindData(dataList.get(index));
//                }

            }

            @Override
            public Rect obtainDraggableArea(View view) {
                if (lenth > 1) {
                    View contentView = view.findViewById(R.id.card_item_content);
                    View topLayout = view.findViewById(R.id.card_top_layout);
                    View bottomLayout = view.findViewById(R.id.card_bottom_layout);
                    int left = view.getLeft() + contentView.getPaddingLeft() + topLayout.getPaddingLeft();
                    int right = view.getRight() - contentView.getPaddingRight() - topLayout.getPaddingRight();
                    int top = view.getTop() + contentView.getPaddingTop() + topLayout.getPaddingTop();
                    int bottom = view.getBottom() - contentView.getPaddingBottom() - bottomLayout.getPaddingBottom();
                    Log.i("TAG", "bottom = " + bottom + "view of bottom =" + view.getBottom());
                    return new Rect(left, top, right, bottom);
                }
                // 可滑动区域定制，该函数只会调用一次


                return new Rect(0, 0, 0, 0);
            }
        });


        // 3. notifyDataSetChanged调用
        //        findViewById(R.id.notify_change).setOnClickListener(new View.OnClickListener() {
        //        notify_change.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                appendDataList();
        //                //4. 数据更新<
        //                slidePanel.getAdapter().notifyDataSetChanged();
        //            }
        //        });
    }

    private void prepareDataList() {
        //        int num = imagePaths.length;
        //        int num = imagePaths.length;
        for (int i = 0; i < pictlist.size(); i++) {
            if (!pictlist.get(i).getName().equals("portrait")) {
                CardDataItem dataItem = new CardDataItem();
                dataItem.userName = pictlist.get(i).getName().trim();
                dataItem.imagePath = pictlist.get(i).getImagePath().trim();
                dataItem.imagePlace = pictlist.get(i).getLocation().trim();
                dataItem.phototime = String.valueOf(pictlist.get(i).getDate());
               // dataItem.note= pictlist.get(i).getNote().trim();
                dataList.add(dataItem);

            }
            lenth=dataList.size();
        }
        //        for (PictureData pictureData : pictlist) {
        //
        //        }
        //        for (int i = 0; i < num; i++) {
        //            CardDataItem dataItem = new CardDataItem();
        //            dataItem.userName = names[i];
        //            dataItem.imagePath = imagePaths[i];
        //            dataItem.imagePlace = imageplaces[i];
        //            dataItem.likeNum = (int) (Math.random() * 10);
        //            dataItem.imageNum = (int) (Math.random() * 6);
        //            dataList.add(dataItem);
        //        }

    }

    private void appendDataList() {
        //        for (int i = 0; i < 6; i++) {
        //            CardDataItem dataItem = new CardDataItem();
        //            dataItem.userName = "From Append";
        //            dataItem.imagePath = imagePaths[8];
        //            dataItem.likeNum = (int) (Math.random() * 10);
        //            dataItem.imageNum = (int) (Math.random() * 6);
        //            dataList.add(dataItem);
        //        }
        //        for (PictureData pictureData : pictlist) {
        //            CardDataItem dataItem = new CardDataItem();
        //            dataItem.userName = pictureData.getName().trim();
        //            dataItem.imagePath = pictureData.getImagePath().trim();
        //            dataItem.imagePlace = pictureData.getLocation().trim();
        //            dataItem.phototime = String.valueOf(pictureData.getDate());
        //            dataList.add(dataItem);
        //        }
//        for (int i = 0; i < pictlist.size(); i++) {
//            CardDataItem dataItem = new CardDataItem();
//            dataItem.userName = pictlist.get(i).getName().trim();
//            dataItem.imagePath = pictlist.get(i).getImagePath().trim();
//            dataItem.imagePlace = pictlist.get(i).getLocation().trim();
//            dataItem.phototime = String.valueOf(pictlist.get(i).getDate());
//            dataItem.note= pictlist.get(i).getNote().trim();
//            dataList.add(dataItem);
//
//        }
        /**
         * 第三次修改
         */
        for (int i = 0; i < pictlist.size(); i++) {
            if (!pictlist.get(i).getName().equals("portrait")) {
                CardDataItem dataItem = new CardDataItem();
                dataItem.userName = pictlist.get(i).getName().trim();
                dataItem.imagePath = pictlist.get(i).getImagePath().trim();
                dataItem.imagePlace = pictlist.get(i).getLocation().trim();
                dataItem.phototime = String.valueOf(pictlist.get(i).getDate());
                // dataItem.note= pictlist.get(i).getNote().trim();
                dataList.add(dataItem);

            }
            lenth=dataList.size();
        }
        //        int num = imagePaths.length;
        //        for (int i = 0; i < num; i++) {
        //            CardDataItem dataItem = new CardDataItem();
        //            dataItem.userName = names[i];
        //            dataItem.imagePath = imagePaths[i];
        //            dataItem.imagePlace = imageplaces[i];
        //            dataItem.likeNum = (int) (Math.random() * 10);
        //            dataItem.imageNum = (int) (Math.random() * 6);
        //            dataList.add(dataItem);
        //        }
    }

    //    @Override
    //    public boolean onTouch(View view, MotionEvent motionEvent) {
    //        return false;
    //    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                //gotomypage()   返回我的界面
                //                startActivity(new Intent(photoBroswerActivity.this, MyInformation.class));
                finish();
                break;
        }
    }

    class ViewHolder {
        ImageView imageView;
        View maskView;
        TextView userNameTv;
        TextView tv_time;
        TextView tv_note;
        //        TextView imageNumTv;
        TextView likeNumTv;
        TextView tv_imageplace;

        public ViewHolder(View view) {
            maskView = view.findViewById(R.id.maskView);
            tv_imageplace = (TextView) view.findViewById(R.id.tv_imageplace);
            imageView = (ImageView) view.findViewById(R.id.card_image_view);
            userNameTv = (TextView) view.findViewById(R.id.card_user_name);
            tv_time = (TextView) view.findViewById(R.id.tv_imagetime);
           // tv_note= (TextView) view.findViewById(R.id.tv_note);
        }

        public void bindData(CardDataItem itemData) {

                Glide.with(photoBroswerActivity.this).load(itemData.imagePath.trim()).into(imageView);
                //            userNameTv.setText("姓名：" + itemData.userName);
                userNameTv.setText(itemData.userName);
          //  tv_note.setText(itemData.note);
            tv_imageplace.setText(itemData.imagePlace);
            if(itemData.phototime.length()==8){
                String photoDate = itemData.phototime;
                if(photoDate.charAt(4) == '0'){
                    if(photoDate.charAt(5) != '0'){
                        month=photoDate.charAt(5)+"月";
                    }

                }else {
                    month=photoDate.substring(4,6)+"月";
                }
                //日
                if(photoDate.charAt(6) == '0'){
                    if(photoDate.charAt(7) != '0'){
                        day=photoDate.charAt(5)+"日";
                    }

                }else {
                    day=photoDate.substring(6,8)+"日";
                }
                tv_time.setText(photoDate.substring(0, 4) + "年");
            }else {
                tv_time.setText("");

            }




//            if (month > 10) {
//                if (day > 10) {
//                    //20171112
//                    newdate = "photoDate.substring(0, 4) + \"年\" + photoDate.substring(4, 6) + \"月\" + photoDate.substring(6, 8) + \"日\"";
//                } else if (day < 10) {
//                    if (day == 0) {
//                        //201711
//                        newdate = "photoDate.substring(0, 4) + \"年\" + photoDate.substring(4, 6) + \"月\"";
//                    }
//                    //2017.11.8
//                    newdate = "photoDate.substring(0, 4) + \"年\" + photoDate.substring(4, 6) + \"月\" + photoDate.substring(7, 8) + \"日\"";
//
//                }
//            } else if (month < 10) {
//                if (month == 0) {
//
//                    if (day > 10) {
//                        //2017.11
//                        newdate = "photoDate.substring(0, 4) + \"年\"+ p+photoDate.substring(6, 8) + \"日\"";
//                    } else {
//                        if (day == 0) {
//                            newdate = "photoDate.substring(0, 4) + \"年\"";
//                        }
//                        newdate = "photoDate.substring(0, 4) + \"年\"+ p+photoDate.substring(7, 8) + \"日\"";
//                    }
//                }
//                if (day > 10) {
//                    //2017.8.18
//                    newdate = "photoDate.substring(0, 4) + \"年\"+ photoDate.substring(4, 5) + \"月\"+photoDate.substring(6, 8) + \"日\"";
//                } else {
//                    if (day == 0) {
//
//                        newdate = "photoDate.substring(0, 4) + \"年\" + photoDate.substring(5, 6) + \"月\"";//2017.8
//                    } else {
//                        newdate = "photoDate.substring(0, 4) + \"年\" + photoDate.substring(5, 6) + \"月\"+photoDate.substring(7, 8) + \"日\"";
//                        //2017.6.8
//                    }
//                }
//            }
//            tv_time.setText(newdate);
            //            if(photoDate.charAt(6) == '0'){
            //                    if(photoDate.charAt(4) == '0'){
            //                            tv_time.setText(photoDate.substring(0, 4) + "年");
            //                    }else {
            //
            //                    }
            //
            //            }

//            if (photoDate.length() == 8) {
//                if (photoDate.charAt(4) == '0') {
//
//                    if (photoDate.charAt(6) == '0') {
//                        tv_time.setText(photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月" + photoDate.substring(7, 8) + "日");
//                    } else {
//                        tv_time.setText(photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月" + photoDate.substring(6, 8) + "日");
//                    }
//                } else {
//                    if (photoDate.charAt(6) == '0') {
//                        if (photoDate.charAt(7) == '0') {
//
//                        }
//                        tv_time.setText(photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月" + photoDate.substring(7, 8) + "日");
//                    }
//
//                    tv_time.setText(photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月" + photoDate.substring(6, 8) + "日");
//                }
//            } else if (photoDate.length() == 6) {
//                if (photoDate.charAt(4) == '0') {
//                    tv_time.setText(photoDate.substring(0, 4) + "年" + photoDate.substring(5, 6) + "月");
//                } else {
//                    tv_time.setText(photoDate.substring(0, 4) + "年" + photoDate.substring(4, 6) + "月");
//
//                }
//            } else if (photoDate.length() == 4) {
//                tv_time.setText(photoDate.substring(0, 4) + "年");
//            } else if (photoDate.length() < 4) {
//                tv_time.setText("");
//            }

            //            tv_imageplace.setText("拍摄地：" + itemData.imagePlace + "");
        }
    }

}
