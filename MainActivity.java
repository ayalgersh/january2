package com.ayalgersh.january;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private EditText etPhone;
    private EditText etMsg;

    private static final int REQUEST_CODE_CALL = 1;
    private static final int REQUEST_CODE_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etMsg = (EditText) findViewById(R.id.etMsg);
    }

    public void setTimer(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, 12, 20, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute + 5);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "WAKIE WAKIE");

        startActivity(intent);
    }

    public void call(@Nullable View view) {

        Uri phoneUri = Uri.parse("tel:" + etPhone.getText());
        Intent callIntent = new Intent(Intent.ACTION_CALL, phoneUri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL);
            return;
        }
        startActivity(callIntent);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_SMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS(null);
                } else {
                    Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT);
                }
                break;
            case REQUEST_CODE_CALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(null);
                } else {
                    Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT);
                }
                break;

        }

    }

    public void sendSMS(@Nullable View view) {
        SmsManager smsManager = SmsManager.getDefault();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_SMS);
            return;
        }

        smsManager.sendTextMessage(etPhone.getText().toString(), null, etMsg.getText().toString(), null, null);
    }




}