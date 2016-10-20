package com.mathapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {


    Button button;
    EditText value1;
    EditText value2;
    TextView result;
    String mathResult;
    Switch remoteSwitch;
    Spinner mathOperationSpinner;
    MathSimple simple = new MathSimple();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize UI widgets
        button = (Button) findViewById(R.id.button);
        value1 = (EditText) findViewById(R.id.value1);
        value2 = (EditText) findViewById(R.id.value2);
        result = (TextView) findViewById(R.id.result);
        remoteSwitch = (Switch) findViewById(R.id.remoteswitch);
        mathOperationSpinner = (Spinner) findViewById(R.id.spinner);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get math operation
                Boolean remoteStatus = remoteSwitch.isChecked();
                //String operation = checkRadioButton.getText().toString();
                String operation = String.valueOf(mathOperationSpinner.getSelectedItem());

                // get simple math values
                Integer sn1 = Integer.valueOf(value1.getText().toString());
                Integer sn2 = Integer.valueOf(value2.getText().toString());

                // if remoteSwitch enabled then make a remote Rest Call
                if (remoteStatus)  {
                    getSimpleRemote(operation, sn1, sn2);
                } else {
                    getSimpleLocal(operation, sn1, sn2);
                }

            }
        });
    }

    private void getSimpleLocal(String operation, Integer sn1, Integer sn2) {
        mathResult = simple.process(operation, sn1, sn2).toString();
        result.setText(mathResult.toString());

    }

    private void getSimpleRemote(String operation, Integer sn1, Integer sn2){

        String host = ((EditText)findViewById(R.id.host)).getText().toString();
        String port = ((EditText)findViewById(R.id.port)).getText().toString();

        String urlString = "http://"+host+":"+port+"/MathProxy/rest/hello/math?" +
                "operation="+operation+"&value1="+sn1.toString()+"&value2="+sn2.toString();
        System.out.print(urlString);
        Object[] mathRequest = {urlString,result,button};
        new MathSimpleRemote().execute(mathRequest);

        // disable button on remote request
        button.setEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
