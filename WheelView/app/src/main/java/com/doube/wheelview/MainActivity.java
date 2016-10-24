package com.doube.wheelview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mBirth;
    private TextView mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBirth = (TextView) findViewById(R.id.tv_birth);
        mAddress = (TextView) findViewById(R.id.tv_address);

        mBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ChangeBirthDialog mChangeBirthDialog = new ChangeBirthDialog(MainActivity.this);
                mChangeBirthDialog.setDate(2015, 03, 29);
                mChangeBirthDialog.show();
                mChangeBirthDialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {

                    @Override
                    public void onClick(String year, String month, String day) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this,
                                year + "-" + month + "-" + day,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        mAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ChangeAddressDialog mChangeAddressDialog = new ChangeAddressDialog(
                        MainActivity.this);
                mChangeAddressDialog.setAddress("四川", "自贡");
                mChangeAddressDialog.show();
                mChangeAddressDialog
                        .setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city) {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this,
                                        province + "-" + city,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


    }
}
