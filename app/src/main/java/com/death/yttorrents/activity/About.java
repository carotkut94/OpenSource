package com.death.yttorrents.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.death.yttorrents.R;
import com.vansuita.materialabout.builder.AboutBuilder;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Toolbar toolbar = new Toolbar(this);
        setSupportActionBar(toolbar);
        View view = AboutBuilder.with(this)
                .setPhoto(R.drawable.profile)
                .setCover(R.mipmap.profile_cover)
                .setName("Death Code")
                .setSubTitle("Mobile Developer")
                .setBrief("I am just a simple mobile developer. You can find me on below networks and if you are interested in getting the code of the app you can find it on github.")
                .setAppIcon(R.drawable.ic_icon)
                .setAppName(R.string.app_name)
                .addGitHubLink("https://github.com/carotkut94")
                .addFacebookLink("https://facebook.com/ceo.sidhant.rajora.2")
                .addYoutubeChannelLink("https://www.youtube.com/channel/UCSSv-AjccAMsSVM5z5SkhMg")
                .addEmailLink("carotkut12@gmail.co")
                .addAction(R.drawable.more_apps,"More Apps","https://play.google.com/store/apps/developer?id=Vishal+Bothra&hl=en")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .build();

        addContentView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
