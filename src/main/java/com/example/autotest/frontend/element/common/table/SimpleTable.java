package com.example.autotest.frontend.element.common.table;

import com.example.autotest.frontend.element.container.HeaderAndBodyContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleTable<T extends TableHeader<?>, S extends TableBody<?>> extends HeaderAndBodyContainer<T, S> implements Table<T, S> {

    public SimpleTable(By locator, String name, T header, S body) {
        super(locator, WebElementType.TABLE, name, header, body);
    }
}
