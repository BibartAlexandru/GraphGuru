package com.example.graphguru;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyGraphsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGraphsFragment extends Fragment {
    private static final String ARG_PARAM1 = "graph_names";
    private ArrayList<String> graph_names_list = new ArrayList<>();
    private int margin_bottom ;
    private ArrayList<Button> graph_name_buttons = new ArrayList<>() ;
    private float density ;

    private RelativeLayout layout ;

    public MyGraphsFragment() {
    }
    public interface my_graphs_interface{
        public void go_to_graph(String graph_name) ;
    }
    private my_graphs_interface activity_interface ;
    public void set_interface(my_graphs_interface new_interface)
    {
        activity_interface = new_interface ;
    }
    public static MyGraphsFragment newInstance(ArrayList<String> graph_names) {
        MyGraphsFragment fragment = new MyGraphsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, graph_names);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            graph_names_list = getArguments().getStringArrayList(ARG_PARAM1);
            for(int i = 0 ; i < graph_names_list.size() ; i++) {
                Log.d("AJUTORR",graph_names_list.get(i)) ;
            }
        }
    }

    public Button CreateGoToGraphButton(String graph_name)
    {
        Button result = new Button(getContext()) ;
        RelativeLayout.LayoutParams btn_params = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ) ;

        result.setTextColor(Color.WHITE);
        result.setTextSize(20);
        result.setText(graph_name);
        result.setBackgroundResource(R.drawable.graph_name_button);
        btn_params.height = (int) (60 * density) ;
        result.setLayoutParams(btn_params);

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_interface.go_to_graph(graph_name);
            }
        });

        return result ;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_graphs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        density = getResources().getDisplayMetrics().density ;
        layout = view.findViewById(R.id.my_graphs_relative_layout) ;

        Log.d("AICI", "SIZE UL LISTEI : " + String.valueOf(graph_names_list.size())) ;
        for(int i = 0 ; i < graph_names_list.size() ; i++)
        {
            Log.d("AICI", graph_names_list.get(i)) ;
            Button graph_name_btn = CreateGoToGraphButton(graph_names_list.get(i)) ;
            RelativeLayout.LayoutParams btn_params = (RelativeLayout.LayoutParams) graph_name_btn.getLayoutParams() ;
            if(graph_name_buttons.size() > 0)
                btn_params.addRule(RelativeLayout.BELOW,graph_name_buttons.get(graph_name_buttons.size()-1).getId());
            else
                btn_params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layout.addView(graph_name_btn);
            graph_name_btn.setId(View.generateViewId());
            graph_name_buttons.add(graph_name_btn) ;
        }
    }
}