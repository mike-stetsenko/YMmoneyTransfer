package com.mairos.ymoneytransfer.view.fragments;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mairos.ymoneytransfer.R;
import com.mairos.ymoneytransfer.storage.SharedPrefs_;
import com.mairos.ymoneytransfer.storage.Storage;
import com.mairos.ymoneytransfer.storage.models.PaymentInfo;
import com.mairos.ymoneytransfer.view.adapters.PaymentInfoAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

@EFragment(R.layout.fragment_payments_list)
public class PaymentsListFragment extends Fragment {

    public static final String TAG = "PaymentsListFragment";

    public interface Callback {
        void onStartCreatingPayment();
        void onStartAuthorization();
    }

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.payments_list)
    ListView mPaymentsList;

    @ViewById(R.id.empty_view)
    TextView mEmptyListText;

    @Pref
    SharedPrefs_ myPrefs;

    public static PaymentsListFragment newInstance() {
        return PaymentsListFragment_.builder()
                .build();
    }

    @AfterViews
    void init(){

        List<PaymentInfo> infoList = Storage.get().get(PaymentInfo.class);
        PaymentInfo[] array = new PaymentInfo[infoList.size()];

        mPaymentsList.setAdapter(new PaymentInfoAdapter(getActivity(), infoList.toArray(array)));

        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(mToolbar);

        if (activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setTitle("Список платежей");
        }

        mPaymentsList.setEmptyView(mEmptyListText);
    }

    @Click(R.id.add_payment_fab)
    void createPayment(){

        if (!myPrefs.token().get().isEmpty()) {

            if(getActivity() instanceof Callback){
                ((Callback) getActivity()).onStartCreatingPayment();
            }

        } else {

            if(getActivity() instanceof Callback){
                ((Callback) getActivity()).onStartAuthorization();
            }

        }
    }

    public void updateList() {

        if(mPaymentsList.getAdapter() != null && mPaymentsList.getAdapter() instanceof PaymentInfoAdapter){
            List<PaymentInfo> infoList = Storage.get().get(PaymentInfo.class);
            PaymentInfo[] array = new PaymentInfo[infoList.size()];

            ((PaymentInfoAdapter) mPaymentsList.getAdapter()).updateList(infoList.toArray(array));
        }
    }
}
