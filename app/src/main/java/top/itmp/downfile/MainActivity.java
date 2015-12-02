package top.itmp.downfile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private Button button = null;
    private TextView textView = null;
    private EditText editText = null;
    private String downFileName = null;

    private int downFileLength = 0;
    private int downedFileLength = 0;
    private InputStream inputStream;
    private OutputStream outputStream;
    private URLConnection urlConnection;
    private ProgressDialog progressDialog;


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
                if (textView.getText() == null) {
                    Toast.makeText(getApplicationContext(), "如要下载文件，请先输入要下载的文件的链接", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                } else {

                    progressDialog = new ProgressDialog(MainActivity.this);

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            //super.run();
                            try {
                                URL url = new URL(editText.getText().toString());
                                urlConnection = url.openConnection();
                                if (urlConnection.getReadTimeout() == 5) {
                                    Toast.makeText(getApplicationContext(), "当前网络可能有问题", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                inputStream = urlConnection.getInputStream();
                                downFileLength = urlConnection.getContentLength();
                            } catch (MalformedInputException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String[] tmps = editText.getText().toString().split("/");
                            downFileName = tmps[tmps.length - 1];

                            String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "downfiles" + File.separator + downFileName;
                            File file = new File(filePath);
                            if (!file.getParentFile().exists()) {
                                file.mkdir();
                            }
                            if (file.exists()) {
                                // todo a dialog to ask user cancel or not
                                Toast.makeText(getApplicationContext(), "文件已存在", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                            Message message = new Message();
                            try{
                            outputStream = new FileOutputStream(file);
                            byte[] buffer = new byte[1024 * 4];
                            message.what = 0;
                            handler.sendMessage(message);
                            while (downedFileLength < downFileLength) {
                                outputStream.write(buffer);
                                downedFileLength += inputStream.read(buffer);
                                Message message1 = new Message();
                                message1.what = 1;
                                handler.sendMessage(message1);
                            }
                            Message message2 = new Message();
                            message2.what = 2;
                            handler.sendMessage(message2);

                            } catch(IOException e){
                                e.printStackTrace();
                            }

                        }


                    };
                }
            }

        });

    }

    private Handler handler = new Handler() {
      public void handleMessage(Message msg){
          if(!Thread.currentThread().isInterrupted()){
              switch (msg.what){
                  case 0:
                      progressDialog.setMessage("下载中");
                      break;
                  case 1:
                      progressDialog.setProgress(downedFileLength);
                      break;
                  case 2:
                      Toast.makeText(getApplicationContext(),"下载完成。",Toast.LENGTH_SHORT).show();
                      break;
                  default:
                      break;


              }
          }
      }
    };
}
