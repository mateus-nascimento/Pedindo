package ledare.com.br.pedindo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.model.Store;

public class StoreDetailActivity extends Activity {

    public static final String EXTRA_POST_KEY = "EXTRA_POST_KEY";

    //UI
    TextView teste;

    //Firebase
    private DatabaseReference mStoreReference;
    private ValueEventListener mStoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        //Firebase
        String storeId = getIntent().getStringExtra(EXTRA_POST_KEY);
        mStoreReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.node_stores)).child(storeId);

        //UI
        teste = (TextView) findViewById(R.id.teste_teste);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener storeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                teste.setText(store.title);
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
