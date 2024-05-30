package com.example.graphguru;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class EditGraphFragment extends Fragment {

    private static final String ARG_PARAM1 = "graph_name";

    private String graph_name = "" ;
    private EditFragmentInterface editFragmentInterface ;
    public interface EditFragmentInterface{
        void on_graph_save_click(Graph graph) ;
    }
    public void setEditFragmentInterface(EditFragmentInterface e)
    {
        this.editFragmentInterface = e ;
    }

    private RelativeLayout containerRelativeLayout;
    private EditText graph_name_edit_text ;

    private ArrayList<Button> node_buttons = new ArrayList<Button>() ;
    private ArrayList<Button> highlight_neighbour_buttons = new ArrayList<>() ;
    private ArrayList<Button> remove_node_buttons = new ArrayList<>() ;
    private ArrayList<EditText> edge_weight_edit_texts = new ArrayList<>() ;
    private String[] optiuniAlgoritmi =   {"Weight of MWST", "Check Connectivity"} ;
    private AutoCompleteTextView autoCompleteTextView ;
    private ArrayList<Boolean> isNeighbourButtonClicked = new ArrayList<>();
    private ArrayAdapter<String> adapterItems ;

    private Button add_node_button ;
    private Button save_graph_button ;

    private Graph graph ;
    private TextInputLayout drop_down_text_input_layout;
    private TextView graph_size_text_view ;

    private TextView weight_label_text_view ;
    private TextView result_text_view ;

    private int node_button_height_dp ;
    private int margin_between_node_buttons ;
    private int margin_between_neighbour_buttons;
    private int left_margin ;
    private int margin_between_remove_buttons ;

    private int topObjectId ;

    private float density ;

    private int algorithm_to_run = 0 ;
    private DatabaseHandler databaseHandler;


    public EditGraphFragment() {
    }
    public static EditGraphFragment newInstance(String graph_name) {
        EditGraphFragment fragment = new EditGraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, graph_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            graph_name = getArguments().getString(ARG_PARAM1);
        }
        databaseHandler = new DatabaseHandler(getContext());
    }

    @Override // se apeleaza dupa onCreate
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_graph, container, false);
    }

    public Button CreateNodeButton(int value)
    {
        Button result = new Button(getContext()) ;
        result.setBackgroundResource(R.drawable.green_circle);
        result.setTextAppearance(R.style.node_button_text_style);
        result.setText(String.valueOf(value));

        RelativeLayout.LayoutParams nodeLayoutParams =  new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        nodeLayoutParams.height = node_button_height_dp ;
        nodeLayoutParams.width = node_button_height_dp ;

        result.setLayoutParams(nodeLayoutParams);

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedNodeIndex = -1 ;
                for(int i = 0 ; i < isNeighbourButtonClicked.size() ; i++)
                    if(isNeighbourButtonClicked.get(i) == true) {
                        selectedNodeIndex = i;
                        break;
                    }
                if(selectedNodeIndex == -1)
                    return ;
                boolean isCurrentNodeNeighbour = false ;
                isCurrentNodeNeighbour = graph.DoesAdjListContainNode(selectedNodeIndex,value) ;
                if(isCurrentNodeNeighbour) {
                    Log.d("AJUTOR", "DOES CONTAIN NODE!") ;
                    graph.RemoveEdge(selectedNodeIndex, value);
                    result.setBackgroundResource(R.drawable.grey_circle);
                }
                else {
                    graph.AddEdge(selectedNodeIndex, value, true);
                    result.setBackgroundResource(R.drawable.red_circle);
                }

            }
        });
        return result ;
    }

    public void SetEditTextsWeights()
    {
        int selected_node_index = -1 ;
        for(int i = 0 ; i < node_buttons.size() ; i++) {
//            Log.d("AJUTORR","VALUE : " + String.valueOf(Integer.valueOf(node_buttons.get(i).getText().toString())));
            if (isNeighbourButtonClicked.get(i) == true) {
                selected_node_index = graph.GetNodeIndex(Integer.valueOf(node_buttons.get(i).getText().toString()));
                break;
            }
        }
        if(selected_node_index == -1)
            return ;
//        Log.d("AJUTORR","INDEX : " + String.valueOf(selected_node_index)) ;
        for(int i = 0 ; i < node_buttons.size() ; i++)
        {
            if(isNeighbourButtonClicked.get(i) == true)
                continue;
            Log.d("AJUTORR","VALOAREA NODULUI : " + node_buttons.get(i).getText().toString() + " INDEXUL LUI : " + graph.GetNodeIndex(Integer.valueOf(node_buttons.get(i).getText().toString()))) ;
            if(!graph.DoesAdjListContainNode(selected_node_index, graph.GetNodeIndex(Integer.valueOf(node_buttons.get(i).getText().toString()))))
            {
                edge_weight_edit_texts.get(i).setVisibility(View.INVISIBLE);
                continue;
            }
            edge_weight_edit_texts.get(i).setVisibility(View.VISIBLE);
            int node_value = Integer.valueOf(node_buttons.get(i).getText().toString()) ;
            int node_index = graph.GetNodeIndexInAdjList(node_value,selected_node_index) ;
            if(node_index == -1)
                Log.d("AJUTORR","INDEX NOD E -1!") ;
            edge_weight_edit_texts.get(i).setText(
                    graph.edgeWeight.get(selected_node_index).get(node_index).toString()
            );
        }
    }

    public Button CreateRemoveNodeButton(int value)
    {
        Button result = new Button(getContext()) ;
        RelativeLayout.LayoutParams x_button_params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        ) ;
        x_button_params.height = (int) (40 * density) ;
        x_button_params.width = (int) (40 * density) ;
        result.setBackgroundResource(R.drawable.remove_button_drawable);
        result.setLayoutParams(x_button_params);

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nodeIndex = -1 ;
                for(int i = 0 ; i < node_buttons.size() ; i++)
                    if(Integer.valueOf(node_buttons.get(i).getText().toString()) == value) {
                        nodeIndex = i;
                        break ;
                    }
                isNeighbourButtonClicked.remove(nodeIndex) ;
                Log.d("HELPME","BEFORE : " + graph.toString()) ;
                graph.RemoveNode(graph.GetNodeIndex(Integer.valueOf(node_buttons.get(nodeIndex).getText().toString())));
                Log.d("HELPME","AFTER : " + graph.toString()) ;
                graph_size_text_view.setText(String.valueOf(graph.nodes.size()));

                containerRelativeLayout.removeView(node_buttons.get(nodeIndex));
                containerRelativeLayout.removeView(highlight_neighbour_buttons.get(nodeIndex));
                containerRelativeLayout.removeView(remove_node_buttons.get(nodeIndex));
                containerRelativeLayout.removeView(edge_weight_edit_texts.get(nodeIndex));

                node_buttons.remove(nodeIndex) ;
                highlight_neighbour_buttons.remove(nodeIndex) ;
                remove_node_buttons.remove(nodeIndex) ;
                edge_weight_edit_texts.remove(nodeIndex) ;

                for(int i = nodeIndex ; i < node_buttons.size() ; i++){
                    RelativeLayout.LayoutParams node_button_params = (RelativeLayout.LayoutParams) node_buttons.get(i).getLayoutParams() ;
                    RelativeLayout.LayoutParams highlight_button_params = (RelativeLayout.LayoutParams) highlight_neighbour_buttons.get(i).getLayoutParams() ;
                    RelativeLayout.LayoutParams remove_button_params = (RelativeLayout.LayoutParams) remove_node_buttons.get(i).getLayoutParams() ;
                    RelativeLayout.LayoutParams edge_weight_params = (RelativeLayout.LayoutParams) edge_weight_edit_texts.get(i).getLayoutParams() ;
                    if(i-1 >= 0)
                    {
                        node_button_params.addRule(RelativeLayout.BELOW,node_buttons.get(i-1).getId());
                        highlight_button_params.addRule(RelativeLayout.BELOW,node_buttons.get(i-1).getId());
                        remove_button_params.addRule(RelativeLayout.BELOW,node_buttons.get(i-1).getId());
                        edge_weight_params.addRule(RelativeLayout.BELOW,node_buttons.get(i-1).getId());
                    }
                    else{
                        node_button_params.addRule(RelativeLayout.BELOW,drop_down_text_input_layout.getId());
                        highlight_button_params.addRule(RelativeLayout.BELOW,drop_down_text_input_layout.getId());
                        remove_button_params.addRule(RelativeLayout.BELOW,drop_down_text_input_layout.getId());
                        edge_weight_params.addRule(RelativeLayout.BELOW,drop_down_text_input_layout.getId());
                    }
                }

                RelativeLayout.LayoutParams add_button_params = (RelativeLayout.LayoutParams) add_node_button.getLayoutParams() ;
                if(node_buttons.size() > 0)
                    add_button_params.addRule(RelativeLayout.BELOW,node_buttons.get(node_buttons.size()-1).getId());
                else
                    add_button_params.addRule(RelativeLayout.BELOW,drop_down_text_input_layout.getId());
                add_button_params.setMargins(left_margin,margin_between_node_buttons,0,0);
            }
        });
        return result ;
    }

    public Button CreateHighlightNeighboursButton(int value)
        {
        Button result = new Button(getContext()) ;
        result.setText("Neighbours");
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nodeIndex = -1 ;
                for(int i = 0 ; i < node_buttons.size() ; i++)
                    if(Integer.valueOf(node_buttons.get(i).getText().toString()) == value) {
                        nodeIndex = i;
                        break ;
                    }
                for(int i = 0 ; i < node_buttons.size() ; i++)
                    if(Integer.valueOf(node_buttons.get(i).getText().toString()) != value)
                        isNeighbourButtonClicked.set(i,false) ;

                if(isNeighbourButtonClicked.get(nodeIndex) == false)
                {
                    weight_label_text_view.setVisibility(View.VISIBLE);
                    for(int i = 0  ; i < node_buttons.size() ; i++)
                        if(i != nodeIndex) {
                            node_buttons.get(i).setClickable(true);
                            edge_weight_edit_texts.get(i).setVisibility(View.VISIBLE);
                        }
                        else {
                            node_buttons.get(i).setClickable(false);
                            edge_weight_edit_texts.get(i).setVisibility(View.INVISIBLE);
                        }
                    for(int a = 0 ; a < node_buttons.size() ; a++) {
                        boolean doesadjListContainNode = false ;
                        try {
                            doesadjListContainNode = graph.DoesAdjListContainNode(nodeIndex,a) ;
                        }
                        catch (Exception e)
                        {
                            doesadjListContainNode = false ;
                        }
                        if (doesadjListContainNode)
                            node_buttons.get(a).setBackgroundResource(R.drawable.red_circle);
                        else
                            node_buttons.get(a).setBackgroundResource(R.drawable.grey_circle);
                    }
                    node_buttons.get(nodeIndex).setBackgroundResource(R.drawable.selected_node_background);
                    isNeighbourButtonClicked.set(nodeIndex,!isNeighbourButtonClicked.get(nodeIndex)) ;
                    SetEditTextsWeights() ;
                }
                else {
                    weight_label_text_view.setVisibility(View.INVISIBLE);
                    for(int a = 0 ; a < node_buttons.size() ; a++) {
                        node_buttons.get(a).setBackgroundResource(R.drawable.green_circle);
                        edge_weight_edit_texts.get(a).setVisibility(View.INVISIBLE);
                    }
                    for(int i = 0  ; i < node_buttons.size() ; i++)
                        node_buttons.get(i).setClickable(false);
                    isNeighbourButtonClicked.set(nodeIndex,!isNeighbourButtonClicked.get(nodeIndex)) ;
                }
                for(int i = 0 ; i < isNeighbourButtonClicked.size() ; i++)
                    if(i != nodeIndex)
                        isNeighbourButtonClicked.set(i,false) ;
            }
        });
        result.setTextSize(10);
        result.setTextColor(Color.WHITE);
        int cornerRadius = getResources().getDimensionPixelSize(R.dimen.round_corder_radius);
        int buttonColor = getResources().getColor(R.color.green);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(cornerRadius);
        drawable.setColor(buttonColor);
        result.setBackground(drawable);
        result.setPadding((int)(10*density),(int)(10*density),(int)(10*density),(int)(10*density));
        RelativeLayout.LayoutParams button_params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        ) ;
        result.setLayoutParams(button_params);
        return result ;
}

    EditText CreateEdgeWeightEditText(int node_value)
    {
        EditText result = new EditText(getContext()) ;
        RelativeLayout.LayoutParams result_params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        ) ;
        result_params.width = left_margin*4;
        result.setSingleLine(true);
        result.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        result.setLayoutParams(result_params);
        result.setVisibility(View.INVISIBLE);

        result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int selected_node_index = -1 ;
                for(int i = 0 ; i < node_buttons.size() ; i++)
                    if (isNeighbourButtonClicked.get(i) == true) {
                        selected_node_index = graph.GetNodeIndex(Integer.valueOf(node_buttons.get(i).getText().toString()));
                        break;
                    }
                if(selected_node_index == -1)
                    return ;
                int current_node_index_in_adj = graph.GetNodeIndexInAdjList(graph.GetNodeIndex(node_value),selected_node_index) ;
                if(current_node_index_in_adj == -1)
                    return ;
                try {
                    Float new_weight = Float.valueOf(s.toString()) ;
                    result.setBackground(getResources().getDrawable(R.drawable.edit_text_right_input));
                    graph.edgeWeight.get(selected_node_index).set(current_node_index_in_adj,new_weight) ;
                    if(graph instanceof UndirectedGraph)
                    {
                        int current_node_index = graph.GetNodeIndex(node_value);
                        int selected_node_in_adj_index = graph.GetNodeIndexInAdjList(graph.nodes.get(selected_node_index).value,current_node_index) ;
                        graph.edgeWeight.get(current_node_index).set(selected_node_in_adj_index,new_weight) ;
                    }
                }
                catch (NumberFormatException e)
                {
                    result.setBackground(getResources().getDrawable(R.drawable.edit_text_wrong_input_outline));
                    Log.d("SETEAZA", "AICI") ;
                    graph.edgeWeight.get(selected_node_index).set(current_node_index_in_adj,0f) ;
                }
            }
        });
        return result ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        containerRelativeLayout = view.findViewById(R.id.edit_graph_relative_layout) ;
        graph_name_edit_text = view.findViewById(R.id.graph_name_edit_text) ;
        drop_down_text_input_layout = view.findViewById(R.id.dropdown_text_input_layout) ;
        save_graph_button = view.findViewById(R.id.save_graph_btn) ;
        graph_size_text_view = view.findViewById(R.id.size_number_text_view) ;
        result_text_view = view.findViewById(R.id.result_text_view) ;
        result_text_view.setText("");
        weight_label_text_view = view.findViewById(R.id.weight_text_view);
        weight_label_text_view.setVisibility(View.INVISIBLE);

