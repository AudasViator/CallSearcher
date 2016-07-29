package pro.audasviator.callsearcher;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

// Здесь всё заморочено, хоть и просто
// По сути проверяем нужные разрешения
// Если пользователь их не дал, то кидаем обидку
// Можно было бы сделать targetApi == 21, но...
@RuntimePermissions // Кодогенерация!
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Сначала проверим разрешения для перехвата всего телефонного
        MainActivityPermissionsDispatcher.checkPhonePermissionWithCheck(this);
        // Если разрешения есть или будут, то запустится checkPhonePermission()
    }


    // Два метода переопределены для библиотеки
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainActivityPermissionsDispatcher.onActivityResult(this, requestCode);
    }

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED})
    void checkPhonePermission() {
        Log.d(TAG, "Check phone permissions...");
        // Если телефоноразрешения есть, то проверим разрешение на рисование поверх приложений
        // Оно немного особенное
        MainActivityPermissionsDispatcher.checkAlertPermissionWithCheck(this);
    }

    // Показывается при второй попытки запросить разрешения
    @OnShowRationale({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED})
    void showRationaleForPhone(PermissionRequest request) {
        showRationale(request);
    }

    // Можно было бы сделать лучше, но...
    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED})
    void showNeverAskForPhone() {
        showNeverAsk();
    }

    // Но нет
    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED})
    void onPhonePermissionDenied() {
        onPermissionDenied();
        setContentView(R.layout.main_activity);
    }

    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void checkAlertPermission() {
        Log.d(TAG, "Check alert permissions...");
        finish(); // Активити больше не нужно
    }

    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void showRationaleForAlert(PermissionRequest request) {
        showRationale(request);
    }

    @OnNeverAskAgain(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void showNeverAskForAlert() {
        // Если не сдвинуть ползунок, то вызовется этот метод
        showNeverAsk();
        setContentView(R.layout.main_activity);
    }

    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void onAlertPermissionDenied() {
        onPermissionDenied();
    }

    private void showRationale(PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.permission_alert_title)
                .setMessage(R.string.permission_alert_message)
                .setPositiveButton(R.string.permission_alert_button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.permission_alert_button_deny, (dialog, button) -> request.proceed())
                .create()
                .show();
    }

    private void showNeverAsk() {
        Toast.makeText(this, R.string.toast_never_ask, Toast.LENGTH_LONG).show();
    }

    private void onPermissionDenied() {
        Toast.makeText(this, R.string.toast_denied, Toast.LENGTH_LONG).show();
    }
}