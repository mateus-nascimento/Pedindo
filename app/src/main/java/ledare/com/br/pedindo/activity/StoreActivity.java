package ledare.com.br.pedindo.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.model.Store;
import ledare.com.br.pedindo.util.Constants;

public class StoreActivity extends BaseActivity {

    public static final String EXTRA_POST_KEY = "EXTRA_POST_KEY";

    //UI

    //Firebase
    private DatabaseReference mStoreReference;
    private ValueEventListener mStoreListener;
    private Store mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //UI

        //Firebase
        String storeId = getIntent().getStringExtra(EXTRA_POST_KEY);
        mStoreReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.DATABASE_STORES).child(storeId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener storeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mStore = dataSnapshot.getValue(Store.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mStoreReference.addValueEventListener(storeListener);
        mStoreListener = storeListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStoreReference.removeEventListener(mStoreListener);
    }

}
