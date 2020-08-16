package com.example.reetuc.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.reetuc.R;

import java.util.Calendar;

public class SetDateDialogFragment extends DialogFragment {

  private DatePicker timePicker1;
  private TextView time;
  private Calendar calendar;
  private String format = "";
  private String selectedTime="";

  private int year, month, day;

  DialogListener dialogListener;

  public static SetDateDialogFragment newInstance(String param1) {
    final SetDateDialogFragment fragment = new SetDateDialogFragment();
    //Bundle args = new Bundle();
    // args.putString(ARG_PARAM1, param1);
    //args.putString(ARG_PARAM2, param2);
    //fragment.setArguments(args);

    return fragment;
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_setdate_dialog, container, false);

  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


    Button btnDone = view.findViewById(R.id.btnDone);


    timePicker1 = (DatePicker) view.findViewById(R.id.timePicker1);
    calendar = Calendar.getInstance();
    year = calendar.get(Calendar.YEAR);

    month = calendar.get(Calendar.MONTH);
    day = calendar.get(Calendar.DAY_OF_MONTH);
    showDate(year, month+1, day);



    btnDone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setTime();
        //DialogListener dialogListener = (DialogListener) getChildFragmentManager();
        dialogListener.onFinishEditDateDialog(selectedTime);
        dismiss();
      }
    });
    Button btnCancel = view.findViewById(R.id.btnCancel);
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getTargetFragment() instanceof DialogListener) {
      dialogListener = (DialogListener) getTargetFragment();
    } else {
      throw new RuntimeException( " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  public interface DialogListener {
    void onFinishEditDateDialog(String inputText);
  }
  public void showDate(int year, int month, int day) {

    selectedTime = new StringBuilder().append(day).append("/").append(month).append("/").append(year).toString();
  }

  public void setTime() {
    int day = timePicker1.getDayOfMonth();
    int month = timePicker1.getMonth();
    int year = timePicker1.getYear();
    showDate(year,month,day);
  }

}

