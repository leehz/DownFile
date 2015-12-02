package top.itmp.downfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button = null;
    private TextView textView = null;
    private EditText editText = null;
    private String downFileName = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.btn);
        textView = (TextView)findViewById(R.id.tv);
        editText = (EditText)findViewById(R.id.link);


    }
}
