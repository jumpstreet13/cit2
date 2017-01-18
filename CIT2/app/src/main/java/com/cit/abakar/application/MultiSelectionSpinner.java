package com.cit.abakar.application;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class MultiSelectionSpinner extends Spinner implements DialogInterface.OnMultiChoiceClickListener {

    String[] items = null;
    boolean[] mSelection = null;

    ArrayAdapter<String> simpleAdapter;

    public MultiSelectionSpinner(Context context) {
        super(context);

        simpleAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simpleAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }


    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

        if(mSelection != null && which < mSelection.length){
            mSelection[which] = isChecked;
            simpleAdapter.clear();
            simpleAdapter.add(buildSelectedItemString());
        }else{
            throw new IllegalArgumentException("Argument 'which' is out of bounds");
        }
    }
    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(items, mSelection, this);
        builder.show();
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(String[] items) {
        items = items;
        mSelection = new boolean[items.length];
        simpleAdapter.clear();
        simpleAdapter.add(items[0]);
        Arrays.fill(mSelection, false);
    }

    public void setItems(List<String> items) {
        this.items = items.toArray(new String[items.size()]);
        mSelection = new boolean[this.items.length];
        simpleAdapter.clear();
        simpleAdapter.add(this.items[0]);
        Arrays.fill(mSelection, false);
    }

    public void setSelection(String[] selection) {
        for (String cell : selection) {
            for (int j = 0; j < items.length; ++j) {
                if (items[j].equals(cell)) {
                    mSelection[j] = true;
                }
            }
        }
    }

    public void setSelection(List<String> selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (String sel : selection) {
            for (int j = 0; j < items.length; ++j) {
                if (items[j].equals(sel)) {
                    mSelection[j] = true;
                }
            }
        }
        simpleAdapter.clear();
        simpleAdapter.add(buildSelectedItemString());
    }

    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simpleAdapter.clear();
        simpleAdapter.add(buildSelectedItemString());
    }

    public void setSelection(int[] selectedIndicies) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (int index : selectedIndicies) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
        }
        simpleAdapter.clear();
        simpleAdapter.add(buildSelectedItemString());
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<String>();
        for (int i = 0; i < items.length; ++i) {
            if (mSelection[i]) {
                selection.add(items[i]);
            }
        }
        return selection;
    }

    public List<Integer> getSelectedIndicies() {
        List<Integer> selection = new LinkedList<Integer>();
        for (int i = 0; i < items.length; ++i) {
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(items[i]);
            }
        }
        return sb.toString();
    }

    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(items[i]);
            }
        }
        return sb.toString();
    }
}
