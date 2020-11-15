package com.myprog.sportlife.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myprog.sportlife.Adapter.listFragmentAdapter;
import com.myprog.sportlife.R;
import com.myprog.sportlife.Interface.ListFragmentListener;
import com.myprog.sportlife.data.DataManager;
import com.myprog.sportlife.model.Training;

import java.util.ArrayList;

public class ListFragment extends Fragment implements ListFragmentListener {

    private View view;
    private RecyclerView recyclerView;
    private Context myContext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        init();
        return view;
    }

    public void init(){
        recyclerView = view.findViewById(R.id.fragmentList_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(myContext));
        DataManager.getListTraining();
    }

    @Override
    public void onChanged(ArrayList<Training> training) {
        recyclerView.setAdapter(new listFragmentAdapter(training, myContext));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity)context;
        DataManager.addFragmentListListener(this);
    }

}
