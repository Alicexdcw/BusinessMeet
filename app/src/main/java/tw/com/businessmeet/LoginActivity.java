package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.bean.LoginBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.network.ApplicationContext;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class LoginActivity extends AppCompatActivity {

//    private LoginViewModel loginViewModel;
private BlueToothHelper blueToothHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        System.out.println("App.get() : " + ApplicationContext.get());
//        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);
         blueToothHelper= new BlueToothHelper(this);
            blueToothHelper.startBuleTooth();
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
//        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        loginButton.setEnabled(true);
        usernameEditText.append("test");
        passwordEditText.append("test");

//        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
//            @Override
//            public void onChanged(@Nullable LoginFormState loginFormState) {
//                if (loginFormState == null) {
//                    return;
//                }
//                loginButton.setEnabled(loginFormState.isDataValid());
//                if (loginFormState.getUsernameError() != null) {
//                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
//                }
//                if (loginFormState.getPasswordError() != null) {
//                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
//                }
//            }
//        });
//
//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });
//
//        TextWatcher afterTextChangedListener = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // ignore
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // ignore
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//            }
//        };
//        usernameEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginViewModel.login(usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
                UserInformationBean userInformationBean = new UserInformationBean();
                userInformationBean.setUserId(usernameEditText.getText().toString());
                userInformationBean.setPassword(passwordEditText.getText().toString());
                userInformationBean.setBluetooth(blueToothHelper.getMyBuleTooth());
                System.out.println("userInformationBean.getBluetooth() = " + userInformationBean.getBluetooth());
                AsyncTasKHelper.execute(login,userInformationBean);
            }
        });
    }
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private AsyncTasKHelper.OnResponseListener<UserInformationBean, LoginBean> login = new AsyncTasKHelper.OnResponseListener<UserInformationBean, LoginBean>() {
        @Override
        public Call<ResponseBody<LoginBean>> request(UserInformationBean... userInformationBeans) {
            return userInformationService.login(userInformationBeans[0]);
        }
        @Override
        public void onSuccess(LoginBean loginBean) {
            Intent it = new Intent(LoginActivity.this, NotificationService.class);
//            stopService(it);
            startService(it);
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, SelfIntroductionActivity.class);
            startActivity(intent);
            System.out.println("identty : " + loginBean.getIdentity() );
        }
        @Override
        public void onFail(int status) {
            Log.d("intomatched","success");
            //unmatchedDeviceRecyclerViewAdapter.dataInsert(userInformationBean);
        }
    };
//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }
//
//    private void showLoginFailed(@StringRes Integer errorString) {
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }
}