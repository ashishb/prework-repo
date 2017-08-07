package todo.net.ashishb.todoapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    public static final String EDIT_POSITION = "position";
    private static final String EDIT_CURRENT_VALUE = "currentValue";
    public static final String EDIT_NEW_VALUE = "newValue";

    private int position;

    public static Intent getIntent(Context context, String currentValue, int position) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(EDIT_CURRENT_VALUE, currentValue);
        intent.putExtra(EDIT_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        EditText editText = (EditText) findViewById(R.id.editItem);
        editText.setText(getIntent().getStringExtra(EDIT_CURRENT_VALUE));
        position = getIntent().getIntExtra(EDIT_POSITION, -1);
    }


    public void onEditItem(View view) {
        EditText editText = (EditText) findViewById(R.id.editItem);
        Intent intent = new Intent();
        intent.putExtra(EDIT_NEW_VALUE, editText.getText().toString());
        intent.putExtra(EDIT_POSITION, position);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
