package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.about_us)
    androidx.constraintlayout.widget.ConstraintLayout about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(AboutActivity.this);
        about.setBackground(getDrawable(R.drawable.bak_about));

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.title)
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup(getString(R.string.connect))
                .addGroup("QQ:970600395")
                .addGroup("微信：18206825944")
                .addEmail("970600395@qq.com")
                .addGitHub("youyyyy")
                .setDescription(getString(R.string.about))
                .create();

        setContentView(aboutPage);
    }
}
