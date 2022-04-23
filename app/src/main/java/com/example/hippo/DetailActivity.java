package com.example.hippo;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.parceler.Parcels;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvDescription;
    TextView tvDuetime;
    ImageView ivImage;
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
//        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvDuetime = findViewById(R.id.tvDuetime);

        String title = getIntent().getStringExtra("title");
        String duetime = getIntent().getStringExtra("duetime");
        String description = getIntent().getStringExtra("description");
//
//        Bundle bundle = getIntent().getExtras();
//        if(bundle!= null){
//            int image = bundle.getInt("image");
//            if(image != 0){
//                ivImage.setImageResource(image);
//            }
//        }
        tvTitle.setText(title);
        tvDuetime.setText(duetime);
        tvDescription.setText(description);


    }
}
