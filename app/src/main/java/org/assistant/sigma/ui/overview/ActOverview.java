package org.assistant.sigma.ui.overview;

import android.databinding.DataBindingUtil;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.joanzapata.iconify.IconDrawable;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActOverviewBinding;
import org.assistant.sigma.databinding.ItemOverviewCategoryBinding;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.ui.util.CategoryStylesProvider;
import org.assistant.sigma.utils.TextUtils;

/**
 * Created by giovanni on 24/12/17.
 *
 */
public class ActOverview extends AppCompatActivity {
    private final float CHART_LINE_WIDTH = 16f;
    private ActOverviewBinding vBind;
    private OverviewPresenter mPresenter;

    /**
     * Keeps a count of all added spent amounts.
     * Useful to calculate the inset point for future added
     * items
     */
    private int chartLinesCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vBind = DataBindingUtil.setContentView(
                this,
                R.layout.act_overview
        );

        mPresenter = new OverviewPresenter(this);
        mPresenter.calcCurrLgPeriodSpentByCategory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    public void renderSpentForCategory(TransactionCategory category, double spent, float percent) {
        addCategoryChartItem(category.getId(), percent);
        addCategoryItem(category, spent, percent);
    }

    public void renderLgPeriodSpent(String periodName, double spent) {
        vBind.tvLgPeriodName.setText(periodName);
        vBind.tvLgPeriodSpent.setText(TextUtils.asMoney(spent));
    }

    public void renderShPeriodSpent(String periodName, double spent) {
        vBind.tvShPeriodName.setText(periodName);
        vBind.tvShPeriodSpent.setText(TextUtils.asMoney(spent));
    }

    private void addCategoryItem(TransactionCategory category, double amount, float percent) {
        View categoryView = getLayoutInflater().inflate(
                R.layout.item_overview_category,
                vBind.llRoot,
                false
        );
        ItemOverviewCategoryBinding itemBind = ItemOverviewCategoryBinding.bind(categoryView);
        IconDrawable icon = CategoryStylesProvider.makeCategoryIcon(
                this,
                category,
                CategoryStylesProvider.getCategoryColor(this, category)
        );

        itemBind.ivIcon.setImageDrawable(icon);
        itemBind.tvCategoryDescription.setText(category.getName());
        itemBind.tvAmount.setText(TextUtils.asMoney(amount));
        itemBind.tvPercent.setText(String.format("%s%%", Math.round(percent)));

        vBind.llCategories.addView(categoryView);
    }

    private void addCategoryChartItem(int categoryId, float value) {
        int color = CategoryStylesProvider.getCategoryColor(this, categoryId);
        SeriesItem.Builder builder = new SeriesItem.Builder(color);
        builder.setRange(0, 100, 0);
        builder.setLineWidth(CHART_LINE_WIDTH);
        if (chartLinesCount > 0) {
            builder.setInset(getInsetPointF());
        }

        int idx = vBind.dynamicArcView.addSeries(builder.build());
        DecoEvent event = new DecoEvent.Builder(value).setIndex(idx).setDelay(500).build();
        vBind.dynamicArcView.addEvent(event);

        chartLinesCount++;
    }

    private PointF getInsetPointF() {
        float pointCoordinate = chartLinesCount * CHART_LINE_WIDTH;
        return new PointF(
                pointCoordinate,
                pointCoordinate
        );
    }
}
