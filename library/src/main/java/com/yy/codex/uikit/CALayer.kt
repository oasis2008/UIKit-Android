package com.yy.codex.uikit

import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.*


/**
 * Created by cuiminghui on 2017/1/3.
 */

open class CALayer {

    enum class BitmapGravity {
        ScaleAspectFit,
        ScaleAspectFill,
        ScaleToFill,
        TopLeft,
        Top,
        TopRight,
        Left,
        Center,
        Right,
        BottomLeft,
        Bottom,
        BottomRight
    }

    /* layoutProps */

    var frame = CGRect(0.0, 0.0, 0.0, 0.0)
        set(value) {
            val x = field.origin.x.toFloat()
            val y = field.origin.y.toFloat()
            val w = field.size.width.toFloat()
            val h = field.size.height.toFloat()
            val newValue = CGRect(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())
            if (field != newValue) {
                field = newValue
                this.setNeedDisplay(true)
            }
            field = value
        }

    /* styleProps */

    var backgroundColor = UIColor.clearColor
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var cornerRadius = 0.0
        set(value) {
            if (!doubleEqual(field, value)) {
                val oldValue = field
                field = value
                this.setNeedDisplay(true)
                requestRootLayer().view?.let {
                    UIViewAnimator.addAnimationState(it, "layer.mCornerRadius", oldValue, value)
                }
            }
        }


    var borderWidth = 0.0
        set(value) {
            val oldValue = field
            if (!doubleEqual(field, value)) {
                field = value
                this.setNeedDisplay(true)
                requestRootLayer().view?.let {
                    UIViewAnimator.addAnimationState(it, "layer.mBorderWidth", oldValue, value)
                }
            }
        }

