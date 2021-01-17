package pl.sgnit.ims.views.util;

public interface QueryableView {

    void doUserQuery(String where);

    long countRecords();

    String getFieldPrefix();
}
