package com.example.myunrealapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private EditText editText;

    private List<City> list = new ArrayList<>();

    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);

        init();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String city = editText.getText().toString();
                        result = getTemp(city);

                    }
                });
                thread.start();
                while (thread.isAlive());
                textView.setText(result);
            }
        });
    }

    public void init(){
        list.add(new City("Chuchin", "53.604412", "24.74256"));
    }
    public String getTemp(String city){
       City city1 = null;
       for (City c: list){
           if (city.equals(c.getName())){
               city1 = c;
               break;
           }
       }
       if (city1!=null){
           try {
               URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+city1.getLat()+"&lon="+city1.getLon()+"&appid=8214b5994813fe7379c043e827584008");
               BufferedReader bufferedReader = new BufferedReader(
                       new InputStreamReader(url.openStream())
               );
               String temp = bufferedReader.readLine();
               JSONObject jsonObject = new JSONObject(temp);
               String main = jsonObject.get("main").toString();
               jsonObject = new JSONObject(main);
               String temp1 = jsonObject.get("temp").toString();
               return temp1;
           }catch (Exception e){
               e.printStackTrace();
               return "";
           }
       }else {
           return "Мы пока не знаем погоду в таком городе";
       }
    }
}