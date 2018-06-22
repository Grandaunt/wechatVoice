package www.branch.com.contentproviderdemo;

import android.Manifest.permission;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class VoiceActivity extends AppCompatActivity implements  ContentDataLoadTask.OnContentDataLoadListener,View.OnClickListener {

    private static final int SPAN_COUNT = 3;
    //APP_ID为自己的hefaID
    private static final  String APP_ID="";
    //IWXAPI是第三方app和微信通信的openapi接口
    private IWXAPI api;
    RecyclerView mRecyclerView;
    private ShowFileAdapter mShowFileAdapter;
    ContentDataLoadTask mContentDataLoadTask;

    ProgressDialog mProgressDialog;
    private Button btn_share;
    private FileSystemType mShowType = FileSystemType.music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        regToWx();
        startLoadData();
        mShowType = FileSystemType.music;
    }

    private void regToWx() {
      //通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this,APP_ID,true);

        //讲应用的appid注册到微信
        api.registerApp(APP_ID);
    }

    private void initView() {
        btn_share = (Button)findViewById(R.id.btn_share);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_voice);
        RecyclerView.LayoutManager layoutManager = null;
        RecyclerviewItemDividerDeoration dividerDeoration = null;
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        dividerDeoration = new RecyclerviewItemDividerDeoration(RecyclerviewItemDividerDeoration.TYPE_VERTICAL, getResources().getDimensionPixelSize(R.dimen.vertical_item_divider_size), getResources().getColor(R.color.divider_color));

        mRecyclerView.addItemDecoration(dividerDeoration);

        mRecyclerView.setLayoutManager(layoutManager);

        mShowFileAdapter = new ShowFileAdapter(this, mShowType);

        mRecyclerView.setAdapter(mShowFileAdapter);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startLoadData();

        }

    }


    private void startLoadData() {

        if (ContextCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            mContentDataLoadTask = new ContentDataLoadTask(this);
            mContentDataLoadTask.setmOnContentDataLoadListener(this);
            mContentDataLoadTask.execute();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }



    @Override
    public void onStartLoad() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.show();

    }

    @Override
    public void onFinishLoad() {
        //sd卡数据搜索加载结束后加载页面控件
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            initView();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContentDataLoadTask != null) {
            mContentDataLoadTask.cancel(true);
            mContentDataLoadTask.setmOnContentDataLoadListener(null);
            mContentDataLoadTask = null;
        }

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
//                share();
                break;
            default:
                break;

        }


    }

//    private void share() {
//        //初始化一个WXMusicObject对象，填写url
//        WXMusicObject music = new WXMusicObject();
//        music.musicUrl="音乐url";
//
//        //用一个WXMusicObject对象初始化一个WXMediaMessage对象，填写标题、描述
//        WXMediaMessage  msg = new WXMediaMessage();
//        msg.mediaObject=music;
//        msg.title = "音乐标题";
//        msg.description="音乐描述";
//        Bitmap thump = BitmapFactory.decodeResource(getResources(),R.drawable.sample_footer_loading);//音乐缩略图
//        msg.thumbData= BmpToByteArray.bmpToByteArray(thump,true);
//
//        //构造一个Req
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("music");//transaction字段用于唯一标识一个请求
//        req.message = msg;
//        req.scene = isTimelineCb.isChecked()?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
//
//        //调用api接口发送数据
//        api.sendReq(req);
//    }

    private String buildTransaction(final String type) { return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis(); }

}
