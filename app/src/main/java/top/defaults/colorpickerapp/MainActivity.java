package top.defaults.colorpickerapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Locale;

import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String SAVED_STATE_KEY_COLOR = "saved_state_key_color";
    private static final int INITIAL_COLOR = 0xFFFF8000;

    private ActivityMainBinding binding;

    void resetColor() {
        binding.colorPicker.reset();
    }

    void popup(View v) {
        new ColorPickerPopup.Builder(this)
                .initialColor(binding.colorPicker.getColor())
                .enableAlpha(true)
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .onlyUpdateOnTouchEventUp(true)
                .build()
                .show(new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        binding.colorPicker.setInitialColor(color);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.colorPicker.subscribe((color, fromUser, shouldPropagate) -> {
            binding.pickedColor.setBackgroundColor(color);
            binding.colorHex.setText(colorHex(color));

            getWindow().setStatusBarColor(color);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(color));
            }
        });

        int color = INITIAL_COLOR;
        if (savedInstanceState != null) {
            color = savedInstanceState.getInt(SAVED_STATE_KEY_COLOR, INITIAL_COLOR);
        }
        binding.colorPicker.setInitialColor(color);
        binding.resetColor.setOnClickListener(v -> resetColor());
        binding.pickedColor.setOnClickListener(this::popup);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_STATE_KEY_COLOR, binding.colorPicker.getColor());
    }

    private String colorHex(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", a, r, g, b);
    }
}
