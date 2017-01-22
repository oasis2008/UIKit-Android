package com.yy.codex.uikit

import android.content.Context

/**
 * Created by PonyCui_Home on 2017/1/20.
 */

class UINavigationController(context: Context) : UIViewController(context) {

    val navigationBar: UINavigationBar = UINavigationBar(context)
    val wrapperView: UIView = UIView(context)

    fun setRootViewController(rootViewController: UIViewController) {
        setViewControllers(arrayOf(rootViewController))
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        view!!.addSubview(wrapperView)
        view!!.addSubview(navigationBar)
    }

    fun setViewControllers(viewControllers: Array<UIViewController>) {
        for (childViewController in childViewControllers) {
            childViewController?.let(UIViewController::removeFromParentViewController)
        }
        for (childViewController in viewControllers) {
            childViewController?.let { addChildViewController(it) }
        }
        resetNavigationItems()
        resetChildViews()
    }

    private var beingPush = false

    fun pushViewController(viewController: UIViewController, animated: Boolean) {
        beingPush = true
        addChildViewController(viewController)
        resetNavigationItems()
        resetChildViews()
        beingPush = false
        doPushAnimation()
    }

    private fun doPushAnimation() {

    }

    private val beingPop = false

    fun popViewController(animated: Boolean) {

    }

    fun resetNavigationItems() {
        val viewControllers = childViewControllers
        val navigationItems = arrayOfNulls<UINavigationItem>(viewControllers.size)
        for (i in viewControllers.indices) {
            navigationItems[i] = viewControllers[i].navigationItem
        }
        navigationBar.setItems(navigationItems, false)
    }

    fun resetChildViews() {
        if (beingPush) {
            val childViewControllers = childViewControllers
            if (childViewControllers.size > 0) {
                val currentView = childViewControllers[childViewControllers.size - 1].view
                wrapperView.addSubview(currentView!!)
            }
        } else if (beingPop) {
            val childViewControllers = childViewControllers
            if (childViewControllers.size > 0) {
                val currentView = childViewControllers[childViewControllers.size - 1].view
                currentView!!.removeFromSuperview()
            }
        } else {
            val subviews = wrapperView.subviews
            for (i in subviews.indices) {
                subviews[i].removeFromSuperview()
            }
            val childViewControllers = childViewControllers
            for (i in childViewControllers.indices) {
                val currentView = childViewControllers[i].view
                wrapperView.addSubview(currentView!!)
            }
        }
        resetContentViewsFrame()
    }

    override fun viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        wrapperView.frame = CGRect(0.0, 0.0, view!!.frame.width, view!!.frame.height)
        resetContentViewsFrame()
    }

    private fun resetContentViewsFrame() {
        val subviews = wrapperView.subviews
        for (i in subviews.indices) {
            val currentView = subviews[i]
            currentView.frame = CGRect(0.0, topLayoutLength(), wrapperView.frame.width, wrapperView.frame.height - topLayoutLength())
        }
    }



}