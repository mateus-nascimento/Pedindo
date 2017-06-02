package ledare.com.br.pedindo.activity;

import android.app.Activity;
import android.os.Bundle;

import ledare.com.br.pedindo.R;

public class StoreDetailActivity extends Activity {

    public static final String EXTRA_POST_KEY = "POST_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
    }
}
