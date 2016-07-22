package dadmc.pract4;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Inmundus on 10/11/2015.
 */
public class TextValidator implements TextWatcher {
    private final TextView textView;
    private final View view;

    public TextValidator(View view) {
        this.textView = (TextView) view;
        this.view = view;
    }

    public void validate(TextView textView, String text){
        int number;
        try{
            number = Integer.valueOf(text);
        }catch (NumberFormatException e){
            number = -1;
        }
        if (number < 0 || number > 255) {
            textView.setError("Error: Número entre 0 y 255");
        } else {
            textView.setError(null);
        }
    }

    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        validate(textView, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}