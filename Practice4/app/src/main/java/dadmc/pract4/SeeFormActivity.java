package dadmc.pract4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SeeFormActivity extends AppCompatActivity {
    private TextView tvNameSended;
    private TextView tvSurnameSended;
    private TextView tvBirthdaySended;
    private ImageView imgSeeFormAvatar;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLayoutComponents();
    }

    public void initLayoutComponents(){
        tvNameSended = (TextView) findViewById(R.id.tvNameSended);
        tvSurnameSended = (TextView) findViewById(R.id.tvSurnameSended);
        tvBirthdaySended = (TextView) findViewById(R.id.tvBirthdaySended);
        imgSeeFormAvatar = (ImageView) findViewById(R.id.imgSeeFormAvatar);

        if (getIntent().getExtras() != null) {
            Bundle data = getIntent().getExtras();
            if (data.getString("etName") != null)
                tvNameSended.setText(data.getString("etName").toString());
            if (data.getString("etSurname") != null)
                tvSurnameSended.setText(data.getString("etSurname").toString());
            if (data.getString("tvShowBirthday") != null)
                tvBirthdaySended.setText(data.getString("tvShowBirthday").toString());
            try {
                if (data.getString("imgAvatar") != null)
                    imageUri = Uri.parse(data.getString("imgAvatar"));
                if (imageUri != null) {
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imgSeeFormAvatar.setImageBitmap(selectedImage);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
