package com.sae.raz.ui.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sae.raz.AppConstant;
import com.sae.raz.AppPreference;
import com.sae.raz.R;
import com.sae.raz.database.DBManager;
import com.sae.raz.model.BeaconModel;
import com.sae.raz.service.BeaconMonitoringService;
import com.sae.raz.ui.dialog.QuestionDialog;
import com.sae.raz.ui.fragment.DiscoverFragment;
import com.sae.raz.ui.fragment.ProfileFragment;
import com.sae.raz.ui.fragment.WorkoutLogFragment;

import java.util.ArrayList;


public class MainActivity extends BaseFragmentActivity implements View.OnClickListener{

    public static final int FRAGMENT_DISCOVER = 0;
    public static final int FRAGMENT_WORKOUT = 1;
    public static final int FRAGMENT_ACCOUNT = 2;

    // Constants
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    private DrawerLayout mDrawer;
    public TextView txt_username;

    // Navigation UI
    View layer_nav_logo;
    View layer_nav_discover;
    ImageView img_nav_discover;
    TextView txt_nav_discover;
    View layer_nav_workout;
    ImageView img_nav_workout;
    TextView txt_nav_workout;
    View layer_nav_account;
    ImageView img_nav_account;
    TextView txt_nav_account;
    View layer_logout;

    // Tab UI
    View layer_tab_discover;
    ImageView img_tab_discover;
    TextView txt_tab_discover;
    View layer_tab_workout;
    ImageView img_tab_workout;
    TextView txt_tab_workout;
    View layer_tab_account;
    ImageView img_tab_account;
    TextView txt_tab_account;

    public static int mCurFragmentIndex = -1;
    static int mOldFragmentIndex = -1;

    public static ArrayList<BeaconModel> allBeacons = new ArrayList<BeaconModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        loadRootFragment(R.id.fl_container, DiscoverFragment.getInstance());
        mCurFragmentIndex = FRAGMENT_DISCOVER;
        initViewState(FRAGMENT_DISCOVER);

        allBeacons.clear();
        allBeacons.addAll(DBManager.getInstance().getAllBeacons());

