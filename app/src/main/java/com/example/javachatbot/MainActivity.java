package com.example.javachatbot;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import androidx.annotation.NonNull;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import android.widget.Toast;

import com.example.javachatbot.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    // Remove the below line after defining your own ad unit ID.
    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";
    private static final String TAG = "MainActivity";
    private static final String AD_UNIT_ID = "ca-app-pub-1997146053947882/8635063831";
    private InterstitialAd mInterstitialAd;
    private ActivityMainBinding binding;
    private AdView mAdView;
    //Chat Bot variable

    private RecyclerView chatsRV;
    private EditText userMsgEdt;
    private ImageButton sendMsgBtn;
    private  final  String BOT_KEY = "bot";
    private  final  String USER_KEY = "user";
    private ArrayList<MessageModal>messageModalArrayList;
    private MessageRVAdapter messageRVAdapter;

    private Button btn1, btn2, btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Log the Mobile Ads SDK version.
        Log.d(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        loadBannerAds();
//        loadInterstitialAd();

        chatsRV = findViewById(R.id.RVChat);
        userMsgEdt = findViewById(R.id.MessageInput);
        sendMsgBtn = findViewById(R.id.SendBtn);
        messageModalArrayList = new ArrayList<>();
        messageRVAdapter = new MessageRVAdapter(messageModalArrayList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatsRV.setLayoutManager(manager);
        chatsRV.setNestedScrollingEnabled(true);
        chatsRV.setAdapter(messageRVAdapter);

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userMsgEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
//                showInterstitial();
                getResponse(userMsgEdt.getText().toString());
                manager.scrollToPosition(messageRVAdapter.getItemCount() - 1);
                userMsgEdt.setText("");
            }
        });

        btn1 = findViewById(R.id.btn1);
        btn1.setText("What is Java Programming language?");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResponse("What is Java Programming language");
                manager.scrollToPosition(messageRVAdapter.getItemCount() - 1);
            }
        });
        btn2 = findViewById(R.id.btn2);
        btn2.setText("What is Java programming used for?");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResponse("What is Java programming used for?");
                manager.scrollToPosition(messageRVAdapter.getItemCount() - 1);
            }
        });
        btn3 = findViewById(R.id.btn3);
        btn3.setText("What are the features of JAVA?");
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResponse("What are the features of JAVA?");
                manager.scrollToPosition(messageRVAdapter.getItemCount() - 1);
            }
        });

    }

    private void getResponse(String message){
        messageModalArrayList.add(new MessageModal(message, USER_KEY));
        messageRVAdapter.notifyItemInserted(messageRVAdapter.getItemCount() - 1);
        String url = "http://api.brainshop.ai/get?bid=169138&key=4DS31oLcC4iIXJp8&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestAPI requestAPI = retrofit.create(RequestAPI.class);
        Call<MsgModal> call = requestAPI.getMessage(url);
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
                if(response.isSuccessful()){
                    MsgModal modal = response.body();
                    messageModalArrayList.add(new MessageModal(modal.getCnt(), BOT_KEY));
                    messageRVAdapter.notifyItemInserted(messageRVAdapter.getItemCount() - 1);
                    chatsRV.smoothScrollToPosition(messageRVAdapter.getItemCount() - 1);

                }
            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                messageModalArrayList.add(new MessageModal("Please revert your message", BOT_KEY));
                messageRVAdapter.notifyItemInserted(messageRVAdapter.getItemCount() - 1);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        InterstitialAd.load(
                this,
                AD_UNIT_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                        Toast.makeText(MainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d(TAG, "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;

                        String error = String.format(
                                Locale.ENGLISH,
                                "domain: %s, code: %d, message: %s",
                                loadAdError.getDomain(),
                                loadAdError.getCode(),
                                loadAdError.getMessage());
                        Toast.makeText(
                                        MainActivity.this,
                                        "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
    public void loadBannerAds() {
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1997146053947882/6513540797");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    private void showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null) {

            mInterstitialAd.show(this);
        } else {
            Toast.makeText(this, "Ad already initiate", Toast.LENGTH_SHORT).show();
        }
    }


}