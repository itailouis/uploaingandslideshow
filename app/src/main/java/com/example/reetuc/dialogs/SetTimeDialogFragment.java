package com.example.reetuc.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.reetuc.R;

import java.util.Calendar;

public class SetTimeDialogFragment extends DialogFragment {

  private TimePicker timePicker1;
  private TextView time;
  private Calendar calendar;
  private String format = "";
  private String selectedTime="";

  DialogListener dialogListener;

  public static SetTimeDialogFragment newInstance(String param1) {
    final SetTimeDialogFragment fragment = new SetTimeDialogFragment();
    //Bundle args = new Bundle();
   // args.putString(ARG_PARAM1, param1);
    //args.putString(ARG_PARAM2, param2);
    //fragment.setArguments(args);

    return fragment;
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_settime_dialog, container, false);

  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


    Button btnDone = view.findViewById(R.id.btnDone);

    timePicker1 = (TimePicker) view.findViewById(R.id.timePicker1);
    calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int min = calendar.get(Calendar.MINUTE);


    showTime(hour, min);
    btnDone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setTime();
        //DialogListener dialogListener = (DialogListener) getChildFragmentManager();
        dialogListener.onFinishEditDialog(selectedTime);
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
    void onFinishEditDialog(String inputText);
  }
  public void showTime(int hour, int min) {
    if (hour == 0) {
      hour += 12;
      format = "AM";
    } else if (hour == 12) {
      format = "PM";
    } else if (hour > 12) {
      hour -= 12;
      format = "PM";
    } else {
      format = "AM";
    }

    selectedTime = new StringBuilder().append(hour).append(" : ").append(min).append(" ").append(format).toString();
  }

  public void setTime() {
    int hour = timePicker1.getCurrentHour();
    int min = timePicker1.getCurrentMinute();
    showTime(hour, min);
  }

}

