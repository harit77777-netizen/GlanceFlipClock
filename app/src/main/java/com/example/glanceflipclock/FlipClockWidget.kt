package com.example.glanceflipclock

import android.content.Context
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.appwidget.*
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider

class FlipClockWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = WidgetStateHolder.getState(context)
            FlipClockContent(state)
        }
    }

    private fun createFlipperRemoteViews(context: Context, oldText: String, newText: String): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.flip_digit)
        rv.removeAllViews(R.id.digit_flipper)
        
        val rvOld = RemoteViews(context.packageName, R.layout.single_card).apply { setTextViewText(R.id.digit_text, oldText) }
        val rvNew = RemoteViews(context.packageName, R.layout.single_card).apply { setTextViewText(R.id.digit_text, newText) }
        
        rv.addView(R.id.digit_flipper, rvOld)
        rv.addView(R.id.digit_flipper, rvNew)
        
        if (oldText != newText) {
            rv.showNext(R.id.digit_flipper)
        }
        return rv
    }

    @Composable
    private fun FlipClockContent(state: WidgetStateHolder.WidgetState) {
        val context = LocalContext.current
        Column(
            modifier = GlanceModifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FlipCardWhite(state.oldHours, state.newHours, context)
                Spacer(modifier = GlanceModifier.width(33.dp))
                FlipCardWhite(state.oldMinutes, state.newMinutes, context)
            }
            Spacer(modifier = GlanceModifier.height(20.dp))
            Text(text = state.date, style = TextStyle(color = ColorProvider(Color.LightGray), fontSize = 16.sp))
        }
    }

    @Composable
    private fun FlipCardWhite(oldValue: String, newValue: String, context: Context) {
        Box(modifier = GlanceModifier.size(120.dp, 113.dp), contentAlignment = Alignment.Center) {
            // 1. Теневые слои (стопка)
            Box(modifier = GlanceModifier.size(120.dp, 113.dp).background(ColorProvider(Color(0xFFD0D0D0))).cornerRadius(12.dp)) {}
            Box(modifier = GlanceModifier.size(120.dp, 108.dp).background(ColorProvider(Color(0xFFE8E8E8))).cornerRadius(12.dp)) {}

            // 2. ИСПРАВЛЕНИЕ: Статичный белый фон. Теперь при сжатии анимации мы видим белую карточку, а не серую дыру.
            Box(modifier = GlanceModifier.size(120.dp, 103.dp).background(ColorProvider(Color(0xFFFFFFFF))).cornerRadius(12.dp)) {}

            // 3. Анимированная карточка
            Box(modifier = GlanceModifier.size(120.dp, 103.dp), contentAlignment = Alignment.Center) {
                AndroidRemoteViews(
                    remoteViews = createFlipperRemoteViews(context, oldValue, newValue),
                    modifier = GlanceModifier.fillMaxSize()
                )
            }
        }
    }
}
