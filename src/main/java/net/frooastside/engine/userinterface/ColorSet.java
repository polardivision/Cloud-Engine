package net.frooastside.engine.userinterface;

public class ColorSet {

  public static final ColorSet DARK_MODE = new ColorSet(
    new Color(0.2f, 0.2f, 0.2f),
    new Color(0.24f, 0.24f, 0.24f),
    new Color(0.28f, 0.28f, 0.28f),
    new Color(1.0f, 1.0f, 1.0f),
    new Color(0.1215f, 0.5529f, 0.1215f));

  private final Color background;
  private final Color baseElement;
  private final Color element;
  private final Color text;
  private final Color accent;

  public ColorSet(Color background, Color baseElement, Color element, Color text, Color accent) {
    this.background = background;
    this.baseElement = baseElement;
    this.element = element;
    this.text = text;
    this.accent = accent;
  }

  public Color background() {
    return background;
  }

  public Color baseElement() {
    return baseElement;
  }

  public Color element() {
    return element;
  }

  public Color text() {
    return text;
  }

  public Color accent() {
    return accent;
  }
}