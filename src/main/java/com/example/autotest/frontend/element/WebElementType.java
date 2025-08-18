package com.example.autotest.frontend.element;

public enum WebElementType {

    LOADER("Индикатор загрузки"),
    STEPPER_ITEM("Раздел навигации"),
    BUTTON("Кнопка"),
    CHECKBOX("Чекбокс"),
    DROPBOX("Выпадающий список"),
    OPTION("Опция"),
    IMAGE("Изображение"),
    MENU_ITEM("Раздел"),
    SWITCHER("Переключатель"),
    TAB("Вкладка"),
    SPOILER("Спойлер"),
    TEXT_FIELD("Поле"),
    TABLE("Таблица"),
    HEADER("Заголовок"),
    BODY("Тело"),
    ROW("Строка"),
    CELL("Ячейка"),
    MENU("Меню"),
    MAIN_MENU("Главное меню"),
    SWIPER("Панель вкладок"),
    MODAL_WINDOW("Модальное окно"),
    LINK("Ссылка"),
    LABEL("Надпись"),
    BREADCRUMB("Меню навигации"),
    PAGE("Страница");

    private final String typeName;

    WebElementType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName(){
        return typeName;
    }
}
