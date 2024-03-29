/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.window;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.glfw.GLFW;

public enum GamePadButton {
  BUTTON_CROSS(GLFW.GLFW_GAMEPAD_BUTTON_A),
  BUTTON_CIRCLE(GLFW.GLFW_GAMEPAD_BUTTON_B),
  BUTTON_SQUARE(GLFW.GLFW_GAMEPAD_BUTTON_X),
  BUTTON_TRIANGLE(GLFW.GLFW_GAMEPAD_BUTTON_Y),
  BUTTON_LEFT_BUMPER(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER),
  BUTTON_RIGHT_BUMPER(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER),
  BUTTON_BACK(GLFW.GLFW_GAMEPAD_BUTTON_BACK),
  BUTTON_START(GLFW.GLFW_GAMEPAD_BUTTON_START),
  BUTTON_GUIDE(GLFW.GLFW_GAMEPAD_BUTTON_GUIDE),
  BUTTON_LEFT_THUMB(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB),
  BUTTON_RIGHT_THUMB(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB),
  BUTTON_DPAD_UP(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP),
  BUTTON_DPAD_RIGHT(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT),
  BUTTON_DPAD_DOWN(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN),
  BUTTON_DPAD_LEFT(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT),
  BUTTON_UNKNOWN(-1);

  private static final Map<Integer, GamePadButton> GAME_PAD_BUTTONS = new HashMap<>();
  private final int value;

  GamePadButton(int value) {
    this.value = value;
  }

  static {
    Arrays.stream(values()).forEach(button -> GAME_PAD_BUTTONS.put(button.value, button));
  }

  public static GamePadButton of(int button) {
    return GAME_PAD_BUTTONS.getOrDefault(button, BUTTON_UNKNOWN);
  }

  public int value() {
    return value;
  }
}
