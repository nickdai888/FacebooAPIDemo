package com.zezooz.facebooklogindemo;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import org.json.JSONObject;


public class ProfileActivity extends AppCompatActivity {
    private TextView nickNameView;
    private ImageView profileImage;
    private ShareButton shareButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nickNameView = (TextView)this.findViewById(R.id.nickname);
        profileImage = (ImageView) findViewById(R.id.profileImage);

        shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("Hello,Taronga!")
                .setContentDescription(
                        "Taronga is a not-for-profit organisation supporting wildlife conservation.")
                .setContentUrl(Uri.parse("https://taronga.org.au/"))
                .build();
        shareButton.setShareContent(content);

        initProfile();
    }

    private void initProfile(){
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            int pWidth = 480;
                            int pHeight = 480;
                            String picUrlString = "https://graph.facebook.com/" + object.getString("id") + "/picture?width=" + pWidth + "&height=" + pHeight;
                            final Context context = getApplicationContext();
                            Glide.with(context).load(picUrlString).asBitmap().centerCrop().into(new BitmapImageViewTarget(profileImage) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    profileImage.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                            String name = object.getString("name");
                            nickNameView.setText(name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }



}
