package com.scube.karandeepdistributors.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.scube.karandeepdistributors.R;
import com.scube.karandeepdistributors.utils.LoginSessionManger;
/**
 * Splash Screen
 */
public class SplashScreenActivity extends Activity {
  private static int SPLASH_DISPLAY_LENGTH = 2000;
  LoginSessionManger loginSessionManger;
  Context mContext;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_splash);
    mContext = this;
    loginSessionManger = new LoginSessionManger(mContext);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (loginSessionManger.isAccessTokenAvailable()) {
          startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
          finish();
        }else {
          Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
          startActivity(intent);
          finish();
        }
      }
    }, SPLASH_DISPLAY_LENGTH);
  }
}
