package projects.shahabgt.com.onlinelibrary.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jaredrummler.materialspinner.MaterialSpinner;

import projects.shahabgt.com.onlinelibrary.R;

public class SettingsDialog extends Dialog {

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private Context context;
    private Button cancel,ok;


    public SettingsDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_settings);
        sp = context.getSharedPreferences("settings",0);
        spEditor = sp.edit();
        setFont();
        setColor();
        setSize();
        setButtons();


    }

    private void setFont(){
        MaterialSpinner spinner =  findViewById(R.id.fontspinner);
        spinner.setItems("وزیر", "بدر", "کامران", "هما","میترا","تیتر");
        switch (sp.getString("font","vazir")){
            case "vazir": spinner.setSelectedIndex(0);
                break;
            case "badr":spinner.setSelectedIndex(1);
                break;
            case "kamran":spinner.setSelectedIndex(2);
                break;
            case "homa":spinner.setSelectedIndex(3);
                break;
            case "mitra":spinner.setSelectedIndex(4);
                break;
            case "titr":spinner.setSelectedIndex(5);
                break;

        }
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                switch (position){
                    case 0: spEditor.putString("font","vazir");
                        break;
                    case 1:spEditor.putString("font","badr");
                        break;
                    case 2:spEditor.putString("font","kamran");
                        break;
                    case 3:spEditor.putString("font","homa");
                        break;
                    case 4:spEditor.putString("font","mitra");
                        break;
                    case 5:spEditor.putString("font","titr");
                        break;
                }

            }
        });
    }
    private void setColor(){
        MaterialSpinner spinner =  findViewById(R.id.colorspinner);
        spinner.setItems("مشکی", "آبی", "سبز", "فیروزه ای","قرمز","ارغوانی");
        switch (sp.getInt("color",Color.BLACK)){

            case Color.BLACK: spinner.setSelectedIndex(0);
                break;
            case Color.BLUE:spinner.setSelectedIndex(1);
                break;
            case Color.GREEN:spinner.setSelectedIndex(2);
                break;
            case Color.CYAN:spinner.setSelectedIndex(3);
                break;
            case Color.RED:spinner.setSelectedIndex(4);
                break;
            case Color.MAGENTA:spinner.setSelectedIndex(5);
                break;

        }
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                switch (position){
                    case 0: spEditor.putInt("color",Color.BLACK);
                        break;
                    case 1:spEditor.putInt("color",Color.BLUE);
                        break;
                    case 2:spEditor.putInt("color",Color.GREEN);
                        break;
                    case 3:spEditor.putInt("color",Color.CYAN);
                        break;
                    case 4:spEditor.putInt("color",Color.RED);
                        break;
                    case 5:spEditor.putInt("color",Color.MAGENTA);
                        break;
                }

            }
        });
    }
    private void setSize(){
        MaterialSpinner spinner =  findViewById(R.id.sizespinner);
        spinner.setItems("12","14","16","18","20","22","25","28","32");

        switch ((int)sp.getFloat("size",16)){
            case 12: spinner.setSelectedIndex(0);
                break;
            case 14: spinner.setSelectedIndex(1);
                break;
            case 16: spinner.setSelectedIndex(2);
                break;
            case 18:spinner.setSelectedIndex(3);
                break;
            case 20:spinner.setSelectedIndex(4);
                break;
            case 22:spinner.setSelectedIndex(5);
                break;
            case 25:spinner.setSelectedIndex(6);
                break;
            case 28:spinner.setSelectedIndex(7);
                break;
            case 32:spinner.setSelectedIndex(8);
                break;

        }
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                switch (position){
                    case 0: spEditor.putFloat("size",12);
                        break;
                    case 1: spEditor.putFloat("size",14);
                        break;
                    case 2: spEditor.putFloat("size",16);
                        break;
                    case 3:spEditor.putFloat("size",18);
                        break;
                    case 4:spEditor.putFloat("size",20);
                        break;
                    case 5:spEditor.putFloat("size",22);
                        break;
                    case 6:spEditor.putFloat("size",25);
                        break;
                    case 7:spEditor.putFloat("size",28);
                        break;
                    case 8:spEditor.putFloat("size",32);
                        break;
                }

            }
        });
    }
    private void setButtons(){
        cancel = findViewById(R.id.settings_cancel);
        ok = findViewById(R.id.settings_ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spEditor.commit();
                System.exit(0);
            }
        });


    }
}
