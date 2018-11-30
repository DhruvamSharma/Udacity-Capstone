package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.TaskRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;

import java.util.Date;

/**
 * This class is responsible for presenting the add task bottom sheet.
 */
public class MainActivityBottomSheetFragment extends BottomSheetDialogFragment {

    private String mTaskDescription;

    private BottomSheetCallBacks mBottomSheetCallBacks;

    private int listId;

    public MainActivityBottomSheetFragment() {
        // Required empty public constructor

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View containerView = inflater.inflate(R.layout.activity_main_new_task_bottom_sheet_fragment,
                container, false);

        final MaterialButton saveTask = containerView.findViewById(R.id.main_activity_bottom_sheet_save_task_fab);


        if(getArguments()!= null)
            listId = Integer.parseInt(getArguments()
                    .getString(getResources().getString(R.string.current_list)));

        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveTask(containerView);

            }
        });

        return containerView;
    }


    private void saveTask(View view) {

        TextInputEditText newTask = view.findViewById(R.id.main_activity_bottom_sheet_edit_task_et);
        if(newTask.getText() != null && !newTask.getText().toString().equals("")) {
            mTaskDescription = newTask.getText().toString();

            final Task task = new Task(mTaskDescription, false, listId, new Date());

            final TaskRepository repository = TaskRepository.getCommonRepository(view.getContext());
            AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    repository.insertTask(task);

                    AppExecutor.getsInstance().getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    });
                }
            });



        }




    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetCallBacks) {
            mBottomSheetCallBacks = (BottomSheetCallBacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBottomSheetCallBacks = null;
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

       // mBottomSheetCallBacks.onBottomSheetDismiss();

    }

    public interface BottomSheetCallBacks {

        void onBottomSheetDismiss();

    }

}
