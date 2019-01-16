package com.productwizardry.pogotypekeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class TypeKeysMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView keyboardView;
    private Keyboard numberKeys;
    private Keyboard typeKeys;
    private String currentKeyboard = "type";

    @Override
    public View onCreateInputView() {
        this.keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);

        this.numberKeys = new Keyboard(this, R.xml.number_keys);
        this.typeKeys = new Keyboard(this, R.xml.type_keys);

        this.keyboardView.setKeyboard(this.typeKeys);
        this.keyboardView.setOnKeyboardActionListener(this);
        return this.keyboardView;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:
                    CharSequence selectedText = inputConnection.getSelectedText(0);

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }

                    break;
                case Keyboard.KEYCODE_SHIFT:
                    this.switchKeyboards();
                    break;
                case Keyboard.KEYCODE_DONE:
                    requestHideSelf(0);
                    break;
                default:
                    char code = (char) primaryCode;
                    inputConnection.commitText(String.valueOf(code), 1);
            }
        }
    }

    public void switchKeyboards() {
        switch (this.currentKeyboard) {
            case "number":
                this.keyboardView.setKeyboard(this.typeKeys);
                this.currentKeyboard = "type";
                break;
            case "type":
            default:
                this.keyboardView.setKeyboard(this.numberKeys);
                this.currentKeyboard = "number";
        }
        this.keyboardView.invalidateAllKeys();
    }

    @Override
    public void onPress(int i) {}

    @Override
    public void onRelease(int i) {}

    @Override
    public void onText(CharSequence charSequence) {}

    @Override
    public void swipeLeft() {}

    @Override
    public void swipeRight() {}

    @Override
    public void swipeDown() {}

    @Override
    public void swipeUp() {}
}