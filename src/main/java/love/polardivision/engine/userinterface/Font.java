/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.polardivision.engine.userinterface;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import love.polardivision.engine.glwrapper.texture.Texture;

public abstract class Font implements Externalizable {

  @Serial
  private static final long serialVersionUID = -1857587127382434365L;

  public static final Character DEFAULT_CHARACTER = new Character(0, 0, 0, 0, 0, 0, 0, 0, 0);
  public static final int SPACE_CODEPOINT = 32;

  private final Map<Integer, Character> supportedCharacters = new HashMap<>();
  private int characterHeight;
  private Texture texture;

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(supportedCharacters);
    out.writeShort(characterHeight);
    out.writeObject(texture);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    for (Map.Entry<?, ?> entry : ((Map<?, ?>) in.readObject()).entrySet()) {
      if (entry.getKey() instanceof Integer key && entry.getValue() instanceof Character value) {
        supportedCharacters.put(key, value);
      }
    }
    characterHeight = in.readShort();
    texture = (Texture) in.readObject();
  }

  public Character character(int codepoint) {
    return supportedCharacters.getOrDefault(codepoint, DEFAULT_CHARACTER);
  }

  public Map<Integer, Character> supportedCharacters() {
    return supportedCharacters;
  }

  public int characterHeight() {
    return characterHeight;
  }

  public void setCharacterHeight(int characterHeight) {
    this.characterHeight = characterHeight;
  }

  public Texture texture() {
    return texture;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  public record Character(double xTextureCoordinate, double yTextureCoordinate, double xMaxTextureCoordinate,
                          double yMaxTextureCoordinate, double xOffset, double yOffset, double xSize, double ySize,
                          double xAdvance) implements Serializable {

  }
}