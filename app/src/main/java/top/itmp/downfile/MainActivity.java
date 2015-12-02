package top.itmp.downfile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getText() == null){
                    Toast.makeText(getApplicationContext(), "如要下载文件，请先输入要下载的文件的链接", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                } else {

                }
            }
        });

    }
}
