package freestar.friends.util.profile;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;

public class SetupMessage extends AppCompatActivity {
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.tv_add)
    TextView tvAdd;
    @Bind(R.id.layout_action)
    RelativeLayout layoutAction;
    @Bind(R.id.switch_mu)
    Switch switchMu;
    @Bind(R.id.switch_sy)
    Switch switchSy;
    @Bind(R.id.switch3)
    Switch switch3;
    @Bind(R.id.switch4)
    Switch switch4;
    @Bind(R.id.switch5)
    Switch switch5;
    @Bind(R.id.switch6)
    Switch switch6;
    @Bind(R.id.set_lin)
    LinearLayout setLin;

    private boolean aSwitchmuStart;
    private boolean aswitch_syStart;
    private boolean aswitch3Start;
    private boolean aswitch4Start;
    private boolean aswitch5Start;
    private boolean aswitch6Start;

    private User user;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;

    // LinearLayout linearLayout;
    //private Switch switch_mu,switch_sy,switch3,switch4,switch5,switch6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_message);
        ButterKnife.bind(this);
        getState();
        user = new User();
        user.setObjectId(App.userId);
    }

    private void getState() {
        preferences = getSharedPreferences(App.userId, Context.MODE_PRIVATE);
        aSwitchmuStart = preferences.getBoolean("Switchmu", false);
        aswitch_syStart = preferences.getBoolean("Switchsy", false);
        aswitch3Start = preferences.getBoolean("Switch3", false);
        aswitch4Start = preferences.getBoolean("Switch4", false);
        aswitch5Start = preferences.getBoolean("Switch5", false);
        aswitch6Start = preferences.getBoolean("Switch6", false);

        switchMu.setChecked(aSwitchmuStart);
        switchSy.setChecked(aswitch_syStart);
        switch3.setChecked(aswitch3Start);
        switch4.setChecked(aswitch4Start);
        switch5.setChecked(aswitch5Start);
        switch6.setChecked(aswitch6Start);
    }

    public void btnvbackclick(View view) {
        finish();
    }

    @OnClick({R.id.switch_mu, R.id.switch_sy, R.id.switch3, R.id.switch4, R.id.switch5, R.id.switch6})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_mu:
                if (switchMu.isChecked()) {
                    Toast.makeText(SetupMessage.this, "您在处于免打扰模式", Toast.LENGTH_SHORT).show();
                } else {

                }
                break;
            case R.id.switch_sy:
                if (switchSy.isChecked()) {
                    Toast.makeText(SetupMessage.this, "✪ω✪", Toast.LENGTH_SHORT).show();
                } else {
                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                    vibrator.vibrate(new long[]{0,1000}, -1);
                   // Toast.makeText(SetupMessage.this, "声音关", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch3:
                if (switch3.isChecked()) {
                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                    vibrator.vibrate(new long[]{0,1000}, -1);
                    Toast.makeText(SetupMessage.this, "≖‿≖✧", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(SetupMessage.this, "震动关", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch4:
                if (switch4.isChecked()) {
                   // Toast.makeText(SetupMessage.this, "呼吸灯开", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SetupMessage.this, "∑(っ °Д °;)っ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch5:
                if (switch5.isChecked()) {
                    Toast.makeText(SetupMessage.this, "ヾ(o◕∀◕)ﾉ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SetupMessage.this, "关闭推送(*ﾟ▽ﾟ*)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch6:
                if (switch6.isChecked()) {
                    Toast.makeText(SetupMessage.this, "(,,• ₃ •,,)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SetupMessage.this, "可能会错过重要消息哦", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        edit = preferences.edit();
        if (aSwitchmuStart != switchMu.isChecked()){
            if (switchMu.isChecked()){
                edit.putBoolean("Switchmu", true);
            } else {
                 edit.putBoolean("Switchmu", false);
            }
        }
        if (aswitch_syStart != switchSy.isChecked()){
            if (switchSy.isChecked()){
                edit.putBoolean("Switchsy", true);
            } else {
                edit.putBoolean("Switchsy", false);
            }
        }
        if (aswitch3Start != switch3.isChecked()){
            if (switch3.isChecked()){
                edit.putBoolean("Switch3", true);
            } else {
                edit.putBoolean("Switch3", false);
            }
        }
        if (aswitch4Start != switch4.isChecked()){
            if (switch4.isChecked()){
                edit.putBoolean("Switch4", true);
            } else {
                edit.putBoolean("Switch4", false);
            }
        }
        if (aswitch5Start != switch5.isChecked()){
            if (switch5.isChecked()){
                edit.putBoolean("Switch5", true);
            } else {
                edit.putBoolean("Switch5", false);
            }
        }
        if (aswitch6Start != switch6.isChecked()){
            if (switch6.isChecked()){
                edit.putBoolean("Switch6", true);
            } else {
                edit.putBoolean("Switch6", false);
            }
        }

        edit.commit();
        super.onDestroy();
    }
}
