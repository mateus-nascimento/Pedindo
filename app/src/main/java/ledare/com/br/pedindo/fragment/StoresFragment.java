package ledare.com.br.pedindo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.activity.StoreDetailActivity;
import ledare.com.br.pedindo.adapter.StoreAdapter;
import ledare.com.br.pedindo.model.Store;
import ledare.com.br.pedindo.adapter.StoreViewHolder;
import ledare.com.br.pedindo.util.StoreListener;

public class StoresFragment extends Fragment {

    //UI
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;

    //Firebase
    private FirebaseRecyclerAdapter<Store, StoreViewHolder> mAdapter;
    private DatabaseReference mDatabase;

    public StoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_stores, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mManager);

        //FirebaseRecyclerAdapter
        final Query storesQuery = mDatabase.child(getString(R.string.node_stores)).limitToFirst(100);

        mAdapter = new StoreAdapter(Store.class, R.layout.item_store,
                StoreViewHolder.class, storesQuery, getActivity(), onClickStore());

        mRecyclerView.setAdapter(mAdapter);
    }

    private StoreListener onClickStore() {
        return new StoreListener() {
            @Override
            public void onClickStore(Store store) {
                Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
                intent.putExtra(StoreDetailActivity.EXTRA_POST_KEY, store.id);
                startActivity(intent);
            }
        };
    }
}
