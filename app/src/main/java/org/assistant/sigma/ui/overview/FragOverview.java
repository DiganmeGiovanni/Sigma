package org.assistant.sigma.ui.overview;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.joanzapata.iconify.IconDrawable;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragOverviewBinding;
import org.assistant.sigma.databinding.ItemOverviewCategoryBinding;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.ui.util.CategoryStylesProvider;
import org.assistant.sigma.utils.TextUtils;

import java.util.Date;

/**
 * Created by giovanni on 5/01/18.
 *
 */
public class FragOverview extends Fragment {
    public static final String START_TIME_MILLIS = "START_TIME";

    private final float CHART_LINE_WIDTH = 24f;
    private FragOverviewBinding vBind;
    private OverviewPresenter mPresenter;

    /**
     * Keeps a count of all added spent amounts.
     * Useful to calculate the inset point for future added
     * items
     */
    private int chartLinesCount = 0;

    /**
     * Overview to render will begins on this date.
     * This time is recovered from fragment arguments
     */
    private Date start;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new OverviewPresenter();

        if (getArguments() != null && getArguments().containsKey(START_TIME_MILLIS)) {
            long startTime = getArguments().getLong(START_TIME_MILLIS);
            start = new Date(startTime);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_overview, container, false);
        vBind = FragOverviewBinding.bind(rootView);

        mPresenter.calcSpentByCategorySince(start, this);
        mPresenter.calcSpentSince(start, this);
        mPresenter.calcIncomeSince(start, this);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void renderIncome(double income) {
        vBind.tvIncome.setText(TextUtils.asMoney(income));
    }

    public void renderSpent(double spent) {
        vBind.tvSpent.setText(TextUtils.asMoney(spent));
    }

    public void renderSpentForCategory(TransactionCategory category, double spent, float percent) {
        addCategoryChartItem(category.getId(), percent);
        addCategoryItem(category, spent, percent);
    }

    /**
     * Hides graphic and text views to show only and empty state view
     */
    public void showEmptyState() {
        vBind.dynamicArcView.setVisibility(View.GONE);
        vBind.tvLbIncome.setVisibility(View.GONE);
        vBind.tvIncome.setVisibility(View.GONE);
        vBind.tvLbSpent.setVisibility(View.GONE);
        vBind.tvSpent.setVisibility(View.GONE);

        vBind.tvWithoutTransactions.setVisibility(View.VISIBLE);
    }

    private void addCategoryItem(TransactionCategory category, double amount, float percent) {
        View categoryView = getLayoutInflater().inflate(
                R.layout.item_overview_category,
                vBind.llRoot,
                false
        );
        ItemOverviewCategoryBinding itemBind = ItemOverviewCategoryBinding.bind(categoryView);
        IconDrawable icon = CategoryStylesProvider.makeCategoryIcon(
                getContext(),
                category,
                CategoryStylesProvider.getCategoryColor(getContext(), category)
        );

        itemBind.ivIcon.setImageDrawable(icon);
        itemBind.tvCategoryDescription.setText(category.getName());
        itemBind.tvAmount.setText(TextUtils.asMoney(amount));
        itemBind.tvPercent.setText(String.format("%s%%", Math.round(percent)));

        vBind.llCategories.addView(categoryView);
    }

    private void addCategoryChartItem(int categoryId, float value) {
        int color = CategoryStylesProvider.getCategoryColor(getContext(), categoryId);
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
