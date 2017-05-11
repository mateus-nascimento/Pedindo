package ledare.com.br.pedindo.activity;

import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.fragment.StoreFragment;
import ledare.com.br.pedindo.model.User;
import ledare.com.br.pedindo.util.CircleTransform;

public class MainActivity extends BaseActivity {

    private NavigationView navigationView;
    private DrawerLayout navigationDrawer;
    private View navigationHeader;

    private TextView txtName, txtEmail;
    private ImageView imgProfile;

    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navigationItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_STORE = "STORE";
    public static String CURRENT_TAG = TAG_STORE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar(getString(R.string.app_name));

        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
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
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(getString(R.string.node_users));
        mDatabase.child(firebaseUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        txtName.setText(user.getName());
                        txtEmail.setText("TESTE@TESTE");
                        Glide.with(MainActivity.this).load(user.getPhoto())
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(MainActivity.this))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgProfile);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_store:
                        navigationItemIndex = 0;
                        CURRENT_TAG = TAG_STORE;
                        break;
                    case R.id.nav_exit:
                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                        finish();
                        navigationDrawer.closeDrawers();
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



