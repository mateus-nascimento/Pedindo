package ledare.com.br.pedindo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.adapter.StoresAdapter;
import ledare.com.br.pedindo.model.Store;
import ledare.com.br.pedindo.util.StoresListener;

public class StoresFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private StoresAdapter mAdapter;
    private List<Store> mStores;

    public StoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_stores, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStores = new ArrayList<>();

        mAdapter = new StoresAdapter(getContext(), mStores, onClickStore());
        mRecyclerView.setAdapter(mAdapter);
    }

    //Tratamento do evento do clique
    private StoresListener onClickStore(){
        return new StoresListener() {
            @Override
            public void onClickStore(View view, int position) {
                Store store = mStores.get(position);
                Toast.makeText(getContext(), store.getTitle() ,Toast.LENGTH_SHORT).show();
            }
        };
    }
}
