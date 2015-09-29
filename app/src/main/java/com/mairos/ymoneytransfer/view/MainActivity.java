package com.mairos.ymoneytransfer.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;

import com.mairos.ymoneytransfer.R;
import com.mairos.ymoneytransfer.network.Constants;
import com.mairos.ymoneytransfer.network.SpiceManagerProvider;
import com.mairos.ymoneytransfer.network.YmoneySpiceService;
import com.mairos.ymoneytransfer.network.events.ProcessPaymentEvent;
import com.mairos.ymoneytransfer.network.events.TokenEvent;
import com.mairos.ymoneytransfer.network.requestResults.ProcessPaymentResult;
import com.mairos.ymoneytransfer.network.requestResults.RequestPaymentResultCode;
import com.mairos.ymoneytransfer.network.requests.TokenRequest;
import com.mairos.ymoneytransfer.storage.Crypto;
import com.mairos.ymoneytransfer.storage.SharedPrefs_;
import com.mairos.ymoneytransfer.storage.Storage;
import com.mairos.ymoneytransfer.storage.models.PaymentInfo;
import com.mairos.ymoneytransfer.view.fragments.CreatePaymentFragment;
import com.mairos.ymoneytransfer.view.fragments.PaymentsListFragment;
import com.mairos.ymoneytransfer.view.fragments.PincodeFragmentDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Date;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity implements
        PaymentsListFragment.Callback, CreatePaymentFragment.Callback,
        PincodeFragmentDialog.Callback, SpiceManagerProvider {

    @Override
    public SpiceManager getSpiceManager() {
        return mSpiceManager;
    }

    @ViewById(R.id.container)
    FrameLayout mContainer;

    @Pref
    SharedPrefs_ myPrefs;

    @InstanceState
    boolean mTokenRequestInProgress = false;

    private SpiceManager mSpiceManager = new SpiceManager(YmoneySpiceService.class);

    @Override
    protected void onStart() {
        mSpiceManager.start(this);
        EventBus.getDefault().registerSticky(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @AfterViews
    void init(){

        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.container, PaymentsListFragment.newInstance(), PaymentsListFragment.TAG);
            ft.commit();
        }

    }

    @Override
    public void onBackPressed(){
        if (getFragmentManager().getBackStackEntryCount() == 0){
            finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri data = intent.getData();
        if (data != null) {

            String tmpToken = data.getQueryParameter("code");

            if (tmpToken == null){
                String errorMsg = data.getQueryParameter("error_description");

                if (errorMsg != null) {
                    showMessage(errorMsg);
                }

            } else {

                if (!mTokenRequestInProgress && myPrefs.token().get().isEmpty() &&
                        getFragmentManager().findFragmentByTag(PincodeFragmentDialog.TAG) == null) {

                    mTokenRequestInProgress = true;

                    TokenRequest request = new TokenRequest(tmpToken);
                    request.setRetryPolicy(null);

                    mSpiceManager.execute(request, 0, DurationInMillis.ALWAYS_EXPIRED, null);
                }
            }
        }
    }

    public void onEventMainThread(TokenEvent event) {

        if (event.isResult()) {
            String token = event.getAccessToken();

            showAddCriptokeyDialog(token);

        } else {

            showMessage("Ошибка авторизации: " + event.getMsg());
        }

        EventBus.getDefault().removeStickyEvent(event);
    }

    public void onEventMainThread(ProcessPaymentEvent event){
        if (event.getResult().getStatus().equals(RequestPaymentResultCode.SUCCESS)) {

            ProcessPaymentResult payInfo = event.getResult();

            Storage.get().put(new PaymentInfo(event.getProtectionCode(),
                    payInfo.getPaymentId(), payInfo.getPayee(), payInfo.getCreditAmount(),
                    new Date().getTime()));

            onPaymentAccepted();

        } else {

            showMessage(event.getResult().getError());
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onStartCreatingPayment() {
        addFragment(CreatePaymentFragment.newInstance(), CreatePaymentFragment.TAG);
    }

    private void addFragment(Fragment fragment, String tag){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }

    @Override // wait for result at onResume()
    public void onStartAuthorization() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Авторизация")
                .setMessage("Для совершения платёжной операции необходимо авторизоваться на " +
                        "сайте Яндекс.Деньги. Вы готовы?")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(true)
                .setPositiveButton("Нет",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendAuthRequestToBrowser();
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void sendAuthRequestToBrowser(){
        String postData = "client_id=" + Constants.CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + Constants.REDIRECT_URL +
                "&scope=" + Constants.APP_SCOPES +
                "&instance_name=instance_name";

        Intent browserIntent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(Constants.AUTHORIZATION_URL + "?" + postData));

        startActivity(browserIntent);
    }

    @Override
    public void onPaymentAccepted() {

        if (getFragmentManager().findFragmentByTag(CreatePaymentFragment.TAG) != null){
            getFragmentManager().popBackStack();
        }

        if (getFragmentManager().findFragmentByTag(PaymentsListFragment.TAG) != null){
            ((PaymentsListFragment) getFragmentManager().findFragmentByTag(PaymentsListFragment.TAG)).updateList();
        }

        showMessage("Платёж успешно завершён");
    }

    @OptionsItem(R.id.action_clear)
    void clearToken() {

       if (myPrefs.token().get().isEmpty()){
            onStartAuthorization();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Деавторизация")
                    .setMessage("Вы хотите пересоздать пинкод? " +
                            "Придётся ещё раз авторизовываться на сайте Яндекс.Денег")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setCancelable(true)
                    .setPositiveButton("Нет",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .setNegativeButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    myPrefs.token().put("");
                                    showMessage("Токен удалён");
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void showAddCriptokeyDialog(final String token){

        if (getFragmentManager().findFragmentByTag(PincodeFragmentDialog.TAG) == null) {
            PincodeFragmentDialog pincodeDialog = PincodeFragmentDialog.newInstance(
                    "Создание ключа доступа", token);
            pincodeDialog.show(getFragmentManager(), PincodeFragmentDialog.TAG);
        }
    }

    private void showMessage(String text){
        View snackbarContainer = findViewById(R.id.coordinator);
        if (snackbarContainer == null) {
            snackbarContainer = mContainer;
        }
        Snackbar.make(snackbarContainer, text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void pincodeSelected(String token, String pincode) {
        if (myPrefs.token().get().isEmpty()) {
            String cryptedToken = "";
            try {
                cryptedToken = Base64.encodeToString(Crypto.encrypt(token, pincode), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            myPrefs.token().put(cryptedToken);

            showMessage("Авторизация успешно завершена");
            mTokenRequestInProgress = false;
        } else {
            showMessage("Сначала удалите старый токен");
        }
    }
}
