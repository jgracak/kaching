package com.moneyapp;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class DatePickerFragment extends SherlockDialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    private OnFragmentClickListener mListener;
    Long longDate;
    Calendar calendar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement listeners!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	if (calendar == null) {
    		Bundle args = getArguments();
    		calendar = Calendar.getInstance();
    		if (args != null) {
        		calendar.setTimeInMillis(args.getLong("longDate"));
        	}
    	}

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // Do something with the date chosen by the user
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        mListener.onFragmentClick(TransactionAddActivity.DATE_PICKER_ACTION, calendar);
    }
}
