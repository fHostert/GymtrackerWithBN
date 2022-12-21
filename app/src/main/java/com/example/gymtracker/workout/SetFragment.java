package com.example.gymtracker.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.gymtracker.R;
import com.example.gymtracker.datastructures.Set;
import com.example.gymtracker.helper.DatabaseManager;
import com.example.gymtracker.helper.Formatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetFragment extends Fragment {

    private static final String ARG_SET = "param1";
    private static final String ARX_EXERCISE_ID = "param2";

    private Set set;
    private int exerciseID;

    public SetFragment() {
        // Required empty public constructor
    }

    public static SetFragment newInstance(Set set, int exerciseID) {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SET, set);
        args.putInt(ARX_EXERCISE_ID, exerciseID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            set = (Set) getArguments().getSerializable(ARG_SET);
            exerciseID = getArguments().getInt(ARX_EXERCISE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_set, container, false);

        //Fill data
        ((TextView) view.findViewById(R.id.set_index_text_view)).
                setText(String.valueOf(set.getIndex()));
        if (set.getReps() != 0) {
            ((EditText)view.findViewById(R.id.weight_edit_text)).
                    setText(String.valueOf(Formatter.formatFloat(set.getWeight())));
            ((EditText)view.findViewById(R.id.reps_edit_text)).setText(String.valueOf(set.getReps()));
            colorSet(view);
        }
        return view;
    }

    private void colorSet(View view) {
        view.findViewById(R.id.set_table_row).setBackgroundColor(
                getResources().getColor(R.color.setCompleted));
        view.findViewById(R.id.save_set_button).setBackgroundColor(
                getResources().getColor(R.color.setCompleted));
    }

    public void saveSet() {
        String repsString = String.valueOf(
                ((EditText) getView().findViewById(R.id.reps_edit_text)).getText());
        String weightString = String.valueOf(
                ((EditText) getView().findViewById(R.id.weight_edit_text)).getText());
        float weight = (weightString.equals("") ? 0 : Float.parseFloat(weightString));
        int reps = (repsString.equals("") ? 0: Integer.parseInt(repsString));
        if (reps < 1) {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.toastNoRepsDone),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        set.setReps(reps);
        set.setWeight(weight);
        DatabaseManager.updateSet(exerciseID, set);
        colorSet(getView());
    }


}