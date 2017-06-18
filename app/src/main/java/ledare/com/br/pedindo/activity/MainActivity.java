package ledare.com.br.pedindo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
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

import java.io.File;

import ledare.com.br.pedindo.util.Constants;
import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.fragment.StoresFragment;
import ledare.com.br.pedindo.model.User;
import ledare.com.br.pedindo.util.CircleTransform;

public class MainActivity extends BaseActivity {

    //UI
    private DrawerLayout drawerLayout;

    //User
    private User mUser;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        getSupportActionBar().setTitle("Stores");

        //Firebase
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            mDatabase = FirebaseDatabase.getInstance().getReference().
                    child(Constants.DATABASE_USERS).
                    child(firebaseUser.getUid());

            setupNavigation();
            setupFragment(new StoresFragment());
        }
    }

    private void setupNavigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (navigationView != null && drawerLayout != null) {
            navigationView.getMenu().getItem(0).setChecked(true);
            //Header
            setupNavigationHeader(navigationView);
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

    private void setupNavigationHeader(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        final TextView headerName = (TextView) headerView.findViewById(R.id.header_name);
        final TextView headerEmail = (TextView) headerView.findViewById(R.id.header_email);
        final ImageView headerPhoto = (ImageView) headerView.findViewById(R.id.header_image);

        //Profile
        mDatabase.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mUser = dataSnapshot.getValue(User.class);

                                headerName.setText(mUser.username);
                                headerEmail.setText(mUser.email);
                                Glide.with(MainActivity.this).load(mUser.photoUrl)
                                        .crossFade()
                                        .thumbnail(0.5f)
                                        .bitmapTransform(new CircleTransform(MainActivity.this))
                                        .into(headerPhoto);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
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
}



