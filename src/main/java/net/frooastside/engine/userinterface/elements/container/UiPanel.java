package net.frooastside.engine.userinterface.elements.container;

import net.frooastside.engine.userinterface.UiConstraints;
import net.frooastside.engine.userinterface.UiColorSet;
import net.frooastside.engine.userinterface.elements.UiFunctionalElement;
import net.frooastside.engine.userinterface.elements.render.UiBox;

public class UiPanel extends UiFunctionalElement {

  private final UiColorSet colorSet;

  public UiPanel(UiColorSet colorSet) {
    this.colorSet = colorSet;
  }

  @Override
  public void initialize() {
    UiBox background = new UiBox(colorSet.background());
    addElement(background);
  }

}
