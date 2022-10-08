package com.sand.apm.multidragrcv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sand.apm.multidragrcv.custom.DownRelativeLayout;
import com.sand.apm.multidragrcv.custom.DragImageView;
import com.sand.apm.multidragrcv.custom.EventHelper;
import com.sand.apm.multidragrcv.custom.TestBean;
import com.sand.apm.multidragrcv.custom.TouchHorizontalScrollView;
import com.sand.apm.multidragrcv.custom.TouchLayout;
import com.sand.apm.multidragrcv.custom.TouchRecylerView;
import com.sand.apm.multidragrcv.databinding.ActivityMainBinding;
import com.sand.apm.multidragrcv.databinding.PageListGroupBinding;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String tag="drag";
    ActivityMainBinding binding;
    ArrayList<TouchRecylerView> touchRecylerViews;
    TouchLayout touchLayout;
    RelativeLayout relativeLayoutTeachRight;
    TouchHorizontalScrollView horizontalScrollView;
    DragImageView drageImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        touchLayout = binding.absLayout;
        relativeLayoutTeachRight = binding.relativeLayoutTeachRight;
        horizontalScrollView=binding.horizontalScrollView;
        drageImageView=binding.imageViewDrag;
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        touchRecylerViews = new ArrayList<>();
        int addRcvCount = 5;//设置5个组演示拖放
        for (int i = 0; i < addRcvCount; i++) {
            PageListGroupBinding pageListGroupBinding = PageListGroupBinding.inflate(getLayoutInflater());
            pageListGroupBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            String groupTitle ="组(RecyclerView)" + i;
            pageListGroupBinding.recyclerView.setAdapter(createFakeAdapter(pageListGroupBinding, groupTitle +"--data--"+ i));
            pageListGroupBinding.recyclerView.setTitle(groupTitle);
            pageListGroupBinding.textViewTitle.setText(groupTitle);

            binding.linearLayoutChilds.addView(pageListGroupBinding.getRoot());
            touchRecylerViews.add(pageListGroupBinding.recyclerView);
        }

        touchLayout.setOnTouchUpListener(new TouchLayout.OnTouchUpAndScrollListener() {
            @Override
            public void onTouchUp(int currentMoveX, int downLocationX, View downView, TouchRecylerView sourceTouchRecylerView) {
                horizontalScrollView.stopScroll();

                TouchRecylerView tarGettouchRecylerViewHover=findCurrentHoverRecylerView(sourceTouchRecylerView,drageImageView.getLocationOnScreenX()+downLocationX,touchRecylerViews);
                if(sourceTouchRecylerView!=tarGettouchRecylerViewHover){
                    if(null!=tarGettouchRecylerViewHover){
                        Log.d(tag, " 拖放=>"+tarGettouchRecylerViewHover.getTitle());
                        sourceTouchRecylerView.moveDataTo(tarGettouchRecylerViewHover);//拖放的数据转移
                        Toast.makeText(MainActivity.this, sourceTouchRecylerView.getTestBean().getText()+
                                " 已经拖放到:"+tarGettouchRecylerViewHover.getTitle()+" 数据默认放到最后了", Toast.LENGTH_SHORT
                        ).show();
                    }
                }
                unMarkAll(touchRecylerViews);
            }

            @Override
            public void onScrollMotion(int currentMoveX, int downLocationX, TouchLayout touchLayout, View downView, ImageView dragImageView, TouchRecylerView sourceTouchRecylerView) {
                if(drageImageView.getRight()>touchLayout.getWidth()){
                    horizontalScrollView.scroll2Left();
                }else if(drageImageView.getLeft()<0){
                    horizontalScrollView.scroll2Right();
                }
                TouchRecylerView touchRecylerViewHover=findCurrentHoverRecylerView(sourceTouchRecylerView,drageImageView.getLocationOnScreenX()+downLocationX,touchRecylerViews);
                if(null!=touchRecylerViewHover){
                    Log.d( tag," 拖放=>"+touchRecylerViewHover.getTitle());
                }
            }
        });
    }



    TouchRecylerView findCurrentHoverRecylerView(TouchRecylerView sourceTouchRecylerView, int pointerX, ArrayList<TouchRecylerView> arrayListTouchRecylerViews){
        TouchRecylerView targetTouchRecylerView=null;
        for(TouchRecylerView touchRecylerView:arrayListTouchRecylerViews){
            if(touchRecylerView.containsPointer(pointerX)){
                targetTouchRecylerView= touchRecylerView;
                if(touchRecylerView==sourceTouchRecylerView){
                    touchRecylerView.markStart();
                }else {
                    touchRecylerView.markDragIn();
                }
            }else {
                touchRecylerView.unMark();
            }
        }
        return targetTouchRecylerView;
    }

    public void unMarkAll(ArrayList<TouchRecylerView> arrayListTouchRecylerViews){
        for(TouchRecylerView touchRecylerView:arrayListTouchRecylerViews){
            touchRecylerView.unMark();
        }
    }

    private CommonAdapter<TestBean> createFakeAdapter(PageListGroupBinding pageList,String prefix) {
        CommonAdapter<TestBean> ada = new CommonAdapter<TestBean>(this, R.layout.item_layout, createFakeData(4, prefix)) {
            @Override
            protected void convert(ViewHolder holder, TestBean testBean, int position) {
                holder.setText(R.id.textViewName, testBean.getText());
                if (position % 2 == 1) {
                    holder.getConvertView().setBackgroundResource(R.drawable.item_group_bg);
                } else {
                    holder.getConvertView().setBackground(null);
                }

                //处理拖拽事件的重点代码
                DownRelativeLayout downRelativeLayout = (DownRelativeLayout) holder.getConvertView();
                downRelativeLayout.setDrawingCacheEnabled(true);
                downRelativeLayout.setOnLongClickListener(v -> {
                    DownRelativeLayout touchingView = (DownRelativeLayout) v;
                    Bitmap dmap = touchingView.getDrawingCache();
                    if (null != dmap) {
                        binding.imageViewDrag.setImageBitmap(dmap);
                        binding.imageViewDrag.setVisibility(View.VISIBLE);
                        pageList.recyclerView.setTestBean(testBean);
                        //把relativeLayoutTeachRight事件转移到另一个touchLayout,这是一句价值10万的代码，极大的简化多个View的事件拦截机制
                        EventHelper.giveFistTargetForViewGroup(touchLayout, relativeLayoutTeachRight);
                        //精确的设置拖拽起始点
                        int[] locaitons = touchLayout.setDownLocation(touchingView.getLongClickX(), touchingView.getLongClickY(), touchingView, pageList.recyclerView);
                    }
                    return true;
                });

            }
        };
        return ada;
    }


    Random random=new Random();
    public ArrayList<TestBean> createFakeData(int count, String preFix) {
        ArrayList<TestBean> fakeDdatas = new ArrayList<>();
        int finalCount=1+random.nextInt(count);
        for (int i = 0; i < finalCount; i++) {
            TestBean testBean = new TestBean(preFix + String.valueOf(i));
            fakeDdatas.add(testBean);
        }
        return fakeDdatas;
    }

}