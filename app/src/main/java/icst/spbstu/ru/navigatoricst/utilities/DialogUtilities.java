package icst.spbstu.ru.navigatoricst.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;

public class DialogUtilities extends DialogFragment {

    private Activity activity;

    private String dialogTitle, dialogText, positiveText, negativeText, viewIdText;
    private TextView tvDialogTitle, tvDialogText;

    public interface OnCompleteListener {
        void onComplete(Boolean isOkPressed, String viewIdText);
    }

    private OnCompleteListener listener;

    public static DialogUtilities newInstance(String dialogTitle, String dialogText, String yes,
                                              String no, String viewIdText) {
        Bundle args = new Bundle();

        args.putString(AppConstants.BUNDLE_KEY_TITLE, dialogTitle);
        args.putString(AppConstants.BUNDLE_KEY_MESSAGE, dialogText);
        args.putString(AppConstants.BUNDLE_KEY_YES, yes);
        args.putString(AppConstants.BUNDLE_KEY_NO, no);
        args.putString(AppConstants.BUNDLE_KEY_VIEW_ID, viewIdText);

        DialogUtilities fragment = new DialogUtilities();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;

        try{
            this.listener = (OnCompleteListener) context;
        } catch (final ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(activity).inflate(R.layout.fragment_custom_dialog, null);

        initVar();
        initView(rootView);
        initFunctionality();

        return new AlertDialog.Builder(activity)
                .setView(rootView)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onComplete(true, viewIdText);
                    }
                })
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null){
                            listener.onComplete(false, viewIdText);
                        }
                    }
                })
                .create();
    }

    private void initVar(){
        Bundle bundle = getArguments();
        if (bundle != null){
            dialogTitle = bundle.getString(AppConstants.BUNDLE_KEY_TITLE);
            dialogText = bundle.getString(AppConstants.BUNDLE_KEY_MESSAGE);
            positiveText = bundle.getString(AppConstants.BUNDLE_KEY_YES);
            negativeText = bundle.getString(AppConstants.BUNDLE_KEY_NO);
            viewIdText = bundle.getString(AppConstants.BUNDLE_KEY_VIEW_ID);
        }
    }

    private void initView(View rootView){
        tvDialogTitle = (TextView)rootView.findViewById(R.id.dialog_title);
        tvDialogText = (TextView)rootView.findViewById(R.id.dialog_text);
    }

    private void initFunctionality() {
        tvDialogTitle.setText(dialogTitle);
        tvDialogText.setText(dialogText);
    }
}
