package com.example.myluggagecalc;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight, etRate;
    private RadioGroup rgClass;
    private Button btnCalculate;
    private TextView tvExcessWeight, tvExcessCharge, tvTotalPayable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find and set the Toolbar as the ActionBar so menu buttons appear
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bind UI elements
        etWeight = findViewById(R.id.etWeight);
        etRate = findViewById(R.id.etRate);
        rgClass = findViewById(R.id.rgClass);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvExcessWeight = findViewById(R.id.tvExcessWeight);
        tvExcessCharge = findViewById(R.id.tvExcessCharge);
        tvTotalPayable = findViewById(R.id.tvTotalPayable);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String weightStr = etWeight.getText().toString().trim();
                    String rateStr = etRate.getText().toString().trim();

                    // Good Design Practice: Check for empty input fields[cite: 4]
                    if (weightStr.isEmpty() || rateStr.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Error: Please fill in all input fields.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double weight = Double.parseDouble(weightStr);
                    double rate = Double.parseDouble(rateStr);

                    // Determine X based on ticket class (Economy = 20kg, Business = 40kg)[cite: 1]
                    int selectedId = rgClass.getCheckedRadioButtonId();
                    double allowanceX = 20.0; // default Economy[cite: 1]

                    if (selectedId == R.id.rbBusiness) {
                        allowanceX = 40.0;
                    }

                    // Calculations[cite: 1]
                    double excessWeight = Math.max(0.0, weight - allowanceX);
                    double excessCharge = excessWeight * rate;
                    double totalPayable = excessCharge + (0.08 * excessCharge); // Fixed formula with '*'

                    // Display outputs formatted properly[cite: 1, 4]
                    tvExcessWeight.setText(String.format("Excess Weight: %.2f kg", excessWeight));
                    tvExcessCharge.setText(String.format("Excess Charge: RM %.2f", excessCharge));
                    tvTotalPayable.setText(String.format("Total Amount Payable (inc. 8%% tax): RM %.2f", totalPayable));

                } catch (NumberFormatException e) {
                    // Good Design Practice / Exception Handling[cite: 4]
                    Toast.makeText(MainActivity.this, "Error: Please enter valid numbers.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Inflate toolbar menu (About & Share options)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle toolbar menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            // Redirects to About Page
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_share) {
            // Triggers Share App intent[cite: 2]
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String appUrl = "https://github.com/shawal555/excessbaggagecalculator"; // Update with your GitHub link[cite: 2, 3]
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Excess Baggage Fee Calculator App");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my mobile app for estimating excess baggage fees: " + appUrl);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}