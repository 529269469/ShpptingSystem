package com.fhxh.shpptingsystem.view.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fhxh.shpptingsystem.R;
import com.fhxh.shpptingsystem.base_app.Constant;
import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;

import java.text.DecimalFormat;

/**
 * Created by  on 2021/5/4.
 */

public class SystemFragment extends Fragment {
    private Spinner TargetReportSpinner;
    private String TargetRecordType;
    private Switch GunVoiceSwitch;
    private boolean GunVoiceIsTrue;
    private Spinner TargetMianSpinner;
    private Spinner ShotNumSpinner;
    private Switch AutoPrintSwitch;
    private Switch ShotVoiceSwitch;
    private boolean ShotVoiceIsTrue;

    private OnListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system, container, false);
        CreatTargetMianSpinner(view);
        CreatTargetReportSpinner(view);
        CreatShotNumSpinner(view);
        CreatGunVoiceSwitch(view);
        CreatShotVoiceSwitch(view);
        CreatAutoPrintSwitch(view);
        setLight(view);
        setIp(view);
        return view;
    }

    public void setListener(OnListener mListener) {
        this.mListener = mListener;
    }

    private void CreatTargetMianSpinner(View view) {
        TargetMianSpinner = view.findViewById(R.id.fragment_system_target_mian_spinner);

        TargetMianSpinner.setSelection( MMKV.defaultMMKV().decodeInt("SelectTargetType") );

        TargetMianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MMKV.defaultMMKV().encode("SelectTargetType",TargetMianSpinner.getSelectedItemPosition());
                mListener.SelectTargetMian(SystemFragment.this, TargetMianSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void CreatTargetReportSpinner(View view) {
        TargetReportSpinner = view.findViewById(R.id.fragment_system_type_spinner);
        TargetRecordType =MMKV.defaultMMKV().decodeString("TargetRecordType");
        String[] TargetRecordTypestrs = getResources().getStringArray(R.array.TargetRecordTpye);
        for (int i = 0; i < TargetRecordTypestrs.length; i++) {
            if (TargetRecordTypestrs[i].equals(TargetRecordType)) {
                TargetReportSpinner.setSelection(i);
            }
        }
        TargetReportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (TargetRecordType!=null&&TargetRecordType!=""){
                    if (!TargetRecordType.equals(TargetReportSpinner.getSelectedItem().toString())) {
                        TargetRecordType = TargetReportSpinner.getSelectedItem().toString();
                        MMKV.defaultMMKV().encode("TargetRecordType",TargetReportSpinner.getSelectedItem().toString());
                        mListener.SelectTargetRecordType(SystemFragment.this, TargetRecordType);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void CreatShotNumSpinner(View view) {
        ShotNumSpinner = view.findViewById(R.id.fragment_system_shot_num_spinner);
        String[] shotnums = getResources().getStringArray(R.array.NumberOfBullets);
        int num = MMKV.defaultMMKV().decodeInt("NumberOfBullets");
        for (int i = 0; i < shotnums.length; i++) {
            if (Integer.parseInt(shotnums[i]) == num) {
                ShotNumSpinner.setSelection(i);
            }
        }
        ShotNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MMKV.defaultMMKV().encode("NumberOfBullets",Integer.parseInt(ShotNumSpinner.getSelectedItem().toString()));
                mListener.SelectShotNum(SystemFragment.this, Integer.parseInt(ShotNumSpinner.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void CreatGunVoiceSwitch(View view) {
        GunVoiceSwitch = view.findViewById(R.id.fragment_system_gun_voice_switch);
        GunVoiceIsTrue =MMKV.defaultMMKV().decodeBool("GunVoice");
        if (GunVoiceIsTrue) {
            GunVoiceSwitch.setChecked(true);
        } else {
            GunVoiceSwitch.setChecked(false);
        }
        GunVoiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GunVoiceIsTrue = GunVoiceSwitch.isChecked();
                MMKV.defaultMMKV().encode("GunVoice",GunVoiceIsTrue);
                mListener.SelectGunVoice(SystemFragment.this, GunVoiceIsTrue);
            }
        });
    }

    private void CreatShotVoiceSwitch(View view) {
        ShotVoiceSwitch = view.findViewById(R.id.fragment_system_shot_voice_switch);
        ShotVoiceIsTrue =MMKV.defaultMMKV().decodeBool("ShotVoice");
        if (ShotVoiceIsTrue) {
            ShotVoiceSwitch.setChecked(true);
        } else {
            ShotVoiceSwitch.setChecked(false);
        }
        ShotVoiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShotVoiceIsTrue = ShotVoiceSwitch.isChecked();
                MMKV.defaultMMKV().encode("ShotVoice",ShotVoiceIsTrue);
                mListener.SelectShotVoice(SystemFragment.this, ShotVoiceIsTrue);
            }
        });
    }

    private void CreatAutoPrintSwitch(View view) {
        AutoPrintSwitch = view.findViewById(R.id.fragment_system_auto_print_switch);
        if (MMKV.defaultMMKV().decodeBool("AutoPrint")) {
            AutoPrintSwitch.setChecked(true);
        } else {
            AutoPrintSwitch.setChecked(false);
        }
        AutoPrintSwitch.setOnClickListener(view1 -> {
            MMKV.defaultMMKV().encode("AutoPrint",AutoPrintSwitch.isChecked());
            mListener.SelectAutoPrint(SystemFragment.this, AutoPrintSwitch.isChecked());
        });
    }

    private void setLight(View view) {
        SeekBar seekBar = view.findViewById(R.id.main_light);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void setIp(View view) {
        String ip = MMKV.defaultMMKV().decodeString(Constant.ip);
        if (ip == null || ip.equals("")) {
            ip = "192.168.1.1";
        }
        int port = MMKV.defaultMMKV().decodeInt(Constant.port);
        if (port <= 0) {
            port = 9291;
        }

        EditText mip = view.findViewById(R.id.main_ip);
        EditText mport = view.findViewById(R.id.main_port);
        TextView textView = view.findViewById(R.id.main_socket_con);

        mip.setText(ip);
        mport.setText(String.valueOf(port));

        textView.setOnClickListener(view1 -> {
            if (mip.getText().toString() != null || mport.getText().toString() != null) {
                MMKV.defaultMMKV().encode(Constant.ip,mip.getText().toString());
                MMKV.defaultMMKV().encode(Constant.port,Integer.parseInt(mport.getText().toString()));
                ToastUtils.show("保存成功");
            } else {
                ToastUtils.show("服务器IP或端口号不能为空");
            }
        });
    }

    public String mFloat(int a, int b) {
        DecimalFormat df = new DecimalFormat("0.00");//设置保留位数
        return df.format((float) a / b);
    }


    public interface OnListener {
        void SelectTargetMian(SystemFragment systemFragment, int position);

        void SelectTargetRecordType(SystemFragment systemFragment, String SelectedTargetRecordType);

        void SelectShotNum(SystemFragment systemFragment, int shotnums);

        void SelectGunVoice(SystemFragment systemFragment, boolean isTrue);

        void SelectShotVoice(SystemFragment systemFragment, boolean isTrue);

        void SelectAutoPrint(SystemFragment systemFragment, boolean isAuto);

    }
}
