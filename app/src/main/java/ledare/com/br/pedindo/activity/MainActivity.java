package ledare.com.br.pedindo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.fragment.StoreFragment;
import ledare.com.br.pedindo.util.CircleTransform;

public class MainActivity extends BaseActivity {

    //Constants
    public static int navigationItemIndex = 0;
    private static final String TAG_STORE = "STORE";
    public static String CURRENT_TAG = TAG_STORE;
    public static final String USER_PREFERENCE = "USER_PREFERENCE";


    private NavigationView navigationView;
    private DrawerLayout navigationDrawer;
    private View navigationHeader;

    private TextView txtName, txtEmail;
    private ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar(getString(R.string.app_name));

        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // navigation view header
        navigationHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navigationHeader.findViewById(R.id.name);
        txtEmail = (TextView) navigationHeader.findViewById(R.id.email);
        imgProfile = (ImageView) navigationHeader.findViewById(R.id.img_profile);

        // setup the interface
        setupNavigationHeader();
        setupNavigationView();
        setupFragment();
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
            navigationDrawer.closeDrawers();
            return;
        }
    }

    private void setupNavigationHeader() {
        SharedPreferences prefs = getSharedPreferences(USER_PREFERENCE, MODE_PRIVATE);
        String name = prefs.getString("name", "No name detected");
        String email = prefs.getString("email", "No email detected");
        String photo = prefs.getString("photo", " ");

        txtName.setText(name);
        txtEmail.setText(email);
        Glide.with(MainActivity.this).load(photo)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(MainActivity.this))
                .into(imgProfile);
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_stores:
                        navigationItemIndex = 0;
                        CURRENT_TAG = TAG_STORE;
                        break;
                    case R.id.nav_exit:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        return true;
                    default:
                        navigationItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                setupFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                navigationDrawer, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the navigationDrawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the navigationDrawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to navigationDrawer layout
        navigationDrawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }

    private void setupFragment() {
        navigationView.getMenu().getItem(navigationItemIndex).setChecked(true);

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            navigationDrawer.closeDrawers();
            return;
        }

        Fragment fragment = getFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();

        // closing drawer on item click
        navigationDrawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    private Fragment getFragment() {
        switch (navigationItemIndex) {
            case 0:
                // store
                StoreFragment storeFragment = new StoreFragment();
                return storeFragment;
            default:
                return new StoreFragment();
        }
    }
}



