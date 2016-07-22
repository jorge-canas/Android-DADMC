package dadmc.pract4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class Form extends FragmentActivity {
    private final String DEBUGTAG = "Pract4";

    private final int SELECT_PHOTO = 1;
    private Button btnBirthday;
    private Button btnSend;
    private TextView tvShowBirthday;
    private EditText etName;
    private EditText etSurname;
    private ImageView imgAvatar;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        initLayoutComponents();
    }

    public void initLayoutComponents(){
        btnBirthday = (Button) findViewById(R.id.btnBirthday);
        btnSend = (Button) findViewById(R.id.btnSend);
        tvShowBirthday = (TextView) findViewById(R.id.tvShowBirthday);
        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
    }

    public void btnBithdayClick(View v){
        if(v.equals(btnBirthday)){
            showDatePicker();
        }
    }

    public void sendForm(View v){
        if(v.equals(btnSend)){
            Intent i = new Intent(getBaseContext(), SeeFormActivity.class);
            if (!tvShowBirthday.getText().toString().equals(""))
                i.putExtra("tvShowBirthday", tvShowBirthday.getText().toString());
            if (!etName.getText().toString().equals(""))
                i.putExtra("etName", etName.getText().toString());
            if (!etSurname.getText().toString().equals(""))
                i.putExtra("etSurname", etSurname.getText().toString());
            if (imageUri != null)
                i.putExtra("imgAvatar", imageUri.toString());
            startActivity(i);
        }
    }

    public void pickAvatar(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        imageUri = imageReturnedIntent.getData();
                        Log.d(DEBUGTAG, "Uri"+imageUri.toString());
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imgAvatar.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            break;
        }
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "dialog");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            tvShowBirthday.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear)
                    + "/" + String.valueOf(year));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("tvShowBirthday", tvShowBirthday.getText().toString());
        //savedInstanceState.putString("etSurname", etSurname.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvShowBirthday.setText(String.valueOf(savedInstanceState.getString("tvShowBirthday")));
        //etName.setText(String.valueOf(savedInstanceState.getString("etName")));
        //etSurname.setText(String.valueOf(savedInstanceState.getString("etSurname")));
    }
}
