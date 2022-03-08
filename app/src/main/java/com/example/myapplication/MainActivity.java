package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher activityResultLauncher;

    // Used to load the 'myapplication' library on application startup.
    static {
        System.loadLibrary("myapplication");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityResultLauncher  = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            // обработка результата
                            String pin = data.getStringExtra("pin");
                            Toast.makeText(MainActivity.this, pin,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        int res = initRing();
        byte[] v = randomBytes(10);

        byte[] key =
                stringToHex("0123456789ABCDEF0123456789ABCDE0");
        byte[] enc = encrypt(key,
                stringToHex("000000000000000102"));
        byte[] dec = decrypt(key, enc);
        String s = new String(Hex.encodeHex(dec)).toUpperCase();
        Log.i("DEC: ", s);
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

        // Example of a call to a native method
//        TextView tv = binding.sampleText;
//        tv.setText(stringFromJNI());
        Intent it = new Intent(this, PinpadActivity.class);
        //startActivity(it);
        activityResultLauncher.launch(it);
    }


    public static byte[] stringToHex(String s)
    {
        byte[] hex;
        try
        {
            hex = Hex.decodeHex(s.toCharArray());
        }
        catch (DecoderException ex)
        {
            hex = null;
        }
        return hex;
    }

    public void onButtonClick (View v)
    {
        Toast.makeText(this, "Hello",
                Toast.LENGTH_SHORT).show();
    }
    /**
     * A native method that is implemented by the 'myapplication' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native int initRing();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);

}