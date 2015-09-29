package com.mairos.ymoneytransfer.view.fragments;

import android.app.Fragment;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mairos.ymoneytransfer.R;
import com.mairos.ymoneytransfer.network.SpiceManagerProvider;
import com.mairos.ymoneytransfer.network.events.ProcessPaymentEvent;
import com.mairos.ymoneytransfer.network.events.RequestPaymentEvent;
import com.mairos.ymoneytransfer.network.requestResults.RequestPaymentResultCode;
import com.mairos.ymoneytransfer.network.requests.ProcessPaymentRequest;
import com.mairos.ymoneytransfer.network.requests.RequestPaymentRequest;
import com.mairos.ymoneytransfer.storage.Crypto;
import com.mairos.ymoneytransfer.storage.SharedPrefs_;
import com.octo.android.robospice.persistence.DurationInMillis;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_create_payment)
public class CreatePaymentFragment extends Fragment implements PincodeFragmentDialog.Callback{

    public  interface Callback {
        void onPaymentAccepted();
    }

    public static final String TAG = "CreatePaymentFragment";

    @ViewById(R.id.request_payment_fab)
    FloatingActionButton mFab;

    @ViewById(R.id.loading_progress)
    LinearLayout mLoadingProgress;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.to)
    EditText mToPerson;

    @ViewById(R.id.amount_due)
    EditText mAmountDue;

    @ViewById(R.id.amount)
    EditText mAmount;

    @ViewById(R.id.comment)
    EditText mComment;

    @ViewById(R.id.expire_period)
    EditText mExpirePeriod;

    @Pref
    SharedPrefs_ myPrefs;

    @SystemService
    ConnectivityManager mConnectivityManager;

    @InstanceState
    boolean mIsLoading = false;

    public static CreatePaymentFragment newInstance() {
        return CreatePaymentFragment_.builder()
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @AfterViews
    void init(){
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(mToolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Новый платёж");
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }

        showLoadingDialog(mIsLoading);
    }

    public void onEventMainThread(ProcessPaymentEvent event){
        mIsLoading = false;
        showLoadingDialog(mIsLoading);

        EventBus.getDefault().removeStickyEvent(event);
    }

    @TextChange(R.id.amount_due)
    void onTextAmountChanges(CharSequence text, TextView hello, int before, int start, int count) {
        String amountDue = mAmountDue.getText().toString();
        mAmount.setText(getAmount(amountDue));
    }

    private String getAmount(String amountDue){
        if (amountDue.isEmpty()){
            amountDue = "0";
        }

        BigDecimal bdAmountDue = new BigDecimal(amountDue);

        if(bdAmountDue.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }

        BigDecimal percent = bdAmountDue.multiply(new BigDecimal("0.005"));
        if (percent.compareTo(new BigDecimal("0.01")) <= 0) {
            percent = new BigDecimal("0.01");
        }
        BigDecimal dbAmount = bdAmountDue.add(percent).setScale(2, BigDecimal.ROUND_HALF_UP);

        return dbAmount.toString();
    }

    public void onEventMainThread(RequestPaymentEvent event){
        if (event.getResult().getStatus().equals(RequestPaymentResultCode.SUCCESS)) {

            if(getActivity() instanceof SpiceManagerProvider){

                ProcessPaymentRequest request = new ProcessPaymentRequest(event.getToken(),
                        event.getResult().getRequestId(), event.getResult().getProtectionCode());
                request.setRetryPolicy(null);

                ((SpiceManagerProvider) getActivity()).getSpiceManager().
                        execute(request, 0, DurationInMillis.ALWAYS_EXPIRED, null);
            }

        } else {

            Snackbar.make(mToPerson, event.getResult().getError() + " - " +
                    event.getResult().getErrorDescription(), Snackbar.LENGTH_LONG).show();

            mIsLoading = false;
            showLoadingDialog(mIsLoading);
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    private boolean amountDueIsCorrect(){
        String amountDue = mAmountDue.getText().toString();
        return !amountDue.isEmpty() && (new BigDecimal(amountDue).compareTo(BigDecimal.ZERO) == 1);
    }

    private String getAmountDueFromUi(){
        String amountDue = mAmountDue.getText().toString();
        return new BigDecimal(amountDue).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Click(R.id.request_payment_fab)
    void createPayment(){

        if(mToPerson.getText().toString().isEmpty() || !amountDueIsCorrect() ||
                mComment.getText().toString().isEmpty() || mExpirePeriod.getText().toString().isEmpty()){

            Snackbar.make(mToPerson, "Заполните все поля", Snackbar.LENGTH_SHORT).show();
        } else {

            if (mConnectivityManager.getActiveNetworkInfo() == null ||
                    !mConnectivityManager.getActiveNetworkInfo().isConnected()){

                Snackbar.make(mToPerson, "Сеть недоступна", Snackbar.LENGTH_SHORT).show();

            } else if(getActivity() instanceof SpiceManagerProvider){

                showGetCriptokeyDialog();
            }
        }
    }

    private void startPaymentProcess(String token){

        RequestPaymentRequest request = new RequestPaymentRequest(token,
                mToPerson.getText().toString(), mAmountDue.getText().toString(),
                mComment.getText().toString(), Integer.parseInt(mExpirePeriod.getText().toString()));
        request.setRetryPolicy(null);

        ((SpiceManagerProvider) getActivity()).getSpiceManager().
                execute(request, 0, DurationInMillis.ALWAYS_EXPIRED, null);

        mIsLoading = true;
        showLoadingDialog(mIsLoading);
    }

    private void showGetCriptokeyDialog(){

        if (getFragmentManager().findFragmentByTag(PincodeFragmentDialog.TAG) == null) {
            PincodeFragmentDialog pincodeDialog = PincodeFragmentDialog.newInstance(
                    "Запрос ключа доступа", myPrefs.token().get());
            pincodeDialog.setTargetFragment(this, 0);
            pincodeDialog.show(getFragmentManager(), PincodeFragmentDialog.TAG);
        }
    }

    @Override
    public void pincodeSelected(String token, String pincode) {
        try {
            token = Crypto.decrypt(Base64.decode(token, Base64.DEFAULT), pincode);
            startPaymentProcess(token);
        } catch (Exception e) {
            Snackbar.make(mToPerson, "Доступ запрещён. Ключ чтоли забыли?", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showLoadingDialog(boolean isLoading) {
        mFab.setEnabled(!isLoading);
        mLoadingProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
