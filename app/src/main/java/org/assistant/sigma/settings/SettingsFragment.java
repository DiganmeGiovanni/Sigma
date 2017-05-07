package org.assistant.sigma.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragSettingsBinding;
import org.assistant.sigma.model.catalogs.Periods;
import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.utils.TextUtils;

import java.util.Calendar;

/**
 *
 * Created by giovanni on 7/05/17.
 */
public class SettingsFragment extends Fragment implements SettingsContract.View {

    private FragSettingsBinding viewBinding;
    private SettingsContract.Presenter mPresenter;

    private int startDayHour;
    private int reminder1;
    private int reminder2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_settings, container, false);
        viewBinding = FragSettingsBinding.bind(rootView);

        if (mPresenter != null) {
            mPresenter.loadSettings();
        }

        setupSaveBtn();
        return rootView;
    }

    @Override
    public void setPresenter(SettingsContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public Settings createSettingsFromForm() {
        Settings settings = new Settings();

        // Small period
        if (viewBinding.rbPeriodSm1.isChecked()) {
            settings.setSmallPeriod(Periods.SM_DAILY_GROUPED_WEEKEND);
        } else if (viewBinding.rbPeriodSm2.isChecked()) {
            settings.setSmallPeriod(Periods.SM_DAILY);
        }

        // Large period
        if (viewBinding.rbPeriodLg1.isChecked()) {
            settings.setLargePeriod(Periods.LG_MONTHLY);
        } else if (viewBinding.rbPeriodLg2.isChecked()) {
            settings.setLargePeriod(Periods.LG_FORTNIGHT);
        } else if (viewBinding.rbPeriodLg3.isChecked()) {
            settings.setLargePeriod(Periods.LG_WEEKLY);
        }

        settings.setIncludeHomeSpentForLimit(viewBinding.cbIncludeHomeSpent.isChecked());

        double spentLimit = Double.parseDouble(viewBinding.etSpentLimit.getText().toString());
        settings.setSpentLimitLarge(spentLimit);
        settings.setStartDayHour(startDayHour);
        settings.setReminder1(reminder1);
        settings.setReminder2(reminder2);

        return settings;
    }

    @Override
    public void setupSaveBtn() {
        viewBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSettings()) {
                    Settings settings = createSettingsFromForm();
                    mPresenter.saveSettings(settings);

                    // Go back
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    @Override
    public void showSettings(Settings settings) {

        // Small period
        switch (settings.getSmallPeriod()) {
            case Periods.SM_DAILY_GROUPED_WEEKEND:
                viewBinding.rbPeriodSm1.setChecked(true);
                break;
            case Periods.SM_DAILY:
                viewBinding.rbPeriodSm2.setChecked(true);
                break;
        }

        // Large period
        switch (settings.getLargePeriod()) {
            case Periods.LG_MONTHLY:
                viewBinding.rbPeriodLg1.setChecked(true);
                break;
            case Periods.LG_FORTNIGHT:
                viewBinding.rbPeriodLg2.setChecked(true);
                break;
            case Periods.LG_WEEKLY:
                viewBinding.rbPeriodLg3.setChecked(true);
                break;
        }

        // Start day hour
        setStartDayHour(settings.getStartDayHour());

        // Reminder 1
        setReminder1(settings.getReminder1());

        // Reminder 2
        setReminder2(settings.getReminder2());

        // Include home spent in limit
        viewBinding.cbIncludeHomeSpent.setChecked(settings.isIncludeHomeSpentForLimit());

        // Spent limit
        viewBinding.etSpentLimit.setText(String.valueOf(settings.getSpentLimitLarge()));

        // Configure time pickers listeners
        setupHoursETs();
    }

    @Override
    public boolean validateSettings() {
        boolean formOk = true;

        // Remove previous errors
        viewBinding.tilSpentLimit.setError(null);
        viewBinding.tilDayStartHour.setError(null);
        viewBinding.tilTransactionsReminder1.setError(null);
        viewBinding.tilTransactionsReminder2.setError(null);

        // Spent limit
        if (viewBinding.etSpentLimit.getText().length() < 1 ||
                Double.parseDouble(viewBinding.etSpentLimit.getText().toString()) < 0) {
            formOk = false;
            viewBinding.tilSpentLimit.setError(getString(R.string.error_quantity_greater_zero));
        }

        // Start day hour
        if (viewBinding.etStartDayHour.getText().length() < 1 || startDayHour > 24) {
            formOk = false;
            viewBinding.tilDayStartHour.setError(getString(R.string.error_valid_hour));
        }

        // Reminder 1 hour
        if (viewBinding.etTransactionsReminder1.getText().length() < 1 || reminder1 > 24) {
            formOk = false;
            viewBinding.tilTransactionsReminder1.setError(getString(R.string.error_valid_hour));
        }

        // Reminder 2 hour
        if (viewBinding.etTransactionsReminder2.getText().length() < 1 || reminder2 > 24) {
            formOk = false;
            viewBinding.tilTransactionsReminder2.setError(getString(R.string.error_valid_hour));
        }

        // Reminders difference
        if (reminder2 < reminder1) {
            formOk = false;
            viewBinding.tilTransactionsReminder2
                    .setError(getString(R.string.error_must_be_greater_than_first_reminder));
        }

        return formOk;
    }

    private void setupHoursETs() {

        // Start day hour
        viewBinding.etStartDayHour.setKeyListener(null);
        viewBinding.etStartDayHour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    TimePickerDialog dialog = TimePickerDialog.newInstance(
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePickerDialog view, int hourOfDay,
                                                      int minute, int second) {
                                    setStartDayHour(hourOfDay);
                                }
                            },
                            startDayHour,
                            0,
                            false
                    );
                    dialog.enableMinutes(false);
                    dialog.vibrate(false);
                    dialog.show(getActivity().getFragmentManager(), "timePicker");
                }

                return true;
            }
        });

        // Reminder 1 hour
        viewBinding.etTransactionsReminder1.setKeyListener(null);
        viewBinding.etTransactionsReminder1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    TimePickerDialog dialog = TimePickerDialog.newInstance(
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePickerDialog view, int hourOfDay,
                                                      int minute, int second) {
                                    setReminder1(hourOfDay);
                                }
                            },
                            reminder1,
                            0,
                            false
                    );
                    dialog.enableMinutes(false);
                    dialog.vibrate(false);
                    dialog.show(getActivity().getFragmentManager(), "timePicker");
                }

                return true;
            }
        });

        // Reminder 2 hour
        viewBinding.etTransactionsReminder2.setKeyListener(null);
        viewBinding.etTransactionsReminder2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    TimePickerDialog dialog = TimePickerDialog.newInstance(
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePickerDialog view, int hourOfDay,
                                                      int minute, int second) {
                                    setReminder2(hourOfDay);
                                }
                            },
                            reminder2,
                            0,
                            false
                    );
                    dialog.enableMinutes(false);
                    dialog.vibrate(false);
                    dialog.show(getActivity().getFragmentManager(), "timePicker");
                }

                return true;
            }
        });
    }

    public void setStartDayHour(int startDayHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, startDayHour);
        calendar.set(Calendar.MINUTE, 0);
        viewBinding.etStartDayHour.setText(TextUtils.forHumans(calendar, true));

        this.startDayHour = startDayHour;
    }

    public void setReminder1(int reminder1) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminder1);
        calendar.set(Calendar.MINUTE, 0);
        viewBinding.etTransactionsReminder1.setText(TextUtils.forHumans(calendar, true));

        this.reminder1 = reminder1;
    }

    public void setReminder2(int reminder2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminder2);
        calendar.set(Calendar.MINUTE, 0);
        viewBinding.etTransactionsReminder2.setText(TextUtils.forHumans(calendar, true));

        this.reminder2 = reminder2;
    }
}
