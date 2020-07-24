package com.example.p4tipcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    EditText etAmount;
    TextView tvPercent;
    SeekBar sbPercent;
    TextView tvTip;
    TextView tvTotal,tvTpp;
    Spinner spin;
    RadioGroup radioGroup;
    RadioButton radioBut_No, radioBut_Tip, radioBut_Total;
    int num_of_customers;
    String[] list={"1","2","3","4","5","6","7","8"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etAmount = findViewById(R.id.et_amount);
        tvPercent = findViewById(R.id.tv_percent);
        sbPercent = findViewById(R.id.sb_percent);
        tvTip = findViewById(R.id.tv_tip);
        tvTotal = findViewById(R.id.tv_total);
        tvTpp = findViewById(R.id.tv_tpp);
        spin=findViewById(R.id.spinner);

        spin.setOnItemSelectedListener(this);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBut_No) {
                    calculate();
                } else if (checkedId == R.id.radioBut_Tip) {
                    double amount = Double.parseDouble(etAmount.getText().toString());
                    int percent = sbPercent.getProgress();
                    double tip = amount*percent/100.0;
                    int t= (int) Math.round(tip);
                    double total = amount + t;
                    double totalperperson=total/ num_of_customers;
                    tvTip.setText(String.valueOf(t));
                    tvTotal.setText(String.valueOf(total));
                    tvTpp.setText(String.valueOf(new DecimalFormat("##.##").format(totalperperson)));
                } else {
                    double amount = Double.parseDouble(etAmount.getText().toString());
                    int percent = sbPercent.getProgress();
                    double tip = amount*percent/100.0;
                    double total = amount + tip;
                    double rounded_total=(double) Math.round(total);
                    double totalperperson=rounded_total/ num_of_customers;
                    tvTip.setText(String.valueOf(tip));
                    tvTotal.setText(String.valueOf(rounded_total));
                    tvTpp.setText(String.valueOf(new DecimalFormat("##.##").format(totalperperson)));
                }
            }
        });

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(arrayAdapter);

        sbPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int percent = progress;
                tvPercent.setText(String.valueOf(percent) + "%");
                calculate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculate();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                String mimeType = "text/plain";
                calculate();
                ShareCompat.IntentBuilder
                        .from(this)
                        .setType(mimeType)
                        .setChooserTitle("Pick an app")
                        .setText("BILL:  Tip: "+tvTip.getText()+"  Total: "+tvTotal.getText()+"  Total per person: "+tvTpp.getText())
                        .startChooser();
                return true;
            case R.id.item2:
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(MainActivity.this);


                builder.setMessage("Choose the number of people sharing the tab to divide the total amount including tips equally. Use the three options to round the tip, the total or none.");
                AlertDialog alertDialog = builder.create();

                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void calculate() {
        if(etAmount.length()== 0) {
            etAmount.requestFocus();
            etAmount.setError("Enter Amount");
        } else {
            double amount = Double.parseDouble(etAmount.getText().toString());
            int percent = sbPercent.getProgress();
            double tip = amount*percent/100.0;
            double total = amount + tip;
            double tPP=total/ num_of_customers;
            tvTip.setText(String.valueOf(tip));
            tvTotal.setText(String.valueOf(total));
            tvTpp.setText(String.valueOf(new DecimalFormat("##.##").format(tPP)));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        num_of_customers = Integer.parseInt(list[i]);
        calculate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}