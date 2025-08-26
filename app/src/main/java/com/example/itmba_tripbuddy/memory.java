package com.example.itmba_tripbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class memory extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_MUSIC_REQUEST = 2;

    private Uri selectedImageUri;
    private Uri selectedMusicUri;
    private String copiedMusicPath;
    private int userId;

    private EditText captionInput;
    private ImageView imagePreview;
    private Button selectImageBtn, selectMusicBtn, saveMemoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_memory);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) userId = SessionManager.getUserId(this);

        captionInput = findViewById(R.id.edtCaption);
        imagePreview = findViewById(R.id.imgPreview);
        selectImageBtn = findViewById(R.id.btnPickImage);
        selectMusicBtn = findViewById(R.id.btnPickMusic);
        saveMemoryBtn = findViewById(R.id.btnSaveMemory);

        selectImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        selectMusicBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_MUSIC_REQUEST);
        });


        saveMemoryBtn.setOnClickListener(v -> saveMemory());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                selectedImageUri = data.getData();
                imagePreview.setImageURI(selectedImageUri);
            } else if (requestCode == PICK_MUSIC_REQUEST) {
                selectedMusicUri = data.getData();
                getContentResolver().takePersistableUriPermission(
                        selectedMusicUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
                copiedMusicPath = copyUriToInternalStorage(selectedMusicUri, "music_" + System.currentTimeMillis() + ".mp3");
                if (copiedMusicPath != null) {
                    Toast.makeText(this, "Music selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to copy music file", Toast.LENGTH_SHORT).show();
                    selectedMusicUri = null;
                }
            }
        }
    }

    private void saveMemory() {
        String caption = captionInput.getText().toString().trim();
        if (caption.isEmpty() || selectedImageUri == null || copiedMusicPath == null) {
            Toast.makeText(this, "Please select image, music, and enter a caption.", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePath = copyUriToInternalStorage(selectedImageUri, "img_" + System.currentTimeMillis() + ".jpg");

        if (imagePath == null) {
            Toast.makeText(this, "Failed to save image file.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = new Database(this).insertMemory(userId, caption, imagePath, copiedMusicPath);
        if (success) {
            Toast.makeText(this, "Memory saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save memory.", Toast.LENGTH_SHORT).show();
        }
    }

    private String copyUriToInternalStorage(Uri sourceUri, String filename) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            File file = new File(getFilesDir(), filename);
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
