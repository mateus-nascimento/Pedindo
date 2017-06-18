package ledare.com.br.pedindo.adapter;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import ledare.com.br.pedindo.model.Store;
import ledare.com.br.pedindo.util.StoreListener;

public class StoreAdapter extends FirebaseRecyclerAdapter<Store, StoreViewHolder> {

    private final Context mContext;
    private StoreListener mListener;

    public StoreAdapter(Class<Store> modelClass,
                        int modelLayout,
                        Class<StoreViewHolder> viewHolderClass,
                        Query ref,
                        Context context,
                        StoreListener storeListener) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = context;
        this.mListener = storeListener;
    }

    @Override
    protected void populateViewHolder(final StoreViewHolder viewHolder, final Store model, final int position) {
        viewHolder.titleView.setText(model.title);
        viewHolder.descriptionView.setText(model.description);
        Glide.with(mContext).load(model.photoUrl)
                .crossFade()
                .thumbnail(0.5f)
                .into(viewHolder.imageView);

        if (mListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickStore(model);
                }
            });
        }
    }
}
