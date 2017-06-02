package ledare.com.br.pedindo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ledare.com.br.pedindo.R;

public class StoreViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView descriptionView;
    public ImageView imageView;

    public StoreViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.title_store);
        descriptionView = (TextView) itemView.findViewById(R.id.description_store);
        imageView = (ImageView) itemView.findViewById(R.id.image_store);
    }
}
