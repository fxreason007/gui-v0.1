# Руководство по GUI для Exalted Client

Это руководство поможет вам понять структуру GUI в Exalted Client и научит вас создавать и модифицировать элементы интерфейса.

## Содержание

1. [Структура GUI](#структура-gui)
2. [Основные компоненты](#основные-компоненты)
3. [Анимации](#анимации)
4. [Рендеринг](#рендеринг)
5. [Обработка ввода](#обработка-ввода)
6. [Примеры модификаций](#примеры-модификаций)

## Структура GUI

GUI в Exalted Client построен на основе следующей архитектуры:

- `ClickGuiScreen` - главный экран GUI, наследуется от Minecraft Screen
- `ClickGuiWindow` - окно GUI, содержит основную логику отображения и взаимодействия
- `ClickGuiRenderer` - отвечает за отрисовку элементов GUI
- `Rect` - базовый класс для прямоугольных элементов интерфейса
- `InputWidget` - компонент для ввода текста
- `Animation` - класс для создания плавных анимаций

## Основные компоненты

### ClickGuiScreen

Этот класс является точкой входа для GUI. Он наследуется от `Screen` Minecraft и обрабатывает основные события:

```java
public class ClickGuiScreen extends Screen implements IMinecraft {
    private final ClickGuiWindow window;
    
    public ClickGuiScreen() {
        super(new TranslationTextComponent("Click Gui"));
        float width = 488, height = 318;
        this.window = new ClickGuiWindow(sr.scaledWidth() / 2 - width / 2, sr.scaledHeight() / 2 - height / 2, width, height);
        ClickGuiRenderer.opening.reset();
    }
    
    // Методы для обработки событий мыши, клавиатуры и т.д.
}
```

### ClickGuiWindow

Этот класс представляет собой окно GUI и содержит логику для:
- Отображения категорий модулей
- Отображения списка модулей
- Обработки кликов и скроллинга
- Перетаскивания окна

```java
public class ClickGuiWindow extends Rect implements IMinecraft {
    private TypeList currentCategory = TypeList.Combat;
    private ClickGuiRenderer renderer;
    private float scroll = 0;
    
    // Методы для отрисовки и обработки событий
}
```

### Rect

Базовый класс для прямоугольных элементов интерфейса:

```java
public class Rect implements IMinecraft {
    protected float x, y, width, height;
    
    // Конструкторы и методы для работы с координатами
    
    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
```

## Анимации

Для создания плавных анимаций используется класс `Animation`:

```java
public class Animation implements IMinecraft {
    private long startTime = System.currentTimeMillis();
    private int speed = 300; // Скорость анимации в миллисекундах
    private double size = 1; // Размер анимации
    private boolean forward = true; // Направление анимации
    private Easing easing = Easing.EASE_OUT_SINE; // Функция плавности
    
    // Методы для управления анимацией
}
```

Пример использования анимации:

```java
// Создание анимации
Animation myAnimation = new Animation().setSpeed(300);

// В методе рендеринга
myAnimation.setDirection(isVisible); // true для появления, false для исчезновения
float alpha = myAnimation.get() * 255; // Получение текущего значения анимации (от 0 до 1)
```

## Рендеринг

Для рендеринга элементов GUI используются методы из класса `RenderUtils`:

```java
// Рисование прямоугольника
RenderUtils.Render2D.drawRect(x, y, width, height, color);

// Рисование прямоугольника со скругленными углами
RenderUtils.Render2D.drawRoundedRect(x, y, width, height, radius, color);

// Рисование текста
mc.fontRenderer.drawString(matrixStack, text, x, y, color);
// или
Fonts.newcode[18].drawString(matrixStack, text, x, y, color);
```

## Обработка ввода

Для обработки ввода пользователя переопределяются следующие методы:

```java
// Клик мышью
@Override
public boolean mouseClicked(double mouseX, double mouseY, int button) {
    // button: 0 - левая кнопка, 1 - правая кнопка, 2 - средняя кнопка
}

// Отпускание кнопки мыши
@Override
public boolean mouseReleased(double mouseX, double mouseY, int button) {
}

// Перетаскивание мышью
@Override
public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
}

// Скроллинг
@Override
public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    // delta > 0 - скролл вверх, delta < 0 - скролл вниз
}

// Нажатие клавиши
@Override
public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    // keyCode - код клавиши GLFW
}

// Ввод символа
@Override
public boolean charTyped(char codePoint, int modifiers) {
}
```

## Примеры модификаций

### Изменение цвета GUI

```java
// В классе ClickGuiRenderer
public ClickGuiRenderer() {
    backgroundColor = new Color(20, 20, 25, 220); // Фон
    accentColor = new Color(0, 120, 255, 220);   // Акцентный цвет
    textColor = new Color(255, 255, 255, 220);   // Цвет текста
}
```

### Добавление нового элемента

```java
// В методе renderWindow класса ClickGuiRenderer
private void renderWindow(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    // Существующий код...
    
    // Добавляем новую кнопку
    float buttonX = x + width - 30;
    float buttonY = y + 5;
    boolean isHovered = mouseX >= buttonX && mouseY >= buttonY && mouseX < buttonX + 20 && mouseY < buttonY + 20;
    
    // Рисуем фон кнопки
    RenderUtils.Render2D.drawRoundedRect(buttonX, buttonY, 20, 20, 4, 
            isHovered ? ColorUtils.rgba(255, 255, 255, 150) : ColorUtils.rgba(200, 200, 200, 100));
    
    // Рисуем иконку или текст кнопки
    Fonts.newcode[14].drawCenteredString(matrixStack, "X", buttonX + 10, buttonY + 6, 
            ColorUtils.rgba(0, 0, 0, 200));
}
```

### Обработка клика по новому элементу

```java
// В методе clicked класса ClickGuiWindow
public boolean clicked(double mouseX, double mouseY, int button) {
    // Проверяем клик на новую кнопку
    float buttonX = x + width - 30;
    float buttonY = y + 5;
    if (mouseX >= buttonX && mouseY >= buttonY && mouseX < buttonX + 20 && mouseY < buttonY + 20 && button == 0) {
        // Действие при клике на кнопку
        close(); // Например, закрытие GUI
        return true;
    }
    
    // Существующий код...
}
```

### Улучшение анимации

```java
// В классе Animation
public Animation setEasing(Easing easing) {
    this.easing = easing;
    return this;
}

// Использование разных функций плавности
Animation fadeAnimation = new Animation().setSpeed(300).setEasing(Easing.EASE_OUT_CUBIC);
Animation scaleAnimation = new Animation().setSpeed(400).setEasing(Easing.EASE_OUT_ELASTIC);
```

## Заключение

Это базовое руководство по работе с GUI в Exalted Client. Для более глубокого понимания рекомендуется изучить исходный код классов GUI и экспериментировать с различными модификациями.

Помните, что хороший GUI должен быть:
- Интуитивно понятным
- Отзывчивым
- Визуально приятным
- Функциональным

При разработке GUI старайтесь следовать этим принципам и тестировать ваши изменения на разных разрешениях экрана. 
