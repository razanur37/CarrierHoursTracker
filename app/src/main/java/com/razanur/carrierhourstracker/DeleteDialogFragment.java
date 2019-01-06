/*
 * Copyright (c) 2019 Casey English
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.razanur.carrierhourstracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DeleteDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, long date);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    DeleteDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the DeleteDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DeleteDialogListener so we can send events to the host
            mListener = (DeleteDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement DeleteDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        final long id = getArguments().getLong("id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message;
        if (id == MainActivity.DELETE_ALL_CONFIRM_ID)
            message = "Delete All Entries?";
        else
            message = "Delete Entry?";
        builder.setMessage(message)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(DeleteDialogFragment.this, id);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(DeleteDialogFragment.this);
                    }
                });
        return builder.create();
    }

    public static DeleteDialogFragment newInstance(Long id) {
        DeleteDialogFragment fragment = new DeleteDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("id", id);

        fragment.setArguments(bundle);

        return fragment;
    }
}
