package org.assistant.sigma.ui.overview;

import android.databinding.DataBindingUtil;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.joanzapata.iconify.IconDrawable;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActOverviewBinding;
import org.assistant.sigma.databinding.ItemOverviewCategoryBinding;
import org.assistant.sigma.model.catalogs.DefaultTransactionCategories;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.utils.TextUtils;
import org.assistant.sigma.utils.services.CategoryIconProvider;

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
        mPresenter.computeCurrentLargePeriodSpent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    public void addSpent(TransactionCategory category, float percent, double spent) {
        addChartItem(category.getId(), percent);
        addCategorySpent(category, percent, spent);
    }

    private void addChartItem(int categoryId, float value) {
        int color = getCategoryColor(categoryId);
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

    private void addCategorySpent(TransactionCategory category, float percent, double amount) {
        View categoryView = getLayoutInflater().inflate(
                R.layout.item_overview_category,
                vBind.llRoot,
                false
        );
        ItemOverviewCategoryBinding itemBind = ItemOverviewCategoryBinding.bind(categoryView);
        IconDrawable icon = CategoryIconProvider.makeCategoryIcon(
                this,
                category,
                getCategoryColor(category.getId())
        );

        itemBind.ivIcon.setImageDrawable(icon);
        itemBind.tvCategoryDescription.setText(category.getName());
        itemBind.tvAmount.setText(TextUtils.asMoney(amount));
        itemBind.tvPercent.setText(String.format("%s%%", Math.round(percent)));

        vBind.llCategories.addView(categoryView);
    }

    @NonNull
    private PointF getInsetPointF() {
        float pointCoordinate = chartLinesCount * CHART_LINE_WIDTH;
        return new PointF(
                pointCoordinate,
                pointCoordinate
        );
    }

    private int getCategoryColor(int categoryId) {
        int colorId;
        switch (categoryId) {
            case DefaultTransactionCategories.ID_PROVISIONS:
                colorId = R.color.colorCategoryProvisions;
                break;
            case DefaultTransactionCategories.ID_BAR:
                colorId = R.color.colorCategoryBar;
                break;
            case DefaultTransactionCategories.ID_HOME:
                colorId = R.color.colorCategoryHome;
                break;
            case DefaultTransactionCategories.ID_RESTAURANT:
                colorId = R.color.colorCategoryRestaurant;
                break;
            case DefaultTransactionCategories.ID_CLOTHES:
                colorId = R.color.colorCategoryClothes;
                break;
            case DefaultTransactionCategories.ID_TRANSPORT:
                colorId = R.color.colorCategoryTransport;
                break;
            case DefaultTransactionCategories.ID_OTHER_SPENT:
                colorId = R.color.colorCategoryOther;
                break;
            default:
                colorId = R.color.colorCategoryProvisions;
                break;
        }

        return ContextCompat.getColor(
                this,
                colorId
        );
    }
}
