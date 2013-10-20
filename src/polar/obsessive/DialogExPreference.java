package polar.obsessive;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

public class DialogExPreference extends DialogPreference 
{
    public DialogExPreference(Context oContext, AttributeSet attrs)
    {
        super(oContext, attrs);     
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if(true){
        	// erase all data
        	// link them somewhere else
        }
        Log.i("dialogBox",positiveResult+"");
    }
}