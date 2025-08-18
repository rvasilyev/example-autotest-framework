package com.example.autotest.frontend.element.common.table;

import com.example.autotest.frontend.element.container.WebElementContainer;
import com.example.autotest.frontend.element.IndexedWebElement;
import com.example.autotest.support.frontend.WebElementUtils;

import java.util.List;

public interface Row<T extends Cell> extends WebElementContainer, IndexedWebElement {

    List<T> getCells();

    default T getCell(int cellIndex) {
        return WebElementUtils.getComponent(cellIndex, getCells());
    }

    default T getCell(int cellIndex, String valueToSearch, boolean mustEqual) {
        return WebElementUtils.getComponent(cellIndex, valueToSearch, mustEqual, getCells());
    }

    default boolean hasCell(int cellIndex) {
        return WebElementUtils.hasComponent(cellIndex, getCells());
    }

    default boolean hasCell(int cellIndex, String valueToSearch, boolean mustEqual) {
        return WebElementUtils.hasComponent(cellIndex, valueToSearch, mustEqual, getCells());
    }
}
