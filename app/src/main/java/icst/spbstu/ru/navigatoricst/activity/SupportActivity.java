package icst.spbstu.ru.navigatoricst.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.utilities.AppUtilities;

public class SupportActivity extends BaseActivity {

    EditText etSupport;
    Spinner spinnerSupport;
    Button btnSupportSend;
    SupportActivity activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        etSupport = (EditText)findViewById(R.id.etSupport);
        spinnerSupport = (Spinner)findViewById(R.id.spinnerSupport);
        btnSupportSend = (Button)findViewById(R.id.btnSupportSend);
        activity = SupportActivity.this;
        context = activity.getApplicationContext();

        initToolbar(true);
        setToolbarTitle(getString(R.string.support));
        enableUpButton();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {

        String subject = "[" + getResources().getString(R.string.app_name) + "] " + spinnerSupport.getSelectedItem().toString();
        String text = etSupport.getText().toString();
        String to = getString(R.string.support_mail_address);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + subject + "&body=" + text + "&to=" + to);
        intent.setData(data);

        try{
            activity.startActivity(Intent.createChooser(intent,
                    "Выберите приложение для отправки"));
        } catch (ActivityNotFoundException e){
            AppUtilities.showToast(context, getString(R.string.no_email_client));
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppUtilities.showToast(context, getResources().getString(R.string.sent));
    }
}
