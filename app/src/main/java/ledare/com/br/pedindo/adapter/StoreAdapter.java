package ledare.com.br.pedindo.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import ledare.com.br.pedindo.model.Store;

public class StoreAdapter extends FirebaseRecyclerAdapter<Store, StoreViewHolder> {

    private final Context mContext;

    public StoreAdapter(Class<Store> model, int layout, Class<StoreViewHolder> holder, Query ref, Context context) {
        super(model, layout, holder, ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(StoreViewHolder viewHolder, Store model, int position) {

        viewHolder.titleView.setText(model.title);
        viewHolder.descriptionView.setText(model.description);
        Glide.with(mContext).load(model.image)
                .crossFade()
                .thumbnail(0.5f)
                .into(viewHolder.imageView);
    }
}
