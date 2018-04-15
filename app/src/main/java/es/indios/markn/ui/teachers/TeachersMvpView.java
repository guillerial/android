package es.indios.markn.ui.teachers;

import java.util.ArrayList;
import java.util.List;

import es.indios.markn.data.model.uvigo.Teacher;
import es.indios.markn.ui.base.MvpView;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public interface TeachersMvpView extends MvpView {
    void setTeachers(ArrayList<Teacher> teachers);
}
