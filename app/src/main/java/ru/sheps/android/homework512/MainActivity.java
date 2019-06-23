package ru.sheps.android.homework512;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultField;
    private EditText numberField;
    private TextView operationField;
    private Double operand = null;
    private String lastOperation = "=";

    private Button switchBtn;
    private Button btnSetting;

    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField = (TextView) findViewById(R.id.resultField);
        numberField = (EditText) findViewById(R.id.numberField);
        operationField = (TextView) findViewById(R.id.operationField);

        switchBtn = findViewById(R.id.switchBtn);
        switchBtn.setOnClickListener(this);

        backgroundImage = findViewById(R.id.backgroundImage);
    }

    private void switchView() {
        View normal = findViewById(R.id.normal);
        View engineer = findViewById(R.id.engineer);
        if (normal.getVisibility() == View.VISIBLE) {
            normal.setVisibility(View.GONE);
            engineer.setVisibility(View.VISIBLE);
        } else {
            normal.setVisibility(View.VISIBLE);
            engineer.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if (operand != null)
            outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand = savedInstanceState.getDouble("OPERAND");
        resultField.setText(operand.toString());
        operationField.setText(lastOperation);
    }

    public void onNumberClick(View view) {

        Button button = (Button) view;
        numberField.append(button.getText());

        if (lastOperation.equals("=") && operand != null) {
            operand = null;
        }
    }

    public void onOperationClick(View view) {

        Button button = (Button) view;
        String op = button.getText().toString();
        String number = numberField.getText().toString();

        if (number.length() > 0) {
            number = number.replace(',', '.');
            try {
                performOperation(Double.valueOf(number), op);
            } catch (NumberFormatException ex) {
                numberField.setText("");
            }
        }
        lastOperation = op;
        operationField.setText(lastOperation);
    }

    private void performOperation(Double number, String operation) {

        if (operand == null) {
            operand = number;
        } else {
            if (lastOperation.equals("=")) {
                lastOperation = operation;
            }
            switch (lastOperation) {
                case "=":
                    operand = number;
                    break;
                case "/":
                    if (number == 0) {
                        operand = 0.0;
                    } else {
                        operand /= number;
                    }
                    break;
                case "*":
                    operand *= number;
                    break;
                case "+":
                    operand += number;
                    break;
                case "-":
                    operand -= number;
                    break;
            }
        }
        resultField.setText(operand.toString().replace('.', ','));
        numberField.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.switchBtn):
                switchView();
                break;
        }
    }

    public void onClickBtnSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + data.getStringExtra("ImageFileName");
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        Drawable drawable = new BitmapDrawable(bitmap);

        backgroundImage.setImageDrawable(drawable);
    }
}
