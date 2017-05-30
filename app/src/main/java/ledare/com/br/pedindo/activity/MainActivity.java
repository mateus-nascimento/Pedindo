package ledare.com.br.pedindo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.fragment.StoresFragment;
import ledare.com.br.pedindo.model.User;
import ledare.com.br.pedindo.util.CircleTransform;

public class MainActivity extends BaseActivity {

    //UI
    private DrawerLayout drawerLayout;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    protected User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = new User();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser ==  null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            mDatabase.child(getString(R.string.node_users))
                    .child(currentUser.getUid())
                    .addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mUser = dataSnapshot.getValue(User.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
            setupNavigation(mUser);
            setupFragment(new StoresFragment());
        }
    }

    private void setupNavigation(User user) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (navigationView != null && drawerLayout != null) {
            navigationView.getMenu().getItem(0).setChecked(true);
            //Header
            setupNavigationHeader(navigationView, user);
            //Navigation itens
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                            menuItem.setChecked(true);
                            drawerLayout.closeDrawers();
                            onNavigationSelected(menuItem);
                            return true;
                        }
                    }
            );

            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };

            drawerLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        }
    }

    private void setupNavigationHeader(NavigationView navigationView, User user) {
        View headerView = navigationView.getHeaderView(0);
        TextView headerName = (TextView) headerView.findViewById(R.id.header_name);
        TextView headerEmail = (TextView) headerView.findViewById(R.id.header_email);
        ImageView headerPhoto = (ImageView) headerView.findViewById(R.id.header_photo);

        headerName.setText(user.getUsername());
        headerEmail.setText(user.getEmail());
        Glide.with(MainActivity.this).load(user.getPhoto())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(MainActivity.this))
                .into(headerPhoto);
    }


    private void onNavigationSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_stores:
                setupFragment(new StoresFragment());
                break;
            case R.id.navigation_exit:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }
    }

    private void setupFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

//    private void setupNavigationHeader() {
//        SharedPreferences prefs = getSharedPreferences(USER_PREFERENCE, MODE_PRIVATE);
//        String name = prefs.getString("name", "No name detected");
//        String email = prefs.getString("email", "No email detected");
//        String photo = prefs.getString("photo", " ");
//
//        txtName.setText(name);
//        txtEmail.setText(email);
//        Glide.with(MainActivity.this).load(photo)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(MainActivity.this))
//                .into(imgProfile);
//    }

//    private void setupNavigationView() {
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.navigation_stores:
//                        navigationItemIndex = 0;
//                        CURRENT_TAG = TAG_STORE;
//                        break;
//                    case R.id.navigation_exit:
//                        FirebaseAuth.getInstance().signOut();
//                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                        finish();
//                        return true;
//                    default:
//                        navigationItemIndex = 0;
//                }
//
//                if (menuItem.isChecked()) {
//                    menuItem.setChecked(false);
//                } else {
//                    menuItem.setChecked(true);
//                }
//                menuItem.setChecked(true);
//
//                setupFragment();
//                return true;
//            }
//        });
//
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
//                navigationDrawer, toolbar, R.string.open_drawer, R.string.close_drawer) {
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                // Code here will be triggered once the navigationDrawer closes as we dont want anything to happen so we leave this blank
//                super.onDrawerClosed(drawerView);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                // Code here will be triggered once the navigationDrawer open as we dont want anything to happen so we leave this blank
//                super.onDrawerOpened(drawerView);
//            }
//        };
//
//        //Setting the actionbarToggle to navigationDrawer layout
//        navigationDrawer.setDrawerListener(actionBarDrawerToggle);
//
//        //calling sync state is necessary or else your hamburger icon wont show up
//        actionBarDrawerToggle.syncState();
//
//    }
//
//    private void setupFragment() {
//        navigationView.getMenu().getItem(navigationItemIndex).setChecked(true);
//
//        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
//            navigationDrawer.closeDrawers();
//            return;
//        }
//
//        Fragment fragment = getFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                android.R.anim.fade_out);
//        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
//        fragmentTransaction.commitAllowingStateLoss();
//
//        // closing drawer on item click
//        navigationDrawer.closeDrawers();
//
//        // refresh toolbar menu
//        invalidateOptionsMenu();
//
//    }
//
//    private Fragment getFragment() {
//        switch (navigationItemIndex) {
//            case 0:
//                // store
//                StoresFragment storeFragment = new StoresFragment();
//                return storeFragment;
//            default:
//                return new StoresFragment();
//        }
//    }
}



