package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.graphicobjects.vertexarray.VertexArrayObject;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferDataType;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferTarget;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.BufferUsage;
import net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer.VertexBufferObject;
import net.frooastside.engine.resource.BufferUtils;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.UiColor;
import net.frooastside.engine.userinterface.UiRenderElement;
import org.joml.Vector2f;

public class UiText extends UiRenderElement {

  public static final float LINE_HEIGHT = 0.025f;

  private final VertexArrayObject model = createVertexArrayObject();
  private final ResourceFont font;
  private final boolean centered;

  private String text;

  private float aspectRatio;

  public UiText(ResourceFont font, String text, UiColor color, boolean centered) {
    super.setColor(color);
    this.font = font;
    this.text = text;
    this.centered = centered;
  }

  @Override
  public void recalculate(Vector2f pixelSize) {
    this.aspectRatio = pixelSize.y / pixelSize.x;
    updateModel();
    super.recalculate(pixelSize);
  }

  private void updateModel() {
    double verticalPerPixelSize = LINE_HEIGHT / (double) font.characterHeight();
    double horizontalPerPixelSize = verticalPerPixelSize / aspectRatio;
    double lineLength = 0;
    int characterCount = 0;
    float rawHeight = constraints().height().rawValue();
    for (char asciiCharacter : text.toCharArray()) {
      ResourceFont.Character character = font.getCharacter(asciiCharacter);
      lineLength += (character.xAdvance() * horizontalPerPixelSize) * rawHeight;
      characterCount += asciiCharacter != ResourceFont.SPACE_ASCII ? 1 : 0;
    }
    double cursorX = centered ? (constraints().width().rawValue() - lineLength) / 2 : 0.0;
    double yLineOffset = LINE_HEIGHT * (rawHeight / 5f);
    float[] positions = new float[characterCount * 12];
    float[] textureCoordinates = new float[characterCount * 12];
    int index = 0;
    for (char asciiCharacter : text.toCharArray()) {
      ResourceFont.Character character = font.getCharacter(asciiCharacter);
      if (asciiCharacter != ResourceFont.SPACE_ASCII) {
        addVerticesFor(positions, textureCoordinates, character, index, verticalPerPixelSize, horizontalPerPixelSize, cursorX, yLineOffset);
        index++;
      }
      cursorX += (character.xAdvance() * horizontalPerPixelSize) * rawHeight;
    }
    if (positions.length / 2 == model.length()) {
      bufferSubData(positions, textureCoordinates);
    } else {
      bufferData(positions, textureCoordinates);
    }
  }

  protected void addVerticesFor(float[] positions, float[] textureCoordinates, ResourceFont.Character character, int characterIndex, double verticalPerPixelSize, double horizontalPerPixelSize, double cursorX, double cursorY) {
    float rawHeight = constraints().height().rawValue();
    double x = cursorX + ((character.xOffset() * horizontalPerPixelSize) * rawHeight);
    double y = cursorY + ((character.yOffset() * verticalPerPixelSize) * rawHeight);
    double xMax = x + ((character.xSize() * horizontalPerPixelSize) * rawHeight);
    double yMax = y + ((character.ySize() * verticalPerPixelSize) * rawHeight);
    addPoints(positions,
      characterIndex,
      x * 2 - 1,
      y * -2 + 1,
      xMax * 2 - 1,
      yMax * -2 + 1);
    addPoints(textureCoordinates,
      characterIndex,
      character.xTextureCoordinate(),
      character.yTextureCoordinate(),
      character.xMaxTextureCoordinate(),
      character.yMaxTextureCoordinate());
  }

  private void addPoints(float[] array, int characterIndex, double x, double y, double xMax, double yMax) {
    array[characterIndex * 12] = (float) x;
    array[characterIndex * 12 + 1] = (float) y;
    array[characterIndex * 12 + 2] = (float) x;
    array[characterIndex * 12 + 3] = (float) yMax;
    array[characterIndex * 12 + 4] = (float) xMax;
    array[characterIndex * 12 + 5] = (float) yMax;

    array[characterIndex * 12 + 6] = (float) x;
    array[characterIndex * 12 + 7] = (float) y;
    array[characterIndex * 12 + 8] = (float) xMax;
    array[characterIndex * 12 + 9] = (float) yMax;
    array[characterIndex * 12 + 10] = (float) xMax;
    array[characterIndex * 12 + 11] = (float) y;
  }

  private static VertexArrayObject createVertexArrayObject() {
    VertexArrayObject vertexArrayObject = new VertexArrayObject(0);
    vertexArrayObject.generateIdentifier();
    vertexArrayObject.bind();
    VertexBufferObject positionBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.DYNAMIC_DRAW);
    vertexArrayObject.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);
    VertexBufferObject textureCoordinateBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.DYNAMIC_DRAW);
    vertexArrayObject.appendVertexBufferObject(textureCoordinateBuffer, 1, 2, false, 0, 0);
    vertexArrayObject.unbind();
    return vertexArrayObject;
  }

  private void bufferData(float[] positions, float[] textureCoordinates) {
    model.bind();
    model.setLength(positions.length / 2);
    VertexBufferObject positionBuffer = model.getVertexBufferObject(0);
    positionBuffer.storeFloatData(BufferUtils.store(positions));
    VertexBufferObject textureCoordinateBuffer = model.getVertexBufferObject(1);
    textureCoordinateBuffer.storeFloatData(BufferUtils.store(textureCoordinates));
    model.unbind();
  }

  private void bufferSubData(float[] positions, float[] textureCoordinates) {
    model.bind();
    VertexBufferObject positionBuffer = model.getVertexBufferObject(0);
    positionBuffer.storeFloatSubData(BufferUtils.store(positions), 0);
    VertexBufferObject textureCoordinateBuffer = model.getVertexBufferObject(1);
    textureCoordinateBuffer.storeFloatSubData(BufferUtils.store(textureCoordinates), 0);
    model.unbind();
  }

  @Override
  public RenderType renderType() {
    return RenderType.TEXT;
  }

  @Override
  public VertexArrayObject model() {
    return model;
  }

  public ResourceFont font() {
    return font;
  }

  public String text() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
    updateModel();
  }
}