//        Log.d("CHECK NAME", graph_name) ;

        density = getResources().getDisplayMetrics().density ;

        graph = databaseHandler.FetchGraph(graph_name) ;
        graph_size_text_view.setText(String.valueOf(graph.nodes.size()));
        graph_name_edit_text.setText(graph_name);

        save_graph_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(graph.name.length() != 0)
                    editFragmentInterface.on_graph_save_click(graph);
                else
                    Toast.makeText(getContext(),"GRAPH NAME CANNOT BE EMPTY", Toast.LENGTH_LONG) ;
            }
        });

        //region Creez viewurile pt noduri
        topObjectId = R.id.dropdown_text_input_layout;
        node_button_height_dp = (int)(69 * density) ;
        left_margin = (int)(20 * density) ;
        margin_between_node_buttons = (int)(10 * density) ;
        margin_between_neighbour_buttons = (int)(20 * density) ;
        margin_between_remove_buttons = (int)(25 * density) ;
        for(int i = 0 ; i < graph.nodes.size() ; i++)
        {
            if(node_buttons.size() > 0)
                topObjectId = node_buttons.get(node_buttons.size()-1).getId();
            else
                topObjectId = drop_down_text_input_layout.getId() ;

            Button node_button = CreateNodeButton(i) ;
            node_buttons.add(node_button) ;
            RelativeLayout.LayoutParams nodeLayoutParams = (RelativeLayout.LayoutParams)node_button.getLayoutParams() ;
            nodeLayoutParams.addRule(RelativeLayout.BELOW,topObjectId);
            nodeLayoutParams.setMargins((int)(20 * density), margin_between_node_buttons,0,0);
            containerRelativeLayout.addView(node_button);
            node_button.setId(View.generateViewId());


            Button highlight_neighbours_button = CreateHighlightNeighboursButton(i) ;
            highlight_neighbour_buttons.add(highlight_neighbours_button) ;

            isNeighbourButtonClicked.add(false) ;
            RelativeLayout.LayoutParams button_params = (RelativeLayout.LayoutParams) highlight_neighbours_button.getLayoutParams() ;
            button_params.addRule(RelativeLayout.BELOW,topObjectId);
            button_params.addRule(RelativeLayout.RIGHT_OF,node_button.getId());
            button_params.setMargins(left_margin,margin_between_neighbour_buttons,0,0);
            highlight_neighbours_button.setLayoutParams(button_params);
            containerRelativeLayout.addView(highlight_neighbours_button);
            highlight_neighbours_button.setId(View.generateViewId());

            Button x_button = CreateRemoveNodeButton(i) ;
            remove_node_buttons.add(x_button) ;
            RelativeLayout.LayoutParams x_button_params = (RelativeLayout.LayoutParams) x_button.getLayoutParams() ;
            x_button_params.addRule(RelativeLayout.BELOW,topObjectId);
            x_button_params.addRule(RelativeLayout.RIGHT_OF,highlight_neighbours_button.getId());
            x_button_params.setMargins(left_margin, margin_between_remove_buttons,0,0);
            x_button.setLayoutParams(x_button_params);
            containerRelativeLayout.addView(x_button);
            x_button.setId(View.generateViewId());

            EditText edge_weight_edit_text = CreateEdgeWeightEditText(i);
            edge_weight_edit_texts.add(edge_weight_edit_text) ;
            RelativeLayout.LayoutParams edge_text_params = (RelativeLayout.LayoutParams) edge_weight_edit_text.getLayoutParams() ;
            edge_text_params.addRule(RelativeLayout.BELOW,topObjectId);
            edge_text_params.addRule(RelativeLayout.RIGHT_OF,x_button.getId());
            edge_text_params.setMargins(left_margin/2,margin_between_neighbour_buttons,0,0);
            containerRelativeLayout.addView(edge_weight_edit_text);
            edge_weight_edit_text.setId(View.generateViewId());

        }
        //endregion

        //region Adding the AddNode BUtton
        Button add_node = new Button(getContext()) ;
        add_node_button = add_node ;
        add_node.setBackgroundResource(R.drawable.add_node_button_drawable);

        RelativeLayout.LayoutParams nodeLayoutParams =  new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if(node_buttons.size() > 0)
            nodeLayoutParams.addRule(RelativeLayout.BELOW,node_buttons.get(node_buttons.size()-1).getId());
        else
            nodeLayoutParams.addRule(RelativeLayout.BELOW,drop_down_text_input_layout.getId());
        nodeLayoutParams.setMargins(left_margin, margin_between_node_buttons,0,0);
        nodeLayoutParams.height = (int)(69 * density) ;
        nodeLayoutParams.width = (int)(69 * density) ;

        add_node.setLayoutParams(nodeLayoutParams);
        containerRelativeLayout.addView(add_node);
        add_node.setId(View.generateViewId());

        add_node.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HELPME","ADDING NODE!") ;
                int nodeValue = graph.GetSmallestNonExistingNodeValue() ;
                add_node.setTranslationY(0);
                graph.AddNode(nodeValue);
                graph_size_text_view.setText(String.valueOf(graph.nodes.size()));
                isNeighbourButtonClicked.add(false) ;

                //region adding_node_button
                Button node_button = CreateNodeButton(nodeValue) ;
                RelativeLayout.LayoutParams nodeLayoutParams =  (RelativeLayout.LayoutParams) node_button.getLayoutParams() ;
                if(node_buttons.size() > 0)
                    nodeLayoutParams.addRule(RelativeLayout.BELOW,node_buttons.get(node_buttons.size()-1).getId());
                else
                    nodeLayoutParams.addRule(RelativeLayout.BELOW,drop_down_text_input_layout.getId());
                nodeLayoutParams.setMargins((int)(20 * density), margin_between_node_buttons,0,0);
                containerRelativeLayout.addView(node_button);
                node_button.setId(View.generateViewId());
                node_buttons.add(node_button) ;
                RelativeLayout.LayoutParams add_node_params = (RelativeLayout.LayoutParams) add_node.getLayoutParams() ;
                add_node_params.addRule(RelativeLayout.BELOW, node_button.getId());
                add_node_params.setMargins(left_margin,margin_between_node_buttons,0,0);
                //endregion

                //region adding_highlight_neighbours_button
                Button highlight_neighbours_button = CreateHighlightNeighboursButton(nodeValue) ;
                isNeighbourButtonClicked.add(false) ;
                RelativeLayout.LayoutParams button_params = (RelativeLayout.LayoutParams) highlight_neighbours_button.getLayoutParams() ;
                if(node_buttons.size() > 1)
                    button_params.addRule(RelativeLayout.BELOW,node_buttons.get(node_buttons.size()-2).getId());
                else
                    button_params.addRule(RelativeLayout.BELOW,drop_down_text_input_layout.getId());
                button_params.addRule(RelativeLayout.RIGHT_OF,node_button.getId());
                button_params.setMargins((int)(20*density),margin_between_neighbour_buttons,0,0);
                containerRelativeLayout.addView(highlight_neighbours_button);
                highlight_neighbours_button.setId(View.generateViewId());
                highlight_neighbour_buttons.add(highlight_neighbours_button) ;
                //endregion

                //region adding remove_node_button
                Button x_button = CreateRemoveNodeButton(nodeValue) ;

                RelativeLayout.LayoutParams x_button_params = (RelativeLayout.LayoutParams) x_button.getLayoutParams() ;
                if(node_buttons.size() >1)
                    x_button_params.addRule(RelativeLayout.BELOW,node_buttons.get(node_buttons.size()-2).getId());
                else
                    x_button_params.addRule(RelativeLayout.BELOW, drop_down_text_input_layout.getId());

                x_button_params.addRule(RelativeLayout.RIGHT_OF,highlight_neighbours_button.getId());
                x_button_params.setMargins(left_margin, margin_between_remove_buttons,0,0);
                remove_node_buttons.add(x_button) ;
                containerRelativeLayout.addView(x_button);
                x_button.setId(View.generateViewId());
                //endregion

                EditText edge_weight_edit_text = CreateEdgeWeightEditText(nodeValue) ;
                RelativeLayout.LayoutParams edge_weight_params = (RelativeLayout.LayoutParams) edge_weight_edit_text.getLayoutParams() ;
                if(node_buttons.size() >1)
                    edge_weight_params.addRule(RelativeLayout.BELOW,node_buttons.get(node_buttons.size()-2).getId());
                else
                    edge_weight_params.addRule(RelativeLayout.BELOW, drop_down_text_input_layout.getId());
                edge_weight_params.addRule(RelativeLayout.RIGHT_OF,x_button.getId());
                edge_weight_params.setMargins(left_margin/2, margin_between_neighbour_buttons,0,0);
                edge_weight_edit_texts.add(edge_weight_edit_text) ;

                containerRelativeLayout.addView(edge_weight_edit_text);
                edge_weight_edit_text.setId(View.generateViewId());

            }
        });
        //endregion

        autoCompleteTextView = view.findViewById(R.id.auto_complete_text) ;

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0)
                {
                    drop_down_text_input_layout.setHint("");
                }
                else
                    drop_down_text_input_layout.setHint(R.string.drop_down_list_choose);
            }
        });

        graph_name_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0 && s.charAt(s.length()-1) == ' ')
                {
                    Toast.makeText(getContext(),"GRAPH NAME CANNOT CONTAIN SPACES!", Toast.LENGTH_LONG) ;
                    s.delete(s.length()-1,s.length()) ;
                }
                else graph.name = s.toString() ;
            }
        });

        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.dropdown_list_item,optiuniAlgoritmi) ;
        autoCompleteTextView.setAdapter(adapterItems);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Solver solver = new Solver() ;
                if(position == 0)
                {
                    if(graph instanceof DirectedGraph)
                        Toast.makeText(getContext(),"GRAPH MUST BE UNDIRECTED!",Toast.LENGTH_LONG).show();
                    else
                        try {
                        result_text_view.setText(String.valueOf(solver.GetMWST(graph)));
                    } catch (GraphNotWeightedException e) {
                        Toast.makeText(getContext(),"GRAPH MUST BE WEIGHTED!",Toast.LENGTH_LONG).show();
                    } catch (GraphNotConnectedException e) {
                        Toast.makeText(getContext(),"GRAPH MUST BE CONNECTED!",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    boolean result = solver.IsConnected(graph) ;
                    if(result == true)
                        result_text_view.setText("YES");
                    else
                        result_text_view.setText("NO");
                }
            }
        });

    }
}