    var borderColor = UIColor.blackColor
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var shadowX = 2.0
        set(value) {
            if (!doubleEqual(field, value)) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var shadowY = 2.0
        set(value) {
            if (!doubleEqual(field, value)) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var shadowRadius = 0.0
        set(value) {
            if (!doubleEqual(field, value)) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var shadowColor = UIColor.blackColor
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var bitmap: Bitmap? = null
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var bitmapGravity = BitmapGravity.ScaleAspectFit
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var bitmapColor: UIColor? = null
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var clipToBounds = false
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    var hidden = false
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    /* renderProps */

    private var needDisplay = false

    fun setNeedDisplay(mNeedDisplay: Boolean) {
        this.needDisplay = mNeedDisplay
        if (mNeedDisplay) {
            requestRootLayer().view?.let(UIView::invalidate)
        }
    }

    /*
        以下情况，在新画布绘制。
        1. 有 transform 属性时
        2. 有子节点 且 mClipToBounds 时
    */
    var isNewCanvasContext = false
        get() {
            val result = this.transforms != null && this.transforms!!.size > 0 || this.subLayers.size > 0 && this.clipToBounds
            return result
        }

    var mask: CALayer? = null // not support
        set(value) {
            if (field != value) {
                field = value
                this.setNeedDisplay(true)
            }
        }

    /* hierarchyProps */

    protected var view: UIView? = null

    fun bindView(view: UIView) {
        this.view = view
    }

    var superLayer: CALayer? = null
        private set

    var subLayers: MutableList<CALayer> = mutableListOf()
        private set

    /* transformProp */

    var transforms: List<CGTransform> = listOf()

    fun setTransform(a: CGTransform) {
        this.transforms = listOf(a)
    }

    /* category CALayer Constructor */

    constructor() {}

    constructor(mFrame: CGRect) {
        val x = mFrame.origin.x.toFloat()
        val y = mFrame.origin.y.toFloat()
        val w = mFrame.size.width.toFloat()
        val h = mFrame.size.height.toFloat()
        this.frame = CGRect(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())
    }

    /* category CALayer Hierarchy */

    fun removeFromSuperLayer() {
        superLayer?.let {
            it.subLayers.remove(this)
        }
    }

    fun addSubLayer(layer: CALayer) {
        layer.removeFromSuperLayer()
        layer.superLayer = this
        this.subLayers.add(layer)
    }

    fun insertSubLayerAtIndex(subLayer: CALayer, index: Int) {
        subLayer.removeFromSuperLayer()
        if (index < 1) {
            this.subLayers.add(0, subLayer)
        } else if (index > this.subLayers.size - 1) {
            this.subLayers.add(subLayer)
        } else {
            this.subLayers.add(index, subLayer)
        }
    }

    fun insertBelowSubLayer(subLayer: CALayer, siblingSubview: CALayer) {
        val idx = this.subLayers.indexOf(siblingSubview)
        if (idx > -1) {
            subLayer.removeFromSuperLayer()
            this.subLayers.add(idx, subLayer)
        }
    }

    fun insertAboveSubLayer(subLayer: CALayer, siblingSubview: CALayer) {
        val idx = this.subLayers.indexOf(siblingSubview)
        if (idx > -1) {
            subLayer.removeFromSuperLayer()
            this.subLayers.add(idx + 1, subLayer)
        }
    }

    fun replaceSubLayer(subLayer: CALayer, newLayer: CALayer) {
        val idx = this.subLayers.indexOf(subLayer)
        if (idx > -1) {
            subLayer.removeFromSuperLayer()
            insertSubLayerAtIndex(newLayer, idx)
        }
    }

    /* category CALayer Appearance */

    fun drawRect(canvas: Canvas, rect: CGRect) {
        if (this.askIfNeedDisplay()) {
            this.resetNeedDisplayToFalse()
            drawAllLayers(canvas, rect)
        }
    }

    private fun drawAllLayers(canvas: Canvas, rect: CGRect) {
        if (hidden) {
            return
        }
        if (this.isNewCanvasContext) {
            CALayerPainter.drawLayerTree(this, canvas)
        } else {
            this.drawInCanvas(canvas)
            for (item in this.subLayers) {
                item.drawAllLayers(canvas, rect)
            }
        }
    }

    protected open fun drawInCanvas(canvas: Canvas) {
        CALayerPainter.drawCurrentLayer(this, canvas)
    }

    fun drawLayerTreeInCanvas(canvas: Canvas) {
        this.drawInCanvas(canvas)
        for (item in this.subLayers) {
            item.drawLayerTreeInCanvas(canvas)
        }
    }

    private fun askIfNeedDisplay(): Boolean {
        return true
    }

    private fun resetNeedDisplayToFalse() {
        this.needDisplay = false
        for (item in this.subLayers) {
            item.resetNeedDisplayToFalse()
        }
    }

    private fun requestRootLayer(): CALayer {
        var root: CALayer = this
        while (root.superLayer != null) {
            root = root.superLayer as CALayer
        }
        return root
    }

    private fun doubleEqual(a: Double, b: Double): Boolean {
        if (Math.abs(a - b) < 0.01) {
            return true
        }
        return false
    }

    /* Animation */

    fun animate(aKey: String, aValue: Float) {
        if (aKey.equals("layer.mCornerRadius", ignoreCase = true)) {
            cornerRadius = aValue.toDouble()
        } else if (aKey.equals("layer.mBorderWidth", ignoreCase = true)) {
            borderWidth = aValue.toDouble()
        }
    }

    companion object {

        /* category CALayer support method */

        fun calcOriginInSuperCoordinate(layer: CALayer): CGPoint {
            val scaledDensity = UIScreen.mainScreen.scale().toFloat()
            var oriX = layer.frame.origin.x
            var oriY = layer.frame.origin.y
            var p: CALayer? = layer.superLayer
            while (p != null) {
                oriX += p.frame.origin.x
                oriY += p.frame.origin.y
                p = p.superLayer
            }
            return CGPoint(oriX * scaledDensity, oriY * scaledDensity)
        }
    }

}