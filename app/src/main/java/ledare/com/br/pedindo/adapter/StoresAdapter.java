package ledare.com.br.pedindo.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.model.Store;
import ledare.com.br.pedindo.util.StoresListener;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.StoresViewHolder> {

    private final Context mContext;
    private final List<Store> mStores;
    private StoresListener mListener;

    public StoresAdapter(Context context, List<Store> storeList, StoresListener listener){
        this.mContext = context;
        this.mStores = storeList;
        this.mListener = listener;
    }

    @Override
    public StoresViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_store, viewGroup, false);
        StoresViewHolder holder = new StoresViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final StoresViewHolder holder, final int position) {
        Store store = mStores.get(position);

        holder.title.setText(store.getTitle());
        holder.description.setText(store.getDescription());
        Glide.with(mContext).load(store.getPhoto())
                .crossFade().
                thumbnail(0.5f)
                .into(holder.photo);

        if(mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickStore(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.mStores != null ? this.mStores.size() : 0;
    }

    public static class StoresViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView title;
        TextView description;
        ImageView photo;

        public StoresViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_store);

            title = (TextView) itemView.findViewById(R.id.title_store);
            description = (TextView) itemView.findViewById(R.id.description_store);
            photo = (ImageView) itemView.findViewById(R.id.photo_store);
        }
    }

}
