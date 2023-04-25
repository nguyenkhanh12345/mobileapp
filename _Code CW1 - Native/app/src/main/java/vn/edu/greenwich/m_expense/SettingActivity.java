package vn.edu.greenwich.m_expense;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import vn.edu.greenwich.m_expense.database.MExpenseDAO;

public class SettingActivity extends AppCompatActivity {
    protected MExpenseDAO _db;
    protected Button settingBackup, settingResetDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.label_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _db = new MExpenseDAO(this);

        settingBackup = findViewById(R.id.settingBackup);
        settingResetDatabase = findViewById(R.id.settingResetDatabase);

        settingBackup.setOnClickListener(v -> backup());
        settingResetDatabase.setOnClickListener(v -> resetDatabase());
    }

    protected void backup() {
        Toast.makeText(this, "Not yet implemented.", Toast.LENGTH_SHORT).show();
    }

    protected void resetDatabase() {
        _db.reset();

        Toast.makeText(this, R.string.label_reset_database, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}