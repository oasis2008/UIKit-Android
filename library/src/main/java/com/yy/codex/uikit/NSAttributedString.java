package com.yy.codex.uikit;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LineHeightSpan;
import android.text.style.ParagraphStyle;
import android.text.style.ReplacementSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

import java.util.HashMap;

/**
 * Created by PonyCui_Home on 2017/1/4.
 */

public class NSAttributedString extends SpannableStringBuilder {

    public static String NSFontAttributeName = "NSFontAttributeName"; // NSFont, default System 17
    public static String NSParagraphStyleAttributeName = "NSParagraphStyleAttributeName"; // NSParagraphStyle, default nil
    public static String NSForegroundColorAttributeName = "NSForegroundColorAttributeName"; // int, default Color.BLACK
    public static String NSBackgroundColorAttributeName = "NSBackgroundColorAttributeName"; // int, default Color.TRANSPARENT: no background
    public static String NSKernAttributeName = "NSKernAttributeName"; // double containing floating point value, in points; amount to modify default kerning. 0 means kerning is disabled.
    public static String NSStrikethroughStyleAttributeName = "NSStrikethroughStyleAttributeName"; // int containing integer, default 0: no strikethrough
    public static String NSUnderlineStyleAttributeName = "NSUnderlineStyleAttributeName"; // int containing integer, default 0: no underline
    public static String NSStrokeColorAttributeName = "NSStrokeColorAttributeName";// TODO: 2017/1/9 not implemented.
    public static String NSStrokeWidthAttributeName = "NSStrokeWidthAttributeName";// TODO: 2017/1/9 not implemented.
    public static String NSShadowAttributeName = "NSShadowAttributeName"; // NSShadow, default nil: no shadow
    public static String NSAttachmentAttributeName = "NSAttachmentAttributeName";// TODO: 2017/1/9 not implemented.
    public static String NSLinkAttributeName = "NSLinkAttributeName";// TODO: 2017/1/9 not implemented.
    public static String NSBaselineOffsetAttributeName = "NSBaselineOffsetAttributeName";// TODO: 2017/1/9 not implemented.
    public static String NSUnderlineColorAttributeName = "NSUnderlineColorAttributeName";// TODO: 2017/1/9 not implemented.
    public static String NSStrikethroughColorAttributeName = "NSStrikethroughColorAttributeName";// TODO: 2017/1/9 not implemented.

    public NSAttributedString(String text) {
        super(text);
        this.reset(new HashMap<String, Object>(), new NSRange(0, text.length()));
    }

    public NSAttributedString(String text, HashMap<String, Object> attributes) {
        super(text);
        this.reset(attributes, new NSRange(0, text.length()));
    }

    public NSAttributedString(NSAttributedString attributedString) {
        super(attributedString);
    }

    public NSAttributedString(SpannedString spannableString) {
        super(spannableString);
    }

    public NSMutableAttributedString mutableCopy() {
        return new NSMutableAttributedString(this);
    }

    public Object getAttribute(String attrName, int atIndex) {
        NSAttributedSpan[] objects = getSpans(atIndex, 1, NSAttributedSpan.class);
        if (objects.length > 0) {
            return objects[0].attrs.get(attrName);
        }
        else {
            return null;
        }
    }

    public HashMap<String, Object> getAttributes(int atIndex) {
        if (atIndex >= length()) {
            return null;
        }
        NSAttributedSpan[] objects = getSpans(atIndex, 1, NSAttributedSpan.class);
        if (objects.length > 0) {
            return objects[0].attrs;
        }
        else {
            return null;
        }
    }

    protected void reset(final HashMap<String, Object> attrs,final NSRange range) {
        setSpan(new NSAttributedSpan(attrs), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (attrs.get(NSFontAttributeName) != null && UIFont.class.isAssignableFrom(attrs.get(NSFontAttributeName).getClass())) {
            UIFont font = (UIFont) attrs.get(NSFontAttributeName);
            setSpan(new TypefaceSpan(font.fontFamily), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new AbsoluteSizeSpan((int)font.fontSize, true), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (attrs.get(NSParagraphStyleAttributeName) != null && NSParagraphStyle.class.isAssignableFrom(attrs.get(NSParagraphStyleAttributeName).getClass())) {
            NSParagraphStyle style = (NSParagraphStyle) attrs.get(NSParagraphStyleAttributeName);
            if (style.alignment != Layout.Alignment.ALIGN_NORMAL) {
                setSpan(new AlignmentSpan.Standard(style.alignment), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (attrs.get(NSForegroundColorAttributeName) != null && Number.class.isAssignableFrom(attrs.get(NSForegroundColorAttributeName).getClass())) {
            setSpan(new ForegroundColorSpan((int)attrs.get(NSForegroundColorAttributeName)), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else {
            setSpan(new ForegroundColorSpan(Color.BLACK), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (attrs.get(NSBackgroundColorAttributeName) != null && Number.class.isAssignableFrom(attrs.get(NSBackgroundColorAttributeName).getClass())) {
            setSpan(new BackgroundColorSpan((int)attrs.get(NSBackgroundColorAttributeName)), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (attrs.get(NSKernAttributeName) != null && Number.class.isAssignableFrom(attrs.get(NSKernAttributeName).getClass())) {
            if ((float)attrs.get(NSKernAttributeName) != 0) {
                setSpan(new CharacterStyle() {
                    @Override
                    public void updateDrawState(TextPaint textPaint) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textPaint.setLetterSpacing((float)attrs.get(NSKernAttributeName));
                        }
                    }
                }, range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (attrs.get(NSUnderlineStyleAttributeName) != null && Number.class.isAssignableFrom(attrs.get(NSUnderlineStyleAttributeName).getClass())) {
            if ((int)attrs.get(NSUnderlineStyleAttributeName) == 1) {
                setSpan(new UnderlineSpan(), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (attrs.get(NSStrikethroughStyleAttributeName) != null && Number.class.isAssignableFrom(attrs.get(NSStrikethroughStyleAttributeName).getClass())) {
            if ((int)attrs.get(NSStrikethroughStyleAttributeName) == 1) {
                setSpan(new StrikethroughSpan(), range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (attrs.get(NSShadowAttributeName) != null && NSShadow.class.isAssignableFrom(attrs.get(NSShadowAttributeName).getClass())) {
            final NSShadow shadow = (NSShadow) attrs.get(NSShadowAttributeName);
            setSpan(new CharacterStyle() {
                @Override
                public void updateDrawState(TextPaint textPaint) {
                    textPaint.setShadowLayer((float) shadow.shadowBlurRadius, (float) shadow.shadowOffset.getWidth(), (float) shadow.shadowOffset.getHeight(), shadow.shadowColor);
                }
            }, range.location, range.location + range.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}

class NSAttributedSpan extends CharacterStyle {

    public HashMap<String, Object> attrs;

    public NSAttributedSpan(HashMap<String, Object> attrs) {
        this.attrs = attrs;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {}

}