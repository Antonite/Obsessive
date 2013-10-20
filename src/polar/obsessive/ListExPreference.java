package polar.obsessive;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;

public class ListExPreference extends ListPreference 
{
    public ListExPreference(Context oContext, AttributeSet attrs)
    {
        super(oContext, attrs);     
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if(true){
        	//implement cron stuff here
        }
    }
}