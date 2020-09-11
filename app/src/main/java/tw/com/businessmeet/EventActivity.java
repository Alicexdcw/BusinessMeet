package tw.com.businessmeet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;

import retrofit2.Call;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;

public class EventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TimelineServiceImpl timelineService = new TimelineServiceImpl();
    private TextView event,eventDate,eventTime,eventLocation,eventParticipant,addEventMemo,eventTag;
    private ImageView participantAvatar,tagIcon,participantIcon;
    private AvatarHelper avatarHelper;
    private AsyncTasKHelper.OnResponseListener<Integer, TimelineBean> timelineBeanOnResponseListener = new AsyncTasKHelper.OnResponseListener<Integer, TimelineBean>() {
        @Override
        public Call<ResponseBody<TimelineBean>> request(Integer... timelineNos) {
            return timelineService.getById(timelineNos[0]);
        }

        @Override
        public void onSuccess(TimelineBean timelineBean) {
            System.out.println(timelineBean.getCreateDateStr());
            event.setText(timelineBean.getTitle());
            eventLocation.setText(timelineBean.getPlace());
            addEventMemo.setText(timelineBean.getRemark());
            if(timelineBean.getTimelinePropertiesNo() == 1){
                eventDate.setText(timelineBean.getStartDate());
                eventTime.setText(timelineBean.getEndDate());
                eventTag.setText(timelineBean.getActivityLabelBeanList().get(0).getContent());
                eventParticipant.setText(timelineBean.getActivityInviteBeanList().get(0).getUserId());
                participantAvatar.setImageBitmap(avatarHelper.getImageResource(timelineBean.getActivityInviteBeanList().get(0).getAvatar()));
            }else{
                tagIcon.setVisibility(View.GONE);
                eventTag.setVisibility(View.GONE);
                eventTime.setVisibility(View.GONE);
                participantIcon.setVisibility(View.GONE);
                participantAvatar.setVisibility(View.GONE);
                eventParticipant.setVisibility(View.GONE);
                eventDate.setText(timelineBean.getCreateDateStr());
            }
        }

        @Override
        public void onFail(int status, String message) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);

        Integer timelineNo = Integer.parseInt(getIntent().getStringExtra("timelineNo"));

        event = findViewById(R.id.event);
        eventDate = findViewById(R.id.event_date);
        eventTime = findViewById(R.id.event_time);
        eventLocation = findViewById(R.id.event_location);
        eventParticipant = findViewById(R.id.event_participant);
        eventTag = findViewById(R.id.event_tag);
        addEventMemo = findViewById(R.id.add_event_memo);
        participantAvatar = findViewById(R.id.participant_avatar);
        participantIcon = findViewById(R.id.participant_icon);
        tagIcon = findViewById(R.id.tag_icon);
        AsyncTasKHelper.execute(timelineBeanOnResponseListener,timelineNo);

        toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        toolbar.inflateMenu(R.menu.event_toolbarmenu);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_16dp);  //back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //do back
                }
            });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

                @Override
                public boolean onMenuItemClick(MenuItem item) { //偵測按下去的事件
                    return false;
                }

        });


    }
}
