package com.example.tammy.happypai2.share;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.tammy.happypai2.AskPositionActivity;
import com.example.tammy.happypai2.CameraActivity;
import com.example.tammy.happypai2.R;

public class CompositionActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton bt_cancel, bt_sure, bt_com_four, bt_com_six;
    private int composition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composition);

        initView();
    }

    private void initView(){
        bt_cancel = (ImageButton)findViewById(R.id.bt_cancel);
        bt_sure = (ImageButton) findViewById(R.id.bt_sure);
        bt_com_four = (ImageButton)findViewById(R.id.btn_com_four);
        bt_com_six = (ImageButton) findViewById(R.id.btn_com_six);

        bt_cancel.setOnClickListener(this);
        bt_sure.setOnClickListener(this);
        bt_com_four.setOnClickListener(this);
        bt_com_six.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_sure:
                Intent intent = new Intent(CompositionActivity.this, ShareEditActivity.class);
                intent.putExtra("composition",composition);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.btn_com_four:
                setComposition(0);
                break;
            case R.id.btn_com_six:
                setComposition(1);
                break;
            default:
                break;
        }
    }

    private void setComposition(int com){
        if (com == composition){
            composition = -1;
            bt_com_four.setImageResource(R.drawable.button_com_four);
            bt_com_six.setImageResource(R.drawable.button_com_six);
        }

        switch (com){
            case 0:
                composition=0;
                bt_com_four.setImageResource(R.drawable.button_com_four_b);
                bt_com_six.setImageResource(R.drawable.button_com_six);
                break;
            case 1:
                composition=1;
                bt_com_four.setImageResource(R.drawable.button_com_four);
                bt_com_six.setImageResource(R.drawable.button_com_six_b);
                break;
            default:
                break;
        }
    }
}
