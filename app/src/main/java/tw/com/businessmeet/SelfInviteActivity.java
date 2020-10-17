package tw.com.businessmeet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.adapter.ProfileTimelineRecyclerViewAdapter;
import tw.com.businessmeet.adapter.SelfInviteRecyclerViewAdapter;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.AvatarHelper;

import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.ActivityInviteService;
import tw.com.businessmeet.service.Impl.ActivityInviteServiceImpl;


public class SelfInviteActivity extends AppCompatActivity implements ProfileTimelineRecyclerViewAdapter.ClickListener {
    private Toolbar toolbar;
    private BottomNavigationView menu;
    private ImageView avatar;
    private UserInformationDAO userInformationDAO;
    private ActivityInviteServiceImpl activityInviteService = new ActivityInviteServiceImpl();
    private BlueToothHelper blueToothHelper;
    private DBHelper DH = null;
    private RecyclerView recyclerViewSelfInvite;
    private List<ActivityInviteBean> activityInviteBeanList = new ArrayList<>();
    private SelfInviteRecyclerViewAdapter selfInviteRecyclerViewAdapter;


    private AsyncTasKHelper.OnResponseListener<ActivityInviteBean, List<ActivityInviteBean>> searchResponseListener = new AsyncTasKHelper.OnResponseListener<ActivityInviteBean, List<ActivityInviteBean>>() {
        @Override
        public Call<ResponseBody<List<ActivityInviteBean>>> request(ActivityInviteBean... activityInviteBeans) {
            return activityInviteService.search(activityInviteBeans[0]);
        }

        @Override
        public void onSuccess(List<ActivityInviteBean> activityInviteBeanList) {
            for (ActivityInviteBean activityInviteBean : activityInviteBeanList) {
                selfInviteRecyclerViewAdapter.dataInsert(activityInviteBean);
            }
        }

        @Override
        public void onFail(int status, String message) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_invite);
        menu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        blueToothHelper = new BlueToothHelper(this);
        recyclerViewSelfInvite = findViewById(R.id.invite_view);
        ActivityInviteBean activityInviteBean = new ActivityInviteBean();
        activityInviteBean.setUserId(blueToothHelper.getUserId());
        activityInviteBean.setStatus(1);
        AsyncTasKHelper.execute(searchResponseListener,activityInviteBean);
        openDB();
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbarMenu
        toolbar.inflateMenu(R.menu.toolbarmenu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }

        });

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //searchUserInformation();
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        Menu BVMenu = bottomNavigationView.getMenu();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        createRecyclerViewSelfInvite();
        AvatarHelper avatarHelper = new AvatarHelper();
        Cursor result = userInformationDAO.getById(blueToothHelper.getUserId());

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));
    }



    //Perform ItemSelectedListener
    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menu_home:
                            return true;
                        case R.id.menu_search:
                            startActivity(new Intent(getApplicationContext()
                                    ,SearchActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.menu_friends:
                            //menuItem.setIcon(R.drawable.ic_people_blue_24dp);
                            startActivity(new Intent(getApplicationContext()
                                    ,FriendsActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }


            });

    private void openDB() {
        Log.d("add", "openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }


    @Override
    public void onClick(View view, int position) {

    }

    private void createRecyclerViewSelfInvite() {
        recyclerViewSelfInvite.setLayoutManager(new LinearLayoutManager(this));
        selfInviteRecyclerViewAdapter = new SelfInviteRecyclerViewAdapter(this,activityInviteBeanList);
        recyclerViewSelfInvite.setAdapter(selfInviteRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewSelfInvite.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewSelfInvite.addItemDecoration(dividerItemDecoration);
    }

}
