package pro.audasviator.callsearcher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pro.audasviator.callsearcher.MainActivity;


public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Начиная с Android 3.1 BroadcastReceiver`ы не работают, пока не будез запщуено активити
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
