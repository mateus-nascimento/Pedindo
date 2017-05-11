package ledare.com.br.pedindo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import ledare.com.br.pedindo.R;

public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupToolbar(String message) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            toolbar.setTitle(message);
            setSupportActionBar(toolbar);
        }
    }

    protected void toastShort(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void toastLong(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void display(String message){
        Log.d("DISPLAY", message);
    }
}