        // Start Beacon Monitoring Service
        if (!isServiceRunning(BeaconMonitoringService.class)) {
            Intent beaconService = new Intent(getApplicationContext(), BeaconMonitoringService.class);
            startService(beaconService);
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_username.setPaintFlags(txt_username.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String username = AppPreference.readString(AppPreference.KEY.SIGN_IN_FULLNAME, "");
        if (!username.isEmpty()) {
            txt_username.setText(username);

        } else {
            String email = AppPreference.readString(AppPreference.KEY.SIGN_IN_EMAIL, "");
            if (!email.isEmpty()) {
                username = email.substring(0, email.indexOf("@"));
                txt_username.setText(username);
            }
        }

        // Navigation UI
        layer_nav_logo = findViewById(R.id.layer_nav_logo);
        layer_nav_discover = findViewById(R.id.layer_nav_discover);
        img_nav_discover = (ImageView) findViewById(R.id.img_nav_discover);
        txt_nav_discover = (TextView) findViewById(R.id.txt_nav_discover);
        layer_nav_workout = findViewById(R.id.layer_nav_workout);
        img_nav_workout = (ImageView) findViewById(R.id.img_nav_workout);
        txt_nav_workout = (TextView) findViewById(R.id.txt_nav_workout);
        layer_nav_account = findViewById(R.id.layer_nav_account);
        img_nav_account = (ImageView) findViewById(R.id.img_nav_account);
        txt_nav_account = (TextView) findViewById(R.id.txt_nav_account);
        layer_logout = findViewById(R.id.layer_logout);

        // Tab UI
        layer_tab_discover = findViewById(R.id.layer_tab_discover);
        img_tab_discover = (ImageView) findViewById(R.id.img_tab_discover);
        txt_tab_discover = (TextView) findViewById(R.id.txt_tab_discover);
        layer_tab_workout = findViewById(R.id.layer_tab_workout);
        img_tab_workout = (ImageView) findViewById(R.id.img_tab_workout);
        txt_tab_workout = (TextView) findViewById(R.id.txt_tab_workout);
        layer_tab_account = findViewById(R.id.layer_tab_account);
        img_tab_account = (ImageView) findViewById(R.id.img_tab_account);
        txt_tab_account = (TextView) findViewById(R.id.txt_tab_account);


        layer_nav_logo.setOnClickListener(this);
        layer_nav_discover.setOnClickListener(this);
        layer_nav_workout.setOnClickListener(this);
        layer_nav_account.setOnClickListener(this);
        layer_logout.setOnClickListener(this);
        layer_tab_discover.setOnClickListener(this);
        layer_tab_workout.setOnClickListener(this);
        layer_tab_account.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean isNotification = intent.getBooleanExtra(AppConstant.EK_FROM_NOTIFICATION, false);
        if (isNotification) {
            SwitchFragment(FRAGMENT_DISCOVER);
        }
    }

    @Override
    public void onClick(final View v) {
        mDrawer.closeDrawer(GravityCompat.START);

        mDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (v == layer_nav_discover || v == layer_tab_discover) {
                    SwitchFragment(FRAGMENT_DISCOVER);

                } else if (v == layer_nav_workout || v == layer_tab_workout) {
                    SwitchFragment(FRAGMENT_WORKOUT);

                } else if (v == layer_nav_account || v == layer_tab_account) {
                    SwitchFragment(FRAGMENT_ACCOUNT);

                } else if (v == layer_logout) {
                    showLogOutDialog();
                }
            }
        }, 250);
    }

    public void SwitchFragment(int fragmentIndex) {
        mOldFragmentIndex = mCurFragmentIndex;
        mCurFragmentIndex = fragmentIndex;

        if (fragmentIndex == FRAGMENT_DISCOVER) {
            clearStack();
            replaceLoadRootFragment(R.id.fl_container, DiscoverFragment.getInstance(), false);

        } else if (fragmentIndex == FRAGMENT_WORKOUT) {
            clearStack();
            replaceLoadRootFragment(R.id.fl_container, WorkoutLogFragment.getInstance(), false);

        } else if (fragmentIndex == FRAGMENT_ACCOUNT) {
            clearStack();
            replaceLoadRootFragment(R.id.fl_container, ProfileFragment.getInstance(), false);

        }

        initViewState(fragmentIndex);
    }

    private void initViewState(int fragmentIndex) {
        img_nav_discover.setImageResource(R.mipmap.app_icon);
        txt_nav_discover.setTextColor(getResources().getColor(R.color.txt_nav_off));

        layer_tab_discover.setBackgroundResource(R.color.color_tab_off);
        img_tab_discover.setImageResource(R.mipmap.app_icon);
        txt_tab_discover.setTextColor(getResources().getColor(R.color.txt_tab_off));

        img_nav_workout.setImageResource(R.mipmap.app_icon);
        txt_nav_workout.setTextColor(getResources().getColor(R.color.txt_nav_off));

        layer_tab_workout.setBackgroundResource(R.color.color_tab_off);
        img_tab_workout.setImageResource(R.mipmap.app_icon);
        txt_tab_workout.setTextColor(getResources().getColor(R.color.txt_tab_off));

        img_nav_account.setImageResource(R.mipmap.app_icon);
        txt_nav_account.setTextColor(getResources().getColor(R.color.txt_nav_off));

        layer_tab_account.setBackgroundResource(R.color.color_tab_off);
        img_tab_account.setImageResource(R.mipmap.app_icon);
        txt_tab_account.setTextColor(getResources().getColor(R.color.txt_tab_off));

        if (fragmentIndex == FRAGMENT_DISCOVER) {
            img_nav_discover.setImageResource(R.mipmap.app_icon);
            txt_nav_discover.setTextColor(getResources().getColor(R.color.txt_nav_on));

            layer_tab_discover.setBackgroundResource(R.color.color_tab_on);
            img_tab_discover.setImageResource(R.mipmap.app_icon);
            txt_tab_discover.setTextColor(getResources().getColor(R.color.txt_tab_on));

        } else if (fragmentIndex == FRAGMENT_WORKOUT) {
            img_nav_workout.setImageResource(R.mipmap.app_icon);
            txt_nav_workout.setTextColor(getResources().getColor(R.color.txt_nav_on));

            layer_tab_workout.setBackgroundResource(R.color.color_tab_on);
            img_tab_workout.setImageResource(R.mipmap.app_icon);
            txt_tab_workout.setTextColor(getResources().getColor(R.color.txt_tab_on));

        } else if (fragmentIndex == FRAGMENT_ACCOUNT) {
            img_nav_account.setImageResource(R.mipmap.app_icon);
            txt_nav_account.setTextColor(getResources().getColor(R.color.txt_nav_on));

            layer_tab_account.setBackgroundResource(R.color.color_tab_on);
            img_tab_account.setImageResource(R.mipmap.app_icon);
            txt_tab_account.setTextColor(getResources().getColor(R.color.txt_tab_on));

        }
    }

    private void clearStack() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        while (backStackEntryCount > 1) {
            pop();
            backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        }
    }

    private void showLogOutDialog() {
        final QuestionDialog dialog = new QuestionDialog(this);
        dialog.show();
        dialog.setQuestion(getString(R.string.string_log_out));
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onLogOut();
            }
        });
    }

    private void onLogOut() {
        myDlg.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                myDlg.hide();
                // remove auto login info
                AppPreference.writeBoolean(AppPreference.KEY.SIGN_IN_AUTO, false);

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.in_right, R.anim.out_right);

                MainActivity.this.finish();
            }
        }, AppConstant.SPLASH_TIME * 1000);
    }

    @Override
    public void onBackPressedSupport() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (mCurFragmentIndex == FRAGMENT_DISCOVER) {
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
                }
            } else {
                SwitchFragment(FRAGMENT_DISCOVER);
            }
        }
    }

    // Custom method to determine whether a service is running
    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        // Loop through the running services
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                // If the service is running then return true
                return true;
            }
        }
        return false;
    }
}
