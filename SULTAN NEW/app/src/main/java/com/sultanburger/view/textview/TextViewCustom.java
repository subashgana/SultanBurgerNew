package com.sultanburger.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.sultanburger.R;
import com.sultanburger.utils.Validator;

public class TextViewCustom extends AppCompatTextView {

    private static final String TAG = TextViewCustom.class.getSimpleName();

    private final int openSansBold = 1;
    private final int openSansBoldItalic = 2;
    private final int openSansExtraBold = 3;
    private final int openSansExtraBoldItalic = 4;
    private final int openSansItalic = 5;
    private final int openSansLight = 6;
    private final int openSansLightItalic = 7;
    private final int openSansRegular = 8;
    private final int openSansSemiBold = 9;
    private final int openSansSemiBoldItalic = 10;

    private final int bold = 1;
    private final int italic = 2;
    private final int normal = 3;
    private final int bole_italic = 4;

    private Context context;
    private AttributeSet attributeSet;
    private int font;
    private int type;

    public TextViewCustom(Context context) {
        super(context);

        this.context = context;
        init();
    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    public TextViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    protected void init() {
        getAttributeSet();
        applyFont();
    }

    protected void getAttributeSet() {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.FontCustom);

            font = typedArray.getInt(R.styleable.FontCustom_fontModel, openSansRegular);
            type = typedArray.getInt(R.styleable.FontCustom_fontType, normal);
        } finally {
            if (Validator.isValid(typedArray))
                typedArray.recycle();
        }
    }

    protected void applyFont() {
        Typeface typeface = null;

        switch (font) {
            case openSansBold:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                break;

            case openSansBoldItalic:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-BoldItalic.ttf");
                break;

            case openSansExtraBold:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-ExtraBold.ttf");
                break;

            case openSansExtraBoldItalic:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
                break;

            case openSansItalic:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
                break;

            case openSansLight:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
                break;

            case openSansLightItalic:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-LightItalic.ttf");
                break;

            case openSansRegular:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                break;

            case openSansSemiBold:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
                break;

            case openSansSemiBoldItalic:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-SemiboldItalic.ttf");
                break;

            default:
                break;
        }

        if (Validator.isValid(typeface)) {
            switch (type) {
                case bold:
                    setTypeface(typeface, Typeface.BOLD);
                    break;

                case italic:
                    setTypeface(typeface, Typeface.ITALIC);
                    break;

                case normal:
                    setTypeface(typeface, Typeface.NORMAL);
                    break;

                case bole_italic:
                    setTypeface(typeface, Typeface.BOLD_ITALIC);
                    break;

                default:
                    setTypeface(typeface, Typeface.NORMAL);
                    break;
            }
        }
    }
}
