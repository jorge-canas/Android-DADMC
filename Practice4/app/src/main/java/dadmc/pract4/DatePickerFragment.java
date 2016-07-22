package dadmc.pract4;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Inmundus on 11/11/2015.
 */
public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener onDateSet;

    public DatePickerFragment() {
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        onDateSet = ondate;
    }

    private int day, month, year;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        day = args.getInt("day");
        month = args.getInt("month");
        year = args.getInt("year");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
    }
}
