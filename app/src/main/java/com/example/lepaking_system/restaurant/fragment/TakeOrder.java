package com.example.lepaking_system.restaurant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.activity.CaptureAct;
import com.google.zxing.integration.android.IntentIntegrator;

public class TakeOrder extends Fragment implements View.OnClickListener {

    Button scanQRCodeButton;
    String restaurant_email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        restaurant_email = this.getArguments().getString("email");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_take_order, container, false);

        //Hooks
        scanQRCodeButton = root.findViewById(R.id.take_order_scan_qr_code_button);

        scanQRCodeButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        scanQRCode();
    }

    public void scanQRCode() {
        IntentIntegrator integrator = new IntentIntegrator(TakeOrder.this.getActivity());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        TakeOrder.this.getActivity().startActivityForResult(integrator.createScanIntent(), 3);
    }
}
