package es.indios.markn.ui.main;

import java.util.List;

import es.indios.markn.data.model.Ribot;
import es.indios.markn.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
