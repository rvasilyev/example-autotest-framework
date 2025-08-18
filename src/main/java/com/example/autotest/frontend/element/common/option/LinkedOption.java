package com.example.autotest.frontend.element.common.option;

import org.openqa.selenium.By;
import com.example.autotest.frontend.element.common.link.Link;

public class LinkedOption extends SelectableOption implements Link {

    public LinkedOption(By locator, int index) {
        super(locator, index);
    }
}
