package com.uva.eletricafacil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends Activity {

    private boolean isChecking = true;
    private int rdBtnId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DecimalFormat formater = new DecimalFormat("#.##");
        final Button confBtn = findViewById(R.id.confBtn);
        final EditText compEditText = findViewById(R.id.altEditText);
        final EditText largEditText = findViewById(R.id.largEditText);
        final TextView qntTextView = findViewById(R.id.qntTextView);
        final TextView areaTextView = findViewById(R.id.areaTextView);
        final TextView potenciaTextView = findViewById(R.id.potTextView);
        final TextView custoTextView = findViewById(R.id.custoTextView);
        final EditText tensaoEditText = findViewById(R.id.tensaoEditText);
        final EditText correnteEditText = findViewById(R.id.correnteEditText);
        final LinearLayout potenciaLayout = findViewById(R.id.potenciaLayout);
        final LinearLayout saidaLayout = findViewById(R.id.saidaLayout);
        final RadioGroup rdGroup1 = findViewById(R.id.rdGroup1);
        final RadioGroup rdGroup2 = findViewById(R.id.rdGroup2);
        final Switch sw1 = findViewById(R.id.sw1);

        rdGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    rdGroup2.clearCheck();
                    rdBtnId = checkedId;
                }
                isChecking = true;
            }
        });

        rdGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    rdGroup1.clearCheck();
                    rdBtnId = checkedId;
                }
                isChecking = true;
            }
        });

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    potenciaLayout.setVisibility(View.VISIBLE);

                }
                else
                {
                    potenciaLayout.setVisibility(View.GONE);
                }
            }
        });

        confBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtRdBtn;
                try {
                    RadioButton radioButton = findViewById(rdBtnId);
                    txtRdBtn = radioButton.getText().toString();
                }
                catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"Selecione o tipo de ambiente", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(sw1.isChecked()){
                    try{
                        double tensao = Double.parseDouble(tensaoEditText.getText().toString());
                        double corrente = Double.parseDouble(correnteEditText.getText().toString());
                        double potencia = tensao*corrente;
                        double custo = potencia*0.68/100;

                        potenciaTextView.setText(getString(R.string.potencia, formater.format(potencia)));
                        custoTextView.setText(getString(R.string.custo, custo));
                    }
                    catch(NumberFormatException e){
                        Toast.makeText(getApplicationContext(), "Valores de tensão/corente inválidos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                try {
                    double largura = Double.parseDouble(largEditText.getText().toString());
                    double comprimento = Double.parseDouble(compEditText.getText().toString());
                    double area = largura * comprimento;
                    double perimetro = 2 * (largura + comprimento);
                    int tomadas = 0;

                    areaTextView.setText(getString(R.string.area,formater.format(area)));

                    switch (txtRdBtn) {
                        case "Cozinha": {
                            double aux = perimetro % 3.5;
                            if (aux == 0)
                                tomadas = (int) (perimetro / 3.5);
                            else
                                tomadas = (int) (perimetro / 3.5) + 1;
                            qntTextView.setText(getString(R.string.min_tomadas_extra, tomadas, "ao lavatório"));
                        }
                        break;
                        case "Banheiro":
                            qntTextView.setText(getString(R.string.min_tomdadas_ban_1));
                            break;
                        case "Varanda":
                            if (area > 2)
                                qntTextView.setText(getString(R.string.min_tomadas_1));
                            else
                                qntTextView.setText(getString(R.string.min_tomadas_0));
                            break;
                        case "Outros Ambientes":
                            if (area > 6) {
                                double aux = perimetro % 5;
                                if (aux == 0)
                                    tomadas = (int) (perimetro / 5);
                                else
                                    tomadas = (int) (perimetro / 5) + 1;
                                qntTextView.setText(getString(R.string.min_tomadas, tomadas));
                            }
                    }
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    saidaLayout.setVisibility(View.VISIBLE);
                }
                catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Por favor, insira dimensões válidas", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro. Tente novamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        largEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Button button = findViewById(R.id.confBtn);
                    button.performClick();
                    return true;
                }
                return false;
            }
        });


    }

}
