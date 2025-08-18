package com.example.autotest.frontend.element.container;

import com.example.autotest.frontend.element.RegularWebElement;
import com.example.autotest.frontend.element.common.table.Row;
import com.example.autotest.support.frontend.WebElementUtils;

import java.util.List;

public interface RowContainer<T extends Row<?>> extends WebElementContainer, RegularWebElement {

    List<T> getRows();

    default T getRow(int rowIndex) {
        return WebElementUtils.getComponent(rowIndex, getRows());
    }

    default T getRow(int cellIndex, String valueToSearch, boolean mustEqual) {
        return WebElementUtils.getComponent(cellIndex, valueToSearch, mustEqual, getRows());
    }

    default boolean hasRow(int cellIndex) {
        return WebElementUtils.hasComponent(cellIndex, getRows());
    }

    default boolean hasRow(int cellIndex, String valueToSearch, boolean mustEqual) {
        return WebElementUtils.hasComponent(cellIndex, valueToSearch, mustEqual, getRows());
    }
}
