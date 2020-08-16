package com.example.reetuc.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.reetuc.App;
import com.example.reetuc.R;
import com.example.reetuc.adapters.ImageAdapter;
import com.example.reetuc.models.ServerImages;
import com.example.reetuc.models.ServerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondFragment extends Fragment {

  List<ServerImages> images = new ArrayList<>();
  ImageAdapter adapterView;
  ViewPager mViewPager;
  private static int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     mViewPager = (ViewPager) view.findViewById(R.id.viewPage);
      adapterView = new ImageAdapter(getContext());
      Call<List<ServerImages>> call = App.getEndPoints().getImages();
      call.enqueue(new Callback<List<ServerImages>>() {
        @Override
        public void onResponse(Call<List<ServerImages>> call, Response<List<ServerImages>> response) {
          images = response.body();
          adapterView.setData(images);


        }

        @Override
        public void onFailure(Call<List<ServerImages>> call, Throwable t) {

        }
      });
      mViewPager.setAdapter(adapterView);
      adapterView.notifyDataSetChanged();
      final Handler handler = new Handler();
      final Runnable Update = new Runnable() {
        public void run() {
          if (currentPage == adapterView.getCount()) {
            currentPage = 0;
          }
          mViewPager.setCurrentItem(currentPage++, true);
        }
      };
      Timer swipeTimer = new Timer();
      swipeTimer.schedule(new TimerTask() {
        @Override
        public void run() {
          handler.post(Update);
        }
      }, 2500, 2500);





      view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}
