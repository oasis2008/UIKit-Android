package com.yy.codex.uikit.sample;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.yy.codex.foundation.NSLog;
import com.yy.codex.foundation.NSNotification;
import com.yy.codex.foundation.NSNotificationCenter;
import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.NSAttributedString;
import com.yy.codex.uikit.UIBarButtonItem;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIEdgeInsets;
import com.yy.codex.uikit.UIFont;
import com.yy.codex.uikit.UIImage;
import com.yy.codex.uikit.UIKeyboardManager;
import com.yy.codex.uikit.UIKeyboardType;
import com.yy.codex.uikit.UINavigationController;
import com.yy.codex.uikit.UINavigationController_ActivityBase;
import com.yy.codex.uikit.UIReturnKeyType;
import com.yy.codex.uikit.UITabBarActivity;
import com.yy.codex.uikit.UITabBarController;
import com.yy.codex.uikit.UITabBarItem;
import com.yy.codex.uikit.UITextField;
import com.yy.codex.uikit.UIView;
import com.yy.codex.uikit.UIViewController;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends UITabBarActivity {

    @NotNull
    @Override
    public List<UIViewController> createViewControllers() {
        UIImage iconImage = new UIImage(this, R.drawable.ani_19);
        UINavigationController navigationControllerI = new UINavigationController_ActivityBase(this);
        navigationControllerI.setRootViewController(new TestViewController(this));
        navigationControllerI.setTabBarItem(new UITabBarItem());
        navigationControllerI.getTabBarItem().setTitle("Test");
        navigationControllerI.getTabBarItem().setImage(iconImage);
        UINavigationController navigationControllerII = new UINavigationController(this);
        navigationControllerII.setRootViewController(new NextViewController(this));
        navigationControllerII.setTabBarItem(new UITabBarItem());
        navigationControllerII.getTabBarItem().setTitle("Second");
        navigationControllerII.getTabBarItem().setImage(iconImage);
        ArrayList<UIViewController> list = new ArrayList<>();
        list.add(navigationControllerI);
        list.add(navigationControllerII);
        return list;
    }

    @NotNull
    @Override
    public UINavigationController createNavigationController() {
        return new UINavigationController_ActivityBase(this);
    }

//    @NotNull
//    @Override
//    public UIViewController rootViewController() {
//        return new TestViewController(this);
//    }

}

class TestViewController extends UIViewController {

    public TestViewController(Context context) {
        super(context);
    }

    @Override
    public void loadView() {
        setView(loadViewFromXML(R.layout.test_view));
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setTitle("Test");
        getNavigationItem().setRightBarButtonItem(new UIBarButtonItem("Next", this, "handleNextButtonTapped"));
        UITextField textField = (UITextField) getView().findViewById(R.id.roundView);
        textField.setReturnKeyType(UIReturnKeyType.Next);
        textField.setPlaceholder("请输入密码");
        UIView leftView = new UIView(getContext());
        leftView.setFrame(new CGRect(0, 0, 20, 10));
        leftView.setBackgroundColor(UIColor.Companion.getBlackColor());
        textField.setLeftView(leftView);
        textField.setLeftViewMode(UITextField.ViewMode.UnlessEditing);
        UIView rightView = new UIView(getContext());
        rightView.setFrame(new CGRect(0, 0, 20, 10));
        rightView.setBackgroundColor(UIColor.Companion.getBlackColor());
        textField.setRightView(rightView);
        textField.setRightViewMode(UITextField.ViewMode.WhileEditing);
    }

    private void handleNextButtonTapped() {
        NextViewController nextViewController = new NextViewController(getContext());
        nextViewController.setHidesBottomBarWhenPushed(true);
        navigationController().pushViewController(nextViewController, true);
    }

}

class NextViewController extends UIViewController {

    public NextViewController(@NonNull Context context) {
        super(context);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setTitle("I'm Next");
        getView().setBackgroundColor(UIColor.Companion.getGrayColor());
        getNavigationItem().setRightBarButtonItem(new UIBarButtonItem("Next", this, "handleNextButtonTapped"));
    }

    private void handleNextButtonTapped() {
        NextViewController nextViewController = new NextViewController(getContext());
        nextViewController.setHidesBottomBarWhenPushed(true);
        navigationController().pushViewController(nextViewController, true);
    }

}

class TestView extends UIView {

    public TestView(Context context, View view) {
        super(context, view);
    }

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void init() {
        super.init();
        final UIView testView = new UIView(getContext());
        testView.setFrame(new CGRect(44, 44, 44, 44));
        testView.setWantsLayer(true);
        testView.getLayer().setBackgroundColor(UIColor.Companion.getOrangeColor());
        testView.getLayer().setCornerRadius(22.0);
        testView.getLayer().setShadowColor(UIColor.Companion.getBlackColor().colorWithAlpha(0.5));
        testView.getLayer().setShadowRadius(8.0);
        testView.getLayer().setShadowX(2);
        testView.getLayer().setShadowY(2);
        testView.setBackgroundColor(UIColor.Companion.getOrangeColor());
        addSubview(testView);
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                UIViewAnimator.INSTANCE.spring(new Runnable() {
//                    @Override
//                    public void run() {
//                        testView.setFrame(new CGRect(200, 200, 88, 88));
//                        testView.getLayer().setCornerRadius(44.0);
//                    }
//                }, null);
//            }
//        }, 3000);
    }

}
