package es.indios.markn.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;

import java.util.concurrent.atomic.AtomicLong;

import es.indios.markn.MarknApplication;
import es.indios.markn.injection.component.ActivityComponent;
import es.indios.markn.injection.component.ConfigPersistentComponent;
import es.indios.markn.injection.component.DaggerConfigPersistentComponent;
import es.indios.markn.injection.component.FragmentComponent;
import es.indios.markn.injection.module.FragmentModule;
import timber.log.Timber;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class BaseFragment extends Fragment {

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent>
            sComponentsMap = new LongSparseArray<>();

    private FragmentComponent mFragmentComponent;
    private long mFragmentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mFragmentId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_FRAGMENT_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent = sComponentsMap.get(mFragmentId, null);

        if (configPersistentComponent == null) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mFragmentId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(MarknApplication.get(this.getActivity()).getComponent())
                    .build();
            sComponentsMap.put(mFragmentId, configPersistentComponent);
        }
        mFragmentComponent = configPersistentComponent.fragmentComponent(new FragmentModule(this));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_FRAGMENT_ID, mFragmentId);
    }

    @Override
    public void onDestroy() {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mFragmentId);
            sComponentsMap.remove(mFragmentId);
        super.onDestroy();
    }

    public FragmentComponent fragmentComponent() {
        return mFragmentComponent;
    }


}
