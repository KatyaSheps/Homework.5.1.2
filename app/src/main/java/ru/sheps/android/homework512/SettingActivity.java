package ru.sheps.android.homework512;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SettingActivity extends AppCompatActivity {
    EditText editText;
    ImageView i;

    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int EXTERNAL_REQUEST = 138;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        editText = findViewById(R.id.editText);

    }

    public void onClick(View view) throws FileNotFoundException {
        requestForPermission();

        String fileName = editText.getText().toString();
       String sdcardPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera";
//        FileInputStream Fin =new FileInputStream (sdcardPath);

//        /storage/emulated/0/DCIM/Camera/IMG_20190623_200429.jpg
        File sdFile = new File(sdcardPath);
        File[] files = sdFile.listFiles();
        for (File localFile : files) {
            if (localFile.getName().equals(fileName)) {

                SharedPreferences.Editor myEditor = MainActivity.sharedPreferences.edit();
                myEditor.putString(MainActivity.IMAGE, localFile.getAbsolutePath());
                myEditor.apply();
                Intent intent = new Intent();
                intent.putExtra("ImageFileName", sdcardPath + "/" + fileName);
                setResult(RESULT_OK, intent);
                Toast.makeText(this, "Готово", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }
}
