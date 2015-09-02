package com.raoul.socailbase;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class MainActivity extends Activity {
    private Dialog progressDialog;
    String email;
    String name;
    String usernmae;
    String facebookid;
    ImageButton facebook_but;
    private static final int LOGIN_REQUEST = 0;
    ParseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser= ParseUser.getCurrentUser();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
        showHomeListActivity();
        } else if (!(currentUser == null)) {
         showHomeListActivity();
        }
        else {
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    MainActivity.this);
            startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
//    }
//
//    private void onfacebookLoginButtonClicked() {
//        MainActivity.this.progressDialog = ProgressDialog.show(
//                MainActivity.this, "", "Logging in...", true);
//        List<String> permissions = Arrays.asList("email", "public_profile", "user_about_me", "user_friends");
//        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException err) {
//                MainActivity.this.progressDialog.dismiss();
//                if (user == null) {
//
//                } else {
//                    if (user.isNew()) {
//                        getFaceBookGraphObject();
//
//
//                    } else {
//                        showHomeListActivity();
//                    }
//                }
//            }
//        });
//    }
//
//    public void getFaceBookGraphObject() {
//
//
//        Session session = ParseFacebookUtils.getSession();
//
//        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//            @Override
//            public void onCompleted(GraphUser user, Response response) {
//
//
//                if (user != null) {
//                    facebookid = user.getId();
//                    email = (String) user.getProperty("email");
//                    usernmae = "unknown";
//                    name = user.getName();
//                    ParseUser currentUser = ParseUser.getCurrentUser();
//                    currentUser.put("facebookId", facebookid);
//                    currentUser.put("displayName", name);
//                    currentUser.put("name", name);
//                    ParseACL rwACL = new ParseACL();
//                    rwACL.setReadAccess(currentUser, true); // allow user to do reads
//                    rwACL.setWriteAccess(currentUser, true);
//                    currentUser.setACL(rwACL);
//                    currentUser.setUsername(usernmae);
//                    currentUser.setEmail(email);
//                    Toast.makeText(MainActivity.this, "Current user is " + name, Toast.LENGTH_LONG).show();
//
//                    try {
//                        currentUser.save();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//                    installation.put("user", currentUser);
//                    installation.put("notification", "true");
//                    installation.saveInBackground();
//                    showHomeListActivity();
//
//
//                }
//
//            }
//        });
//        request.executeAsync();
//    }

    private void showHomeListActivity() {
        Intent intent = new Intent(this, Base.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // This closes the login screen so it's not on the back stack
    }


}
