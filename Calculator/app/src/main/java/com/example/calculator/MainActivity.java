package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText result;                                                                         // Deklariranje na promenlivite result i displayOperation koi gi prikazuvaat rezultatot od kalkulacijata i operacijata soodvetno.
    private EditText newNumber;
    private TextView displayOperation;
    private static final String OPERATION_SAVED = "OperationSaving";                                          // OPERATION_SAVED & RESULT_SAVED are reserved memory to save the value of variables operand1 wh
    private static final String RESULT_SAVED = "ResultSaving";


    // Variables to hold the operands and type of operations.

    private Double operand1 = null;
    // private Double operand2 = null;                                                              // Deklariranje i inicijaliziranje na operandite operand1 i operand2(poradi pricistuvanje na kodot ke bide izbrisan)
    private String pendingOperation = "=";                                                          // koi ke gi sodrzat broevite vrz koi ke bide primeneta operacijata
    // koja ke bide smestena vo promenlivata pendingOperation.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (EditText) findViewById(R.id.result);                                              // Kreiranje na promenlivi za sekoe kopce i porvrzuvanje so kreiranite objekti preku funkcijata findViewById.
        newNumber = (EditText) findViewById(R.id.newNumber);
        displayOperation = (TextView) findViewById(R.id.operation);

        Button button0 = (Button) findViewById(R.id.button0);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button buttonDot = (Button) findViewById(R.id.buttonDot);

        Button buttonEquals = (Button) findViewById(R.id.buttonEquals);
        Button buttonDivide = (Button) findViewById(R.id.buttonDivide);
        Button buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        Button buttonNeg = (Button) findViewById(R.id.buttonNeg);
        Button buttonReset = (Button) findViewById(R.id.buttonReset);
        Button buttonC = (Button) findViewById(R.id.buttonC);

        View.OnClickListener listener = new View.OnClickListener() {                                // Kreiranje na metod listener koj ke bide odgovoren za dodavanje na nov vnesen broj vo promenlivata newNumber.
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                newNumber.append(b.getText().toString());
            }
        };

        button0.setOnClickListener(listener);                                                       // Povikuvanje na metodot listener za sekoe od kopcinjata so vrednost od 0-9 vklucuvajki ja i tockata za vnesuvanje na decimalni broevi.
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
        buttonDot.setOnClickListener(listener);
        // Kreiranje na metod opListener ke izvrsuva poveke operacii. Najprvo proveruva dali ima vneseno broj vo promenlivata newNumber izvrsuvanje na presmetka.
        View.OnClickListener opListener = new View.OnClickListener() {                              // Dokolku e ispolnet uslovot togas se povikuva metodot performOperation.
            @Override                                                                       // Promenlivata pendingOperation se setira da ja sodrzi vrednosta na operacijata vo cekanje. Vo displayOperation se prikazuva operacijata vo cekanje.
            public void onClick(View v) {
                Button b = (Button) v;
                String op = b.getText().toString();
                String value = newNumber.getText().toString();
                try {
                    Double doubleValue = Double.valueOf(value);
                    performOperation(doubleValue, op);
                } catch (NumberFormatException e) {
                    newNumber.setText("");
                }
                pendingOperation = op;
                displayOperation.setText(op);
            }
        };

        View.OnClickListener negNumber = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double value;
                if (newNumber.length()==0) {
                    newNumber.setText("-");
                }
                else if (newNumber.getText().toString().equals("-")){
                    newNumber.setText("");
                }
                else {
                        Double negacija = Double.valueOf(newNumber.getText().toString());
                        value = negacija *(-1);
                        newNumber.setText(value.toString());
                }

            }
        };

       buttonReset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               newNumber.setText("");
               result.setText("");
               displayOperation.setText("");
               operand1 = null;
           }
       });

       buttonC.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String value = newNumber.getText().toString();
               if(value.length()==0){
                   newNumber.setText("");
               }
               else {
                   newNumber.setText(value.substring(0, value.length() - 1));
               }
           }
       });

        buttonEquals.setOnClickListener(opListener);
        buttonMultiply.setOnClickListener(opListener);
        buttonDivide.setOnClickListener(opListener);
        buttonMinus.setOnClickListener(opListener);
        buttonPlus.setOnClickListener(opListener);
        buttonNeg.setOnClickListener(negNumber);



    }

    private void performOperation(Double value, String operation) {
        if (operand1 == null) {                                                                     // Vo metodot performOperation se izvrsuva glavnata funkcija na kalkulatorot. Najprvo se proveruva dali operand1 e seuste prazen odnosno ima vrednost null.
            operand1 = value;                                                                       // Dokolku e prazen togas argumentot value ispraten od metodot opListener se dodeluva na istiot.

        } else {                                                                                    // Dokolku operand1 e veke setiran i sodrzi vrednost, argumentot value se dodeluva na promenlivata operand2.
            if (pendingOperation.equals("=")) {                                                     // Dokolku vnesenata operacija pendingOperation ispratena kako argument operation od metodot opListener e znakot za ednakvost, se sproveduva
                pendingOperation = operation;                                                       // switch funkcija za sekoja od operaciite, so toa sto operacijata za delenje proveruva dali operand2 e ednakov na 0.
            }
            switch (pendingOperation) {

                case "=":
                    operand1 = value;
                    break;

                case "/": {
                    if (value == 0) {
                        operand1 = 0.0;
                    } else
                        operand1 /= value;
                    break;
                }

                case "*":
                    operand1 *= value;
                    break;

                case "-":
                    operand1 -= value;
                    break;
                case "+":
                    operand1 += value;
                    break;
            }

        }
        result.setText(operand1.toString());
        newNumber.setText("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(OPERATION_SAVED, pendingOperation);
        if (operand1 != null) {
            outState.putDouble(RESULT_SAVED, operand1);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString(OPERATION_SAVED);
        operand1 = savedInstanceState.getDouble(RESULT_SAVED);
        displayOperation.setText(pendingOperation);
    }
}
