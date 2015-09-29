package com.mairos.ymoneytransfer.view.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mairos.ymoneytransfer.R;

public class PincodeFragmentDialog extends DialogFragment {

    public static final String TAG = "PincodeFragmentDialog";

    public interface Callback {
        void pincodeSelected(String token, String pincode);
    }

    private String mTitle = "";
    private String mToken = "";
    private EditText mCryptoKey;
    private Button mOkButton;

    public static PincodeFragmentDialog newInstance(String title, String token) {
        PincodeFragmentDialog f = new PincodeFragmentDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("token", token);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title");
        mToken = getArguments().getString("token");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.key_dialog_layout, container);

        mCryptoKey = (EditText) view.findViewById(R.id.crypto_key);
        mOkButton = (Button) view.findViewById(R.id.key_entered);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mCryptoKey.getText().toString();
                if (key.length() < 4) {
                    Toast.makeText(getActivity(), "Ну введите 4 цифры, лень чтоли!?", Toast.LENGTH_SHORT).show();
                } else {

                    if (getTargetFragment() instanceof CreatePaymentFragment &&
                            getTargetFragment() instanceof Callback){
                        ((Callback) getTargetFragment()).pincodeSelected(mToken, key);
                    } else {
                        if (getActivity() instanceof Callback){
                            ((Callback) getActivity()).pincodeSelected(mToken, key);
                        }
                    }
                    getDialog().cancel();
                }
            }
        });

        getDialog().setTitle(mTitle);
        setCancelable(false);

        return view;
    }

}

