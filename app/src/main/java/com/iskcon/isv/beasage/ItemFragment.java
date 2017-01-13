package com.iskcon.isv.beasage;

/**
 * Created by mayank on 1/13/17.
 */
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Created by maygupta on 9/21/15.
 */
public class ItemFragment extends DialogFragment {

    OnDialogResult mDialogResult;
    private View view;
    private boolean isNewTodo;
    private int position;
    History todo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_dialog, null);
        view.findViewById(R.id.save).setOnClickListener(new SaveListener());
        view.findViewById(R.id.cancel).setOnClickListener(new CancelListener());
        return view;
    }

    /**
     * LISTENERS FOR ACTION BUTTONS
     **/
    private class SaveListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    private class DeleteListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private class CancelListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    public void setDialogResult(OnDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    private String parseDatePicker(DatePicker d) {
        return String.format("%d-%d-%d", d.getDayOfMonth(), d.getMonth()+1, d.getYear());
    }

    public interface OnDialogResult {
        void finish(History todo, int position);
        void delete(int position);
    }

}