package com.deeptalk.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deeptalk.app.R;
import com.deeptalk.app.utils.PreferenceUtils;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    private EditText editTextApiKey;
    private Button buttonSave;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        // 设置View的layout的文件
        setContentView(R.layout.activity_settings);

        editTextApiKey = findViewById(R.id.edit_api_key);
        buttonSave = findViewById(R.id.btn_save_key);

        buttonSave.setOnClickListener(v -> {
            String key = editTextApiKey.getText().toString().trim();
            if (!key.isEmpty()) {
                PreferenceUtils.saveApiKey(getApplicationContext(), key);
                Toast.makeText(this, "API Key saved", Toast.LENGTH_SHORT).show();

                // Set the return result and notify the caller to refresh
                Intent resultIntent = new Intent();
                resultIntent.putExtra("key_updated", true);
                setResult(RESULT_OK, resultIntent);
                finish(); // 返回 ChatActivity
            } else {
                Toast.makeText(this, "please input valid API Key", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
