package tw.com.bussinessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import tw.com.bussinessmeet.Bean.UserInformationBean;
import tw.com.bussinessmeet.DAO.UserInformationDAO;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;

public class SelfIntroductionActivity extends AppCompatActivity {
    private TextView userName,company,position,email,tel;
    private Button editButton;
    private UserInformationDAO userInformationDAO;
    private  DBHelper DH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_introduction);
        userName = (TextView) findViewById(R.id.profile_name);
        company = (TextView) findViewById(R.id.profile_company);
        position = (TextView) findViewById(R.id.profile_position);
        email = (TextView) findViewById(R.id.profile_email);
        tel = (TextView) findViewById(R.id.profile_tel);
        editButton = (Button) findViewById(R.id.editPersonalProfileButton);
        editButton.setOnClickListener(editButtonClick);
        company.append("\n");
        position.append("\n");
        email.append("\n");
        tel.append("\n");
        openDB();
        searchUserInformation();

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                        startActivity(new Intent(getApplicationContext()
                                ,FriendsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public void searchUserInformation(){
        UserInformationBean ufb = new UserInformationBean();
        BlueToothHelper blueToothHelper = new BlueToothHelper(this);
        blueToothHelper.startBuleTooth();
        ufb.setBlueTooth(blueToothHelper.getMyBuleTooth());

//        ufb.setBlueTooth("1");
        Cursor result = userInformationDAO.searchAll(ufb);
        Log.d("result",String.valueOf(result.getColumnCount()));
        Log.d("result",String.valueOf(result.getColumnIndex("user_name")));


        if (result.moveToFirst()) {
            userName.append(result.getString(result.getColumnIndex("user_name")));
            company.append(result.getString(result.getColumnIndex("company")));
            position.append(result.getString(result.getColumnIndex("position")));
            email.append(result.getString(result.getColumnIndex("email")));
            tel.append(result.getString(result.getColumnIndex("tel")));
        }
        result.close();

    }
    public View.OnClickListener editButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToEditIntroductionPage();
        }
    };
    public void changeToEditIntroductionPage(){
        Intent intent = new Intent();
        intent.setClass(SelfIntroductionActivity.this,EditIntroductionActivity.class);
        startActivity(intent);
    }



}
