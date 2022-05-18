package com.dam.pickup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    private ListView searchs;
    private ArrayAdapter<String> mAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.history_fragment,container,false);
        searchs = view.findViewById(R.id.history_list);
        ArrayList<String> items = new ArrayList<String>();
        //Obtener historial de busqueda mediante firebase
        mAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.historial, items);
        searchs.setAdapter(mAdapter);
        return view;
    }
}